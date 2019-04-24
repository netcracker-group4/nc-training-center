<template>
    <v-container>
        <h1>Attendance</h1>
        <v-stepper v-model="e6" vertical>
            <v-stepper-step :complete="e6 > 1" step="1" class="">
                Choose trainer
            </v-stepper-step>

            <v-stepper-content step="1">
                <attendance-trainer-board :data="attendances.trainers" @forward="next"></attendance-trainer-board>
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
        data(){
            return{
                e6: 0,
                attendances: [],
                groups: [],
                students: [],
                lessons: []
            }
        },
        mounted() {
            axios.get('http://localhost:8080/attendances')
                .then(response => {
                    this.attendances = response.data
                })
                .catch(error => console.log(error))
        },
        methods:{
            next(item){
                this.e6++
                if(this.e6 == 2){
                    this.groups = this.attendances.trainers.find(trainer => trainer.id == item).groups
                }
                if(this.e6 == 3){
                    this.students = this.groups.find(group => group.id == item).students
                }
                if(this.e6 == 4){
                    this.lessons = this.students.find(student => student.id == item).lessons
                    console.log(this.lessons)
                }
            },
        }
    }
</script>

<style scoped>
    .back-button{
        margin-top: 50px;
    }
    h1{
        margin-bottom: 40px;
    }
</style>