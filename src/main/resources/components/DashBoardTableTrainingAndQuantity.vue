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
                        <tr @click="goToGroupPage(props.item.group.id)" class="inner-table my-link">
                            <td class="text-xs-right">{{ props.item.group.title }}</td>
                            <td class="text-xs-right">{{ props.item.quantityOfEmployees }}</td>
                        </tr>
                    </template>
                </v-data-table>
            </template>
        </v-data-table>
    </div>
</template>

<script>
    import axios from 'axios'
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
                coursesAndQuantities: []
            }
        },
        methods: {
            goToGroupPage(groupId) {
                this.$router.push('/groups/' + groupId);
            },
            goToCoursePage(courseId) {
                this.$router.push('/courses/' + courseId);
            }
        },
        mounted() {
            let self = this;
            axios.get('http://localhost:8080/dashboard/training-and-quantity')
                .then(function (response) {
                    self.coursesAndQuantities = response.data;
                })
                .catch(function (error) {
                    console.log(error);
                });
        }
    }
</script>

<style scoped>

</style>