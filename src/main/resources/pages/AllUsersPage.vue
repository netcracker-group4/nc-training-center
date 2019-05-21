<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-container>
        <v-layout row wrap>
            <v-flex xs12 sm12>
                <v-toolbar flat color="white" v-if="!loading">
                    <v-toolbar-title>Users</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-btn
                            flat
                            color="primary"
                            class="mb-2"
                            @click="goToNewUserPage()"
                            v-if="isAdmin()"
                    >
                        Add new user
                    </v-btn>
                </v-toolbar>
                <progress-circular-component v-if="loading"></progress-circular-component>
                <v-data-table
                        :headers="headers"
                        :items="allUsers"
                        :expand="true"
                        item-key="id"
                        :rows-per-page-items="nums"
                        v-if="!loading"
                >
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
                            <td class="text-xs-right">{{ props.item.active }}
                                <!--<img src="../img/icon/baseline_done_black_18dp.png"/>--></td>
                        </tr>
                    </template>
                </v-data-table>
            </v-flex>
        </v-layout>
    </v-container>

</template>

<script>
    import axios from 'axios';
    import store from '../store/store.js';
    import ProgressCircularComponent from "../components/ProgressCircularComponent.vue";


    export default {

        name: "AllUsersPage",
        data: function () {
            return {
                loading: true,
                nums: [10, 25, {"text": "$vuetify.dataIterator.rowsPerPageAll", "value": -1}],
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
            isAdmin() {
                return store.state.userRoles.includes("ADMIN");
            },
            goToUserPage(userId) {
                // this.$emit("sendId", userId);
                this.$router.push('/users/' + userId);
                window.scrollTo(0, 0);
            },
            goToNewUserPage() {
                this.$router.push('/registration');
            }
        },
        mounted() {
            let self = this;
            axios.get(this.$store.state.apiServer + '/api/admin')
                .then(function (response) {
                    self.allUsers = response.data;
                    self.loading = false;
                    console.log(self.allUsers)
                })
                .catch(function (error) {
                    console.log(error);
                });
        },
        components: {
            ProgressCircularComponent
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