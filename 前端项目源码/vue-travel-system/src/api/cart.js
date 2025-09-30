import axios from 'axios';
import { req } from './axiosFun';

// 购物车相关API

// 获取购物车列表
export const CartList = (params) => {
    return axios.post('/api/cart/list', params, {
        headers: {
            'Content-Type': 'application/json',
            token: localStorage.getItem('logintoken')
        }
    }).then(res => res.data)
}

// 加入购物车
export const AddToCart = (data) => {
    return axios.post('/api/cart', data, {
        headers: {
            'Content-Type': 'application/json',
            token: localStorage.getItem('logintoken')
        }
    }).then(res => res.data)
}

// 更新购物车商品数量
export const CartUpdate = (id, data) => {
    return axios.post('/api/cart/update', { ...data, id }, {
        headers: {
            'Content-Type': 'application/json',
            token: localStorage.getItem('logintoken')
        }
    }).then(res => res.data)
}

// 删除购物车商品
export const CartDelete = (id) => {
    return axios.post('/api/cart/delete', { id }, {
        headers: {
            'Content-Type': 'application/json',
            token: localStorage.getItem('logintoken')
        }
    }).then(res => res.data)
}

// 批量删除购物车商品
export const CartBatchDelete = (data) => {
    return axios.post('/api/cart/batchDelete', data.ids, {
        headers: {
            'Content-Type': 'application/json',
            token: localStorage.getItem('logintoken')
        }
    }).then(res => res.data)
}

// 清空购物车
export const CartClear = (params) => {
    return axios.post('/api/cart/clear', params, {
        headers: {
            'Content-Type': 'application/json',
            token: localStorage.getItem('logintoken')
        }
    }).then(res => res.data)
}

// 购物车结算
export const CartCheckout = (data) => {
    return axios.post('/api/cart/checkout', data, {
        headers: {
            'Content-Type': 'application/json',
            token: localStorage.getItem('logintoken')
        }
    }).then(res => res.data)
}

// 获取购物车数量
export const GetCartCount = (params) => {
    return axios.get('/api/cart/count', {
        params: params,
        headers: {
            token: localStorage.getItem('logintoken')
        }
    }).then(res => res.data)
}
