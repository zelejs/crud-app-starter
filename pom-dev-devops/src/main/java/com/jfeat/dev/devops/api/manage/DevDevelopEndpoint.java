
package com.jfeat.dev.devops.api.manage;


import com.jfeat.crud.plus.META;
import com.jfeat.am.core.jwt.JWTKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.dao.DuplicateKeyException;
import com.jfeat.dev.devops.services.domain.dao.QueryDevDevelopDao;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.request.Ids;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.crud.base.annotation.BusinessLog;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.plus.CRUDObject;
import com.jfeat.crud.plus.DefaultFilterResult;
import com.jfeat.am.common.annotation.Permission;

import java.math.BigDecimal;

import com.jfeat.dev.devops.services.domain.service.*;
import com.jfeat.dev.devops.services.domain.model.DevDevelopRecord;
import com.jfeat.dev.devops.services.gen.persistence.model.DevDevelop;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;

/**
 * <p>
 * api
 * </p>
 *
 * @author Code generator
 * @since 2022-09-16
 */
@RestController
@Api("DevDevelop")
@RequestMapping("/api/crud/devops/devDevelop/devDevelops")
public class DevDevelopEndpoint {

    @Resource
    DevDevelopService devDevelopService;

    @Resource
    QueryDevDevelopDao queryDevDevelopDao;


    @BusinessLog(name = "DevDevelop", value = "create DevDevelop")
    @PostMapping
    @ApiOperation(value = "新建 DevDevelop", response = DevDevelop.class)
    public Tip createDevDevelop(@RequestBody DevDevelop entity) {
        Integer affected = 0;
        try {
            affected = devDevelopService.createMaster(entity);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(BusinessCode.DuplicateKey);
        }

        return SuccessTip.create(affected);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查看 DevDevelop", response = DevDevelop.class)
    public Tip getDevDevelop(@PathVariable Long id) {
        return SuccessTip.create(devDevelopService.queryMasterModel(queryDevDevelopDao, id));
    }

    @BusinessLog(name = "DevDevelop", value = "update DevDevelop")
    @PutMapping("/{id}")
    @ApiOperation(value = "修改 DevDevelop", response = DevDevelop.class)
    public Tip updateDevDevelop(@PathVariable Long id, @RequestBody DevDevelop entity) {
        entity.setId(id);
        return SuccessTip.create(devDevelopService.updateMaster(entity));
    }

    @BusinessLog(name = "DevDevelop", value = "delete DevDevelop")
    @DeleteMapping("/{id}")
    @ApiOperation("删除 DevDevelop")
    public Tip deleteDevDevelop(@PathVariable Long id) {
        return SuccessTip.create(devDevelopService.deleteMaster(id));
    }

    @ApiOperation(value = "DevDevelop 列表信息", response = DevDevelopRecord.class)
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", dataType = "Integer"),
            @ApiImplicitParam(name = "search", dataType = "String"),
            @ApiImplicitParam(name = "id", dataType = "Long"),
            @ApiImplicitParam(name = "title", dataType = "String"),
            @ApiImplicitParam(name = "description", dataType = "String"),
            @ApiImplicitParam(name = "queryFileName", dataType = "String"),
            @ApiImplicitParam(name = "executionFileName", dataType = "String"),
            @ApiImplicitParam(name = "paramStatus", dataType = "Integer"),
            @ApiImplicitParam(name = "sqlVersion", dataType = "String"),
            @ApiImplicitParam(name = "note", dataType = "String"),
            @ApiImplicitParam(name = "createTime", dataType = "Date"),
            @ApiImplicitParam(name = "updateTime", dataType = "Date"),
            @ApiImplicitParam(name = "orderBy", dataType = "String"),
            @ApiImplicitParam(name = "sort", dataType = "String")
    })
    public Tip queryDevDevelopPage(Page<DevDevelopRecord> page,
                                   @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                   @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                   // for tag feature query
                                   @RequestParam(name = "tag", required = false) String tag,
                                   // end tag
                                   @RequestParam(name = "search", required = false) String search,

                                   @RequestParam(name = "title", required = false) String title,

                                   @RequestParam(name = "description", required = false) String description,

                                   @RequestParam(name = "queryFileName", required = false) String queryFileName,

                                   @RequestParam(name = "executionFileName", required = false) String executionFileName,

                                   @RequestParam(name = "paramStatus", required = false) Integer paramStatus,

                                   @RequestParam(name = "sqlVersion", required = false) String sqlVersion,

                                   @RequestParam(name = "note", required = false) String note,

                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                   @RequestParam(name = "createTime", required = false) Date createTime,

                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                   @RequestParam(name = "updateTime", required = false) Date updateTime,
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

        DevDevelopRecord record = new DevDevelopRecord();
        record.setTitle(title);
        record.setDescription(description);
        record.setQueryFileName(queryFileName);
        record.setExecutionFileName(executionFileName);
        record.setParamStatus(paramStatus);
        record.setSqlVersion(sqlVersion);
        record.setNote(note);
        record.setCreateTime(createTime);
        record.setUpdateTime(updateTime);


        List<DevDevelopRecord> devDevelopPage = queryDevDevelopDao.findDevDevelopPage(page, record, tag, search, orderBy, null, null);


        page.setRecords(devDevelopPage);

        return SuccessTip.create(page);
    }
}

