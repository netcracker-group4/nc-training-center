import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export default new Vuex.Store({
    state:{
        isAuthorized: isAuthorized,
        user: user,
        userRoles: userRoles,
        imgFolder: '/img/'
    },
    getters: {
        userRoles: state => {
            return state.userRoles
        },
        user: state => {
            return state.user
        },
        isAdmin: state => {
            if(userRoles != null && userRoles != undefined){
                return state.userRoles.includes('ADMIN')
            }else return false
        },
        isTrainer: state => {
            if(userRoles != null && userRoles != undefined){
                return state.userRoles.includes('TRAINER')
            }else return false
        },
        isManager: state => {
            if(userRoles != null && userRoles != undefined){
                return state.userRoles.includes('MANAGER')
            }else return false
        },

        isAuthorized: state => {
            if( state.isAuthorized != false &&
                state.isAuthorized != null &&
                state.isAuthorized != undefined){
                return true
            }else return false
        },
    },

})