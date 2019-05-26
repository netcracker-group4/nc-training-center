<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-data-table :headers="headers" :items="this.data" :expand="true" class="elevation-1" item-key="id">
            <template v-slot:items="props">
                <tr>
                    <td @click="forwardTo(props.item.id)">
                        <div>{{props.item.title}}</div>
                    </td>
                </tr>
            </template>
        </v-data-table>
    </div>
</template>

<script>
    export default {
        name: "AttendanceGroupBoard",
        props: ['data'],
        data() {
            return {
                item: null,
                headers: [
                    {
                        text: 'Group title',
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