<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-layout row wrap>
        <v-expansion-panel expand>
            <v-expansion-panel-content>
                <!--suppress HtmlUnknownBooleanAttribute -->
                <template v-slot:header>
                    <div>{{elemName}}</div>
                </template>
                <div style="padding: 20px;">
                    <v-toolbar flat color="white">
                        <v-spacer></v-spacer>
                        <v-dialog v-model="dialog" max-width="500px">
                            <template v-slot:activator="{ on }">
                                <v-icon class="mr-4" @click="editItem">
                                    edit
                                </v-icon>
                            </template>
                            <v-card>
                                <v-card-title>
                                    <span class="headline">Change Manager</span>
                                </v-card-title>

                                <v-card-text>
                                    <v-container grid-list-md>
                                        <v-layout wrap>
                                            <v-flex xs10>
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
                    </v-toolbar>
                    <v-layout row wrap v-if="user.dtoManager !== null && user.dtoManager !== undefined " align-center>
                        <v-flex xs12 md5 class="text-xs-right">
                            <v-avatar style="margin: auto" size="180">
                                <img src="https://png.pngtree.com/svg/20161212/f93e57629c.svg" alt="avatar">
                            </v-avatar>

                        </v-flex>
                        <v-flex xs12 md7 class="text-xs-right" style="padding: 20px 0">
                            <v-list two-line>
                                <v-list-tile>
                                    <v-list-tile-content>
                                        <v-list-tile-title><span class="grey--text">Name : </span> <span
                                                class="font-weight-medium">{{user.dtoManager.firstName}}</span>
                                        </v-list-tile-title>
                                    </v-list-tile-content>
                                </v-list-tile>
                                <v-list-tile>
                                    <v-list-tile-content>
                                        <v-list-tile-title><span class="grey--text">Surname : </span> <span
                                                class="font-weight-medium">{{user.dtoManager.lastName}}</span>
                                        </v-list-tile-title>
                                    </v-list-tile-content>
                                </v-list-tile>
                                <v-list-tile>
                                    <v-list-tile-content>
                                        <v-list-tile-title><span class="grey--text">Email : </span><span
                                                class="font-weight-medium">{{user.dtoManager.email}}</span>
                                        </v-list-tile-title>
                                    </v-list-tile-content>
                                </v-list-tile>
                            </v-list>
                        </v-flex>
                    </v-layout>
                    <v-layout row wrap v-if="user.dtoManager !== null && user.dtoManager !== undefined "
                              style="margin-bottom: 40px">
                        <v-spacer></v-spacer>
                        <v-btn color="success">Message</v-btn>
                    </v-layout>
                    <div v-else>
                        <v-card>
                            <v-card-text class="headline grey--text">
                                {{ifNullMessage}}
                            </v-card-text>
                        </v-card>
                    </div>
                </div>
            </v-expansion-panel-content>
        </v-expansion-panel>
    </v-layout>
</template>
<script>
    import axios from 'axios';

    export default {
        name: 'UsersMangerComponent',
        props: {elemName: String, user: {}, ifNullMessage: String},
        data: function () {
            return {
                dialog: false,
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
                managers: [],
            }
        },
        methods: {
            editItem() {
                this.editUser = Object.assign({}, this.user);
                this.dialog = true;
                let self = this;
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

            getFullName(value) {
                return value.firstName + ' ' + value.lastName;
            },


            isActive() {
                axios.put('http://localhost:8080/users/update-active', {
                    active: this.user.active,
                    id: this.user.id
                })
            },

            save() {
                Object.assign(this.user, this.editUser);
                axios.put('http://localhost:8080/users/update', {
                    id: this.user.id,
                    firstName: this.editUser.firstName,
                    lastName: this.editUser.lastName,
                    dtoManager: this.editUser.dtoManager
                });
                // .then(response => alert("User updated"))
                this.close()
            }

        }
    }
</script>
<style scoped>
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
</style>