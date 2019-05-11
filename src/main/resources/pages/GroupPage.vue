<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <div class="subheading pt-3">
            <b>{{course.name}}</b>
            <p>
                <b>Trainer</b>
                <b @click="forwardToTrainerPage(teacher.id)">{{teacher.firstName}} {{teacher.lastName}}</b>
            </p>
        </div>
        <div>Group with  {{ id }}  number</div>
        <v-data-table
            :headers="headers"
            :items="students"
            :expand="true"
            item-key="id"
        >
        <template v-slot:items="props" >
            <tr @click="forwardToUserPage(props.item.id)">
                <td class="my-link">
                    <div>{{ props.item.id }}</div>
                </td>
                <td class="text-xs-left">{{ props.item.firstName +' '+ props.item.lastName }}</td>
                <td class="text-xs-left" v-if="isAdmin">
                    {{props.item.email}}
                </td>
                <td class="text-xs-right" v-if="isAdmin">
                    <v-btn color="error" @click="deleteStudent(props.item.id)" v-if="isAdmin">Delete</v-btn>
                </td>
            </tr>
        </template>
    </v-data-table>
    </div>
</template>

<script>
    import axios from 'axios'

    export default {
        props: ['id'],
        name: "GroupPage",
        data: function(){
            return{
                students: [],
                teacher: [],
                course: [],
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
                        text: 'action',
                        width: "30", align: 'right'
                    }
                ],
                isAdmin: this.$store.getters.isAdmin,
            }
        },
        methods:{
            deleteStudent(id) {
                if (confirm("Are you sure you want to delete " + this.findUserById(id).firstName +' '+ this.findUserById(id).lastName)) {
                    axios.delete('http://localhost:8080/groups/' + self.id + '/user' + id)
                        .then(function (response) {
                        });
                }
            },
            findUserById(id){
                return this.students.find(s => s.id === id);
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
            },
            forwardToUserPage(id){
                this.$router.push('/userpage/' + id)
            },
            forwardToTrainerPage(id){
                this.$router.push('/trainers/' + id)
            }

        },
        mounted(){
            let self = this;
            console.log(self);
            self.setGroup();
            //alert((self.$store.state.userRoles.find(r => r === "TRAINER")));
            console.log(self);
            /*if(!((self.$store.state.userRoles.find(r => r === "TRAINER")) || self.isAdmin)){
                alert("You don`t have permission");
                self.$router.push("/");
            }*/
        },

    }
</script>

<style scoped>

</style>