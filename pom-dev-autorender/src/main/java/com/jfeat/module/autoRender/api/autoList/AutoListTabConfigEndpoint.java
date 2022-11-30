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
import com.jfeat.module.autoRender.util.JsonUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api("AutoListTabConfigEndpoint")
@RequestMapping("/dev/auto/forms/autoList")
public class AutoListTabConfigEndpoint {

    @Resource
    AutoPageService autoPageService;

    @Resource
    ModuleService moduleService;

    @Resource
    ModuleDataService moduleDataService;

    @Resource
    MockJsonService mockJsonService;

    //    查看tabConfig
    @GetMapping("/{pageId}/tabConfig")
    public Tip getTabConfig(@PathVariable("pageId") Long pageId,
                            @RequestParam(value = "currentModule", required = false, defaultValue = "0") Integer currentModule) {

        JSONObject json = autoPageService.getPageConfigJsonByPageId(pageId);
        if (json == null) {
            throw new BusinessException(BusinessCode.CodeBase, "json配置不存在");
        }
        JSONObject modules = moduleService.getModuleByIndex(json, currentModule);
        JSONObject moduleData = moduleDataService.getModuleDataByKey(json, modules.getString("key"));

        JSONObject tabConfig = moduleData.getJSONObject("tabConfig");
        return SuccessTip.create(tabConfig);
    }

    //    修改属性
    @PostMapping("/{pageId}/tabConfig")
    public Tip updateConfig(@PathVariable("pageId") Long pageId,
                            @RequestBody JSONObject entity,
                            @RequestParam(value = "currentModule", required = false, defaultValue = "0") Integer currentModule) {

        JSONObject json = autoPageService.getPageConfigJsonByPageId(pageId);
        if (json == null) {
            throw new BusinessException(BusinessCode.CodeBase, "json配置不存在");
        }
        JSONObject modules = moduleService.getModuleByIndex(json, currentModule);
        JSONObject moduleData = moduleDataService.getModuleDataByKey(json, modules.getString("key"));

        if (moduleData != null) {
            if (moduleData.containsKey("tabConfig")) {
                JSONObject tabConfig = moduleData.getJSONObject("tabConfig");
                JsonUtil.modifyJson(tabConfig, entity);
            } else {
                moduleData.put("tabConfig", entity);
            }
        }

        mockJsonService.saveJsonToFile(json, pageId);
        return SuccessTip.create(json);
    }


    //    查看全部的tab
    @GetMapping("/{pageId}/tabConfig/list")
    public Tip getTabConfigList(@PathVariable("pageId") Long pageId,
                                @RequestParam(value = "currentModule", required = false, defaultValue = "0") Integer currentModule) {



        JSONObject json = autoPageService.getPageConfigJsonByPageId(pageId);
        if (json == null) {
            throw new BusinessException(BusinessCode.CodeBase, "json配置不存在");
        }
        JSONObject modules = moduleService.getModuleByIndex(json, currentModule);
        JSONObject moduleData = moduleDataService.getModuleDataByKey(json, modules.getString("key"));

        if (moduleData != null && moduleData.containsKey("tabConfig")) {
            JSONObject tabConfig = moduleData.getJSONObject("tabConfig");
            if (tabConfig != null && tabConfig.containsKey("list")) {
                return SuccessTip.create(tabConfig.get("list"));
            }
        }
        return SuccessTip.create();
    }

    //    查看指定的tab
    @GetMapping("/{pageId}/tabConfig/list/{index}")
    public Tip getTabConfig(@PathVariable("pageId") Long pageId,
                                        @PathVariable("index") Integer index,
                                        @RequestParam(value = "currentModule", required = false, defaultValue = "0") Integer currentModule) {

        JSONObject json = autoPageService.getPageConfigJsonByPageId(pageId);
        if (json == null) {
            throw new BusinessException(BusinessCode.CodeBase, "json配置不存在");
        }
        JSONObject modules = moduleService.getModuleByIndex(json, currentModule);
        JSONObject moduleData = moduleDataService.getModuleDataByKey(json, modules.getString("key"));

        if (moduleData != null && moduleData.containsKey("tabConfig")) {
            JSONObject tabConfig = moduleData.getJSONObject("tabConfig");
            if (tabConfig != null && tabConfig.containsKey("list")) {
                JSONArray jsonArray = tabConfig.getJSONArray("list");
                return SuccessTip.create(jsonArray.get(index));
            }
        }

        return SuccessTip.create();
    }

    //    添加tab
    @PostMapping("/{pageId}/tabConfig/list/op/add")
    public Tip addTabConfigList(@PathVariable("pageId") Long pageId,
                                        @RequestParam(value = "currentModule", required = false, defaultValue = "0") Integer currentModule,
                                        @RequestBody JSONObject entity) {


        JSONObject json = autoPageService.getPageConfigJsonByPageId(pageId);
        if (json == null) {
            throw new BusinessException(BusinessCode.CodeBase, "json配置不存在");
        }
        JSONObject modules = moduleService.getModuleByIndex(json, currentModule);
        JSONObject moduleData = moduleDataService.getModuleDataByKey(json, modules.getString("key"));


        Integer index = null;
        if (entity.containsKey("index")) {
            index = entity.getInteger("index");
            entity.remove("index");
        }


        if (moduleData != null) {
            if (moduleData.containsKey("tabConfig")) {
                JSONObject tabConfig = moduleData.getJSONObject("tabConfig");
                if (tabConfig != null && tabConfig.containsKey("list")) {
                    JSONArray jsonArray = tabConfig.getJSONArray("list");
                    JsonArrayUtil.addJson(jsonArray, entity, index);
                }
            } else {
                throw new BusinessException(BusinessCode.JsonSyntaxError, "未找到tabConfig");
            }
        }

        mockJsonService.saveJsonToFile(json, pageId);
        return SuccessTip.create(json);
    }

