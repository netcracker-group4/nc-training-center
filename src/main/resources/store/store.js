import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

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
        isAdmin: state => {
            if(userRoles != null && userRoles != undefined){
                return state.userRoles.includes('ADMIN')
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