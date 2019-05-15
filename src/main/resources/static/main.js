import Vue from 'vue'
import App from 'Application.vue'
import Vuetify from 'vuetify'
import '@babel/polyfill'
import VueResource from 'vue-resource'
import VeeValidate from 'vee-validate'
import 'vuetify/dist/vuetify.min.css'
import router from '../router/router'
import store from '../store/store'
import Notifications from 'vue-notification'
import velocity from 'velocity-animate'
import Snotify, {SnotifyPosition} from 'vue-snotify'
import { connect } from '../websocket/ws'

connect()

const options = {
    toast: {
        position: SnotifyPosition.rightTop
    }
};

Vue.use(VeeValidate);
Vue.use(VueResource);
Vue.use(Vuetify);
Vue.use(Snotify, options);
// noinspection JSUnusedGlobalSymbols
Vue.use(Notifications, {velocity: velocity});
new Vue({
    el: '#app',
    router,
    store,
    render: a => a(App)
});