    //    修改tab
    @PostMapping("/{pageId}/tabConfig/list/op/modify")
    public Tip modifyTabConfigList(@PathVariable("pageId") Long pageId,
                                           @RequestParam(value = "currentModule", required = false, defaultValue = "0") Integer currentModule,
                                           @RequestBody JSONObject entity) {

        if (!entity.containsKey("index")) {
            throw new BusinessException(BusinessCode.BadRequest, "index为必填项");
        }

        Integer index = entity.getInteger("index");
        entity.remove("index");

        JSONObject json = autoPageService.getPageConfigJsonByPageId(pageId);
        if (json == null) {
            throw new BusinessException(BusinessCode.CodeBase, "json配置不存在");
        }

        JSONObject modules = moduleService.getModuleByIndex(json, currentModule);
        JSONObject moduleData = moduleDataService.getModuleDataByKey(json, modules.getString("key"));

        if (moduleData != null) {
            if (moduleData.containsKey("tabConfig")) {
                JSONObject tabConfig = moduleData.getJSONObject("tabConfig");
                if (tabConfig != null && tabConfig.containsKey("list")) {
                    JSONArray jsonArray = tabConfig.getJSONArray("list");
                    JsonArrayUtil.modifyItemJson(jsonArray, entity, index);
                }
            } else {
                throw new BusinessException(BusinessCode.JsonSyntaxError, "未找到tabConfig");
            }
        }

        mockJsonService.saveJsonToFile(json, pageId);
        return SuccessTip.create(json);
    }

    //    复制tab
    @PostMapping("/{pageId}/tabConfig/list/op/copy")
    public Tip copyTabConfigList(@PathVariable("pageId") Long pageId,
                                         @RequestParam(value = "currentModule", required = false, defaultValue = "0") Integer currentModule,
                                         @RequestBody JSONObject entity) {

        if (!entity.containsKey("from")) {
            throw new BusinessException(BusinessCode.BadRequest, "from为必填项");
        }


        Integer to = null;
        if (entity.containsKey("to")) {
            to = entity.getInteger("to");
        }

        Integer from = entity.getInteger("from");

        JSONObject json = autoPageService.getPageConfigJsonByPageId(pageId);
        if (json == null) {
            throw new BusinessException(BusinessCode.CodeBase, "json配置不存在");
        }

        JSONObject modules = moduleService.getModuleByIndex(json, currentModule);
        JSONObject moduleData = moduleDataService.getModuleDataByKey(json, modules.getString("key"));


        if (moduleData != null) {
            if (moduleData.containsKey("tabConfig")) {
                JSONObject tabConfig = moduleData.getJSONObject("tabConfig");
                if (tabConfig != null && tabConfig.containsKey("list")) {
                    JSONArray jsonArray = tabConfig.getJSONArray("list");
                    JsonArrayUtil.copy(jsonArray, from, to);
                }
            } else {
                throw new BusinessException(BusinessCode.JsonSyntaxError, "未找到tabConfig");
            }
        }


        mockJsonService.saveJsonToFile(json, pageId);
        return SuccessTip.create(json);
    }

    //    移动tab
    @PostMapping("/{pageId}/tabConfig/list/op/move")
    public Tip moveTabConfigList(@PathVariable("pageId") Long pageId,
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


        if (moduleData != null) {
            if (moduleData.containsKey("tabConfig")) {
                JSONObject tabConfig = moduleData.getJSONObject("tabConfig");
                if (tabConfig != null && tabConfig.containsKey("list")) {
                    JSONArray jsonArray = tabConfig.getJSONArray("list");
                    JsonArrayUtil.move(jsonArray, from, to);
                }
            } else {
                throw new BusinessException(BusinessCode.JsonSyntaxError, "未找到tabConfig");
            }
        }

        mockJsonService.saveJsonToFile(json, pageId);
        return SuccessTip.create(json);
    }

    //    移除tab
    @PostMapping("/{pageId}/tabConfig/list/op/remove")
    public Tip removeTabConfigList(@PathVariable("pageId") Long pageId,
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


        if (moduleData != null) {
            if (moduleData.containsKey("tabConfig")) {
                JSONObject tabConfig = moduleData.getJSONObject("tabConfig");
                if (tabConfig != null && tabConfig.containsKey("list")) {
                    JSONArray jsonArray = tabConfig.getJSONArray("list");
                    JsonArrayUtil.remove(jsonArray, index);
                }
            } else {
                throw new BusinessException(BusinessCode.JsonSyntaxError, "未找到tabConfig");
            }
        }

        mockJsonService.saveJsonToFile(json, pageId);
        return SuccessTip.create(json);
    }
}
