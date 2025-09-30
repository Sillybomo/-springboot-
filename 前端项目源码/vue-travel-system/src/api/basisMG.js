import axios from 'axios';
import { req } from './axiosFun';

/**
 * 商品管理
 **/
// 商品管理-获取商品管理列表（分页查询）
export const GoodsList = (params) => { return req("post", "/api/product/productList", params) };
// 商品管理-保存商品（JSON 提交）
export const GoodsSave = (data) => { return axios({ method: 'post', url: `/api/product/add`, headers: { 'Content-Type': 'application/json' }, data }).then(res => res.data) };
// 商品管理-删除商品（按ID）
export const GoodsDelete = (productId) => { return axios.delete(`/api/product/${productId}`).then(res => res.data) };
// 商品管理-更新商品（按ID）
export const GoodsUpdate = (productId, data) => { return axios({ method: 'put', url: `/api/product/${productId}`, headers: { 'Content-Type': 'application/json' }, data }).then(res => res.data) };
/**
 * 分类管理
 **/
// 分类管理-获取分类列表（JSON提交）
export const CategoryList = (params) => { 
    return axios({ 
      method: 'post', 
      url: `/api/category/list`, 
      headers: { 
        'Content-Type': 'application/json',
        token: localStorage.getItem('logintoken')
      }, 
      data: params 
    }).then(res => res.data) 
  };
  
/**
 * 机器信息管理 
 **/
// 机器信息管理-获取机器信息管理列表
export const MachineList = (params) => { return req("post", "/api/Machine/list", params) };
// 机器信息管理-保存机器信息管理
export const MachineSave = (params) => { return req("post", "/api/Machine/save", params) };
// 机器信息管理-删除机器信息管理
export const MachineDelete = (params) => { return axios.delete("/api/Machine/delete?ids=" + params + "&token=" + localStorage.getItem('logintoken')).then(res => res.data) };

/**
 * 货道信息管理
 **/
// 货道信息管理-获取获取货道信息管理列表
export const MachineAisleList = (params) => { return req("post", "/api/MachineAisle/list", params) };
// 货道信息管理-删除货道信息管理
export const MachineAisleDelete = (params) => { return axios.delete("/api/MachineAisle/delete?ids=" + params + "&token=" + localStorage.getItem('logintoken')).then(res => res.data) };
// 货道信息管理-保存货道信息管理
export const MachineAisleRsave = (params) => { return req("post", "/api/MachineAisle/save", params) };