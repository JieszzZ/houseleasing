
import Vue from 'vue'
import App from './App.vue'
import {router} from './router'
import ElementUI from 'element-ui';
import * as jquery from 'jquery';
import * as flotr2 from 'flotr2/flotr2.amd.js'
import 'element-ui/lib/theme-chalk/index.css';
import './style/global.scss'

Vue.config.productionTip = false

Vue.use(ElementUI);

new Vue({
    router,
    render: h => h(App)
}).$mount('#app')
