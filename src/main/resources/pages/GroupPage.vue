<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <div class="subheading pt-3">
            <b>{{course.name}}</b>
            <p>
                <b>Trainer</b>
                <b class="clickable" @click="forwardToUserPage(teacher.id)">{{teacher.firstName}} {{teacher.lastName}}</b>
            </p>
            <v-icon v-if="hasRights()"  @click="forwardToGroupSchedulePage" >
                event
            </v-icon>
        </div>
        <div>Group with  {{ id }}  number</div>
        <v-data-table
            :headers="headers"
            :items="students"
            :expand="true"
            item-key="id"
        >
            <template v-slot:items="props" >
                <tr class="clickable" @click="forwardToUserPage(props.item.id)">
                    <td >
                        <div>{{ props.item.id }}</div>
                    </td>
                    <td class="text-xs-left clickable">{{ props.item.firstName +' '+ props.item.lastName }}</td>
                    <td class="text-xs-left" v-if="isAdmin">
                        {{props.item.email}}
                    </td>
                    <td class="text-xs-right" v-if="isAdmin">
                        <v-btn color="error" @click="deleteStudent(props.item.id)" v-if="isAdmin">Delete</v-btn>
                    </td>
                </tr>
            </template>
        </v-data-table>
        <v-data-table
                :headers="headers2"
                :items="lessons"
                :expand="true"
                item-key="id"
                no-data-text = "No lessons available"
        >
            <template v-slot:items="props">
                <tr class="clickable" @click="forwardToLessonPage(props.item.id)">
                    <td class="my-link">
                        <div>{{ props.item.topic}}</div>
                    </td>
                    <td>
                        <div>{{props.item.timeDate.toString()}}</div>
                    </td>
                    <td>
                        <div>{{ lessonStatus(props.item.isCanceled)}}</div>
                    </td>
                </tr>
            </template>
        </v-data-table>
        <v-container v-if="$store.getters.isAdmin || $store.getters.isTrainer">
            <group-attendance class="margin" :groupId="id"/>
        </v-container>
    </div>
</template>

<script>
    import axios from 'axios';
    import store from '../store/store.js';
    import GroupAttendance from "../components/GroupAttendance.vue";

    export default {
        props: ['id'],
        name: "GroupPage",
        components: {GroupAttendance},
        data: function(){
            return{
                students: [],
                teacher: [],
                course: [],
                lessons:[],
                headers: [
                    {
                        text: 'Student Id',
                        align: 'left',
                        value: 'id'
                    },
                    {
                        text: 'Student name', value: 'firstName'+'lastName',
                        align: 'left'
                    },
                    {
                        text: '@email', value: 'email',
                        width: "40", align: 'right'
                    },
                    {
                        text: 'action', value: 'action',
                        width: "30", align: 'right'
                    }
                ],
                headers2: [
                    {   text: 'Lesson topic',
                        align: 'left',
                        value: 'topic'
                    },
                    {
                     text: 'Date', value: 'timeDate.toString()', align: 'left'
                    },
                    {
                     text: 'Status', value: 'isCanceled', align: 'left'
                    }

                ],
                isAdmin: this.$store.getters.isAdmin,
            }
        },
        methods:{
            lessonStatus(flag){
                if(flag == true){
                    return 'Canceled';
                }
                return 'OK';
                },
            deleteStudent(id) {
                if (confirm("Are you sure you want to delete " + this.findUserById(id).firstName +' '+ this.findUserById(id).lastName)) {
                    axios.delete('http://localhost:8080/groups/' + self.id + '/user' + id)
                        .then(function (response) {
                        });
                }
            },
            hasRights(){
                return store.getters.user.id == this.teacher.id || store.getters.isAdmin;
            },
            findUserById(id){
                return this.students.find(s => s.id == id);
            },
            setGroup(){
                let self = this;
                let c;
                axios.get('http://localhost:8080/groups/'+self.id+'/course')
                    .then(function (response) {
                        c = response.data;
                        self.course = c;
                    }).catch(function (error) {
                    console.log(error);
                });
                axios.get('http://localhost:8080/groups/'+self.id+'/trainer')
                    .then(function (response) {
                        self.teacher = response.data;
                    });
                axios.get('http://localhost:8080/groups/'+self.id+'/users')
                    .then(function (response) {
                        self.students = response.data;
                    }).catch(function (error) {
                    console.log(error);
                });
                axios.get('http://localhost:8080/schedule/'+self.id)
                    .then(function (response) {
                        self.lessons = response.data;
                    }).catch(function (error) {
                    console.log(error);
                });
            },
            forwardToUserPage(id){
                this.$router.push('/userpage/' + id)
            },
            forwardToTrainerPage(id){
                this.$router.push('/trainers/' + id)
            },
            forwardToLessonPage(id){
                this.$router.push('/lesson/' + id)
            },
            forwardToGroupSchedulePage(){
                let self = this;
                this.$router.push('/groups/' + self.id + '/schedule')
            }

        },
        mounted(){
            let self = this;
            console.log(self);
            self.setGroup();
            //alert((self.$store.state.userRoles.find(r => r === "TRAINER"))); //self.$store.getters.isTrainer
            console.log(self);
            /*if(!((self.$store.state.userRoles.find(r => r === "TRAINER")) || self.isAdmin)){
                alert("You don`t have permission");
                self.$router.push("/");
            }*/
        },

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