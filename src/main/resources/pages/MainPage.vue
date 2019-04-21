<template>
    <v-container>
        <v-layout row wrap >
            <v-flex xs12 sm10 offset-sm1 class="course-item">
                <v-card>
                    <v-img src="http://www.stgeorges.co.uk/sites/default/files/Business%20English%20Courses%20London.png" aspect-ratio="2.75"></v-img>
                    <v-card-title primary-title>
                        <div>
                            <h3 class="headline mb-0">English course</h3>
                        </div>
                    </v-card-title>
                    <v-card-actions>
                        <v-btn flat>more</v-btn>
                    </v-card-actions>
                </v-card>
            </v-flex>
            <v-flex xs12 sm10 offset-sm1 class="course-item">
                <v-card>
                    <v-img src="https://cdn.lynda.com/course/529631/529631-636531738331126522-16x9.jpg" aspect-ratio="2.75"></v-img>
                    <v-card-title primary-title>
                        <div>
                            <h3 class="headline mb-0">Database course</h3>
                        </div>
                    </v-card-title>
                    <v-card-actions>
                        <v-btn flat>more</v-btn>
                    </v-card-actions>
                </v-card>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    export default {
        name: "MainPage",
    }
</script>

<style scoped>
    .course-item{
        margin-bottom: 40px;
    }
</style>