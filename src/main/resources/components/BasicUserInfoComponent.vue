<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-layout row wrap>
        <v-container xs12>
            <div v-if="user !== null && user !== undefined " style="padding: 20px;">
                <v-toolbar flat color="white">
                    <span>{{elemName}}</span>
                    <v-spacer></v-spacer>
                    <span v-if="viewerIsAdmin()" class="text-xs-center align-center ">
                        <v-switch v-if="viewerIsAdmin()" style="margin-bottom: -20px"
                                  v-model="user.active"
                                  @change="isActive"
                                  label="Active"></v-switch>
                    </span>
                    <v-dialog v-if="viewerIsAdmin() || isUserThisProfile()" v-model="dialog" max-width="500px">
                        <template v-slot:activator="{ on }">
                            <v-icon @click="editItem" style="margin-left: 30px">
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
                                        <v-flex xs10 v-if="viewerIsAdmin()">
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
                    <change-password-component :user="user"></change-password-component>
                </v-toolbar>

                <v-layout row wrap>
                    <v-flex xs12 md5 aling-center class="text-xs-center" style="display: flex;">
                        <!--                        <v-avatar style="margin: auto" size="75%" class="avatar">-->
                        <!--                            <img src="https://png.pngtree.com/svg/20161212/f93e57629c.svg" alt="avatar">-->
                        <!--                        </v-avatar>-->
                        <!--                        <input type="file" style="display: none" @change="onFileSelected()" ref="fileInput"/>-->
                        <!--                        <v-btn @click="$refs.fileInput.click()">Pick photo</v-btn>-->
                        <image-upload-component style="margin:auto;" :user="user"></image-upload-component>
                    </v-flex>
                    <v-flex xs12 md7 class="text-xs-right" style="padding: 20px 0">
