<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-toolbar flat color="white">
            <v-toolbar-title>Levels and quantity of groups</v-toolbar-title>
            <v-spacer></v-spacer>
        </v-toolbar>
        <v-data-table
                :headers="headers"
                :items="levelsAndGroupQuantities"
                :expand="true"
                item-key="levelId"
        >
            <template v-slot:items="props">
                <tr>
                    <td @click="props.expanded = !props.expanded">
                        <v-btn smal flat><span v-if="props.expanded">fold</span>
                            <span v-else>unfold</span></v-btn>
                    </td>
                    <td>
                        <div>{{ props.item.level }}</div>
                    </td>
                    <td class="text-xs-right">{{ props.item.quantityOfGroups }}</td>
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
                        <tr class="inner-table">
                            <td @click="goToGroupPage(props.item.groupId)" class="text-xs-right my-link">{{ props.item.groupName }}</td>
                            <td @click="goToCoursePage(props.item.courseId)" class="text-xs-right my-link">{{ props.item.courseName }}</td>
                        </tr>
                    </template>
                </v-data-table>
            </template>
        </v-data-table>
    </div>
</template>

<script>
    export default {
        name: "DashBoardTableLevelAndQuantity",
        data: function () {
            return {
                headers: [
                    {text: '  Fold/Unfold', sortable: false, width: "15", align: 'center'},
                    {
                        text: 'Level',
                        align: 'left',
                        value: 'level'
                    },
                    {
                        text: 'Number of groups', value: 'quantityOfGroups',
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
                    {text: 'Course name', value: 'courseName'},
                ],
                levelsAndGroupQuantities: [
                    {
                        level: 'junior',
                        quantityOfGroups: 159,
                        levelId: 1,
                        groups: [
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'gr',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: '3343',
                                courseName: 55,
                                groupId: 1,
                                courseId: 2
                            }
                        ]

                    },
                    {
                        level: 'middle',
                        quantityOfGroups: 237,
                        levelId: 2,
                        groups: [
                            {
                                groupName: 'grd',
                                courseName: 20,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'group1',
                                courseName: 85,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'group1',
                                courseName: 5,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'g',
                                courseName: 0,
                                groupId: 1,
                                courseId: 2
                            }
                        ]
                    },
                    {
                        level: 'senior',
                        quantityOfGroups: 262,
                        levelId: 3,
                        groups: [
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            }
                        ]
                    },
                    {
                        level: 'elementary',
                        quantityOfGroups: 305,
                        levelId: 4,
                        groups: [
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            }
                        ]
                    },
                    {
                        level: 'intermediate',
                        quantityOfGroups: 356,
                        levelId: 5,
                        groups: [
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            }
                        ]
                    },
                    {
                        level: 'pre-intermediate',
                        quantityOfGroups: 375,
                        levelId: 6,
                        groups: [
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            }
                        ]
                    },
                    {
                        level: 'advanced',
                        quantityOfGroups: 392,
                        levelId: 7,
                        groups: [
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            }
                        ]
                    },
                    {
                        level: 'upper-intermediate',
                        quantityOfGroups: 408,
                        levelId: 8,
                        groups: [
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
                            },
                            {
                                groupName: 'group1',
                                courseName: 10,
                                groupId: 1,
                                courseId: 2
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
            goToCoursePage(levelId) {
                this.$router.push('/courses/' + levelId);
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