<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-container>
            <v-layout row wrap>
                <v-flex md6 xs12>
                    <basic-user-info-component :user="user" :elem-name="'Employee'"/>
                </v-flex>
                <v-flex md6 xs12>
                    <users-manger-component :user="user" :elem-name="'Manager'"
                                            :if-null-message="'Employee has mo manager'"/>
                </v-flex>
            </v-layout>

            <users-attendance class="margin"  :user="user"/>

            <feedback-component class="margin" :lorem="lorem" :user="user"/>

            <calendar-list-schedule-component class="margin" :groups-list="user.groups"
                                              :lessons-list="lessons"></calendar-list-schedule-component>

            <users-groups-and-courses class="margin" :groups="user.groups"
                                      :trainers="user.dtoTeachers"></users-groups-and-courses>

        </v-container>
    </div>
</template>

<script>
    import axios from 'axios'
    import CalendarListScheduleComponent from "../components/CalendarListScheduleComponent.vue";
    import FeedbackComponent from "./FeedbackComponent.vue";
    import UsersMangerComponent from "../components/UsersMangerComponent.vue";
    import BasicUserInfoComponent from "../components/BasicUserInfoComponent.vue";
    import UsersGroupsAndCourses from "../components/UsersGroupsAndCourses.vue";
    import UsersAttendance from "./UsersAttendance.vue";

    export default {
        components: {
            UsersAttendance,
            FeedbackComponent,
            UsersMangerComponent,
            BasicUserInfoComponent,
            CalendarListScheduleComponent,
            UsersGroupsAndCourses
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

                lorem: 'Lorem ipsum dolor sit amet, at aliquam vivendum vel, everti delicatissimi cu eos. Dico iuvaret debitis mel an, et cum zril menandri. Eum in consul legimus accusam. Ea dico abhorreant duo, quo illum minimum incorrupte no, nostro voluptaria sea eu. Suas eligendi ius at, at nemore equidem est. Sed in error hendrerit, in consul constituam cum.'
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
            }
        },
        mounted() {
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
        },
        watch: {
            '$route'(to, from) {
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
            }
        }
    }
</script>

<style scoped>
    .con_wrapper {
        background: #eeecec;
        display: flex;

    }

    .div_avatar {
        padding: 30px 0 0 5%;
        width: 30%;
    }

    .div_table {
        width: 70%;
    }

    .avatar {
        margin: 0 0 0 0;
    }

    .avatar_img {
        /*max-width: 180px;*/
        /*max-height: 180px;*/
    }

    .table_user {
        margin-right: 0px;
        float: right;
        width: 100%;
        font-size: 14px;
        background: white;
        text-align: left;
        border-collapse: collapse;
        color: #3E4347;
        box-sizing: border-box;
    }

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

    .avatar_icon {
        font-size: 150px;
    }

    .cursor {
        cursor: pointer;
    }
</style>