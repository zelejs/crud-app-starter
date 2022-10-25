package com.jfeat.module.autoRender.api.route;


import com.alibaba.fastjson.JSONObject;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.module.autoRender.service.domain.service.AutoPageService;
import com.jfeat.module.autoRender.service.domain.service.ModuleDataService;
import com.jfeat.module.autoRender.service.domain.service.ModuleService;
import com.jfeat.module.autoRender.service.gen.persistence.model.AutoRoute;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api("RouterModule")
@RequestMapping("/dev/auto/forms")
public class RouterModuleEndpoint {

    @Resource
    AutoPageService autoPageService;

    @Resource
    ModuleService moduleService;

    @Resource
    ModuleDataService moduleDataService;

    @GetMapping("/{id}//route/module")
    @ApiOperation(value = "获取路由module")
    public Tip getRouteModule(@PathVariable("id")Long id,@RequestParam(value = "currentModule",required = false,defaultValue = "0") Integer currentModule){


        JSONObject json = autoPageService.getPageConfigJsonByPageId(id);
        String key = moduleService.getModuleKeyByIndex(json,currentModule);
        JSONObject jsonObject =  moduleDataService.getModuleDataByKey(json,key);
        if (jsonObject!=null&& jsonObject.containsKey("itemModule")&&jsonObject.getJSONObject("itemModule")!=null){
            JSONObject itemModule = jsonObject.getJSONObject("itemModule");
            if (itemModule.containsKey("name")&& itemModule.getString("name")!=null){
                return SuccessTip.create(itemModule.getString("name"));
            }
        }
        return SuccessTip.create();
    }

    @PostMapping("/{id}/route/module")
    @ApiOperation(value = "更新路由module")
    public Tip updateRouteModule(@PathVariable("id") Long id, @RequestBody AutoRoute autoRoute){

        int currentModule = 0;
        if (autoRoute.getCurrentModule()!=null && autoRoute.getCurrentModule()>0){
            currentModule = autoRoute.getCurrentModule();
        }
        JSONObject json = autoPageService.getPageConfigJsonByPageId(id);
        String key = moduleService.getModuleKeyByIndex(json,currentModule);

        JSONObject jsonObject =  moduleDataService.getModuleDataByKey(json,key);
        if (jsonObject!=null&& jsonObject.containsKey("itemModule")&&jsonObject.getJSONObject("itemModule")!=null){
            JSONObject itemModule = jsonObject.getJSONObject("itemModule");
            itemModule.put("name",autoRoute.getModuleName());
        }
        return SuccessTip.create(json);
    }

}
