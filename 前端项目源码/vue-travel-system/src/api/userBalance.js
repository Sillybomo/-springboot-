import axios from 'axios'

// 用户余额相关API

// 获取用户余额
export const GetUserBalance = (userId) => {
  return axios.get(`/api/user/balance/${userId}`, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}

// 更新用户余额
export const UpdateUserBalance = (userId, data) => {
  return axios.put(`/api/user/balance/${userId}`, data, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}

// 充值余额
export const RechargeBalance = (userId, amount) => {
  return axios.post(`/api/user/recharge`, {
    userId: userId,
    amount: amount
  }, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}

// 申请退票
export const ApplyRefund = (orderId, reason) => {
  return axios.post(`/api/order/refund`, {
    orderId: orderId,
    reason: reason
  }, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}

// 处理退票申请（管理员）
export const ProcessRefund = (refundId, status, remark) => {
  return axios.put(`/api/order/refund/${refundId}`, {
    status: status,
    remark: remark
  }, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}

// 获取退票申请列表
export const GetRefundList = (params) => {
  return axios.post('/api/order/refund/list', params, {
    headers: {
      'Content-Type': 'application/json',
      token: localStorage.getItem('logintoken')
    }
  }).then(res => res.data)
}
