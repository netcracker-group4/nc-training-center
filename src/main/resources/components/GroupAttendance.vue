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
            axios.get('http://localhost:8080/groups/' + this.groupId + '/attendance-graph')
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

</style>