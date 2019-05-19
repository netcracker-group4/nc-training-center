<template>
    <div>
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
    </div>
</template>

<script>
    import axios from 'axios'

    export default {
        $_veeValidate: {
            validator: 'new'
        },
        name: "NewUserE",
        data: () => ({
            mailSender: {
                email: '',
                subject: 'Invite',
                text: 'http://localhost:8080/registration',
            },
            defaultMailSender: {
                email: '',
                subject: 'Invite',
                text: 'http://localhost:8080/registration',
            },

        }),
        mounted () {
            this.$validator.localize('en', this.dictionary)
        },
        methods: {
            submitMailSender () {
                this.$validator.validateAll()
                axios.post('/api/users/mail-send', {
                    to: this.mailSender.email,
                    subject: this.mailSender.subject,
                    text: this.mailSender.text,
                });
                console.log(this.mailSender)
            },
            clearMailSender () {
                this.mailSender = Object.assign({}, this.defaultMailSender)
                this.$validator.reset()
            },
            // showBlockManagerAndTrainer() {
            //     this.showBlock = 1;
            // },
            // showBlockEmployee() {
            //     this.showBlock = 2;
            // },
        },
    }
</script>

<style scoped>
    .user_block_label {
        font-size: 20px;
    }
</style>