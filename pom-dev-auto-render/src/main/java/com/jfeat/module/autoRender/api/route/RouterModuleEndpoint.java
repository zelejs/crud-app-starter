package com.jfeat.module.autoRender.api.route;


import com.alibaba.fastjson.JSONObject;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.module.autoRender.service.domain.service.AutoPageService;
import com.jfeat.module.autoRender.service.domain.service.ModuleService;
import com.jfeat.module.autoRender.service.gen.persistence.model.AutoRoute;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/dev/auto/forms")
public class RouterModuleEndpoint {

    @Resource
    AutoPageService autoPageService;

    @Resource
    ModuleService moduleService;

    @GetMapping("/{id}//route/module")
    public Tip getRouteModule(@PathVariable("id")Long id,@RequestParam(value = "currentModule",required = false,defaultValue = "0") Integer currentModule){


        JSONObject json = autoPageService.getPageConfigJsonByPageId(id);
        String key = moduleService.getModuleKeyByIndex(json,currentModule);
        JSONObject jsonObject =  moduleService.getModuleDataByKey(json,key);
        if (jsonObject!=null&& jsonObject.containsKey("itemModule")&&jsonObject.getJSONObject("itemModule")!=null){
            JSONObject itemModule = jsonObject.getJSONObject("itemModule");
            if (itemModule.containsKey("name")&& itemModule.getString("name")!=null){
                return SuccessTip.create(itemModule.getString("name"));
            }
        }
        return SuccessTip.create();
    }

    @PostMapping("/{id}/route/module")
    public Tip updateRouteModule(@PathVariable("id") Long id, @RequestBody AutoRoute autoRoute){

        int currentModule = 0;
        if (autoRoute.getCurrentModule()!=null && autoRoute.getCurrentModule()>0){
            currentModule = autoRoute.getCurrentModule();
        }
        JSONObject json = autoPageService.getPageConfigJsonByPageId(id);
        String key = moduleService.getModuleKeyByIndex(json,currentModule);

        JSONObject jsonObject =  moduleService.getModuleDataByKey(json,key);
        if (jsonObject!=null&& jsonObject.containsKey("itemModule")&&jsonObject.getJSONObject("itemModule")!=null){
            JSONObject itemModule = jsonObject.getJSONObject("itemModule");
            itemModule.put("name",autoRoute.getModuleName());
        }
        return SuccessTip.create(json);
    }

}
