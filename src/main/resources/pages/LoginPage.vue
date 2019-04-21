<template>
    <v-container class="form">
        <v-layout row wrap>
            <v-flex xs12 sm12 >
                <v-container>
                    <v-layout row wrap>
                        <v-flex xs12 sm12>
                            <v-text-field v-model="email" label="Email" ></v-text-field>
                        </v-flex>
                        <v-flex xs12 sm12>
                            <v-text-field
                                    :append-icon="pass_show ? 'visibility' : 'visibility_off'"
                                    v-model="password"
                                    :type="pass_show ? 'text' : 'password'"
                                    name="input-10-2"
                                    label="Password"
                                    class="input-group--focused"
                                    @click:append="pass_show = !pass_show"
                            ></v-text-field>
                        </v-flex>
                        <v-flex xs12 sm12>
                            <v-btn @click="login" class="green lighten-3 login-button">login</v-btn>
                            <v-btn @click="forwardToRegistrationPage" class="light-blue lighten-3 registration-button">registration page</v-btn>
                        </v-flex>
                    </v-layout>
                </v-container>
            </v-flex>
        </v-layout>
        <modal-page v-show="isModalVisible" @close="closeModal" :message="modalMessage"/>
    </v-container>
</template>

<script>
    import ModalPage from './ModalPage.vue'

    export default {
        name: "LoginPage",
        components: {ModalPage},
        data(){
            return{
                pass_show: false,
                email: null,
                password: null,
                isModalVisible: false,
                modalMessage: null
            }
        },
        methods:{
            login(){
                if(this.emailValidation(this.email) && this.passwordValidation(this.password)){
                    let form = new FormData()
                    let request = new XMLHttpRequest();
                    request.open('POST', 'http://localhost:8080/login');
                    form.append('username', this.email)
                    form.append('password', this.password)
                    request.send(form)
                    request.onreadystatechange = function () {
                        location.replace(location.origin)
                    }
                }else{
                    this.modalMessage = "Incorrect data"
                    //this.showModal()
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
            closeModal(){
                this.isModalVisible = false
            },
            showModal(){
                this.isModalVisible = true
            }
        }

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
</style>