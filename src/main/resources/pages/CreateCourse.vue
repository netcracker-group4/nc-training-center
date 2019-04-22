<template>
    <v-container>
        <v-layout row wrap>
            <v-flex xs12 sm4>
                <v-container>
                    <v-layout row wrap>
                        <v-flex xs12 sm12>
                            <v-text-field v-model="name" label="name" clearable></v-text-field>
                        </v-flex>
                        <v-flex xs12 sm12>
                            <v-text-field v-model="imageUrl" label="image url" clearable></v-text-field>
                        </v-flex>
                        <v-flex xs12 sm12>
                            <div v-for="lvl in levels">
                                <input type="radio" id="two" value="Два" v-model="picked" v-on:click="setLevel(lvl)">
                                <label for="two">{{lvl.title}}</label>
                            </div>
                        </v-flex>
                        <v-flex>
                            <p style="margin-top: 3%">Is on landing page?</p>
                            <input type="radio" @click="setOnLandingPage(true)" v-model="picked" >
                            <label>yes</label>
                            <input type="radio" @click="setOnLandingPage(false)" v-model="picked" >
                            <label>no</label>
                        </v-flex>
                        <v-flex xs12 sm12>
                            <v-flex xs6 style="width: 30%">
                                <v-textarea
                                        box
                                        name="input-7-4"
                                        label="Course description"
                                        auto-grow
                                ></v-textarea>
                            </v-flex>
                        </v-flex>
                        <v-spacer></v-spacer>
                    </v-layout>
                </v-container>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    import axios from 'axios'

    export default {
        name: "CreateCourse",
        data(){
            return{
                name: null,
                level: null,
                courseStatus: null,
                imageUrl: null,
                isOnLandingPage: null,
                description: null,
                levels: [],
                courseStatuses: []
            }
        },

        mounted() {
            try {
                let self = this;
                axios.get('http://localhost:8080/getInfo/getLevels')
                    .then(function (response) {
                        self.levels = response.data;
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
                /*axios.get('http://localhost:8080/getInfo/getStatuses')
                .then(function (response) {
                    self.courseStatuses = response.data;
                })
                .catch(function (error) {
                    console.log(error);
                });*/
            }catch (e) {

            }
        },
        methods:{
            /*submit(){
                this.description = desc;
                if(this.email != null & this.firstName != null
                    & this.lastName != null & this.password != null){
                    console.log(this.email + " " + this.password);
                    axios.post('http://localhost:8080/users', {
                        email: this.email,
                        firstName: this.firstName,
                        lastName: this.lastName,
                        password: this.password
                    })
                        .then(response => alert("User added"))
                }else{
                    alert("Incorrect information in fields")
                }

            },*/
            setLevel(lvl){
                this.level = lvl;
            },
            setOnLandingPage(is){
                this.isOnLandingPage = is;
            }
        }
    }
</script>

<style scoped>

</style>