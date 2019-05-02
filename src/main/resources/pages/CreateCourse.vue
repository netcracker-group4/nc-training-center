<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
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
                                <input type="radio" id="two" value="Два" v-on:click="setLevel(lvl.title)">
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
                                        id="course-descr-text"
                                        box
                                        name="input-7-4"
                                        label="Course description"
                                        auto-grow
                                ></v-textarea>
                            </v-flex>
                        </v-flex>
                        <ChooserDate/>
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
    import ChooserDate from "../components/ChooserDate.vue";

    export default {
        name: "CreateCourse",
        components: {ChooserDate},
        data(){
            return{
                name: null,
                level: null,
                courseStatus: null,
                imageUrl: null,
                isOnLandingPage: null,
                description: null,
                startDay: null,
                endDay: null,
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
                console.log(e);
            }
        },
        methods:{

            setLevel(lvl){
                this.level = lvl;
            },
            setOnLandingPage(is){
                this.isOnLandingPage = is;
            },
            setData(){
                this.description=document.querySelector('#course-descr-text')._value;
                this.startDay = ChooserDate.data().date1;
                this.endDay = ChooserDate.data().date2;
                console.log(this);
            },
            submit(){
                    this.setData();
                let form = new FormData();
                let request = new XMLHttpRequest();
                request.open('POST', 'http://localhost:8080/getcourses/create');
                form.append('name', this.name);
                form.append('level',this.level);
                form.append('courseStatus',this.courseStatus);
                form.append('imageUrl',this.imageUrl);
                form.append('isOnLandingPage',this.isOnLandingPage);
                form.append('description',this.description);
                form.append('startDay',this.startDay);
                form.append('endDay',this.endDay);
                request.send(form);
                    /*axios.post('http://localhost:8080/getcourses/create', {
                        name: this.name,
                        level: this.level,
                        courseStatus: this.courseStatus,
                        imageUrl: this.imageUrl,
                        isOnLandingPage: this.isOnLandingPage,
                        description: this.description,
                        startDay: this.startDay,
                        endDay: this.endDay
                    })
                        .then(response => alert("Course created"))*/
            }

        }
    }
</script>

<style scoped>

</style>