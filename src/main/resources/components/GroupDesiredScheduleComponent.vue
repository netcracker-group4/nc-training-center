<template>
    <div>
        <v-tabs v-model="currentDay" centered>
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
                                <tr v-for="item in allSchedules" :key="item.userId" style="cursor: pointer">
                                    <td>{{ item.userId }}</td>
                                    <td class="student-name" v-bind:class="{'notAttending': !item.attending}"
                                        v-bind:style="{width: '30%'}">{{ item.userName }}
                                    </td>
                                    <td v-for="forInterval in item.scheduleForIntervals"
                                        v-bind:style="{backgroundColor: getColor(forInterval)}">
                                        {{item.isAttending}}
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
    import store from "../store/store.js";

    export default {
        name: "groupSchedule",
        order: 8,
        props: ['groupId'],
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
                return forInterval.colorsForWeekDays[this.currentDay];
            },
            errorAutoClosable(title) {
                this.$snotify.error(title, {
                    timeout: 2000,
                    showProgressBar: false,
                    closeOnClick: false,
                    pauseOnHover: true
                });
            },
        },
        mounted() {
            if (store.getters.isAdmin || store.getters.isTrainer) {
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/desired-schedule/' + this.newGroupId)
                    .then(function (response) {
                        self.allSchedules = response.data;
                        console.log(response.data);
                    })
                    .catch(function (error) {
                        console.log(error);
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                        // self.errorAutoClosable(error.response.data);
                    });
                axios.get(this.$store.state.apiServer + '/api/desired-schedule/day-intervals')
                    .then(function (response) {
                        self.dayIntervals = response.data;
                    })
                    .catch(function (error) {
                        console.log(error);
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                        // self.errorAutoClosable(error.response.data);
                    });
            }
        },
        computed: {
            newGroupId() {
                return this.groupId;
            }
        }
    }
</script>

<style scoped>
    /*noinspection CssUnusedSymbol*/
    .notAttending {
        background-color: #3E4347;
        color: #cfcfcf;
        text-decoration-line: line-through;
    }
</style>