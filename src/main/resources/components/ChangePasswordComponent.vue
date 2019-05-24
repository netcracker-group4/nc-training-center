<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-dialog v-if="isUserThisProfile()" v-model="dialog" max-width="500px">
            <template v-slot:activator="{ on }">
                <v-btn
                        flat
                        color="black"
                        class="mb-2"
                        @click="editItem"
                        style="margin-left: 30px"
                >
                    Edit password
                </v-btn>
            </template>
            <v-card>
                <v-card-title>
                    <span class="headline">Edit password</span>
                </v-card-title>

                <v-card-text>
                    <v-container grid-list-md>
                        <v-layout wrap>
                            <v-flex xs10>
                                <v-text-field v-model="oldPassword"
                                              label="Enter the old password"
                                              v-validate="'required'"
                                              :error-messages="errors.collect('oldPassword')"
                                              data-vv-name="oldPassword"
                                              required
                                ></v-text-field>
                            </v-flex>
                            <v-flex xs10>
                                <v-text-field v-model="newPassword"
                                              label="Enter the new password"
                                              v-validate="'required'"
                                              :error-messages="errors.collect('newPassword')"
                                              data-vv-name="newPassword"
                                              required
                                ></v-text-field>
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
    </div>
</template>

<script>
    import axios from 'axios/index';
    import store from '../store/store.js';

    export default {
        $_veeValidate: {
            validator: 'new'
        },
        name: "change-password-component",
        props: {user: {}},
        data: function () {
            return {
                dialog: false,
                oldPassword: '',
                newPassword: '',
                dictionary: {
                    custom: {
                        oldPassword: {
                            required: () => 'Password can not be empty',
                        },
                        newPassword: {
                            required: () => 'Password can not be empty',
                        },
                    }
                }
            }
        },
        methods: {
            isUserThisProfile() {
                return store.state.user.id === this.user.id;
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
            editItem() {
                // this.editUser = Object.assign({}, this.user);
                this.oldPassword = '';
                this.newPassword = '';
                this.$validator.reset();
                this.dialog = true;
            },
            close() {
                this.oldPassword = '';
                this.newPassword = '';
                this.$validator.reset();
                this.dialog = false;
                setTimeout(() => {
                }, 300)
            },
            save() {
                let self = this;
                this.$validator.validateAll().then((result) => {
                    if (result) {
                        axios.put(this.$store.state.apiServer + '/api/users/update-password', {
                            userId: this.user.id,
                            oldPassword: this.oldPassword,
                            newPassword: this.newPassword,
                        }).then(function () {
                            self.close();
                            self.successAutoClosable('Password is updated');
                        }).catch(function (error) {
                            self.errorAutoClosable('Password is incorrect')
                        })
                    }
                })
            }
        },
        mounted() {
            this.$validator.localize('en', this.dictionary);
        },
    }
</script>

<style scoped>

</style>