<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div class="row">
        <progress-circular-component v-if="loading"></progress-circular-component>

        <v-tabs v-model="currentDay" centered>
            <v-tab
                    v-for="n in days"
                    :key="n"
            >
                {{ n }}
            </v-tab>
        </v-tabs>

        <br/>
        <br/>
        <br/>
        <v-container v-if="!loading" fluid >
            <v-layout row wrap>
                <v-flex style="margin-bottom: 50px" lg6 xs12>
                    <div>
                        <h2>All ungrouped students for a course " {{course.name}} "</h2>
                        <table class="zui-table ">
                            <thead class="thead-dark">
                            <tr>
                                <th scope="col">Id</th>
                                <th scope="col">Attending</th>
                                <th scope="col" class="student-name">Name</th>
                                <th v-for="dayName in dayIntervals" scope="col">{{dayName}}</th>
                            </tr>
                            </thead>
                            <draggable v-model="allSchedules" tag="tbody" group="people" >
                                <tr v-for="item in allSchedules" :key="item.userId">
                                    <td>{{ item.userId }}</td>
                                    <td style="width: 5%">
                                        <v-switch v-model="item.isAttending"
                                                  class="ma-0 pa-0 text-xs-center justify-center"
                                                  @change="invertAttending(item.id, item.userName)"></v-switch>
                                    </td>
                                    <td class="student-name"
                                        v-bind:class="{'notAttending': !item.isAttending}">{{ item.userName }}
                                    </td>
                                    <td v-for="forInterval in item.scheduleForIntervals"
                                        v-bind:style="{backgroundColor: getColor(forInterval)}">
                                    </td>
                                </tr>
                            </draggable>
                        </table>
                        <v-layout>
                            <v-btn alert @click="addGroup">Add group</v-btn>
                            <v-spacer></v-spacer>
                            <v-btn v-if="groups.length > 0" alert @click="saveAll">Save All</v-btn>
                        </v-layout>
                    </div>
                </v-flex>
                <v-flex lg6 xs12>
                    <div>
                        <v-expansion-panel
                                v-model="panel"
                                expand
                        >
                            <v-expansion-panel-content
                                    v-for="(group, index) in groups"
                                    :key="index"
                                    style="margin-bottom:30px; padding: 10px"

                            >
                                <template v-slot:header>
                                    <div>Drag and drop here students for a group number {{index}}</div>
                                </template>
                                <div>
                                    <table class="zui-table ">
                                        <thead class="thead-dark">
                                        <tr>
                                            <th scope="col">Id</th>
                                            <th scope="col">Attending</th>
                                            <th scope="col" class="student-name">Name</th>
                                            <th v-for="dayName in dayIntervals" scope="col">{{dayName}}</th>
                                        </tr>
                                        </thead>
                                        <draggable v-model="group.groupScheduleList" tag="tbody" group="people" >
                                            <tr v-for="item in group.groupScheduleList" :key="item.userId"
                                                style="cursor: pointer">
                                                <td>{{ item.userId }}</td>
                                                <td style="width: 5%">
                                                    <v-switch v-model="item.isAttending"
                                                              class="ma-0  pa-0  text-xs-center justify-center"
                                                              @change="invertAttending(item.id, item.userName)"></v-switch>
                                                </td>
                                                <td class="student-name" v-bind:class="{'notAttending': !item.isAttending}">
                                                    {{ item.userName }}
                                                </td>
                                                <td v-for="forInterval in item.scheduleForIntervals"
                                                    v-bind:style="{backgroundColor: getColor(forInterval)}">
                                                </td>
                                            </tr>
                                        </draggable>
                                    </table>
                                    <v-layout>
                                        <v-flex>
                                            <v-text-field v-model="group.name"
                                                          placeholder="Name"
                                                          label="Group Name"
                                                          required
                                                          :data-vv-name="'group_' + index"
                                                          v-validate="'required|max:256'"
                                                          :error-messages="errors.collect('group_' + index)"
                                            ></v-text-field>
                                            <span>{{ errors.first('myinput') }}</span>
                                        </v-flex>
                                        <v-spacer></v-spacer>
                                        <v-btn alert color="error" @click="deleteGroup(index)">Delete Group</v-btn>
                                        <v-btn alert color="success" @click="saveGroup(index)">Save Group</v-btn>
                                    </v-layout>

                                </div>
                            </v-expansion-panel-content>
                        </v-expansion-panel>
                    </div>
                </v-flex>
            </v-layout>
        </v-container>
    </div>
</template>

