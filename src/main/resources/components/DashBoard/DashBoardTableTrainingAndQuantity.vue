<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-toolbar>
            <v-select
                    v-model="selectedCourses"
                    item-value="course.id"
                    :items="coursesAndQuantities"
                    item-text="course.name"
                    label="Select Trainings"
                    multiple
            >
                <template v-slot:selection="{ item, index }">
                    <v-chip v-if="index === 0">
                        <span>{{ item.course.name }}</span>
                    </v-chip>
                    <span
                            v-if="index === 1"
                            class="grey--text caption"
                    >(+{{ selectedCourses.length - 1 }} others)</span>
                </template>
            </v-select>
        </v-toolbar>
        <div>
            <v-toolbar flat color="white">
                <v-toolbar-title>Trainings and quantity of employees</v-toolbar-title>
                <v-spacer></v-spacer>
                <v-btn flat color="primary" @click="downloadDashboardReport" class="download-button">download excel report</v-btn>
            </v-toolbar>

            <v-data-table
                    :headers="headers"
                    :items="filteredCourses"
                    :expand="true"
                    class="elevation-1"
                    item-key="course.id"
            >
                <template v-slot:items="props">
                    <tr>
                        <td @click="props.expanded = !props.expanded">
                            <v-btn smal flat><span v-if="props.expanded">fold</span>
                                <span v-else>unfold</span></v-btn>
                        </td>
                        <td @click="goToCoursePage(props.item.course.id)" class="my-link">
                            <div>{{ props.item.course.name }}</div>
                        </td>
                        <td class="text-xs-right">{{ props.item.numberOfEmployees }}</td>
                    </tr>
                </template>
                <template v-slot:expand="props">
                    <v-data-table
                            :headers="headers2"
                            :items="props.item.groups"
                            hide-headers
                            hide-actions
                    >
                        <template v-slot:items="props">
                            <tr @click="goToGroupPage(props.item.id)" class="inner-table my-link">
                                <td class="text-xs-right">{{ props.item.title }}</td>
                                <td class="text-xs-right">{{ props.item.quantityOfEmployees }}</td>
                            </tr>
                        </template>
                    </v-data-table>
                </template>
            </v-data-table>
        </div>
    </div>
</template>

<script>
    import axios from 'axios/index'
    import store from "../../store/store.js";

    export default {
        name: "DashBoardTableLevelAndTrainers",
        data: function () {
            return {
                headers: [
                    {text: '  Fold/Unfold', sortable: false, width: "15", align: 'center'},
                    {
                        text: 'Name of the course',
                        align: 'left',
                        value: 'course.name'
                    },
                    {
                        text: 'Number of employees', value: 'quantityOfEmployees',
                        width: "20", align: 'right'
                    },
                ],
                headers2: [
                    {
                        text: 'Group name',
                        align: 'right',
                        sortable: false,
                        value: 'group.title'
                    },
                    {text: 'Number of employees', value: 'quantityOfEmployeesInGroup'},
                ],
                coursesAndQuantities: [],
                selectedCourses: [],
                selectedGroups: [],
                allGroups: [],
                groupsForCourses: {}
            }
        },
        methods: {
            errorAutoClosable(title) {
                this.$snotify.error(title, {
                    timeout: 2000,
                    showProgressBar: false,
                    closeOnClick: false,
                    pauseOnHover: true
                });
            },
            goToGroupPage(groupId) {
                this.$router.push('/groups/' + groupId);
            },
            goToCoursePage(courseId) {
                this.$router.push('/courses/' + courseId);
            },
            downloadDashboardReport(){
                window.open(this.$store.state.apiServer + "/api/download-report/dashboard-report", "_blank");
            }
        },
        mounted() {
            if(store.getters.isAdmin) {
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/dashboard/training-and-quantity')
                    .then(function (response) {
                        self.coursesAndQuantities = response.data;
                        console.log(response.data);
                        self.coursesAndQuantities.forEach(function (s) {
                            self.selectedCourses.push(s.course.id);
                            s.groups.forEach(function (ee) {
                                self.allGroups.push(ee.group);
                            });
                            self.$emit('loaded');
                            self.groupsForCourses[s.course.id] = s.groups;
                        });
                    })
                    .catch(function (error) {
                        console.log(error);
                        self.errorAutoClosable(error.response.data);
                    });
            }
        },
        computed: {
            filteredCourses() {
                return this.coursesAndQuantities.filter((i) => {
                    return this.selectedCourses.includes(i.course.id);
                })
            }
        }
    }
</script>

<style scoped>
    .inner-table {
        background-color: lavender;
    }

    .my-link {
        cursor: pointer;
    }
</style>