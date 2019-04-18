import Vue from 'vue'
import Router from 'vue-router'
import MainPage from '../pages/MainPage.vue'
import RegistrationPage from '../pages/RegistrationPage.vue'


Vue.use(Router)

const routes = [
    {path: '/', component: MainPage},
    {path: '/registration', component: RegistrationPage}
]

export default new Router({
    mode: 'history',
    routes
})

