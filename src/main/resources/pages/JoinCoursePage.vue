<!--suppress ALL -->
<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div class="row">
        <v-container fluid grid-list-md>
            <v-layout row wrap>
                <v-flex d-flex style="margin-bottom: 50px">
                    <div>
                        <h2>Desired Schedule for " {{course.name}} "</h2>
                    </div>
                </v-flex>
            </v-layout>
            {{desiredSchedule2}}
            <v-layout row wrap>
                <v-flex d-flex style="margin-bottom: 20px">
                    <v-radio-group v-model="activeSuitability" row>
                        <v-radio v-for="s in suitabilities"
                                 :label="s.title"
                                 :value="s.id"
                                 :key="s.id"></v-radio>
                    </v-radio-group>
                    <v-spacer></v-spacer>
                    <v-btn style="margin-top: 15px" large :color="getColor(activeSuitability)">selected</v-btn>
                </v-flex>
            </v-layout>

            <v-layout row wrap>
                <v-flex d-flex style="margin-bottom: 50px">
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
                </v-flex>
            </v-layout>
            <v-layout row wrap>
                <v-flex d-flex style="margin-bottom: 50px">
                    <v-btn alert @click="save">Save</v-btn>
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
                desiredSchedule2: [],
                dayIntervals: [],
                days: [
                    "MON",
                    "TUE",
                    "WED",
                    "THU",
                    "FRI",
                    "SAT",
                    "SUN"
                ],
                headers: [{
                    text: 'Day',
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
                let searched = this.suitabilities.filter((e) => e.id === i)[0];
                if (searched !== undefined && searched !== null) {
                    return searched.color;
                }
                return "grey";
            },
            save() {
                let self = this;
                console.log({
                    courseId: this.$route.params.id,
                    array: this.desiredSchedule2
                });
                axios.post('/getcourses/desired/', {
                    courseId: this.$route.params.id,
                    forDays: this.desiredSchedule2
                }).then(function (response) {
                        console.log(response);
                        self.successAutoClosable("You successfully joined course!")
                    })
                    .catch(function (error) {
                        console.log(error);
                        self.errorAutoClosable(error.response.data);
                    });

            },
            fillWithColor2(dayIndex, index) {
                if (this.activeSuitability !== undefined && this.activeSuitability !== null) {
                    let arr = this.desiredSchedule2[dayIndex].array;
                    arr[index] = this.activeSuitability;
                    // this.$set(this.desiredSchedule2[dayIndex].array, index, this.activeSuitability);
                    this.$set(this.desiredSchedule2, dayIndex, {
                        day: dayIndex,
                        array: arr
                    });
                }
            },
            loadInfo() {
                let self = this;
                axios.get('http://localhost:8080/getcourses/desired/day-intervals')
                    .then(function (response) {
                        self.dayIntervals = response.data;
                        for (const index of Array(self.days.length).keys()) {
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
                        }));
                        console.log(self.dayIntervals);
                        console.log(self.desiredSchedule);
                    })
                    .catch(function (error) {
                        // self.errorAutoClosable(error.response.data);
                        console.log(error);
                    });
                axios.get('http://localhost:8080/getcourses/' + this.$route.params.id)
                    .then(function (response) {
                        self.course = response.data;
                        console.log(self.course);
                    })
                    .catch(function (error) {
                        // self.errorAutoClosable(error.response.data);
                        console.log(error);
                    });

                axios.get('http://localhost:8080/getcourses/suitabilities')
                    .then(function (response) {
                        self.suitabilities = response.data;
                        if (self.suitabilities.length > 0) {
                            self.activeSuitability = self.suitabilities[0].priority;
                        }
                        console.log(self.suitabilities)
                    })
                    .catch(function (error) {
                        self.errorAutoClosable(error.response.data);
                        console.log(error);
                    });
            }
        },
        mounted() {
            let self = this;
            axios.get('http://localhost:8080/getcourses/' + this.$route.params.id + '/course-group' )
                .then(function (response) {
                    console.log(response.data);
                    if(response.data == true){
                        self.$router.push('/404');
                    }
                    self.loadInfo();
                }).catch(function (error) {
                console.log(error);
                console.log(error.response.data);
                self.errorAutoClosable(error.response.data);
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

