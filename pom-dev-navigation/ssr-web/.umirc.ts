// ref: https://umijs.org/config/
export default {
  title: 'demo-page',
  hash: true,
  //ssr服务 需要把history -> type 设为 browser
  history: {
    // type: 'hash',
    type: 'browser',
  },
  //ssr服务
  ssr: {
    removeWindowInitialProps: true,
  },
  //ssr服务
  routes: [
    { path: '/', component: '@/pages/index' },
    { path: '/nav-ui', component: '@/pages/nav-ui' },
  ],

  devtool: false,
  locale: {
    default: 'zh-CN',
    antd: true,
    title: false,
    baseNavigator: true,
    baseSeparator: '-',
  },
  antd: {},
  //配置model, 禁用即 dva:true
  dva: {
    hmr: true,
    immer: false,
  },

  ignoreMomentLocale: true, // 忽略 moment 的 locale 文件

}

