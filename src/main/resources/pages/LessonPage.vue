<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-container
                fluid
                grid-list-md
        >
            <v-layout row wrap >
                <v-flex xs12 sm6 offset-sm3>
                    <template>
                        <v-card>
                            <v-card-text>
                            Topic: {{topic}}
                            </v-card-text>
                            <v-card-text @click="goToTrainerPage()">
                            Trainer: {{firstName}} {{lastName}}
                            </v-card-text>
                            <v-card-text>
                            Date: {{date}}
                            </v-card-text>
                            <v-card-text>
                            Duration: {{duration}}
                            </v-card-text>
                            <v-card-text>
                            Status: {{status}}
                            </v-card-text>
                        </v-card>
                    </template>
                </v-flex>
            </v-layout>
            <v-layout row wrap >
                <v-flex xs12 sm6 offset-sm3>
                    <v-toolbar flat color="white">
                    <span>Lesson attachments</span>
                    <v-dialog  v-if="isLessonTrainer()" v-model="dialog" max-width="500px">
                        <template v-slot:activator="{ on }">
                            <v-icon  @click="uploadForm" style="margin-left: 75%">
                                present_to_all
                            </v-icon>
                        </template>
                        <v-card>
                            <form id="uploadForm" name="uploadForm" enctype="multipart/form-data">

                                <input type="file" id="file" name="file" ><br>
                            </form>
                            <v-btn color="green" @click = "uploadFile">Upload</v-btn>
                        </v-card>
                    </v-dialog>
                    </v-toolbar>
                    <v-data-table
                            :headers="headers"
                            :items="attachments"
                            :expand="true"
                            item-key="id"
                            no-data-text = "No attachments available"
                    >
                        <template v-slot:items="props">
                            <tr>
                                <td class="my-link">
                                    <div>{{ props.item.name }}</div>
                                </td>
                                <td>
                                    <v-btn color="green" @click="downloadFile(props.item.id)">Download</v-btn>
                                </td>
                                <td>
                                    <v-btn color="error" @click="unlink(props.item.id)">Delete</v-btn>
                                </td>
                            </tr>
                        </template>
                    </v-data-table>
                </v-flex>
            </v-layout>
        </v-container>
    </div>
</template>

<script>

    import axios from 'axios';
    import store from '../store/store.js';
    export default {
        name: "AbsenceReasons",
        data: function(){
          return{
                dialog: false,
                topic:null,
                trainer: [],
                firstName:null,
                lastName:null,
                date:null,
                duration:null,
                status:"Active",
                attachments:[],
                headers: [
                  {
                      text: 'Name',
                      align: 'left',
                      value: 'title'
                  }
                ]
          }
        },
        mounted() {
            this.loadInfo();
        },
        watch: {
            '$route'(to, from) {
                this.loadInfo();
            }
        },
        methods: {
            goToTrainerPage() {
                if (this.trainer !== null) {
                    this.$router.push('/userpage/' + this.trainer.id);
                }
                window.scrollTo(0,0);
            },
            uploadForm(){
                this.dialog = true;

            },
            loadInfo(){
                let self = this;
                axios.get('http://localhost:8080/schedule/lesson/' + this.$route.params.id)
                                .then(function (response) {
                                    let dat = response.data;
                                    self.topic = dat.topic;
                                    self.trainer = dat.trainerId;
                                    axios.get('http://localhost:8080/users/' + self.trainer)
                                                 .then(function (response) {
                                                 self.trainer = response.data;
                                                 self.firstName = self.trainer.firstName;
                                                 self.lastName = self.trainer.lastName;
                                                 })
                                                 .catch(function (error) {
                                                     console.log(error);
                                                 });
                                    self.date = dat.time;
                                    self.duration = dat.duration;
                                    if(dat.isCanceled == true){
                                    self.status = "Canceled";}
                                })
                                .catch(function (error) {
                                    console.log(error);
                                });
            axios.get('http://localhost:8080/attachments/lesson/' + this.$route.params.id)
            .then(function (response) {
                self.attachments= response.data;
            })
            .catch(function (error) {
                console.log(error);
            });
                },
            uploadFile(){
                let form = new FormData(document.getElementById('uploadForm'));
                let imagefile = document.querySelector('#file');

                let request = new XMLHttpRequest();
                request.open('POST', 'http://localhost:8080/attachments/upload-file');
                form.append('file', imagefile.files[0]);
                form.append('lessonId',this.$route.params.id);
                form.append('descr', '');
               request.send(form);
                this.dialog = false;
            },
            isLessonTrainer(){
                return this.$store.state.user.id == this.trainer.id;
             },
             unlink(id){
                let form = new FormData();
                let request = new XMLHttpRequest();
                request.open('DELETE','http://localhost:8080/attachments/unlink');
                form.append('lessonId',this.$route.params.id);
                form.append('attachmentId', id);
                request.send(form);

             }

        }
    }

</script>