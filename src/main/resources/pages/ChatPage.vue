<template>
    <v-container fluid id="chat" ref="chat">
        <!--<v-btn v-if="this.isBtnShow()" @click="loadPage()">Load more</v-btn>-->
        <v-layout row wrap>
            <v-flex xs1 offset-xs6>
                <v-icon v-if="this.isBtnShow()" @click="loadPage()" class="load-more">replay</v-icon>
            </v-flex>
        </v-layout>

        <div>
            <div v-for="message in messages">
                <v-layout v-if="message.senderId == self.$store.state.user.id" class="message-line">
                    <v-flex xs3 offset-xs9>
                        <v-card color="light-blue lighten-5">
                            <v-card-text>
                                <div class="message-right sender-name">{{message.senderFirstName + ' ' +
                                    message.senderLastName}}
                                </div>
                                <div class="message-right">{{message.text}}</div>
                                <div class="message-right date">{{messageDate(message.dateTime)}}</div>
                            </v-card-text>
                        </v-card>
                    </v-flex>
                </v-layout>
                <v-layout v-if="message.senderId != self.$store.state.user.id" class="message-line">
                    <v-flex xs3>
                        <v-card color="blue-grey lighten-5">
                            <v-card-text>
                                <div class="sender-name">{{message.senderFirstName + ' ' + message.senderLastName}}
                                </div>
                                {{message.text}}
                                <div class="date">{{messageDate(message.dateTime)}}</div>
                            </v-card-text>
                        </v-card>
                    </v-flex>
                </v-layout>
            </div>
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
    import {addHandler, sendMessage} from "../websocket/ws";
    import {connect} from '../websocket/ws'

    export default {
        name: "MessagePage",
        components: {},
        data() {
            return {
                self: this,
                message: '',
                messages: [],
                messageListPage: 0,
            }
        },
        created() {
            let id = this.$route.params.id;
            this.loadPage()
            addHandler(data => {
                if(data.chatId == id){
                    this.messages.push(data)
                }
            })
        },
        mounted(){

            connect()
        },
        updated() {
            if (this.messageListPage == 1) {
                this.scroll()
            }
        },
        methods: {
            send() {
                if (this.message != '' & this.message != null & this.message != undefined) {
                    let id = this.$route.params.id;
                    let msg = new Object()
                    msg.chatId = id
                    msg.senderId = this.$store.state.user.id
                    msg.text = this.message
                    sendMessage(msg)
                    this.message = ''
                }
            },
            loadPage() {
                let id = this.$route.params.id;
                if (this.messageListPage != -1) {
                    axios.get(this.$store.state.apiServer + '/api/messages?chatId=' + id + '&page=' + this.messageListPage)
                        .then(response => {
                            if (response.data.messages != undefined) {
                                this.addMessagesToEnd(response.data.messages)
                            }
                            this.messageListPage = response.data.page
                        }).catch(function (error) {
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                    })
                }

            },
            scroll() {
                let height = document.getElementById("chat").scrollHeight
                window.scrollTo(0, height)
            },
            messageDate(date) {
                return date.substr(0, 10) + ' at ' + date.substr(11, 2) + ':' + date.substr(14, 2)
            },
            addMessagesToEnd(...messages) {
                if (messages.length[0] < 3) {
                    alert(messages[0].length)
                    this.isLessThenPage = true
                }
                if (messages != null) {
                    let chatId = this.$route.params.id
                    for (let i = 0; i < messages[0].length; i++) {
                        this.messages.unshift(messages[0][i])
                    }
                }
            },
            isBtnShow() {
                if (this.messageListPage == -1 | this.messages.length < 3) {
                    return false
                } else {
                    return true
                }
            }
        },
        watch: {
            '$route'(to, from) {
                this.messages = []
                this.messageListPage = 0
                this.loadPage()

            }
        }
    }
</script>

<style scoped>
    .text-area {
        margin-top: 30px;
        padding-left: 10%;
    }

    .btn-area {
        margin-right: 10%;
    }

    .message-line {
        margin-top: 20px;
        margin-bottom: 20px;
    }

    .message-right {
        text-align: right;
    }

    .date {
        margin-top: 10px;
        color: #9a9a9a;
    }

    #chat {
        margin-bottom: 200px;
        overflow-y: auto;
    }

    .sender-name {
        color: #9a9a9a;
        margin-bottom: 10px;
        font-size: 10px;
    }

    .load-more {
        font-size: 30px;
        color: #9a9a9a;
        margin-bottom: 10px;
    }
</style>