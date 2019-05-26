<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-data-table :headers="headers" :items="this.data" :expand="true" class="elevation-1" item-key="id">
            <template v-slot:items="props">
                <tr>
                    <td @click="forwardTo(props.item.id)">
                        <div>{{props.item.firstName + ' ' + props.item.lastName}}</div>
                    </td>
                    <!--<td class="text-xs-right">{{ props.item.lessons.length }}</td>-->
                </tr>
            </template>
        </v-data-table>
    </div>
</template>

<script>
    export default {
        name: "AttendanceStudentsBoard",
        props: ['data'],
        data() {
            return {
                item: null,
                headers: [
                    {
                        text: 'Student name',
                        align: 'left',
                        value: 'level.title'
                    },
                ],
            }
        },
        methods: {
            forwardTo(item) {
                this.$emit('forward', item);
            }
        }
    }
</script>

<style scoped>

</style>