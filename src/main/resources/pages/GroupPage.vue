<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-container>

        <!--        тренер урока должен видеть только свои уроки-->
        <!--        Ученики могут видеть все уроки но не видят панели редактирования-->
        <!--        Админ и супертренер видят все-->

        <v-layout row wrap>
            <v-flex xs12 sm12 style="margin-bottom: 50px">
                <v-layout>
                    <span class="grey--text" style="font-size: 22px; margin-top: 15px">Group :  </span>
                    <span style="font-size: 22px; margin-top: 15px" class="font-weight-medium">{{ group.title}}</span>
                    <v-spacer></v-spacer>
                    <v-btn large flat v-on:click="downloadGroupAttendanceReport()" v-if="hasRights()">
                        <b>Download group attendance</b></v-btn>
                    <v-btn large flat v-on:click="$router.push('/courses/' + course.id)">
                        <b>Course: {{course.name}}</b></v-btn>
                    <v-btn large flat v-on:click="forwardToUserPage(teacher.id)">
                        <b>Trainer: {{teacher.firstName + ' ' + teacher.lastName}}</b></v-btn>

                </v-layout>
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
    </v-container>
</template>

<script>
    import axios from 'axios';
    import store from '../store/store.js';
    import GroupAttendanceGraph from "../components/GroupAttendanceGraph.vue";
    import GroupScheduleComponent from "../components/GroupScheduleComponent.vue";

    export default {
        props: ['id'],
        name: "GroupPage",
        components: {GroupAttendanceGraph, GroupScheduleComponent},
        data: function () {
            return {
                group: {},
                students: [],
                teacher: [],
                course: [],
                lessons: [],
                reasons: [],
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
                if (confirm("Are you sure you want to delete " + this.findUserById(id).firstName + ' ' + this.findUserById(id).lastName)) {
                    axios.delete('/api/groups/' + this.group.id + '/user/' + id)
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
            downloadGroupAttendanceReport(){
                window.open("/download-report/attendance-report/" + this.id, "_blank");
            },

        },
        mounted() {
            let self = this;
            axios.get('/api/groups/' + self.id)
                .then(function (response) {
                    self.group = response.data;
                }).catch(function (error) {
                console.log(error);
            });
            axios.get('/api/groups/' + self.id + '/course')
                .then(function (response) {
                    self.course = response.data;
                }).catch(function (error) {
                console.log(error);
            });
            axios.get('/api/groups/' + self.id + '/trainer')
                .then(function (response) {
                    self.teacher = response.data;
                });
            axios.get('/api/groups/' + self.id + '/users')
                .then(function (response) {
                    self.students = response.data;
                    if (self.$store.state.userRoles.includes('ADMIN') ||
                        parseInt(self.$store.state.user.id) === parseInt(self.course.teacher.id) ||
                        self.isStudentOfGroup()) {
                    } else {
                        self.$router.push('/403');
                    }

                }).catch(function (error) {
                console.log(error);
            });
            axios.get('/api/groups/'+ self.id + '/getAttendanceGraph')
                .then(function (response) {
                    self.reasons = response.data;
                    console.log(response.data);
                }).catch(function (error) {
                console.log(error);
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