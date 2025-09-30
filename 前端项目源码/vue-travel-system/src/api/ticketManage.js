import axios from 'axios';
import { req } from './axiosFun.js'

// 获取订单列表（分页+筛选）
export const OrderList = (params) => req('get', '/api/order/orderList', params)

// 删除未支付订单
export const OrderDelete = (id) => { 
    return axios.delete(`/api/order/${id}`, {
        headers: {
            'Content-Type': 'application/json',
            token: localStorage.getItem('logintoken')
        }
    }).then(res => res.data) 
}

// 订单发货（更新发货状态）
export const OrderDelivery = (id , data) => {
    return axios.put(`/api/order/${id}/delivery`, data, {
        headers: {
            'Content-Type': 'application/json',
            token: localStorage.getItem('logintoken')
        }
    }).then(res => res.data)
}


