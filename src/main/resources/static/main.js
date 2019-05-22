import Vue from 'vue'
import App from 'Application.vue'
import Vuetify from 'vuetify'
import '@babel/polyfill'
import VueResource from 'vue-resource'
import VeeValidate from 'vee-validate'
import 'vuetify/dist/vuetify.min.css'
import router from '../router/router'
import store from '../store/store'
import Snotify, {SnotifyPosition} from 'vue-snotify'
import {connect} from '../websocket/ws'
import VueCarousel from 'vue-carousel';



//connect();

const options = {
    toast: {
        position: SnotifyPosition.rightTop
    }
};

Vue.use(VueCarousel);
Vue.use(VeeValidate);
Vue.use(VueResource);
Vue.use(Vuetify);
Vue.use(Snotify, options);
new Vue({
    el: '#app',
    router,
    store,
    render: a => a(App)
});
