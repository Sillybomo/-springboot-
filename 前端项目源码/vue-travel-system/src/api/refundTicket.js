import axios from 'axios';
import { req } from './axiosFun.js'

// 获取订单列表（分页+筛选）
export const OrderList = (params) => req('get', '/api/order/orderList', params)


// 退款处理
export const OrderDelivery = (id, data) => {
    return axios.put(`/api/order/refund/${id}`, data, {
        headers: {
            'Content-Type': 'application/json',
            token: localStorage.getItem('logintoken')
        }
    }).then(res => res.data)
}

