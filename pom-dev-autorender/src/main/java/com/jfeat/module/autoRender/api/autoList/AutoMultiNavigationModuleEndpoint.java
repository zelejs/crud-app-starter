package com.jfeat.module.autoRender.api.autoList;


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
@Api("AutoListModuleEndpoint")
@RequestMapping("/dev/auto/forms/autoList")
public class AutoMultiNavigationModuleEndpoint {

    @Resource
    AutoPageService autoPageService;

    @Resource
    ModuleService moduleService;

    @Resource
    ModuleDataService moduleDataService;

    @Resource
    MockJsonService mockJsonService;


    //    查看全部
    @GetMapping("/{pageId}/multiNavigationModule")
    public Tip getAllMultiNavigationModule(@PathVariable("pageId") Long pageId,
                                           @RequestParam(value = "currentModule", required = false, defaultValue = "0") Integer currentModule) {

        JSONObject json = autoPageService.getPageConfigJsonByPageId(pageId);
        if (json == null) {
            throw new BusinessException(BusinessCode.CodeBase, "json配置不存在");
        }
        JSONObject modules = moduleService.getModuleByIndex(json, currentModule);
        JSONObject moduleData = moduleDataService.getModuleDataByKey(json, modules.getString("key"));

        JSONObject multiNavigationModule = moduleData.getJSONObject("multiNavigationModule");
        JSONArray multiItemNavigation = moduleData.getJSONArray("multiItemNavigation");

        JSONObject result = new JSONObject();
        result.put("multiNavigationModule", multiNavigationModule);
        result.put("multiItemNavigation", multiItemNavigation);
        return SuccessTip.create(result);
    }

//    查看指定

    @GetMapping("/{pageId}/multiNavigationModule/{index}")
    public Tip getMultiNavigationModule(@PathVariable("pageId") Long pageId,
                                        @PathVariable("index") Integer index,
                                        @RequestParam(value = "currentModule", required = false, defaultValue = "0") Integer currentModule) {

        JSONObject json = autoPageService.getPageConfigJsonByPageId(pageId);
        if (json == null) {
            throw new BusinessException(BusinessCode.CodeBase, "json配置不存在");
        }
        JSONObject modules = moduleService.getModuleByIndex(json, currentModule);
        JSONObject moduleData = moduleDataService.getModuleDataByKey(json, modules.getString("key"));

        JSONObject multiNavigationModule = moduleData.getJSONObject("multiNavigationModule");
        JSONArray multiItemNavigation = moduleData.getJSONArray("multiItemNavigation");

        JSONObject multiNavigationModuleItem = multiNavigationModule.getJSONArray("options").getJSONObject(index.intValue());
        String multiItemNavigationItem = multiItemNavigation.getString(index.intValue());

        JSONObject result = new JSONObject();
        result.put("multiNavigationModule", multiNavigationModuleItem);
        result.put("multiItemNavigation", multiItemNavigationItem);
        return SuccessTip.create(result);
    }

