<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-layout>
            <v-flex>
                <v-sheet height="500">
                    <v-calendar
                            color="primary"
                            ref="calendar"
                            v-model="start">
                        <template v-slot:day="{ date }">
                            <template v-for="lesson in lessonsMap[date]">
                                <v-menu :key="lesson.id" v-model="lesson.open" full-width offset-x>
                                    <template v-slot:activator="{ on }">
                                        <div
                                                v-ripple
                                                class="my-lesson"
                                                v-on="on"
                                                v-html="lesson.topic"
                                        >
                                        </div>
                                    </template>
                                    <v-card color="grey lighten-4" min-width="350px" flat>
                                        <v-toolbar color="primary" dark>
                                            <v-btn icon v-on:click="editLesson(lesson)">
                                                <v-icon>edit</v-icon>
                                            </v-btn>
                                            <v-spacer></v-spacer>
                                        </v-toolbar>
                                        <v-card-title primary-title>
                                            <span v-html="lesson.topic"></span>
                                            <span v-html="lesson.trainerId"></span>
                                        </v-card-title>
                                        <v-card-actions>
                                            <v-btn flat color="secondary">
                                                Cancel
                                            </v-btn>
                                        </v-card-actions>
                                    </v-card>
                                </v-menu>
                            </template>
                        </template>
                    </v-calendar>
                </v-sheet>
            </v-flex>
        </v-layout>
        <v-layout>
            <v-flex sm4 xs12 class="text-sm-left text-xs-center">
                <v-btn @click="$refs.calendar.prev()">
                    <v-icon dark left>
                        keyboard_arrow_left
                    </v-icon>
                    Prev
                </v-btn>
            </v-flex>
            <v-flex sm4 xs12 class="text-xs-center">
                <h2>{{currentMonth}}</h2>
            </v-flex>
            <v-flex sm4 xs12 class="text-sm-right text-xs-center">
                <v-btn @click="$refs.calendar.next()">
                    Next
                    <v-icon right dark>
                        keyboard_arrow_right
                    </v-icon>
                </v-btn>
            </v-flex>
        </v-layout>

        <lesson-editing-component v-if="editing" @saving-event="save" @cancel-event="cancel"
                                  :attachments="allAttachments" :currentLesson="currentLesson"
                                  :trainers="allTrainers"></lesson-editing-component>
    </div>
</template>

<script>
    import axios from 'axios';
    import LessonEditingComponent from '../components/LessonEditingComponent.vue';

    export default {
        name: "GroupSchedulePage",
        data: function () {
            return {
                lessons: [],
                currentLesson: {},
                start: new Date().toISOString().slice(0, 10),
                editing: false,
                allTrainers: [],
                allAttachments: []
            }
        },
        mounted() {
            let self = this;
            axios.get('http://localhost:8080/schedule/' + self.$route.params.id)
                .then(function (response) {
                    self.lessons = response.data;
                    self.lessons.forEach(function (one) {
                        one.open = false;
                    })
                })
                .catch(function (error) {
                    console.log(error);
                });
            axios.get('http://localhost:8080/users/get-all-trainers')
                .then(function (response) {
                    self.allTrainers = response.data;
                })
                .catch(function (error) {
                    console.log(error);
                });
            axios.get('http://localhost:8080/attachments/all')
                .then(function (response) {
                    self.allAttachments = response.data;
                })
                .catch(function (error) {
                    console.log(error);
                });
        },
        computed: {
            lessonsMap() {
                const map = {};
                this.lessons.forEach(e => (map[e.timeDate.substr(0, 10)] = map[e.timeDate.substr(0, 10)] || []).push(e));
                return map;
            },
            currentMonth() {
                let monthNames = [
                    "January", "February", "March",
                    "April", "May", "June", "July",
                    "August", "September", "October",
                    "November", "December"
                ];
                let date = new Date(this.start);
                let monthIndex = date.getMonth();
                let year = date.getFullYear();
                return monthNames[monthIndex] + ' ' + year;
            }
        },
        methods: {
            editLesson(lesson) {
                this.currentLesson = lesson;
                this.editing = true;
            },
            save(newLesson) {
                let self = this;
                axios.post('/schedule', newLesson)
                    .then(function (response) {
                        newLesson.id = response.data;
                        self.cancel();
                    })
                    .catch(function (error) {
                        console.log(error);
                    });

                console.log(newLesson);
            },
            cancel() {
                this.currentLesson = null;
                this.editing = false;
            }
        },
        components: {
            LessonEditingComponent
        }
    }
</script>

<style scoped>
    .my-lesson {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        border-radius: 2px;
        background-color: #1867c0;
        color: #ffffff;
        border: 1px solid #1867c0;
        width: 100%;
        font-size: 12px;
        padding: 3px;
        cursor: pointer;
        margin-bottom: 1px;
    }
</style>