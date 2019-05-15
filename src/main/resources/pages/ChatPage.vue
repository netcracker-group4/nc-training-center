<template>
    <v-container>
       <div v-for="message in messages">
           <v-layout v-if="message.senderId == self.$store.state.user.id" class="message-line">
               <v-flex xs4 offset-xs8 >
                   <v-card>
                       <v-card-text>
                           <div class="message-right">{{message.text}}</div>
                           <div class="message-right date">{{message.dateTime}}</div>
                       </v-card-text>
                   </v-card>
               </v-flex>
           </v-layout>
           <v-layout v-if="message.senderId != self.$store.state.user.id" class="message-line">
               <v-flex xs4>
                   <v-card>
                       <v-card-text>
                           {{message.text}}
                           <div class="date">{{messageDate(message.dateTime)}}</div>
                       </v-card-text>
                   </v-card>
               </v-flex>
           </v-layout>
       </div>
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
                self: this,
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
                    sendMessage(msg)
                    this.message = ''
                }
            },
            messageDate(date){
                return date.substr(0, 10) + ' at ' + date.substr(11, 2) + ':' + date.substr(14, 2)
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
    .message-line{
        margin-top: 20px;
        margin-bottom: 20px;
    }
    .message-right{
        text-align: right;
    }
    .date{
        margin-top: 10px;
        color: #9a9a9a;
    }
</style>