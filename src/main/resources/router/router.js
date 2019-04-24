import Vue from 'vue'
import Router from 'vue-router'
import MainPage from '../pages/MainPage.vue'
import RegistrationPage from '../pages/RegistrationPage.vue'
import DashBoardPage from "../pages/DashBoardPage.vue";
import LoginPage from '../pages/LoginPage.vue'
import GroupPage from "../pages/GroupPage.vue";
import CoursePage from "../pages/CoursePage.vue";
import TrainerPage from "../pages/TrainerPage.vue";
import AdminCourses from "../pages/AdminCourses.vue";
import CreateCourse from "../pages/CreateCourse.vue";
import AbsenceReasons from "../pages/AbsenceReasons.vue";
import AllUsersPage from "../pages/AllUsersPage.vue";
import UserPage from "../pages/UserPage.vue";



Vue.use(Router);

const routes = [
    {path: '/', component: MainPage},
    {path: '/registration', component: RegistrationPage},
    {path: '/dashboard', component: DashBoardPage},
    {path: '/login', component: LoginPage},
    {path: '/dashboard', component: DashBoardPage},
    {path: '/groups/:id', component: GroupPage},
    {path: '/courses/:id', component: CoursePage},
    {path: '/trainers/:id', component: TrainerPage},
    {path: '/admincourses', component: AdminCourses},
    {path: '/coursecreate', component: CreateCourse},
    {path: '/absencereasons', component: AbsenceReasons},
    {path: '/userpage', component: AllUsersPage},
    {path: '/userpage/:id', component: UserPage}

];

export default new Router({
    mode: 'history',
    routes
})

