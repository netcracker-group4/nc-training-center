<template>
    <v-app>
        <navigation-bar/>
        <v-content>
                <router-view></router-view>
        </v-content>
        <vue-snotify></vue-snotify>
    </v-app>
</template>

<script>
    import NavigationBar from "../components/NavigationBar.vue";
    import axios from 'axios'

    //axios.defaults.baseURL = 'http://localhost:8080';
    export default {
        name: "Application",
        components: {NavigationBar},
        mounted() {
            axios.get(this.$store.state.apiServer + '/api/chats')
                .then(response => this.$store.state.chats = response.data)
        }
    }
</script>

<style scoped>
    @import "~vue-snotify/styles/material.css";
</style>
