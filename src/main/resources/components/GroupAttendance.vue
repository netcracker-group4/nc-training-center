<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-expansion-panel class="margin">
            <v-expansion-panel-content>
                <!--suppress HtmlUnknownBooleanAttribute -->
                <template v-slot:header>
                    <div>Group's attendance</div>
                </template>
                    <v-card flat>
                        <group-attendance-graph :absenceReasons="reasons"/>
                    </v-card>
            </v-expansion-panel-content>
        </v-expansion-panel>
    </div>
</template>
<script>
    import axios from 'axios/index'
    import GroupAttendanceGraph from "./GroupAttendanceGraph.vue"

    export default {

        name: 'group-attendance',
        components: {GroupAttendanceGraph},
        props: ['groupId'],
        data() {
            return {
                reasons: []
            }
        },
        mounted() {
            axios.get(this.$store.state.apiServer + '/api/groups/' + this.groupId + '/attendance-graph')
                .then(function (response) {
                    self.reasons = response.data;
                    console.log(response.data);
                }).catch(function (error) {
                console.log(error);
            });
        }
    }
</script>
<style scoped>

    .table_user td:first-child {
        background: #e6e4ee;
        /*border-bottom: 2px solid #e6e4ee;*/
        border-left: none;
    }

    .table_user td {
        border-right: 20px solid white;
        border-left: 20px solid white;
        border-bottom: 2px solid #e6e4ee;
        padding: 12px 10px;
        color: #8b8e91;
    }

    .table_user tr:last-child td {
        border-bottom: none;
    }

    .attendance {
        margin-bottom: 20px;
        margin-top: 20px;
    }

    .margin {
        margin-top: 30px;
        margin-bottom: 30px;
    }

</style>