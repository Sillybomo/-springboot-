import axios from 'axios'

// 获取用户列表
export const UserList = (params) => {
  return axios.post('/api/user/userList', params, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}

// 创建用户
export const UserCreate = (userData) => {
  return axios.post('/api/user/create', userData, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}

// 更新用户
export const UserUpdate = (userId, userData) => {
  return axios.put(`/api/user/update/${userId}`, userData, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}

// 删除用户
export const UserDelete = (userId) => {
  return axios.delete(`/api/user/delete/${userId}`, {
    headers: {
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}

// 批量删除用户
export const UserBatchDelete = (userIds) => {
  return axios.post('/api/user/batchDelete', { userIds }, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}

// 获取用户详情
export const UserDetail = (userId) => {
  return axios.get(`/api/user/detail/${userId}`, {
    headers: {
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}
