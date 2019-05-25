<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-container>
        <progress-circular-component v-if="loading"></progress-circular-component>

        <v-layout v-if="!loading" row wrap mb-2>
            <v-flex xs12>
                <template>
                    <v-card>
                        <v-card-actions>
                            <v-list-tile class="grow">
                                <v-list-tile-content>
                                    <v-list-tile-title>Topic: {{lesson.topic}}</v-list-tile-title>
                                </v-list-tile-content>
                                <v-layout align-center justify-end>
                                    <v-btn :disabled="lesson.isPerformed"
                                           flat
                                           color="secondary" v-on:click="cancelLesson(lesson)"
                                           v-if="userCanEdit(lesson)">
                                        <span v-if="lesson.isCanceled">Activate</span>
                                        <span v-else>cancel</span>
                                    </v-btn>
                                    <span class="mr-1">·</span>
                                    <v-btn v-if="userCanEdit(lesson)" flat v-on:click="editLesson(lesson)">
                                        <span>edit</span>
                                    </v-btn>
                                </v-layout>
                            </v-list-tile>
                        </v-card-actions>
                        <v-card-text class="cursor" @click="goToTrainerPage()">
                            Trainer: {{trainer.firstName}} {{trainer.lastName}}
                        </v-card-text>
                        <v-card-text>
                            Date: {{getDate(lesson.timeDate) + ' '+ computedTime(lesson.timeDate.substr(11, 5))}}
                        </v-card-text>
                        <v-card-text>
                            Duration: {{lesson.duration}}
                        </v-card-text>
                        <v-card-text>
                            Status:
                            <span v-if="lesson.isPerformed">Performed</span>
                            <span v-else-if="lesson.isCanceled">Canceled</span>
                            <span v-else>Active</span>
                        </v-card-text>
                    </v-card>
                    <v-spacer></v-spacer>
                </template>
            </v-flex>
        </v-layout>
        <v-layout v-if="!loading" row wrap>
            <v-btn color="success" :disabled="performed" v-if="isLessonTrainer()" @click="checked()">
                <b>Check attendance</b>
            </v-btn>
            <v-flex xs12 mt-2>
                <v-toolbar flat color="white">
                    <span>Lesson attachments</span>
                </v-toolbar>
                <v-data-table
                        :headers="headers"
                        :items="attachments"
                        :expand="true"
                        item-key="id"
                        no-data-text="No attachments available"
                >
                    <template v-slot:items="props">
                        <tr>
                            <td class="my-link">
                                <div>{{ props.item.name }}</div>
                            </td>
                            <td>
                                <v-btn color="green" @click="downloadFile(props.item.id)">Download</v-btn>
                            </td>
                            <td>
                                <v-btn v-if="isLessonTrainer()" color="error" @click="unlink(props.item.id)">Delete
                                </v-btn>
                            </td>
                        </tr>
                    </template>
                </v-data-table>
            </v-flex>
        </v-layout>
        <lesson-editing-component ref='form' style="margin-bottom: 20px" v-if="editing" @saving-event="save"
                                  @delete-event="deleteLesson"
                                  @cancel-event="cancel"
                                  :attachments="allAttachments"
                                  :currentLesson="currentEditingLesson"
                                  :trainers="allTrainers"
                                  :courseTrainerId="courseTrainerId"></lesson-editing-component>

        <lesson-attendance-table v-if="!loading"
                                 :lessonId="this.$route.params.id"
                                 :trainerId="this.trainer.id"
                                 :key="this.$route.params.id"/>
    </v-container>

</template>

