<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-card>
            <v-card-title>
                <span class="headline">Edit course</span>
            </v-card-title>

            <v-card-text>
                <v-container grid-list-md>
                    <v-layout wrap>
                        <v-flex xs5>
                            <v-text-field v-model="name"
                                          label="Name"></v-text-field>
                        </v-flex>
                        <v-flex xs10>
                            <v-flex xs12 sm12>
                                <v-select
                                        v-model="trainer"
                                        item-value="id"
                                        :items="trainers"
                                        :item-text="trainerName"
                                        :menu-props="{ maxHeight: '300' }"
                                        label="Set trainer"
                                        :rules="[rules.required]"
                                ></v-select>
                            </v-flex>
                        </v-flex>
                        <v-flex xs10>
                            <v-flex xs12 sm12>
                                <v-select
                                        v-model="status"
                                        item-value="id"
                                        :items="statuses"
                                        :rules="[rules.required]"
                                        :menu-props="{ maxHeight: '300' }"
                                        label="Set status"

                                ></v-select>
                            </v-flex>
                        </v-flex>
                        <v-flex xs10>
                            <v-flex xs12 sm12>
                                <v-radio-group v-model="level">
                                    <v-radio
                                            v-for="lvl in levels"
                                            :key="lvl.id"
                                            :label="lvl.title"
                                            :value="lvl.title"
                                            :rules="[rules.required]"
                                    ></v-radio>
                                </v-radio-group>
                            </v-flex>
                        </v-flex>
                        <v-flex>
                            <p style="margin-top: 3%">
                                <v-checkbox v-model="isOnLandingPage" :label="`Is on landing page? `">
                                </v-checkbox>
                            </p>
                        </v-flex>
                        <v-layout row wrap>
                            <v-flex xs4>
                                <v-menu
                                        :v-model="false"
                                        :close-on-content-click="false"
                                        :nudge-right="40"
                                        lazy
                                        transition="scale-transition"
                                        offset-y
                                        max-width="200px"
                                        min-width="200px"
                                >
                                    <template v-slot:activator="{ on }">
                                        <v-text-field
                                                v-model="startDay"
                                                label="Starts on"
                                                persistent-hint
                                                prepend-icon="event"
                                                v-on="on"
                                        ></v-text-field>
                                    </template>
                                    <v-date-picker :min="getCurrent()" v-model="startDay"></v-date-picker>
                                </v-menu>
                            </v-flex>
                            <v-flex xs4>
                                <v-menu
                                        :v-model="false"
                                        :close-on-content-click="false"
                                        :nudge-right="40"
                                        lazy
                                        transition="scale-transition"
                                        offset-y
                                        max-width="200px"
                                        min-width="200px"
                                >
                                    <template v-slot:activator="{ on }">
                                        <v-text-field

                                                v-model="endDay"
                                                label="Ends on"
                                                persistent-hint
                                                prepend-icon="event"
                                                v-on="on"
                                        ></v-text-field>
                                    </template>
                                    <v-date-picker :ranges="startDay" v-model="endDay"></v-date-picker>
                                </v-menu>
                            </v-flex>
                        </v-layout>
                        <v-flex xs6>
                            <v-textarea v-model="description"
                                        label="description"></v-textarea>
                        </v-flex>
                    </v-layout>
                </v-container>
            </v-card-text>

            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" flat @click="save">Save</v-btn>
            </v-card-actions>
        </v-card>
    </div>
</template>

<script>
    import axios from 'axios'

    export default {
        props: {course:{}},
        name: "EditCourseComponent",
        data() {
            return {
                name: '',
                status: '',
                level: {title: ''},
                description: '',
                startDay: '',
                endDay: '',
                trainer: {firstName: '', lastName: ''},
                isOnLandingPage: false,
                levels: [],
                statuses: [],
                trainers: [],
                rules: {
                    required: value => !!value || 'Required.',
                }
            }

        },
        methods: {
            trainerName: item => item.firstName + " " + item.lastName,
            getCurrent(){
                let date = new Date();
                let year = date.getFullYear();
                let month = date.getMonth();
                let day = date.getDay();
                return year+'-'+month+'-'+day;
            },
            save() {
                let self = this;
                let form = new FormData();
                let request = new XMLHttpRequest();
                try {
                    request.open('PUT', this.$store.state.apiServer + '/api/getcourses/' + self.course.id + '/edit');
                    form.append('id', self.id);
                    form.append('name', self.name);
                    form.append('level', self.level);
                    form.append('courseStatus', self.status);
                    form.append('trainer', self.trainer);
                    form.append('isOnLandingPage', self.isOnLandingPage);
                    form.append('description', self.description);
                    form.append('startDay', self.startDay);
                    form.append('endDay', self.endDay);
                    request.send(form);
                    this.$snotify.success('Course updated', {
                        timeout: 2000,
                        showProgressBar: false,
                        closeOnClick: false,
                        pauseOnHover: true
                    });
                    this.$router.push("/courses/" + self.id);
                } catch (e) {
                    this.$snotify.error("Error occured, course wasn`t updated.", {
                        timeout: 2000,
                        showProgressBar: false,
                        closeOnClick: false,
                        pauseOnHover: true
                    });
                }
            },
            setFields(){
                this.name = this.course.name;
                this.status= this.course.status;
                this.level = this.course.level;
                this.description = this.course.description;
                this.startDay = this.course.startDay;
                this.endDay = this.course.endDay;
                this.trainer = this.course.trainer;
                this.isOnLandingPage = this.course.isOnLandingPage;
            }
        },
        beforeMount() {
            let self = this;
            self.setFields();
            axios.get(this.$store.state.apiServer + '/api/getInfo/getStatuses')
                .then(function (response) {
                    self.statuses = response.data;
                })
                .catch(function (error) {
                    console.log(error);
                    if (error.response != null && error.response.status === 400)
                        self.$router.push('/404');
                });
            axios.get(this.$store.state.apiServer + '/api/getInfo/getLevels')
                .then(function (response) {
                    self.levels = response.data;
                })
                .catch(function (error) {
                    if (error.response != null && error.response.status === 400)
                        self.$router.push('/404');
                    console.log(error);
                });
            axios.get(this.$store.state.apiServer + '/api/users/get-all-trainers')
                .then(function (response) {
                    self.trainers = response.data;
                })
                .catch(function (error) {
                    if (error.response != null && error.response.status === 400)
                        self.$router.push('/404');
                    console.log(error);
                });
        }
    }
</script>

<style scoped>

</style>