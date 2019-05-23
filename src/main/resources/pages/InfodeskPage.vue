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
                        <v-tab-item
                            v-for="n in statuses"
                            :key="n.id"
                        >
                            <v-data-table
                                    :headers="headers"
                                    :items="dataToShowOnTab(n)"
                                    class="elevation-1"
                                    style="margin-bottom: 50px"
                            >
                                <template v-slot:items="problems">
                                    <td v-if="isAdmin()" v-on:click="$router.push('/users/' + problems.item.studentId)">
                                        {{ problemsMap.get(problems.item) }}</td>
                                    <td class="text-xs-right">{{problems.item.description}}</td>
                                    <td class="text-xs-right">
                                        <v-btn color="success">Open Chat</v-btn>
                                    </td>
                                    <td class="text-xs-right" v-if="isStudent()">
                                        <v-btn @click="markAsAnswered(problems.item.id)" color="warning" :disabled="canMakeAnswered(problems.item.status)">
                                            Answered
                                        </v-btn>
                                    </td>
                                </template>
                            </v-data-table>

                        </v-tab-item>

                </v-tabs>
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
                statuses: [],
                problemsMap: new Map(),
                problems: [],
                currentStatus: 0
            }
        },
        methods: {
            loadInfo: function () {
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/requests/statuses')
                    .then(function (response) {
                        response.data.forEach(function (e) {
                            self.statuses.push(e);
                        });
                        for (var i = 0; i < self.statuses.length; i++) {
                            if (self.statuses[i].title === 'draft') {
                                self.statuses.splice(i, 1);
                            }
                        }
                        console.log(response.data);
                    })
                    .catch(function (error) {
                        console.log(error);
                         if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                    });
                if (this.$store.state.user.roles.includes('ADMIN')) {
                    axios.get(self.$store.state.apiServer + '/api/requests/get-all-requests')
                        .then(function (response) {
                            response.data.forEach(function (p) {
                                let id = p.studentId;
                                self.problems.push(p);
                                axios.get(self.$store.state.apiServer + '/api/users/' + id)
                                    .then(function (response) {
                                        let name = response.data.firstName.toString() + " "
                                            + response.data.lastName.toString();
                                        self.problemsMap.set(p, name);
                                        console.log(self.problems);
                                        self.problems.push (p);
                                    }).catch(function (error) {
                                        console.log(error);
                                        self.errorAutoClosable(error.response.data);
                                    })
                            });
                            console.log (self.problems);
                        })
                        .catch(function (error) {
                            console.log(error);
                        });
                } else {
                    axios.get('/api/requests/get-requests-by-user', {
                        params: {
                            userId: parseInt(self.$store.state.user.id)
                        }
                    })
                        .then(function (response) {
                            self.problems = response.data;
                        })
                        .catch(function (error) {
                            console.log(error);
                        })
                }
            },
            isStudent() {
                return this.$store.state.user.roles.includes('EMPLOYEE')
            },
            isAdmin () {
                return this.$store.state.user.roles.includes('ADMIN');
            },
            canMakeAnswered(status) {
                let status2 = this.statuses.filter(function (e) {
                    return parseInt(e.id) === parseInt(status)
                })[0];
                if (status2 !== undefined) {
                    return !(this.$store.state.user.roles.includes('EMPLOYEE') &&
                        (status2.title === 'open' || status2.title === 'reopened'))
                } else return true;
            },
            dataToShowOnTab (status) {
                let list = [];
                let pr = this.problems;
                pr.forEach(function (e){
                    if (e.status === status.id) list.push(e);
                });
                return list;
            },
            markAsAnswered (requestId) {
                let form = new FormData();

                let request = new XMLHttpRequest();
                request.open('PUT', this.$store.state.apiServer + '/api/requests/change-request-type');
                form.append('requestId', requestId);
                form.append('requestType', 'answered');
                request.send(form);
            }
        },
        mounted() {
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
                let h = [];
                if (this.isAdmin()) {
                    h.push({text: 'StudentName', align: 'left', value: 'name'})
                }
                h.push({text: 'Description', align: 'right', value: 'description'});
                h.push({text: 'Open chat', align: 'right', sortable: false, value: 'button'});
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