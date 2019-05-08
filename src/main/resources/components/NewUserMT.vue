<template>
    <div>
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
    </div>
</template>

<script>
    import axios from 'axios'
    export default {
        $_veeValidate: {
            validator: 'new'
        },
        data: () => ({
            user: {
                firstName: '',
                lastName: '',
                email: '',
                password: '',
                role: '',
                imageUrl: null
            },
        }),
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
        },
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
                });
                console.log(this.user)
            },
            clearUser () {
                this.user.firstName = '';
                this.user.lastName = '';
                this.user.email = '';
                this.user.password = '';
                this.user.role = '';
                this.$validator.reset()
            },
            // showBlockManagerAndTrainer() {
            //     this.showBlock = 1;
            // },
            // showBlockEmployee() {
            //     this.showBlock = 2;
            // },
            onFileSelected(event) {
                this.user.imageUrl = event.target.files[0]
            }
        },
    }
</script>

<style scoped>
    .user_block_label {
        font-size: 20px;
    }
</style>