<!--                        <table class="zui-table">-->
<!--                            <tbody>-->
<!--                            <tr>-->
<!--                                <td><span class="grey&#45;&#45;text">Name :</span></td>-->
<!--                                <td> <span-->
<!--                                        class="font-weight-medium">{{user.firstName}}</span>-->
<!--                                </td>-->
<!--                            </tr>-->
<!--                            <tr>-->
<!--                                <td><span class="grey&#45;&#45;text">Surname :</span></td>-->
<!--                                <td><span-->
<!--                                        class="font-weight-medium">{{user.lastName}}</span>-->
<!--                                </td>-->
<!--                            </tr>-->
<!--                            <tr>-->
<!--                                <td><span class="grey&#45;&#45;text">Email :</span></td>-->
<!--                                <td><span-->
<!--                                        class="font-weight-medium">{{user.email}}</span>-->
<!--                                </td>-->
<!--                            </tr>-->
<!--                            <tr v-if="canShowManager()" class="cursor"-->
<!--                                v-on:click="goToUserPage(user.dtoManager) ">-->
<!--                                <td><span class="grey&#45;&#45;text">Manger :</span></td>-->
<!--                                <td> <span-->
<!--                                        class="font-weight-medium">{{getManagersName(user.dtoManager)}}</span>-->
<!--                                </td>-->
<!--                            </tr>-->
<!--                            <tr v-if="canShowManager()">-->
<!--                                <td><span class="grey&#45;&#45;text">Teachers :</span></td>-->
<!--                                <td><span v-if="trainers.length < 1">No teachers yet</span>-->
<!--                                    <span v-else v-on:click="goToUserPage(teacher) "-->
<!--                                          v-for="teacher in trainers" class="font-weight-medium cursor">-->
<!--                                        {{getManagersName(teacher)}}</span>-->
<!--                                </td>-->
<!--                            </tr>-->
<!--                            <tr v-if="canShowManager()">-->
<!--                                <td>-->
<!--                                    <span class="grey&#45;&#45;text">Groups :</span>-->
<!--                                </td>-->
<!--                                <td>-->
<!--                                    <span v-if="groups.length < 1">No groups yet</span>-->
<!--                                    <span v-else v-for="group in groups" v-on:click="goToGroupPage(group.id)"-->
<!--                                          class="font-weight-medium cursor">{{group.title}}</span>-->
<!--                                </td>-->
<!--                            </tr>-->
<!--                            </tbody>-->
<!--                        </table>-->
                        <v-list>
                            <v-list-tile>
                                <v-list-tile-content>
                                    <v-list-tile-title><span class="grey--text name-my" >Name :</span> <span
                                            class="font-weight-medium">{{user.firstName}}</span></v-list-tile-title>
                                </v-list-tile-content>
                            </v-list-tile>
                            <v-list-tile>
                                <v-list-tile-content>
                                    <v-list-tile-title><span class="grey--text name-my">Surname :</span> <span
                                            class="font-weight-medium">{{user.lastName}}</span></v-list-tile-title>
                                </v-list-tile-content>
                            </v-list-tile>
                            <v-list-tile>
                                <v-list-tile-content>
                                    <v-list-tile-title><span class="grey--text name-my">Email :</span> <span
                                            class="font-weight-medium">{{user.email}}</span></v-list-tile-title>
                                </v-list-tile-content>
                            </v-list-tile>
                            <v-list-tile v-if="canShowManager()" class="cursor"
                                         v-on:click="goToUserPage(user.dtoManager) ">
                                <v-list-tile-content>
                                    <v-list-tile-title><span class="grey--text name-my">Manger :</span> <span
                                            class="font-weight-medium">{{getManagersName(user.dtoManager)}}</span>
                                    </v-list-tile-title>
                                </v-list-tile-content>
                            </v-list-tile>

                            <v-list-tile v-if="canShowManager()">
                                <v-list-tile-content>
                                    <v-list-tile-title><span class="grey--text name-my">Teachers :</span>
                                        <span v-if="trainers.length < 1">No teachers yet</span>
                                        <span v-else v-on:click="goToUserPage(teacher) "
                                              v-for="teacher in trainers" class="font-weight-medium cursor">
                                        {{getManagersName(teacher)}}</span>

                                    </v-list-tile-title>
                                </v-list-tile-content>
                            </v-list-tile>

                            <v-list-tile v-if="canShowManager()">
                                <v-list-tile-content>
                                    <v-list-tile-title><span class="grey--text name-my">Groups :</span>
                                        <span v-if="groups.length < 1">No groups yet</span>
                                        <span v-else v-for="group in groups" v-on:click="goToGroupPage(group.id)"
                                              class="font-weight-medium cursor">{{group.title}}</span>
                                    </v-list-tile-title>
                                </v-list-tile-content>
                            </v-list-tile>
                        </v-list>
                    </v-flex>
                </v-layout>
                <v-layout row wrap style="margin-bottom: 40px">
                    <v-spacer></v-spacer>
                    <div class="text-xs-center">
                        <v-dialog v-model="sendMessageWindowShow" width="500">
                            <template v-slot:activator="{ on }">
                                <v-btn v-if="self.$store.state.user.id != self.$route.params.id" color="success" large
                                       @click="sendMessageWindowShow = ! sendMessageWindowShow">Message
                                </v-btn>
                            </template>

                            <v-card>
                                <v-card-title class="headline grey lighten-2" primary-title>Send message to
                                    {{user.firstName + ' ' + user.lastName}}
                                </v-card-title>
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
            </div>
        </v-container>

    </v-layout>
