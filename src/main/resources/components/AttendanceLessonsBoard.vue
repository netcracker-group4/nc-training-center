<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-data-table :headers="headers" :items="this.data" :expand="true" class="elevation-1" item-key="id">
            <template v-slot:items="props">
                <tr>
                    <td>
                        <div>{{props.item.topic}}</div>
                    </td>
                    <td>{{props.item.status}}</td>
                    <td>{{props.item.reason}}</td>
                </tr>
            </template>
        </v-data-table>
    </div>
</template>

<script>
    export default {
        name: "AttendanceLessonsBoard",
        props: ['data'],
        data() {
            return {
                item: null,
                headers: [
                    {
                        text: 'Lesson topic',
                        align: 'left',
                        value: 'level.title'
                    },
                    {
                        text: 'Absence', value: 'quantityOfGroups',
                        width: "20", align: 'left'
                    },
                    {
                        text: 'Absence reason', value: 'quantityOfGroups',
                        width: "20", align: 'left'
                    },
                ],
            }
        }
    }
</script>

<style scoped>

</style>