<template>
    <v-navigation-drawer class="drawer grey lighten-5"
                         fixed
                         v-model="drawer"
                         width="180"
                         hide-overlay
                         stateless
    >
        <v-list class="pt-0" dense>
            <v-list-tile to="/">
                <v-list-tile-action>
                    <v-icon>dashboard</v-icon>
                </v-list-tile-action>

                <v-list-tile-content>
                    <v-list-tile-title>Main</v-list-tile-title>
                </v-list-tile-content>
            </v-list-tile>
        </v-list>
    </v-navigation-drawer>
</template>

<script>
    export default {
        name: "NavigationDrawer",
        data() {
            return {
                drawer: true,
            }
        },
    }
</script>

<style scoped>
    .drawer{
        margin-top: 64px !important;
    }
</style>