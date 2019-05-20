<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-layout row wrap>
        <v-expansion-panel expand>
            <v-expansion-panel-content>
                <!--suppress HtmlUnknownBooleanAttribute -->
                <template v-slot:header>
                    <div>Manager's subordinates</div>
                </template>
                <div style="padding: 20px;">
                    <v-data-table
                            :headers="headers"
                            :items="subordinates"
                            class="elevation-1">
                        <template v-slot:items="props">
                            <td class="clickable" v-on:click="goToUserPage(props.item.id)">
                                {{props.item.firstName + ' ' + props.item.lastName}}
                            </td>
                            <td class="text-xs-right">
                                {{props.item.email}}
                            </td>
                            <td class="text-xs-right ">
                                <v-btn color="success">Message</v-btn>
                            </td>
                        </template>
                    </v-data-table>
                </div>
            </v-expansion-panel-content>
        </v-expansion-panel>
    </v-layout>
</template>

<script>
    import axios from 'axios'

    export default {
        name: "SubordinatesComponent",
        data: function () {
            return {
                subordinates: [],
                headers: [
                    {
                        text: 'Employee name',
                        align: 'left',
                        value: 'name'
                    },
                    {text: 'Email', value: 'email', align: 'right'},
                    {text: 'Message', value: 'message', align: 'right'}
                ],
            }
        },
        methods: {
            errorAutoClosable(title) {
                this.$snotify.error(title, {
                    timeout: 2000,
                    showProgressBar: false,
                    closeOnClick: false,
                    pauseOnHover: true
                });
            },
            goToUserPage(userId) {
                this.$router.push('/users/' + userId);
                window.scrollTo(0,0);
            },
            loadInfo() {
                let self = this;
                let id = this.$route.params.id;
                axios.get(this.$store.state.apiServer + '/api/users/' + id + '/subordinates')
                    .then(function (response) {
                        self.subordinates = response.data;
                        console.log(self.subordinates)
                    })
                    .catch(function (error) {
                        console.log(error);
                        self.errorAutoClosable(error.response.data);
                    });
            }
        },
        mounted() {
            this.loadInfo();
        },
        watch: {
            '$route'(to, from) {
                this.loadInfo();
            }
        }
    }
</script>

<style scoped>
    .clickable {
        cursor: pointer;
        margin-bottom: 5px;
        margin-top: 5px;
        color: black;
    }
</style>