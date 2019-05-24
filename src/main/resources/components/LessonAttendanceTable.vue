<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-dialog v-model="dialog" max-width="500px">
            <v-card>
                <v-card-title>
                    <span class="headline">Edit</span>
                </v-card-title>

                <v-card-text>
                    <v-container grid-list-md>
                        <v-layout wrap>
                            <v-flex xs12 sm12 md10>
                                <v-select v-model="editedItem.status" :items="statuses" label="Change status">
                                    <template slot="selection" slot-scope="status">
                                        {{ status.item.title}}
                                    </template>
                                    <template slot="item" slot-scope="status">
                                        {{ status.item.title }}
                                    </template>
                                </v-select>
                            </v-flex>
                            <v-flex xs12 sm12 md10>
                                <v-select v-model="editedItem.reason"
                                          :items="reasons" label="Change reason"
                                          :disabled="editedItem.status == null || editedItem.status.id != 2">
                                    <template slot="selection" slot-scope="status">
                                        {{ status.item.title}}
                                    </template>
                                    <template slot="item" slot-scope="status">
                                        {{ status.item.title }}
                                    </template>
                                </v-select>
                            </v-flex>
                        </v-layout>
                    </v-container>
                </v-card-text>

                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue darken-1" flat @click="close">Cancel</v-btn>
                    <v-btn color="blue darken-1" flat @click="save">Save</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
        <v-flex sx12>
            <v-data-table
                    :headers="headers"
                    :items="attendances"
                    :rows-per-page-items="rows"
                    no-data-text="No attendance to show">

                <template v-slot:items="props">
                    <td class="clickable" @click="forwardToUserPage(props.item.userId)">
                        <div>{{ props.item.studentFirstName + ' ' + props.item.studentLastName}}</div>
                    </td>
                    <td class="text-xs-left">{{ props.item.status }}</td>
                    <td class="text-xs-center">{{ props.item.reason }}</td>
                    <td class="justify-center px-0">
                        <v-icon small class="mr-2" @click="editItem(props.item)">edit</v-icon>
                    </td>
                </template>
            </v-data-table>
        </v-flex>
    </div>
</template>

<script>
    import axios from 'axios'

    export default {
        name: "LessonAttendanceTable",
        props: ['lessonId'],
        data: () => ({
            dialog: false,
            headers: [
                {
                    text: 'Student name',
                    align: 'left',
                    sortable: false,
                    value: 'name'
                },
                {text: 'Attendance', value: 'attendance', sortable: false,},
                {text: '', value: 'reason', sortable: false,},
                {text: '', value: 'name', sortable: false}

            ],
            statuses: [],
            reasons: [],
            attendances: [],
            editedIndex: -1,
            editedItem: {
                attendanceId: null,
                status: null,
                reason: null
            },
            rows: [10, {"text": "$vuetify.dataIterator.rowsPerPageAll", "value": -1}]
        }),

        watch: {
            dialog(val) {
                val || this.close()
            }
        },

        methods: {
            editItem(item) {
                this.editedItem.attendanceId = item.attendanceId;
                //this.editedItem.status = this.statuses.find(status => status.title == item.status)
                // this.editedItem.reason = this.reasons.find(reason => reason.title == item.reason)
                this.dialog = true;
            },
            close() {
                this.editedItem = [];
                this.dialog = false;
            },
            save() {
                let self = this;
                let attendanceId = self.editedItem.attendanceId;
                let statusId = self.editedItem.status.id;
                let reasonId = null;
                if (self.editedItem.reason == null || self.editedItem.reason === "undefined") {
                    reasonId = null
                } else {
                    reasonId = self.editedItem.reason.id
                }
                let form = new FormData();
                let request = new XMLHttpRequest();
                request.open('PUT', this.$store.state.apiServer + '/api/attendances');
                form.append('attendanceId', attendanceId);
                form.append('statusId', statusId);
                form.append('absenceId', reasonId);
                request.send(form);
                request.onloadend = function () {
                    if (request.status === 200) {
                        axios.get(self.$store.state.apiServer + '/api/attendances?lessonId=' + self.lessonId)
                            .then(response => self.attendances = response.data)
                            .catch(error => console.log(error))
                    }
                    if (request.status === 404) {
                        //self.modalMessage = "There is no user with such email and password"
                        //self.dialog = true
                    }
                };
                console.log(this.editedItem);
                this.close();
            },
            forwardToUserPage(id) {
                this.$router.push('/users/' + id)
            },
        },
        mounted() {
            axios.get(this.$store.state.apiServer + '/api/attendances?lessonId=' + this.lessonId)
                .then(response => this.attendances = response.data)
                .catch(error =>{
                    if (error.response != null && error.response.status == 400)
                        self.$router.push('/404');
                    console.log(error)
                } );

            axios.get(this.$store.state.apiServer + '/api/attendance-status')
                .then(response => this.statuses = response.data)
                .catch(error => console.log(error));

            axios.get(this.$store.state.apiServer + '/api/absence-reason')
                .then(response => this.reasons = response.data)
                .catch(error => console.log(error));

        }
    }
</script>

<style scoped>

</style>