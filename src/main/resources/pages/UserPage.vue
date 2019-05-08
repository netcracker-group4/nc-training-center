<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-container>
            <v-layout row wrap>
                <v-flex md6 xs12>
                    <basic-user-info-component :user="user" :elem-name="'Employee'"/>
                </v-flex>
                <v-flex md6 xs12>
                    <users-manger-component :user="user" :elem-name="'Manager'"
                                            :if-null-message="'Employee has mo manager'"/>
                </v-flex>
            </v-layout>
            <div>
                <v-expansion-panel class="margin" expand>
                    <v-expansion-panel-content>
                        <!--suppress HtmlUnknownBooleanAttribute -->
                        <template v-slot:header>
                            <div>Employee's attendance</div>
                        </template>
                        <div class="attendance" v-for="group in user.groups">
                            <v-card>
                                <v-card-title>
                                    Attendance of user {{user.firstName + ' ' + user.lastName}} in group {{group.title}}
                                </v-card-title>

                                <attendance-table :user-id="user.id"
                                                  :group-id="group.id"
                                                  :key="group.id"/>
                            </v-card>
                        </div>
                    </v-expansion-panel-content>
                </v-expansion-panel>
            </div>

            <calendar-list-schedule-component class="margin" :groups-list="user.groups"
                                              :lessons-list="lessons"></calendar-list-schedule-component>

            <users-groups-and-courses class="margin" :groups="user.groups"
                                      :trainers="user.dtoTeachers"></users-groups-and-courses>

        </v-container>
    </div>
</template>

<script>
    import axios from 'axios'
    import AttendanceTable from "../components/AttendanceTable.vue";
    import CalendarListScheduleComponent from "../components/CalendarListScheduleComponent.vue";
    import BasicUserInfoComponent from "../components/BasicUserInfoComponent.vue";
    import UsersMangerComponent from "../components/UsersMangerComponent.vue";
    import UsersGroupsAndCourses from "../components/UsersGroupsAndCourses.vue";

    export default {
        components: {
            UsersMangerComponent,
            BasicUserInfoComponent,
            AttendanceTable,
            CalendarListScheduleComponent,
            UsersGroupsAndCourses
        },
        data: function () {
            return {
                user: '',
                right: null,
                prevId: null,
                groups: [],
                managers: [],
                lessons: []
            }
        },
        mounted() {
            let self = this;
            let id = this.$route.params.id;
            axios.get('http://localhost:8080/users/' + id)
                .then(function (response) {
                    self.user = response.data;
                    console.log(self.user)
                })
                .catch(function (error) {
                    console.log(error);
                });
            axios.get('http://localhost:8080/schedule/employee/' + id)
                .then(function (response) {
                    console.log(response.data);
                    self.lessons = response.data;
                    self.lessons.forEach(function (one) {
                        one.open = false;
                    })
                })
                .catch(function (error) {
                    console.log(error);
                });
        },
    }
</script>

<style scoped>
    .table_user td:first-child {
        background: #e6e4ee;
        /*border-bottom: 2px solid #e6e4ee;*/
        border-left: none;
    }

    .table_user td {
        border-right: 20px solid white;
        border-left: 20px solid white;
        border-bottom: 2px solid #e6e4ee;
        padding: 12px 10px;
        color: #8b8e91;
    }

    .table_user tr:last-child td {
        border-bottom: none;
    }

    .attendance {
        margin-bottom: 20px;
        margin-top: 20px;
    }

    .margin {
        margin-top: 30px;
        margin-bottom: 30px;
    }
</style>