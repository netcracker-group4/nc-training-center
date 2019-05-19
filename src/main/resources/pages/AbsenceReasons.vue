<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-container>
        <v-layout row wrap>
            <v-flex xs12 sm6>
            </v-flex>
        </v-layout>
        <v-layout row wrap style="margin-bottom: 50px">
            <v-flex xs12>
                <template>
                    <v-data-table
                            :headers="headers"
                            :items="reasons"
                            :expand="true"
                            item-key="id"
                    >
                        <template v-slot:items="props">
                            <tr>
                                <td class="clickable">
                                    <div>{{ props.item.title }}</div>
                                </td>
                                <td>
                                    <v-btn color="error" @click="deleteReason(props.item.id)">Delete</v-btn>
                                </td>
                            </tr>
                        </template>
                    </v-data-table>
                </template>
            </v-flex>
        </v-layout>
        <v-layout>
            <v-text-field v-model="title"
                          label="New reason title"
                          :data-vv-name="'reason title'"
                          v-validate="'required|max:256'"
                          :error-messages="errors.collect('reason title')"></v-text-field>
            <v-spacer></v-spacer>
            <v-btn color="success" @click="add">Add</v-btn>
        </v-layout>


    </v-container>

</template>

<script>

    import axios from 'axios'

    export default {
        name: "AbsenceReasons",
        data: function () {
            return {
                title: '',
                headers: [
                    {
                        text: 'Absent Reasons',
                        align: 'left',
                        value: 'title'
                    }, {
                        text: 'Delete',
                        align: 'right',
                        value: 'delete'
                    }],
                reasons: [],
                isAdmin: 'true'

            }
        },
        mounted() {
            this.loadInfo();
        },
        watch: {
            '$route'(to, from) {
                this.loadInfo();
            }
        },
        methods: {
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
            deleteReason(idReason) {
                let self = this;
                axios.delete('http://localhost:8080/absence-reason/' + idReason)
                    .then(function (response) {
                        self.reasons = self.reasons.filter(function (e) {
                            return e.id !== idReason;
                        });
                        self.successAutoClosable('Reason has been deleted');
                    })
                    .catch(function (error) {
                        console.log(error);
                        self.errorAutoClosable('Error occurred');
                    });
            },
            loadInfo() {
                let self = this;
                axios.get('http://localhost:8080/absence-reason')
                    .then(function (response) {
                        self.reasons = response.data;
                    })
                    .catch(function (error) {
                        console.log(error);
                        self.errorAutoClosable('Error occurred');
                    });
            },
            add() {
                let self = this;
                this.$validator.validateAll().then((result) => {
                    if (result) {
                        axios.post('http://localhost:8080/absence-reason', {title: this.title})
                            .then(function (response) {
                                self.reasons.push({
                                    id: response.data,
                                    title: self.title
                                });
                                self.successAutoClosable(self.title + ' reason has been added');
                                self.title = '';
                                self.$validator.reset()
                            })
                            .catch(function (error) {
                                self.errorAutoClosable("Couldn't add new reason.\n Try again")
                            });
                        return;
                    }
                    if (!result) {
                        self.errorAutoClosable("Valid absence reason title required")
                    }
                });
            }
        }
    }

</script>

<style>
    .clickable {
        cursor: pointer;
        margin-bottom: 5px;
        margin-top: 5px;
        color: black;
    }
</style>
