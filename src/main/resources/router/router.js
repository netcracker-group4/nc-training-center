import Vue from 'vue'
import Router from 'vue-router'
import MainPage from '../pages/MainPage.vue'
import RegistrationPage from '../pages/RegistrationPage.vue'
import DashBoardPage from "../pages/DashBoardPage.vue";
import LoginPage from '../pages/LoginPage.vue'
import GroupPage from "../pages/GroupPage.vue";
import CoursePage from "../pages/CoursePage.vue";
import AdminCourses from "../pages/AdminCourses.vue";
import CreateCourse from "../pages/CreateCourse.vue";
import AllUsersPage from "../pages/AllUsersPage.vue";
import UserPage from "../pages/UserPage.vue";
import AttendancePage from "../pages/AttendancePage.vue";
import AbsenceReasons from '../pages/AbsenceReasons.vue'
import DesiredSchedulePage from "../pages/DesiredSchedulePage.vue";
import AttachmentsPage from "../pages/AttachmentsPage.vue"
import TestPage from '../pages/TestPage.vue'
import AllGroups from "../pages/AllGroups.vue";
import NotFound from "../pages/NotFound.vue";
import ChatPage from '../pages/ChatPage.vue'
import InfodeskRequest from "../pages/InfodeskRequest.vue";
import JoinCoursePage from "../pages/JoinCoursePage.vue";
import LessonPage from "../pages/LessonPage.vue";
import AccessDenied from "../pages/AccessDenied.vue";
import InfodeskPage from "../pages/InfodeskPage.vue";

Vue.use(Router);


const routes = [
    {path: '/', component: MainPage},

    // {path: '/login', component: LoginPage},
    {path: '/login', component: LoginPage},
    // {path: '/registration', component: RegistrationPage},
    {path: '/registration', component: RegistrationPage},

    // {path: '/dashboard', component: DashBoardPage},
    {path: '/dashboard', component: DashBoardPage},

    // {path: '/allgroups', component: AllGroups},
    {path: '/groups', component: AllGroups},
    // {path: '/group/:id', component: GroupPage, props: true},
    {path: '/groups/:id', component: GroupPage, props: true},
    // {path: '/lesson/:id', component: LessonPage},
    {path: '/lessons/:id', component: LessonPage},

    // {path: '/admincourses', component: AdminCourses},
    {path: '/courses', component: AdminCourses},
    // {path: '/coursecreate', component: CreateCourse},
    {path: '/courses/new', component: CreateCourse},
    // {path: '/courses/:id', component: CoursePage, props: true},
    {path: '/courses/:id', component: CoursePage, props: true},
    // {path: '/coursecreate/:id', component: CreateCourse,props: true},
    {path: '/courses/:id/edit', component: CreateCourse,props: true},
    // {path: '/courses/:id/join', component: JoinCoursePage, props: true},
    {path: '/courses/:id/join', component: JoinCoursePage, props: true},
    // {path: '/desired-schedule/:id', component: DesiredSchedulePage},
    {path: '/courses/:id/desired-schedule', component: DesiredSchedulePage},

    // {path: '/userpage', component: AllUsersPage},
    {path: '/users', component: AllUsersPage},
    // {path: '/userpage/:id', component: UserPage},
    {path: '/users/:id', component: UserPage},

    // {path: '/attendance', component: AttendancePage},
    {path: '/attendance', component: AttendancePage},
    // {path: '/absence-reasons', component: AbsenceReasons},
    {path: '/absence-reasons', component: AbsenceReasons},

    // {path: '/attachments-page', component: AttachmentsPage},
    {path: '/attachments', component: AttachmentsPage},

    // {path: '/test', component: TestPage},
    {path: '/test', component: TestPage},

    // {path: '/chat/:id', component: ChatPage},
    {path: '/chats/:id', component: ChatPage},

    // {path: '/infodesk', component: InfodeskPage},
    {path: '/requests', component: InfodeskPage},
    // {path: '/requests', component: InfodeskRequest},
    {path: '/requests/new', component: InfodeskRequest},

    {path: '/404', component: NotFound },
    {path: '/403', component: AccessDenied },
    {path: '*', redirect: '/404'}
];

export default new Router({
    mode: 'history',
    routes
})

