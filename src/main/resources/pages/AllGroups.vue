<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-toolbar flat color="white">
            <v-toolbar-title>Groups</v-toolbar-title>

        </v-toolbar>
        <v-data-table
                :headers="headers"
                :items="groupsAndQuantities"
                :expand="true"
                item-key="course.id"
        >
            <template v-slot:items="props">
                <tr>
                    <td class="my-link">
                        <div @click="" >{{ props.item.group.id }}</div>
                    </td>
                    <td class="my-link">
                        <div @click="goToGroupPage(props.item.group.id)" >{{ props.item.group.title }}</div>
                    </td>
                    <td class="text-xs-right">{{ props.item.numberOfEmployees }}</td>


                </tr>
            </template>
        </v-data-table>
    </div>
</template>

<script>
    import axios from 'axios'

    export default {
        name: "AllGroups",
        data: function(){
            return {
                headers: [
                    {
                        text: 'Group id',
                        align: 'left',
                        value: 'id'
                    },
                    {
                        text: 'Group title', value: 'title'
                    },
                    {
                        text: 'Amount of students', value: 'quantityOfEmployees',
                        width: "20", align: 'right'
                    }
                ],
                groupsAndQuantities: [],
                isAdmin: this.$store.getters.isAdmin
            }
        },
        methods: {
            goToGroupPage(id){
                this.$router.push("/group/"+id)
            }
        },
        mounted(){
            let self = this;
            axios.get('http://localhost:8080/groups/groups-and-quantity')
                .then(function (response) {
                    self.groupsAndQuantities = response.data;
                })
                .catch(function (error) {
                    console.log(error);
                });
        }
    }
</script>

<style scoped>

</style>