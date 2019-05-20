<template>
    <div>
        <v-container>
            <v-layout row wrap>
                <v-flex xs12 sm12>
                    <v-container class="con_wrapper">
                        <div class="div_wrapper">
                            <div style="position: relative;">
                                <label v-if="isAdmin()" class="user_block_label">Add manager or trainer</label>
                                <label v-if="!isAdmin()" class="user_block_label">
                                    Registration
                                </label>
                            </div>
                            <form>
                                <v-text-field
                                        v-model="user.firstName"
                                        v-validate="'required'"
                                        :error-messages="errors.collect('name')"
                                        label="Name"
                                        data-vv-name="name"
                                        required
                                ></v-text-field>
                                <v-text-field
                                        v-model="user.lastName"
                                        v-validate="'required'"
                                        :error-messages="errors.collect('surname')"
                                        label="Surname"
                                        data-vv-name="surname"
                                        required
                                ></v-text-field>
                                <v-text-field
                                        v-model="user.email"
                                        v-validate="'required|email'"
                                        :error-messages="errors.collect('email')"
                                        label="E-mail"
                                        data-vv-name="email"
                                        required
                                ></v-text-field>
                                <v-text-field
                                        v-model="user.password"
                                        v-validate="'required'"
                                        :error-messages="errors.collect('password')"
                                        data-vv-name="password"
                                        label="Password"
                                        required
                                ></v-text-field>
                                <v-radio-group
                                        v-model="user.role"
                                        row
                                        v-if="isAdmin()"
                                        v-validate="'required'"
                                        :error-messages="errors.collect('role')"
                                        data-vv-name="role"
                                        required
                                >
                                    <v-radio label="Manager" value="MANAGER"></v-radio>
                                    <v-radio label="Trainer" value="TRAINER"></v-radio>
                                </v-radio-group>

                                <div class="buttons">
                                    <v-btn
                                            @click="clearUser"
                                            fab
                                            small
                                            dark
                                            color="#e6e4ee"
                                    >
                                        <v-icon>clear</v-icon>
                                    </v-btn>
                                    <v-btn @click="submitUser">submit</v-btn>
                                    <v-btn
                                            @click="toLoginPage()"
                                            flat
                                            color="primary"
                                            class="mb-2 login"
                                            v-if="!isAdmin()"
                                    >
                                        Login
                                    </v-btn>
                                </div>
                            </form>
                        </div>
                    </v-container>
                </v-flex>
            </v-layout>
        </v-container>
    </div>
</template>

<script>
    import axios from 'axios';
    import store from '../store/store.js';

    export default {
        $_veeValidate: {
            validator: 'new'
        },

        data: () => ({
            user: {
                firstName: '',
                lastName: '',
                email: '',
                password: '',
                role: null,
            },
            dictionary: {
                attributes: {
                    email: 'E-mail Address',
                    // custom attributes
                },
                custom: {
                    surname: {
                        required: () => 'Surname can not be empty',
                    },
                    name: {
                        required: () => 'Name can not be empty',
                    },
                    password: {
                        required: () => 'Password can not be empty',
                    },
                    role: {
                        required: () => 'The role is not chosen'
                    }
                }
            },

        }),

        methods: {
            isAdmin() {
                return store.state.userRoles !== null && store.state.userRoles.includes("ADMIN");
            },
            isAdminOrRegisteredUser() {
                return store.state.userRoles === null || store.state.userRoles.includes("ADMIN");
            },
            toLoginPage() {
                this.$router.push('/login');
            },
            submitUser () {
                this.$validator.validateAll().then((result) => {
                    if (result) {
                        let self = this;
                        console.log(this.user);
                        axios.post('/api/users', {
                            firstName: this.user.firstName,
                            lastName: this.user.lastName,
                            email: this.user.email,
                            password: this.user.password,
                            role: this.user.role
                        }).then(response => {
                            console.log(response);
                            self.$validator.reset();
                        });
                    }
                })
            },
            clearUser () {
                this.user.firstName = '';
                this.user.lastName = '';
                this.user.email = '';
                this.user.password = '';
                this.user.role = '';
                this.$validator.reset();
            },
        },

        mounted () {
            this.$validator.localize('en', this.dictionary)
            if (!this.isAdminOrRegisteredUser()) {
                this.$router.push('/404');
            }
        },
    }
</script>

<style scoped>
    .con_wrapper {
        background: white;
        display: flex;
        margin: 0 auto 0;
        width: 100%;
    }
    .div_wrapper {
        margin: 0 auto;
        width: 30%;
    }
    .user_block_label {
        font-size: 20px;
    }
    .buttons {
        position: relative;
    }
    .buttons .login {
        position: absolute;
        right: 0;
    }
</style>