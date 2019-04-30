<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-toolbar flat color="white">
            <v-toolbar-title>Users</v-toolbar-title>
            <v-spacer></v-spacer>
        </v-toolbar>
        <v-data-table
                :headers="headers"
                :items="allUsers"
                :expand="true"
                item-key="id"
        >
            <template v-slot:items="props">
                <tr>
                    <td @click="goToUserPage(props.item.id)">
                        <v-btn smal flat><span>Page</span></v-btn>
                    </td>
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
    </div>
</template>

<script>
    import axios from 'axios'
    import UserPage from 'UserPage.vue'

    export default {

        name: "AllUsersPage",
        data: function () {
            return {
                headers: [
                    {text: 'Fold/Unfold', sortable: false, width: "20", align: 'center'},
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
                        width: "20", align: 'right'
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
            UserPage
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
</style>