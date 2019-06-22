const StatsPlugin = require('stats-webpack-plugin')
const OfflinePlugin = require('offline-plugin')
const path = require('path')

module.exports = {
    pages: {
        index: {
            entry: 'src/pages/index/main.js',
            template: 'index.template.html',
            filename: 'index.html',
            title: 'Index'
        },
        user: {
            entry: 'src/pages/user/main.js',
            template: 'user.template.html',
            filename: 'user.html',
            title: 'User',
            // 当前版本暂不支持chunks  复议
            // chunks: ['chunk-common']
        },
        management: {
            entry: 'src/pages/management/main.js',
            template: 'management.template.html',
            filename: 'management.html',
            title: 'Management',
        },
    },
    devServer: {
        proxy: {
            '/api/v1': {
                target: 'https://39.106.131.88'
            },
        },
        https:true
    },
    configureWebpack: {
        plugins: [
            // new StatsPlugin('stats.json', {
            //     chunkModules: true,
            //     chunks: true,
            //     assets: false,//html,css这些 
            //     modules: true,
            //     children: true,
            //     chunksSort: true,//排序这两个都要加上   
            //     assetsSort: true
            // }),
            new OfflinePlugin({
                caches: {
                    main: [],
                    additional: [],
                    optional: []
                }
            }),


        ]
    },
    // chainWebpack: config => {
    //     // worker Loader
    //     config.module
    //         .rule('webworker')
    //         .test(/\.worker\.js$/)
    //         .use('worker-loader')
    //         .loader('worker-loader')
    //         .end()
    //
    // }

}
