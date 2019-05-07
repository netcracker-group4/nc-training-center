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