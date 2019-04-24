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
        <h2>All Students for a course " {{this.course.name}} "</h2>
        <div class="col-12">
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


        <br/>
        <br/>
        <br/>
        <br/>
        <h2>Drag and drop here students for a group</h2>
        <div class="col-12">
            <table class="zui-table ">
                <thead class="thead-dark">
                <tr>
                    <th scope="col">Id</th>
                    <th scope="col" v-bind:style="{width: '30%'}">Name</th>
                    <th v-for="dayName in dayIntervals" scope="col">{{dayName}}</th>
                </tr>
                </thead>
                <draggable v-model="list2" tag="tbody" group="people">
                    <tr v-for="item in list2" :key="item.userId">
                        <td>{{ item.userId }}</td>
                        <td v-bind:style="{width: '30%'}">{{ item.userName }}</td>
                        <td v-for="forInterval in item.scheduleForIntervals"
                            v-bind:style="{backgroundColor: getColor(forInterval)}">
                        </td>
                    </tr>
                </draggable>
            </table>
        </div>
    </div>
</template>

<script>
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
                allSchedules: []
            }

        },
        methods: {
            addGroup() {
                this.groups.push({name: '', students: []});
            },
            getColor(forInterval){
                console.log(forInterval);
                console.log(forInterval[this.currentDay]);
                return forInterval.colorsForDays[this.currentDay];
            }
        },
        mounted() {
            let self = this;
            axios.get('http://localhost:8080/getcourses/' + self.$route.params.id + '/desired')
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