</template>
<script>
    import axios from 'axios/index';
    import store from '../store/store.js';
    import ImageUploadComponent from './image/ImageUploadComponent.vue';
    import ChangePasswordComponent from './ChangePasswordComponent.vue';

    export default {
        name: 'basic-user-info-component',
        components: {
            ImageUploadComponent,
            ChangePasswordComponent
        },
        props: {elemName: String, user: {}, ifNullMessage: String, trainers: Array, groups: Array},
        data: function () {
            return {
                self: this,
                message: '',
                sendMessageWindowShow: false,
                dialog: false,
                editUser: {
                    id: 0,
                    firstName: '',
                    lastName: '',
                    email: '',
                    image: null,
                    roles: [],
                    dtoManager: '',
                    dtoTeachers: [],
                    groups: [],
                    active: false,
                },
                managers: [],
                expanded: [true]
            }
        },

        methods: {
            chatsReload() {
                let self = this
                axios.get(this.$store.state.apiServer + '/api/chats')
                    .then(response => {
                        self.$store.state.chats = response.data
                    })
            },
            goToGroupPage(groupId) {
                this.$router.push('/groups/' + groupId)
            },
            sendMessage() {
                let self = this
                let form = new FormData();
                let request = new XMLHttpRequest();
                request.open('POST', this.$store.state.apiServer + '/api/messages');
                form.append('text', this.message);
                form.append('senderId', this.$store.state.user.id);
                form.append('receiverId', this.user.id);
                request.send(form);
                request.onloadend = function () {
                    self.chatsReload()
                }
                this.message = ''
                this.sendMessageWindowShow = false
            },
            onFileSelected(event) {
                this.user.image = event.target.files[0]
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
            viewerIsAdmin() {
                return store.getters.isAdmin;
            },
            isUserThisProfile() {
                return store.state.user.id === this.user.id;
            },
            getManagersName(dtoManager) {
                if (dtoManager !== null) {
                    return dtoManager.firstName + ' ' + dtoManager.lastName
                } else return "Employee has no manager";
            },
            canShowManager() {
                if (this.user.roles !== undefined)
                    return this.user.roles.includes('EMPLOYEE');
            },
            editItem() {
                this.editUser = Object.assign({}, this.user);
                this.dialog = true;
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/users/get-all-managers')
                    .then(function (response) {
                        self.managers = response.data;
                        console.log(self.managers)
                    })
                    .catch(function (error) {
                        console.log(error);
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                        // self.errorAutoClosable(error.response.data);
                    });
            },
            goToUserPage(dtoManager) {
                if (dtoManager !== null) {
                    this.$router.push('/users/' + dtoManager.id);
                }
                window.scrollTo(0, 0);
            },
            close() {
                this.dialog = false;
                setTimeout(() => {
                }, 300)
            },

            getFullName(value) {
                return value.firstName + ' ' + value.lastName;
            },


            isActive() {
                let self = this;
                axios.put(this.$store.state.apiServer + '/api/users/update-active', {
                    active: this.user.active,
                    id: this.user.id
                }).then(function (response) {
                    if (self.user.active)
                        self.successAutoClosable("User is now active");
                    else self.successAutoClosable("User is now not active")
                }).catch(function (error) {
                    console.log(error);
                    self.errorAutoClosable('Server error occurred. User is not updated');
                })
            },

            save() {
                let self = this;
                if (this.editUser.firstName != null && this.editUser.lastName != null) {
                    axios.put(this.$store.state.apiServer + '/api/users/update', {
                        id: this.user.id,
                        firstName: this.editUser.firstName,
                        lastName: this.editUser.lastName,
                        dtoManager: this.editUser.dtoManager
                    }).then(function () {
                            Object.assign(self.user, self.editUser);
                            self.successAutoClosable('User is updated')
                        }
                    ).catch(function (error) {
                        console.log(error);
                        self.errorAutoClosable(error.response.data + '.  User is not updated');
                    })
                } else {
                    alert("Incorrect information in fields")
                }
                this.close()
            }

        }
    }
</script>
<style scoped>
    .name-my{
        width: 21%;
        display: inline-block;
    }
    .table_user {
        font-size: 14px;
        background: white;
        text-align: left;
        border-collapse: collapse;
        color: #3E4347;
        box-sizing: border-box;
        width: 100%;
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

    .cursor {
        cursor: pointer;
        margin-right: 10px;
    }

    .message-textarea {
        margin-top: 20px;
    }
</style>