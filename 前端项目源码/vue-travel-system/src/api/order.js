import axios from 'axios'

// 订单相关API

// 获取订单列表
export const OrderList = (params) => {
  return axios.post('/api/order/list', params, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}

// 根据ID获取订单详情
export const GetOrderById = (id) => {
  return axios.get(`/api/order/${id}`, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}

// 创建订单
export const CreateOrder = (data) => {
  return axios.post('/api/order', data, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}

// 更新订单
export const UpdateOrder = (id, data) => {
  return axios.put(`/api/order/${id}`, data, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}

// 删除订单
export const DeleteOrder = (id) => {
  return axios.delete(`/api/order/${id}`, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}

// 订单发货
export const DeliveryOrder = (id, data) => {
  return axios.put(`/api/order/${id}/delivery`, data, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}

// 分页查询订单
export const GetOrderPage = (params) => {
  return axios.post('/api/order/page', params, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}
