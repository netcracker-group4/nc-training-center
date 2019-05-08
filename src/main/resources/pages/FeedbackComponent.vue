<!--suppress HtmlUnknownBooleanAttribute -->
<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-expansion-panel>
            <v-expansion-panel-content>
                <template v-slot:header>
                    <div>Feedback</div>
                </template>
                <v-expansion-panel popout>
                    <v-expansion-panel-content
                            v-for="(feedback, i) in user.dtoFeedbacks"
                            :key="i"
                            hide-actions
                    >
                        <template v-slot:header>
                            <v-layout
                                    align-center
                                    row
                                    spacer
                            >
                                <v-flex xs4 sm2 md1>
                                    <v-avatar size="36px">
                                        <img
                                                src="https://avatars0.githubusercontent.com/u/9064066?v=4&s=460"
                                                alt="Avatar">
                                    </v-avatar>
                                </v-flex>

                                <v-flex sm5 md3 hidden-xs-only>
                                    <strong v-html="feedback.teacher.firstName"></strong>
                                </v-flex>

                                <v-flex
                                        class="grey--text"
                                        ellipsis
                                        hidden-sm-and-down
                                >
                                    &mdash;
                                    {{ feedback.text }}
                                </v-flex>
                            </v-layout>
                        </template>

                        <v-card>
                            <v-divider></v-divider>
                            <v-card-text v-text="lorem"></v-card-text>
                        </v-card>
                    </v-expansion-panel-content>
                </v-expansion-panel>
            </v-expansion-panel-content>
        </v-expansion-panel>
    </div>
</template>
<script>
    export default {
        name: 'feedback-component',
        props: {
            lorem: {},
            user: {}
        }
    }
</script>
<style scoped>

    .table_user td:first-child {
        background: #e6e4ee;
        /*border-bottom: 2px solid #e6e4ee;*/
        border-left: none;
    }

    .table_user td {
        border-right: 20px solid white;
        border-left: 20px solid white;
        border-bottom: 2px solid #e6e4ee;
        padding: 12px 10px;
        color: #8b8e91;
    }

    .table_user tr:last-child td {
        border-bottom: none;
    }

    .margin {
        margin-top: 30px;
        margin-bottom: 30px;
    }

</style>