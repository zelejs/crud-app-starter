package com.jfeat.module.autoRender.api.route;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jfeat.am.module.ioJson.services.domain.service.MockJsonService;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.crud.plus.CRUD;
import com.jfeat.module.autoRender.service.domain.service.AutoPageService;
import com.jfeat.module.autoRender.service.domain.service.ModuleDataService;
import com.jfeat.module.autoRender.service.domain.service.ModuleService;
import com.jfeat.module.autoRender.service.gen.persistence.model.AutoRoute;
import com.jfeat.module.autoRender.util.ParameterUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@Api("Route")
@RequestMapping("/dev/auto/forms")
public class RouteEndpoint {

    @Resource
    MockJsonService mockJsonService;

    @Resource
    ModuleService moduleService;

    @Resource
    AutoPageService autoPageService;

    @Resource
    ModuleDataService moduleDataService;


    //    查看路由
    @GetMapping("{id}/routes")
    @ApiOperation(value = "查看路由")
    public Tip getRouterList(@PathVariable("id")Long id,@RequestParam(value = "currentModule",required = false,defaultValue = "0") Integer currentModule){

        JSONObject json = autoPageService.getPageConfigJsonByPageId(id);
        String key = moduleService.getModuleKeyByIndex(json,currentModule);

        JSONObject module =  moduleDataService.getModuleDataByKey(json,key);
        if (module!=null && module.containsKey("navList") && module.get("navList")!=null){
            JSONArray navList =module.getJSONArray("navList");
            return SuccessTip.create(navList);


        }
        return SuccessTip.create();
    }


    //    查看路由
    @GetMapping("{id}/routes/{index}")
    @ApiOperation(value = "查看路由")
    public Tip getRouter(@PathVariable("id")Long id,@PathVariable Integer index,@RequestParam(value = "currentModule",required = false,defaultValue = "0") Integer currentModule){
        JSONObject json = autoPageService.getPageConfigJsonByPageId(id);
        String key = moduleService.getModuleKeyByIndex(json,currentModule);

        JSONObject module =  moduleDataService.getModuleDataByKey(json,key);
        if (module!=null && module.containsKey("navList") && module.get("navList")!=null){
            JSONArray navList =module.getJSONArray("navList");
            if (navList!=null && navList.size()>0 && index<navList.size()){
                return SuccessTip.create(navList.get(index));
            }
        }

        return SuccessTip.create();
    }


