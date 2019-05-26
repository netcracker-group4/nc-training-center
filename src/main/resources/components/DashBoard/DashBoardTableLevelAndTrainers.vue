<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-toolbar-items class="hidden-sm-and-down" v-if="  this.$store.getters.isAuthorized &&
                                                            (this.$store.getters.isAdmin ||
                                                            this.$store.getters.isTrainer)">
            <v-toolbar>
                <v-select
                        v-model="selectedTrainers"
                        item-value="trainer.id"
                        :items="trainersAndLevels"
                        :item-text="trainerName"
                        label="Select Trainers"
                        multiple

                >
                    <template v-slot:selection="{ item, index }">
                        <v-chip v-if="index === 0">
                            <span>{{ item.trainer.firstName  + ' ' + item.trainer.lastName}}</span>
                        </v-chip>
                        <span
                                v-if="index === 1"
                                class="grey--text caption"
                        >(+{{ selectedTrainers.length - 1 }} others)</span>
                    </template>
                </v-select>
            </v-toolbar>
        </v-toolbar-items>
        <div>
            <v-toolbar flat color="white">
                <v-toolbar-title>Training level and trainers</v-toolbar-title>
                <v-spacer></v-spacer>
                <v-btn large flat @click="downloadDashboardReport">
                    <b>download excel report</b></v-btn>
            </v-toolbar>
            <v-data-table
                    :headers="headers"
                    :items="filteredTrainers"
                    :expand="true"
                    class="elevation-1"
                    item-key="trainer.id"
                    :rows-per-page-items="nums"
            >
                <template v-slot:items="props">
                    <tr>
                        <td @click="props.expanded = !props.expanded">
                            <v-btn smal flat><span v-if="props.expanded">fold</span>
                                <span v-else>unfold</span></v-btn>
                        </td>
                        <td @click="goToTrainerPage(props.item.trainer.id)" class="my-link">
                            <div>{{ props.item.trainer.firstName + " " + props.item.trainer.lastName}}</div>
                        </td>
                        <td class="text-xs-right">{{ props.item.numberOfCourses }}</td>
                    </tr>
                </template>
                <template v-slot:expand="props">
                    <v-data-table
                            :headers="headers2"
                            :items="props.item.courseAndLevels"
                            hide-headers
                            hide-actions
                    >
                        <template v-slot:items="props">
                            <tr @click="goToCoursePage(props.item.course.id)" class="inner-table my-link">
                                <td class="text-xs-right">{{ props.item.course.name }}</td>
                                <td class="text-xs-right">{{ props.item.level.title }}</td>
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

    export default {
        name: "DashBoardTableTrainingAndQuantity",
        data: function () {
            return {
                nums: [10, 25, {"text": "$vuetify.dataIterator.rowsPerPageAll", "value": -1}],
                headers: [
                    {text: '  Fold/Unfold', sortable: false, width: "15", align: 'center'},
                    {
                        text: 'Trainer Name',
                        align: 'left',
                        value: 'props.item.trainer.firstName + " " + props.item.trainer.lastName'
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
                        value: 'course.name'
                    },
                    {text: 'Course level', value: 'level.title'},
                ],
                trainersAndLevels: [],
                selectedTrainers: []
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
            goToTrainerPage(trainerId) {
                this.$router.push('/users/' + trainerId);
            },
            goToCoursePage(trainerId) {
                this.$router.push('/courses/' + trainerId);
            },
            downloadDashboardReport() {
                window.open(this.$store.state.apiServer + "/api/download-report/dashboard-report", "_blank");
            },
            trainerName: item => item.trainer.firstName + ' ' + item.trainer.lastName
        },
        mounted() {
            if (this.$store.getters.isAdmin) {
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/dashboard/level-and-trainers')
                    .then(function (response) {
                        self.trainersAndLevels = response.data;
                        self.trainersAndLevels.forEach(function (value) {
                            console.log(value);
                            self.selectedTrainers.push(value.trainer.id);
                        })
                    })
                    .catch(function (error) {
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                        console.log(error);
                        // self.errorAutoClosable(error.response.data);
                    });
            }
        },
        computed: {
            filteredTrainers() {
                return this.trainersAndLevels.filter((i) => {
                    return this.selectedTrainers.includes(i.trainer.id);
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


