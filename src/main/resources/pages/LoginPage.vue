<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-container class="form" id="email">
        <v-layout row wrap>
            <v-flex xs12 sm12 >
                <v-container>
                    <v-layout row wrap>
                        <v-flex xs12 sm12>
                            <v-text-field v-model="email" placeholder="Email"></v-text-field>
                        </v-flex>
                        <v-flex xs12 sm12>
                            <v-text-field
                                    :append-icon="pass_show ? 'visibility' : 'visibility_off'"
                                    v-model="password"
                                    :type="pass_show ? 'text' : 'password'"
                                    name="input-10-2"
                                    placeholder="Password"
                                    class="input-group--focused"
                                    @click:append="pass_show = !pass_show"
                            ></v-text-field>
                        </v-flex>
                        <v-flex xs12 sm12>
                            <v-btn @click="login" class="green lighten-3 login-button">login</v-btn>
                            <v-btn @click="forwardToRegistrationPage" class="light-blue lighten-3 registration-button">registration page</v-btn>
                        </v-flex>
                        <v-dialog v-model="recoverDialog" max-width="500px">
                            <template v-slot:activator="{ on }">
                                <v-flex xs12 sm12>
                                    <v-btn
                                            flat
                                            color="primary"
                                            class="mb-2"
                                            @click="recoverPassword"
                                            style="margin: 10px 0 0 0"
                                    >
                                        forget password
                                    </v-btn>
                                </v-flex>
                            </template>
                            <v-card>
                                <v-card-title>
                                    <span class="headline">Recover password</span>
                                </v-card-title>

                                <v-card-text>
                                    <v-container grid-list-md>
                                        <v-layout wrap>
                                            <v-flex xs10>
                                                <v-text-field v-model="emailSend"
                                                              label="Enter your email"
                                                              v-validate="'required|email'"
                                                              :error-messages="errors.collect('email')"
                                                              data-vv-name="email"
                                                              required
                                                ></v-text-field>
                                            </v-flex>

                                        </v-layout>
                                    </v-container>
                                </v-card-text>

                                <v-card-actions>
                                    <v-spacer></v-spacer>
                                    <v-btn color="blue darken-1" flat @click="close">Cancel</v-btn>
                                    <v-btn color="blue darken-1" flat @click="sendPassword">Send password</v-btn>
                                </v-card-actions>
                            </v-card>
                        </v-dialog>
                    </v-layout>
                </v-container>
            </v-flex>
        </v-layout>
        <div class="text-xs-center">
            <v-dialog v-model="dialog" width="500" height="300">
                <v-card>
                    <v-card-title class="headline grey lighten-4" primary-title>
                       {{this.modalMessage}}
                    </v-card-title>
                    <v-card-actions class="grey lighten-4">
                        <v-spacer></v-spacer>
                        <v-btn class="error-button light-blue lighten-3" @click="dialog = false">
                            submit
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-dialog>
        </div>
    </v-container>
</template>

<script>
    import axios from 'axios/index';
    import ModalPage from './ModalPage.vue'

    export default {
        $_veeValidate: {
            validator: 'new'
        },
        name: "LoginPage",
        components: {ModalPage},
        data(){
            return{
                pass_show: false,
                email: null,
                password: null,
                isModalVisible: false,
                modalMessage: null,
                dialog: false,
                recoverDialog: false,
                emailSend: '',
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
        methods:{
            login(){
                let self = this
                if(this.emailValidation(this.email) && this.passwordValidation(this.password)){
                    let form = new FormData();
                    let request = new XMLHttpRequest();
                    request.open('POST', this.$store.state.apiServer + '/login');
                    form.append('username', this.email);
                    form.append('password', this.password);
                    request.send(form);
                    request.onloadend = function () {
                        if(request.status == 200){
                            location.replace(location.origin)
                        }
                        if(request.status == 401){
                            self.modalMessage = "There is no user with such email and password"
                            self.dialog = true
                        }
                        if(request.status == 404){
                            request.send(form)
                        }
                    }
                }else{
                    this.modalMessage = "Entered data is incorrect"
                    this.dialog = true
                }
            },
            forwardToRegistrationPage(){
                this.$router.push('/registration')
            },
            emailValidation(email){
                var regularExpression = /^([a-zA-Z0-9\.-_]+)@([a-zA-Z0-9-]+).([a-z]{2,20})/
                return regularExpression.test(email)
            },
            passwordValidation(password){
                if( password != null &&
                    password != undefined &&
                    password.replace(/\s/g, '') != ''){
                    return true
                }else return false
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
            recoverPassword() {
                this.emailSend = null;
                this.$validator.reset();
                this.recoverDialog = true;
            },
            close() {
                this.emailSend = null;
                this.$validator.reset();
                this.recoverDialog = false;
                setTimeout(() => {
                }, 300)
            },
            sendPassword() {
                let self = this;
                this.$validator.validateAll().then((result) => {
                    if (result) {
                        axios.post(this.$store.state.apiServer + '/api/users/recover-password', {
                            email: this.emailSend
                        }).then(function () {
                            self.close();
                            self.successAutoClosable('New password sent to mail');
                        }).catch(function (error) {
                            self.errorAutoClosable('This user is not registered on the system')
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
    .form {
        width: 380px;
        margin: 4em auto;
        padding: 3em 2em 2em 2em;
        background: #f5f5f5;
        box-shadow: rgba(24, 24, 24, 0.4) 4px 4px 5px 0px
    }
    .login-button{
        margin: 0px;
        margin-top: 40px;
    }
    .registration-button{
        margin: 0px;
        margin-top: 40px;
        float: right;
    }
    .error-button{
        margin-bottom: 10px;
        margin-right: 10px;
    }
</style>