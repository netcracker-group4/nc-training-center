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

    export default {
        name: "Application",
        components: {NavigationBar}
    }
</script>

<style scoped>
    @import "~vue-snotify/styles/material.css";
</style>
