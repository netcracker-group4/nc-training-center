<template>
    <div>
        <v-layout row wrap class="text-xs-center">
            <v-flex xs12 sm12 md4>
                <v-btn flat v-on:click="showTrainingAndQuantity" :class="{active: shownTable === 1}">Trainings and quantity of employee</v-btn>
            </v-flex>
            <v-flex xs12 sm12 md4>
                <v-btn flat v-on:click="showLevelAndQuantity" :class="{active: shownTable === 2}">Training level and quantity of groups</v-btn>
            </v-flex>
            <v-flex xs12 sm12 md4>
                <v-btn flat v-on:click="showLevelAndTrainers" :class="{active: shownTable === 3}">Training level and trainers</v-btn>
            </v-flex>
        </v-layout>

        <v-container>
            <DashBoardTableTrainingAndQuantity v-show="shownTable===1"></DashBoardTableTrainingAndQuantity>
            <DashBoardTableLevelAndQuantity  v-show="shownTable===2"></DashBoardTableLevelAndQuantity>
            <DashBoardTableLevelAndTrainers  v-show="shownTable===3"></DashBoardTableLevelAndTrainers>
        </v-container>



    </div>
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