package com.jfeat.module.autoRender.api.common;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfeat.am.module.ioJson.services.domain.service.MockJsonService;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.module.autoRender.service.domain.service.AutoPageService;
import com.jfeat.module.autoRender.service.domain.service.ModuleDataService;
import com.jfeat.module.autoRender.service.domain.service.ModuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

@RestController
@Api("AutoListModuleEndpoint")
@RequestMapping("/dev/auto/forms/common/moduleData")
public class CommonModuleDataEndpoint {


    @Resource
    AutoPageService autoPageService;

    @Resource
    ModuleService moduleService;

    @Resource
    ModuleDataService moduleDataService;

    @Resource
    MockJsonService mockJsonService;

//    添加和修改moduleData 属性
    @PostMapping("/{pageId}")
    @ApiOperation("通用修改moduleData 属性值")
    public Tip updateModuleData(
            @PathVariable("pageId") Long pageId,
            @RequestParam(value = "currentModule",required = false,defaultValue = "0") Integer currentModule,
            @RequestBody Map<String,Object> map){

        JSONObject json = autoPageService.getPageConfigJsonByPageId(pageId);
        if (json==null){
            throw new BusinessException(BusinessCode.CodeBase,"json配置不存在");
        }
        String key = moduleService.getModuleKeyByIndex(json, currentModule);
        JSONObject moduleData = moduleDataService.getModuleDataByKey(json, key);

        if (map!=null){
            Set<String> strings = map.keySet();
            for (String propkey:strings){
                moduleData.put(propkey,map.get(propkey));
            }
        }

        mockJsonService.saveJsonToFile(json, pageId);
        return SuccessTip.create(json);
    }


    @DeleteMapping("/{pageId}/{name}")
    @ApiOperation("删除moduleData 属性值")
    public Tip deleteModuleData(@PathVariable("pageId") Long pageId,
                                @PathVariable("name") String name,@RequestParam(value = "currentModule",required = false,defaultValue = "0") Integer currentModule){

        JSONObject json = autoPageService.getPageConfigJsonByPageId(pageId);
        if (json==null){
            throw new BusinessException(BusinessCode.CodeBase,"json配置不存在");
        }
        String key = moduleService.getModuleKeyByIndex(json, currentModule);
        JSONObject moduleData = moduleDataService.getModuleDataByKey(json, key);

        moduleData.remove(name);
        mockJsonService.saveJsonToFile(json, pageId);
        return SuccessTip.create(json);

    }
//    删除moduleData属性
}
