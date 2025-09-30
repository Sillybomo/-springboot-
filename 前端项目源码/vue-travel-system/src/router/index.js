// 导入组件
import Vue from "vue";
import Router from "vue-router";
// 登录
import login from "@/views/login";
// 注册
import register from "@/views/register";
// 首页
import index from "@/views/index";
// 主页面
import Home from "@/views/Home";
// 购物车页面
import Cart from "@/views/user/Cart.vue";
// 订单页面
import Orders from "@/views/user/Orders.vue";
// 个人中心页面
import Profile from "@/views/user/Profile.vue";
// 管理员页面
import AdminTicket from "@/views/admin/Ticket.vue";
import AdminRefundTicket from "@/views/admin/RefundTicket.vue";
import AdminUserManage from "@/views/admin/UserManage.vue";
import mychart from "@/views/charts/mychart.vue";
import myEcharts from "@/views/charts/pyecharts.vue";
/**
 * 基础菜单
 */
// 商品管理

// 启用路由
Vue.use(Router);

// 导出路由
export default new Router({
  routes: [
    {
      path: "/",
      name: "Home",
      component: Home,
      hidden: true,
      meta: {
        requireAuth: false
      }
    },
    {
      path: "/home",
      name: "主页面",
      component: Home,
      hidden: true,
      meta: {
        requireAuth: false
      }
    },
    {
      path: "/login",
      name: "登录",
      component: login,
      hidden: true,
      meta: {
        requireAuth: false
      }
    },
    {
      path: "/register",
      name: "注册",
      component: register,
      hidden: true,
      meta: {
        requireAuth: false
      }
    },
    {
      path: "/index",
      name: "管理后台",
      component: index,
      iconCls: "el-icon-tickets",
      meta: {
        requireAuth: true
      },
      children: [
        {
          path: "/cart",
          name: "购物车",
          component: Cart,
          meta: {
            requireAuth: true
          }
        },
        {
          path: "/orders",
          name: "我的订单",
          component: Orders,
          meta: {
            requireAuth: true
          }
        },
        {
          path: "/profile",
          name: "个人中心",
          component: Profile,
          meta: {
            requireAuth: true
          }
        },
        {
          path: "/charts/pyecharts",
          name: "pyecharts绘图",
          component: myEcharts,
          meta: {
            requireAuth: true
          }
        },
        {
          path: "/admin/Ticket",
          name: "放票管理",
          component: AdminTicket,
          meta: {
            requireAuth: true,
            requireAdmin: true
          }
        },
        {
          path: "/admin/RefundTicket",
          name: "退票管理",
          component: AdminRefundTicket,
          meta: {
            requireAuth: true,
            requireAdmin: true
          },
          props: {
            defaultStatus: "REFUND"
          }
        },
        {
          path: "/admin/UserManage",
          name: "用户管理",
          component: AdminUserManage,
          meta: {
            requireAuth: true,
            requireAdmin: true
          }
        },
        {
          path: "/charts/mychart",
          name: "AI智能助手",
          component: mychart,
          meta: {
            requireAuth: false // 不需要登录就能使用AI助手
          }
        }
      ]
    }
  ]
});
