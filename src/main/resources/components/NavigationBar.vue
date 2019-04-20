<template>
    <v-toolbar app>
        <v-toolbar-items class="hidden-sm-and-down" v-if="isAuthorized">
            <v-btn @click="forwardToMainPage" flat>Main</v-btn>
        </v-toolbar-items>
        <v-spacer></v-spacer>
        <form action="/logout" method="get" v-if="isAuthorized">
            <v-btn type="submit" flat>Exit</v-btn>
        </form>
        <form action="/logout" method="get" v-if="isAuthorized == false">
            <v-btn type="submit" flat>login page</v-btn>
        </form>
    </v-toolbar>
</template>

<script>
    export default {
        name: "NavigationBar",
        methods:{
            forwardToMainPage(){
                this.$router.push('/')
            }
        },
        data(){
            return{
                isAuthorized: isAuthorized
            }
        }
    }
</script>

<style scoped>

</style>