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
                                <v-list-tile-avatar class="cursor-pointer feedback_avatar">
                                    <v-icon v-if="$store.state.user.imageUrl == null">account_circle</v-icon>
                                    <v-avatar size="70">
                                        <v-img :src="$store.state.apiServer + '/api/users/image?url=' + $store.state.user.imageUrl"
                                               aspect-ratio="2"/>
                                    </v-avatar>
                                </v-list-tile-avatar>
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
                                    <v-container class="v-container_button"
                                                 style="display:flex; justify-content: flex-end; margin-top: 10px;">
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
                                    <v-list-tile-avatar class="cursor-pointer feedback_avatar"
                                                        @click="forwardToUser(props.item.teacher.id)">
                                        <v-icon v-if="props.item.teacher.imageUrl == null">account_circle</v-icon>
                                        <v-avatar size="70">
                                            <v-img :src="$store.state.apiServer + '/api/users/image?url=' + props.item.teacher.imageUrl"
                                                   aspect-ratio="2"/>
                                        </v-avatar>
                                    </v-list-tile-avatar>
                                </td>
                                <td style="vertical-align: top;">
                                    <div class="div_teacherName">
                                        <span class="cursor-pointer"
                                              @click="forwardToUser(props.item.teacher.id)">
                                            {{ props.item.teacher.firstName }} {{ props.item.teacher.lastName }}
                                        </span>
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
                                        {{ props.item.time }}
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
        data: () => ({
            feedbackText: '',
            text: '',
            userFeedback: [],
            defaultFeedback: '',
            selectCourse: {
                id: ''
            },
            defaultSelectCourse: {
                id: ''
            },
            coursesName: [],
            rows: [3, 5, 10, {"text": "$vuetify.dataIterator.rowsPerPageAll", "value": -1}],
            show: false,
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
                        axios.post(this.$store.state.apiServer + '/api/feedback/add', {
                            studentId: this.user.id,
                            teacher: this.getAuthorizationUser(),
                            course: this.selectCourse,
                            text: this.feedbackText,
                        }).then(response => {
                            self.text = self.feedbackText;
                            self.feedbackText = '';
                            self.selectCourse = Object.assign({}, self.defaultSelectCourse);
                            this.sendMessageToManager();
                            console.log(response);
                            this.getFeedback();
                            this.$validator.reset();
                        }).catch(function (error) {
                            console.log(error);
                        });
                    }
                })

            },
            sendMessageToManager() {
                axios.post(this.$store.state.apiServer + '/api/email', {
                    studentId: this.user.id,
                    teacher: this.getAuthorizationUser(),
                    course: this.selectCourse,
                    text: this.text,
                }).then(response => {
                    console.log(response);
                }).catch(function (error) {
                    console.log(error);
                });
            },
            forwardToUser(id) {
                this.$router.push('/users/' + id);
            },
            isNotOnlyTrainer() {
                return store.state.userRoles.includes("ADMIN") ||
                    store.state.userRoles.includes("MANAGER") ||
                    store.state.userRoles.includes("EMPLOYEE");
            },
            getAllFeedback() {
                let self = this;
                axios.get(this.$store.state.apiServer + '/api/feedback/get-by-user?userId=' + this.$route.params.id)
                    .then(function (response) {
                        self.userFeedback = response.data;
                        console.log(self.userFeedback)
                    })
                    .catch(function (error) {
                        console.log(error);
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
                    });
            },
            deleteFeedback(item) {
                let self = this;
                axios.delete(this.$store.state.apiServer + '/api/feedback/delete/' + item.id)
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
                axios.get(this.$store.state.apiServer + '/api/feedback/get-by-rainer-and-by-user?userId=' +
                    this.$route.params.id + "&trainerId=" + store.state.user.id)
                    .then(function (response) {
                        self.userFeedback = response.data;
                        console.log(self.userFeedback)
                    })
                    .catch(function (error) {
                        if (error.response != null && error.response.status == 400)
                            self.$router.push('/404');
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

    .feedback_avatar {
        padding: 20px 0 0 0;
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

    .div_feedbackTimeDate {
        color: #999999;
    }

    .v-container_button {
        padding-top: 0;
        padding-bottom: 0;
    }

    .cursor-pointer {
        cursor: pointer;
    }

</style>