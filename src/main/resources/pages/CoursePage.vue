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
                            <div class="subheading pt-3"> <b>{{courseStatus}}</b>
                             <b>{{level.title}}</b>
                                <v-btn v-if="this.$store.getters.userRoles.includes('EMPLOYEE')" @click="sign()">Sign course</v-btn>
                                <<!--v-dialog v-model="dialog" max-width="500px">
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
                                </v-dialog>-->
                            </div>
                            <!--<v-img sizes="" src="https://picsum.photos/510/300?random" aspect-ratio="2" wight="100%"></v-img>-->
                            <v-img :src="'../'+'img/'+imageUrl+'.jpg'" aspect-ratio="2"></v-img>
                            <div v-if="isAdmin">
                                <v-text-field label="Select Image" @click='pickFile' v-model='imageUrl' prepend-icon='attach_file'></v-text-field>
                                <input
                                        type="file"
                                        style="display: none"
                                        ref="image"
                                        accept="image/*"
                                        @change="onFilePicked"
                                >
                            </div>
                        </v-layout>
                    </v-flex>
                    <v-flex  xs5 offset-xs0 offset-lg0 style="margin-left: 2%">
                        <v-layout column>
                            <div class="subheading pt-3"> <b>Trainers</b></div>
                            <div v-for="tr in trainer" > <b @click="goTrainerPage(tr.id)"> {{tr.firstName }}   {{tr.lastName}} </b>
                            </div>
                            <div class="subheading pt-3">
                                <b>Groups</b> <v-btn v-if="isAdmin" @click="manageGroups()">Manage groups</v-btn>
                            </div>
                            <div v-for="group in groups">
                                <b @click="goGroupPage(group.id)" >{{group.title}}</b>
                                <v-btn v-if="isAdmin" @click="manageSchedule(group.id)">Manage schedule</v-btn>
                            </div>
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
                id: this.$route.params.id,
                name: null,
                levelId: null,
                level: null,
                courseStatus: null,
                courseStatusId: null,
                imageUrl: null,
                img: null,
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
                axios.get('http://localhost:8080/getcourses/'+self.id)
                    .then(function (response) {
                        //self.levels = response.data;
                        let dat = response.data;
                        self.name = dat.name;
                        self.levelId = dat.level;
                        self.setLevel(dat.level);
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
                this.$router.push('/userpage/' + id);
            },
            goGroupPage(id){
                this.$router.push('/groups/' + id);
            },
            sign(){
                this.$router.push('/courses/'+this.id+'/join');
            },
            getGroups(){
                let self = this;
                axios.get('http://localhost:8080/groups/get-groups/'+self.id)
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
            setLevel(levelId){
                let self = this;
                axios.get('http://localhost:8080/getInfo/getLevel/'+levelId)
                    .then(function (response) {
                        self.level = response.data;
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
            },
            submit() {
                this.description=document.querySelector('#course-descr-text')._value;
                let form = new FormData();
                let request = new XMLHttpRequest();
                    request.open('PUT', 'http://localhost:8080/getcourses/'+this.$route.params.id+'/create');
                    form.append('name', this.name);
                    form.append('level', this.level.title);
                    form.append('courseStatus', this.courseStatus);
                    form.append('imageUrl', this.imageUrl);
                    form.append('image',this.img);
                    form.append('trainer', this.trainer);
                    form.append('isOnLandingPage', this.isOnLandingPage);
                    form.append('description', this.description);
                    form.append('startDay', this.startDay);
                    form.append('endDay', this.endDay);
                    request.send(form);

            },
            pickFile () {
                this.$refs.image.click ()
            },
            onFilePicked (e) {
                const files = e.target.files;
                if(files[0] !== undefined) {
                    const fr = new FileReader ();
                    fr.readAsDataURL(files[0]);
                    fr.addEventListener('load', () => {
                        this.imageUrl = fr.result;
                        this.img = files[0]
                    })
                } else {
                    this.img = '';
                    this.imageUrl = '';
                }
            },
            manageGroups(){
                this.$router.push('/desired-schedule/' + this.$route.params.id);
            },
            manageSchedule(id){
                this.$router.push('/groups/'+id+'/schedule');
            }
        },
        mounted() {
            try {
                let self = this;
                self.setCourse();
                    axios.get('http://localhost:8080/img/'+self.imageUrl+'.jpg')
                        .then(function (response) {
                            self.img = response.data;
                        })
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