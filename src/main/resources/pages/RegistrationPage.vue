<template>
    <v-container>
        <v-layout row wrap>
            <v-flex xs12 sm4>
                <v-container>
                    <v-layout row wrap>
                        <v-flex xs12 sm12>
                            <v-text-field v-model="email" label="Email" clearable></v-text-field>
                        </v-flex>
                        <v-flex xs12 sm12>
                            <v-text-field v-model="firstName" label="First name" clearable></v-text-field>
                        </v-flex>
                        <v-flex xs12 sm12>
                            <v-text-field v-model="lastName" label="Last name" clearable></v-text-field>
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
                        <v-spacer></v-spacer>
                        <v-flex xs12 sm12>
                            <v-btn @click="submit">submit</v-btn>
                        </v-flex>
                    </v-layout>
                </v-container>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    import axios from 'axios'

    export default {
        name: "RegistrationPage",
        data(){
            return{
                pass_show: false,
                email: null,
                firstName: null,
                lastName: null,
                password: null,
                isActive: false,
            }
        },
        methods:{
            submit(){
                if(this.email != null & this.firstName != null
                    & this.lastName != null & this.password != null){
                    console.log(this.email + " " + this.password)
                    axios.post('/api/users', {
                        email: this.email,
                        firstName: this.firstName,
                        lastName: this.lastName,
                        password: this.password
                    })
                        .then(response => alert("User added"))
                }else{
                    alert("Incorrect information in fields")
                }

            }
        },
    }
</script>

<style scoped>

</style>