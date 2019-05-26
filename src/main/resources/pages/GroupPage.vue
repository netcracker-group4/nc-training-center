<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-container>
        <progress-circular-component v-if="loading"></progress-circular-component>

        <v-layout v-if="!loading" row wrap style="margin-bottom: 50px">
            <v-flex md6 lg4 sx12>
                <span class="grey--text" style="font-size: 22px; margin-top: 15px">Group :  </span>
                <span style="font-size: 22px; margin-top: 15px"
                      class="font-weight-medium">{{ group.title}}</span>
            </v-flex>
            <v-flex md6 lg3 sx12>
                <v-btn large flat v-on:click="downloadGroupAttendanceReport()" v-if="hasRights()">
                    <b>Download group attendance</b></v-btn>
            </v-flex>
            <v-flex md6 lg3 sx12>
                <v-btn large flat v-on:click="$router.push('/courses/' + course.id)">
                    <b>Course: {{course.name}}</b></v-btn>
            </v-flex>
            <v-flex md6 lg2 sx12>
                <v-btn large flat v-on:click="forwardToUserPage(teacher.id)">
                    <b>Trainer: {{teacher.firstName + ' ' + teacher.lastName}}</b></v-btn>
            </v-flex>

            <v-flex xs12 sm12 style="margin-bottom: 50px">
                <v-data-table
                        :headers="headers"
                        :items="students"
                        :expand="true"
                        item-key="id"
                >
                    <template v-slot:items="props">
                        <tr>
                            <td class="text-xs-right" @click="forwardToUserPage(props.item.id)">
                                <div>
                                    {{ props.item.id }}
                                </div>
                            </td>
                            <td class="text-xs-left clickable" @click="forwardToUserPage(props.item.id)">
                                {{props.item.firstName +' '+ props.item.lastName }}
                            </td>
                            <td class="text-xs-left clickable" @click="forwardToUserPage(props.item.id)">
                                {{props.item.email}}
                            </td>
                            <td class="text-xs-right" v-if="isAdmin">
                                <v-btn color="error" @click="deleteStudent(props.item.id)" v-if="isAdmin">Remove</v-btn>
                            </td>
                        </tr>
                    </template>
                </v-data-table>
            </v-flex>
            <v-flex xs12 sm12 style="margin-bottom: 50px">
                <group-schedule-component :course-trainer-id="teacher.id"
                                          :is-student-of-group="isStudentOfGroup()"></group-schedule-component>
            </v-flex>

            <v-flex style="margin-bottom: 50px" xs12 sm12 v-if="hasRights()">
                <group-attendance-graph :absenceReasons="reasons"/>
            </v-flex>
        </v-layout>
        <v-layout v-if="!loading" row wrap style="margin-bottom: 40px">
            <v-spacer></v-spacer>
            <div class="text-xs-center">
                <v-dialog v-model="sendMessageWindowShow" width="500">
                    <template v-slot:activator="{ on }">
                        <v-btn v-if="isBtnShow"
                               color="success" large @click="sendMessageWindowShow = ! sendMessageWindowShow">Message
                        </v-btn>
                    </template>

                    <v-card>
                        <v-card-title class="headline grey lighten-2" primary-title>Send message to</v-card-title>
                        <v-divider></v-divider>
                        <v-layout row wrap>
                            <v-flex xs10 offset-xs1 class="message-textarea">
                                <v-textarea
                                        v-model="message"
                                        solo
                                        name="input-7-4"
                                        label="Type message"
                                ></v-textarea>
                            </v-flex>
                        </v-layout>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" flat @click="sendMessageWindowShow = false">Cancel</v-btn>
                            <v-btn color="primary" flat @click="sendMessage">Send</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </div>
        </v-layout>
    </v-container>
</template>

