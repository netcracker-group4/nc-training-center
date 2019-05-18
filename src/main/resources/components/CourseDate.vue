<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-container grid-list-md>
        <v-layout row wrap>
            <v-flex xs12 lg6>
                <v-menu
                        v-model="menu1"
                        :close-on-content-click="false"
                        full-width
                        max-width="290"
                >
                    <template v-slot:activator="{ on }">
                        <v-text-field
                                :value="StartingDate"
                                clearable
                                label="First day"
                                readonly="isNotAdmin"
                                v-on="on"
                        ></v-text-field>
                    </template>
                    <v-date-picker
                            v-model="date1"
                            @change="menu1 = false"
                    ></v-date-picker>
                </v-menu>
            </v-flex>

            <v-flex xs12 lg6>
                <v-menu
                        v-model="menu2"
                        :close-on-content-click="false"
                        full-width
                        max-width="290"
                >
                    <template v-slot:activator="{ on }">
                        <v-text-field
                                :value="EndingDate"
                                clearable
                                label="The last day"
                                readonly="props.isNotAdmin"
                                v-on="on"
                        ></v-text-field>
                    </template>
                    <v-date-picker
                            v-model="date2"
                            @change="menu2 = false"
                    ></v-date-picker>
                </v-menu>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    import moment from 'moment'
    import format from 'date-fns/format'

    export default {
        props: ['isNotAdmin','startDate','endDate'],
        name: "CourseDate",
        data: () => ({
            date1: props.startDate,
            date2: props.endDate,
            menu1: false,
            menu2: false
        }),

        computed: {
            StartingDate () {
                return this.date1 ? moment(this.date1).format('dddd, MMMM Do YYYY') : ''
            },
            EndingDate () {
                return this.date2 ? format(this.date2, 'dddd, MMMM Do YYYY') : ''
            }
        }
    }
</script>

<style scoped>

</style>