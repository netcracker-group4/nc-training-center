<template>
    <div>
        <v-container>

            <basic-user-info-component :user="user" :groups="groups" :trainers="trainers"
                                       :elem-name="userComponentHeader"/>

            <v-btn justify-start large flat @click="downloadGroupsAttendanceReport()" v-if="isTrainer() && isMyPage()">Download attendance reports</v-btn>

            <user-attendance-progress v-if="canShowAttendance()" :absenceReasons="absenceReasons"/>

            <users-attendance v-if="canShowAttendance()" class="margin" :user="user" :groups="groups"/>

            <feedback-component v-if="canShowFeedbacks()" class="margin" :user="user" :courses="courses"/>

            <calendar-list-schedule-component v-if="canShowSchedule()"
                                              class="margin" :groups-list="groups"
                                              :lessons-list="lessons"
                                              :component-header="getScheduleComponentHeader()"/>

            <users-groups-and-courses v-if="canShowCoursesAndGroups()"
                                      class="margin"
                                      :groups="groups"
                                      :trainers="trainers"/>

            <users-courses v-if="canShowCourses()"
                           class="margin"
                           :groups="groups"
                           :trainers="trainers"/>

            <subordinates-component v-if="canShowManagersSubordinates()"/>
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
                    active: false,
                },
                managers: [],
                lessons: [],
                absenceReasons: [],
                trainers: [],
                groups: [],
                courses: []
            }
        },
        methods: {
            canShowManager() {
                if (this.user.roles !== undefined)
                    return this.user.roles.includes('EMPLOYEE');
            },
            isTrainer() {
                if (this.user.roles !== undefined)
                    return this.user.roles.includes('TRAINER');
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
            getFullName(value) {
                return value.firstName + ' ' + value.lastName;
            },
            hasFullPrivilege() {
                return store.getters.isAdmin || parseInt(store.state.user.id) === parseInt(this.$route.params.id);
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
                    ((store.state.userRoles.includes("ADMIN")) ||
                        (store.state.userRoles.includes("MANAGER") && store.state.user.id === this.user.dtoManager.id) ||
                        (store.state.userRoles.includes("TRAINER") && this.courses.length > 0)) &&
                    (this.user.roles.includes("EMPLOYEE"));
            },
            canShowSchedule() {
                return (this.user.roles !== undefined &&
                    (this.user.roles.includes("TRAINER") ||
                        this.user.roles.includes("EMPLOYEE")))
                    && this.viewerIsNotOnlyEmployee()
            },
            canShowCourses() {
                if (this.user.roles !== undefined)
                    return this.user.roles.includes("TRAINER");
            },
            viewerIsNotOnlyEmployee() {
                return this.hasFullPrivilege() ||
                    this.authenticatedUserIsThisOnesManager() ||
                    store.state.userRoles.includes("TRAINER")
            },
            authenticatedUserIsThisOnesManager() {
                return store.state.userRoles.includes("MANAGER") &&
                    parseInt(store.state.user.id) === parseInt(this.user.dtoManager.id);
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
                axios.get(this.$store.state.apiServer + '/api/users/' + id)
                    .then(function (response) {
                        self.user = response.data;
                        console.log(self.user);
                        if (self.canShowManager()) {
                            self.loadAdditional();
                        }
                        if (self.canShowSchedule()) {
                            self.loadSchedule();
                        }
                        if (self.isTrainer) {
                            self.loadCourses();
                        }
                    }).catch(function (error) {
                    console.log(error);
                    self.errorAutoClosable(error.response.data);
                });
            },
            loadSchedule() {
                let self = this;
                let role;
                if(this.user.roles.includes('TRAINER')){
                    role = 'trainer/';
                    axios.get(this.$store.state.apiServer + '/api/groups/trainer/' + this.$route.params.id)
                        .then(function (response) {
                            self.groups = response.data;
                            console.log(response.data);
                        }).catch(function (error) {
                        console.log(error);
                        self.errorAutoClosable(error.response.data);
                    });
                }else role = 'employee/';
                axios.get(this.$store.state.apiServer + '/api/schedule/' + role + this.$route.params.id)
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
            },
            loadAdditional() {
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/users/' + this.$route.params.id + '/trainers')
                    .then(function (response) {
                        self.trainers = response.data;
                        console.log(response.data);
                    }).catch(function (error) {
                    console.log(error);
                    self.errorAutoClosable(error.response.data);
                });
                axios.get(this.$store.state.apiServer + '/api/users/' + this.$route.params.id + '/getAttendanceGraph')
                    .then(function (response) {
                        self.absenceReasons = response.data;
                        console.log(response.data);
                    }).catch(function (error) {
                    console.log(error);
                    self.errorAutoClosable(error.response.data);
                });
                console.log(this.$route.params.id);
                axios.get(this.$store.state.apiServer + '/api/groups/employee/' + this.$route.params.id)
                    .then(function (response) {
                        self.groups = response.data;
                        console.log(response.data);
                    }).catch(function (error) {
                    console.log(error);
                    self.errorAutoClosable(error.response.data);
                });
            },
            loadCourses() {
                let self = this;
                let id = this.$route.params.id;
                axios.get(this.$store.state.apiServer + '/api/getcourses/get-all-courses-by-trainer-and-employee?trainerId=' +
                    store.state.user.id + "&employeeId=" + id)
                    .then(function (response) {
                        self.courses = response.data;
                        console.log(self.courses)
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
            },
            isMyPage() {
                return this.$store.state.user.id.toString() === this.$route.params.id;
            },
            downloadGroupsAttendanceReport() {
                window.open(this.$store.state.apiServer + "/download-report/attendance-report", "_blank");
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