<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-toolbar>
            <v-select
                    v-model="selectedLevels"
                    item-value="level.id"
                    :items="levelsAndGroupQuantities"
                    item-text="level.title"
                    label="Select Levels"
                    multiple
            >
                <template v-slot:selection="{ item, index }">
                    <v-chip v-if="index === 0">
                        <span>{{ item.level.title }}</span>
                    </v-chip>
                    <span
                            v-if="index === 1"
                            class="grey--text caption"
                    >(+{{ selectedLevels.length - 1 }} others)</span>
                </template>
            </v-select>
        </v-toolbar>
        <div>
            <v-toolbar flat color="white">
                <v-toolbar-title>Levels and quantity of groups</v-toolbar-title>
                <v-btn flat color="primary" @click="downloadDashboardReport" class="download-button">download excel report</v-btn>
                <v-spacer></v-spacer>
            </v-toolbar>
            <v-data-table
                    :headers="headers"
                    :items="filteredLevels"
                    :expand="true"
                    class="elevation-1"
                    item-key="level.id"
            >
                <template v-slot:items="props">
                    <tr>
                        <td @click="props.expanded = !props.expanded">
                            <v-btn smal flat><span v-if="props.expanded">fold</span>
                                <span v-else>unfold</span></v-btn>
                        </td>
                        <td>
                            <div>{{ props.item.level.title }}</div>
                        </td>
                        <td class="text-xs-right">{{ props.item.numberOfGroups }}</td>
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
                                <td @click="goToGroupPage(props.item.group.id)" class="text-xs-right my-link">
                                    {{ props.item.group.title }}
                                </td>
                                <td @click="goToCoursePage(props.item.course.id)" class="text-xs-right my-link">
                                    {{props.item.course.name }}
                                </td>
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

        name: "DashBoardTableLevelAndQuantity",
        data: function () {
            return {
                headers: [
                    {text: '  Fold/Unfold', sortable: false, width: "15", align: 'center'},
                    {
                        text: 'Level',
                        align: 'left',
                        value: 'level.title'
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
                        value: 'group.mame'
                    },
                    {text: 'Course name', value: 'course.name'},
                ],
                levelsAndGroupQuantities: [],
                selectedLevels : []
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
                this.$router.push('/group/' + groupId);
            },
            goToCoursePage(levelId) {
                this.$router.push('/courses/' + levelId);
            },
            downloadDashboardReport(){
                window.open("http://localhost:8080/download-report/dashboard-report", "_blank");
            }
        },
        mounted() {
            if(store.getters.isAdmin) {
                let self = this;
                axios.get('http://localhost:8080/dashboard/level-and-quantity')
                    .then(function (response) {
                        self.levelsAndGroupQuantities = response.data;
                        self.levelsAndGroupQuantities.forEach(function (value) {
                            self.selectedLevels.push(value.level.id)
                        })
                    })
                    .catch(function (error) {
                        console.log(error);
                        self.errorAutoClosable(error.response.data);
                    });
            }
        },
        computed : {
            filteredLevels () {
                return this.levelsAndGroupQuantities.filter((i) => {
                    return this.selectedLevels.includes(i.level.id);
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