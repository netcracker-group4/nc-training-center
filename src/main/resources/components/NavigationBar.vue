<template>
    <v-toolbar app>
        <v-toolbar-items class="hidden-sm-and-down">
            <v-btn @click="forwardToMainPage" flat>Main</v-btn>
        </v-toolbar-items>
        <v-spacer></v-spacer>
        <form action="/logout" method="get">
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
            }
        }
    }
</script>

<style scoped>

</style>