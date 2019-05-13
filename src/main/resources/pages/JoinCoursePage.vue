<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div class="row">
        <v-container fluid grid-list-md>
            <v-layout row wrap>
                <v-flex d-flex style="margin-bottom: 50px">
                    <div>
                        {{activeSuitability}}
                        <h2>Desired Schedule for " {{this.course.name}} "</h2>
                        <v-select
                                :items="suitabilities"
                                label="Select Suitability"
                                solo
                                item-value="priority"
                                v-model="activeSuitability"
                                item-text="title"
                                color="color"
                        ></v-select>
                        <!--                        <table class="zui-table ">-->
                        <!--                            <thead class="thead-dark">-->
                        <!--                            <tr>-->
                        <!--                                <th v-for="am in headers" scope="col">{{am.text}}</th>-->
                        <!--                            </tr>-->
                        <!--                            </thead>-->
                        <!--                            <tbody>-->
                        <!--                            <tr v-for="(item, key) in  desiredSchedule" :key="key">-->
                        <!--                                <td>{{ days[key] }}</td>-->
                        <!--                                <td v-for="(value, index) in item"-->
                        <!--                                    v-bind:style="{ backgroundColor: getColor(value)}"-->
                        <!--                                    v-on:click="fillWithColor(key, index)">{{value}}-->
                        <!--                                </td>-->
                        <!--                            </tr>-->
                        <!--                            </tbody>-->
                        <!--                        </table>-->

                        <v-data-table
                                :headers="headers"
                                :items="desiredSchedule2"
                                class="elevation-1"
                                item-key="day"
                                hide-actions
                        >
                            <template v-slot:items="props">
                                <td>{{ days[props.item.day] }}</td>
                                <td v-for="(value, index) in props.item.array" style="padding: 0px">
                                    <div class="clickable"
                                         v-bind:style="{ backgroundColor: getColor(value) }"
                                         v-on:click="fillWithColor2(props.item.day, index)"
                                    ></div>
                                </td>
                            </template>
                        </v-data-table>
                        <v-layout>
                            <v-btn alert @click="save">Save</v-btn>
                        </v-layout>
                    </div>
                </v-flex>
            </v-layout>
        </v-container>
    </div>
</template>

<script>
    import axios from 'axios';

    export default {
        name: "JoinCoursePage",
        display: "Table",
        data() {
            return {
                list2: [],
                course: {},
                desiredSchedule: [],
                desiredSchedule2: [],
                dayIntervals: [],
                days: [
                    "MONDAY",
                    "TUESDAY",
                    "WEDNESDAY",
                    "THURSDAY",
                    "FRIDAY",
                    "SATURDAY",
                    "SUNDAY"
                ],
                headers: [{
                    text: 'Day of week',
                    align: 'left',
                    sortable: false,
                    value: 'dayOfWeek'
                }],
                suitabilities: [],
                activeSuitability: null
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
            getColor(i) {
                console.log('looking for ' + i);
                let searched = this.suitabilities.filter((e) => e.priority === i)[0];
                if (searched !== undefined && searched !== null) {
                    console.log(searched.color);
                    return searched.color;
                }
                console.log("not today");
                return "grey";
            },
            save() {
                let self = this;
                for (let i = 0; i < this.groups.length; i++) {
                    axios.post('/groups', self.groups[i])
                        .then(function (response) {
                            console.log(response);
                            self.groups[i].id = response.data;
                            self.successAutoClosable('All groups has been saved')
                        })
                        .catch(function (error) {
                            console.log(error);
                            self.errorAutoClosable(error.response.data);
                        });
                }
            },
            fillWithColor(key, index) {
                // values[i] = this.activeSuitability.priority;
                if (this.activeSuitability !== undefined && this.activeSuitability !== null)
                // this.$set(this.activeSuitability[key], index, this.activeSuitability)
                    this.desiredSchedule[key][index] = this.activeSuitability;
                console.log(this.desiredSchedule);

            },
            fillWithColor2(dayIndex, index) {
                if (this.activeSuitability !== undefined && this.activeSuitability !== null) {
                    let arr = this.desiredSchedule2[dayIndex].array;
                    arr[index] = this.activeSuitability;
                    // this.$set(this.desiredSchedule2[dayIndex].array, index, this.activeSuitability);
                    this.$set(this.desiredSchedule2, dayIndex, {
                        day: dayIndex,
                        array : arr
                    });
                }
                console.log(this.desiredSchedule2);
            }
        },
        mounted() {
            let self = this;
            axios.get('http://localhost:8080/getcourses/desired/day-intervals')
                .then(function (response) {
                    self.dayIntervals = response.data;
                    for (const index of Array(self.days.length).keys()) {
                        self.desiredSchedule.push(
                            Array(self.dayIntervals.length).fill(-1)
                        );
                        self.desiredSchedule2.push({
                                day: index,
                                array: Array(self.dayIntervals.length).fill(-1)
                            }
                        );
                    }
                    self.dayIntervals.forEach((e) => self.headers.push({
                        text: e,
                        align: 'left',
                        sortable: false,
                        value: 'am'
                    }))
                    console.log(self.dayIntervals);
                    console.log(self.desiredSchedule);
                })
                .catch(function (error) {
                    // self.errorAutoClosable(error.response.data);
                    console.log(error);
                });

            axios.get('http://localhost:8080/getcourses/suitabilities')
                .then(function (response) {
                    self.suitabilities = response.data;
                    console.log(self.suitabilities)
                })
                .catch(function (error) {
                    self.errorAutoClosable(error.response.data);
                    console.log(error);
                });
        }
    }
</script>

<style>

    .student-name {
        width: 25%;
        cursor: pointer;
    }

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

    /*noinspection CssUnusedSymbol*/
    .notAttending {
        background-color: #3E4347;
        color: #cfcfcf;
        text-decoration-line: line-through;
    }

    .clickable {
        cursor: pointer;
        width: 100%;
        height: 100%;
        color: black;
    }
</style>

