
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
import com.jfeat.dev.devops.services.domain.dao.QueryDevVersionDao;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.request.Ids;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.crud.base.annotation.BusinessLog;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.plus.CRUDObject;
import com.jfeat.crud.plus.DefaultFilterResult;
import com.jfeat.dev.devops.api.permission.*;
import com.jfeat.am.common.annotation.Permission;

import java.math.BigDecimal;

import com.jfeat.dev.devops.services.domain.service.*;
import com.jfeat.dev.devops.services.domain.model.DevVersionRecord;
import com.jfeat.dev.devops.services.gen.persistence.model.DevVersion;

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
@Api("DevVersion")
@RequestMapping("/api/crud/devops/devVersion/devVersions")
public class DevVersionEndpoint {

    @Resource
    DevVersionService devVersionService;

    @Resource
    QueryDevVersionDao queryDevVersionDao;


    @BusinessLog(name = "DevVersion", value = "create DevVersion")
    @Permission(DevVersionPermission.DEVVERSION_NEW)
    @PostMapping
    @ApiOperation(value = "新建 DevVersion", response = DevVersion.class)
    public Tip createDevVersion(@RequestBody DevVersion entity) {
        Integer affected = 0;
        try {
            affected = devVersionService.createMaster(entity);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(BusinessCode.DuplicateKey);
        }

        return SuccessTip.create(affected);
    }

    @Permission(DevVersionPermission.DEVVERSION_VIEW)
    @GetMapping("/{id}")
    @ApiOperation(value = "查看 DevVersion", response = DevVersion.class)
    public Tip getDevVersion(@PathVariable Long id) {
        return SuccessTip.create(devVersionService.queryMasterModel(queryDevVersionDao, id));
    }

    @BusinessLog(name = "DevVersion", value = "update DevVersion")
    @Permission(DevVersionPermission.DEVVERSION_EDIT)
    @PutMapping("/{id}")
    @ApiOperation(value = "修改 DevVersion", response = DevVersion.class)
    public Tip updateDevVersion(@PathVariable Long id, @RequestBody DevVersion entity) {
        entity.setId(id);
        return SuccessTip.create(devVersionService.updateMaster(entity));
    }

    @BusinessLog(name = "DevVersion", value = "delete DevVersion")
    @Permission(DevVersionPermission.DEVVERSION_DELETE)
    @DeleteMapping("/{id}")
    @ApiOperation("删除 DevVersion")
    public Tip deleteDevVersion(@PathVariable Long id) {
        return SuccessTip.create(devVersionService.deleteMaster(id));
    }

    @Permission(DevVersionPermission.DEVVERSION_VIEW)
    @ApiOperation(value = "DevVersion 列表信息", response = DevVersionRecord.class)
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", dataType = "Integer"),
            @ApiImplicitParam(name = "search", dataType = "String"),
            @ApiImplicitParam(name = "id", dataType = "Long"),
            @ApiImplicitParam(name = "sqlVersion", dataType = "String"),
            @ApiImplicitParam(name = "appid", dataType = "String"),
            @ApiImplicitParam(name = "note", dataType = "String"),
            @ApiImplicitParam(name = "createTime", dataType = "Date"),
            @ApiImplicitParam(name = "updateTime", dataType = "Date"),
            @ApiImplicitParam(name = "orderBy", dataType = "String"),
            @ApiImplicitParam(name = "sort", dataType = "String")
    })
    public Tip queryDevVersionPage(Page<DevVersionRecord> page,
                                   @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                   @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                   // for tag feature query
                                   @RequestParam(name = "tag", required = false) String tag,
                                   // end tag
                                   @RequestParam(name = "search", required = false) String search,

                                   @RequestParam(name = "sqlVersion", required = false) String sqlVersion,

                                   @RequestParam(name = "appid", required = false) String appid,

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

        DevVersionRecord record = new DevVersionRecord();
        record.setSqlVersion(sqlVersion);
        record.setAppid(appid);
        record.setNote(note);
        record.setCreateTime(createTime);
        record.setUpdateTime(updateTime);


        List<DevVersionRecord> devVersionPage = queryDevVersionDao.findDevVersionPage(page, record, tag, search, orderBy, null, null);


        page.setRecords(devVersionPage);

        return SuccessTip.create(page);
    }
}

