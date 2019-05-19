<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <!--<template v-slot:activator="{ on }">
            <v-icon @click="editItem" style="margin-left: 30px">
                edit
            </v-icon>
        </template>-->
        <v-card>
            <v-card-title>
                <span class="headline">Edit course</span>
            </v-card-title>

            <v-card-text>
                <v-container grid-list-md>
                    <v-layout wrap>
                        <v-flex xs5>
                            <v-text-field v-model="course.name"
                                          label="Name"></v-text-field>
                        </v-flex>
                        <v-flex xs10>
                            <v-flex xs12 sm12>
                                <v-select
                                        v-model="course.trainer"
                                        item-value="id"
                                        :items="trainers"
                                        :item-text="trainerName"
                                        :menu-props="{ maxHeight: '300' }"
                                        label="Set trainer"

                                ></v-select>
                            </v-flex>
                        </v-flex>
                        <v-flex xs6>
                            <v-textarea v-model="course.description"
                                         label="description"></v-textarea>
                        </v-flex>
                    </v-layout>
                </v-container>
            </v-card-text>

            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" flat @click="close">Cancel</v-btn>
                <v-btn color="blue darken-1" flat >Save</v-btn>
            </v-card-actions>
        </v-card>
    </div>
</template>

<script>
    export default {
        props: {trainers: Array},
        name: "EditCourseComponent",
        data(){
            return{
                    course: {
                        name: '',
                        level: '',
                        trainer: '',
                        status: '',
                        description: ''
                    }
            }

        },
        methods:{
            trainerName: item => item.firstName +" "+ item.lastName,
            close() {
                this.dialog = false;
                setTimeout(() => {
                }, 300)
            }
        },
        mounted() {
            alert(trainers);
        }
    }
</script>

<style scoped>

</style>