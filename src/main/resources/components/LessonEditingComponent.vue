<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <form>
            <v-text-field
                    v-model="lesson.topic"
                    label="Name"
                    required
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
                                label="Date (read only text field)"
                                persistent-hint
                                prepend-icon="event"
                                readonly
                                v-on="on"
                        ></v-text-field>
                    </template>
                    <v-date-picker v-model="date" @input="menu = false"></v-date-picker>
                </v-menu>
                </v-flex>
                <v-flex md12 lg6>
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
                                    label="Picker in menu"
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
            <v-btn @click="save">save</v-btn>
            <v-btn @click="cancel">cancel</v-btn>

        </form>
    </div>

</template>

<script>
    export default {
        name: "LessonEditingComponent",
        props: ['currentLesson', 'trainers', 'attachments'],
        data: function () {
            return {
                lesson: this.currentLesson,
                date: this.currentLesson.timeDate.substr(0, 10),
                time: this.currentLesson.timeDate.substr(11, 20),
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
                menu : false,
                menu2 : false
            }
        },
        methods: {
            save() {
                this.lesson.timeDate = this.date + ' ' + this.time;
                this.lesson.attachments = this.selectedAttachments;
                this.$emit('saving-event', this.lesson);
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
            }
        },
        computed:{
            computedTime : function () {
                let [hours, minutes] = this.time.split(':');
                let modifier = +hours < 12 ? 'am' : 'pm';
                hours = +hours % 12 || 12;
                minutes = +minutes === 0 ? '' : `:${minutes}`;
                return hours + minutes + modifier
            }
        }

    }
</script>

<style scoped>
</style>