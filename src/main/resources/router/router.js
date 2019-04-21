import Vue from 'vue'
import Router from 'vue-router'
import MainPage from '../pages/MainPage.vue'
import RegistrationPage from '../pages/RegistrationPage.vue'
import DashBoardPage from "../pages/DashBoardPage.vue";
import LoginPage from '../pages/LoginPage.vue'


Vue.use(Router);

const routes = [
    {path: '/', component: MainPage},
    {path: '/registration', component: RegistrationPage},
    {path: '/dashboard', component: DashBoardPage},
    {path: '/login', component: LoginPage}
];

export default new Router({
    mode: 'history',
    routes
})

