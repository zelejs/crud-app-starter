package com.jfeat.module.autoRender.api.autoList;


import com.alibaba.fastjson.JSONObject;
import com.jfeat.am.module.ioJson.services.domain.service.MockJsonService;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.module.autoRender.service.domain.service.AutoPageService;
import com.jfeat.module.autoRender.service.domain.service.ModuleDataService;
import com.jfeat.module.autoRender.service.domain.service.ModuleService;
import com.jfeat.module.autoRender.service.gen.persistence.model.autolist.AutoListModuleData;
import com.jfeat.module.autoRender.service.gen.persistence.model.autolist.AutoListModuleDataResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api("AutoListModuleEndpoint")
@RequestMapping("/dev/auto/forms/autoList")
public class AutoListModuleEndpoint {


    @Resource
    AutoPageService autoPageService;

    @Resource
    ModuleService moduleService;

    @Resource
    ModuleDataService moduleDataService;

    @Resource
    MockJsonService mockJsonService;


    @ApiOperation("添加autoList 的 loadApi")
    @PostMapping("/{pageId}/loadApi")
    public Tip updateAutoListLoadApi(@PathVariable("pageId") Long pageId, @RequestBody AutoListModuleData autoListModuleData) {

        if (autoListModuleData == null || autoListModuleData.getLoadApi() == null) {
            throw new BusinessException(BusinessCode.BadRequest, "loadApi不能为空");
        }
        int currentModule = 0;
        if (autoListModuleData.getCurrentModule() != null && autoListModuleData.getCurrentModule() > 0) {
            currentModule = autoListModuleData.getCurrentModule();
        }

        JSONObject json = autoPageService.getPageConfigJsonByPageId(pageId);
        String key = moduleService.getModuleKeyByIndex(json, currentModule);
        JSONObject jsonObject = moduleDataService.getModuleDataByKey(json, key);

        if (jsonObject != null) {
            jsonObject.put("loadApi", autoListModuleData.getLoadApi());
        }

        mockJsonService.saveJsonToFile(json, pageId);
        return SuccessTip.create(json);

    }


    @ApiOperation("添加autoList 的 itemNavigation")
    @PostMapping("/{pageId}/itemNavigation")
    public Tip updateAutoListItemNavigation(@PathVariable("pageId") Long pageId, @RequestBody AutoListModuleData autoListModuleData) {

        if (autoListModuleData == null || autoListModuleData.getItemNavigation() == null) {
            throw new BusinessException(BusinessCode.BadRequest, "itemNavigation不能为空");
        }
        int currentModule = 0;
        if (autoListModuleData.getCurrentModule() != null && autoListModuleData.getCurrentModule() > 0) {
            currentModule = autoListModuleData.getCurrentModule();
        }

        JSONObject json = autoPageService.getPageConfigJsonByPageId(pageId);
        String key = moduleService.getModuleKeyByIndex(json, currentModule);
        JSONObject jsonObject = moduleDataService.getModuleDataByKey(json, key);

        if (jsonObject != null) {
            jsonObject.put("itemNavigation", autoListModuleData.getItemNavigation());
        }
        mockJsonService.saveJsonToFile(json, pageId);
        return SuccessTip.create(json);

    }


    @ApiOperation("添加autoList 的 response")
    @PostMapping("/{pageId}/response")
    public Tip updateAutoListResponse(@PathVariable("pageId") Long pageId, @RequestBody AutoListModuleDataResponse response) {

        if (response == null || response.getList() == null) {
            throw new BusinessException(BusinessCode.BadRequest, "response不能为空");
        }
        int currentModule = 0;
        if (response.getCurrentModule() != null && response.getCurrentModule() > 0) {
            currentModule = response.getCurrentModule();
        }

        JSONObject json = autoPageService.getPageConfigJsonByPageId(pageId);
        String key = moduleService.getModuleKeyByIndex(json, currentModule);
        JSONObject jsonObject = moduleDataService.getModuleDataByKey(json, key);

        if (jsonObject != null) {
            if (jsonObject.containsKey("response")){
                JSONObject responseJson = jsonObject.getJSONObject("response");
                responseJson.put("list",response.getList());
            }else {
                JSONObject responseJson =new JSONObject();
                responseJson.put("list",response.getList());
                jsonObject.put("response",responseJson);
            }
        }
        mockJsonService.saveJsonToFile(json, pageId);
        return SuccessTip.create(json);
    }


    @ApiOperation("修改autolist binding")
    @PostMapping("/{pageId}/binding")
    public Tip updateAutoListBinding(@PathVariable("pageId") Long pageId,
                                     @RequestParam(value = "currentModule",required = false,defaultValue = "0") Integer currentModule,
                                     @RequestBody JSONObject binding){
        JSONObject json = autoPageService.getPageConfigJsonByPageId(pageId);
        JSONObject modules = moduleService.getModuleByIndex(json, currentModule);
        JSONObject jsonObject = moduleDataService.getModuleDataByKey(json, modules.getString("key"));
        if (jsonObject!=null){
            if (binding!=null){
                jsonObject.put("binding",binding);
            }else {
                jsonObject.put("binding",new JSONObject());
            }

        }
        mockJsonService.saveJsonToFile(json, pageId);

        return SuccessTip.create(json);
    }



}
