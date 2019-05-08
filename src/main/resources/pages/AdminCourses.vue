<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <v-toolbar flat color="white">
            <v-toolbar-title>Courses</v-toolbar-title>
            <span class="text-xs-right">
                <v-btn flat color="primary" @click="createCourse">Create new course</v-btn>
            </span>

        </v-toolbar>
        <v-data-table
                :headers="headers"
                :items="coursesAndQuantities"
                :expand="true"
                item-key="course.id"
        >
            <template v-slot:items="props">
                <tr>
                    <td class="my-link">
                        <div @click="goToCoursePage(props.item.course.id)" >{{ props.item.course.name }}</div>
                    </td>
                    <td class="text-xs-right">{{ props.item.numberOfEmployees }}</td>
                    <td class="text-xs-right" v-if="isAdmin">
                        <v-btn color="success" @click="update(props.item.course.id)">Update</v-btn>
                    </td>
                    <td class="text-xs-right" v-if="isAdmin">
                        <v-btn color="error" @click="deleteCourse(props.item.course.id)">Delete</v-btn>
                    </td>

                </tr>
            </template>
        </v-data-table>
    </div>
</template>
<script>
    import axios from 'axios'

    export default {
    name: "AdminCourses",
 data: function(){
            return{
            headers: [
            {
                text: 'Name of the course',
                    align: 'left',
                value: 'courseName'
            },
            {
                text: 'Number of employees', value: 'quantityOfEmployees',
                width: "20", align: 'right'
            },
                {
                    text: '', value: 'quantityOfEmployees',
                    width: "20", align: 'right'
                },
                {
                    text: '', value: 'quantityOfEmployees',
                    width: "20", align: 'right'
                },
            ],
                coursesAndQuantities: [],
                    isAdmin: this.$store.getters.isAdmin
            }
    },
    mounted() {
        let self = this;
        axios.get('http://localhost:8080/dashboard/training-and-quantity')
            .then(function (response) {
                self.coursesAndQuantities = response.data;
            })
            .catch(function (error) {
                console.log(error);
            });
    },
    methods: {
        goToCoursePage(id){
          this.$router.push('/courses/'+ id);
        },
        search(){

        },
        deleteCourse(courseId){
            if(confirm("Are you sure you want to delete "+this.findCourseById(courseId).name)){
                axios.delete('http://localhost:8080/getcourses/'+courseId)
                    .catch(function (error) {
                        console.log(error);
                    });
            }
        },
        findCourseById(id){
            return this.coursesAndQuantities.find(c => c.course.id===id).course;
        },
        update(courseId){
            this.$router.push('/coursecreate/'+ courseId);
        },
        createCourse(){
            this.$router.push('/coursecreate');
        }
    }
}
</script>


<style scoped>

</style>