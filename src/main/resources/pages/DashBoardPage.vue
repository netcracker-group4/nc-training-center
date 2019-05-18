<template>
    <v-container>
        <v-layout row wrap >
            <v-flex xs12 sm12>
                <div class="row" style="margin-bottom: 50px">
                    <v-tabs v-model="shownTable" fixed-tabs>
                        <v-tab>
                            Trainings and quantity of employee
                        </v-tab>
                        <v-tab>
                            raining level and quantity of groups
                        </v-tab>
                        <v-tab>
                            Training level and trainers
                        </v-tab>
                    </v-tabs>
                </div>
            </v-flex>
            <v-flex xs12 sm12>
                <DashBoardTableTrainingAndQuantity v-show="shownTable===0"></DashBoardTableTrainingAndQuantity>
                <DashBoardTableLevelAndQuantity v-show="shownTable===1"></DashBoardTableLevelAndQuantity>
                <DashBoardTableLevelAndTrainers v-show="shownTable===2"></DashBoardTableLevelAndTrainers>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    import DashBoardTableLevelAndQuantity from "../components/DashBoard/DashBoardTableLevelAndQuantity.vue";
    import DashBoardTableLevelAndTrainers from "../components/DashBoard/DashBoardTableLevelAndTrainers.vue";
    import DashBoardTableTrainingAndQuantity from "../components/DashBoard/DashBoardTableTrainingAndQuantity.vue";
    import store from "../store/store.js";

    export default {
        name: "DashBoardPage",
        data: function () {
            return {
                shownTable: 1
            }
        },
        components: {
            DashBoardTableLevelAndQuantity,
            DashBoardTableLevelAndTrainers,
            DashBoardTableTrainingAndQuantity
        },
        mounted() {
            if (!store.getters.isAdmin) {
                this.$router.push('/404');
            }
        }
    }
</script>

<style scoped>
    .active {
        background-color: grey;
    }
</style>