<template>
    <form class="table" style="margin-top: 20px">
        <div class="row">
                <v-tabs v-model="shownTable" centered>
                    <v-tab v-on:click="showTrainingAndQuantity" >
                        Trainings and quantity of employee
                    </v-tab>
                    <v-tab v-on:click="showLevelAndQuantity" >
                        raining level and quantity of groups
                    </v-tab>
                    <v-tab v-on:click="showLevelAndTrainers" >
                        Training level and trainers
                    </v-tab>
                </v-tabs>
        </div>

            <v-container style="margin-top: 50px">
                <DashBoardTableTrainingAndQuantity v-show="shownTable===0"></DashBoardTableTrainingAndQuantity>
                <DashBoardTableLevelAndQuantity v-show="shownTable===1"></DashBoardTableLevelAndQuantity>
                <DashBoardTableLevelAndTrainers v-show="shownTable===2"></DashBoardTableLevelAndTrainers>
            </v-container>
    </form>
</template>

<script>
    import DashBoardTableLevelAndQuantity from "../components/DashBoardTableLevelAndQuantity.vue";
    import DashBoardTableLevelAndTrainers from "../components/DashBoardTableLevelAndTrainers.vue";
    import DashBoardTableTrainingAndQuantity from "../components/DashBoardTableTrainingAndQuantity.vue";

    //TODO function for checking our logged user's roles (is he an admin?)
    //TODO error page
    //TODO testing with greater amount of data
    export default {
        name: "DashBoardPage",
        data: function () {
            return {
                shownTable: 1
            }
        },
        methods: {
            showTrainingAndQuantity: function () {
                this.shownTable = 1;
            },
            showLevelAndQuantity: function () {
                this.shownTable = 2;
            },
            showLevelAndTrainers: function () {
                this.shownTable = 3
            }

        },
        components: {
            DashBoardTableLevelAndQuantity,
            DashBoardTableLevelAndTrainers,
            DashBoardTableTrainingAndQuantity
        }
    }
</script>

<style scoped>
    .active {
        background-color: grey;
    }
</style>