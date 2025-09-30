import axios from 'axios'

// 门票相关API

// 获取门票列表
export const TicketList = (params) => {
  return axios.post('/api/ticket/list', params, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}

// 根据ID获取门票详情
export const GetTicketById = (id) => {
  return axios.get(`/api/ticket/${id}`, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}

// 根据景点ID获取门票列表
export const GetTicketsByAttractionId = (attractionId) => {
  return axios.post('/api/ticket/list', {
    attractionId: attractionId
  }, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}

// 添加门票
export const AddTicket = (data) => {
  return axios.post('/api/ticket', data, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}

// 更新门票
export const UpdateTicket = (id, data) => {
  return axios.put(`/api/ticket/${id}`, data, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}

// 删除门票
export const DeleteTicket = (id) => {
  return axios.delete(`/api/ticket/${id}`, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}