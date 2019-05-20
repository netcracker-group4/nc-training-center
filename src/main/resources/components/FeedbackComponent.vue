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
                        <form>
                            <v-container style="display:flex; justify-content: space-between;">
                                <v-avatar size="60px">
                                    <img
                                            src="https://avatars0.githubusercontent.com/u/9064066?v=4&s=460"
                                            alt="Avatar"/>
                                </v-avatar>
                                <div style="width: 90%;">
                                    <v-textarea
                                            box
                                            full-width
                                            hide-details
                                            outline
                                            auto-grow
                                            rows="4"
                                            placeholder="Leave feedback"
                                            v-model="feedbackText"
                                            v-validate="'required'"
                                            :error-messages="errors.collect('feedback')"
                                            data-vv-name="feedback"
                                            required
                                    ></v-textarea>
                                    <v-container class="v-container_button" style="display:flex; justify-content: flex-end; margin-top: 10px;">
                                        <v-flex xs12 sm6 d-flex>
                                            <v-select
                                                    v-model="selectCourse.id"
                                                    :items="courses"
                                                    item-text="name"
                                                    item-value="id"
                                                    color="blue"
                                                    box
                                                    background-color="white"
                                                    label="Select course"
                                                    v-validate="'required'"
                                                    :error-messages="errors.collect('select')"
                                                    data-vv-name="select"
                                                    required
                                            ></v-select>
                                        </v-flex>
                                        <div style="margin: 10px 0 0 30px;">
                                            <v-btn @click="leaveFeedback()" color="info">Submit</v-btn>
                                        </div>
                                    </v-container>
                                </div>
                            </v-container>
                        </form>
                    </template>
                    <v-data-table
                            :items="userFeedback"
                            hide-headers
                            :rows-per-page-items="rows"
                            :expand="false"
                            item-key="id"
                    >
                        <template v-slot:items="props">
                            <div class="table_item" style="position: relative"
                                 @mouseenter="props.expanded = true" @mouseleave="props.expanded = false">
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
                                        <div class="feedback_buttons"
                                             :class="{ show_buttons: props.expanded }"
                                             v-if="canShowButtons()"
                                        >
                                            <div class="delete" @click="deleteFeedback(props.item)">
                                                <v-icon>clear</v-icon>
                                            </div>
                                        </div>
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
        $_veeValidate: {
            validator: 'new'
        },
        name: 'feedback-component',
        props: [
            'user',
            'courses'
        ],
        data:() => ({
            feedbackText: '',
            userFeedback: [],
            defaultFeedback: '',
            selectCourse: {
                id: ''
            },
            defaultSelectCourse: {
                id: ''
            },
            coursesName: [],
            rows: [3,5,10,{"text":"$vuetify.dataIterator.rowsPerPageAll","value":-1}],
            show: false,
        }),
        methods: {
            getAuthorizationUser() {
                return store.state.user;
            },
            canShowLeaveFeedbackBlock() {
                return store.state.userRoles.includes("TRAINER") && this.courses.length > 0;
            },
            canShowButtons() {
                return store.state.userRoles.includes("ADMIN") ||
                    store.state.userRoles.includes("TRAINER");
            },
            leaveFeedback() {
                this.$validator.validateAll().then((result) => {
                    if (result) {
                        let self = this;
                        axios.post('/api/feedback/add', {
                            studentId: this.user.id,
                            teacher: this.getAuthorizationUser(),
                            course: this.selectCourse,
                            text: this.feedbackText,
                        }).then(response => {
                            self.feedbackText = '';
                            self.selectCourse = Object.assign({}, self.defaultSelectCourse);
                            console.log(response);
                            this.getFeedback();
                            this.$validator.reset();
                        }).catch(function (error) {
                            console.log(error);
                        });
                    }
                })

            },
            isNotOnlyTrainer() {
                return store.state.userRoles.includes("ADMIN") ||
                    store.state.userRoles.includes("MANAGER") ||
                    store.state.userRoles.includes("EMPLOYEE");
            },
            getAllFeedback() {
                let self = this;
                axios.get('/api/feedback/get-by-user?userId=' + this.$route.params.id)
                    .then(function (response) {
                        self.userFeedback = response.data;
                        console.log(self.userFeedback)
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
            },
            deleteFeedback(item) {
                let self = this;
                axios.delete('/api/feedback/delete/' + item.id)
                    .then(response => {
                        console.log(response);
                        if (response.status === 200) {
                            let index = self.userFeedback.indexOf(item);
                            self.userFeedback.splice(index, 1);
                        } else {

                        }
                    })
            },
            getAllFeedbackByTrainer() {
                let self = this;
                axios.get('/api/feedback/get-by-rainer-and-by-user?userId=' +
                    this.$route.params.id + "&trainerId=" + store.state.user.id)
                    .then(function (response) {
                        self.userFeedback = response.data;
                        console.log(self.userFeedback)
                    })
                    .catch(function (error) {
                        console.log(error);
                    });
            },
            getFeedback() {
                if (this.isNotOnlyTrainer()) {
                    this.getAllFeedback();
                } else {
                    this.getAllFeedbackByTrainer();
                }
            },
        },
        mounted() {
            this.$validator.localize('en', this.dictionary)
            this.getFeedback();
        },
        watch: {
            '$route'(to, from) {
                this.getFeedback();
            }
        },
        dictionary: {
            custom: {
                feedback: {
                    required: () => 'Feedback can not be empty',
                    max: 'The name field may not be greater than 10 characters'
                    // custom messages
                },
                select: {
                    required: 'Select field is required'
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
    .feedback_buttons {
        position: absolute;
        width: 20px;
        height: 20px;
        top: 20px;
        right: 10px;
        /*display: none;*/
        opacity: 0;
    }
    .feedback_buttons .delete {
        cursor: pointer;
    }
    .show_buttons {
        /*display: block;*/
        opacity: 1;
        transition-duration: 0.5s;
    }
    .div_feedbackTimeDate {
        color: #999999;
    }
    .v-container_button {
        padding-top: 0;
        padding-bottom: 0;
    }

</style>