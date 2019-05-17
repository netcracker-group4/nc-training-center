<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div style="margin-top: 50px;">
        <v-layout style="margin-bottom: 20px">
            <v-expansion-panel>
                <v-expansion-panel-content>
                    <!--suppress HtmlUnknownBooleanAttribute -->
                    <template v-slot:header>
                        <div>Desired schedule for group</div>
                    </template>
                    <group-schedule :group-id="lesson.groupId"></group-schedule>
                </v-expansion-panel-content>
            </v-expansion-panel>
        </v-layout>
        <form>
            <v-text-field
                    v-model="lesson.topic"
                    label="Name"
                    required
                    :data-vv-name="'lesson topic'"
                    v-validate="'required|max:256'"
                    :error-messages="errors.collect('lesson topic')"
            ></v-text-field>
            <v-select
                    v-model="lesson.trainerId"
                    :items="trainers"
                    item-value="id"
                    :item-text="getName"
                    required
            ></v-select>
            <v-layout row wrap>
                <v-flex>
                    <v-menu
                            v-model="menu"
                            :close-on-content-click="false"
                            :nudge-right="40"
                            lazy
                            transition="scale-transition"
                            offset-y
                            full-width
                            max-width="290px"
                            min-width="290px"
                    >
                        <template v-slot:activator="{ on }">
                            <v-text-field
                                    v-model="date"
                                    label="Date"
                                    persistent-hint
                                    prepend-icon="event"
                                    readonly
                                    v-on="on"
                            ></v-text-field>
                        </template>
                        <v-date-picker v-model="date" @input="menu = false"></v-date-picker>
                    </v-menu>
                </v-flex>
                <v-flex>
                    <v-menu
                            ref="menu"
                            v-model="menu2"
                            :close-on-content-click="false"
                            :nudge-right="40"
                            :return-value.sync="time"
                            lazy
                            transition="scale-transition"
                            offset-y
                            full-width
                            max-width="290px"
                            min-width="290px"
                    >
                        <template v-slot:activator="{ on }">
                            <v-text-field
                                    v-model="computedTime"
                                    label="Start time"
                                    prepend-icon="access_time"
                                    readonly
                                    v-on="on"
                            ></v-text-field>
                        </template>
                        <v-time-picker
                                v-if="menu2"
                                v-model="time"
                                full-width
                                @click:minute="$refs.menu.save(time)"
                        ></v-time-picker>
                    </v-menu>
                </v-flex>
                <v-flex>
                    <v-menu
                            ref="menu2"
                            v-model="menu3"
                            :close-on-content-click="false"
                            :nudge-right="40"
                            :return-value.sync="lesson.duration"
                            lazy
                            transition="scale-transition"
                            offset-y
                            full-width
                            max-width="290px"
                            min-width="290px"
                    >
                        <template v-slot:activator="{ on }">
                            <v-text-field
                                    v-model="lesson.duration"
                                    label="Duration"
                                    prepend-icon="access_time"
                                    readonly
                                    v-on="on"
                            ></v-text-field>
                        </template>
                        <v-time-picker
                                v-if="menu3"
                                format="24hr"
                                v-model="lesson.duration"
                                full-width
                                @click:minute="$refs.menu2.save(lesson.duration)"
                        ></v-time-picker>
                    </v-menu>
                </v-flex>
                <v-flex>
                    <v-menu
                            :close-on-content-click="false"
                            :nudge-right="40"
                            lazy
                            offset-y
                            full-width
                            max-width="290px"
                            min-width="290px"
                    >
                        <template v-slot:activator="{ on }">
                            <v-text-field
                                    v-model="approximateEndTime"
                                    label="Approximate end time"
                                    persistent-hint
                                    prepend-icon="event"
                                    readonly
                            ></v-text-field>
                        </template>
                    </v-menu>
                </v-flex>
            </v-layout>
            <v-layout>
                <div class="text-xs-left" style="margin-bottom: 20px;">
                    <v-chip close @input="remove(attachment)" v-for="attachment in selectedAttachments"
                            :key="attachment.id">{{attachment.description}}
                    </v-chip>
                </div>
            </v-layout>
            <v-data-table
                    v-model="selectedAttachments"
                    :headers="headers"
                    :items="attachments"
                    :pagination.sync="pagination"
                    select-all
                    item-key="id"
                    class="elevation-1"
            >
                <template v-slot:headers="props">
                    <tr>
                        <th>
                            <v-checkbox
                                    :input-value="props.all"
                                    :indeterminate="props.indeterminate"
                                    primary
                                    hide-details
                                    @click.stop="toggleAll"
                            ></v-checkbox>
                        </th>
                        <th
                                v-for="header in props.headers"
                                :key="header.text"
                                :class="['column sortable', pagination.descending ? 'desc' : 'asc', header.value === pagination.sortBy ? 'active' : '']"
                                @click="changeSort(header.value)"
                        >
                            <v-icon small>arrow_upward</v-icon>
                            {{ header.text }}
                        </th>
                    </tr>
                </template>
                <template v-slot:items="props">
                    <tr :active="props.selected" @click="props.selected = !props.selected">
                        <td>
                            <v-checkbox
                                    :input-value="props.selected"
                                    primary
                                    hide-details
                            ></v-checkbox>
                        </td>
                        <td>{{ props.item.description }}</td>
                        <td class="text-xs-right">{{ props.item.url }}</td>
                    </tr>
                </template>
            </v-data-table>
            <v-layout style="margin-top: 20px">
                <v-spacer></v-spacer>
                <v-btn color="error" @click="deleteLesson">delete</v-btn>
                <v-btn color="success" @click="save">save</v-btn>
                <v-btn color="warning" @click="cancel">cancel</v-btn>
            </v-layout>
        </form>
    </div>