<script>
    // noinspection NpmUsedModulesInstalled
    import draggable from "vuedraggable";
    import axios from 'axios';
    import store from "../store/store.js";
    import ProgressCircularComponent from "../components/ProgressCircularComponent.vue";

    export default {
        name: "StudentLineInTable",
        display: "Table",
        order: 8,
        components: {draggable, ProgressCircularComponent},
        data() {
            return {
                loading: false,
                list2: [],
                panel : [],
                errorMessages: '',
                formHasErrors: false,
                course: {},
                dragging: false,
                currentDay: 0,
                days: [
                    "MONDAY",
                    "TUESDAY",
                    "WEDNESDAY",
                    "THURSDAY",
                    "FRIDAY",
                    "SATURDAY",
                    "SUNDAY"
                ],
                dayIntervals: [],
                allSchedules: [],
                groups: []
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
            invertAttending(id, userName) {
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/desired-schedule/invert-attending/' + id)
                    .then(function (response) {
                        self.successAutoClosable('Employee  ' + userName + ' now updated')
                    })
                    .catch(function (error) {
                        console.log(error);
                        self.errorAutoClosable(error.response.data);
                    });
            },
            getColor(forInterval) {
                return forInterval.colorsForWeekDays[this.currentDay];
            },
            addGroup() {
                this.groups.push({
                    id: 0,
                    name: "",
                    courseId: this.$route.params.id,
                    groupScheduleList: []
                })
            },
            saveGroup(index) {
                let self = this;
                this.$validator.validate('group_' + index, self.groups[index].name).then((result) => {
                    if (result) {
                        axios.post(this.$store.state.apiServer + '/api/desired-schedule', self.groups[index])
                            .then(function (response) {
                                self.groups[index].id = response.data;
                                self.successAutoClosable('Group  ' + self.groups[index].name + ' has been saved')
                            })
                            .catch(function (error) {
                                console.log(error);
                                self.errorAutoClosable(error.response.data);
                            });
                        return;
                    }
                    if (!result) {
                        self.errorAutoClosable("Valid group name required")
                    }
                });

            },
            deleteGroup(index) {
                let self = this;
                axios.delete(this.$store.state.apiServer + '/api/desired-schedule/' + self.groups[index].id)
                    .then(function (response) {
                        self.allSchedules = self.allSchedules.concat(self.groups[index].groupScheduleList);
                        self.successAutoClosable('Group ' + self.groups[index].name + ' has been deleted');
                        self.groups.splice(index, 1);
                        self.panel.splice(index, 1);
                    })
                    .catch(function (error) {
                        console.log(error);
                        self.errorAutoClosable(error.response.data);
                    });

            },
            saveAll: function () {
                let self = this;
                this.$validator.validateAll().then((result) => {
                    if (result) {
                        for (let i = 0; i < this.groups.length; i++) {
                            axios.post(this.$store.state.apiServer + '/api/desired-schedule', self.groups[i])
                                .then(function (response) {
                                    console.log(response);
                                    self.groups[i].id = response.data;
                                    self.successAutoClosable('Group ' + self.groups[i].name + ' has been saved');
                                })
                                .catch(function (error) {
                                    console.log(error);
                                    self.errorAutoClosable(error.response.data);
                                });
                        }
                        return;
                    }
                    if (!result) {
                        self.errorAutoClosable("Valid group name required")
                    }
                });
            }
        },
        mounted() {
            this.$validator.localize('en', this.dictionary);
            if (store.getters.isAdmin) {
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/desired-schedule/' + self.$route.params.id + '/ungrouped')
                    .then(function (response) {
                        self.allSchedules = response.data;
                        self.loading = false;
                    })
                    .catch(function (error) {
                        // self.errorAutoClosable(error.response.data);
                        console.log(error);
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                    });


                axios.get(this.$store.state.apiServer + '/api/desired-schedule/' + self.$route.params.id + '/grouped')
                    .then(function (response) {
                        self.groups = response.data;
                        self.groups.forEach(function () {
                            self.panel.push(false);
                        })
                    })
                    .catch(function (error) {
                        // self.errorAutoClosable(error.response.data);
                        console.log(error);
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                    });
                axios.get(this.$store.state.apiServer + '/api/desired-schedule/day-intervals')
                    .then(function (response) {
                        self.dayIntervals = response.data;
                    })
                    .catch(function (error) {
                        // self.errorAutoClosable(error.response.data);
                        console.log(error);
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                    });
                axios.get(this.$store.state.apiServer + '/api/getcourses/' + self.$route.params.id)
                    .then(function (response) {
                        self.course = response.data;
                        console.log(response.data)
                    })
                    .catch(function (error) {
                        // self.errorAutoClosable(error.response.data);
                        console.log(error);
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                    });
            } else {
                this.$router.push('/403')
            }
        }
    }
</script>

<style>

    .student-name {
        width: 25%;
        cursor: pointer;
        padding: 5px;
    }

    .zui-table {
        border: solid 1px #e6e4ee;
        border-collapse: collapse;
        border-spacing: 0;
        font: normal 10px Arial, sans-serif;
        /*width: 100%;*/
        margin-top: 20px;
        margin-bottom: 30px;
    }

    .zui-table thead th {
        background-color: #eaefed;
        border: solid 1px #eeecec;
        color: #6a6b68;
        padding: 3px;
        text-align: left;
        text-shadow: 1px 1px 1px #fff;
    }

    .zui-table tbody td {
        border: solid 1px #e9eeeb;
        color: #333;
        padding-left: 3px;
        text-shadow: 1px 1px 1px #fff;
    }

    .zui-table-vertical tbody td {
        border-top: none;
        border-bottom: none;
    }

    /*noinspection CssUnusedSymbol*/
    .notAttending {
        background-color: #3E4347;
        color: #cfcfcf;
        text-decoration-line: line-through;
    }
</style>
