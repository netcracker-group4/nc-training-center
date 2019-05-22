<template>
    <v-app id="app" style="min-height: 10px">
        <v-container grid-list-xl style="min-height: 10px">
            <image-input-component :user="user" v-model="avatar">
                <div slot="activator">
                    <v-avatar size="200px" v-ripple v-if="!avatar" class="grey lighten-3 mb-3">
                        <img src="https://png.pngtree.com/svg/20161212/f93e57629c.svg" alt="avatar">
                    </v-avatar>
                    <v-avatar size="200px" v-ripple v-else class="mb-3">
                        <img :src="avatar.imageURL" alt="avatar">
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
        data () {
            return {
                avatar: null,
                saving: false,
                saved: false
            }
        },
        components: {
            ImageInputComponent
        },
        watch:{
            avatar: {
                handler: function() {
                    this.saved = false
                },
                deep: true
            }
        },
        methods: {
            uploadImage() {
                let self = this;
                // console.log(self.user, self.user.id, self.user.image);
                // console.log(self.user, self.user.id, self.avatar);
                if (true) {
                    let formData = new FormData();
                    formData.set('id', this.user.id);
                    formData.append('image', this.avatar);
                    axios.put(this.$store.state.apiServer + '/api/users/upload-image',
                        formData, {
                            headers: {
                                'Content-Type': 'multipart/form-data; boundary=${form._boundary}'
                            }
                        }
                    // axios.put(this.$store.state.apiServer + '/api/users/upload-image', {
                    //         id: this.user.id,
                    //         image: this.avatar
                        ).then(response => {
                            this.saving = true;
                            setTimeout(() => this.savedAvatar(), 1000);
                        }).catch(function (error) {
                            console.log(error);
                        });
                        this.saving = true;
                        setTimeout(() => this.savedAvatar(), 1000);
                }
                // let self = this;
                // axios.put(this.$store.state.apiServer + '/api/users/upload-image', {
                //     id: this.user.id,
                //     image: this.avatar,
                // }).then(response => {
                //
                // }).catch(function (error) {
                //     console.log(error);
                // });
                // this.saving = true;
                // setTimeout(() => this.savedAvatar(), 1000);

            },
            savedAvatar() {
                this.saving = false;
                this.saved = true;
            }
        }
    }
</script>

<style>
    .application--wrap {
        min-height: 0;
    }
</style>
<!--<template>-->
<!--    <v-app id="app" class="mt-0">-->
<!--        <v-container grid-list-xl>-->
<!--            <image-input v-model="avatar">-->
<!--                <div slot="activator">-->
<!--                    <v-avatar size="150px" v-ripple v-if="!avatar" class="grey lighten-3 mb-3">-->
<!--                        <span>Click to add avatar</span>-->
<!--                    </v-avatar>-->
<!--                    <v-avatar size="150px" v-ripple v-else class="mb-3">-->
<!--                        <img :src="avatar.imageURL" alt="avatar">-->
<!--                    </v-avatar>-->
<!--                </div>-->
<!--            </image-input>-->
<!--            <v-slide-x-transition>-->
<!--                <div v-if="avatar && saved == false">-->
<!--                    <v-btn class="primary" @click="uploadImage" :loading="saving">Save Avatar</v-btn>-->
<!--                </div>-->
<!--            </v-slide-x-transition>-->
<!--        </v-container>-->
<!--    </v-app>-->
<!--</template>-->

<!--<script>-->
<!--    import ImageInput from './components/ImageInput.vue'-->
<!--    export default {-->
<!--        name: 'app',-->
<!--        data () {-->
<!--            return {-->
<!--                avatar: null,-->
<!--                saving: false,-->
<!--                saved: false-->
<!--            }-->
<!--        },-->
<!--        components: {-->
<!--            ImageInput: ImageInput-->
<!--        },-->
<!--        watch:{-->
<!--            avatar: {-->
<!--                handler: function() {-->
<!--                    this.saved = false-->
<!--                },-->
<!--                deep: true-->
<!--            }-->
<!--        },-->
<!--        methods: {-->
<!--            uploadImage() {-->
<!--                this.saving = true-->
<!--                setTimeout(() => this.savedAvatar(), 1000)-->
<!--            },-->
<!--            savedAvatar() {-->
<!--                this.saving = false-->
<!--                this.saved = true-->
<!--            }-->
<!--        }-->
<!--    }-->
<!--</script>-->