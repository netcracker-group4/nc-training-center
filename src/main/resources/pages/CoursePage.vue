<template>
    <div>
        <div class="title mb-1">{{name}}</div>

            <v-container
                    fluid
                    grid-list-md
            >
                <v-layout row wrap>
                    <v-flex xs5>
                        <v-layout column>
                            <div class="subheading pt-3"> <b>{{courseStatus}}</b></div>
                            <!--<v-img sizes="" src="https://picsum.photos/510/300?random" aspect-ratio="2" wight="100%"></v-img>-->
                            <v-img :src="''+imageUrl" aspect-ratio="2"></v-img> <!-- Something wrong here, try to fix it &ndash;&gt;-->
                        </v-layout>
                    </v-flex>
                    <v-flex  xs5 offset-xs0 offset-lg0 style="margin-left: 2%">
                        <v-layout column>
                            <div class="subheading pt-3"> <b>Trainers</b></div>
                            <div v-for="trainer in trainers" @click="goTrainerPage(trainer.id)"> <b> {{trainer.firstName }}   {{trainer.lastName}} </b> </div>
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
                        name="input-7-1"
                        box
                        label="Description"
                        auto-grow
                        :value="description"
                        :readonly="read"
                ></v-textarea>
            </v-flex>
            </v-layout>
        </v-container>
    </div>
</template>

<script>
    import axios from 'axios'

    export default {
        name: "CoursePage",
        data() {
            return{
                read: true,
                name: null,
                level: null,
                courseStatus: null,
                courseStatusId: null,
                imageUrl: null,
                isOnLandingPage: null,
                description: null,
                startDay: null,
                endDay: null,
                trainers: null,
                groups: null,
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
                        self.getTrainer();
                        self.getGroups();
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
                        self.trainers = response.data;
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