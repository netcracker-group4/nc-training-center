import Vue from 'vue'
import App from 'Application.vue'
import Vuetify from 'vuetify'
import '@babel/polyfill'
import VueResource from 'vue-resource'
import 'vuetify/dist/vuetify.min.css'
import router from '../router/router';


Vue.use(VueResource);
Vue.use(Vuetify);


new Vue({
    el: '#app',
    router,
    render: a => a(App)
});

