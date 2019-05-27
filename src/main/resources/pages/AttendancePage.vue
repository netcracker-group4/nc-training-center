<template>
    <v-container>
        <v-layout>
            <h1>Attendance</h1>
            <v-spacer></v-spacer>
            <v-btn flat v-if="this.$store.getters.isAdmin"
                   @click="downloadAttendance" class="download-button">download attendance report
            </v-btn>
        </v-layout>
        <v-layout>
            <v-stepper v-model="e6" vertical style="width: 100%">
                <v-stepper-step :complete="e6 > 1" step="1" class="">
                    Choose trainer
                </v-stepper-step>

                <v-stepper-content step="1">
                    <attendance-trainer-board :data="trainers" @forward="next"></attendance-trainer-board>
                </v-stepper-content>

                <v-stepper-step :complete="e6 > 2" step="2">Choose group</v-stepper-step>

                <v-stepper-content step="2">
                    <attendance-group-board :data="groups" @forward="next"></attendance-group-board>
                    <v-btn flat @click="e6 = 1" class="back-button grey lighten-3">back</v-btn>
                </v-stepper-content>

                <v-stepper-step :complete="e6 > 3" step="3">Choose student</v-stepper-step>

                <v-stepper-content step="3">
                    <attendance-students-board :data="students" @forward="next"></attendance-students-board>
                    <v-btn flat @click="e6 = 2" class="back-button grey lighten-3">back</v-btn>
                </v-stepper-content>

                <v-stepper-step step="4">Attendance of student</v-stepper-step>
                <v-stepper-content step="4">
                    <attendance-lessons-board :data="lessons" @forward="next"></attendance-lessons-board>
                    <v-btn flat @click="e6 = 3" class="back-button grey lighten-3">back</v-btn>
                </v-stepper-content>
            </v-stepper>
        </v-layout>
    </v-container>
</template>

<script>
    import axios from 'axios'
    import AttendanceTrainerBoard from "../components/AttendanceTrainersBoard.vue";
    import AttendanceGroupBoard from "../components/AttendanceGroupBoard.vue";
    import AttendanceStudentsBoard from "../components/AttendanceStudentsBoard.vue";
    import AttendanceLessonsBoard from "../components/AttendanceLessonsBoard.vue";


    export default {
        name: "AttendancePage",
        components: {AttendanceLessonsBoard, AttendanceStudentsBoard, AttendanceGroupBoard, AttendanceTrainerBoard},
        data() {
            return {
                e6: 0,
                trainers: [],
                attendances: [],
                groups: [],
                students: [],
                lessons: [],
                groupId: null
            }
        },
        mounted() {
            axios.get(this.$store.state.apiServer + '/api/users/get-all-trainers')
                .then(response => this.trainers = response.data)
            /*axios.get(this.$store.state.apiServer + '/api/attendances')
                .then(response => {
                    this.attendances = response.data
                })
                .catch(error => {
                    console.log(error);
                    if (error.response != null && error.response.status == 400)
                        self.$router.push('/404');
                })*/
        },
        methods: {
            next(item) {
                this.e6++
                if (this.e6 == 2) {
                    axios.get(this.$store.state.apiServer + '/api/groups/trainer/' + item)
                        .then(response => this.groups = response.data)
                }
                if (this.e6 == 3) {
                    axios.get(this.$store.state.apiServer + '/api/groups/' + item + '/users')
                        .then(response => {
                            this.students = response.data
                            this.groupId = item
                        })
                }
                if (this.e6 == 4) {
                    axios.get(this.$store.state.apiServer + '/api/attendances?userId=' + item + '&groupId=' + this.groupId)
                        .then(response => this.lessons = response.data)
                }
            },
            downloadAttendance() {
                window.open(this.$store.state.apiServer + "/api/download-report/attendance-report", "_blank");
            },
        }
    }
</script>

<style scoped>
    .back-button {
        margin-top: 50px;
    }

    h1 {
        margin-bottom: 40px;
    }

    .download-button {
    }
</style>