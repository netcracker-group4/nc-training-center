<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-expansion-panel class="margin">
            <v-expansion-panel-content>
                <!--suppress HtmlUnknownBooleanAttribute -->
                <template v-slot:header>
                    <div>Employee's attendance</div>
                </template>
                <div class="attendance" v-for="group in user.groups">
                    <v-card flat>
                        <v-card-title>
                            Attendance of user {{user.firstName + ' ' + user.lastName}}
                            in group {{group.title}}
                        </v-card-title>

                        <attendance-table :user-id="user.id"
                                          :group-id="group.id"
                                          :key="group.id"/>
                    </v-card>
                </div>
            </v-expansion-panel-content>
        </v-expansion-panel>
    </div>
</template>
<script>
    import AttendanceTable from "../components/AttendanceTable.vue"

    export default {
        name: 'users-attendance',
        components: {AttendanceTable},
        props: {
            user: {}
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