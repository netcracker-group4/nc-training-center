<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-expansion-panel expand>
            <v-expansion-panel-content>
                <!--suppress HtmlUnknownBooleanAttribute -->
                <template v-slot:header>
                    <div>{{componentHeader}}</div>
                </template>
                <div style="margin: 20px">
                    <v-layout style="margin-bottom: 20px;">
                        <v-radio-group v-model="calendarView" row>
                            <v-radio :key="1" :label="'Calendar'" :value="true"></v-radio>
                            <v-radio :key="2" :label="'List'" :value="false"></v-radio>
                        </v-radio-group>
                        <v-spacer></v-spacer>
                        <v-select v-model="selectedGroups"
                                  item-value="id"
                                  :items="groupsList"
                                  item-text="title"
                                  label="Select groups"
                                  multiple
                        >
                            <template v-slot:selection="{ item, index }">
                                <v-chip v-if="index === 0">
                                    <span>{{ item.title}}</span>
                                </v-chip>
                                <span
                                        v-if="index === 1"
                                        class="grey--text caption"
                                >(+{{ selectedGroups.length - 1 }} others)</span>
                            </template>
                        </v-select>
                    </v-layout>
                    <!--        buttons-->
                    <v-layout style="margin-bottom: 20px">
                        <v-flex sm4 xs12 class="text-sm-left text-xs-center">
                            <v-btn @click="$refs.calendar.prev()">
                                <v-icon dark left>
                                    keyboard_arrow_left
                                </v-icon>
                                Prev
                            </v-btn>
                        </v-flex>
                        <v-flex sm4 xs12 class="text-xs-center">
                            <h2>{{currentMonth}}</h2>
                        </v-flex>
                        <v-flex sm4 xs12 class="text-sm-right text-xs-center">
                            <v-btn @click="$refs.calendar.next()">
                                Next
                                <v-icon right dark>
                                    keyboard_arrow_right
                                </v-icon>
                            </v-btn>
                        </v-flex>
                    </v-layout>

                    <div v-show="calendarView">
                        <!--        calendar-->
                        <v-layout>
                            <v-flex>
                                <v-sheet height="500">
                                    <v-calendar
                                            color="primary"
                                            ref="calendar"
                                            v-model="start">
                                        <template v-slot:day="{ date }">
                                            <template v-for="lesson in lessonsMap[date]">
                                                <v-menu :key="lesson.id" v-model="lesson.open" full-width offset-x>
                                                    <template v-slot:activator="{ on }">
                                                        <div
                                                                v-ripple
                                                                class="my-lesson"
                                                                v-bind:class="{ 'active-lesson': !lesson.isCanceled  , 'canceled-lesson': lesson.isCanceled }"
                                                                v-on="on"
                                                                v-html="lesson.topic"
                                                        >
                                                        </div>
                                                    </template>
                                                    <v-card color="grey lighten-4" min-width="350px" flat>
                                                        <v-toolbar color="primary" dark>
                                                            <h3>{{computedTime(lesson.timeDate.substr(11, 5))}}</h3>
                                                            <v-spacer></v-spacer>
                                                            <h3>{{computedInterval(lesson.duration)}}</h3>
                                                            <v-btn icon flat v-on:click="$router.push('/lessons/' + lesson.id)">
                                                                <v-icon>arrow_forward</v-icon>
                                                            </v-btn>
                                                        </v-toolbar>

                                                        <v-card-title primary-title>
                                                            <div>{{lesson.topic}}</div>
                                                        </v-card-title>

                                                        <v-card-text>
                                                            <span class="clickable"
                                                                  v-on:click="goToTrainerPage(lesson.trainerId)">
                                                                Trainer : {{lesson.trainerName}}</span>
                                                            <v-divider></v-divider>
                                                            <div class="clickable"
                                                                 v-on:click="goToGroupPage(lesson.groupId)">
                                                                Group : {{getGroupName(lesson.groupId)}}
                                                            </div>
                                                            <v-divider></v-divider>
                                                            <div class="clickable"
                                                                 v-on:click="goToCoursePage(getCourseId(lesson.groupId))">
                                                                Course : {{getCourseName(lesson.groupId)}}
                                                            </div>
                                                        </v-card-text>
                                                    </v-card>
                                                </v-menu>
                                            </template>
                                        </template>
                                    </v-calendar>
                                </v-sheet>
                            </v-flex>
                        </v-layout>
                    </div>

                    <div v-show="!calendarView">
                        <div v-for="(lesson, index) in lessonsMapMonthlyGrouped[startMonth]">
                            <v-divider
                                    :key="index" inset
                            ></v-divider>
                            <v-card class="mx-auto card">
                                <v-card-title
                                        v-bind:class="{ 'active-lesson': !lesson.isCanceled  , 'canceled-lesson': lesson.isCanceled }">
                                    <span class="title font-weight-light">
                                        {{getDate(lesson.timeDate) + ' '+ computedTime(lesson.timeDate.substr(11, 5))}}
                                    </span>
                                    <v-spacer></v-spacer>
                                    <span class="title font-weight-light">{{lesson.duration}} </span>
                                    <v-btn icon flat v-on:click="$router.push('/lessons/' + lesson.id)">
                                        <v-icon>arrow_forward</v-icon>
                                    </v-btn>
                                </v-card-title>

                                <v-card-text class="headline font-weight-bold">
                                    <div style="margin-bottom: 20px">{{lesson.topic}}</div>
                                    <v-divider style="margin-top: 20px; margin-bottom: 20px;"></v-divider>
                                    <div class="text-xs-left">
                                        <v-chip v-for="attachment in lesson.attachments" :key="attachment.id">
                                            {{attachment.description}}
                                        </v-chip>
                                    </div>
                                </v-card-text>

                                <v-card-actions>
                                    <span class="clickable title" v-on:click="goToTrainerPage(lesson.trainerId)">
                                                                Trainer : {{lesson.trainerName}}</span>
                                    <v-divider vertical inset class="mx-3"></v-divider>
                                    <div class="clickable title" v-on:click="goToGroupPage(lesson.groupId)">
                                        Group : {{getGroupName(lesson.groupId)}}
                                    </div>
                                    <v-divider vertical inset class="mx-3"></v-divider>
                                    <div class="clickable title"
                                         v-on:click="goToCoursePage(getCourseId(lesson.groupId))">
                                        Course : {{getCourseName(lesson.groupId)}}
                                    </div>
                                </v-card-actions>
                            </v-card>
                        </div>
                    </div>
                </div>
            </v-expansion-panel-content>
        </v-expansion-panel>
    </div>
