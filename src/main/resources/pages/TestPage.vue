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
                            <v-flex xs12 sm6 md4>
                                <!--<v-select
                                        v-model="editUser.dtoManager"
                                        :items="managers"
                                        label="Change manager"
                                >
                                    <template slot="selection" slot-scope="managers">
                                        {{ managers.item.firstName }} {{ managers.item.lastName }}
                                    </template>
                                    <template slot="item" slot-scope="managers">
                                        {{ managers.item.firstName }} {{ managers.item.lastName }}
                                    </template>
                                </v-select>-->
                                <v-text-field v-model="editedItem.status" label="Status"></v-text-field>
                            </v-flex>
                            <v-flex xs12 sm6 md4>
                                <v-text-field v-model="editedItem.reason" label="Reason"></v-text-field>
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
                class="elevation-1"
        >
            <template v-slot:items="props">
                <td>{{ props.item.studentFirstName + ' ' + props.item.studentLastName}}</td>
                <td class="text-xs-left">{{ props.item.topic}}</td>
                <td class="text-xs-left">{{ props.item.timeDate}}</td>
                <td class="text-xs-left">{{ props.item.status }}</td>
                <td class="text-xs-left">{{ props.item.reason }}</td>
                <td class="justify-center layout px-0">
                    <v-icon small class="mr-2" @click="editItem(props.item)">edit</v-icon>
                </td>
            </template>
            <template v-slot:no-data>
                <v-btn color="primary" @click="initialize">Reset</v-btn>
            </template>
        </v-data-table>
    </div>
</template>

<script>
    import axios from 'axios'
    export default {
        name: "TestPage",
        data: () => ({
            dialog: false,
            headers: [
                {
                    text: 'Student name',
                    align: 'left',
                    sortable: false,
                    value: 'name'
                },
                { text: 'Topic', value: 'topic', align: 'left', sortable: false,},
                { text: 'Date', value: 'date', sortable: false,},
                { text: 'Attendance', value: 'attendance', sortable: false, },
                { text: 'Reason', value: 'reason', sortable: false, },
                { text: 'Actions', value: 'name', sortable: false }
            ],
            desserts: [],
            attendances: [],
            editedIndex: -1,
            editedItem: {
                name: '',
                calories: 0,
                fat: 0,
                carbs: 0,
                protein: 0
            },
            defaultItem: {
                name: '',
                calories: 0,
                fat: 0,
                carbs: 0,
                protein: 0
            }
        }),

        computed: {
            formTitle () {
                return this.editedIndex === -1 ? 'New Item' : 'Edit Item'
            }
        },

        watch: {
            dialog (val) {
                val || this.close()
            }
        },

        created () {
            this.initialize()
        },

        methods: {
            initialize () {

            },

            editItem (item) {
                this.editedIndex = this.desserts.indexOf(item)
                this.editedItem = Object.assign({}, item)
                this.dialog = true
            },
            close () {
                this.dialog = false
                setTimeout(() => {
                    this.editedItem = Object.assign({}, this.defaultItem)
                    this.editedIndex = -1
                }, 300)
            },

            save () {
                if (this.editedIndex > -1) {
                    Object.assign(this.desserts[this.editedIndex], this.editedItem)
                } else {
                    this.desserts.push(this.editedItem)
                }
                this.close()
            },
        },
        mounted() {
            let userId = 13;
            let groupId = 1;
            axios.get('http://localhost:8080/attendances?userId='+ userId + '&groupId=' + groupId)
                .then(response => this.attendances = response.data)
                .catch(error => console.log(error))

            console.log(this.attendances)
        }
    }
</script>

<style scoped>

</style>