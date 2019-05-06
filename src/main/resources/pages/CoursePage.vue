<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <div class="title mb-1">{{name}}</div>

            <v-container
                    fluid
                    grid-list-md
            >
                <v-layout row wrap>
                    <v-flex xs5>
                        <v-layout column>
                            <div class="subheading pt-3"> <b>{{courseStatus}}</b>
                                <v-dialog v-model="dialog" max-width="500px">
                                    <template v-slot:activator="{ on }">
                                        <v-icon
                                                class="mr-4"
                                                @click="editStatus()"
                                        >
                                            edit
                                        </v-icon>
                                    </template>
                                    <v-card>

                                        <v-select
                                                    v-model="courseStatus"
                                                    item-value="id"
                                                    :items="statuses"
                                                    item-text="courseStatus"
                                                    :menu-props="{ maxHeight: '300' }"
                                                    label="Change status"
                                            ></v-select>
                                    </v-card>
                                </v-dialog>
                            </div>
                            <!--<v-img sizes="" src="https://picsum.photos/510/300?random" aspect-ratio="2" wight="100%"></v-img>-->
                            <v-img :src="''+imageUrl" aspect-ratio="2"></v-img> <!-- Something wrong here, try to fix it &ndash;&gt;-->
                        </v-layout>
                    </v-flex>
                    <v-flex  xs5 offset-xs0 offset-lg0 style="margin-left: 2%">
                        <v-layout column>
                            <div class="subheading pt-3"> <b>Trainers</b></div>
                            <div v-for="tr in trainer" > <b @click="goTrainerPage(tr.id)"> {{tr.firstName }}   {{tr.lastName}} </b>
                               <!-- <v-select
                                        v-if="isAdmin"
                                    v-model="trainer"
                                    :items="trainers"
                                    :label=this.trainer.firstName
                            >
                                    <template slot="selection" slot-scope="trainers">
                                        {{ trainers.item.firstName }} {{ trainers.item.lastName }}
                                    </template>
                                    <template slot="item" slot-scope="trainers">
                                        {{ trainers.item.firstName }} {{ trainers.item.lastName }}
                                    </template>
                                </v-select>-->
                            </div>
                            <div class="subheading pt-3"> <b>Groups</b></div>
                            <div v-for="group in groups" @click="goGroupPage(group.id)">{{group.title}}</div>
                        </v-layout>
                    </v-flex>
                </v-layout>
            </v-container>
        <v-container
                fluid
                grid-list-md
        >
            <v-layout row wrap>
            <v-flex xs5>
                <v-textarea
                        id="course-descr-text"
                        name="input-7-1"
                        box
                        label="Description"
                        auto-grow
                        :value="description"
                        :readonly="!isAdmin"
                ></v-textarea>
            </v-flex>
            </v-layout>
            <v-flex xs12 sm12>
                <v-btn @click="submit">submit</v-btn>
            </v-flex>
        </v-container>
    </div>
</template>

<script>
    import axios from 'axios'

    export default {
        name: "CoursePage",
        data() {
            return{
                name: null,
                level: null,
                courseStatus: null,
                courseStatusId: null,
                imageUrl: null,
                isOnLandingPage: null,
                description: null,
                startDay: null,
                endDay: null,
                trainer: null,
                groups: null,
                isAdmin: this.$store.getters.isAdmin,
                trainers: null,
                dialog: null,
                statuses:[]
            }
        },
        methods:{
            setCourse(){
                let self = this;
                axios.get('http://localhost:8080/getcourses/'+this.$route.params.id)
                    .then(function (response) {
                        //self.levels = response.data;
                        let dat = response.data;
                        self.name = dat.name;
                        self.level = dat.level;
                        self.getStatus(dat.courseStatusId);
                        self.isOnLandingPage = dat.isOnLandingPage;
                        self.imageUrl = dat.imageUrl;
                        self.description = dat.description;
                        self.startDay = dat.startDay;
                        self.endDay = dat.endDay;
                        self.getTrainer();
                        self.getGroups();
                        if(self.isAdmin()){
                            self.getTrainers();
                        }
                        console.log(self);
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
            },
            getStatus(id){
                let self = this;
                axios.get('http://localhost:8080/getInfo/getStatus/'+id)
                    .then(function (response) {
                        self.courseStatus = response.data;
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
            },
            getTrainer(){
                let self = this;
                axios.get('http://localhost:8080/getcourses/'+this.$route.params.id+'/trainer')
                    .then(function (response) {
                        self.trainer = response.data;
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
            },
            getTrainers(){
                let self = this;
                axios.get('http://localhost:8080/dashboard/level-and-trainers')
                    .then(function (response) {
                        self.trainers = response.data;
                        self.trainers.forEach(function (value) {
                            self.selectedTrainers.push(value.trainer.id);
                        })
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
            },
            goTrainerPage(id){
                this.$router.push('/trainers/' + id);
            },
            goGroupPage(id){
                this.$router.push('/groups/' + id);
            },
            getGroups(){
                let self = this;
                axios.get('http://localhost:8080/groups/get-groups/'+this.$route.params.id)
                    .then(function (response) {
                        self.groups = response.data;
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
            },
            editStatus() {
                let self = this;
                this.dialog = true;
                for (let i = 1; i < 5; i++) {
                axios.get('http://localhost:8080/getInfo/getStatus/' + i)
                    .then(function (response) {
                        self.statuses.push(response.data);
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
            }
            },
            submit() {
                this.description=document.querySelector('#course-descr-text')._value;
                let form = new FormData();
                let request = new XMLHttpRequest();
                    request.open('PUT', 'http://localhost:8080/getcourses/create');
                    form.append('name', this.name);
                    form.append('level', this.level);
                    form.append('courseStatus', this.courseStatus);
                    form.append('imageUrl', this.imageUrl);
                    form.append('trainer', this.trainer);
                    form.append('isOnLandingPage', this.isOnLandingPage);
                    form.append('description', this.description);
                    form.append('startDay', this.startDay);
                    form.append('endDay', this.endDay);
                    request.send(form);

            }
        },
        mounted() {
            try {
                let self = this;
                self.setCourse();
                /*axios.get('http://localhost:8080/getcourses/{id}/trainer)
                    .then(function (response) {
                        self.courseStatus = response.data;
                    })
                    .catch(function (error) {
                        console.log(error);
                    });*/

            }catch (e) {
                console.log(e);
            }
            console.log(this);
        }
    }
</script>

<style scoped>

</style>