</template>

<script>
    export default {
        name: "CalendarListScheduleComponent",
        props: ['lessonsList', 'groupsList', "componentHeader"],
        data: function () {
            return {
                start: new Date().toISOString().slice(0, 10),
                calendarView: true,
                selectedGroups: []
            }
        },
        computed: {
            lessonsMap() {
                const map = {};
                let l = this.lessonsList.filter((i) => {
                    return this.selectedGroups.includes(i.groupId);
                });
                l.forEach(e => (map[e.timeDate.substr(0, 10)] = map[e.timeDate.substr(0, 10)] || []).push(e));
                return map;
            },
            lessonsMapMonthlyGrouped() {
                const map = {};
                let l = this.lessonsList.filter((i) => {
                    return this.selectedGroups.includes(i.groupId);
                });
                l.forEach(e => (map[e.timeDate.substr(0, 7)] = map[e.timeDate.substr(0, 7)] || []).sort(this.compare).push(e));
                return map;
            },
            startMonth() {
                return this.start.slice(0, 7)
            },
            currentMonth() {
                let monthNames = [
                    "January", "February", "March",
                    "April", "May", "June", "July",
                    "August", "September", "October",
                    "November", "December"
                ];
                let date = new Date(this.start);
                let monthIndex = date.getMonth();
                let year = date.getFullYear();
                return monthNames[monthIndex] + ' ' + year;
            },
            selectedG(){
                let selectedCourses = [];
                this.groupsList.forEach(function (value) {
                    selectedCourses.push(value.id)
                });
                return selectedCourses;
            }
        },
        methods: {
            getDate(strDate) {
                return new Date(strDate).toDateString().slice(0, 20);
            },
            compare(aa, bb) {
                let a = new Date(aa.timeDate);
                let b = new Date(bb.timeDate);
                return (
                    isFinite(a = this.convert(a).valueOf()) &&
                    isFinite(b = this.convert(b).valueOf()) ?
                        (a > b) - (a < b) : NaN
                );
            },
            convert(d) {
                return (
                    d.constructor === Date ? d :
                        d.constructor === Array ? new Date(d[0], d[1], d[2]) :
                            d.constructor === Number ? new Date(d) :
                                d.constructor === String ? new Date(d) :
                                    typeof d === "object" ? new Date(d.year, d.month, d.date) :
                                        NaN
                );
            },
            computedTime(time) {
                let [hours, minutes] = time.split(':');
                let modifier = +hours < 12 ? 'am' : 'pm';
                hours = +hours % 12 || 12;
                minutes = +minutes === 0 ? '' : `:${minutes}`;
                return hours + minutes + modifier
            },
            computedInterval(interval) {
                let [hours, minutes] = interval.split(':');
                return parseInt(hours) + 'h ' + parseInt(minutes) + 'm';
            },
            goToGroupPage(groupId) {
                this.$router.push('/groups/' + groupId);
            },
            goToCoursePage(courseId) {
                this.$router.push('/courses/' + courseId);
            },
            goToTrainerPage(trainerId) {
                this.$router.push('/users/' + trainerId);
            },
            getGroupName(groupId) {
                return this.groupsList.filter((e) => {
                    return e.id === groupId;
                })[0].title;
            },
            getCourseId(groupId) {
                return this.groupsList.filter((e) => {
                    return e.id === groupId;
                })[0].courseId;
            },
            getCourseName(groupId) {
                return this.groupsList.filter((e) => {
                    return e.id === groupId;
                })[0].courseName;
            }
        },


    }
</script>

<style scoped>
    .my-lesson {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        border-radius: 2px;
        color: #ffffff;
        width: 100%;
        font-size: 12px;
        padding: 3px;
        cursor: pointer;
        margin-bottom: 1px;
    }

    /*noinspection ALL*/
    .active-lesson {
        background-color: #1867c0;
        border: 1px solid #1867c0;
    }

    /*noinspection ALL*/
    .canceled-lesson {
        background-color: #656266;
        border: 1px solid #656266;
    }

    .card {
        padding: 30px;
        margin-bottom: 30px;
        margin-top: 30px;
    }

    .title {
        color: white;
    }

    .clickable {
        cursor: pointer;
        margin-bottom: 5px;
        margin-top: 5px;
        color: black;
    }

</style>