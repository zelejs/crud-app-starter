package com.jfeat.module.autoRender.api.autoList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jfeat.am.module.ioJson.services.domain.service.MockJsonService;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.module.autoRender.service.domain.service.AutoPageService;
import com.jfeat.module.autoRender.service.domain.service.ModuleDataService;
import com.jfeat.module.autoRender.service.domain.service.ModuleService;
import com.jfeat.module.autoRender.service.domain.service.TableItemModuleService;
import com.jfeat.module.autoRender.service.domain.service.imp.TableItemModuleServiceImp;
import com.jfeat.module.autoRender.service.gen.persistence.model.AutoTableItemColumns;
import com.jfeat.module.autoRender.util.ParameterUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.swing.table.TableColumn;
import java.util.List;

@RestController
@RequestMapping("/dev/auto/forms")
public class TableItemModuleEndpoint {

    @Resource
    ModuleService moduleService;

    @Resource
    ModuleDataService moduleDataService;

    @Resource
    AutoPageService autoPageService;

    @Resource
    TableItemModuleService tableItemModuleService;

    @Resource
    MockJsonService mockJsonService;

    //    查看全部的columns
    @GetMapping("/{id}/columns")
    public Tip getAllColumns(
            @PathVariable("id") Long id,
            @RequestParam(value = "currentModule", defaultValue = "0", required = false) Integer currentModule) {
        JSONObject json = autoPageService.getPageConfigJsonByPageId(id);
        if (json == null) {
            throw new BusinessException(BusinessCode.BadRequest, "没有找到该json配置文件");
        }
        return SuccessTip.create(tableItemModuleService.getALlColumns(json, currentModule));
    }

    //    查看指定columns
    @GetMapping("/{id}/columns/{index}")
    public Tip getColumnsById(@PathVariable("id") Long id,
                              @PathVariable("index") Integer index,
                              @RequestParam(value = "currentModule", defaultValue = "0", required = false) Integer currentModule) {
        JSONObject json = autoPageService.getPageConfigJsonByPageId(id);
        if (json == null) {
            throw new BusinessException(BusinessCode.BadRequest, "没有找到该json配置文件");
        }
        JSONArray jsonArray = tableItemModuleService.getALlColumns(json, currentModule);
        if (jsonArray == null || json.size() <= index) {
            throw new BusinessException(BusinessCode.OutOfRange, "超出指定范围");
        }
        return SuccessTip.create(jsonArray.get(index));
    }

