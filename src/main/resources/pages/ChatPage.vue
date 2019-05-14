<template>
    <v-container>
        {{messages}}
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
                messages: [],
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
                //sendMessage('hello')
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

</style>