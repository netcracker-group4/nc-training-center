<template>
    <v-app>
        <navigation-bar/>
        <v-content>
            <v-container fluid>
                <router-view></router-view>
            </v-container>
        </v-content>
        <vue-snotify></vue-snotify>
    </v-app>
</template>

<script>
    import NavigationBar from "../components/NavigationBar.vue";
    import axios from 'axios'

    export default {
        name: "Application",
        components: {NavigationBar},
        mounted() {
            axios.get('http://localhost:8080/chats')
                .then(response => this.$store.state.chats = response.data)
        }
    }
</script>

<style scoped>
    @import "~vue-snotify/styles/material.css";
</style>
