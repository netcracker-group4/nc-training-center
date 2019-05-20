<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-container>
        <v-layout row wrap style="margin-bottom: 50px">
            <v-layout style="margin-bottom: 50px">
                <h2>All problems </h2>
                <v-spacer></v-spacer>
                <v-btn alert @click="$router.push('/requests/new')" v-if="isStudent()">Open new Request</v-btn>
            </v-layout>
            <v-flex xs12 sm12>
                <v-tabs v-model="currentStatus" centered style="width: 100%; margin-bottom: 50px">
                    <v-tab
                            v-for="n in statuses"
                            :key="n.id"
                    >
                        {{ n.title }}
                    </v-tab>
                </v-tabs>
            </v-flex>
            <v-flex xs12 sm12>
                <v-data-table
                        :headers="headers"
                        :items="problemsWithStatus"
                        class="elevation-1"
                        style="margin-bottom: 50px"
                >
                    <template v-slot:items="props">
                        <td v-on:click="$router.push('/users/' + props.item.studentId)">{{ props.item.studentId }}
                        </td>
                        <td class="text-xs-right">{{ props.item.description }}</td>
                        <td class="text-xs-right">
                            <v-btn color="success">Open Chat</v-btn>
                        </td>
                        <td class="text-xs-right" v-if="isStudent()">
                            <v-btn color="warning" :disabled="canMakeAnswered(props.item.status)">
                                Answered
                            </v-btn>
                        </td>
                    </template>
                </v-data-table>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    import axios from "axios";

    export default {
        name: "InfodeskPage",
        data: function () {
            return {
                statuses: [{
                    id: 0,
                    title: 'All',
                    description: 'all requests'
                }],
                problems: [],
                currentStatus: 0
            }
        },
        methods: {
            loadInfo() {
                let self = this;
                axios.get('/api/requests/statuses')
                    .then(function (response) {
                        response.data.forEach(function (e) {
                            self.statuses.push(e);
                        });
                        console.log(response.data);
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
                let all = [
                    {
                        id: 1,
                        studentId: 22,
                        description: 'desc1',
                        message: 'message',
                        status: 2
                    }, {
                        id: 2,
                        studentId: 20,
                        description: 'desc2',
                        message: 'message2',
                        status: 1
                    }, {
                        id: 3,
                        studentId: 16,
                        description: 'desc3',
                        message: 'message3',
                        status: 3
                    }, {
                        id: 4,
                        studentId: 20,
                        description: 'desc4',
                        message: 'message4',
                        status: 2
                    }, {
                        id: 5,
                        studentId: 16,
                        description: 'desc5',
                        message: 'message5',
                        status: 1
                    }];
                console.log(this.$store.state.user.roles.includes('ADMIN'));
                if (this.$store.state.user.roles.includes('ADMIN')) {
                    ///get all for admin
                    this.problems = all;
                } else {
                    // get just for this employeee
                    // this.$store.state.user.id
                    let self = this;
                    this.problems = all.filter(function (e) {
                        return parseInt(e.studentId) === parseInt(self.$store.state.user.id)
                    });
                }
            },
            isStudent() {
                return this.$store.state.user.roles.includes('EMPLOYEE')
            },
            canMakeAnswered(status) {
                let status2 = this.statuses.filter(function (e) {
                    return parseInt(e.id) === parseInt(status)
                })[0];
                if (status2 !== undefined) {
                    return !(this.$store.state.user.roles.includes('EMPLOYEE') &&
                        (status2.title === 'open' || status2.title === 'reopened'))
                } else return true;
            }
        },
        mounted() {
            // /requests/statuses
            if (this.$store.state.user.roles.includes('ADMIN') || this.isStudent()) {
                this.loadInfo();
            } else this.$router.push('/404');
        },
        computed: {
            problemsWithStatus() {
                if (this.currentStatus == 0) {
                    return this.problems;
                } else {
                    let self = this;
                    return this.problems.filter(function (e) {
                        return parseInt(e.status) === self.currentStatus;
                    })
                }
            },
            headers() {
                let h = [
                    {text: 'StudentName', align: 'left', value: 'name'},
                    {text: 'Description', align: 'right', value: 'description'},
                    {text: 'Open chat', align: 'right', sortable: false, value: 'button'}
                ];
                if (this.isStudent()) {
                    h.push({text: 'Answered', align: 'right', sortable: false, value: 'button'});
                }
                return h;
            }
        }
    }
</script>

<style scoped>

</style>