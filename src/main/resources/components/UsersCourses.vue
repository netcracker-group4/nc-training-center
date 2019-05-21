<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-layout row wrap>
        <v-expansion-panel expand>
            <v-expansion-panel-content>
                <!--suppress HtmlUnknownBooleanAttribute -->
                <template v-slot:header>
                    <div>Trainer's courses</div>
                </template>
                <div style="padding: 20px;">
                    <v-data-table
                            :headers="headers"
                            :items="courses"
                            class="elevation-1">
                        <template v-slot:items="props">
                            <td class="clickable" v-on:click="goToCoursePage(props.item.id)">
                                {{props.item.name }}
                            </td>
                            <td class="text-xs-right">
                                {{props.item.startDate }}
                            </td>
                            <td class="text-xs-right">
                                {{props.item.endDate }}
                            </td>
                            <td class="text-xs-right">
                                {{props.item.level }}
                            </td>
                            <td class="text-xs-right">
                                {{props.item.courseStatusId }}
                            </td>
                            <!--                            <td v-on:click="goToTrainerPage(props.item.userId)" class="text-xs-right clickable">-->
                            <!--                                {{getTrainerName(props.item.userId) }}-->
                            <!--                            </td>-->
                        </template>
                    </v-data-table>
                </div>
            </v-expansion-panel-content>
        </v-expansion-panel>
    </v-layout>
</template>

<script>
    import axios from 'axios'

    export default {
        name: "UsersCourses",
        props: ['trainers'],
        data: function () {
            return {
                courses: [],
                headers: [
                    {
                        text: 'Course name',
                        align: 'left',
                        value: 'courseName'
                    },
                    {text: 'Start date', value: 'startDate', align: 'right'},
                    {text: 'End date', value: 'trainerName', align: 'right'},
                    {text: 'Level', value: 'level_id', align: 'right'},
                    {text: 'Status', value: 'status_id', align: 'right'}
                ],
            }
        },
        methods: {
            errorAutoClosable(title) {
                this.$snotify.error(title, {
                    timeout: 2000,
                    showProgressBar: false,
                    closeOnClick: false,
                    pauseOnHover: true
                });
            },
            goToCoursePage(courseId) {
                this.$router.push('/courses/' + courseId);
            },
            goToTrainerPage(trainerId) {
                this.$router.push('/users/' + trainerId);
            },
            loadInfo() {
                let self = this;
                let id = this.$route.params.id;
                axios.get(this.$store.state.apiServer + '/api/getcourses/trainer/' + id)
                    .then(function (response) {
                        self.courses = response.data;
                        console.log(self.courses)
                    })
                    .catch(function (error) {
                        console.log(error);
                        self.errorAutoClosable(error.response.data);
                    });
            }
        },
        mounted() {
            this.loadInfo();
        },
        watch: {
            '$route'(to, from) {
                this.loadInfo();
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