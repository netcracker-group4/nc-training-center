<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-container>
        <v-layout wrap row>
            <v-flex xs10 sm10 md10 offset-xs1 offset-sm1 offset-md1>
                <v-card v-for="course in courses" :key="course.id" class="course">
                    <v-img
                            :src="'/img/' + course.imageUrl + '.jpg'"
                            aspect-ratio="2.00"
                    ></v-img>

                    <v-card-title primary-title>
                        <div>
                            <h3 class="headline mb-0">{{course.name}}</h3>
                            <div> {{ course.description}} </div>
                        </div>
                    </v-card-title>

                    <v-card-actions>
                        <v-btn flat @click="forwardToCoursePage(course.id)">More</v-btn>
                    </v-card-actions>
                </v-card>
                <!--<v-card>
                    <div class = "text">
                        Trainers on landing page:
                    </div>
                </v-card>
                <v-expansion-panel>
                    <v-expansion-panel-content
                            v-for="trainer in trainers"
                            :key="trainer.id"
                    >
                        <template v-slot:header>
                            <div>{{trainer.lastName}} {{trainer.firstName}}</div>
                        </template>
                        <v-card>
                            <v-card-text>
                                <div>Email: {{trainer.email}}</div>
                                <div>Description: {{trainer.description}}</div>
                            </v-card-text>
                        </v-card>
                    </v-expansion-panel-content>
                </v-expansion-panel>-->
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>

    import axios from 'axios'

    export default {
        name: "MainPage",
        data: function(){
          return{
              courses: [],
              trainers: []
          }
        },
        mounted() {
            let self = this;
            axios.get('http://localhost:8080/main-page/courses-on-landing-page')
                            .then(function (response) {
                                self.courses = response.data;
                            })
                            .catch(function (error) {
                                console.log(error);
                            });
            axios.get('http://localhost:8080/main-page/trainers-on-landing-page')
                            .then(function (response) {
                                self.trainers = response.data;
                            })
                            .catch(function (error) {
                                console.log(error);
                            });

},
        methods: {
            forwardToCoursePage(id){
                this.$router.push('/courses/' + id)
            }
        }
    }

</script>

<style>
    .name{
    position:static;
    up:5px;
    background-color: rgba(0, 0, 0, 0.5);
    color: white;
    font-size: 2em;
      }
    .descr{

    position:absolute;
    bottom:50px;
    background-color: rgba(0, 0, 0, 0.5);
    color: white;
    font-size: 1em;
    padding: 5px;
    }
    .course{
        margin-bottom: 40px;
    }
</style>