<template >
    <v-layout row>
        <v-flex xs12 sm6 offset-sm3>
            <v-card>
                <v-toolbar color="white" flat>
                    <v-btn icon light>
                        <v-icon color="grey darken-2">arrow_back</v-icon>
                    </v-btn>

                    <v-toolbar-title class="grey--text text--darken-4">Courses</v-toolbar-title>

                    <v-spacer></v-spacer>

                    <v-btn icon light>
                        <v-icon color="grey darken-2" @click="search">search</v-icon>
                    </v-btn>
                </v-toolbar>
                <v-subheader>June</v-subheader>
                <v-container fluid grid-list-xs v-for="course in courses">
                    <v-layout row wrap>
                        <v-flex
                                v-for="i in 6"
                                :key="i"
                                xs4
                        >
                            <img
                                    :src=course.imageURL
                                    alt="lorem"
                                    class="image"
                                    height="100%"
                                    width="100%"
                            >
                        </v-flex>
                        <v-flex>course.name</v-flex>
                    </v-layout>
                    <v-btn color="success" @click="updatecourse">Update</v-btn>
                    <v-btn color="error" @click="deleteCourse">Delete</v-btn>
                </v-container>

                <v-footer class="mt-5"></v-footer>
            </v-card>
        </v-flex>
    </v-layout>
</template>

<script>
    import axios from 'axios'

    export default {
        name: "AdminCourses",
        props: ['courses'],
        created: function () {
            axios.get('http://localhost:8001/courses').then(function (response) {
                response.data.forEach(course => this.courses.push(course));
                //console.log(response);
            })
        },
        methods: {
            search(){

            },
            deleteCourse(){

            },
            updatecourse(){

            }
        }
    }
</script>

<style scoped>

</style>