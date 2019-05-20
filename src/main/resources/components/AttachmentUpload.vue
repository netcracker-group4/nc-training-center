<template>
    <v-container>
        <v-layout row wrap>
            <v-flex xs12 sm6>
                <v-text-field v-model="lessonId" label="Lesson Id" clearable></v-text-field>
            </v-flex>
            <v-flex xs12 sm4>

                <form id="uploadForm" name="uploadForm" enctype="multipart/form-data">

                    <input type="file" id="file" name="file" ><br>
                </form>
            </v-flex>

            <v-flex xs12 sm12>
                <v-btn @click="uploadFile">Upload</v-btn>
            </v-flex>
        </v-layout>
    </v-container>
</template>


<script>

    export default {
        name: "AttachmentUpload",
        data(){
            return{
                lessonId: null
            }
        },

        mounted() {
        },
        methods:{
            uploadFile(){
                let form = new FormData(document.getElementById('uploadForm'));
                let imagefile = document.querySelector('#file');

                let request = new XMLHttpRequest();
                request.open('POST', 'http://localhost:8080/api/attachments/upload-file');
                form.append('file', imagefile.files[0]);
                form.append('lessonId',this.lessonId);
                request.send(form);

            }
        }
    }
</script>

<style scoped>

</style>