// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
// 引入element UI
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import App from './App';
// 引入路由
import router from './router';
// 引入状态管理
import store from './vuex/store';
// 引入icon
import './assets/icon/iconfont.css'
// 


import axios from 'axios';
Vue.prototype.$axios = axios;

Vue.config.productionTip = false;

// 使用element UI
Vue.use(ElementUI);
// 过滤器
import * as custom from './utils/util'

Object.keys(custom).forEach(key => {
    Vue.filter(key, custom[key])
})

// 路由拦截器:vue全局前置 路由守卫router.beforeEach(to,from,next)（在页面跳转前即路径都没改变前执行）
//参数的含义：1.to:表示要进入到哪个路由去，2、from:从哪个路由跳转过来的 3、next：路由的控制参数，常用的有next(true)和next(false)
//路由守卫router.beforeEach表示在初始化时被调用，或者路由切换之前被调用，用于保护路由安全（登录权限验证）
router.beforeEach((to, from, next) => {
    console.log("路由拦截器----beforeEach")
    if (to.matched.length != 0) {
        console.log("-----to.matched.length != 0----if---"+to.matched.length)
        if (to.meta.requireAuth) { // 判断该路由是否需要登录权限
            console.log("-------判断该路由是否需要登录权限----if---"+to.meta.requireAuth)
            
            //为了能够进入登录页面，这里将localStorage中保存的数据移,移除以后就不能进入登录成功的页面了。
           // localStorage.removeItem("userInfo")

            if (Boolean(localStorage.getItem("logintoken"))) { // 检查登录token是否存在
                next();
            } else {
                next({
                    path: '/login',
                    query: { redirect: to.fullPath } // 将跳转的路由path作为参数，登录成功后跳转到该路由
                })
            }
        } else {
            console.log("------判断该路由是否需要登录权限------else---")
            // 对于不需要登录权限的路由（如Home页面、登录页面、注册页面），直接放行
            if (to.path === '/' || to.path === '/home' || to.path === '/login' || to.path === '/register') {
                next();
            } else if (Boolean(localStorage.getItem("logintoken"))) { // 判断是否登录
                next();
            } else {
                // 如果访问其他需要登录的页面但未登录，跳转到登录页
                next({
                    path: '/login',
                    query: { redirect: to.fullPath }
                })
            }
        }
    } else {
        console.log("-----to.matched.length != 0----else---")
        next({
            path: '/',
            query: { redirect: to.fullPath } // 将跳转的路由path作为参数，登录成功后跳转到该路由
        })
    }
})

/* eslint-disable no-new */
new Vue({
    el: '#app',
    router,
    store, //使用store vuex状态管理
    components: { App },
    template: '<App/>',
    data: {
        // 空的实例放到根组件下，所有的子组件都能调用
        Bus: new Vue()
    }

})