    //    添加新的columns
    @PostMapping("/{id}/columns")
    public Tip addColumns(
            @PathVariable("id") Long id,
            @RequestBody AutoTableItemColumns columns) {
        JSONObject json = autoPageService.getPageConfigJsonByPageId(id);
        if (json == null) {
            throw new BusinessException(BusinessCode.BadRequest, "没有找到该json配置文件");
        }
        Integer currentModule = 0;
        if (columns.getCurrentModule() != null && columns.getCurrentModule() > 0) {
            currentModule = columns.getCurrentModule();
            columns.setCurrentModule(null);
        }
        JSONArray jsonArray = tableItemModuleService.getALlColumns(json, currentModule);


//        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(columns);
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(columns, SerializerFeature.DisableCircularReferenceDetect));
        jsonArray.add(jsonObject);
        mockJsonService.saveJsonToFile(json, id);
        return SuccessTip.create(json);
    }

    //    修改指定位置的colums
    @PutMapping("/{id}/columns/{index}")
    public Tip updateColumns(@PathVariable("id") Long id,
                             @PathVariable("index") int index,
                             @RequestBody AutoTableItemColumns columns) {

        JSONObject json = autoPageService.getPageConfigJsonByPageId(id);
        if (json == null) {
            throw new BusinessException(BusinessCode.BadRequest, "没有找到该json配置文件");
        }
        Integer currentModule = 0;
        if (columns.getCurrentModule() != null && columns.getCurrentModule() > 0) {
            currentModule = columns.getCurrentModule();
            columns.setCurrentModule(null);
        }
        JSONArray jsonArray = tableItemModuleService.getALlColumns(json, currentModule);
        List<AutoTableItemColumns> columnsList = JSONObject.parseArray(jsonArray.toJSONString(), AutoTableItemColumns.class);

        if (jsonArray.size() <= index) {
            throw new BusinessException(BusinessCode.OutOfRange, "超出指定范围");
        }

        columnsList.set(index, ParameterUtil.parameterReplace(columnsList.get(index), columns));

        json = tableItemModuleService.setColumns(json, JSONArray.parseArray(JSON.toJSONString(columnsList)), currentModule);
        mockJsonService.saveJsonToFile(json, id);
        return SuccessTip.create(json);
    }


    //    移动colums
    @PostMapping("/{id}/columns/op/arrange")
    public Tip moveColumn(@PathVariable("id") Long id,
                          @RequestBody AutoTableItemColumns columns) {
        JSONObject json = autoPageService.getPageConfigJsonByPageId(id);
        if (json == null) {
            throw new BusinessException(BusinessCode.BadRequest, "没有找到该json配置文件");
        }
        Integer currentModule = 0;
        if (columns.getCurrentModule() != null && columns.getCurrentModule() > 0) {
            currentModule = columns.getCurrentModule();
            columns.setCurrentModule(null);
        }
        JSONArray jsonArray = tableItemModuleService.getALlColumns(json, currentModule);
        if (jsonArray == null || jsonArray.size() <= 0) {
            throw new BusinessException(BusinessCode.CodeBase, "Columns为空");
        }
        if (columns.getFrom() == null || columns.getFrom() < 0 || columns.getFrom() >= jsonArray.size()) {
            throw new BusinessException(BusinessCode.OutOfRange, "from 超出范围");
        }
        if (columns.getTo() == null || columns.getTo() < 0 || columns.getTo() >= jsonArray.size()) {
            throw new BusinessException(BusinessCode.OutOfRange, "to 超出范围");
        }
        JSONObject fromJson = JSON.parseObject(JSONObject.toJSONString(jsonArray.getJSONObject(columns.getFrom().intValue())));

        jsonArray.remove(columns.getFrom().intValue());
        jsonArray.add(columns.getTo().intValue(), fromJson);

        mockJsonService.saveJsonToFile(json, id);
        return SuccessTip.create(json);

    }


    //    移除colums
    @PostMapping("/{id}/columns/op/remove")
    public Tip removeColumn(@PathVariable("id") Long id,
                          @RequestBody AutoTableItemColumns columns) {
        JSONObject json = autoPageService.getPageConfigJsonByPageId(id);
        if (json == null) {
            throw new BusinessException(BusinessCode.BadRequest, "没有找到该json配置文件");
        }
        Integer currentModule = 0;
        if (columns.getCurrentModule() != null && columns.getCurrentModule() > 0) {
            currentModule = columns.getCurrentModule();
            columns.setCurrentModule(null);
        }
        JSONArray jsonArray = tableItemModuleService.getALlColumns(json, currentModule);
        if (jsonArray == null || jsonArray.size() <= 0) {
            throw new BusinessException(BusinessCode.CodeBase, "Columns为空");
        }
        if (columns.getIndex() == null || columns.getIndex() < 0 || columns.getIndex() >= jsonArray.size()) {
            throw new BusinessException(BusinessCode.OutOfRange, "index 超出范围");
        }

        jsonArray.remove(columns.getIndex().intValue());


        mockJsonService.saveJsonToFile(json, id);
        return SuccessTip.create(json);

    }


//    复制colums

    @PostMapping("/{id}/columns/op/copy")
    public Tip copyColumn(@PathVariable("id") Long id,
                          @RequestBody AutoTableItemColumns columns) {
        JSONObject json = autoPageService.getPageConfigJsonByPageId(id);
        if (json == null) {
            throw new BusinessException(BusinessCode.BadRequest, "没有找到该json配置文件");
        }
        Integer currentModule = 0;
        if (columns.getCurrentModule() != null && columns.getCurrentModule() > 0) {
            currentModule = columns.getCurrentModule();
            columns.setCurrentModule(null);
        }
        JSONArray jsonArray = tableItemModuleService.getALlColumns(json, currentModule);
        if (jsonArray == null || jsonArray.size() <= 0) {
            throw new BusinessException(BusinessCode.CodeBase, "Columns为空");
        }
        if (columns.getIndex() == null || columns.getIndex() < 0 || columns.getIndex() >= jsonArray.size()) {
            throw new BusinessException(BusinessCode.OutOfRange, "index 超出范围");
        }

        JSONObject indexJson = JSON.parseObject(JSONObject.toJSONString(jsonArray.getJSONObject(columns.getIndex().intValue())));


        jsonArray.add(jsonArray.size(), indexJson);

        mockJsonService.saveJsonToFile(json, id);
        return SuccessTip.create(json);

    }
}
