package com.jfeat.am.module.navigation.api;


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
import com.jfeat.am.module.navigation.services.domain.dao.QueryNavigationDao;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.request.Ids;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.am.module.log.annotation.BusinessLog;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.plus.CRUDObject;
import com.jfeat.am.module.navigation.api.permission.*;
import com.jfeat.am.common.annotation.Permission;

import java.math.BigDecimal;

import com.jfeat.am.module.navigation.services.domain.service.*;
import com.jfeat.am.module.navigation.services.domain.model.NavigationRecord;
import com.jfeat.am.module.navigation.services.gen.persistence.model.Navigation;

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
 * @since 2022-04-18
 */
@RestController

@Api("Navigation")
@RequestMapping("/api/c/navigation/navigations")
public class NavigationEndpoint {

    @Resource
    NavigationService navigationService;


    @Resource
    QueryNavigationDao queryNavigationDao;

    @BusinessLog(name = "Navigation", value = "create Navigation")
    @PostMapping
    @ApiOperation(value = "新建 Navigation", response = Navigation.class)
    public Tip createNavigation(@RequestBody Navigation entity) {

        Integer affected = 0;
        try {
            affected = navigationService.createMaster(entity);

        } catch (DuplicateKeyException e) {
            throw new BusinessException(BusinessCode.DuplicateKey);
        }

        return SuccessTip.create(affected);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查看 Navigation", response = Navigation.class)
    public Tip getNavigation(@PathVariable Long id) {
        return SuccessTip.create(navigationService.retrieveMaster(id));
    }

    @BusinessLog(name = "Navigation", value = "update Navigation")
    @PutMapping("/{id}")
    @ApiOperation(value = "修改 Navigation", response = Navigation.class)
    public Tip updateNavigation(@PathVariable Long id, @RequestBody Navigation entity) {
        entity.setId(id);
        return SuccessTip.create(navigationService.updateMaster(entity));
    }

    @BusinessLog(name = "Navigation", value = "delete Navigation")
    @DeleteMapping("/{id}")
    @ApiOperation("删除 Navigation")
    public Tip deleteNavigation(@PathVariable Long id) {
        return SuccessTip.create(queryNavigationDao.deleteById(id));
    }

    @ApiOperation(value = "Navigation 列表信息", response = NavigationRecord.class)
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", dataType = "Integer"),
            @ApiImplicitParam(name = "search", dataType = "String"),
            @ApiImplicitParam(name = "id", dataType = "Long"),
            @ApiImplicitParam(name = "name", dataType = "String"),
            @ApiImplicitParam(name = "url", dataType = "String"),
            @ApiImplicitParam(name = "title", dataType = "String"),
            @ApiImplicitParam(name = "desc", dataType = "String"),
            @ApiImplicitParam(name = "orderBy", dataType = "String"),
            @ApiImplicitParam(name = "sort", dataType = "String")
    })
    public Tip queryNavigations(Page<NavigationRecord> page,
                                @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                @RequestParam(name = "search", required = false) String search,
                                @RequestParam(name = "id", required = false) Long id,

                                @RequestParam(name = "name", required = false) String name,

                                @RequestParam(name = "url", required = false) String url,

                                @RequestParam(name = "title", required = false) String title,

                                @RequestParam(name = "desc", required = false) String desc,
                                @RequestParam(name = "orderBy", required = false) String orderBy,
                                @RequestParam(name = "sort", required = false) String sort) {

        if (orderBy != null && orderBy.length() > 0) {
            if (sort != null && sort.length() > 0) {
                String pattern = "(ASC|DESC|asc|desc)";
                if (!sort.matches(pattern)) {
                    throw new BusinessException(BusinessCode.BadRequest.getCode(), "sort must be ASC or DESC");//此处异常类型根据实际情况而定
                }
            } else {
                sort = "ASC";
            }
            orderBy = "`" + orderBy + "`" + " " + sort;
        }
        page.setCurrent(pageNum);
        page.setSize(pageSize);

        NavigationRecord record = new NavigationRecord();
        record.setId(id);
        record.setName(name);
        record.setUrl(url);
        record.setTitle(title);
        record.setDesc(desc);


        List<NavigationRecord> navigationPage = queryNavigationDao.findNavigationPage(page, record, search, orderBy, null, null);

        page.setRecords(navigationPage);

        return SuccessTip.create(page);
    }
}
