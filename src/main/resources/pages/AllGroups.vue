<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-container>
        <progress-circular-component v-if="loading"></progress-circular-component>

        <v-layout v-if="!loading" row wrap>
            <v-flex xs12 sm12>
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
                    <template v-slot:items="props" >
                        <tr @click="goToGroupPage(props.item.id)">
                            <td class="my-link">
                                <div @click="">{{ props.item.id }}</div>
                            </td>
                            <td class="my-link clickable" >
                                <div >{{ props.item.title }}</div>
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
    import ProgressCircularComponent from "../components/ProgressCircularComponent.vue";

    export default {
        name: "AllGroups",
        components: {
            ProgressCircularComponent
        },
        data: function () {
            return {
                nums: [10, 25, {"text": "$vuetify.dataIterator.rowsPerPageAll", "value": -1}],
                loading: true,
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
            axios.get(this.$store.state.apiServer + '/api/groups/groups-and-quantity')
                .then(function (response) {
                    self.groupsAndQuantities = response.data;
                    self.loading = false;
                })
                .catch(function (error) {
                    console.log(error);
                     if (error.response != null && error.response.status == 400)
                        self.$router.push('/404');
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