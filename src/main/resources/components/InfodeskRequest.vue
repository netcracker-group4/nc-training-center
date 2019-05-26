<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-dialog v-model="dialog" max-width="500px">
            <template v-slot:activator="{ on }">
                <v-btn v-if="problem.studentId===0" @click="editItem" style="margin-left: 30px">
                    Open new request
                </v-btn>
                <v-btn v-else @click="editItem" style="margin-left: 30px">
                    Edit
                </v-btn>
            </template>
            <v-card>
                <v-card-title>
                    <span class="headline">New Request</span>
                </v-card-title>
                <v-card-text>
                    <v-container>
                        <v-layout row wrap>
                            <v-flex xs12 sm12>
                                <v-text-field
                                        v-model="editProblem.description"
                                        label="Type a short description of your problem"
                                        :rules="['Required']"
                                ></v-text-field>
                            </v-flex>
                            <v-flex xs12 sm12>

                                <v-text-field
                                        v-model="editProblem.message"
                                        label="Describe your problem in details:"
                                        :rules="['Required']"
                                ></v-text-field>
                            </v-flex>

                        </v-layout>
                    </v-container>
                </v-card-text>

                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue darken-1" flat @click="close">Cancel</v-btn>
                    <v-btn color="blue darken-1" flat @click="save">Save</v-btn>
                    <v-btn color="blue darken-1" flat @click="send">Send</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </div>
</template>

<script>

    import store from "../store/store.js";


    export default {
        name: "infodesk-request",
        props: {problem: {}},
        data() {
            return {
                description: this.problem.description,
                message: this.problem.message,
                dialog: false,
                userId: this.$store.state.user.id,
                editProblem: {
                    description: this.problem.description,
                    message: this.problem.message,
                }
            }
        },
        methods: {
            send() {
                let self = this;
                if (self.title === '' || self.description === '')
                    if (self.problem.studentId === 0)
                        self.createRequest('open');
                    else self.changeRequestType(self.problem.id, 'open');
                else alert ("You can't leave a field empty");
            },
            save() {
                let self = this;
                if (self.title === '' || self.description === '')
                    if (self.problem.studentId === 0)
                        this.createRequest('draft');
                    else this.changeRequestType(self.problem.id, 'draft');
                else alert ("You can't leave a field empty");
            },
            createRequest(type) {
                let form = new FormData();
                let request = new XMLHttpRequest();
                let self = this;
                request.open('POST', self.$store.state.apiServer + '/api/requests/create-request');
                form.append('userId', self.userId);
                form.append('description', self.editProblem.description);
                form.append('message', this.editProblem.message);
                form.append('requestType', type);
                request.send(form);
                request.onloadend = function () {
                    self.successAutoClosable('Your request is updated');
                    console.log(self.editProblem);
                    self.close();
                };
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
                    self.successAutoClosable('Your request is updated');
                    console.log(self.problem);
                    self.close();
                    // for (var i = 0; i < self.problems.length; i++) {
                    //     if (self.problems[i].id === ) {
                    //         self.statuses.splice(i, 1);
                    //     }
                    // }
                }
            },
            isAdmin() {
                return store.getters.isAdmin;
            },
            isUserThisProfile() {
                return store.state.user.id === this.user.id;
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
            editItem() {
                this.description = '';
                this.message = '';
                this.dialog = true;
            },
            close() {
                this.editProblem.description = '';
                this.editProblem.message = '';
                this.dialog = false;
                setTimeout(() => {
                }, 300)
            }
        }
    }

</script>