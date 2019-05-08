import Vue from 'vue'
import App from 'Application.vue'
import Vuetify from 'vuetify'
import '@babel/polyfill'
import VueResource from 'vue-resource'
import VeeValidate from 'vee-validate'
import 'vuetify/dist/vuetify.min.css'
import router from '../router/router'
import store from '../store/store'

Vue.use(VeeValidate);
Vue.use(VueResource);
Vue.use(Vuetify);


new Vue({
    el: '#app',
    router,
    store,
    render: a => a(App)
});

