<template>
    <v-toolbar app>
        <v-toolbar-items class="hidden-sm-and-down" v-if="isAuthorized">
            <v-btn @click="forwardToMainPage" flat>Main</v-btn>
        </v-toolbar-items>
        <v-spacer></v-spacer>
        <v-toolbar-items class="hidden-sm-and-down" v-if="isAdmin">
            <v-btn @click="forwardToDashboard" flat>Dashboard</v-btn>
        </v-toolbar-items>
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
            } ,
            forwardToDashboard(){
                this.$router.push('/dashboard')
            }
        },
        data(){
            return{
                isAuthorized: isAuthorized,
                //TODO function that checks if logged user is an admin
                isAdmin : true
            }
        }
    }
</script>

<style scoped>

</style>