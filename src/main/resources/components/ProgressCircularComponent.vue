<template>
    <div class="text-xs-center">
        <v-progress-circular
                :size="70"
                :width="7"
                color="primary"
                indeterminate
        ></v-progress-circular>
    </div>
</template>

<script>
    export default {
        name: "progress-circular-component"
    }
</script>

<style scoped>
    .text-xs-center {
        width: 100%;
        height: 100%;
        position: fixed;
        top: 0;
        left: 0;
        display: flex;
        align-items: center;
        align-content: center;
        justify-content: center;
        overflow: auto;
    }
</style>