<template>
    <div>
        <v-container>
            <v-layout row wrap>
                <v-flex xs12 sm12>
<!--                    <v-toolbar flat color="white">-->
<!--                    </v-toolbar>-->
                    <v-container class="con_wrapper">
                        <v-container @click="showBlockManagerAndTrainer" class="con_user_block" :class="{active: showBlock === 1}">
                            <NewUserMT></NewUserMT>
                        </v-container>
                        <v-container @click="showBlockEmployee" class="con_user_block" :class="{active: showBlock === 2}">
                            <NewUserE></NewUserE>
                        </v-container>
                    </v-container>
                </v-flex>
            </v-layout>
        </v-container>
    </div>
</template>

<script>
    // import NavigationDrawer from "../components/NavigationDrawer.vue"
    import NewUserE from "../components/NewUserE.vue"
    import NewUserMT from "../components/NewUserMT.vue"

    export default {
        $_veeValidate: {
            validator: 'new'
        },

        data: () => ({
            showBlock: 1,

        }),

        mounted () {
            this.$validator.localize('en', this.dictionary)
        },

        methods: {
            showBlockManagerAndTrainer() {
                this.showBlock = 1;
            },
            showBlockEmployee() {
                this.showBlock = 2;
            },
        },
        components: {
            NewUserE,
            NewUserMT
        }
    }
</script>

<style scoped>
    .con_wrapper {
        background: white;
        display: flex;
        margin: 0 auto 0;
        width: 100%;
    }
    .con_user_block {
        margin: 0 auto 0;
        opacity: 0.4;
    }
    .active {
        opacity: 1;
        transition-duration: 0.5s;
    }
</style>