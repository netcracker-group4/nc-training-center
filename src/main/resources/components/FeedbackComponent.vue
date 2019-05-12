<!--suppress HtmlUnknownBooleanAttribute -->
<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-expansion-panel>
            <v-expansion-panel-content>
                <template v-slot:header>
                    <div>Feedback</div>
                </template>
                <div class="div_table_item">
                    <template v-if="canShowLeaveFeedbackBlock()">
                        <v-container style="display:flex; justify-content: space-between;">
                            <v-avatar size="60px">
                                <img
                                        src="https://avatars0.githubusercontent.com/u/9064066?v=4&s=460"
                                        alt="Avatar"/>
                            </v-avatar>
                            <div style="width: 90%;">
                                <v-textarea
                                        name="input-1"
                                        box
                                        full-width
                                        hide-details
                                        outline
                                        auto-grow
                                        rows="3"
                                        placeholder="Leave feedback"
                                        v-model="feedbackText"
                                ></v-textarea>
                            </div>
                        </v-container>
                        <v-container class="v-container_button">
                            <div style="display: flex; justify-content: flex-end;">
                                <v-btn @click="leaveFeedback()" color="info">Submit</v-btn>
                            </div>
                        </v-container>
                    </template>
                    <v-data-table
                            :items="userFeedback"
                            hide-headers
                            :rows-per-page-items="rows"
                    >
                        <template v-slot:items="props">
                            <div class="table_item">
                                <td>
                                    <v-avatar size="60px">
                                        <img
                                                src="https://avatars0.githubusercontent.com/u/9064066?v=4&s=460"
                                                alt="Avatar"/>
                                    </v-avatar>
                                </td>
                                <td style="vertical-align: top;">
                                    <div class="div_teacherName">
                                        {{ props.item.teacher.firstName }} {{ props.item.teacher.lastName }}
                                        <span class="span_courseName">{{ props.item.course.name }}</span>
                                    </div>
                                    <div class="div_feedbackText">
                                        {{ props.item.text }}
                                    </div>
                                    <div class="div_feedbackTimeDate">
                                        {{ props.item.timeDate }}
                                    </div>
                                </td>
                            </div>
                        </template>
                    </v-data-table>
                </div>
            </v-expansion-panel-content>
        </v-expansion-panel>
    </div>
</template>
<script>
    import axios from 'axios';
    import store from '../store/store.js';

    export default {
        name: 'feedback-component',
        props: [
            'user'
        ],
        data:() => ({
            feedbackText: '',
            userFeedback: [],
            rows: [3,5,10,{"text":"$vuetify.dataIterator.rowsPerPageAll","value":-1}]
        }),
        methods: {
            getAuthorizationUser() {
                return store.state.user;
            },
            canShowLeaveFeedbackBlock() {
                return store.state.userRoles.includes("ADMIN") ||
                    store.state.userRoles.includes("TRAINER");
            },
            leaveFeedback() {
                let self = this;
                axios.post('http://localhost:8080/feedback/add', {
                    studentId: this.user.id,
                    teacher: this.getAuthorizationUser(),
                    text: this.feedbackText,
                })
                .then(response => {
                    // self.userFeedback = response.data;
                    self.feedbackText = '';
                    console.log(response);
                    if (this.isNotOnlyTrainer()) {
                        this.getAllFeedback();
                    } else {
                        this.getAllFeedbackByTrainer();
                    }
                })
                .catch(function (error) {
                    console.log(error);
                });

            },
            isNotOnlyTrainer() {
                return store.state.userRoles.includes("ADMIN") ||
                    store.state.userRoles.includes("MANAGER") ||
                    store.state.userRoles.includes("EMPLOYEE");
            },
            getAllFeedback() {
                let self = this;
                axios.get('http://localhost:8080/feedback/get-by-user?userId=' + this.$route.params.id)
                    .then(function (response) {
                        self.userFeedback = response.data;
                        console.log(self.userFeedback)
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
            },
            getAllFeedbackByTrainer() {
                let self = this;
                axios.get('http://localhost:8080/feedback/get-by-rainer-and-by-user?userId=' +
                    this.$route.params.id + "&trainerId=" + store.state.user.id)
                    .then(function (response) {
                        self.userFeedback = response.data;
                        console.log(self.userFeedback)
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
            }
        },
        mounted() {
            if (this.isNotOnlyTrainer()) {
                this.getAllFeedback();
            } else {
                this.getAllFeedbackByTrainer();
            }

        },
        watch: {
            '$route'(to, from) {
                if (this.isNotOnlyTrainer()) {
                    this.getAllFeedback();
                } else {
                    this.getAllFeedbackByTrainer();
                }
            }
        }
    }
</script>
<style scoped>

    .div_table_item {
        padding: 20px 7%;
    }
    .table_item {
        padding: 20px 0 20px 0;
    }
    .div_teacherName {
        color: darkred;
        font-size: 15px;
        font-weight: bold;
    }
    .span_courseName {
        margin: 0 0 0 5px;
        color: crimson;
        font-size: 13px;
    }
    .div_feedbackText {
        font-size: 14px;
        color: #656266;
    }
    .div_feedbackTimeDate {
        color: #999999;
    }
    .v-container_button {
        padding-top: 0;
        padding-bottom: 0;
    }

</style>