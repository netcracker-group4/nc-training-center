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
            <div>
                <v-expansion-panel class="margin" expand>
                    <v-expansion-panel-content>
                        <!--suppress HtmlUnknownBooleanAttribute -->
                        <template v-slot:header>
                            <div>Employee's attendance</div>
                        </template>
                        <div class="attendance" v-for="group in user.groups">
                            <v-card>
                                <v-card-title>
                                    Attendance of user {{user.firstName + ' ' + user.lastName}} in group {{group.title}}
                                </v-card-title>
                                <v-container>
                                    <v-layout row wrap>
                                        <v-flex xs12 sm12>
                                            <v-toolbar flat color="white">
                                                <v-toolbar-title>User</v-toolbar-title>
                                                <v-divider
                                                        class="mx-2"
                                                        inset
                                                        vertical
                                                ></v-divider>
                                                <v-spacer></v-spacer>

                                                <v-dialog v-model="dialog" max-width="500px">
                                                    <template v-slot:activator="{ on }">
                                                        <v-icon
                                                                class="mr-4"
                                                                @click="editItem(user)"
                                                        >
                                                            edit
                                                        </v-icon>
                                                    </template>
                                                    <v-card>
                                                        <v-card-title>
                                                            <span class="headline">Edit user</span>
                                                        </v-card-title>

                                                        <v-card-text>
                                                            <v-container grid-list-md>
                                                                <v-layout wrap>
                                                                    <v-flex xs5>
                                                                        <v-text-field v-model="editUser.firstName"
                                                                                      label="Name"></v-text-field>
                                                                    </v-flex>
                                                                    <v-flex xs5>
                                                                        <v-text-field v-model="editUser.lastName"
                                                                                      label="Surname"></v-text-field>
                                                                    </v-flex>
                                                                    <v-flex xs10 v-if="editUser.dtoManager != null">
                                                                        <v-select
                                                                                v-model="editUser.dtoManager"
                                                                                :items="managers"
                                                                                label="Change manager"
                                                                        >
                                                                            <template slot="selection"
                                                                                      slot-scope="managers">
                                                                                {{ managers.item.firstName }} {{
                                                                                managers.item.lastName }}
                                                                            </template>
                                                                            <template slot="item" slot-scope="managers">
                                                                                {{ managers.item.firstName }} {{
                                                                                managers.item.lastName }}
                                                                            </template>
                                                                        </v-select>
                                                                    </v-flex>
                                                                    <v-flex xs10>
                                                                        <v-select
                                                                                v-model="editUser.groups"
                                                                                item-value="id"
                                                                                :items="groups"
                                                                                item-text="title"
                                                                                :menu-props="{ maxHeight: '300' }"
                                                                                label="Change groups"
                                                                                multiple
                                                                        ></v-select>
                                                                    </v-flex>
                                                                </v-layout>
                                                            </v-container>
                                                        </v-card-text>

                                                        <v-card-actions>
                                                            <v-spacer></v-spacer>
                                                            <v-btn color="blue darken-1" flat @click="close">Cancel
                                                            </v-btn>
                                                            <v-btn color="blue darken-1" flat @click="save">Save</v-btn>
                                                        </v-card-actions>
                                                    </v-card>
                                                </v-dialog>
                                            </v-toolbar>
                                            <v-container class="con_wrapper">

                                                <div class="div_avatar">
                                                    <v-avatar
                                                            size="180"
                                                            class="avatar"
                                                    >
                                                        <img v-if="user.image != null"
                                                             src="https://99px.ru/sstorage/53/2017/03/tmb_193520_3284.jpg"
                                                             alt="avatar" class="avatar_img">
                                                        <v-icon v-if="user.image == null" alt="avatar"
                                                                class="avatar_img avatar_icon">account_circle
                                                        </v-icon>
                                                    </v-avatar>
                                                    <v-container fluid style="display:block;">
                                                        <v-switch v-model="user.active" @change="isActive"
                                                                  label="Active"></v-switch>
                                                    </v-container>
                                                </div>
                                                <div class="div_table">
                                                    <table class="table_user">
                                                        <tr>
                                                            <td>Name</td>
                                                            <td>{{ user.firstName }}</td>
                                                        </tr>
                                                        <tr>
                                                            <td>Surname</td>
                                                            <td>{{ user.lastName }}</td>
                                                        </tr>
                                                        <tr>
                                                            <td>Email</td>
                                                            <td>{{ user.email }}</td>
                                                        </tr>
                                                        <tr>
                                                            <td>Role</td>
                                                            <td><span v-for="role in user.roles">{{ role + ' ' }}</span>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>Teachers</td>
                                                            <td><p v-for="teacher in user.dtoTeachers"
                                                                   @click="forwardToUserPage(teacher.id)"
                                                                   class="p cursor">{{ getFullName(teacher) }}</p></td>
                                                        </tr>
                                                        <tr>
                                                            <td>Groups</td>
                                                            <td><p v-for="group in user.groups" class="p">{{ group.title
                                                                }}</p></td>
                                                        </tr>
                                                        <tr>
                                                            <td>Manager</td>
                                                            <td><span @click="forwardToUserPage(user.dtoManager.id)"
                                                                      class="p cursor" v-if="user.dtoManager">{{ user.dtoManager.firstName + ' ' + user.dtoManager.lastName}}</span>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </v-container>
                                        </v-flex>
                                    </v-layout>

                                    <div>
                                        <v-expansion-panel class="margin">
                                            <v-expansion-panel-content>
                                                <!--suppress HtmlUnknownBooleanAttribute -->
                                                <template v-slot:header>
                                                    <div>Employee's attendance</div>
                                                </template>
                                                <div class="attendance" v-for="group in user.groups">
                                                    <v-card flat>
                                                        <v-card-title>
                                                            Attendance of user {{user.firstName + ' ' + user.lastName}}
                                                            in group {{group.title}}
                                                        </v-card-title>

                                                        <attendance-table :user-id="user.id"
                                                                          :group-id="group.id"
                                                                          :key="group.id"/>
                                                    </v-card>
                                                </div>
                                            </v-expansion-panel-content>
                                        </v-expansion-panel>
                                    </div>
                                    <attendance-table :user-id="user.id"
                                                      :group-id="group.id"
                                                      :key="group.id"/>
                            </v-card>
                        </div>
                    </v-expansion-panel-content>
                </v-expansion-panel>
            </div>
            <div>
                <v-expansion-panel class="margin">
                    <v-expansion-panel-content>
                        <template v-slot:header>
                            <div>Feedback</div>
                        </template>
                        <v-expansion-panel popout>
                            <v-expansion-panel-content
                                    v-for="(feedback, i) in user.dtoFeedbacks"
                                    :key="i"
                                    hide-actions
                            >
                                <template v-slot:header>
                                    <v-layout
                                            align-center
                                            row
                                            spacer
                                    >
                                        <v-flex xs4 sm2 md1>
                                            <v-avatar
                                                    size="36px"
                                            >
                                                <img
                                                        src="https://avatars0.githubusercontent.com/u/9064066?v=4&s=460"
                                                        alt="Avatar"
                                                >
                                            </v-avatar>
                                        </v-flex>

                                        <v-flex sm5 md3 hidden-xs-only>
                                            <strong v-html="feedback.teacher.firstName"></strong>
                                            <!--                                            <span-->
                                            <!--                                                    v-if="message.total"-->
                                            <!--                                                    class="grey&#45;&#45;text"-->
                                            <!--                                            >-->
                                            <!--                                              &nbsp;({{ message.total }})-->
                                            <!--                                            </span>-->
                                        </v-flex>

                                        <v-flex
                                                class="grey--text"
                                                ellipsis
                                                hidden-sm-and-down
                                        >
                                            &mdash;
                                            {{ feedback.text }}
                                        </v-flex>
                                    </v-layout>
                                </template>

                                <v-card>
                                    <v-divider></v-divider>
                                    <v-card-text v-text="lorem"></v-card-text>
                                </v-card>
                            </v-expansion-panel-content>
                        </v-expansion-panel>
                    </v-expansion-panel-content>
                </v-expansion-panel>
            </div>

            <calendar-list-schedule-component class="margin" :groups-list="user.groups"
                                              :lessons-list="lessons"></calendar-list-schedule-component>

            <users-groups-and-courses class="margin" :groups="user.groups"
                                      :trainers="user.dtoTeachers"></users-groups-and-courses>

        </v-container>
    </div>
</template>

<script>
    import axios from 'axios'
    import AttendanceTable from "../components/AttendanceTable.vue";
    import CalendarListScheduleComponent from "../components/CalendarListScheduleComponent.vue";

    export default {
        components: {
            UsersMangerComponent,
            BasicUserInfoComponent,
            AttendanceTable,
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

    .attendance {
        margin-bottom: 20px;
        margin-top: 20px;
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