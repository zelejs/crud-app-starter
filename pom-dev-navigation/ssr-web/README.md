### install
```bash
$ yarn
或
$ npm install --force
```

### 设置 endpoint, 文件位置 src/global.js
```
...
//引用库
import { setEndpoint, setToken } from 'zero-element-boot/lib/components/config/common';

//设置域名或IP
setEndpoint('http://api.webtools.io')
...
```

### 新增页面需要同时在 .umirc.ts 文件中新增路由
```
...
routes: [
    { path: '/', component: '@/pages/index' },
    { path: '/nav-ui', component: '@/pages/nav-ui' },
],
...
```


### 运行项目, 执行命令会同时生成 dist 文件夹
```bash
$ yarn dev
或
$ npm run dev
```

