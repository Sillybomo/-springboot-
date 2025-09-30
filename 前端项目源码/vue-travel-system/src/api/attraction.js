import axios from 'axios'

// 创建axios实例
const api = axios.create({
  baseURL: '/api', // 使用代理路径
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    // 可以在这里添加token等认证信息
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    return response
  },
  error => {
    // 统一错误处理
    console.error('API请求错误:', error)
    if (error.response) {
      // 服务器响应了错误状态码
      switch (error.response.status) {
        case 401:
          // 未授权，清除token并跳转到登录页
          localStorage.removeItem('token')
          window.location.href = '/login'
          break
        case 403:
          console.error('没有权限访问该资源')
          break
        case 404:
          console.error('请求的资源不存在')
          break
        case 500:
          console.error('服务器内部错误')
          break
      }
    } else if (error.request) {
      // 请求已发出但没有收到响应
      console.error('网络错误，请检查网络连接')
    }
    return Promise.reject(error)
  }
)

// 景点相关API
export const attractionAPI = {
  // 获取景点列表
  getAttractionList(params = {}) {
    return api.post('/attraction/list', params)
  },

  // 根据ID获取景点详情
  getAttractionById(id) {
    return api.get(`/attraction/${id}`)
  },

  // 添加景点
  addAttraction(data) {
    return api.post('/attraction', data)
  },

  // 批量添加景点
  batchAddAttractions(list) {
    return api.post('/attraction/batch', list)
  },

  // 更新景点信息
  updateAttraction(id, data) {
    return api.put(`/attraction/${id}`, data)
  },

  // 删除景点
  deleteAttraction(id) {
    return api.delete(`/attraction/${id}`)
  },

  // 搜索景点（按名称）
  searchAttractions(keyword) {
    return api.post('/attraction/list', {
      name: keyword
    })
  },

  // 按省份获取景点
  getAttractionsByProvince(province) {
    return api.post('/attraction/list', {
      province: province
    })
  },

  // 获取热门景点（按评分排序）
  getHotAttractions(limit = 6) {
    return api.post('/attraction/list', {
      orderBy: 'rating',
      currentPage: 1,
      pageSize: limit
    })
  }
}

export default api
