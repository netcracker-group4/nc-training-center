<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-navigation-drawer
                v-model="drawer"
                fixed
                temporary
        >
            <v-list class="pa-1">
                <v-list-tile avatar>
                    <v-list-tile-avatar>
                        <v-icon>account_circle</v-icon>
                    </v-list-tile-avatar>
                    <v-list-tile-content class="cursor-pointer" v-on:click="goToMyPage()">
                        <v-list-tile-title v-if="this.$store.getters.isAuthorized">
                            {{$store.state.user.firstName + ' ' + $store.state.user.lastName}}
                        </v-list-tile-title>
                    </v-list-tile-content>
                </v-list-tile>
            </v-list>

            <v-list class="pt-0" dense>
                <v-divider></v-divider>
                <v-list-tile
                        v-for="item in items"
                        :key="item.title"
                        :to="item.link"
                        v-if="item.canBeShown(self)"
                >
                    <v-list-tile-action>
                        <v-icon>{{ item.icon }}</v-icon>
                    </v-list-tile-action>

                    <v-list-tile-content>
                        <v-list-tile-title>{{ item.title }}</v-list-tile-title>
                    </v-list-tile-content>
                </v-list-tile>

                <v-list-group
                        prepend-icon="chat"
                        no-action
                        v-if="$store.state.chats != null &
                              $store.state.chats != undefined &
                               $store.state.chats.length > 0"
                >
                    <template v-slot:activator>
                        <v-list-tile>
                            <v-list-tile-title>Chats</v-list-tile-title>
                        </v-list-tile>
                    </template>
                    <v-list-tile
                            v-for="chat in self.$store.state.chats"
                            :key="chat.id"
                            :to="'/chats/' + chat.id"
                    >
                        <v-list-tile-title v-text="chat.name"></v-list-tile-title>
                        <v-list-tile-action>

                        </v-list-tile-action>
                    </v-list-tile>

                </v-list-group>

            </v-list>
        </v-navigation-drawer>
        <v-toolbar class="grey lighten-4" app v-if="">
            <v-toolbar-items v-if="this.$store.getters.isAuthorized">
                <v-toolbar-side-icon @click="drawer = !drawer"></v-toolbar-side-icon>
            </v-toolbar-items>
            <v-toolbar-items class="hidden-sm-and-down" v-if="$store.getters.isAuthorized &&
                                                            $store.getters.isAdmin">
            </v-toolbar-items>
            <v-spacer></v-spacer>
            <v-toolbar-items class="hidden-sm-and-down" v-if="!$store.getters.isAuthorized">
                <v-btn @click="forwardToLoginPage" flat>login</v-btn>
            </v-toolbar-items>
            <form action="/logout" method="get" v-if="$store.getters.isAuthorized">
                <v-btn type="submit" flat>Exit</v-btn>
            </form>
        </v-toolbar>
    </div>

</template>

<script>
    export default {
        name: "NavigationBar",
        data() {
            return {
                self: this,
                drawer: false,
                chats: this.$store.state.chats,
                items: [
                    {
                        title: 'Main', icon: 'home', link: '/', canBeShown: function () {
                            return true
                        }
                    },
                    {
                        title: 'Users', icon: 'person', link: '/users', canBeShown: function (self) {
                            return self.$store.getters.isAdmin
                        }
                    },
                    {
                        title: 'Courses', icon: 'view_list', link: '/courses', canBeShown: function () {
                            return true
                        }
                    },
                    {
                        title: 'Groups', icon: 'group', link: '/groups',
                        canBeShown: function (self) {
                            return self.$store.getters.isAdmin
                        }

                    },
                    {
                        title: 'Dashboard', icon: 'dashboard', link: '/dashboard',
                        canBeShown: function (self) {
                            return self.$store.getters.isAdmin
                        }
                    },
                    {
                        title: 'Infodesk', icon: 'contact_support', link: '/requests',
                        canBeShown: function (self) {
                            if (self.$store.state.isAuthorized) {
                                return self.$store.getters.isAdmin || self.$store.state.userRoles.includes('EMPLOYEE')
                            }
                        }
                    },
                    {
                        title: 'Absence reasons', icon: 'add', link: '/absence-reasons',
                        canBeShown: function (self) {
                            return self.$store.getters.isAdmin
                        }
                    }
                ]
            }
        },
        methods: {
            goToMyPage() {
                this.$router.push('/users/' + this.$store.state.user.id);
                this.drawer = false;
            },
            forwardToLoginPage() {
                this.$router.push('/login')
            },
        },
    }
</script>

<style scoped>
    .cursor-pointer {
        cursor: pointer;
    }
</style>