    //    添加路由
    @PostMapping("/{id}/routes")
    @ApiOperation(value = "添加路由")
    public Tip addRoute(@PathVariable("id")Long id, @RequestBody AutoRoute autoRoute){
        int currentModule = 0;
        if (autoRoute.getCurrentModule()!=null && autoRoute.getCurrentModule()>0){
            currentModule = autoRoute.getCurrentModule();
        }
        autoRoute.setCurrentModule(null);
        JSONObject json = autoPageService.getPageConfigJsonByPageId(id);
        String key = moduleService.getModuleKeyByIndex(json,currentModule);


        JSONObject module =  moduleDataService.getModuleDataByKey(json,key);
        if (module!=null && module.containsKey("navList")){
            JSONArray navList =module.getJSONArray("navList");
            String navListS = JSONObject.toJSONString(navList);
            if (navList!=null && navList.size()>=0){
                List<AutoRoute> autoRouteList =  JSONObject.parseArray(navListS, AutoRoute.class);


                autoRouteList.add(navList.size(), autoRoute);

                JSONArray array = JSONArray.parseArray(JSON.toJSONString(autoRouteList,SerializerFeature.DisableCircularReferenceDetect));
                module.put("navList",array);


                JSONObject newJson  =moduleDataService.replaceModuleData(json,module,key);
                mockJsonService.saveJsonToFile(json,id);
                return SuccessTip.create(newJson);
            }

        }

        return SuccessTip.create();
    }


//    修改路由
    @PutMapping("/{id}/routes/{index}")
    @ApiOperation(value = "修改路由")
    public Tip updateRouteDate(@PathVariable("id")Long id, @PathVariable("index")Integer index,@RequestBody AutoRoute autoRoute){
        int currentModule = 0;
        if (autoRoute.getCurrentModule()!=null && autoRoute.getCurrentModule()>0){
            currentModule = autoRoute.getCurrentModule();
        }
        autoRoute.setCurrentModule(null);
        JSONObject json = autoPageService.getPageConfigJsonByPageId(id);
        String key = moduleService.getModuleKeyByIndex(json,currentModule);

        JSONObject module =  moduleDataService.getModuleDataByKey(json,key);
        if (module!=null && module.containsKey("navList") && module.get("navList")!=null){
            JSONArray navList =module.getJSONArray("navList");
            String navListS = JSONObject.toJSONString(navList);
            if (navList!=null && navList.size()>0){
                List<AutoRoute> autoRouteList =  JSONObject.parseArray(navListS, AutoRoute.class);
                if (index<0||index> autoRouteList.size()){
                    throw new BusinessException(BusinessCode.BadRequest,"index越界");
                }

                autoRouteList.set(index,ParameterUtil.parameterReplace(autoRouteList.get(index), autoRoute));

                JSONArray array = JSONArray.parseArray(JSON.toJSONString(autoRouteList));
                module.put("navList",array);

                JSONObject newJson  =moduleDataService.replaceModuleData(json,module,key);
                mockJsonService.saveJsonToFile(json,id);
                return SuccessTip.create(newJson);
            }

        }
        return SuccessTip.create();
    }


//   变更路由跳转页面
    @PostMapping("/{id}/routes/{index}/op/next")
    @ApiOperation(value = "变更路由跳转页面")
    public Tip updateRouteDate(@PathVariable("id")Long id, @RequestBody AutoRoute autoRoute, @PathVariable("index") Integer index){
        int currentModule = 0;
        if (autoRoute.getCurrentModule()!=null && autoRoute.getCurrentModule()>0){
            currentModule = autoRoute.getCurrentModule();
        }
        autoRoute.setCurrentModule(null);
        JSONObject jsonObject = autoPageService.getPageConfigJsonByPageId(id);
        String key = moduleService.getModuleKeyByIndex(jsonObject,currentModule);

        JSONObject module =  moduleDataService.getModuleDataByKey(jsonObject,key);
        if (module!=null && module.containsKey("navList") && module.get("navList")!=null){
            JSONArray navList =module.getJSONArray("navList");
            String navListS = JSONObject.toJSONString(navList);
            if (navList!=null && navList.size()>0){
                List<AutoRoute> autoRouteList =  JSONObject.parseArray(navListS, AutoRoute.class);
                if (index<0||index>= autoRouteList.size()){
                    throw new BusinessException(BusinessCode.BadRequest,"index越界");
                }

                AutoRoute navRouter = autoRouteList.get(index);
//                    routeList.get(index)
                String nav = navRouter.getNav();

                if (nav!=null&& !nav.equals("")){
                    String pattern = "=.*(\\d+).*";

                    Pattern p = Pattern.compile(pattern);
                    // get a matcher object
                    Matcher m = p.matcher(nav);
                    if (m.find()){
                        nav = m.replaceFirst("=".concat(String.valueOf(autoRoute.getPageId())));
                        navRouter.setNav(nav);
                    }else {
                        throw new BusinessException(BusinessCode.CodeBase,"未找到pageID");
                    }
                    autoRouteList.set(index,navRouter);
                }else {
                    throw new BusinessException(BusinessCode.EmptyNotAllowed,"nav为空");
                }


                JSONArray array = JSONArray.parseArray(JSON.toJSONString(autoRouteList));
                module.put("navList",array);

                JSONObject json  =moduleDataService.replaceModuleData(jsonObject,module,key);
                mockJsonService.saveJsonToFile(jsonObject,id);
                return SuccessTip.create(json);
            }

        }
        return SuccessTip.create();
    }


//    移动路由位置
    @PostMapping("/{id}/route/op/arrange")
    @ApiOperation(value = "移动路由位置")
    public Tip updateRoutePosition(@PathVariable("id")Long id, @RequestBody AutoRoute autoRoute){
        int currentModule = 0;
        if (autoRoute.getCurrentModule()!=null && autoRoute.getCurrentModule()>0){
            currentModule = autoRoute.getCurrentModule();
        }
        autoRoute.setCurrentModule(null);
        JSONObject jsonObject = autoPageService.getPageConfigJsonByPageId(id);
        String key = moduleService.getModuleKeyByIndex(jsonObject,currentModule);

        JSONObject module =  moduleDataService.getModuleDataByKey(jsonObject,key);
        if (module!=null && module.containsKey("navList") && module.get("navList")!=null){
            JSONArray navList =module.getJSONArray("navList");
            String navListS = JSONObject.toJSONString(navList);
            if (navList!=null && navList.size()>0){
                List<AutoRoute> autoRouteList =  JSONObject.parseArray(navListS, AutoRoute.class);

                if (autoRoute.getFrom()<0|| autoRoute.getFrom()>= autoRouteList.size()){
                    throw new BusinessException(BusinessCode.BadRequest,"from越界");
                }

                if (autoRoute.getTo()<0|| autoRoute.getTo()> autoRouteList.size()){
                    throw new BusinessException(BusinessCode.BadRequest,"to越界");
                }

                AutoRoute indexAutoRoute = autoRouteList.get(autoRoute.getFrom());
                autoRouteList.remove(autoRoute.getFrom().intValue());
                autoRouteList.add(autoRoute.getTo().intValue(), indexAutoRoute);

                JSONArray array = JSONArray.parseArray(JSON.toJSONString(autoRouteList,SerializerFeature.DisableCircularReferenceDetect));
                module.put("navList",array);

                JSONObject json  =moduleDataService.replaceModuleData(jsonObject,module,key);
                mockJsonService.saveJsonToFile(jsonObject,id);

                return SuccessTip.create(json);
            }

        }
        return SuccessTip.create();
    }





//    移除路由
    @PostMapping("/{id}/route/op/remove")
    @ApiOperation(value = "移除路由")
    public Tip removeRoute(@PathVariable("id")Long id, @RequestBody AutoRoute autoRoute){
        int currentModule = 0;
        if (autoRoute.getCurrentModule()!=null && autoRoute.getCurrentModule()>0){
            currentModule = autoRoute.getCurrentModule();
        }
        autoRoute.setCurrentModule(null);
        JSONObject jsonObject = autoPageService.getPageConfigJsonByPageId(id);
        String key = moduleService.getModuleKeyByIndex(jsonObject,currentModule);

        JSONObject module =  moduleDataService.getModuleDataByKey(jsonObject,key);
        if (module!=null && module.containsKey("navList") && module.get("navList")!=null){
            JSONArray navList =module.getJSONArray("navList");
            String navListS = JSONObject.toJSONString(navList);
            if (navList!=null && navList.size()>0){
                List<AutoRoute> autoRouteList =  JSONObject.parseArray(navListS, AutoRoute.class);


                if (autoRoute.getIndex()<0|| autoRoute.getIndex()>= autoRouteList.size()){
                    throw new BusinessException(BusinessCode.BadRequest,"target越界");
                }
                autoRouteList.remove(autoRoute.getIndex().intValue());

                JSONArray array = JSONArray.parseArray(JSON.toJSONString(autoRouteList,SerializerFeature.DisableCircularReferenceDetect));
                module.put("navList",array);


                JSONObject json  =moduleDataService.replaceModuleData(jsonObject,module,key);
                mockJsonService.saveJsonToFile(jsonObject,id);
                return SuccessTip.create(json);
            }

        }
        return SuccessTip.create();
    }


//    复制路由
    @PostMapping("/{id}/route/op/copy")
    @ApiOperation(value = "复制路由")
    public Tip copyRoute(@PathVariable("id")Long id, @RequestBody AutoRoute autoRoute){
        int currentModule = 0;
        if (autoRoute.getCurrentModule()!=null && autoRoute.getCurrentModule()>0){
            currentModule = autoRoute.getCurrentModule();
        }
        autoRoute.setCurrentModule(null);
        JSONObject jsonObject = autoPageService.getPageConfigJsonByPageId(id);
        String key = moduleService.getModuleKeyByIndex(jsonObject,currentModule);

        JSONObject module =  moduleDataService.getModuleDataByKey(jsonObject,key);
        if (module!=null && module.containsKey("navList") && module.get("navList")!=null){
            JSONArray navList =module.getJSONArray("navList");
            String navListS = JSONObject.toJSONString(navList);
            if (navList!=null && navList.size()>0){
                List<AutoRoute> autoRouteList =  JSONObject.parseArray(navListS, AutoRoute.class);

                if (autoRoute.getIndex()<0|| autoRoute.getIndex()>= autoRouteList.size()){
                    throw new BusinessException(BusinessCode.BadRequest,"index越界");
                }


                AutoRoute index = autoRouteList.get(autoRoute.getIndex());

                autoRouteList.add(navList.size(),index);

                JSONArray array = JSONArray.parseArray(JSON.toJSONString(autoRouteList, SerializerFeature.DisableCircularReferenceDetect));
                module.put("navList",array);


                JSONObject json  =moduleDataService.replaceModuleData(jsonObject,module,key);
                mockJsonService.saveJsonToFile(jsonObject,id);
                return SuccessTip.create(json);
            }

        }

        return SuccessTip.create();
    }



}
