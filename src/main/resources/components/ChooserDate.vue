<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-container grid-list-md>
        <v-layout row wrap style="display: block">
            <v-flex >
                <v-menu
                        v-model="menu1"
                        :close-on-content-click="false"
                        full-width

                >
                    <template v-slot:activator="{ on }">
                        <v-text-field
                                :rules="[rules.required]"
                                :value="StartingDate"
                                clearable
                                label="First day"
                                readonly
                                v-on="on"
                        ></v-text-field>
                    </template>
                    <v-date-picker
                            :min="getCurrent()"
                            v-model="date1"
                            @change="menu1 = false"
                    ></v-date-picker>
                </v-menu>
            </v-flex>

            <v-flex >
                <v-menu
                        v-model="menu2"
                        :close-on-content-click="false"
                        full-width
                        max-width="290"
                >
                    <template v-slot:activator="{ on }">
                        <v-text-field
                                :rules="[rules.required]"
                                :value="EndingDate"
                                clearable
                                label="The last day"
                                readonly
                                v-on="on"
                        ></v-text-field>
                    </template>
                    <v-date-picker
                            :min="date1"
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
        name: "ChooserDate",
        data: () => ({
            date1: new Date().toISOString().substr(0, 10),
            date2: new Date().toISOString().substr(0, 10),
            menu1: false,
            menu2: false,
            rules: {
                required: value => !!value || 'Required.',
            }
        }),
        methods:{
          getCurrent(){
              return new Date().toISOString().substr(0, 10);
          },
        },
        computed: {
            StartingDate() {
                return this.date1 ? moment(this.date1).format('dddd, MMMM Do YYYY') : ''
            },
            EndingDate() {
                return this.date2 ? format(this.date2, 'dddd, MMMM Do YYYY') : ''
            }
        }
    }
</script>

<style scoped>

</style>