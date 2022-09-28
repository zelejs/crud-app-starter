package com.jfeat.module.autoRender.api.component;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jfeat.am.common.annotation.Permission;
import com.jfeat.crud.base.annotation.BusinessLog;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.crud.plus.CRUDObject;
import com.jfeat.crud.plus.DefaultFilterResult;
import com.jfeat.crud.plus.META;
import com.jfeat.module.lc_low_auto_component.api.permission.LowAutoComponentPermission;
import com.jfeat.module.lc_low_auto_component.services.domain.dao.QueryLowAutoComponentDao;
import com.jfeat.module.lc_low_auto_component.services.domain.model.LowAutoComponentRecord;
import com.jfeat.module.lc_low_auto_component.services.domain.service.LowAutoComponentOverModelService;
import com.jfeat.module.lc_low_auto_component.services.gen.crud.model.LowAutoComponentModel;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/dev/auto/component")
public class ComponentStandardEndpoint {


    @Resource
    LowAutoComponentOverModelService lowAutoComponentOverModelService;

    @Resource
    QueryLowAutoComponentDao queryLowAutoComponentDao;


    @BusinessLog(name = "LowAutoComponent", value = "create LowAutoComponent")
    @Permission(LowAutoComponentPermission.LOWAUTOCOMPONENT_NEW)
    @PostMapping
    @ApiOperation(value = "新建 LowAutoComponent", response = LowAutoComponentModel.class)
    public Tip createLowAutoComponent(@RequestBody LowAutoComponentModel entity) {
        Integer affected = 0;
        try {
            DefaultFilterResult filterResult = new DefaultFilterResult();
            affected = lowAutoComponentOverModelService.createMaster(entity, filterResult, null, null);
            if (affected > 0) {
                return SuccessTip.create(filterResult.result());
            }
        } catch (DuplicateKeyException e) {
            throw new BusinessException(BusinessCode.DuplicateKey);
        }

        return SuccessTip.create(affected);
    }

    @BusinessLog(name = "LowAutoComponent", value = "查看 LowAutoComponentModel")
    @Permission(LowAutoComponentPermission.LOWAUTOCOMPONENT_VIEW)
    @GetMapping("/{id}")
    @ApiOperation(value = "查看 LowAutoComponent", response = LowAutoComponentModel.class)
    public Tip getLowAutoComponent(@PathVariable Long id) {
        CRUDObject<LowAutoComponentModel> entity = lowAutoComponentOverModelService
                .registerQueryMasterDao(queryLowAutoComponentDao)
                .retrieveMaster(id, null, null, null);
        if (entity != null) {
            return SuccessTip.create(entity.toJSONObject());
        } else {
            return SuccessTip.create();
        }

    }

    @BusinessLog(name = "LowAutoComponent", value = "update LowAutoComponent")
    @Permission(LowAutoComponentPermission.LOWAUTOCOMPONENT_EDIT)
    @PutMapping("/{id}")
    @ApiOperation(value = "修改 LowAutoComponent", response = LowAutoComponentModel.class)
    public Tip updateLowAutoComponent(@PathVariable Long id, @RequestBody LowAutoComponentModel entity) {
        entity.setId(id);
        int newOptions = META.UPDATE_CASCADING_DELETION_FLAG;  //default to delete not exist items
        return SuccessTip.create(lowAutoComponentOverModelService.updateMaster(entity, null, null, null, newOptions));
    }

    @BusinessLog(name = "LowAutoComponent", value = "delete LowAutoComponent")
    @Permission(LowAutoComponentPermission.LOWAUTOCOMPONENT_DELETE)
    @DeleteMapping("/{id}")
    @ApiOperation("删除 LowAutoComponent")
    public Tip deleteLowAutoComponent(@PathVariable Long id) {
        return SuccessTip.create(lowAutoComponentOverModelService.deleteMaster(id));
    }

    @Permission(LowAutoComponentPermission.LOWAUTOCOMPONENT_VIEW)
    @ApiOperation(value = "LowAutoComponent 列表信息", response = LowAutoComponentRecord.class)
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", dataType = "Integer"),
            @ApiImplicitParam(name = "search", dataType = "String"),
            @ApiImplicitParam(name = "id", dataType = "Long"),
            @ApiImplicitParam(name = "name", dataType = "String"),
            @ApiImplicitParam(name = "componentName", dataType = "String"),
            @ApiImplicitParam(name = "componentType",dataType = "String"),
            @ApiImplicitParam(name = "componentOption", dataType = "String"),
            @ApiImplicitParam(name = "orderBy", dataType = "String"),
            @ApiImplicitParam(name = "sort", dataType = "String")
    })
    public Tip queryLowAutoComponentPage(Page<LowAutoComponentRecord> page,
                                         @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                         @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                         // for tag feature query
                                         @RequestParam(name = "tag", required = false) String tag,
                                         // end tag
                                         @RequestParam(name = "search", required = false) String search,
                                         @RequestParam(name = "name", required = false) String name,
                                         @RequestParam(name = "componentName", required = false) String componentName,
                                         @RequestParam(name = "componentType", required = false) String componentType,
                                         @RequestParam(name = "componentOption",required = false) String componentOption,
                                         @RequestParam(name = "orderBy", required = false) String orderBy,
                                         @RequestParam(name = "sort", required = false) String sort) {

        if (orderBy != null && orderBy.length() > 0) {
            if (sort != null && sort.length() > 0) {
                String sortPattern = "(ASC|DESC|asc|desc)";
                if (!sort.matches(sortPattern)) {
                    throw new BusinessException(BusinessCode.BadRequest.getCode(), "sort must be ASC or DESC");//此处异常类型根据实际情况而定
                }
            } else {
                sort = "ASC";
            }
            orderBy = "`" + orderBy + "`" + " " + sort;
        }
        page.setCurrent(pageNum);
        page.setSize(pageSize);

        LowAutoComponentRecord record = new LowAutoComponentRecord();
        record.setName(name);
        record.setComponentName(componentName);
        record.setComponentType(componentType);
        record.setComponentOption(componentOption);


        List<LowAutoComponentRecord> lowAutoComponentPage = queryLowAutoComponentDao.findLowAutoComponentPage(page, record, tag, search, orderBy, null, null);


        page.setRecords(lowAutoComponentPage);

        return SuccessTip.create(page);
    }



}
