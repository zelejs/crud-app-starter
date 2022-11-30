package com.jfeat.module.autoRender.api.modules;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfeat.am.module.ioJson.services.domain.service.MockJsonService;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.module.autoRender.service.domain.service.AutoPageService;
import com.jfeat.module.autoRender.service.domain.service.ModuleDataService;
import com.jfeat.module.autoRender.service.domain.service.ModuleService;
import com.jfeat.module.autoRender.util.JsonArrayUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api("AutoModulesEndpoint")
@RequestMapping("/dev/auto/forms/modules")
public class AutoModulesEndpoint {

    @Resource
    AutoPageService autoPageService;

    @Resource
    ModuleService moduleService;

    @Resource
    ModuleDataService moduleDataService;

    @Resource
    MockJsonService mockJsonService;

//    修改container
    @PostMapping("/{pageId}/container")
    public Tip updateContainer(@PathVariable("pageId") Long pageId,
                               @RequestParam(value = "currentModule",required = false,defaultValue = "0") Integer currentModule,
                               @RequestBody JSONObject container){
        JSONObject json = autoPageService.getPageConfigJsonByPageId(pageId);
        JSONObject modules = moduleService.getModuleByIndex(json, currentModule);
        if (modules!=null){
            if (container!=null){
                modules.put("container",container);
            }else {
                modules.put("container",new JSONObject());
            }
        }
        mockJsonService.saveJsonToFile(json, pageId);
        return SuccessTip.create(json);
    }
}
