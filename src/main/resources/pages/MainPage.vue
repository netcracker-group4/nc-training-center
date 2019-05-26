<template>
    <v-container>
        <progress-circular-component v-if="loading"></progress-circular-component>
        <v-layout v-if="!loading" wrap row>
            <v-flex xs12 sm12 md12>
                <v-container fluid class="trainer-container">
                    <h1 class="mr">Trainers</h1>
                    <v-layout row>
                        <v-flex xs4 v-for="trainer in trainers" :key="trainer.id" class="trainer-element" @click="forwardToUserPage(trainer.id)">
                            <v-card flat color="grey lighten-4" class="tr-card">
                                <v-img  v-if="trainer.imageUrl != null"
                                        :src="self.$store.state.apiServer + '/api/users/image?url='+ trainer.imageUrl"
                                        aspect-ratio="1.25"
                                ></v-img>
                                <img v-if="trainer.imageUrl == null"
                                     src="https://png.pngtree.com/svg/20161212/f93e57629c.svg"
                                     class="avatar">

                                <v-card-title primary-title>
                                    <div>
                                        <h3 class="headline mb-0 trainer-name">{{trainer.firstName + ' ' + trainer.lastName}}</h3>
                                    </div>
                                </v-card-title>
                            </v-card>
                        </v-flex>

                    </v-layout>
                </v-container>
            </v-flex>
            <v-flex xs12 sm12 md12 >
                <h1 class="mr">Courses</h1>
                <carousel :per-page="1" :mouse-drag="true">
                    <slide v-for="course in courses" :key="course.id">
                        <v-card flat color="grey lighten-4">
                            <v-img
                                     :src="self.$store.state.apiServer + '/api/users/image?url='+ course.imageUrl"
                                     aspect-ratio="2.20"
                             ></v-img>

                             <v-card-title primary-title>
                                 <div>
                                     <h3 class="headline mb-0">{{course.name}}</h3>
                                     <div> {{ course.description}}</div>
                                 </div>
                             </v-card-title>

                             <v-card-actions>
                                 <v-btn flat class="grey lighten-2 btn" @click="forwardToCoursePage(course.id)">More</v-btn>
                             </v-card-actions>
                         </v-card>
                     </slide>
                 </carousel>

             </v-flex>
         </v-layout>
     </v-container>
 </template>

 <script>
     import {connect} from '../websocket/ws'
     import axios from 'axios'
     import ProgressCircularComponent from "../components/ProgressCircularComponent.vue";

     export default {
         name: "MainPage",
         components: {
             ProgressCircularComponent
         },
         data: function () {
             return {
                 self: this,
                 courses: [],
                 trainers: [],
                 loading: true
             }
         },
         mounted() {
             let self = this;
             axios.get(this.$store.state.apiServer + '/api/main-page/courses-on-landing-page')
                 .then(function (response) {
                     self.courses = response.data;
                     self.loading = false;
                 })
                 .catch(function (error) {
                     console.log(error);
                 });
             axios.get(this.$store.state.apiServer + '/api/main-page/trainers-on-landing-page')
                 .then(function (response) {
                     self.trainers = response.data;
                 })
                 .catch(function (error) {
                     console.log(error);
                 });
                 //connect()

         },
         methods: {
             forwardToCoursePage(id) {
                 this.$router.push('/courses/' + id)
             },
             forwardToUserPage(id){
                 this.$router.push('/users/' + id)
             }
         }
     }

 </script>

 <style>
     .name {
         position: static;
         up: 5px;
         background-color: rgba(0, 0, 0, 0.5);
         color: white;
         font-size: 2em;
     }

     .descr {

         position: absolute;
         bottom: 50px;
         background-color: rgba(0, 0, 0, 0.5);
         color: white;
         font-size: 1em;
         padding: 5px;
     }

     .course {
         margin-bottom: 40px;
     }
     .btn{
         margin-bottom: 10px;
     }
     img {
         padding: 5px;
         width: 100px;
     }
     .trainer-container{
         padding: 0;
         margin-bottom: 20px;
     }
     .trainer-element{
         padding: 10px
     }
     .trainer-element :hover{
         cursor: pointer;

     }
     .trainer-name{
         color: rgba(0,0,0,.87)
     }
     .mr{
         margin-bottom: 30px;
     }
     .tr-card{
         padding: 20px;
     }
 </style>