<script>
    import axios from 'axios';
    import store from '../store/store.js';
    import GroupAttendanceGraph from "../components/GroupAttendanceGraph.vue";
    import GroupScheduleComponent from "../components/GroupScheduleComponent.vue";
    import ProgressCircularComponent from "../components/ProgressCircularComponent.vue";

    export default {
        props: ['id'],
        name: "GroupPage",
        components: {GroupAttendanceGraph, GroupScheduleComponent, ProgressCircularComponent},
        data: function () {
            return {
                message: '',
                self: this,
                sendMessageWindowShow: false,
                group: {},
                students: [],
                teacher: [],
                course: [],
                lessons: [],
                reasons: [],
                loading: true,
                headers2: [
                    {
                        text: 'Lesson topic',
                        align: 'left',
                        value: 'topic'
                    },
                    {
                        text: 'Date', value: 'timeDate.toString()', align: 'left'
                    },
                    {
                        text: 'Status', value: 'isCanceled', align: 'left'
                    }

                ],
                isAdmin: this.$store.getters.isAdmin,
            }
        },
        methods: {
            isBtnShow(){
              return   self.group.trainerId == self.$store.state.user.id
            },
            chatsReload() {
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/chats')
                    .then(response => {
                        self.$store.state.chats = response.data
                    })
            },
            sendMessage() {
                let self = this;
                let form = new FormData();
                let request = new XMLHttpRequest();
                request.open('POST', this.$store.state.apiServer + '/api/messages');
                form.append('text', this.message);
                form.append('senderId', this.$store.state.user.id);
                form.append('groupId', this.group.id);
                request.send(form);
                this.message = '';
                request.onloadend = function () {
                    self.chatsReload()
                };
                this.sendMessageWindowShow = false

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
            deleteStudent(id) {
                let self = this;
                if (confirm("Are you sure you want to delete " +
                    this.findUserById(id).firstName + ' ' + this.findUserById(id).lastName)) {
                    axios.delete(this.$store.state.apiServer + '/api/groups/' + this.group.id + '/user/' + id)
                        .then(function (response) {
                            self.successAutoClosable('Employee has been removed from group');
                            self.students = self.students.filter(function (e) {
                                return e.id !== id;
                            })
                        })
                        .catch(function (error) {
                            self.errorAutoClosable('Error occured')
                        });
                }
            },
            hasRights() {
                return store.getters.user.id == this.teacher.id || store.getters.isAdmin;
            },
            findUserById(id) {
                return this.students.find(s => s.id == id);
            },
            forwardToUserPage(id) {
                this.$router.push('/users/' + id)
            },
            isStudentOfGroup() {
                let self = this;
                return this.students.filter(e => e.id === self.$store.state.user.id).length > 0;
            },
            downloadGroupAttendanceReport() {
                window.open(this.$store.state.apiServer + "/attendance-report/" + this.id, "_blank");
            },

        },
        mounted() {
           // alert(self.group.trainerId == self.$store.state.user.id)
            let self = this;
            axios.get(this.$store.state.apiServer + '/api/groups/' + self.$route.params.id)
                .then(function (response) {
                    self.group = response.data;
                    self.loading = false;
                })
                .catch(function (error) {
                    console.log(error);
                    if (error.response != null && error.response.status == 400)
                        self.$router.push('/404');
                });
            axios.get(this.$store.state.apiServer + '/api/groups/' + self.$route.params.id + '/course')
                .then(function (response) {
                    self.course = response.data;
                    axios.get(self.$store.state.apiServer + '/api/groups/' + self.$route.params.id + '/users')
                        .then(function (response) {
                            self.students = response.data;
                            axios.get(self.$store.state.apiServer + '/api/users/' + self.course.userId)
                                .then(function (response) {
                                    self.teacher = response.data;
                                    console.log(self.teacher);
                                    if (self.$store.state.userRoles.includes('ADMIN') ||
                                        self.$store.state.userRoles.includes('TRAINER') ||
                                        self.isStudentOfGroup()) {
                                    } else {
                                        self.$router.push('/403');
                                    }
                                })
                                .catch(function (error) {
                                    if (error.response != null && error.response.status == 400)
                                        self.$router.push('/404');
                                });
                        })
                        .catch(function (error) {
                            console.log(error);
                            if (error.response != null && error.response.status == 400)
                                self.$router.push('/404');
                        });

                })
                .catch(function (error) {
                    console.log(error);
                    if (error.response != null && error.response.status == 400)
                        self.$router.push('/404');
                });


            axios.get(this.$store.state.apiServer + '/api/groups/' + self.$route.params.id + '/getAttendanceGraph')
                .then(function (response) {
                    self.reasons = response.data;
                    console.log(response.data);
                })
                .catch(function (error) {
                    console.log(error);
                    if (error.response != null && error.response.status == 400)
                        self.$router.push('/404');
                });
        },
        computed: {
            headers() {
                let h = [
                    {
                        text: 'Student Id',
                        align: 'left',
                        value: 'id'
                    },
                    {
                        text: 'Student name', value: 'firstName' + 'lastName',
                        align: 'left'
                    },
                    {
                        text: '@email', value: 'email',
                        width: "40", align: 'right'
                    }
                ];
                if (this.isAdmin) {
                    h.push({
                        text: 'action', value: 'action',
                        width: "30", align: 'right'
                    });
                }
                return h;
            }
        }

    }
</script>

<style scoped>
    .clickable {
        cursor: pointer;
        margin-bottom: 5px;
        margin-top: 5px;
        color: black;
    }
</style>