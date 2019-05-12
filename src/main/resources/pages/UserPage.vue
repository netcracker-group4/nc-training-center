<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-container>

            <basic-user-info-component :user="user" :elem-name="userComponentHeader"/>
            <user-attendance-progress v-if="canShowAttendance()" :absenceReasons="absenceReasons"/>
            <users-attendance v-if="canShowAttendance()" class="margin" :user="user"/>

            <feedback-component v-if="canShowFeedbacks()" class="margin" :user="user"/>

            <calendar-list-schedule-component v-if="canShowSchedule()"
                                              class="margin" :groups-list="user.groups"
                                              :lessons-list="lessons"
                                              :previously-selected="getArray(user.groups)"
                                              :component-header="getScheduleComponentHeader()"
            ></calendar-list-schedule-component>

            <users-groups-and-courses v-if="canShowCoursesAndGroups()" class="margin" :groups="user.groups"
                                      :trainers="user.dtoTeachers"></users-groups-and-courses>

            <users-courses v-if="canShowCourses()" class="margin" :groups="user.groups"
                           :trainers="user.dtoTeachers"></users-courses>

            <subordinates-component v-if="canShowManagersSubordinates()"></subordinates-component>
        </v-container>
    </div>
</template>

<script>
    import axios from 'axios'
    import store from '../store/store.js';
    import CalendarListScheduleComponent from "../components/CalendarListScheduleComponent.vue";
    import FeedbackComponent from "../components/FeedbackComponent.vue";
    import BasicUserInfoComponent from "../components/BasicUserInfoComponent.vue";
    import UsersGroupsAndCourses from "../components/UsersGroupsAndCourses.vue";
    import UsersAttendance from "./UsersAttendance.vue";
    import UsersCourses from "../components/UsersCourses.vue";
    import SubordinatesComponent from "../components/SubordinatesComponent.vue";
    import UserAttendanceProgress from "../components/UserAttendanceProgress.vue";
    export default {
        components: {
            UsersAttendance,
            FeedbackComponent,
            BasicUserInfoComponent,
            CalendarListScheduleComponent,
            UsersGroupsAndCourses,
            UsersCourses,
            SubordinatesComponent,
            UserAttendanceProgress
        },
        data: function () {
            return {
                dialog: false,
                user: '',
                right: null,
                prevId: null,
                editUser: {
                    id: 0,
                    firstName: '',
                    lastName: '',
                    email: '',
                    image: '',
                    roles: [],
                    dtoManager: '',
                    dtoTeachers: [],
                    groups: [],
                    active: false,
                },
                groups: [],
                managers: [],
                lessons: [],
                absenceReasons: []
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
            getFullName(value) {
                return value.firstName + ' ' + value.lastName;
            },
            hasFullPrivilege(){
                return store.getters.isAdmin() || store.state.user.id === this.$route.params.id;
            },
            getScheduleComponentHeader() {
                if (this.user.roles !== undefined && this.user.roles.includes('EMPLOYEE')) {
                    return 'Employee\'s schedule';
                }
                if (this.user.roles !== undefined && this.user.roles.includes('TRAINER')) {
                    return 'Trainer\'s schedule';
                }
            },
            canShowAttendance() {
                if (this.user.roles !== undefined)
                    return this.user.roles.includes('EMPLOYEE') && this.viewerIsNotOnlyEmployee()
            },
            canShowFeedbacks() {
                return (this.user.roles !== undefined) &&
                    (store.state.userRoles.includes("ADMIN")) ||
                    (store.state.userRoles.includes("MANAGER") && store.state.user.id === this.user.dtoManager.id) ||
                    (store.state.userRoles.includes("TEACHER")) ||
                    (store.state.userRoles.includes("EMPLOYEE") && store.state.user.id === this.user.id);
            },
            canShowSchedule() {
                return (this.user.roles !== undefined &&
                    (this.user.roles.includes("TRAINER") || this.user.roles.includes("EMPLOYEE")))
                    && this.viewerIsNotOnlyEmployee()
            },
            canShowCourses() {
                if (this.user.roles !== undefined)
                    return this.user.roles.includes("TRAINER");
            },
            viewerIsNotOnlyEmployee() {
                return store.state.userRoles.includes("MANAGER") ||
                    store.state.userRoles.includes("TRAINER") ||
                    this.hasFullPrivilege()
            },
            canShowManagersSubordinates() {
                if (this.user.roles !== undefined)
                    return this.user.roles.includes("MANAGER") && this.viewerIsNotOnlyEmployee()
            },
            canShowCoursesAndGroups() {
                if (this.user.roles !== undefined)
                    return this.user.roles.includes("EMPLOYEE");
            },
            close() {
                this.dialog = false;
                setTimeout(() => {
                }, 300)
            },
            getArray(groupsList) {
                let selectedCourses = [];
                groupsList.forEach(function (value) {
                    selectedCourses.push(value.id)
                });
                return selectedCourses;
            },
            loadInfo() {
                let self = this;
                let id = this.$route.params.id;
                axios.get('http://localhost:8080/users/' + id)
                    .then(function (response) {
                        self.user = response.data;
                        console.log(self.user)
                    })
                    .catch(function (error) {
                        console.log(error);
                        self.errorAutoClosable(error.response.data);
                    });
                axios.get('http://localhost:8080/schedule/employee/' + id)
                    .then(function (response) {
                        console.log(response.data);
                        self.lessons = response.data;
                        self.lessons.forEach(function (one) {
                            one.open = false;
                        })
                    })
                    .catch(function (error) {
                        console.log(error);
                        self.errorAutoClosable(error.response.data);
                    });
                axios.get('http://localhost:8080/users/'+this.$route.params.id+'/getAttendanceGraph')
                    .then(function (response) {
                        self.absenceReasons = response.data;
                        console.log(response.data);
                    }).catch(function (error) {
                    console.log(error);
                    self.errorAutoClosable(error.response.data);
                });
            }
        }
        ,
        mounted() {
            this.loadInfo();
        }
        ,
        watch: {
            '$route'(to, from) {
                this.loadInfo();
            }
        }
        ,
        computed: {
            userComponentHeader() {
                if (this.user.roles !== null && this.user.roles !== undefined && this.user.roles.length !== 0) {
                    return this.user.roles.join(', ');
                }
            }
        }
    }
</script>

<style scoped>
    .table_user td:first-child {
        background: #e6e4ee;
        /*border-bottom: 2px solid #e6e4ee;*/
        border-left: none;
    }
    .table_user td {
        border-right: 20px solid white;
        border-left: 20px solid white;
        border-bottom: 2px solid #e6e4ee;
        padding: 12px 10px;
        color: #8b8e91;
    }
    .table_user tr:last-child td {
        border-bottom: none;
    }
    .margin {
        margin-top: 30px;
        margin-bottom: 30px;
    }
</style>