<template>
    <v-container>
        {{messages}}
        <v-btn @click="send"></v-btn>
        <v-footer fixed
                  height="150"
                  color="grey lighten-2">
                    <v-textarea
                            class="text-area"
                            v-model="message"
                            solo
                            name="input-7-4"
                            label="Type message"
                    ></v-textarea>
                    <v-btn @click="send" class="btn-area">send</v-btn>
        </v-footer>
    </v-container>
</template>

<script>
    import axios from 'axios'
    import { sendMessage} from "../websocket/ws";
    import { addHandler} from "../websocket/ws";

    export default {
        name: "MessagePage",
        components: {},
        data(){
            return {
                message: '',
                messages: []
            }
        },
        created() {
           let id = this.$route.params.id;
            axios.get('http://localhost:8080/messages?chatId=' + id)
                .then(response => this.messages = response.data)

            addHandler(data => this.messages.push(data))
        },
        methods:{
            send(){

                if(this.message != '' & this.message != null & this.message != undefined){
                    let id = this.$route.params.id;
                    let msg = new Object()
                    msg.chatId = id
                    msg.senderId = this.$store.state.user.id
                    msg.text = this.message
                    //sendMessage(msg)
                    alert(msg.text)
                }
            }
        },
        watch:{
            '$route.params.id'(to, from){
                let id = this.$route.params.id;
                axios.get('http://localhost:8080/messages?chatId=' + id)
                    .then(response => this.messages = response.data)
            }
        }
    }
</script>

<style scoped>
    .text-area{
        margin-top: 30px;
        padding-left: 10%;
    }
    .btn-area{
        margin-right: 10%;
    }
</style>