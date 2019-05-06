<template>
    <div class="row">
        <v-tabs fixed-tabs v-model="currentDay" centered>
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
        <v-container fluid grid-list-md>
            <v-layout row wrap>
                <v-flex d-flex style="margin-bottom: 50px">
                    <div>
                        <h2>All ungrouped students for a course " {{this.course.name}} "</h2>
                        <table class="zui-table ">
                            <thead class="thead-dark">
                            <tr>
                                <th scope="col">Id</th>
                                <th scope="col" v-bind:style="{width: '30%'}">Name</th>
                                <th v-for="dayName in dayIntervals" scope="col">{{dayName}}</th>
                            </tr>
                            </thead>
                            <draggable v-model="allSchedules" tag="tbody" group="people">
                                <tr v-for="item in allSchedules" :key="item.userId">
                                    <td>{{ item.userId }}</td>
                                    <td v-bind:style="{width: '30%'}">{{ item.userName }}</td>
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
                <v-flex d-flex>
                    <div>
                        <div style="margin-bottom: 30px" v-for="(group, index) in groups">
                            <h2>Drag and drop here students for a group number {{index}}</h2>
                            <div>
                                <table class="zui-table ">
                                    <thead class="thead-dark">
                                    <tr>
                                        <th scope="col">Id</th>
                                        <th scope="col" v-bind:style="{width: '30%'}">Name</th>
                                        <th v-for="dayName in dayIntervals" scope="col">{{dayName}}</th>
                                    </tr>
                                    </thead>
                                    <draggable v-model="group.groupScheduleList" tag="tbody" group="people">
                                        <tr v-for="item in group.groupScheduleList" :key="item.userId">
                                            <td>{{ item.userId }}</td>
                                            <td v-bind:style="{width: '30%'}">{{ item.userName }}</td>
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
                                        ></v-text-field>
                                    </v-flex>
                                    <v-spacer></v-spacer>
                                    <v-btn alert color="error" @click="deleteGroup(index)">Delete Group</v-btn>
                                    <v-btn alert  color="success" @click="saveGroup(index)">Save Group</v-btn>
                                </v-layout>

                            </div>
                            <br/>
                            <br/>
                            <br/>
                        </div>
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

    export default {
        name: "StudentLineInTable",
        display: "Table",
        order: 8,
        components: {draggable},
        data() {
            return {
                list2: [],
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
            getColor(forInterval) {
                return forInterval.colorsForDays[this.currentDay];
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
                axios.post('/groups', self.groups[index])
                    .then(function (response) {
                        self.groups[index].id = response.data;
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
            },
            deleteGroup(index) {
                let self = this;
                axios.delete('/groups/' + self.groups[index].id)
                    .then(function (response) {
                        self.allSchedules = self.allSchedules.concat(self.groups[index].groupScheduleList);
                        self.groups.splice(index, 1);
                    })
                    .catch(function (error) {
                        console.log(error);
                    });

            },
            saveAll() {
                console.log("saving all groups");
                let self = this;
                for (let i = 0; i < this.groups.length; i++) {
                    axios.post('/groups', self.groups[i])
                        .then(function (response) {
                            console.log(response);
                            self.groups[i].id = response.data;
                        })
                        .catch(function (error) {
                            console.log(error);
                        });
                }
            }
        },
        mounted() {
            let self = this;
            axios.get('http://localhost:8080/getcourses/' + self.$route.params.id + '/desired/ungrouped')
                .then(function (response) {
                    self.allSchedules = response.data;
                })
                .catch(function (error) {
                    console.log(error);
                });
            axios.get('http://localhost:8080/getcourses/' + self.$route.params.id + '/desired/grouped')
                .then(function (response) {
                    self.groups = response.data;
                })
                .catch(function (error) {
                    console.log(error);
                });
            axios.get('http://localhost:8080/getcourses/desired/day-intervals')
                .then(function (response) {
                    self.dayIntervals = response.data;
                })
                .catch(function (error) {
                    console.log(error);
                });
            axios.get('http://localhost:8080/getcourses/' + self.$route.params.id)
                .then(function (response) {
                    self.course = response.data;
                })
                .catch(function (error) {
                    console.log(error);
                });
        }
    }
</script>

<style>


    .zui-table {
        border: solid 1px #e6e4ee;
        border-collapse: collapse;
        border-spacing: 0;
        font: normal 13px Arial, sans-serif;
        width: 100%;
        margin-top: 20px;
        margin-bottom: 30px;
    }

    .zui-table thead th {
        background-color: #eaefed;
        border: solid 1px #eeecec;
        color: #6a6b68;
        padding: 10px;
        text-align: left;
        text-shadow: 1px 1px 1px #fff;
    }

    .zui-table tbody td {
        border: solid 1px #e9eeeb;
        color: #333;
        padding: 10px;
        text-shadow: 1px 1px 1px #fff;
    }

    .zui-table-vertical tbody td {
        border-top: none;
        border-bottom: none;
    }

</style>
