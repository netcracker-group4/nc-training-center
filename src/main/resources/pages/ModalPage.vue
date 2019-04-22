<template>
    <div>
    <transition name="modal-fade">
        <div>
        <v-container>
            <v-layout>
                <v-flex xs12 sm12>

                </v-flex>
            </v-layout>
        </v-container>
        <div class="modal-backdrop">
            <div class="modal" role="dialog" aria-labelledby="modalTitle" aria-describedby="modalDescription">
                <section class="modal-body" id="modalDescription">
                    <slot name="body">
                        {{this.message}}
                    </slot>
                </section>
                <v-btn @click="close">submit</v-btn>
            </div>
        </div>
        </div>
    </transition>
    </div>
</template>

<script>
    export default {
        name: "ModalPage",
        props: ['message'],
        methods: {
            close() {
                this.$emit('close');
            }
        }
    }
</script>

<style scoped>
    .modal-backdrop {
        position: fixed;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        background-color: rgba(47, 47, 47, 0.3);
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .modal {
        background: #FFFFFF;
        box-shadow: 2px 2px 20px 1px;
        overflow-x: auto;
        display: flex;
        flex-direction: column;
        width: 50%;
    }

    .modal-header{
        padding: 15px;
        display: flex;
    }

    .modal-header {
        color: #000;
        justify-content: space-between;
    }


    .modal-body {
        position: relative;
        padding: 20px 10px;
        margin: 20px;
    }
</style>