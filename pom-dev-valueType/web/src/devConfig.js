/** 
 * @开发环境配置
 * @关于Config配置说明
 * @优先级 此地方中的 endpoint 高于 public/config.js 中 window.ZEle.endpoint
 * @说明 此地方为开发环境的endpoint配置
*/
export const Config = {

  endpoint: "http://app1.console.smallsaas.cn:8001",//api接口endpoint

  // endpoint:"http://192.168.3.194:8081",//api接口endpoint 
  // endpoint:"http://192.168.3.197:8080",//api接口endpoint

  breadcrumbType: "header", //header,top
  theme: "LeftCover",//TopCover,LeftCover
  aloneWindow: [   // 使用方法:在这里增加页面路径即可，如 /login
    "/dynamicPage/dynamicPage-add",
    "/dynamicPage/dynamicPage-edit",
    "/layoutPage"
  ]
}