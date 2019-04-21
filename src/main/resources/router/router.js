import Vue from 'vue'
import Router from 'vue-router'
import MainPage from '../pages/MainPage.vue'
import RegistrationPage from '../pages/RegistrationPage.vue'
import DashBoardPage from "../pages/DashBoardPage.vue";
import AdminCourses from "../pages/AdminCourses.vue";


Vue.use(Router);

const routes = [
    {path: '/', component: MainPage},
    {path: '/registration', component: RegistrationPage},
    {path: '/dashboard', component: DashBoardPage},
    {path: '/admincourses', component: AdminCourses}
];

export default new Router({
    mode: 'history',
    routes
})

