import Vue from 'vue';
import Router from 'vue-router';
import MainPage from '../pages/MainPage.vue';
import RegistrationPage from '../pages/RegistrationPage.vue';
import DashBoardPage from "../pages/DashBoardPage.vue";
import LoginPage from '../pages/LoginPage.vue';
import GroupPage from "../pages/GroupPage.vue";
import CoursePage from "../pages/CoursePage.vue";
import AdminCourses from "../pages/AdminCourses.vue";
import CreateCourse from "../pages/CreateCourse.vue";
import AllUsersPage from "../pages/AllUsersPage.vue";
import UserPage from "../pages/UserPage.vue";
import AttendancePage from "../pages/AttendancePage.vue";
import AbsenceReasons from '../pages/AbsenceReasons.vue';
import DesiredSchedulePage from "../pages/DesiredSchedulePage.vue";
import TestPage from '../pages/TestPage.vue';
import AllGroups from "../pages/AllGroups.vue";
import NotFound from "../pages/NotFound.vue";
import ChatPage from '../pages/ChatPage.vue';
import InfodeskRequest from "../pages/InfodeskRequest.vue";
import JoinCoursePage from "../pages/JoinCoursePage.vue";
import LessonPage from "../pages/LessonPage.vue";
import AccessDenied from "../pages/AccessDenied.vue";
import InfodeskPage from "../pages/InfodeskPage.vue";

Vue.use(Router);


const routes = [
    {path: '/', component: MainPage},

    {path: '/login', component: LoginPage},
    {path: '/registration', component: RegistrationPage},

    {path: '/dashboard', component: DashBoardPage},

    {path: '/groups', component: AllGroups},
    {path: '/groups/:id', component: GroupPage, props: true},
    {path: '/lessons/:id', component: LessonPage},

    {path: '/courses', component: AdminCourses},
    {path: '/courses/new', component: CreateCourse},
    {path: '/courses/:id', component: CoursePage, props: true},
    {path: '/courses/:id/edit', component: CreateCourse, props: true},
    {path: '/courses/:id/join', component: JoinCoursePage, props: true},
    {path: '/courses/:id/desired-schedule', component: DesiredSchedulePage},

    {path: '/users', component: AllUsersPage},
    {path: '/users/:id', component: UserPage},

    {path: '/attendance', component: AttendancePage},
    {path: '/absence-reasons', component: AbsenceReasons},

    {path: '/test', component: TestPage},

    {path: '/chats/:id', component: ChatPage},

    {path: '/requests', component: InfodeskPage},
    {path: '/requests/new', component: InfodeskRequest},

    {path: '/404', component: NotFound},
    {path: '/403', component: AccessDenied},
    {path: '*', redirect: '/404'}
];

export default new Router({
    mode: 'history',
    routes
})

