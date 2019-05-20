<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-container>
        <v-layout row wrap>
            <v-flex xs12 sm12 >
                <v-toolbar flat color="white">
                    <v-toolbar-title>Groups</v-toolbar-title>
                </v-toolbar>
            </v-flex>
            <v-flex>
                <v-data-table
                        :headers="headers"
                        :items="groupsAndQuantities"
                        :expand="true"
                        item-key="id"
                        :rows-per-page-items="nums"
                >
                    <template v-slot:items="props">
                        <tr>
                            <td class="my-link">
                                <div @click="">{{ props.item.id }}</div>
                            </td>
                            <td class="my-link clickable">
                                <div @click="goToGroupPage(props.item.id)">{{ props.item.title }}</div>
                            </td>
                            <td class="text-xs-right">{{ props.item.numberOfEmployees }}</td>


                        </tr>
                    </template>
                </v-data-table>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    import axios from 'axios'

    export default {
        name: "AllGroups",
        data: function () {
            return {
                nums : [10,25,{"text":"$vuetify.dataIterator.rowsPerPageAll","value":-1}],
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
            goToGroupPage(id) {
                this.$router.push("/groups/" + id)
            }
        },
        mounted() {
            let self = this;
            axios.get('/api/groups/groups-and-quantity')
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
    .clickable {
        cursor: pointer;
        margin-bottom: 5px;
        margin-top: 5px;
        color: black;
    }
</style>