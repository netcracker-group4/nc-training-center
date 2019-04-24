<template>
    <student-line-in-table></student-line-in-table>
</template>

<script>
    import StudentLineInTable from "../components/StudentLineInTable.vue";

    export default {
        name: "DesiredSchedulePage",
        components: {
            'student-line-in-table' :StudentLineInTable
        }
    }
</script>

<style scoped>

</style>