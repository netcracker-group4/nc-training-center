<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-data-table :headers="headers" :items="this.data" :expand="true" class="elevation-1" item-key="id">
            <template v-slot:items="props">
                <tr>
                    <td @click="forwardTo(props.item.id)">
                        <div>{{ props.item.firstName + ' ' + props.item.lastName}}</div>
                    </td>
                    <td class="text-xs-right">{{ props.item.groups.length }}</td>
                </tr>
            </template>
        </v-data-table>
    </div>
</template>

<script>
    export default {
        name: "AttendanceTrainerBoard",
        props: ['data'],
        data() {
            return {
                item: null,
                headers: [
                    {
                        text: 'Trainer name',
                        align: 'left',
                        value: 'level.title'
                    },
                    {
                        text: 'Number of groups', value: 'quantityOfGroups',
                        width: "20", align: 'right'
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
    .group-table {
        background-color: #A5D6A7;
    }

</style>