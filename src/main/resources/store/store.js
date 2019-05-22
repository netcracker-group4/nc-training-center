import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        apiServer: 'http://localhost:8080',
        //apiServer: 'http://45.66.10.81:8080',
        isAuthorized: isAuthorized,
        user: user,
        userRoles: userRoles,
        imgFolder: '/img/',
        chats: [],
    },
    getters: {
        userRoles: state => {
            return state.userRoles
        },
        user: state => {
            return state.user
        },
        isAdmin: state => {
            if (state.userRoles !== undefined && state.userRoles != null) {
                return state.userRoles.includes('ADMIN')
            } else return false
        },
        isTrainer: state => {
            if(state.userRoles !== undefined && state.userRoles != null){
                return state.userRoles.includes('TRAINER')
            }else return false
        },
        isManager: state => {
            if(state.userRoles !== undefined && state.userRoles != null){
                return state.userRoles.includes('MANAGER')
            }else return false
        },

        isAuthorized: state => {
            return state.isAuthorized !== undefined &&
                state.isAuthorized !== null &&
                state.isAuthorized !== false;
        },
        getChatById: state => id =>{
            return state.chats.find(chat => chat.id == id)
        }
    },

})