<template>
    <div>
        <v-tabs fixed-tabs v-model="currentDay" centered>
            <v-tab
                    v-for="n in days"
                    :key="n"
            >
                {{ n }}
            </v-tab>
        </v-tabs>
        <v-container fluid grid-list-md style="margin-top: 20px">
            <v-layout row wrap>
                <v-flex d-flex style="margin-bottom: 50px">
                    <div>
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
        name: "groupSchedule",
        order: 8,
        props : ['groupId'],
        components: {draggable},
        data() {
            return {
                list2: [],
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
                allSchedules: []
            }

        },
        methods: {
            getColor(forInterval) {
                return forInterval.colorsForDays[this.currentDay];
            }
        },
        mounted() {
            let self = this;
            axios.get('http://localhost:8080/getcourses/desired/' + this.groupId)
                .then(function (response) {
                    self.allSchedules = response.data;
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
        }
    }
</script>

<style scoped>

</style>