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
import AllUsersPage from "../pages/AllUsersPage.vue";
import UserPage from "../pages/UserPage.vue";
import NewUserPage from "../pages/NewUserPage.vue";
import AttendancePage from "../pages/AttendancePage.vue";
import AbsenceReasons from '../pages/AbsenceReasons.vue'
import DesiredSchedulePage from "../pages/DesiredSchedulePage.vue";
import VueDraggable from 'vuedraggable';
import AttachmentUpload from "../pages/AttachmentUpload.vue"
import TestPage from '../pages/TestPage.vue'
import GroupSchedulePage from "../pages/GroupSchedulePage.vue";

Vue.use(Router);
Vue.use(VueDraggable);
const routes = [
    {path: '/', component: MainPage},
    {path: '/registration', component: RegistrationPage},
    {path: '/dashboard', component: DashBoardPage},
    {path: '/login', component: LoginPage},
    {path: '/dashboard', component: DashBoardPage},
    {path: '/groups/:id', component: GroupPage},
    {path: '/groups/:id/schedule', component: GroupSchedulePage},
    {path: '/courses/:id', component: CoursePage, props: true},
    {path: '/trainers/:id', component: TrainerPage},
    {path: '/admincourses', component: AdminCourses},
    {path: '/coursecreate', component: CreateCourse},
    {path: '/coursecreate/:id', component: CreateCourse,props: true},
    {path: '/userpage', component: AllUsersPage},
    {path: '/userpage/:id', component: UserPage},
    {path: '/add-user', component: NewUserPage},
    {path: '/attendance', component: AttendancePage},
    {path: '/absence-reasons', component: AbsenceReasons},
    {path: '/desired-schedule/:id', component: DesiredSchedulePage},
    {path: '/attachment-upload', component: AttachmentUpload},
    {path: '/test', component: TestPage},
];

export default new Router({
    mode: 'history',
    routes
})

