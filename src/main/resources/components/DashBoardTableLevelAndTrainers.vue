<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-toolbar flat color="white">
            <v-toolbar-title>Training level and trainers</v-toolbar-title>
            <v-spacer></v-spacer>
        </v-toolbar>
        <v-data-table
                :headers="headers"
                :items="trainersAndLevels"
                :expand="true"
                item-key="trainerId"
        >
            <template v-slot:items="props">
                <tr>
                    <td @click="props.expanded = !props.expanded">
                        <v-btn smal flat><span v-if="props.expanded">fold</span>
                            <span v-else>unfold</span></v-btn>
                    </td>
                    <td @click="goToTrainerPage(props.item.trainerId)" class="my-link">
                        <div>{{ props.item.trainerName }}</div>
                    </td>
                    <td class="text-xs-right">{{ props.item.quantityOfCourses }}</td>
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
                        <tr @click="goToCoursePage(props.item.courseId)" class="inner-table my-link">
                            <td class="text-xs-right">{{ props.item.courseName }}</td>
                            <td class="text-xs-right">{{ props.item.courseLevel }}</td>
                        </tr>
                    </template>
                </v-data-table>
            </template>
        </v-data-table>
    </div>
</template>

<script>
    export default {
        name: "DashBoardTableTrainingAndQuantity",
        data: function () {
            return {
                headers: [
                    {text: '  Fold/Unfold', sortable: false, width: "15", align: 'center'},
                    {
                        text: 'Trainer Name',
                        align: 'left',
                        value: 'trainerName'
                    },
                    {
                        text: 'Number of courses', value: 'quantityOfCourses',
                        width: "20", align: 'right'
                    },
                ],
                headers2: [
                    {
                        text: 'Course name',
                        align: 'right',
                        sortable: false,
                        value: 'courseName'
                    },
                    {text: 'Course level', value: 'courseLevel'},
                ],
                trainersAndLevels: [
                    {
                        trainerName: 'Business english',
                        quantityOfCourses: 159,
                        trainerId: 1,
                        groups: [
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'gr',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: '3343',
                                courseLevel: 55,
                                courseId: 1
                            }
                        ]

                    },
                    {
                        trainerName: 'Python',
                        quantityOfCourses: 237,
                        trainerId: 2,
                        groups: [
                            {
                                courseName: 'grd',
                                courseLevel: 20,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 85,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 5,
                                courseId: 1
                            },
                            {
                                courseName: 'g',
                                courseLevel: 0,
                                courseId: 1
                            }
                        ]
                    },
                    {
                        trainerName: 'Java basics',
                        quantityOfCourses: 262,
                        trainerId: 3,
                        groups: [
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            }
                        ]
                    },
                    {
                        trainerName: 'Databases',
                        quantityOfCourses: 305,
                        trainerId: 4,
                        groups: [
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            }
                        ]
                    },
                    {
                        trainerName: 'No SQL databases',
                        quantityOfCourses: 356,
                        trainerId: 5,
                        groups: [
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            }
                        ]
                    },
                    {
                        trainerName: 'QA',
                        quantityOfCourses: 375,
                        trainerId: 6,
                        groups: [
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            }
                        ]
                    },
                    {
                        trainerName: 'Angular 2',
                        quantityOfCourses: 392,
                        trainerId: 7,
                        groups: [
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            }
                        ]
                    },
                    {
                        trainerName: 'Soft skills',
                        quantityOfCourses: 408,
                        trainerId: 8,
                        groups: [
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            }
                        ]
                    },
                    {
                        trainerName: 'Management',
                        quantityOfCourses: 452,
                        trainerId: 9,
                        groups: [
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            },
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            }
                        ]
                    },
                    {
                        trainerName: 'HR',
                        quantityOfCourses: 518,
                        trainerId: 10,
                        groups: [
                            {
                                courseName: 'group1',
                                courseLevel: 10,
                                courseId: 1
                            }
                        ]
                    }
                ]
            }
        },
        methods: {
            goToTrainerPage(trainerId) {
                this.$router.push('/trainers/' + trainerId);
            },
            goToCoursePage(trainerId) {
                this.$router.push('/courses/' + trainerId);
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


