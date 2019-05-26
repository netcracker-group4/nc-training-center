<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-container>
        <progress-circular-component v-if="loading"></progress-circular-component>
        <v-layout v-if="!loading" row wrap>

            <!--            toolbar-->
            <v-flex xs12 style="margin-bottom: 50px">
                <v-toolbar flat color="white">
                    <v-toolbar-title>Course : {{name}}</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-btn flat @click="edit()">
                        <v-icon>edit</v-icon>
                    </v-btn>
                    <v-dialog v-if="isAdmin" v-model="dialog" max-width="500px">
                        <EditCourseComponent :starts-day="startDay" :ends-day="endDay"
                                             :descr="description" :id="id" :nm="name"/>
                    </v-dialog>
                    <v-btn v-if="canJoinCourse" @click="sign()">Sign course</v-btn>
                </v-toolbar>
            </v-flex>

            <!--            status-->
            <v-flex xs12 md6 style="padding: 10px">
                <v-list>
                    <v-list-tile>
                        <v-list-tile-content>
                            <v-list-tile-title><span class="grey--text name-my">Status :</span> <span
                                    class="font-weight-medium">{{courseStatus}}</span></v-list-tile-title>
                        </v-list-tile-content>
                    </v-list-tile>
                    <v-list-tile>
                        <v-list-tile-content>
                            <v-list-tile-title><span class="grey--text name-my">Level:</span> <span
                                    class="font-weight-medium">{{level.title}}</span></v-list-tile-title>
                        </v-list-tile-content>
                    </v-list-tile>
                    <v-list-tile>
                        <v-list-tile-content>
                            <v-list-tile-title><span class="grey--text name-my">Starts on:</span> <span
                                    class="font-weight-medium">{{startDay}}</span></v-list-tile-title>
                        </v-list-tile-content>
                    </v-list-tile>
                    <v-list-tile>
                        <v-list-tile-content>
                            <v-list-tile-title><span class="grey--text name-my">Ends on:</span> <span
                                    class="font-weight-medium">{{endDay}}</span>
                            </v-list-tile-title>
                        </v-list-tile-content>
                    </v-list-tile>
                    <v-list-tile>
                        <v-list-tile-content @click="goTrainerPage(trainer.id)" class="clickable">
                            <v-list-tile-title><span class="grey--text name-my">Trainer:</span> <span
                                    class="font-weight-medium">{{trainer.firstName }} {{trainer.lastName}}</span>
                            </v-list-tile-title>
                        </v-list-tile-content>
                    </v-list-tile>
                </v-list>
            </v-flex>

            <!--            groups-->
            <v-flex xs12 md6 style="padding: 10px">
                <v-toolbar flat color="white">
                    <v-toolbar-title>Groups</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-btn :small="true" v-if="isAdmin" @click="manageGroups()">Manage groups</v-btn>
                </v-toolbar>
                <v-data-table
                        :headers="headers"
                        :items="groups"
                        :expand="true"
                        item-key="id"
                        hide-actions
                >
                    <template v-slot:items="props">
                        <tr>
                            <td class="my-link">
                                <div @click="">{{ props.item.id }}</div>
                            </td>
                            <td class="my-link clickable">
                                <div @click="goGroupPage(props.item.id)">{{props.item.title}}</div>
                            </td>
                        </tr>
                    </template>
                </v-data-table>

            </v-flex>

            <!--            image-->
        </v-layout>
        <v-layout v-if="!loading" row wrap>
            <v-flex xs12 md6 style="padding: 10px">
                <v-img v-if="imageUrl"
                       :src="this.$store.state.apiServer+ '/api/files-img?url=' + imageUrl"
                       aspect-ratio="2"></v-img>
                <v-progress-linear v-if="!imageUrl" :indeterminate="true"></v-progress-linear>
                <div v-if="isAdmin">
                    <v-text-field label="Select Image"
                                  @click='pickFile'
                                  v-model='imageUrl'
                                  prepend-icon='attach_file'></v-text-field>
                    <form id="uploadForm" name="uploadForm" enctype="multipart/form-data" style="height: 0">
                        <input
                                type="file"
                                style="display: none"
                                ref="image"
                                accept="image/*"
                                @change="onFilePicked"
                                id="img"
                        ></form>
                </div>
            </v-flex>

            <!--            description-->
            <v-flex xs12 md6 style="padding: 10px">
                    <v-textarea
                            id="course-descr-text"
                            name="input-7-1"
                            label="Description"
                            :value="description"
                            :readonly="!isAdmin"
                            box
                            auto-grow
                    ></v-textarea>
                <v-layout>
                    <v-spacer></v-spacer>
                    <v-btn color="success" @click="submit">submit</v-btn>
                </v-layout>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    import axios from 'axios'
    import CourseDate from "../components/CourseDate.vue";
    import EditCourseComponent from "../components/EditCourseComponent.vue";
    import ProgressCircularComponent from "../components/ProgressCircularComponent.vue";

    export default {
        name: "CoursePage",
        components: {EditCourseComponent, CourseDate, ProgressCircularComponent},
        data() {
            return {
                id: this.$route.params.id,
                name: null,
                canJoinCourse: false,
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
                isNotAdmin: this.isAdmin === "false",
                dialog: false,
                headers: [
                    {
                        text: 'Group id',
                        align: 'left',
                        value: 'id'
                    },
                    {
                        text: 'Group title', value: 'title'
                    }
                ],
                loading: true
            }
        },
        methods: {
            setCourse() {
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/getcourses/' + self.id)
                    .then(function (response) {
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
                        self.loading = false;
                    })
                    .catch(function (error) {
                        console.log(error);
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                    });
            },
            getStatus(id) {
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/getInfo/getStatus/' + id)
                    .then(function (response) {
                        self.courseStatus = response.data;
                    })
                    .catch(function (error) {
                        console.log(error);
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                    });
            },
            getTrainer() {
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/getcourses/' + this.$route.params.id + '/trainer')
                    .then(function (response) {
                        self.trainer = response.data[0];
                    })
                    .catch(function (error) {
                        console.log(error);
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                    });
            },
            goTrainerPage(id) {
                this.$router.push('/users/' + id);
            },
            goGroupPage(id) {
                this.$router.push('/groups/' + id);
            },
            sign() {
                this.$router.push('/courses/' + this.id + '/join');
            },
            getGroups() {
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/groups/get-groups/' + self.id)
                    .then(function (response) {
                        self.groups = response.data;
                    })
                    .catch(function (error) {
                        console.log(error);
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                    });
            },
            edit() {
                this.dialog = true;
            },
            setLevel(levelId) {
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/getInfo/getLevel/' + levelId)
                    .then(function (response) {
                        self.level = response.data;
                    })
                    .catch(function (error) {
                        console.log(error);
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                    });
            },
            submit() {
                this.description = document.querySelector('#course-descr-text')._value;
                let form = new FormData();
                let request = new XMLHttpRequest();
                request.open('PUT', this.$store.state.apiServer + '/getcourses/' + this.$route.params.id + '/create');
                form.append('name', this.name);
                form.append('level', this.level.title);
                form.append('courseStatus', this.courseStatus);
                form.append('imageUrl', this.imageUrl);
                form.append('image', this.img);
                form.append('trainer', this.trainer);
                form.append('isOnLandingPage', this.isOnLandingPage);
                form.append('description', this.description);
                form.append('startDay', this.startDay);
                form.append('endDay', this.endDay);
                request.send(form);

            },
            pickFile() {
                this.$refs.image.click()
            },
            onFilePicked(e) {
                const files = e.target.files;
                if (files[0] !== undefined) {
                    const fr = new FileReader();
                    fr.readAsDataURL(files[0]);
                    fr.addEventListener('load', () => {
                        this.imageUrl = fr.result;
                        this.img = files[0]
                    });
                    let form = new FormData();
                    form.append('img', files[0]);
                    axios.put(this.$store.state.apiServer + '/api/getcourses/' + this.$route.params.id + '/upload-img', form, {
                        headers: {
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
            manageGroups() {
                this.$router.push('/courses/' + this.$route.params.id + '/desired-schedule');
            },
        },
        mounted() {
            let self = this;
            axios.get(this.$store.state.apiServer + '/api/getcourses/' + this.$route.params.id + '/can-join')
                .then(function (response) {
                    console.log(response.data);
                    console.log(self.$store.state.userRoles);
                    self.canJoinCourse = response.data;
                }).catch(function (error) {
                self.canJoinCourse = false;
            });
            self.setCourse();
        }
    }
</script>

<style scoped>
    .name-my {
        width: 21%;
        display: inline-block;
    }
</style>