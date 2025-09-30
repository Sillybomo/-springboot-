import { login, register, getUserInfo, updateUserInfo, updatePassword } from '@/api/client/auth'

const state = {
  token: localStorage.getItem('token') || '',
  userInfo: null,
  isLoggedIn: false
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
    localStorage.setItem('token', token)
  },
  SET_USER_INFO: (state, userInfo) => {
    state.userInfo = userInfo
    state.isLoggedIn = true
  },
  CLEAR_USER: (state) => {
    state.token = ''
    state.userInfo = null
    state.isLoggedIn = false
    localStorage.removeItem('token')
  }
}

const actions = {
  // 用户登录
  login({ commit }, userInfo) {
    return new Promise((resolve, reject) => {
      login(userInfo).then(response => {
        const { token } = response.data
        commit('SET_TOKEN', token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  
  // 用户注册
  register({ commit }, userInfo) {
    return new Promise((resolve, reject) => {
      register(userInfo).then(response => {
        resolve(response)
      }).catch(error => {
        reject(error)
      })
    })
  },
  
  // 获取用户信息
  getInfo({ commit }) {
    return new Promise((resolve, reject) => {
      getUserInfo().then(response => {
        commit('SET_USER_INFO', response.data)
        resolve(response.data)
      }).catch(error => {
        reject(error)
      })
    })
  },
  
  // 更新用户信息
  updateInfo({ commit }, userInfo) {
    return new Promise((resolve, reject) => {
      updateUserInfo(userInfo).then(response => {
        commit('SET_USER_INFO', response.data)
        resolve(response.data)
      }).catch(error => {
        reject(error)
      })
    })
  },
  
  // 更新密码
  updatePassword({ commit }, passwordInfo) {
    return new Promise((resolve, reject) => {
      updatePassword(passwordInfo).then(response => {
        resolve(response)
      }).catch(error => {
        reject(error)
      })
    })
  },
  
  // 退出登录
  logout({ commit }) {
    return new Promise(resolve => {
      commit('CLEAR_USER')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}