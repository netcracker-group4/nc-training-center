<template>
    <v-container>
        <v-layout row wrap>
            <v-flex >
                <v-container style="display: flex; position: absolute;">
                    <div style="margin-left: auto">
                        <v-flex xs12 sm12>
                            <v-text-field :rules="[rules.required]" v-model="name" label="name"></v-text-field>
                        </v-flex>
                        <!--<v-flex xs12 sm12>
                            <v-text-field v-model="imageUrl" label="image url" clearable></v-text-field>
                            <v-btn @click="uploadFile">Upload</v-btn>
                        </v-flex>-->
                        <v-flex xs12 sm12 class="text-xs-left text-sm-left text-md-left text-lg-left">
                            <img :src="imageUrl" height="150" v-if="imageUrl"/>
                            <div @click="pickFile()" style="cursor: pointer;">
                                <v-icon>
                                    attach_file
                                </v-icon>
                                Select Image
                            </div>
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
                                        :value="lvl.title"></v-radio>
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
                                    :rules="[rules.required]"
                            ></v-select>
                        </v-flex>
                        <v-flex xs12 sm12>
                            <v-select
                                    :rules="[rules.required]"
                                    v-model="courseStatus"
                                    item-value="id"
                                    :items="statuses"
                                    :menu-props="{ maxHeight: '300' }"
                                    label="Set status"

                            ></v-select>
                        </v-flex>
                        <v-spacer></v-spacer>
                        <v-flex xs12 sm12>
                            <v-btn @click="submit">submit</v-btn>
                        </v-flex>
                    </div>
                    <div style="justify-content: center; display: block; margin-right: auto; margin-left: 5%;">
                        <v-flex>
                            <p style="margin-top: 3%">
                                <v-checkbox v-model="isOnLandingPage" :label="`Is on landing page? `"></v-checkbox>
                            </p>
                        </v-flex>
                        <v-flex xs12 >
                            <v-textarea
                                    id="course-descr-text"
                                    box
                                    :rules="[rules.required]"
                                    name="input-7-4"
                                    label="Course description"
                                    auto-grow
                            ></v-textarea>
                        </v-flex>
                        <ChooserDate/>
                    </div>
                </v-container>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    import axios from 'axios'
    import ChooserDate from "../components/ChooserDate.vue";

    export default {
        props: ['id'],
        name: "CreateCourse",
        components: {ChooserDate},
        data() {
            return {
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
                trainers: [],
                statuses: [],
                rules: {
                    required: value => !!value || 'Required.',
                }
            }
        },

        mounted() {
            try {
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/getInfo/getLevels')
                    .then(function (response) {
                        self.levels = response.data;
                    })
                    .catch(function (error) {
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                        console.log(error);
                    });
                axios.get(this.$store.state.apiServer + '/api/users/get-all-trainers')
                    .then(function (response) {
                        self.trainers = response.data;
                    })
                    .catch(function (error) {
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                        console.log(error);
                    });
                axios.get(this.$store.state.apiServer + '/api/getInfo/getStatuses')
                    .then(function (response) {
                        self.statuses = response.data;
                    })
                    .catch(function (error) {
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                        console.log(error);
                    });

            } catch (e) {
                console.log(e);
            }
        },
        methods: {
            setData() {
                this.description = document.querySelector('#course-descr-text')._value;
                this.startDay = ChooserDate.data().date1;
                this.endDay = ChooserDate.data().date2;
            },
            check() {
                if (!this.name) {
                    return 'Name';
                }
                if (!this.level) {
                    return 'Level';
                }
                if (!this.courseStatus) {
                    return 'Status';
                }
                if (!this.imageUrl) {
                    return 'Image';
                }
                if (!this.trainer) {
                    return 'Trainer';
                }
                if (!this.description) {
                    return 'Description';
                }
                if (!this.startDay || !this.endDay) {
                    return 'Date';
                }
                return null;

            },
            submit() {
                this.setData();
                let nullField = this.check();
                if (nullField) {
                    this.$snotify.error(nullField + ' is required!', {
                        timeout: 2000,
                        showProgressBar: false,
                        closeOnClick: true,
                        pauseOnHover: true
                    });
                } else {
                    let self = this;
                    let form = new FormData();
                    let request = new XMLHttpRequest();
                    try {
                        request.open('POST', this.$store.state.apiServer + '/api/getcourses/create');
                        request.onreadystatechange = function() {
                            if (request.readyState === XMLHttpRequest.DONE) {
                                self.$router.push("/courses");
                            }
                        };
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
                        self.loading = true;
                        request.send(form);
                        this.$snotify.success('Course created', {
                            timeout: 2000,
                            showProgressBar: false,
                            closeOnClick: false,
                            pauseOnHover: true
                        });
                        self.$router.push("/courses");
                    } catch (e) {
                        this.$snotify.error(e.toString(), {
                            timeout: 2000,
                            showProgressBar: false,
                            closeOnClick: false,
                            pauseOnHover: true
                        });
                    }
                }
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
                        this.imageFile = files[0]
                    })
                } else {
                    this.imageFile = '';
                    this.imageUrl = '';
                }
            },
            trainerName: item => item.firstName + " " + item.lastName
        }
    }
</script>

<style scoped>

</style>