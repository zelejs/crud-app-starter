# 业务系统开发脚手架
### 功能列表  (增加表格说明)
### 完善可视化首页配置

## 主要配置文件
[config.js](./public/config.js)
[devConfig.js](./src/devConfig.js)

## 环境
```
node version 10
npm version 6
```

## 安装

> 设置淘宝镜像
```shell
$ npm config set registry https://registry.npm.taobao.org
$ npm i
```

## 以开发环境运行
```shell
npm start
# 默认运行在本地的 8000 端口
```


### 配置导航栏风格
前往 [config.js](./public/config.js) 配置 window.ZEle.nav 有四种风格
top 顶部导航栏
both 顶部导航栏
left 左侧导航栏
LeftNavCollaps 可收缩的左侧导航栏

### 配置面包屑风格
前往 [devConfig.js](./src/devConfig.js) 配置 breadcrumbType，有两种风格 header 和 top

### 整体风格
前往 [devConfig.js](./src/devConfig.js) 配置 theme，有两种风格
TopCover 和 LeftCover

## 如何设置页面不显示顶部导航栏及左侧栏
前往 [devConfig.js](./src/devConfig.js) 配置 aloneWindow 在里面配置需要屏蔽的路径即可

```js
export const Config = {
  ...其他配置,
  aloneWindow:{
    "/login",
    "/其他页面路径"
  }
}
```

## 以生产环境运行
```shell
npm run build
# 然后把生成的 dist/ 拷贝到 web server 下
```

## npm un build 提示找不到模块 'webpack'
> 电脑第一次构建前端的项目的话，需要先全局安装 webpack 相关模块
```
# 以管理员 (Linux root) 运行
npm install webpack -g
npm install webpack-cli -g
```

## 如何对接API

### 运行后台API代码
> 运行详情见`README.md`说明
> 
查看后台代码 [app-crud-bundle](https://github.com/zelejs/crud-app-bundle)

### 设置后台API端口
#### 开发环境

前往 [devConfig.js](./src/devConfig.js) 更改 Config 下的 endpoint 即可

#### 生产环境

前往 [config.js](./public/config.js) 更改 window.ZEle.endpoint 项即可

> listAction保留仅为测试用,测试成功之后迁移至[zero-element-antd](https://github.com/kequandian/zero-element-antd)

>.keep可提交空文件夹，勿删

迁移至antd方法，迁移到antd下src的对应文件夹，然后添加index.js及map.json路径
index.js增加方法
```javascript
// 在头部调用
import youListAction from './listAction/youListAction'
// 找到 LASet
LASet({
  'youListAction':youListAction
})
```
以上仅调用ListAction方法来说明，其余方法同理，可参考[index.js](https://github.com/kequandian/zero-element-antd/blob/master/src/index.js)来定义

然后，更改map.json
```javascript
// 找到 LASet，添加
LASet({
  "youlistAction":"listAction/youlistAction"
})
```


### 页面下载按钮文件摆放位置
 > 1. 下载并解压文件, 将整个文件夹 复制到 src/pages 目录下
 > 2. 在 src/config/router.config.js 配置相应的菜单
 > 3. npm start 启动项目
 