<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-container>
        <v-layout row wrap>
            <v-flex xs12 sm6>
            </v-flex>
        </v-layout>
        <v-layout row wrap>
            <v-flex xs12>
                <template>
                  <v-card>
                    <div class = "text">
                    Courses on landing page:
                  </div>
                  </v-card>
                  <v-carousel>
                    <v-carousel-item
                      v-for="crs in courses"
                      v-bind:src="crs.imageUrl"
                      :key="crs.id">


                      <div class = "name">
                          <a href="'/courses/' + crs.id" style="color:white"> {{crs.name}} </a>
                      </div>
                      <div class = "descr">
                      Description: {{crs.description}}
                      </div>
                    </v-carousel-item>
                  </v-carousel>
                  <v-card>
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
                          <div>{{trainer.last_name}} {{trainer.first_name}}</div>
                        </template>
                        <v-card>
                          <v-card-text>
                            Here will be info about this trainer
                          </v-card-text>
                        </v-card>
                      </v-expansion-panel-content>
                    </v-expansion-panel>
                </template>
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
            search(){

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
    background-repeat:x;
    position:absolute;
    bottom:50px;
    background-color: rgba(0, 0, 0, 0.5);
    color: white;
    font-size: 1em;
    padding: 5px;
    }
</style>