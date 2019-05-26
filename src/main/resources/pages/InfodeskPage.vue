<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-container v-if="!loading">
        <v-layout row wrap style="margin-bottom: 50px">
            <v-layout style="margin-bottom: 50px">
                <h2>All problems </h2>
                <v-spacer></v-spacer>
                <infodesk-request v-if="!isAdmin()" :problem="{description: '', message: '', studentId: 0}"></infodesk-request>
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
                                :key="componentKey"

                        >
                            <template v-slot:items="problems">

                                <td v-if="isAdmin()" v-on:click="$router.push('/users/' + problems.item.studentId)">
                                    {{ problemsMap.get(problems.item) }}
                                </td>

                                <td class="text-xs-right">{{problems.item.description}}</td>

                                <td class="text-xs-right" v-if="n.title !== 'draft'">
                                    <v-btn color="success">Open Chat</v-btn>
                                </td>
                                <td class="text-xs-right" v-else>
                                    <infodesk-request :problem="problems.item"
                                                      @close="forceRerender">
                                    </infodesk-request>
                                </td>

                                <td class="text-xs-right" v-if="isStudent() && n.title !=='answered'">
                                    <v-btn @click="markAsAnswered(problems.item.id)" color="warning"
                                           :disabled="canMakeAnswered(problems.item.status)">
                                        Answered
                                    </v-btn>
                                </td>
                                <td class="text-xs-right" v-if="isStudent() && n.title =='answered'">
                                    <v-btn @click="markAsReopened(problems.item.id)" color="warning">
                                        REOPEN
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
    import InfodeskRequest from '../components/InfodeskRequest.vue';


    export default {
        name: "InfodeskPage",
        components: {
            InfodeskRequest
        },
        data: function () {
            return {
                loading: true,
                statuses: [],
                problemsMap: new Map(),
                problems: [],
                currentStatus: 0,
                componentKey: 0,
                userId: this.$store.state.user.id
            }
        },
        methods: {
            isAdmin() {
                return this.$store.state.user.roles.includes('ADMIN');
            },
            loadStatuses: function () {
                let self = this;
                self.statuses.length = 0;
                axios.get(this.$store.state.apiServer + '/api/requests/statuses')
                    .then(function (response) {
                        response.data.forEach(function (e) {
                            self.statuses.push(e);
                        });
                        if (self.isAdmin()) {
                            for (var i = 0; i < self.statuses.length; i++) {
                                if (self.statuses[i].title === 'draft') {
                                    self.statuses.splice(i, 1);
                                }
                            }
                        }
                    })
                    .catch(function (error) {
                        console.log(error);
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                    });
            },
            loadProblems: function () {
                let self = this;
                self.problems = [];
                if (this.isAdmin()) {
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
                                        self.problems.push(p);
                                    }).catch(function (error) {
                                    console.log(error);
                                    self.errorAutoClosable(error.response.data);
                                })
                            });
                        })
                        .catch(function (error) {
                            console.log(error);
                        });
                } else {
                    axios.get('/api/requests/get-requests-by-user', {
                        params: {
                            userId: self.userId
                        }
                    })
                        .then(function (response) {
                            response.data.forEach(function (e) {
                                self.problems.push(e);
                            })
                        })
                        .catch(function (error) {
                            console.log(error);
                        })
                }
                self.loading = false;
                console.log(self.problems);
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
            },
            dataToShowOnTab(status) {
                let list = [];
                let pr = this.problems;
                pr.forEach(function (e) {
                    if (e.status === status.id) list.push(e);
                });
                return list;
            },
            changeRequestType: function (requestId, type) {
                let self = this;
                let form = new FormData();
                let request = new XMLHttpRequest();
                request.open('PUT', this.$store.state.apiServer + '/api/requests/update-request-type');
                form.append('requestId', requestId);
                form.append('requestType', type);
                request.send(form);
                request.onloadend = function () {
                    self.forceRerender();
                    self.successAutoClosable('Your request is updated');
                    // for (var i = 0; i < self.problems.length; i++) {
                    //     if (self.problems[i].id === ) {
                    //         self.statuses.splice(i, 1);
                    //     }
                    // }
                }

            },
            markAsAnswered(requestId) {
                if (this.title === '' || this.description === '')
                    this.changeRequestType(requestId, 'answered');
                else alert("You can't leave a field empty");

            },
            markAsReopened(requestId) {
                if (this.title === '' || this.description === '')
                    this.changeRequestType(requestId, 'reopened');
                else alert("You can't leave a field empty");
            },
            successAutoClosable(title) {
                this.$snotify.success(title, {
                    timeout: 2000,
                    showProgressBar: false,
                    closeOnClick: false,
                    pauseOnHover: true
                });
            },
            errorAutoClosable(title) {
                this.$snotify.error(title, {
                    timeout: 2000,
                    showProgressBar: false,
                    closeOnClick: false,
                    pauseOnHover: true
                });
            },
            forceRerender() {
                this.loadProblems();
                this.componentKey += 1;
            }
        },
        mounted() {
            if (this.isAdmin() || this.isStudent()) {
                this.loadStatuses();
                this.loadProblems();
            } else this.$router.push('/404');
        },
        computed: {
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