<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-container>
        <v-layout row wrap>
            <v-flex xs12 sm6>
            </v-flex>
        </v-layout>
        <v-layout row wrap>
            <v-flex xs12>
                <template>
                   <v-data-table
                    :headers="headers"
                    :items="reasons"
                    :expand="true"
                    item-key="id"
                   >
                     <template v-slot:items="props">
                        <tr>
                           <td class="my-link">
                               <div>{{ props.item.title }}</div>
                           </td>
                           <td>
                                <v-btn color="error" @click="deleteReason(props.item.id)">Delete</v-btn>
                           </td>
                        </tr>
                     </template>
                   </v-data-table>
                </template>
            </v-flex>
        </v-layout>
        <v-layout>
            <v-container>
                <v-layout row wrap>
                    <v-flex xs12 sm6>
                        <v-text-field v-model="title" label="New reason title" clearable></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm12>
                        <v-btn @click="add">Add</v-btn>
                    </v-flex>
                </v-layout>
            </v-container>
        </v-layout>

    </v-container>

</template>

<script>

    import axios from 'axios'

    export default {
        name: "AbsenceReasons",
        data: function(){
          return{
              title: null,
              headers: [
                  {
                      text: 'Absent Reasons',
                      align: 'left',
                      value: 'title'
                  }              ],
              reasons: [],
              isAdmin: 'true'

          }
        },
        mounted() {
            let self = this;
            axios.get('http://localhost:8080/absence-reason')
                            .then(function (response) {
                                self.reasons= response.data;
                            })
                            .catch(function (error) {
                                console.log(error);
                            });


        },
        methods: {
            deleteReason(idReason){
                axios.delete('http://localhost:8080/absence-reason/' + idReason)
                    .catch(function (error) {
                        console.log(error);
                    });

            },
            add(){
                let form = new FormData();
                let request = new XMLHttpRequest();
                request.open('POST', 'http://localhost:8080/absence-reason');
                form.append('title', this.title);
                request.send(form);
                /*axios.post('http://localhost:8080/absence-reason', {
                    title: this.title
                })
                    .then(response => alert("Absence Reason created"))*/
            }
        }
    }

</script>