    //    添加
    @PostMapping("/{pageId}/multiNavigationModule/op/add")
    public Tip addMultiNavigationModule(@PathVariable("pageId") Long pageId,
                                        @RequestParam(value = "currentModule", required = false, defaultValue = "0") Integer currentModule,
                                        @RequestBody JSONObject entity) {

        if (!entity.containsKey("multiNavigationModule") || !(entity.get("multiNavigationModule") instanceof JSONObject)) {
            throw new BusinessException(BusinessCode.BadRequest, "不存在multiNavigationModule 或者 multiNavigationModule 不符合规范");
        }
        if (!entity.containsKey("multiItemNavigation")) {
            throw new BusinessException(BusinessCode.BadRequest, "multiItemNavigation 为必填项");
        }

        JSONObject json = autoPageService.getPageConfigJsonByPageId(pageId);
        if (json == null) {
            throw new BusinessException(BusinessCode.CodeBase, "json配置不存在");
        }

        JSONObject modules = moduleService.getModuleByIndex(json, currentModule);
        JSONObject moduleData = moduleDataService.getModuleDataByKey(json, modules.getString("key"));


        JSONArray multiItemNavigation = new JSONArray();
        if (moduleData != null && moduleData.containsKey("multiItemNavigation")) {
            multiItemNavigation = moduleData.getJSONArray("multiItemNavigation");
        } else {
            moduleData.put("multiItemNavigation", multiItemNavigation);
        }

        Integer index = multiItemNavigation.size();
        if (entity.containsKey("index")) {
            index = entity.getInteger("index");
        }

        JSONArray multiNavigationModuleItem = new JSONArray();
        if (moduleData != null && moduleData.containsKey("multiNavigationModule")) {
            JSONObject multiNavigationModule = moduleData.getJSONObject("multiNavigationModule");
            if (multiNavigationModule != null && multiNavigationModule.containsKey("options")) {
                multiNavigationModuleItem = multiNavigationModule.getJSONArray("options");
            } else {
                multiNavigationModule.put("options", multiNavigationModuleItem);
            }

        } else {
            JSONObject multiNavigationModule = new JSONObject();
            multiNavigationModule.put("options", multiNavigationModuleItem);
            moduleData.put("multiNavigationModule", multiNavigationModule);
        }

        multiItemNavigation.add(index, entity.getString("multiItemNavigation"));
        multiNavigationModuleItem.add(index, entity.getJSONObject("multiNavigationModule"));

        mockJsonService.saveJsonToFile(json, pageId);
        return SuccessTip.create(json);
    }

//    修改

    @PostMapping("/{pageId}/multiNavigationModule/op/modify")
    public Tip modifyMultiNavigationModule(@PathVariable("pageId") Long pageId,
                                           @RequestParam(value = "currentModule", required = false, defaultValue = "0") Integer currentModule,
                                           @RequestBody JSONObject entity) {

        if (!entity.containsKey("index")) {
            throw new BusinessException(BusinessCode.BadRequest, "index为必填项");
        }

        Integer index = entity.getInteger("index");

        JSONObject json = autoPageService.getPageConfigJsonByPageId(pageId);
        if (json == null) {
            throw new BusinessException(BusinessCode.CodeBase, "json配置不存在");
        }

        JSONObject modules = moduleService.getModuleByIndex(json, currentModule);
        JSONObject moduleData = moduleDataService.getModuleDataByKey(json, modules.getString("key"));


        if (entity.containsKey("multiNavigationModule") && (entity.get("multiNavigationModule") instanceof JSONObject)) {
            if (moduleData != null && moduleData.containsKey("multiNavigationModule")) {
                JSONObject multiNavigationModule = moduleData.getJSONObject("multiNavigationModule");
                if (multiNavigationModule != null && multiNavigationModule.containsKey("options")) {
                    JSONArray multiNavigationModuleItem = multiNavigationModule.getJSONArray("options");
                    JsonArrayUtil.modifyItemJson(multiNavigationModuleItem, entity.getJSONObject("multiNavigationModule"), index);
                } else {
                    throw new BusinessException(BusinessCode.JsonSyntaxError, "不存在options");
                }

            } else {
                throw new BusinessException(BusinessCode.JsonSyntaxError, "不存在multiNavigationModule");
            }

        }

        if (entity.containsKey("multiItemNavigation")) {
            if (moduleData != null && moduleData.containsKey("multiItemNavigation")) {
                JSONArray multiItemNavigation = moduleData.getJSONArray("multiItemNavigation");
                multiItemNavigation.set(index, entity.getString("multiItemNavigation"));
            } else {
                throw new BusinessException(BusinessCode.JsonSyntaxError, "不存在multiItemNavigation");
            }
        }
        mockJsonService.saveJsonToFile(json, pageId);
        return SuccessTip.create(json);
    }

