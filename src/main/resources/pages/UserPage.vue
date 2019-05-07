<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
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
                                            <v-text-field v-model="editUser.firstName" label="Name"></v-text-field>
                                        </v-flex>
                                        <v-flex xs5>
                                            <v-text-field v-model="editUser.lastName" label="Surname"></v-text-field>
                                        </v-flex>
                                        <v-flex xs10 v-if="editUser.dtoManager != null">
                                            <v-select
                                                    v-model="editUser.dtoManager"
                                                    :items="managers"
                                                    label="Change manager"
                                            >
                                                <template slot="selection" slot-scope="managers">
                                                    {{ managers.item.firstName }} {{ managers.item.lastName }}
                                                </template>
                                                <template slot="item" slot-scope="managers">
                                                    {{ managers.item.firstName }} {{ managers.item.lastName }}
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
                                <v-btn color="blue darken-1" flat @click="close">Cancel</v-btn>
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
                            <img src="https://99px.ru/sstorage/53/2017/03/tmb_193520_3284.jpg" alt="avatar" class="avatar_img">
                        </v-avatar>
                        <v-container fluid style="display:block;">
                            <v-switch v-model="user.active" @change="isActive" label="Active"></v-switch>
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
                                <td><span v-for="role in user.roles">{{ role + ' ' }}</span></td>
                            </tr>
                            <tr>
                                <td>Teachers</td>
                                <td><p v-for="teacher in user.dtoTeachers" class="p">{{ getFullName(teacher) }}</p></td>
                            </tr>
                            <tr>
                                <td>Groups</td>
                                <td><p v-for="group in user.groups" class="p">{{ group.title }}</p></td>
                            </tr>
                            <tr>
                                <td>Manager</td>
                                <td><span class="p" v-if="user.dtoManager">{{ user.dtoManager.firstName + ' ' + user.dtoManager.lastName}}</span></td>
                            </tr>
                        </table>
                    </div>
                </v-container>
            </v-flex>
        </v-layout>

        <div>
            <v-expansion-panel  class="margin" >
                <v-expansion-panel-content>
                    <!--suppress HtmlUnknownBooleanAttribute -->
                    <template v-slot:header>
                        <div>Employee's attendance</div>
                    </template>
                    <div class="attendance" v-for="group in user.groups" >
                        <v-card>
                            <v-card-title>
                                Attendance of user {{user.firstName + ' ' + user.lastName}}  in group {{group.title}}
                            </v-card-title>

                            <attendance-table :user-id="user.id"
                                              :group-id="group.id"
                                              :key="group.id"/>
                        </v-card>
                    </div>
                </v-expansion-panel-content>
            </v-expansion-panel>
        </div>

        <calendar-list-schedule-component class="margin" :groups-list="user.groups" :lessons-list="lessons"></calendar-list-schedule-component>

    </v-container>
        {{user}}
    </div>
</template>

<script>
    import axios from 'axios'
    import AttendanceTable from "../components/AttendanceTable.vue";
    import CalendarListScheduleComponent from "../components/CalendarListScheduleComponent.vue";
    export default {
        components: {AttendanceTable, CalendarListScheduleComponent},
        data() {
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
                lessons : []
            }
        },
        methods: {
            getFullName (value) {
                return value.firstName + ' ' + value.lastName;
            },
            isActive () {
                axios.put('http://localhost:8080/users/update-active', {
                    active: this.user.active,
                    id: this.user.id
                })
            },
            editItem (user) {
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
            close () {
                this.dialog = false;
                setTimeout(() => {
                }, 300)
            },
            save () {
                if(this.editUser.firstName != null && this.editUser.lastName != null){
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
                }else{
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
    .table_user tr:last-child td{
        border-bottom: none;
    }
    .table_user tr td .p {
        margin: 0;
    }
    .select {
        width: 300px;
    }
    .attendance{
        margin-bottom: 20px;
        margin-top: 20px;
    }
    .margin{
        margin-top: 30px;
        margin-bottom: 30px;
    }
</style>