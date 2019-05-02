<template>
    <div>
        <div class="title mb-1">{{name}}</div>

            <v-container
                    fluid
                    grid-list-md
            >
                <v-layout row wrap>
                    <v-flex xs7>
                        <v-card>
                        <v-layout column>
                            <div class="subheading pt-3"> <b>{{courseStatus}}</b> <p>{{imageUrl}}</p></div>
                            <v-img sizes="" src="https://picsum.photos/510/300?random" aspect-ratio="2" wight="100%"></v-img>
                            <!--<v-img :src="require(imageUrl)" aspect-ratio="2"></v-img> &lt;!&ndash; Something wrong here, try to fix it &ndash;&gt;-->
                        </v-layout>
                        </v-card>
                    </v-flex>
                    <v-flex  xs5 offset-xs8 offset-lg0>
                        <v-card>
                            <div class="div_avatar">
                                <v-avatar
                                        size="180"
                                        class="avatar"
                                >
                                    <img src="https://99px.ru/sstorage/53/2017/03/tmb_193520_3284.jpg" alt="avatar" class="avatar_img">
                                </v-avatar>
                                <!--<v-container fluid style="display:block;">
                                    <v-switch v-model="user.active" @change="isActive" label="Active"></v-switch>
                                </v-container>-->
                            </div>
                        </v-card>
                    </v-flex>
                </v-layout>
            </v-container>






        <v-flex xs8>

        </v-flex>

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
                trainer: null
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
            }
        },
        mounted() {
            try {
                let self = this;
                self.setCourse();
                /*axios.get('http://localhost:8080/getInfo/getStatus/'+this.data.courseStatusId)
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