<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-container>
        <progress-circular-component v-if="loading"></progress-circular-component>
        <v-layout v-if="!loading" row wrap>

            <!--            toolbar-->
            <v-flex xs12 style="margin-bottom: 50px">
                <v-toolbar flat color="white">
                    <v-toolbar-title>Course : {{course.name}}</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-btn flat @click="edit()">
                        <v-icon>edit</v-icon>
                    </v-btn>
                    <v-dialog v-if="isAdmin" v-model="dialog" max-width="500px">
                        <EditCourseComponent :course="course"/>
                    </v-dialog>
                    <v-btn v-if="canJoinCourse" @click="sign()">Sign course</v-btn>
                </v-toolbar>
            </v-flex>

            <!--            status-->
            <v-flex xs12 md6 style="padding: 10px">
                <v-list>
                    <v-list-tile>
                        <v-list-tile-content>
                            <v-list-tile-title><span class="grey--text name-my">Status :</span> <span
                                    class="font-weight-medium">{{course.status}}</span></v-list-tile-title>
                        </v-list-tile-content>
                    </v-list-tile>
                    <v-list-tile>
                        <v-list-tile-content>
                            <v-list-tile-title><span class="grey--text name-my">Level:</span> <span
                                    class="font-weight-medium">{{course.level.title}}</span></v-list-tile-title>
                        </v-list-tile-content>
                    </v-list-tile>
                    <v-list-tile>
                        <v-list-tile-content>
                            <v-list-tile-title><span class="grey--text name-my">Starts on:</span> <span
                                    class="font-weight-medium">{{course.startDay}}</span></v-list-tile-title>
                        </v-list-tile-content>
                    </v-list-tile>
                    <v-list-tile>
                        <v-list-tile-content>
                            <v-list-tile-title><span class="grey--text name-my">Ends on:</span> <span
                                    class="font-weight-medium">{{course.endDay}}</span>
                            </v-list-tile-title>
                        </v-list-tile-content>
                    </v-list-tile>
                    <v-list-tile>
                        <v-list-tile-content @click="goTrainerPage(course.trainer.id)" class="clickable">
                            <v-list-tile-title><span class="grey--text name-my">Trainer:</span> <span
                                    class="font-weight-medium">{{course.trainer.firstName }} {{course.trainer.lastName}}</span>
                            </v-list-tile-title>
                        </v-list-tile-content>
                    </v-list-tile>
                </v-list>
            </v-flex>

            <!--            groups-->
            <v-flex xs12 md6 style="padding: 10px">
                <v-toolbar flat color="white">
                    <v-toolbar-title>Groups</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-btn :small="true" v-if="isAdmin" @click="manageGroups()">Manage groups</v-btn>
                </v-toolbar>
                <v-data-table
                        :headers="headers"
                        :items="groups"
                        :expand="true"
                        item-key="id"
                        hide-actions
                >
                    <template v-slot:items="props">
                        <tr>
                            <td class="my-link">
                                <div @click="">{{ props.item.id }}</div>
                            </td>
                            <td class="my-link clickable">
                                <div @click="goGroupPage(props.item.id)">{{props.item.title}}</div>
                            </td>
                        </tr>
                    </template>
                </v-data-table>

            </v-flex>

            <!--            image-->
        </v-layout>
        <v-layout v-if="!loading" row wrap>
            <v-flex xs12 md6 style="padding: 10px">
                <v-img v-if="course.imageUrl"
                       :src="this.$store.state.apiServer+ '/api/files-img?url=' + course.imageUrl"
                       aspect-ratio="2"></v-img>
                <v-progress-linear v-if="!course.imageUrl" :indeterminate="true"></v-progress-linear>
                <div v-if="isAdmin">
                    <v-text-field label="Select Image"
                                  @click='pickFile'
                                  v-model='course.imageUrl'
                                  prepend-icon='attach_file'></v-text-field>
                    <form id="uploadForm" name="uploadForm" enctype="multipart/form-data" style="height: 0">
                        <input
                                type="file"
                                style="display: none"
                                ref="image"
                                accept="image/*"
                                @change="onFilePicked"
                                id="img"
                        ></form>
                </div>
            </v-flex>

            <!--            description-->
            <v-flex xs12 md6 style="padding: 10px">
                    <v-textarea
                            id="course-descr-text"
                            name="input-7-1"
                            label="Description"
                            :value="course.description"
                            :readonly="true"
                            box
                            auto-grow
                    ></v-textarea>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    import axios from 'axios'
    import CourseDate from "../components/CourseDate.vue";
    import EditCourseComponent from "../components/EditCourseComponent.vue";
    import ProgressCircularComponent from "../components/ProgressCircularComponent.vue";

    export default {
        name: "CoursePage",
        components: {EditCourseComponent, CourseDate, ProgressCircularComponent},
        data() {
            return {
                course:{
                    id: this.$route.params.id,
                    name: null,
                    level: {title: ''},
                    status: null,
                    imageUrl: null,
                    isOnLandingPage: null,
                    description: null,
                    startDay: null,
                    endDay: null,
                    trainer: {firstName: '', lastName: ''},
                },
                canJoinCourse: false,
                levelId: null,
                courseStatusId: null,
                img: null,
                groups: [],
                isAdmin: this.$store.getters.isAdmin,
                isNotAdmin: this.isAdmin === "false",
                dialog: false,
                headers: [
                    {
                        text: 'Group id',
                        align: 'left',
                        value: 'id'
                    },
                    {
                        text: 'Group title', value: 'title'
                    }
                ],
                loading: true
            }
        },
        methods: {
            setCourse() {
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/getcourses/' + self.course.id)
                    .then(function (response) {
                        let dat = response.data;
                        self.course.name = dat.name;
                        self.levelId = dat.level;
                        self.setLevel(dat.level);
                        self.getStatus(dat.courseStatusId);
                        self.course.isOnLandingPage = dat.isOnLandingPage;
                        self.course.imageUrl = dat.imageUrl;
                        self.course.description = dat.description;
                        self.course.startDay = new Date(dat.startDate).toISOString().substr(0, 10);
                        self.course.endDay = new Date(dat.endDate).toISOString().substr(0, 10);
                        self.getTrainer();
                        self.getGroups();
                        self.loading = false;
                    })
                    .catch(function (error) {
                        console.log(error);
                        if (error.response != null && (error.response.status == 400 || error.response.status == 404))
                            self.$router.push('/404');
                    });
            },
            getStatus(id) {
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/getInfo/getStatus/' + id)
                    .then(function (response) {
                        self.course.status = response.data;
                    })
                    .catch(function (error) {
                        console.log(error);
                        if (error.response != null && error.response.status === 400)
                            self.$router.push('/404');
                    });
            },
            getTrainer() {
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/getcourses/' + this.course.id + '/trainer')
                    .then(function (response) {
                        self.course.trainer = response.data[0];
                    })
                    .catch(function (error) {
                        if (error.response != null && error.response.status === 400)
                            self.$router.push('/404');
                    });
            },
            goTrainerPage(id) {
                this.$router.push('/users/' + id);
            },
            goGroupPage(id) {
                this.$router.push('/groups/' + id);
            },
            sign() {
                this.$router.push('/courses/' + this.course.id + '/join');
            },
            getGroups() {
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/groups/get-groups/' + self.course.id)
                    .then(function (response) {
                        self.groups = response.data;
                    })
                    .catch(function (error) {
                        if (error.response != null && error.response.status === 400)
                            self.$router.push('/404');
                    });
            },
            edit() {
                this.dialog = true;
            },
            setLevel(levelId) {
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/getInfo/getLevel/' + levelId)
                    .then(function (response) {
                        self.course.level = response.data;
                    })
                    .catch(function (error) {
                        console.log(error);
                        if (error.response != null && error.response.status === 400)
                            self.$router.push('/404');
                    });
            },
            pickFile() {
                this.$refs.image.click()
            },
            onFilePicked(e) {
                const files = e.target.files;
                if (files[0] !== undefined) {
                    const fr = new FileReader();
                    fr.readAsDataURL(files[0]);
                    fr.addEventListener('load', () => {
                        this.course.imageUrl = fr.result;
                        this.img = files[0]
                    });
                    let form = new FormData();
                    form.append('img', files[0]);
                    axios.put(this.$store.state.apiServer + '/api/getcourses/' + this.$route.params.id + '/upload-img', form, {
                        headers: {
                            'Content-Type': 'multipart/form-data'
                        }
                    })
                        .then(function (response) {
                            console.log(response);
                        })
                        .catch(function (error) {
                            console.log(error);
                        });
                } else {
                    this.img = '';
                    this.course.imageUrl = '';
                }
            },
            manageGroups() {
                this.$router.push('/courses/' + this.$route.params.id + '/desired-schedule');
            },
        },
        mounted() {
            let self = this;
            axios.get(this.$store.state.apiServer + '/api/getcourses/' + this.$route.params.id + '/can-join')
                .then(function (response) {
                    self.canJoinCourse = response.data;
                }).catch(function (error) {
                self.canJoinCourse = false;
            });
            self.setCourse();
        }
    }
</script>

<style scoped>
    .name-my {
        width: 21%;
        display: inline-block;
    }
</style>