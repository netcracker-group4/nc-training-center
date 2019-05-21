<template>
    <v-container class="form" id="email">
        <v-layout row wrap>
            <v-flex xs12 sm12>
                <v-container>
                    <v-layout row wrap>
                        <v-flex xs12 sm12>
                            Type a short description of your problem: {{userId}}
                            <v-text-field
                                    clearable
                                    v-model="description"
                                    counter
                                    maxlength="50"
                                    full-width
                            ></v-text-field>
                        </v-flex>
                        <v-flex xs12 sm12>
                            Describe your problem in details:
                            <v-text-field
                                    clearable
                                    v-model="message"
                                    full-width
                                    single-line
                            ></v-text-field>
                        </v-flex>

                        <v-flex xs12 sm12>
                            <v-btn @click="send" class="green lighten-3 login-button">SEND</v-btn>
                        </v-flex>
                    </v-layout>
                </v-container>
            </v-flex>
        </v-layout>

    </v-container>
</template>


<script>

    import store from "../store/store.js";


    export default {
        name: "InfodeskRequests",
        data () {
            return{
                description: null,
                message: null,
                userId: this.$store.state.user.id
            }
        },
        methods:{
            send (){
                // if (this.description != null && this.message != null) {
                //         console.log(this.description + " " + this.message);
                //         axios.post('http://localhost:8080/api/requests/create-request', {
                //             userId: this.userId.toString(),
                //             description: this.description,
                //             message: this.message
                //         })
                //             .then(response => alert("Your request added"))
                //     } else {
                //         alert("Sorry, you can't leave a field empty")
                //     }

                let form = new FormData();

                let request = new XMLHttpRequest();
                request.open('POST', this.$store.state.apiServer + '/api/requests/create-request');
                form.append('userId', this.userId);
                form.append('description', this.description);
                form.append('message', this.message);
                request.send(form);
            },
            isUserAdmin(){
                return store.getters.isAdmin;
            }
        },
    }

</script>