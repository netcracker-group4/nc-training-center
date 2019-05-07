<template>
    <div>
        <v-navigation-drawer
                v-model="drawer"
                absolute
                temporary
        >
            <v-list class="pa-1">
                <v-list-tile avatar>
                    <v-list-tile-avatar>
                       <v-icon>account_circle</v-icon>
                    </v-list-tile-avatar>

                    <v-list-tile-content>
                        <v-list-tile-title>{{this.$store.state.user.firstName + ' ' +
                                            this.$store.state.user.lastName}}</v-list-tile-title>
                    </v-list-tile-content>
                </v-list-tile>
            </v-list>

            <v-list class="pt-0" dense>
                <v-divider></v-divider>

                <v-list-tile
                        v-for="item in items"
                        :key="item.title"
                        @click=""
                        :to="item.link"
                >
                    <v-list-tile-action>
                        <v-icon>{{ item.icon }}</v-icon>
                    </v-list-tile-action>

                    <v-list-tile-content>
                        <v-list-tile-title>{{ item.title }}</v-list-tile-title>
                    </v-list-tile-content>
                </v-list-tile>
            </v-list>
        </v-navigation-drawer>
        <v-toolbar class="grey lighten-2" app v-if="">
            <v-toolbar-items class="hidden-sm-and-down" v-if="this.$store.getters.isAuthorized">
                <v-toolbar-side-icon @click="drawer = !drawer"></v-toolbar-side-icon>
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
    </div>

</template>

<script>
    export default {
        name: "NavigationBar",
        data () {
            return {
                drawer: false,
                items: [
                    { title: 'Main', icon: 'dashboard', link: '/' },
                    { title: 'About', icon: 'question_answer' },
                ]
            }
        },
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
        mounted() {
        }
    }
</script>

<style scoped>

</style>