<template>
    <v-toolbar app v-if="">
        <v-toolbar-items class="hidden-sm-and-down" v-if="this.$store.getters.isAuthorized">
            <v-btn @click="forwardToMainPage" flat>Main</v-btn>
        </v-toolbar-items>
        <v-toolbar-items class="hidden-sm-and-down" v-if="  this.$store.getters.isAuthorized &&
                                                            this.$store.getters.isAdmin">
            <v-btn @click="forwardToDashboardPage" flat>dashboard</v-btn>
            <v-btn @click="forwardToAttendancePage" flat>attendance</v-btn>
            <v-btn @click="forwardToCoursePage" flat>courses</v-btn>
            <v-btn @click="forwardToUsersPage" flat>users</v-btn>
            <v-btn @click="forwardToSchedulePage" flat>schedule</v-btn>
        </v-toolbar-items>
        <v-spacer></v-spacer>
        <v-toolbar-items class="hidden-sm-and-down" v-if="!this.$store.getters.isAuthorized">
            <v-btn @click="forwardToLoginPage" flat>login</v-btn>
        </v-toolbar-items>
        <form action="/logout" method="get" v-if="this.$store.getters.isAuthorized">
            <v-btn type="submit" flat>Exit</v-btn>
        </form>
    </v-toolbar>
</template>

<script>
    export default {
        name: "NavigationBar",
        methods:{
            forwardToMainPage(){
                this.$router.push('/')
            },
            forwardToLoginPage(){
                this.$router.push('/login')
            },
            forwardToDashboardPage(){
                this.$router.push('/dashboard')
            },
            forwardToAttendancePage(){
                this.$router.push('/attendance')
            },
            forwardToCoursePage(){
                this.$router.push('/admincourses')
            },
            forwardToUsersPage(){
                this.$router.push('/userpage')
            },
            forwardToSchedulePage(){
                this.$router.push('/desired-schedule/1')
            }
        },
        data(){
            return{

            }
        },
        mounted() {
        }
    }
</script>

<style scoped>

</style>