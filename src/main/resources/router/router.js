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
import NewUserPage from "../pages/NewUserPage.vue";
import AttendancePage from "../pages/AttendancePage.vue";
import AbsenceReasons from '../pages/AbsenceReasons.vue'
import DesiredSchedulePage from "../pages/DesiredSchedulePage.vue";
import AttachmentsPage from "../pages/AttachmentsPage.vue"
import TestPage from '../pages/TestPage.vue'
import GroupSchedulePage from "../pages/GroupSchedulePage.vue";
import AllGroups from "../pages/AllGroups.vue";
import NotFound from "../pages/NotFound.vue";
import ChatPage from '../pages/ChatPage.vue'
import JoinCoursePage from "../pages/JoinCoursePage.vue";
import LessonPage from "../pages/LessonPage.vue";

Vue.use(Router);


const routes = [
    {path: '/', component: MainPage},
    {path: '/registration', component: RegistrationPage},
    {path: '/dashboard', component: DashBoardPage},
    {path: '/login', component: LoginPage},
    {path: '/dashboard', component: DashBoardPage},
    {path: '/group/:id', component: GroupPage, props: true},
    {path: '/groups/:id/schedule', component: GroupSchedulePage},
    {path: '/courses/:id', component: CoursePage, props: true},
    {path: '/courses/:id/join', component: JoinCoursePage, props: true},
    {path: '/admincourses', component: AdminCourses},
    {path: '/coursecreate', component: CreateCourse},
    {path: '/coursecreate/:id', component: CreateCourse,props: true},
    {path: '/userpage', component: AllUsersPage},
    {path: '/userpage/:id', component: UserPage},
    {path: '/add-user', component: NewUserPage},
    {path: '/attendance', component: AttendancePage},
    {path: '/absence-reasons', component: AbsenceReasons},
    {path: '/desired-schedule/:id', component: DesiredSchedulePage},
    {path: '/attachments-page', component: AttachmentsPage},
    {path: '/lesson/:id', component: LessonPage},
    {path: '/test', component: TestPage},
    {path: '/allgroups', component: AllGroups},
    {path: '/chat/:id', component: ChatPage},
    {path: '/404', component: NotFound },
    {path: '*', redirect: '/404'}
];

export default new Router({
    mode: 'history',
    routes
})

