<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-expansion-panel class="margin">
            <v-expansion-panel-content>
                <!--suppress HtmlUnknownBooleanAttribute -->
                <template v-slot:header>
                    <div>Attendance graph</div>
                </template>
                <div class="attendance" v-for="reason in Object.keys(absenceReasons)">
                    <v-card flat>
                        <v-card-title>
                            {{reason}}
                        </v-card-title>
                        <v-progress-linear
                                color="success"
                                height="10"
                                v-model="absenceReasons[reason]"></v-progress-linear>
                    </v-card>
                </div>
                <v-footer class="pa-3"></v-footer>
            </v-expansion-panel-content>
        </v-expansion-panel>
    </div>
</template>

<script>
    export default {
        props: ['absenceReasons'],
        name: "GroupAttendanceGraph"
    }
</script>

<style scoped>

</style>