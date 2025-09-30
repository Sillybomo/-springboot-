import axios from 'axios'

// 获取用户详细信息
export const GetUserProfile = (userId) => {
  return axios.get(`/api/user/profile/${userId}`, {
    headers: {
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}

// 更新用户基本信息
export const UpdateUserProfile = (userId, data) => {
  return axios.put(`/api/user/profile/${userId}`, data, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}

// 修改密码
export const ChangePassword = (userId, data) => {
  return axios.put(`/api/user/password/${userId}`, data, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}

// 上传头像
export const UploadAvatar = (userId, formData) => {
  return axios.post(`/api/user/avatar/${userId}`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}