</template>

<script>
    import groupSchedule from './GroupSchedule.vue';

    export default {
        name: "LessonEditingComponent",
        props: ['currentLesson', 'trainers', 'attachments'],
        data: function () {
            return {
                lesson: this.currentLesson,
                date: this.currentLesson.timeDate.substr(0, 10),
                time: this.currentLesson.timeDate.substr(11, 5),
                appendix: this.currentLesson.timeDate.substr(16, 6),
                selectedAttachments: this.currentLesson.attachments,
                headers: [
                    {
                        text: 'Attachments',
                        align: 'right',
                        value: 'description'
                    },
                    {
                        text: 'url', align: 'right',
                        value: 'url'
                    }
                ],
                pagination: {
                    sortBy: 'description'
                },
                menu: false,
                menu2: false,
                menu3: false,

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
            save() {
                let self = this;
                this.$validator.validateAll().then((result) => {
                    if (result) {
                        self.lesson.timeDate = self.date + ' ' + self.time + self.appendix;
                        self.lesson.attachments = self.selectedAttachments;
                        let trainer = self.trainers.filter(e => {
                            return e.id === self.lesson.trainerId;
                        })[0];
                        self.lesson.trainerName = trainer.firstName + ' ' + trainer.lastName;
                        self.$emit('saving-event', self.lesson);
                        return;
                    }
                    if(!result){
                        self.errorAutoClosable("Valid lesson topic required")
                    }
                });
            },
            deleteLesson() {
                this.$emit('delete-event', this.lesson);
            },
            cancel() {
                this.$emit('cancel-event', this.lesson);
            },
            getName: item => item.firstName + ' ' + item.lastName,
            toggleAll() {
                if (this.selectedAttachments.length) this.selectedAttachments = [];
                else this.selectedAttachments = this.attachments.slice()
            },
            changeSort(column) {
                if (this.pagination.sortBy === column) {
                    this.pagination.descending = !this.pagination.descending
                } else {
                    this.pagination.sortBy = column;
                    this.pagination.descending = false
                }
            },
            remove(attachment) {
                this.selectedAttachments = this.selectedAttachments.filter(el => el.id !== attachment.id);
            },
            toDateFunction(str, format) {
                var normalized = str.replace(/[^a-zA-Z0-9]/g, '-');
                var normalizedFormat = format.toLowerCase().replace(/[^a-zA-Z0-9]/g, '-');
                var formatItems = normalizedFormat.split('-');
                var dateItems = normalized.split('-');

                var monthIndex = formatItems.indexOf("mm");
                var dayIndex = formatItems.indexOf("dd");
                var yearIndex = formatItems.indexOf("yyyy");
                var hourIndex = formatItems.indexOf("hh");
                var minutesIndex = formatItems.indexOf("ii");
                var secondsIndex = formatItems.indexOf("ss");

                var today = new Date();

                var year = yearIndex > -1 ? dateItems[yearIndex] : today.getFullYear();
                var month = monthIndex > -1 ? dateItems[monthIndex] - 1 : today.getMonth() - 1;
                var day = dayIndex > -1 ? dateItems[dayIndex] : today.getDate();

                var hour = hourIndex > -1 ? dateItems[hourIndex] : today.getHours();
                var minute = minutesIndex > -1 ? dateItems[minutesIndex] : today.getMinutes();
                var second = secondsIndex > -1 ? dateItems[secondsIndex] : today.getSeconds();

                return new Date(year, month, day, hour, minute, second);
            },
            ampmTime(time) {
                let [hours, minutes] = time.split(':');
                let modifier = +hours < 12 ? 'am' : 'pm';
                hours = +hours % 12 || 12;
                minutes = +minutes === 0 ? '' : `:${minutes}`;
                return hours + minutes + modifier
            }
        },
        computed: {
            computedTime: function () {
                return this.ampmTime(this.time);
            },
            approximateEndTime: function () {
                let [hours, minutes] = this.currentLesson.duration.split(':');
                let date = this.toDateFunction(this.date + ' ' + this.time, "yyyy-mm-dd hh:ii");
                date.setHours(date.getHours() + parseInt(hours));
                date.setMinutes(date.getMinutes() + parseInt(minutes));
                return date.toDateString() + '  ' + this.ampmTime(date.toTimeString());
            }
        },
        components: {groupSchedule}
    }
</script>

<style scoped>
</style>