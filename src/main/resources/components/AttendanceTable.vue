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
                                <v-select v-model="editedItem.reason" :items="reasons" label="Change reason"
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
        <v-data-table
                :headers="headers"
                :items="attendances"
                :rows-per-page-items="[10, 5, 20]"
        >
            <template v-slot:items="props">
                <td>{{ props.item.studentFirstName + ' ' + props.item.studentLastName}}</td>
                <td class="text-xs-left">{{ props.item.topic}}</td>
                <td class="text-xs-left">{{ props.item.timeDate}}</td>
                <td class="text-xs-left">{{ props.item.status }}</td>
                <td class="text-xs-left">{{ props.item.reason }}</td>
                <td class="justify-center layout px-0">
                    <v-icon v-if="isAdmin | isTrainer | isManager" small class="mr-2" @click="editItem(props.item)">edit</v-icon>
                </td>
            </template>
        </v-data-table>
    </div>
</template>

<script>
    import axios from 'axios'

    export default {
        name: "AttendanceTable",
        props: ['userId', 'groupId'],
        data(){
            return {
                dialog: false,
                isAdmin: this.$store.getters.isAdmin,
                isTrainer: this.$store.getters.isTrainer,
                isManager : this.$store.getters.isManager,
                headers: [
                    {
                        text: 'Student name',
                        align: 'left',
                        sortable: false,
                        value: 'name'
                    },
                    {text: 'Topic', value: 'topic', align: 'left', sortable: false,},
                    {text: 'Date', value: 'date', sortable: false,},
                    {text: 'Attendance', value: 'attendance', sortable: false,},
                    {text: 'Reason', value: 'reason', sortable: false,},
                    {text: 'Actions', value: 'name', sortable: false}
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
            }
        },

        watch: {
            dialog(val) {
                val || this.close()
            }
        },

        methods: {
            editItem(item) {
                this.editedItem.attendanceId = item.attendanceId
                //this.editedItem.status = this.statuses.find(status => status.title == item.status)
                // this.editedItem.reason = this.reasons.find(reason => reason.title == item.reason)
                this.dialog = true
            },
            close() {
                this.editedItem = []
                this.dialog = false
            },
            save() {
                let self = this
                if(self.editedItem.attendanceId != null & self.editedItem.status != null) {
                let attendanceId = self.editedItem.attendanceId
                let statusId = self.editedItem.status.id
                let reasonId = null

                    if (self.editedItem.reason == null || self.editedItem.reason == undefined) {
                        reasonId = null
                    } else {
                        reasonId = self.editedItem.reason.id
                    }
                    let form = new FormData();
                    let request = new XMLHttpRequest();
                    request.open('PUT', this.$store.state.apiServer + '/api/attendances')
                    form.append('attendanceId', attendanceId);
                    form.append('statusId', statusId);
                    form.append('absenceId', reasonId);
                    request.send(form);

                    request.onloadend = function () {
                        if (request.status == 200) {
                            axios.get(self.$store.state.apiServer + '/api/attendances?userId=' + self.userId + '&groupId=' + self.groupId)
                                .then(response => self.attendances = response.data)
                                .catch(error => console.log(error))
                        }
                        if (request.status == 404) {
                            //self.modalMessage = "There is no user with such email and password"
                            //self.dialog = true
                        }
                    };
                    console.log(this.editedItem)
                    this.close()
                }
            },
        },
        mounted() {
            if (this.groupIdNew != undefined) {
                axios.get(this.$store.state.apiServer + '/api/attendances?userId=' + this.userId + '&groupId=' + this.groupIdNew)
                    .then(response => this.attendances = response.data)
                    .catch(error => console.log(error))

                axios.get(this.$store.state.apiServer + '/api/attendance-status')
                    .then(response => this.statuses = response.data)
                    .catch(error => console.log(error))
            }

            axios.get(this.$store.state.apiServer + '/api/absence-reason')
                .then(response => this.reasons = response.data)
                .catch(error => console.log(error))

        },
        computed: {
            groupIdNew() {
                return this.groupId;
            }
        }
    }
</script>

<style scoped>

</style>