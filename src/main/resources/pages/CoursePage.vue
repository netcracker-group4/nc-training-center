<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <div class="title mb-1">
            {{name}}
            <v-btn @click="edit()">
                <v-icon>edit</v-icon>
            </v-btn>
            <v-btn v-if="checkStud" @click="sign()">Sign course</v-btn>
        </div>
        <v-dialog  v-if="isAdmin" v-model="dialog" max-width="500px">
            <EditCourseComponent :starting-day="startDay" :ending-day="endDay"
                                :descr="description" :id="id"/>
        </v-dialog>
            <v-container
                    fluid
                    grid-list-md
            >
                <v-layout row wrap>
                    <v-flex xs5>
                        <v-layout column>
                            <div class="subheading pt-3"> <b>{{courseStatus}}</b>
                             <p>Level: {{level.title}}</p>
                            </div>
                                <v-img v-if="imageUrl" :src="this.$store.state.apiServer+imageUrl" aspect-ratio="2"></v-img>
                            <v-progress-linear v-if="!imageUrl" :indeterminate="true"></v-progress-linear>
                            <div v-if="isAdmin">
                                <v-text-field label="Select Image" @click='pickFile' v-model='imageUrl' prepend-icon='attach_file'></v-text-field>
                                <form id="uploadForm" name="uploadForm" enctype="multipart/form-data">
                                <input
                                        type="file"
                                        style="display: none"
                                        ref="image"
                                        accept="image/*"
                                        @change="onFilePicked"
                                        id="img"
                                ></form>
                            </div>
                        </v-layout>
                    </v-flex>
                    <v-flex  offset-xs0 offset-lg0 style="margin-left: 2%">
                        <v-layout column>
                            <div>
                                <b>
                                    <p>Starts on: {{startDay}}</p>
                                    <p>Ends on: {{endDay}}</p>
                                </b>
                            </div>
                            <div class="subheading pt-3"> <b>Trainer</b></div>
                            <div>
                                <b @click="goTrainerPage(tr.id)"> {{trainer.firstName }}   {{trainer.lastName}} </b>
                            </div>
                            <div class="subheading pt-3">
                                <b>Groups</b> <v-btn :small="true" v-if="isAdmin" @click="manageGroups()">Manage groups</v-btn>
                            </div>
                            <v-data-table
                                    :headers="headers"
                                    :items="groups"
                                    :expand="true"
                                    item-key="id"
                            >
                                <template v-slot:items="props">
                                    <tr>
                                        <td class="my-link">
                                            <div @click="">{{ props.item.id }}</div>
                                        </td>
                                        <td class="my-link clickable">
                                            <div @click="goGroupPage(props.item.id)">{{props.item.title}}</div>
                                        </td>
                                        <td class="text-xs-right">
                                            <v-btn :small="true"  v-if="isAdmin" @click="manageSchedule(props.item.id)">Manage schedule</v-btn>
                                        </td>
                                    </tr>
                                </template>
                            </v-data-table>
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
    import CourseDate from "../components/CourseDate.vue";
    import EditCourseComponent from "../components/EditCourseComponent.vue";

    export default {
        name: "CoursePage",
        components: {EditCourseComponent, CourseDate},
        data() {
            return{
                id: this.$route.params.id,
                name: null,
                levelId: null,
                level: {title: ''},
                courseStatus: null,
                courseStatusId: null,
                imageUrl: null,
                img: null,
                isOnLandingPage: null,
                description: null,
                startDay: null,
                endDay: null,
                trainer: {firstName: '', lastName: ''},
                groups: [],
                isAdmin: this.$store.getters.isAdmin,
                isNotAdmin: this.isAdmin==="false",
                dialog: false,
                headers: [
                    {
                        text: 'Group id',
                        align: 'left',
                        value: 'id'
                    },
                    {
                        text: 'Group title', value: 'title'
                    },
                    {
                        text: 'Action',value: '',
                        width: "20", align: 'right'
                    }
                ]
            }
        },
        methods:{
            setCourse(){
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/getcourses/'+self.id)
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
                        self.startDay = new Date(dat.startDate).toISOString().substr(0, 10);
                        self.endDay = new Date(dat.endDate).toISOString().substr(0, 10);
                        self.getTrainer();
                        self.getGroups();
                        self.getTrainers();
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
            },
            getStatus(id){
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/getInfo/getStatus/'+id)
                    .then(function (response) {
                        self.courseStatus = response.data;
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
            },
            getTrainer(){
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/getcourses/'+this.$route.params.id+'/trainer')
                    .then(function (response) {
                        self.trainer = response.data[0];
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
            },
            goTrainerPage(id){
                this.$router.push('/users/' + id);
            },
            goGroupPage(id){
                this.$router.push('/groups/' + id);
            },
            sign(){
                this.$router.push('/courses/'+this.id+'/join');
            },
            getGroups(){
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/groups/get-groups/'+self.id)
                    .then(function (response) {
                        self.groups = response.data;
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
            },
            edit(){
              this.dialog =true;
            },
            setLevel(levelId){
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/getInfo/getLevel/'+levelId)
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
                    request.open('PUT', this.$store.state.apiServer + '/getcourses/'+this.$route.params.id+'/create');
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
                    });
                    let form = new FormData();
                    form.append('img', files[0]);
                    axios.put(this.$store.state.apiServer + '/api/getcourses/'+this.$route.params.id+'/upload-img',form, {
                        headers:{
                            'Content-Type': 'multipart/form-data'
                        }
                    })
                        .then(function (response) {
                            console.log(response);
                        })
                        .catch(function (error) {
                            console.log(error);
                        });
                } else {
                    this.img = '';
                    this.imageUrl = '';
                }
            },
            manageGroups(){
                this.$router.push('/courses/' + this.$route.params.id + '/desired-schedule');
            },
            manageSchedule(id){
                this.$router.push('/groups/'+id+'/schedule');
            },
            checkStud(){
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/getcourses/' + this.$store.state.user.id + '/course-group')
                    .then(function (response) {
                        return response.data != true;
                    }).catch(function (error) {
                });
            }
        },
        created() {
            try {
                let self = this;
                self.setCourse();
            }catch (e) {
                console.log(e);
            }
            console.log(this);
        }
    }
</script>

<style scoped>

</style>