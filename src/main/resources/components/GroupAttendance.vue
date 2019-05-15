<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-expansion-panel class="margin">
            <v-expansion-panel-content>
                <!--suppress HtmlUnknownBooleanAttribute -->
                <template v-slot:header>
                    <div>Group's attendance</div>
                </template>
                <div class="attendance" v-for="lesson in lessons">
                    <v-card flat>
                        <v-card-title>
                            Attendance for '{{ lesson.topic }}'
                        </v-card-title>

                        <group-attendance-table :group-id="groupId"
                                                :lessonId="lesson.id"
                                                :key="lesson.id"/>
                    </v-card>
                </div>
            </v-expansion-panel-content>
        </v-expansion-panel>
    </div>
</template>
<script>
    import axios from 'axios/index'
    import GroupAttendanceTable from "./GroupAttendanceTable.vue"

    export default {

        name: 'group-attendance',
        components: {GroupAttendanceTable},
        props: ['groupId'],
        data() {
            return {
                lessons: []
            }
        },
        mounted() {
            axios.get('http://localhost:8080/schedule/' + String(this.groupId))
                .then(response => this.lessons = response.data)
                .catch(error => console.log(error));
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