    //    移动
    @PostMapping("/{pageId}/multiNavigationModule/op/move")
    public Tip moveMultiNavigationModule(@PathVariable("pageId") Long pageId,
                                           @RequestParam(value = "currentModule", required = false, defaultValue = "0") Integer currentModule,
                                           @RequestBody JSONObject entity) {

        if (!entity.containsKey("from")) {
            throw new BusinessException(BusinessCode.BadRequest, "from为必填项");
        }

        if (!entity.containsKey("to")) {
            throw new BusinessException(BusinessCode.BadRequest, "to为必填项");
        }

        Integer from = entity.getInteger("from");
        Integer to = entity.getInteger("to");

        JSONObject json = autoPageService.getPageConfigJsonByPageId(pageId);
        if (json == null) {
            throw new BusinessException(BusinessCode.CodeBase, "json配置不存在");
        }

        JSONObject modules = moduleService.getModuleByIndex(json, currentModule);
        JSONObject moduleData = moduleDataService.getModuleDataByKey(json, modules.getString("key"));


        JSONObject multiNavigationModule = moduleData.getJSONObject("multiNavigationModule");
        JSONArray multiItemNavigation = moduleData.getJSONArray("multiItemNavigation");
        JSONArray multiNavigationModuleItem = multiNavigationModule.getJSONArray("options");

        JsonArrayUtil.move(multiItemNavigation,from,to);
        JsonArrayUtil.move(multiNavigationModuleItem,from,to);

        mockJsonService.saveJsonToFile(json, pageId);
        return SuccessTip.create(json);
    }

//    复制
    @PostMapping("/{pageId}/multiNavigationModule/op/copy")
    public Tip copyMultiNavigationModule(@PathVariable("pageId") Long pageId,
                                         @RequestParam(value = "currentModule", required = false, defaultValue = "0") Integer currentModule,
                                         @RequestBody JSONObject entity) {

        if (!entity.containsKey("from")) {
            throw new BusinessException(BusinessCode.BadRequest, "from为必填项");
        }



        Integer from = entity.getInteger("from");

        JSONObject json = autoPageService.getPageConfigJsonByPageId(pageId);
        if (json == null) {
            throw new BusinessException(BusinessCode.CodeBase, "json配置不存在");
        }

        JSONObject modules = moduleService.getModuleByIndex(json, currentModule);
        JSONObject moduleData = moduleDataService.getModuleDataByKey(json, modules.getString("key"));


        JSONObject multiNavigationModule = moduleData.getJSONObject("multiNavigationModule");
        JSONArray multiItemNavigation = moduleData.getJSONArray("multiItemNavigation");
        JSONArray multiNavigationModuleItem = multiNavigationModule.getJSONArray("options");

        Integer to = multiItemNavigation.size();
        if (entity.containsKey("to")) {
            to = entity.getInteger("to");
        }

        JsonArrayUtil.copy(multiItemNavigation,from,to);
        JsonArrayUtil.copy(multiNavigationModuleItem,from,to);

        mockJsonService.saveJsonToFile(json, pageId);
        return SuccessTip.create(json);
    }

//    移除

    @PostMapping("/{pageId}/multiNavigationModule/op/remove")
    public Tip removeMultiNavigationModule(@PathVariable("pageId") Long pageId,
                                         @RequestParam(value = "currentModule", required = false, defaultValue = "0") Integer currentModule,
                                         @RequestBody JSONObject entity) {

        if (!entity.containsKey("index")) {
            throw new BusinessException(BusinessCode.BadRequest, "index为必填项");
        }

        Integer index = entity.getInteger("index");

        JSONObject json = autoPageService.getPageConfigJsonByPageId(pageId);
        if (json == null) {
            throw new BusinessException(BusinessCode.CodeBase, "json配置不存在");
        }

        JSONObject modules = moduleService.getModuleByIndex(json, currentModule);
        JSONObject moduleData = moduleDataService.getModuleDataByKey(json, modules.getString("key"));


        JSONObject multiNavigationModule = moduleData.getJSONObject("multiNavigationModule");
        JSONArray multiItemNavigation = moduleData.getJSONArray("multiItemNavigation");
        JSONArray multiNavigationModuleItem = multiNavigationModule.getJSONArray("options");


        JsonArrayUtil.remove(multiItemNavigation,index);
        JsonArrayUtil.remove(multiNavigationModuleItem,index);

        mockJsonService.saveJsonToFile(json, pageId);
        return SuccessTip.create(json);
    }
}
