<template>
    <v-container>
        <v-layout row wrap>
            <v-flex xs12 sm4>
                <v-container>
                    <v-layout row wrap>
                        <v-flex xs12 sm12>
                            <v-text-field v-model="name" label="name"></v-text-field>
                        </v-flex>
                        <!--<v-flex xs12 sm12>
                            <v-text-field v-model="imageUrl" label="image url" clearable></v-text-field>
                            <v-btn @click="uploadFile">Upload</v-btn>
                        </v-flex>-->
                        <v-flex xs12 sm12 class="text-xs-center text-sm-center text-md-center text-lg-center">
                            <img :src="imageUrl" height="150" v-if="imageUrl"/>
                            <v-text-field label="Select Image" @click='pickFile' v-model='imageUrl' prepend-icon='attach_file'></v-text-field>
                            <input
                                    type="file"
                                    style="display: none"
                                    ref="image"
                                    accept="image/*"
                                    @change="onFilePicked"
                            >
                        </v-flex>
                        <v-flex xs12 sm12>
                            <v-radio-group v-model="level">
                                <v-radio
                                        v-for="lvl in levels"
                                        :key="lvl.id"
                                        :label="lvl.title"
                                        :value="lvl.title"
                                ></v-radio>
                            </v-radio-group>
                        </v-flex>
                        <v-flex xs12 sm12>
                            <v-select
                                    v-model="trainer"
                                    item-value="id"
                                    :items="trainers"
                                    :item-text="trainerName"
                                    :menu-props="{ maxHeight: '300' }"
                                    label="Set trainer"

                            ></v-select>
                        </v-flex>
                        <v-flex>
                            <p style="margin-top: 3%"><v-checkbox v-model="isOnLandingPage" :label="`Is on landing page? `"></v-checkbox></p>
                        </v-flex>
                            <v-flex xs12 style="width: 30%">
                                <v-textarea
                                        id="course-descr-text"
                                        box
                                        name="input-7-4"
                                        label="Course description"
                                        auto-grow
                                ></v-textarea>
                            </v-flex>
                        <ChooserDate/>
                        <v-spacer></v-spacer>
                        <v-flex xs12 sm12>
                            <v-btn @click="submit">submit</v-btn>
                        </v-flex>
                    </v-layout>
                </v-container>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    import axios from 'axios'
    import ChooserDate from "../components/ChooserDate.vue";

    export default {
        props:['id'],
        name: "CreateCourse",
        components: {ChooserDate},
        data(){
            return{
                name: null,
                level: null,
                courseStatus: null,
                imageUrl: null,
                imageFile: null,
                trainer: null,
                isOnLandingPage: null,
                description: null,
                startDay: null,
                endDay: null,
                levels: [],
                courseStatuses: [],
                trainers: []
            }
        },

        mounted() {
            try {
                let self = this;
                axios.get('http://localhost:8080/getInfo/getLevels')
                    .then(function (response) {
                        self.levels = response.data;
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
                axios.get('http://localhost:8080/users/get-all-trainers')
                    .then(function (response) {
                        self.trainers = response.data;
                    })
                    .catch(function (error) {
                        console.log(error);
                    });

            }catch (e) {
                console.log(e);
            }
        },
        methods:{

            setData(){
                this.description=document.querySelector('#course-descr-text')._value;
                this.startDay = ChooserDate.data().date1;
                this.endDay = ChooserDate.data().date2;
            },
            submit(){
                    this.setData();
                let form = new FormData();
                let request = new XMLHttpRequest();
                try {
                    if(this.id===undefined){
                        request.open('POST', 'http://localhost:8080/getcourses/create');
                    } else{
                        request.open('PUT', 'http://localhost:8080/getcourses/create');
                    }
                    form.append('name', this.name);
                    form.append('level', this.level);
                    form.append('courseStatus', this.courseStatus);
                    form.append('imageUrl', this.imageUrl);
                    form.append('image', this.imageFile);
                    form.append('trainer', this.trainer);
                    form.append('isOnLandingPage', this.isOnLandingPage);
                    form.append('description', this.description);
                    form.append('startDay', this.startDay);
                    form.append('endDay', this.endDay);
                    request.send(form);
                }catch (e) {
                    alert(e.toString());
                }
                alert('Course created');
                this.$router.push("/admincourses");
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
                        this.imageFile = files[0]
                    })
                } else {
                    this.imageFile = '';
                    this.imageUrl = '';
                }
            },
            trainerName: item => item.firstName +" "+ item.lastName
        }
    }
</script>

<style scoped>

</style>