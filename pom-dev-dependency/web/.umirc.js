
// ref: https://umijs.org/config/
export default {
  title: 'Demo',
  hash: true,
  // history: {
  //   type: 'hash',
  // },

  devtool: false,
  locale: {
    default: 'zh-CN',
    antd: true,
    title: false,
    baseNavigator: true,
    baseSeparator: '-',
  },
  antd: {},
  dva: false,

  ignoreMomentLocale: true, // 忽略 moment 的 locale 文件
}
