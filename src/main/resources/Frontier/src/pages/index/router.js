import Vue from 'vue'
import Router from 'vue-router'

import {toFirstLowerCase, toFirstUpperCase} from "../../utils/index";

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
            redirect:'/login'
        },
        {
            path: '/login',
            name: 'login',
            component: _('login'),
            props: route => ({ redirect:route.query.redirect })
        },
        {
            path: '/register',
            name: 'register',
            component: _('register')
        },
    ]
})
router.beforeEach(((to, from, next) => {
    next()
}))