<template>
    <v-app id="app" style="min-height: 10px">
        <v-container grid-list-xl style="min-height: 10px">
            <image-input-component :user="user" v-model="avatar">
                <div slot="activator">
<!--                    <v-avatar size="200px" v-ripple v-if="true" class="grey lighten-3 mb-3">-->
<!--                        <img src="https://png.pngtree.com/svg/20161212/f93e57629c.svg" alt="avatar">-->
<!--                    </v-avatar>-->
                    <v-avatar size="200px" v-ripple v-if="true" class="grey lighten-3 mb-3">
                        <img src="http://localhost:8080/api/users/image" id="image" alt="avatar">
                    </v-avatar>
                    <v-avatar size="200px" v-ripple v-else class="mb-3">
                        <img :src="apiServer + user.image" alt="avatar">
                    </v-avatar>
                </div>
            </image-input-component>
            <v-slide-x-transition>
                <div v-if="avatar && saved == false">
                    <v-btn class="primary" @click="uploadImage()" :loading="saving">Save Avatar</v-btn>
                </div>
            </v-slide-x-transition>
        </v-container>
    </v-app>
</template>

<script>
    import axios from 'axios';
    import ImageInputComponent from './ImageInputComponent.vue';

    export default {
        name: 'image-upload-component',
        props: [
            'user',
        ],
        data() {
            return {
                avatar: null,
                saving: false,
                saved: false,
                apiServer: 'http://45.66.10.81:21',
                image: '',
            }
        },
        components: {
            ImageInputComponent
        },
        watch: {
            avatar: {
                handler: function () {
                    this.saved = false
                },
                deep: true
            }
        },
        methods: {
            uploadImage() {
                let self = this;
                if (this.avatar != '' && this.avatar != undefined) {
                    let formData = new FormData();
                    formData.append('id', this.user.id);
                    formData.append('image', this.avatar.imageFile);
                    axios.post(this.$store.state.apiServer + '/api/users/upload-image',
                        formData, {
                            headers: {
                                'Content-Type': 'multipart/form-data'
                            }
                        }
                    ).then(function(response) {
                        self.user = Object.assign({}, self.response.data);
                        self.saving = true;
                        setTimeout(() => this.savedAvatar(), 1000);
                    }).catch(function (error) {
                        console.log(error);
                    });
                    self.saving = true;
                    setTimeout(() => this.savedAvatar(), 1000);
                }
            },
            savedAvatar() {
                this.saving = false;
                this.saved = true;
            },
            getPath() {
                return "https://45.66.10.81:21/" + this.user.image;
            },
            getImage() {
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/users/image')
                    .then(function (response) {
                        self.image = response.data;
                        // document.getElementById("image").src = response.data;
                    })
                    .catch(function (error) {

                    });
            }
        },
        mounted() {
            this.getImage();
        }
    }
</script>

<style>
    .application--wrap {
        min-height: 0;
    }
</style>