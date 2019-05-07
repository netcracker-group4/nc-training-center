<template>
    <div>
        <NavigationDrawer></NavigationDrawer>
        <v-container>
            <v-layout row wrap>
                <v-flex xs12 sm10 offset-sm2>
<!--                    <v-toolbar flat color="white">-->
<!--                    </v-toolbar>-->
                    <v-container class="con_wrapper">
                        <v-container @click="showBlockManagerAndTrainer" class="con_user_block" :class="{active: showBlock === 1}">
                            <label class="user_block_label">Add manager or trainer</label>
                            <form>
                                <input type="file" style="display: none" @change="onFileSelected" ref="fileInput"/>
                                <v-btn @click="$refs.fileInput.click()">Pick photo</v-btn>
                                <v-text-field
                                        v-model="user.firstName"
                                        v-validate="'required|max:20'"
                                        :counter="20"
                                        :error-messages="errors.collect('name')"
                                        label="Name"
                                        data-vv-name="name"
                                        required
                                ></v-text-field>
                                <v-text-field
                                        v-model="user.lastName"
                                        v-validate="'required|max:20'"
                                        :counter="20"
                                        :error-messages="errors.collect('surname')"
                                        label="Surname"
                                        data-vv-name="surname"
                                        required
                                ></v-text-field>
                                <v-text-field
                                        v-model="user.email"
                                        v-validate="'required|email'"
                                        :error-messages="errors.collect('email')"
                                        label="E-mail"
                                        data-vv-name="email"
                                        required
                                ></v-text-field>
                                <v-text-field
                                        v-model="user.password"
                                        v-validate="'required|max:20'"
                                        :counter="20"
                                        :error-messages="errors.collect('password')"
                                        data-vv-name="name"
                                        label="Password"
                                        required
                                ></v-text-field>
                                <v-radio-group v-model="user.role" row>
                                    <v-radio label="Manager" value="MANAGER"></v-radio>
                                    <v-radio label="Trainer" value="TRAINER"></v-radio>
                                </v-radio-group>

                                <v-btn @click="clearUser" color="error" fab small dark>
                                    <v-icon>clear</v-icon>
                                </v-btn>
                                <v-btn @click="submitUser">submit</v-btn>
                            </form>
                        </v-container>
                        <v-container @click="showBlockEmployee" class="con_user_block" :class="{active: showBlock === 2}">
                            <label class="user_block_label">Add employee</label>
                            <form>
                                <v-text-field
                                        v-model="mailSender.email"
                                        v-validate="'required|email'"
                                        :error-messages="errors.collect('mailSender')"
                                        label="E-mail"
                                        data-vv-name="mailSender"
                                        required
                                ></v-text-field>
                                <v-text-field
                                        v-model="mailSender.subject"
                                        v-validate="'required|max:256'"
                                        :error-messages="errors.collect('subject')"
                                        label="Subject"
                                        data-vv-name="subject"
                                        required
                                ></v-text-field><v-text-field
                                    v-model="mailSender.text"
                                    v-validate="'required|max:256'"
                                    :error-messages="errors.collect('text')"
                                    label="Text"
                                    data-vv-name="text"
                                    required
                                ></v-text-field>
                                <v-btn @click="clearMailSender" color="error" fab small dark>
                                    <v-icon>clear</v-icon>
                                </v-btn>
                                <v-btn @click="submitMailSender">submit</v-btn>

                            </form>
                        </v-container>
                    </v-container>
                </v-flex>
            </v-layout>
        </v-container>
    </div>
</template>

<script>
    import axios from 'axios'
    import NavigationDrawer from "../components/NavigationDrawer.vue"

    export default {
        $_veeValidate: {
            validator: 'new'
        },

        data: () => ({
            showBlock: 1,
            user: {
                firstName: '',
                lastName: '',
                email: '',
                password: '',
                role: '',
                imageUrl: null
            },
            mailSender: {
                email: '',
                subject: 'Invite',
                text: '',
            },
            defaultMailSender: {
                email: '',
                subject: 'Invite',
                text: '',
            },
            checkbox: null,
            dictionary: {
                attributes: {
                    email: 'E-mail Address',
                    // custom attributes
                },
                custom: {
                    surname: {
                        required: () => 'Surname can not be empty',
                        max: 'The surname field may not be greater than 20 characters'
                    },
                    name: {
                        required: () => 'Name can not be empty',
                        max: 'The name field may not be greater than 20 characters'
                        // custom messages
                    },
                    select: {
                        required: 'Select field is required'
                    }
                }
            }
        }),

        mounted () {
            this.$validator.localize('en', this.dictionary)
        },

        methods: {
            submitUser () {
                this.$validator.validateAll()
                axios.post('http://localhost:8080/users', {
                    firstName: this.user.firstName,
                    lastName: this.user.lastName,
                    email: this.user.email,
                    password: this.user.password,
                    role: this.user.role
                })
                // let form = new FormData();
                // let request = new XMLHttpRequest();
                // try {
                //     request.open('POST', 'http://localhost:8080/users');
                //     form.append('firstName', this.user.firstName);
                //     form.append('lastName', this.user.lastName);
                //     form.append('email', this.user.email);
                //     form.append('password', this.user.password);
                //     form.append('role', this.user.role);
                //     request.send(form);
                // }catch (e) {
                //     console.log(e);
                // }
                console.log(this.user)
            },
            submitMailSender () {
                this.$validator.validateAll()
                axios.post('http://localhost:8080/users/main-send', {
                    to: this.mailSender.email,
                    subject: this.mailSender.subject,
                    text: this.mailSender.text,
                });
                console.log(this.mailSender)
            },
            clearUser () {
                this.user.firstName = '';
                this.user.lastName = '';
                this.user.email = '';
                this.user.password = '';
                this.user.role = '';
                this.$validator.reset()
            },
            clearMailSender () {
                this.mailSender = Object.assign({}, this.defaultMailSender)
                this.$validator.reset()
            },
            showBlockManagerAndTrainer() {
                this.showBlock = 1;
            },
            showBlockEmployee() {
                this.showBlock = 2;
            },
            onFileSelected(event) {
                this.user.imageUrl = event.target.files[0]
            }
        },
        components: {
            NavigationDrawer
        }
    }
</script>

<style scoped>
    .con_wrapper {
        background: white;
        display: flex;
        margin: 40px auto 0;
        width: 100%;
    }
    .con_user_block {
        margin: 0 auto 0;
        opacity: 0.5;
    }
    .active {
        opacity: 1;
        transition-duration: 0.5s;
    }
    .user_block_label {
        font-size: 20px;
    }
</style>