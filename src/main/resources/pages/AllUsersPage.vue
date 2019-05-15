<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-container>
            <v-layout row wrap>
                <v-flex xs12 sm12>
                    <v-toolbar flat color="white">
                        <v-toolbar-title>Users</v-toolbar-title>
                        <v-spacer></v-spacer>
                        <template>
                            <v-btn color="primary" dark class="mb-2" @click="goToNewUserPage">Add new user</v-btn>
                        </template>
                    </v-toolbar>
                    <v-data-table
                            :headers="headers"
                            :items="allUsers"
                            :expand="true"
                            item-key="id"
                    >v-if
                        <template v-slot:items="props">
                            <tr class="my-link" @click="goToUserPage(props.item.id)">
                                <td>
                                    <div>{{ props.item.firstName }}</div>
                                </td>
                                <td>
                                    <div>{{ props.item.lastName }}</div>
                                </td>
                                <td>
                                    <div v-for="role in props.item.roles" class="role">{{ role }}</div>
                                </td>
                                <td class="text-xs-right">{{ props.item.active }}<!--<img src="../img/icon/baseline_done_black_18dp.png"/>--></td>
                            </tr>
                        </template>
                    </v-data-table>
                </v-flex>
            </v-layout>
        </v-container>
    </div>
</template>

<script>
    import axios from 'axios'
    // import NavigationDrawer from "../components/NavigationDrawer.vue"


    export default {

        name: "AllUsersPage",
        data: function () {
            return {
                headers: [
                    {
                        text: 'First name', value: 'firstName',
                        width: "20", align: 'left'
                    },
                    {
                        text: 'Last name', value: 'lastName',
                        width: "20", align: 'left'
                    },
                    {
                        text: 'Role', value: 'role',
                        width: "20", align: 'left'
                    },
                    {
                        text: 'Is active', value: 'isActive',
                        width: "20", align: 'right'
                    }
                ],
                allUsers: []
            }
        },
        methods: {
            goToUserPage(userId) {
                // this.$emit("sendId", userId);
                this.$router.push('/userpage/' + userId);
                window.scrollTo(0,0);
            },
            goToNewUserPage() {
                this.$router.push('/add-user');
            }
        },
        mounted() {
            let self = this;
            axios.get('http://localhost:8080/admin')
                .then(function (response) {
                    self.allUsers = response.data;
                    console.log(self.allUsers)
                })
                .catch(function (error) {
                    console.log(error);
                });
        },
        components: {
            // NavigationDrawer
        }
    }
</script>

<style scoped>
    .inner-table {
        background-color: lavender;
    }

    .my-link {
        cursor: pointer;
    }
    .role {
        align-items: center;
    }
    .clickable {
        cursor: pointer;
        margin-bottom: 5px;
        margin-top: 5px;
        color: black;
    }
</style>