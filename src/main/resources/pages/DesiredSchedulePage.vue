<template>
    <v-container>
        <student-line-in-table></student-line-in-table>
    </v-container>
</template>

<script>
    import StudentLineInTable from "../components/DesiredScheduleDraggableTable.vue";

    export default {
        name: "DesiredSchedulePage",
        components: {
            'student-line-in-table': StudentLineInTable
        },
         data: function () {
             return{
             }
         }
    }
</script>

<style scoped>

</style>