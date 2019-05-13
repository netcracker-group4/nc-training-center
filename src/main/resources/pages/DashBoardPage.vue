<template>
    <form class="table" style="margin-top: 20px">
        <div class="row">
            <v-tabs v-model="shownTable" centered>
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