<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
    <div>Group with  {{ $route.params.id }}  number</div><v-data-table
            :headers="headers"
            :items="students"
            :expand="true"
            item-key="id"
    >
        <template v-slot:items="props">
            <tr>
                <td class="my-link">
                    <div>{{ props.item.id }}</div>
                </td>
                <td class="text-xs-right">{{ props.item.firstName +' '+ props.item.lastName }}</td>
                <td class="text-xs-right" v-if="isAdmin">
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
        name: "GroupPage",
        data: function(){
            return{
                id: this.$route.params.id,
                students: [],
                teacher: null,
                course: null,
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
            }
        },
        mounted(){
            let self = this;
            axios.get('http://localhost:8080/groups/'+self.id+'/users')
                .then(function (response) {
                    self.students = response.data;
                });
            axios.get('http://localhost:8080/groups/'+self.id+'/users')
                .then(function (response) {
                    self.students = response.data;
                });
            axios.get('http://localhost:8080/groups/'+self.id+'/course')
                .then(function (response) {
                    self.course = response.data;
                });
            console.log(self);
        }
    }
</script>

<style scoped>

</style>