<template>
    <v-toolbar app v-if="">
        <v-toolbar-items class="hidden-sm-and-down" v-if="isAuthorized">
            <v-btn @click="forwardToMainPage" flat>Main</v-btn>
        </v-toolbar-items>
        <v-spacer></v-spacer>
        <v-toolbar-items class="hidden-sm-and-down" v-if="isAuthorized == false || isAuthorized == null">
            <v-btn @click="forwardToLoginPage" flat>login</v-btn>
        </v-toolbar-items>
        <form action="/logout" method="get" v-if="isAuthorized">
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
        },
        data(){
            return{
                isAuthorized: isAuthorized,
            }
        }
    }
</script>

<style scoped>

</style>