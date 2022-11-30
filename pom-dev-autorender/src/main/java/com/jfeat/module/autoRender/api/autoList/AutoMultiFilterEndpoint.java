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
public class AutoMultiFilterEndpoint {


    @Resource
    AutoPageService autoPageService;

    @Resource
    ModuleService moduleService;

    @Resource
    ModuleDataService moduleDataService;

    @Resource
    MockJsonService mockJsonService;



    //    查看全部的tab
    @GetMapping("/{pageId}/multiFilter")
    public Tip getMultiFilterList(@PathVariable("pageId") Long pageId,
                                @RequestParam(value = "currentModule", required = false, defaultValue = "0") Integer currentModule) {



        JSONObject json = autoPageService.getPageConfigJsonByPageId(pageId);
        if (json == null) {
            throw new BusinessException(BusinessCode.CodeBase, "json配置不存在");
        }
        JSONObject modules = moduleService.getModuleByIndex(json, currentModule);
        JSONObject moduleData = moduleDataService.getModuleDataByKey(json, modules.getString("key"));

        if (moduleData != null && moduleData.containsKey("multiFilter")) {
            return SuccessTip.create(moduleData.get("multiFilter"));
        }
        return SuccessTip.create();
    }

    //    查看指定的tab
    @GetMapping("/{pageId}/multiFilter/{index}")
    public Tip getMultiFilter(@PathVariable("pageId") Long pageId,
                            @PathVariable("index") Integer index,
                            @RequestParam(value = "currentModule", required = false, defaultValue = "0") Integer currentModule) {

        JSONObject json = autoPageService.getPageConfigJsonByPageId(pageId);
        if (json == null) {
            throw new BusinessException(BusinessCode.CodeBase, "json配置不存在");
        }
        JSONObject modules = moduleService.getModuleByIndex(json, currentModule);
        JSONObject moduleData = moduleDataService.getModuleDataByKey(json, modules.getString("key"));

        if (moduleData != null && moduleData.containsKey("multiFilter")) {
            JSONArray multiFilter = moduleData.getJSONArray("multiFilter");
            return SuccessTip.create(multiFilter.get(index));
        }

        return SuccessTip.create();
    }

    //    添加tab
    @PostMapping("/{pageId}/multiFilter/op/add")
    public Tip addMultiFilter(@PathVariable("pageId") Long pageId,
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
            if (moduleData.containsKey("multiFilter")) {
                JSONArray multiFilter = moduleData.getJSONArray("multiFilter");
                JsonArrayUtil.addJson(multiFilter, entity, index);
            } else {
                JSONArray multiFilter = new JSONArray();
                JsonArrayUtil.addJson(multiFilter,entity,null);
                moduleData.put("multiFilter",multiFilter);
            }
        }

        mockJsonService.saveJsonToFile(json, pageId);
        return SuccessTip.create(json);
    }

    //    修改tab
    @PostMapping("/{pageId}/multiFilter/op/modify")
    public Tip modifyMultiFilter(@PathVariable("pageId") Long pageId,
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
            if (moduleData.containsKey("multiFilter")) {
                JSONArray multiFilter = moduleData.getJSONArray("multiFilter");
                JsonArrayUtil.modifyItemJson(multiFilter, entity, index);
            } else {
                throw new BusinessException(BusinessCode.JsonSyntaxError, "未找到multiFilter");
            }
        }

        mockJsonService.saveJsonToFile(json, pageId);
        return SuccessTip.create(json);
    }

    //    复制tab
    @PostMapping("/{pageId}/multiFilter/op/copy")
    public Tip copyMultiFilter(@PathVariable("pageId") Long pageId,
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
            if (moduleData.containsKey("multiFilter")) {
                JSONArray multiFilter = moduleData.getJSONArray("multiFilter");
                JsonArrayUtil.copy(multiFilter, from, to);
            } else {
                throw new BusinessException(BusinessCode.JsonSyntaxError, "未找到multiFilter");
            }
        }

        mockJsonService.saveJsonToFile(json, pageId);
        return SuccessTip.create(json);
    }

    //    移动tab
    @PostMapping("/{pageId}/multiFilter/op/move")
    public Tip moveMultiFilter(@PathVariable("pageId") Long pageId,
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
            if (moduleData.containsKey("multiFilter")) {
                JSONArray multiFilter = moduleData.getJSONArray("multiFilter");
                JsonArrayUtil.move(multiFilter, from, to);
            } else {
                throw new BusinessException(BusinessCode.JsonSyntaxError, "未找到multiFilter");
            }
        }

        mockJsonService.saveJsonToFile(json, pageId);
        return SuccessTip.create(json);
    }

    //    移除tab
    @PostMapping("/{pageId}/multiFilter/op/remove")
    public Tip removeMultiFilter(@PathVariable("pageId") Long pageId,
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
            if (moduleData.containsKey("multiFilter")) {
                JSONArray multiFilter = moduleData.getJSONArray("multiFilter");
                JsonArrayUtil.remove(multiFilter, index);
            } else {
                throw new BusinessException(BusinessCode.JsonSyntaxError, "未找到multiFilter");
            }
        }

        mockJsonService.saveJsonToFile(json, pageId);
        return SuccessTip.create(json);
    }
}
