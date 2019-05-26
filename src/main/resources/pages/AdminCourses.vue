<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-container>
        <progress-circular-component v-if="loading"></progress-circular-component>

        <v-layout v-if="!loading" row wrap>
            <v-flex xs12 sm12>
                <v-toolbar flat color="white">
                    <v-toolbar-title>Courses</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-btn flat color="primary" v-if="isAdmin" @click="createCourse">Create new course</v-btn>
                </v-toolbar>
            </v-flex>
            <v-flex>
                <v-data-table
                        :headers="headers"
                        :items="coursesAndQuantities"
                        :expand="true"
                        item-key="course.id"
                        :rows-per-page-items="nums"
                >
                    <template v-slot:items="props">
                        <tr>
                            <td class="text-xs-left" @click="goToCoursePage(props.item.course.id)">
                                {{ props.item.course.id }}
                            </td>
                            <td class="my-link clickable" @click="goToCoursePage(props.item.course.id)">
                                {{ props.item.course.name }}
                            </td>
                            <td class="text-xs-right" @click="goToCoursePage(props.item.course.id)">
                                {{ props.item.numberOfEmployees }}
                            </td>
                            <td class="text-xs-right" v-if="isAdmin">
                                <v-btn color="error" @click="deleteCourse(props.item.course)">Delete</v-btn>
                            </td>

                        </tr>
                    </template>
                </v-data-table>
            </v-flex>
        </v-layout>
    </v-container>
</template>
<script>
    import axios from 'axios'
    import ProgressCircularComponent from "../components/ProgressCircularComponent.vue";

    export default {
        name: "AdminCourses",
        components: {
            ProgressCircularComponent
        },
        data: function () {
            return {
                nums: [10, 25, {"text": "$vuetify.dataIterator.rowsPerPageAll", "value": -1}],
                loading: true,
                headers: [
                    {
                        text: 'Course id',
                        align: 'left',
                        value: 'course.id'
                    },
                    {
                        text: 'Name of the course',
                        align: 'left',
                        value: 'course.name'
                    },
                    {
                        text: 'Number of employees', value: 'numberOfEmployees',
                        width: "20", align: 'right'
                    },
                    {
                        text: '', value: 'quantityOfEmployees',
                        sortable: false,
                        width: "20", align: 'right'
                    }
                ],
                coursesAndQuantities: [],
                isAdmin: this.$store.getters.isAdmin
            }
        },
        mounted() {
            let self = this;
            axios.get(this.$store.state.apiServer + '/api/getcourses')
                .then(function (response) {
                    self.coursesAndQuantities = response.data;
                    self.loading = false;
                })
                .catch(function (error) {
                    console.log(error);
                    if (error.response != null && error.response.status == 400)
                        self.$router.push('/404');
                });
        },
        methods: {
            successAutoClosable(title) {
                this.$snotify.success(title, {
                    timeout: 2000,
                    showProgressBar: false,
                    closeOnClick: false,
                    pauseOnHover: true
                });
            },
            errorAutoClosable(title) {
                this.$snotify.error(title, {
                    timeout: 2000,
                    showProgressBar: false,
                    closeOnClick: false,
                    pauseOnHover: true
                });
            },
            goToCoursePage(id) {
                this.$router.push('/courses/' + id);
            },
            deleteCourse(course) {
                let self = this;
                if (confirm("Are you sure you want to delete " + course.name)) {
                    axios.delete(this.$store.state.apiServer + '/api/getcourses/' + course.id)
                        .then(function () {
                            self.successAutoClosable("Course" + course.name + " has been deleted");
                            self.coursesAndQuantities = self.coursesAndQuantities.filter(function (e) {
                                return e.course.id != course.id;
                            })
                        })
                        .catch(function (error) {
                            console.log(error);
                            self.errorAutoClosable(error.response.data);
                        });
                }
            },
            update(courseId) {
                this.$router.push('/courses/' + courseId + '/edit');
            },
            createCourse() {
                this.$router.push('/courses/new');
            }
        }
    }
</script>


<style scoped>
    .clickable {
        cursor: pointer;
        margin-bottom: 5px;
        margin-top: 5px;
        color: black;
    }
</style>