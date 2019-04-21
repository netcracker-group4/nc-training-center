<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-toolbar flat color="white">
            <v-toolbar-title>Trainings and quantity of employees</v-toolbar-title>
            <v-spacer></v-spacer>
        </v-toolbar>
        <v-data-table
                :headers="headers"
                :items="coursesAndQuantities"
                :expand="true"
                item-key="courseId"
        >
            <template v-slot:items="props">
                <tr>
                    <td @click="props.expanded = !props.expanded">
                        <v-btn smal flat><span v-if="props.expanded">fold</span>
                            <span v-else>unfold</span></v-btn>
                    </td>
                    <td @click="goToCoursePage(props.item.courseId)" class="my-link">
                        <div>{{ props.item.courseName }}</div>
                    </td>
                    <td class="text-xs-right">{{ props.item.quantityOfEmployees }}</td>
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
                        <tr @click="goToGroupPage(props.item.groupId)" class="inner-table my-link">
                            <td class="text-xs-right">{{ props.item.groupName }}</td>
                            <td class="text-xs-right">{{ props.item.quantityOfEmployeesInGroup }}</td>
                        </tr>
                    </template>
                </v-data-table>
            </template>
        </v-data-table>
    </div>
</template>

<script>
    export default {
        name: "DashBoardTableLevelAndTrainers",
        data: function () {
            return {
                headers: [
                    {text: '  Fold/Unfold', sortable: false, width: "15", align: 'center'},
                    {
                        text: 'Name of the course',
                        align: 'left',
                        value: 'courseName'
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
                        value: 'groupName'
                    },
                    {text: 'Number of employees', value: 'quantityOfEmployeesInGroup'},
                ],
                coursesAndQuantities: [
                    {
                        courseName: 'Business english',
                        quantityOfEmployees: 159,
                        courseId: 1,
                        groups: [
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'gr',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: '3343',
                                quantityOfEmployeesInGroup: 55,
                                groupId: 1
                            }
                        ]

                    },
                    {
                        courseName: 'Python',
                        quantityOfEmployees: 237,
                        courseId: 2,
                        groups: [
                            {
                                groupName: 'grd',
                                quantityOfEmployeesInGroup: 20,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 85,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 5,
                                groupId: 1
                            },
                            {
                                groupName: 'g',
                                quantityOfEmployeesInGroup: 0,
                                groupId: 1
                            }
                        ]
                    },
                    {
                        courseName: 'Java basics',
                        quantityOfEmployees: 262,
                        courseId: 3,
                        groups: [
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            }
                        ]
                    },
                    {
                        courseName: 'Databases',
                        quantityOfEmployees: 305,
                        courseId: 4,
                        groups: [
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            }
                        ]
                    },
                    {
                        courseName: 'No SQL databases',
                        quantityOfEmployees: 356,
                        courseId: 5,
                        groups: [
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            }
                        ]
                    },
                    {
                        courseName: 'QA',
                        quantityOfEmployees: 375,
                        courseId: 6,
                        groups: [
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            }
                        ]
                    },
                    {
                        courseName: 'Angular 2',
                        quantityOfEmployees: 392,
                        courseId: 7,
                        groups: [
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            }
                        ]
                    },
                    {
                        courseName: 'Soft skills',
                        quantityOfEmployees: 408,
                        courseId: 8,
                        groups: [
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            }
                        ]
                    },
                    {
                        courseName: 'Management',
                        quantityOfEmployees: 452,
                        courseId: 9,
                        groups: [
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            },
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            }
                        ]
                    },
                    {
                        courseName: 'HR',
                        quantityOfEmployees: 518,
                        courseId: 10,
                        groups: [
                            {
                                groupName: 'group1',
                                quantityOfEmployeesInGroup: 10,
                                groupId: 1
                            }
                        ]
                    }
                ]
            }
        },
        methods: {
            goToGroupPage(groupId) {
                this.$router.push('/groups/' + groupId);
            },
            goToCoursePage(courseId) {
                this.$router.push('/courses/' + courseId);
            }
        }
    }
</script>

<style scoped>

</style>