<script>

    import axios from 'axios';
    import LessonAttendanceTable from "../components/LessonAttendanceTable.vue";
    import ProgressCircularComponent from "../components/ProgressCircularComponent.vue";
    import LessonEditingComponent from "../components/LessonEditingComponent.vue";

    export default {
        name: "LessonPage",
        components: {LessonAttendanceTable, ProgressCircularComponent, LessonEditingComponent},
        data: function () {
            return {
                trainer: [],
                lesson: {},
                status: "Active",
                attachments: [],
                courseTrainerId: null,
                currentEditingLesson: {},
                headers: [
                    {
                        text: 'Name',
                        align: 'left',
                        value: 'title'
                    },
                ],
                performed: true,
                loading: true,
                editing: false,
                allTrainers: [],
                allAttachments: []
            }
        },
        mounted() {
            this.loadInfo();
        },
        watch: {
            '$route'(to, from) {
                this.loadInfo();
            }
        },
        methods: {
            getDate(strDate) {
                return new Date(strDate).toDateString().slice(0, 20);
            },
            computedTime(time) {
                let [hours, minutes] = time.split(':');
                let modifier = +hours < 12 ? 'am' : 'pm';
                hours = +hours % 12 || 12;
                minutes = +minutes === 0 ? '' : `:${minutes}`;
                return hours + minutes + modifier
            },
            goToTrainerPage() {
                if (this.trainer !== null) {
                    this.$router.push('/users/' + this.trainer.id);
                }
                window.scrollTo(0, 0);
            },
            downloadFile(fileId) {
                window.open(this.$store.state.apiServer + '/api/attachments/download/' + fileId);
            },
            successAutoClosable(title) {
                this.$snotify.success(title, {
                    timeout: 2000,
                    showProgressBar: false,
                    closeOnClick: false,
                    pauseOnHover: true
                });
            },
            errorAutoClosable(title) {
                this.$snotify.error(title, {
                    timeout: 2000,
                    showProgressBar: false,
                    closeOnClick: false,
                    pauseOnHover: true
                });
            },
            loadInfo() {
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/schedule/lesson/' + this.$route.params.id)
                    .then(function (response) {
                        let dat = response.data;
                        self.lesson = response.data;
                        self.loading = false;
                        self.loadTrainerAndAttachments();
                        console.log(dat.groupId);
                        axios.get(self.$store.state.apiServer + '/api/groups/' + dat.groupId + '/course')
                            .then(function (response) {
                                console.log(response.data);
                                self.courseTrainerId = response.data.userId;
                            })
                            .catch(function (error) {
                                if (error.response != null && error.response.status == 400)
                                    self.$router.push('/404');
                                console.log(error);
                            });
                        self.date = dat.time;
                        self.duration = dat.duration;
                        if (dat.isCanceled === true) {
                            self.status = "Canceled";
                        }

                    })
                    .catch(function (error) {
                        console.log(error);
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                    });
                axios.get(this.$store.state.apiServer + '/api/users/get-all-trainers')
                    .then(function (response) {
                        self.allTrainers = response.data;
                    })
                    .catch(function (error) {
                        console.log(error);
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                        // self.errorAutoClosable(error.response.data);
                    });
                axios.get(this.$store.state.apiServer + '/api/attachments/all')
                    .then(function (response) {
                        self.allAttachments = response.data;
                    })
                    .catch(function (error) {
                        console.log(error);
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                        // self.errorAutoClosable(error.response.data);
                    });
            },
            loadTrainerAndAttachments() {
                let self = this;
                console.log('self.lesson');
                console.log(self.lesson);
                axios.get(self.$store.state.apiServer + '/api/users/' + self.lesson.trainerId)
                    .then(function (response) {
                        self.trainer = response.data;
                    })
                    .catch(function (error) {
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                        console.log(error);
                    });
                axios.get(this.$store.state.apiServer + '/api/attachments/lesson/' + this.$route.params.id)
                    .then(function (response) {
                        self.attachments = response.data;
                    })
                    .catch(function (error) {
                        console.log(error);
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                    });
            },
            isLessonTrainer() {
                return this.$store.state.user.id === parseInt(this.trainer.id);
            },
            downloadFile(id) {
                window.open(this.$store.state.apiServer + '/api/attachments/download/' + id);
            },
            unlink(idFile) {

                let self = this;
                axios.delete(this.$store.state.apiServer + '/api/attachments/unlink', {
                    data: {
                        lessonId: this.$route.params.id,
                        attachmentId: idFile
                    }
                })
                    .then(function (response) {
                        self.attachments = self.attachments.filter(function (e) {
                            return e.id !== idFile;
                        });
                        self.successAutoClosable('File has been unlinked from this lesson');
                    })
                    .catch(function (error) {
                        console.log(error);
                        self.errorAutoClosable('Error occurred');
                    });


            },
            checked() {
                axios.post(this.$store.state.apiServer + '/api/attendances/' + this.$route.params.id);
                axios.post(this.$store.state.apiServer + '/api/schedule/' + this.$route.params.id + '/performed')
                    .then(function (response) {
                        this.performed = response.data;
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
                this.$router.go(0);
            },
            userCanEdit(lesson) {
                console.log('this.$store.state.user.id == this.courseTrainerId');
                console.log(this.$store.state.user.id == this.courseTrainerId);
                return (this.$store.state.user.id == lesson.trainerId)
                    || (this.$store.state.user.roles.includes('ADMIN'))
                    || (this.$store.state.user.id == this.courseTrainerId)
            },
            editLesson(lesson) {
                this.currentEditingLesson = Object.assign({}, lesson);
                this.editing = true;
                window.scrollTo(0, document.body.scrollHeight);
            },
            cancelLesson(lesson) {
                let self = this;
                axios.post(this.$store.state.apiServer + '/api/schedule/' + lesson.id)
                    .then(function (response) {
                        lesson.isCanceled = response.data;
                        lesson.open = false;
                        if (lesson.isCanceled) self.successAutoClosable('Lesson has been canceled');
                        else self.successAutoClosable('Lesson has been activated');
                    })
                    .catch(function (error) {
                        console.log(error);
                        self.errorAutoClosable(error.response.data);
                        console.log(error.response.data);
                    });
            },
            deleteLesson(lesson) {
                let self = this;
                axios.delete(this.$store.state.apiServer + '/api/schedule/' + lesson.id)
                    .then(function (response) {
                        self.cancel();
                        self.successAutoClosable('Lesson has been archived');
                        self.$router.push('/');
                    })
                    .catch(function (error) {
                        console.log(error);
                        self.errorAutoClosable(error.response.data);
                    });
            },
            save(newLesson) {
                let self = this;
                axios.post(this.$store.state.apiServer + '/api/schedule', newLesson)
                    .then(function (response) {
                        newLesson.id = response.data;
                        newLesson.open = false;
                        self.lesson = newLesson;
                        self.loadTrainerAndAttachments();
                        self.cancel();
                        self.successAutoClosable('Lesson has been updated');
                    })
                    .catch(function (error) {
                        console.log(error);
                        self.errorAutoClosable(error.response.data);
                    });
            },
            cancel() {
                this.currentEditingLesson = null;
                this.editing = false;
            }
        }


    }

</script>