<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-container>
        <progress-circular-component v-if="loading"></progress-circular-component>

        <v-layout v-if="!loading" row wrap>
            <v-flex xs12>
                <template>
                    <v-card>
                        <v-card-text>
                            Topic: {{topic}}
                        </v-card-text>
                        <v-card-text @click="goToTrainerPage()">
                            Trainer: {{firstName}} {{lastName}}
                        </v-card-text>
                        <v-card-text>
                            Date: {{date}}
                        </v-card-text>
                        <v-card-text>
                            Duration: {{duration}}
                        </v-card-text>
                        <v-card-text>
                            Status: {{status}}
                        </v-card-text>
                    </v-card>
                </template>
            </v-flex>
        </v-layout>
        <v-layout v-if="!loading" row wrap>
            <v-flex xs12>
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
                                <v-btn v-if="isLessonTrainer()" color="error" @click="unlink(props.item.id)">Delete</v-btn>
                            </td>
                        </tr>
                    </template>
                </v-data-table>
            </v-flex>
        </v-layout>
        <lesson-attendance-table v-if="!loading"
                                 :lessonId="this.$route.params.id"
                                 :key="this.$route.params.id"/>
    </v-container>

</template>

<script>

    import axios from 'axios';
    import LessonAttendanceTable from "../components/LessonAttendanceTable.vue";
    import ProgressCircularComponent from "../components/ProgressCircularComponent.vue";

    export default {
        name: "LessonPage",
        components: {LessonAttendanceTable, ProgressCircularComponent},
        data: function () {
            return {
                topic: null,
                trainer: [],
                firstName: null,
                lastName: null,
                date: null,
                duration: null,
                status: "Active",
                attachments: [],
                headers: [
                    {
                        text: 'Name',
                        align: 'left',
                        value: 'title'
                    },
                ],
                loading: true
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
            goToTrainerPage() {
                if (this.trainer !== null) {
                    this.$router.push('/users/' + this.trainer.id);
                }
                window.scrollTo(0, 0);
            },
            downloadFile(fileId){
                window.open(this.$store.state.apiServer + '/api/attachments/download/'+fileId);
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
                        console.log(dat);
                        self.topic = dat.topic;
                        self.trainer = dat.trainerId;
                        self.loading = false;
                        axios.get(self.$store.state.apiServer + '/api/users/' + self.trainer)
                            .then(function (response) {
                                self.trainer = response.data;
                                self.firstName = self.trainer.firstName;
                                self.lastName = self.trainer.lastName;
                            })
                            .catch(function (error) {
                                console.log(error);
                            });
                        self.date = dat.time;
                        self.duration = dat.duration;
                        if (dat.isCanceled == true) {
                            self.status = "Canceled";
                        }

                    })
                    .catch(function (error) {
                        console.log(error);
                    });
                axios.get(this.$store.state.apiServer + '/api/attachments/lesson/' + this.$route.params.id)
                    .then(function (response) {
                        self.attachments = response.data;
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
            },
            isLessonTrainer() {
                return this.$store.state.user.id == this.trainer.id;
            },
            downloadFile(id) {
                window.open(this.$store.state.apiServer + '/api/attachments/download/' + id);
            },
            unlink(idFile){

                let self = this;
                axios.delete(this.$store.state.apiServer +'/api/attachments/unlink',{data:{lessonId: this.$route.params.id ,
                 attachmentId: idFile}})
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


            }

        }
    }

</script>