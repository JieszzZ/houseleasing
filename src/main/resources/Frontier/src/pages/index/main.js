import Vue from 'vue'
import App from './App.vue'
import {router} from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './style/global.scss'


/**
 * ServiceWorker部分代码
 */
import * as OfflinePluginRuntime from 'offline-plugin/runtime';

OfflinePluginRuntime.install({
    onInstalled: function () {
    },

    onUpdating: function () {

    },

    onUpdateReady: function () {
        OfflinePluginRuntime.applyUpdate();
    },
    onUpdated: function () {
        window.location.reload();
    }
})


Vue.config.productionTip = false

Vue.use(ElementUI);

new Vue({
    router,
    render: h => h(App)
}).$mount('#app')

