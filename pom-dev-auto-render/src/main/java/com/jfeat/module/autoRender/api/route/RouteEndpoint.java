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
import com.jfeat.module.autoRender.service.domain.service.ModuleService;
import com.jfeat.module.autoRender.service.gen.persistence.model.Route;
import com.jfeat.module.autoRender.util.ParameterUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/dev/auto/forms")
public class RouteEndpoint {

    @Resource
    MockJsonService mockJsonService;

    @Resource
    ModuleService moduleService;



    //    查看路由
    @GetMapping("{id}/routes/{index}")
    public Tip getRouter(@PathVariable("id")Long id,@PathVariable Integer index){
        JSONObject jsonObject =  mockJsonService.readJsonFile(id);

        List<String> keyList = moduleService.getModuleKeyByType(jsonObject,"navlist");

        if (keyList!=null&&keyList.size()==1){
            JSONObject module =  moduleService.getModuleDataByKey(jsonObject,keyList.get(0));
            if (module!=null && module.containsKey("navList") && module.get("navList")!=null){
                JSONArray navList =module.getJSONArray("navList");
                if (navList!=null && navList.size()>0 && index<navList.size()){
                    return SuccessTip.create(navList.get(index));
                }


            }
        }
        return SuccessTip.create();
    }


    //    添加路由
    @PostMapping("/{id}/routes")
    public Tip addRoute(@PathVariable("id")Long id, @RequestBody Route route){
        JSONObject jsonObject =  mockJsonService.readJsonFile(id);

        List<String> keyList = moduleService.getModuleKeyByType(jsonObject,"navlist");

        if (keyList!=null&&keyList.size()==1){
            JSONObject module =  moduleService.getModuleDataByKey(jsonObject,keyList.get(0));
            if (module!=null && module.containsKey("navList") && module.get("navList")!=null){
                JSONArray navList =module.getJSONArray("navList");
                String navListS = JSONObject.toJSONString(navList);
                if (navList!=null && navList.size()>0){
                    List<Route> routeList =  JSONObject.parseArray(navListS,Route.class);


                    routeList.add(navList.size(),route);

                    JSONArray array = JSONArray.parseArray(JSON.toJSONString(routeList,SerializerFeature.DisableCircularReferenceDetect));
                    module.put("navList",array);


                    JSONObject json  =moduleService.replaceModuleData(jsonObject,module,keyList.get(0));
                    mockJsonService.saveJsonToFile(jsonObject,id);
                    return SuccessTip.create(json);
                }

            }
        }
        return SuccessTip.create();
    }


//    修改路由
    @PutMapping("/{id}/routes/{index}")
    public Tip updateRouteDate(@PathVariable("id")Long id, @PathVariable("index")Integer index,@RequestBody Route route){
        JSONObject jsonObject =  mockJsonService.readJsonFile(id);

        List<String> keyList = moduleService.getModuleKeyByType(jsonObject,"navlist");

        if (keyList!=null&&keyList.size()==1){
            JSONObject module =  moduleService.getModuleDataByKey(jsonObject,keyList.get(0));
            if (module!=null && module.containsKey("navList") && module.get("navList")!=null){
                JSONArray navList =module.getJSONArray("navList");
                String navListS = JSONObject.toJSONString(navList);
                if (navList!=null && navList.size()>0){
                    List<Route> routeList =  JSONObject.parseArray(navListS,Route.class);
                    if (index<0||index>routeList.size()){
                        throw new BusinessException(BusinessCode.BadRequest,"index越界");
                    }

                    routeList.set(index,ParameterUtil.parameterReplace(routeList.get(index),route));

                    JSONArray array = JSONArray.parseArray(JSON.toJSONString(routeList));
                    module.put("navList",array);

                    JSONObject json  =moduleService.replaceModuleData(jsonObject,module,keyList.get(0));
                    mockJsonService.saveJsonToFile(jsonObject,id);
                    return SuccessTip.create(json);
                }

            }
        }
        return SuccessTip.create();
    }


//   变更路由跳转页面
    @PostMapping("/{id}/routes/{index}/op/next")
    public Tip updateRouteDate(@PathVariable("id")Long id, @RequestBody Route route,@PathVariable("index") Integer index){
        JSONObject jsonObject =  mockJsonService.readJsonFile(id);

        List<String> keyList = moduleService.getModuleKeyByType(jsonObject,"navlist");

        if (keyList!=null&&keyList.size()==1){
            JSONObject module =  moduleService.getModuleDataByKey(jsonObject,keyList.get(0));
            if (module!=null && module.containsKey("navList") && module.get("navList")!=null){
                JSONArray navList =module.getJSONArray("navList");
                String navListS = JSONObject.toJSONString(navList);
                if (navList!=null && navList.size()>0){
                    List<Route> routeList =  JSONObject.parseArray(navListS,Route.class);
                    if (index<0||index>=routeList.size()){
                        throw new BusinessException(BusinessCode.BadRequest,"index越界");
                    }

                    Route navRouter = routeList.get(index);
//                    routeList.get(index)
                    String nav = navRouter.getNav();

                    if (nav!=null&& !nav.equals("")){
                        String pattern = "=(\\d+)";

                        Pattern p = Pattern.compile(pattern);
                        // get a matcher object
                        Matcher m = p.matcher(nav);
                        if (m.find()){
                            nav = m.replaceFirst("=".concat(String.valueOf(route.getPageId())));
                            navRouter.setNav(nav);
                        }else {
                            throw new BusinessException(BusinessCode.CodeBase,"未找到pageID");
                        }



                        routeList.set(index,navRouter);
                    }else {
                        throw new BusinessException(BusinessCode.EmptyNotAllowed,"nav为空");
                    }


                    JSONArray array = JSONArray.parseArray(JSON.toJSONString(routeList));
                    module.put("navList",array);

                    JSONObject json  =moduleService.replaceModuleData(jsonObject,module,keyList.get(0));
                    mockJsonService.saveJsonToFile(jsonObject,id);
                    return SuccessTip.create(json);
                }

            }
        }
        return SuccessTip.create();
    }


//    移动路由位置
    @PostMapping("/{id}/route/op/arrange")
    public Tip updateRoutePosition(@PathVariable("id")Long id, @RequestBody Route route){
        JSONObject jsonObject =  mockJsonService.readJsonFile(id);

        List<String> keyList = moduleService.getModuleKeyByType(jsonObject,"navlist");

        if (keyList!=null&&keyList.size()==1){
            JSONObject module =  moduleService.getModuleDataByKey(jsonObject,keyList.get(0));
            if (module!=null && module.containsKey("navList") && module.get("navList")!=null){
                JSONArray navList =module.getJSONArray("navList");
                String navListS = JSONObject.toJSONString(navList);
                if (navList!=null && navList.size()>0){
                    List<Route> routeList =  JSONObject.parseArray(navListS,Route.class);

                    if (route.getFrom()<0||route.getFrom()>=routeList.size()){
                        throw new BusinessException(BusinessCode.BadRequest,"from越界");
                    }

                    if (route.getTo()<0||route.getTo()>routeList.size()){
                        throw new BusinessException(BusinessCode.BadRequest,"to越界");
                    }

                    Route indexRoute = routeList.get(route.getFrom());
                    routeList.remove(route.getFrom().intValue());
                    routeList.add(route.getTo().intValue(),indexRoute);

                    JSONArray array = JSONArray.parseArray(JSON.toJSONString(routeList,SerializerFeature.DisableCircularReferenceDetect));
                    module.put("navList",array);

                    JSONObject json  =moduleService.replaceModuleData(jsonObject,module,keyList.get(0));
                    mockJsonService.saveJsonToFile(jsonObject,id);
                    return SuccessTip.create(json);
                }

            }
        }
        return SuccessTip.create();
    }





//    移除路由
    @PostMapping("/{id}/route/op/remove")
    public Tip removeRoute(@PathVariable("id")Long id, @RequestBody Route route){
        JSONObject jsonObject =  mockJsonService.readJsonFile(id);

        List<String> keyList = moduleService.getModuleKeyByType(jsonObject,"navlist");

        if (keyList!=null&&keyList.size()==1){
            JSONObject module =  moduleService.getModuleDataByKey(jsonObject,keyList.get(0));
            if (module!=null && module.containsKey("navList") && module.get("navList")!=null){
                JSONArray navList =module.getJSONArray("navList");
                String navListS = JSONObject.toJSONString(navList);
                if (navList!=null && navList.size()>0){
                    List<Route> routeList =  JSONObject.parseArray(navListS,Route.class);


                    if (route.getIndex()<0||route.getIndex()>=routeList.size()){
                        throw new BusinessException(BusinessCode.BadRequest,"target越界");
                    }
                    routeList.remove(route.getIndex().intValue());

                    JSONArray array = JSONArray.parseArray(JSON.toJSONString(routeList,SerializerFeature.DisableCircularReferenceDetect));
                    module.put("navList",array);


                    JSONObject json  =moduleService.replaceModuleData(jsonObject,module,keyList.get(0));
                    mockJsonService.saveJsonToFile(jsonObject,id);
                    return SuccessTip.create(json);
                }

            }
        }
        return SuccessTip.create();
    }


//    复制路由
    @PostMapping("/{id}/route/op/copy")
    public Tip copyRoute(@PathVariable("id")Long id, @RequestBody Route route){
        JSONObject jsonObject =  mockJsonService.readJsonFile(id);


        List<String> keyList = moduleService.getModuleKeyByType(jsonObject,"navlist");

        if (keyList!=null&&keyList.size()==1){
            JSONObject module =  moduleService.getModuleDataByKey(jsonObject,keyList.get(0));
            if (module!=null && module.containsKey("navList") && module.get("navList")!=null){
                JSONArray navList =module.getJSONArray("navList");
                String navListS = JSONObject.toJSONString(navList);
                if (navList!=null && navList.size()>0){
                    List<Route> routeList =  JSONObject.parseArray(navListS,Route.class);

                    if (route.getIndex()<0||route.getIndex()>=routeList.size()){
                        throw new BusinessException(BusinessCode.BadRequest,"index越界");
                    }


                    Route index = routeList.get(route.getIndex());

                    routeList.add(navList.size(),index);

                    JSONArray array = JSONArray.parseArray(JSON.toJSONString(routeList, SerializerFeature.DisableCircularReferenceDetect));
                    module.put("navList",array);


                    JSONObject json  =moduleService.replaceModuleData(jsonObject,module,keyList.get(0));
                    mockJsonService.saveJsonToFile(jsonObject,id);
                    return SuccessTip.create(json);
                }

            }
        }
        return SuccessTip.create();
    }



}
