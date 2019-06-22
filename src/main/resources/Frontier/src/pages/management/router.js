import Vue from 'vue'
import Router from 'vue-router'

import {toFirstLowerCase, toFirstUpperCase} from "../../utils/index";
import {hasLoggedIn} from "../../resource/authorization";
const views = require.context('./views')

/*
对组件的引用需先考虑一下引用方式，如果没有，再直接通过import引用
 */
/*
 第一种引用方式
 可以引用符合如下规范的组件：
   路径为./views/componentName/ComponnentName
   (注意大小写)
 例如：
   ./views/home/Home.vue
   可以通过_('home')或_('Home')引用
*/
const _ = name => views(`./${toFirstLowerCase(name)}/${toFirstUpperCase(name)}.vue`).default

Vue.use(Router)


export let router = new Router({
    routes: [
        {
            path: '/',
            name: 'home',
            component: _('home')
        },
        // {
        //     path: '/login',
        //     name: 'login',
        //     component: _('login')
        // },
        // {
        //     path: '/register',
        //     name: 'register',
        //     component: _('register')
        // },
        {
            path:'/group/:id',
            name:'group',
            // props:true,
            component: _('group')
        },
        // {
        //     path:'/app',
        //     name:'app',
        //     // props:true,
        //     component: _('app')
        // }
    ]
})
// router.beforeEach(((to, from, next) => {
//
//         if (!hasLoggedIn()){
//             // 跳转到首页
//             window.location.href = '/index.html'
//         }else {
//             next()
//         }
// }))