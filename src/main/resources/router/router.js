import Vue from 'vue'
import Router from 'vue-router'
import MainPage from '../pages/MainPage.vue'


Vue.use(Router)

const routes = [
    {path: '/', component: MainPage, name: 'Main'},
]

export default new Router({
    mode: 'history',
    routes
})

