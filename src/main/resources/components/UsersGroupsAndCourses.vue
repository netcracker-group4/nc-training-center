<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-layout row wrap>
        <v-expansion-panel expand>
            <v-expansion-panel-content>
                <!--suppress HtmlUnknownBooleanAttribute -->
                <template v-slot:header>
                    <div>Employee's courses</div>
                </template>
                <div style="padding: 20px;">
                    <v-data-table
                            :headers="headers"
                            :items="groups"
                            class="elevation-1">
                        <template v-slot:items="props">
                            <td class="clickable" v-on:click="goToCoursePage(props.item.courseId)">
                                {{props.item.courseName }}
                            </td>
                            <td class="text-xs-right">
                                {{props.item.level }}
                            </td>
                            <td v-on:click="goToGroupPage(props.item.id)" class="text-xs-right clickable ">
                                {{props.item.title }}
                            </td>
                            <td v-on:click="goToTrainerPage(props.item.trainerId)" class="text-xs-right clickable">
                                {{getTrainerName(props.item.trainerId) }}
                            </td>
                        </template>
                    </v-data-table>
                </div>
            </v-expansion-panel-content>
        </v-expansion-panel>
    </v-layout>
</template>

<script>
    export default {
        name: "UsersGroupsAndCourses",
        props: ['trainers', 'groups'],

        data: function () {
            return {
                headers: [
                    {text: 'Course name', align: 'left', value: 'courseName'},
                    {text: 'Course level', value: 'level', align: 'right'},
                    {text: 'Group name', value: 'title', align: 'right'},
                    {text: 'Trainer', value: 'trainerName', align: 'right'}
                ],
            }
        },
        methods: {
            getTrainerName(trainerId) {
                let trainer = this.trainers.filter((e) => {
                    return e.id === trainerId;
                })[0];
                return trainer.firstName + ' ' + trainer.lastName;
            },

            goToGroupPage(groupId) {
                this.$router.push('/group/' + groupId);
            },
            goToCoursePage(courseId) {
                this.$router.push('/courses/' + courseId);
            },
            goToTrainerPage(trainerId) {
                this.$router.push('/userpage/' + trainerId);
            },
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