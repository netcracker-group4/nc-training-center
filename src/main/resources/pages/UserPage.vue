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
    import UsersMangerComponent from "../components/UsersMangerComponent.vue";
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
            UsersMangerComponent,
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
                absenceReasons: [
                    ]
            }
        },
        methods: {
            getFullName(value) {
                return value.firstName + ' ' + value.lastName;
            },
            isActive() {
                axios.put('http://localhost:8080/users/update-active', {
                    active: this.user.active,
                    id: this.user.id
                })
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
                return this.canShowAttendance();
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
                    store.state.userRoles.includes("ADMIN")
            },
            canShowManagersSubordinates() {
                if (this.user.roles !== undefined)
                    return this.user.roles.includes("MANAGER") && this.viewerIsNotOnlyEmployee()
            },
            canShowCoursesAndGroups() {
                if (this.user.roles !== undefined)
                    return this.user.roles.includes("EMPLOYEE");
            },
            editItem(user) {
                this.editUser = Object.assign({}, user);
                this.dialog = true;
                let self = this;
                axios.get('http://localhost:8080/groups/get-all')
                    .then(function (response) {
                        self.groups = response.data;
                        console.log(self.groups)
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
                axios.get('http://localhost:8080/users/get-all-managers')
                    .then(function (response) {
                        self.managers = response.data;
                        console.log(self.managers)
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
            },
            close() {
                this.dialog = false;
                setTimeout(() => {
                }, 300)
            },
            save() {
                if (this.editUser.firstName != null && this.editUser.lastName != null) {
                    Object.assign(this.user, this.editUser);
                    console.log(this.editUser.firstName + " " +
                        this.editUser.lastName + " " +
                        this.editUser.dtoManager + " " +
                        this.editUser.groups + "\n" + this.user.groups);
                    axios.put('http://localhost:8080/users/update', {
                        id: this.user.id,
                        firstName: this.editUser.firstName,
                        lastName: this.editUser.lastName,
                        dtoManager: this.editUser.dtoManager
                    })
                    // .then(response => alert("User updated"))
                } else {
                    alert("Incorrect information in fields")
                }
                this.close()
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
                    });
                axios.get('http://localhost:8080/users/'+this.$route.params.id+'/getAttendanceGraph')
                    .then(function (response) {
                        self.absenceReasons = response.data;
                        console.log(response.data);
                    }).catch(function (error) {
                    console.log(error);
                });
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