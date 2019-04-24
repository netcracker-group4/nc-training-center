<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>

    </div>
</template>

<script>
    import axios from 'axios'

    export default {
        name: "UserPage",
        // props: ['userId'],
        data() {
            return {
                User: []
            }
        },
        mounted() {
            let self = this;
            axios.get('http://localhost:8080/users/1')
                .then(function (response) {
                    self.User = response.data;
                    console.log(self.User)
                })
                .catch(function (error) {
                    console.log(error);
                });
        }
    }
</script>

<style scoped>
    .wrapper {
        width: 900px;
        margin: 0 auto;
    }
</style>