package com.jfeat.module.autoRender.api.page;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jfeat.am.module.ioJson.services.domain.service.MockJsonService;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.module.autoRender.service.domain.service.AutoPageService;
import com.jfeat.module.autoRender.service.domain.service.ModuleService;
import com.jfeat.module.autoRender.service.gen.persistence.model.AutoPage;
import com.jfeat.module.frontPage.services.gen.persistence.dao.FrontPageMapper;
import com.jfeat.module.frontPage.services.gen.persistence.model.FrontPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api("PagePropOperation")
@RequestMapping("/dev/auto/forms")
public class PagePropOperationEndpoint {

    @Resource
    AutoPageService autoPageService;

    @Resource
    ModuleService moduleService;

    @Resource
    MockJsonService mockJsonService;


    @Resource
    FrontPageMapper frontPageMapper;


    @PutMapping("/{id}/page")
    @ApiOperation(value = "修改页面属性id",response = AutoPage.class)
    public Tip updatePageProp(@PathVariable("id") Long id,
                              @RequestBody JSONObject prop){
        JSONObject json  = autoPageService.getPageConfigJsonByPageId(id);
        autoPageService.updatePageProp(json,prop);
        mockJsonService.saveJsonToFile(json,id);
        return SuccessTip.create(json);
    }


    @PostMapping("/{id}/page/{category}")
    @ApiOperation(value = "创建页面")
    public Tip createPage(@PathVariable("id") Long id,
                              @PathVariable("category") String category){
        QueryWrapper<FrontPage> frontPageQueryWrapper = new QueryWrapper<>();
        frontPageQueryWrapper.eq(FrontPage.PAGE_ID,id);
        FrontPage frontPage = frontPageMapper.selectOne(frontPageQueryWrapper);
        if (frontPage!=null){
            throw new BusinessException(BusinessCode.CodeBase,"该页面id已存在,请重填");
        }

        QueryWrapper<FrontPage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(FrontPage.TEMPLATE_STATUS,FrontPage.TEMPLATE_STATUS_YES).eq(FrontPage.TITLE,category).eq(FrontPage.APPID,"module");
        FrontPage template = frontPageMapper.selectOne(queryWrapper);

        if (template==null){
            throw new BusinessException(BusinessCode.CodeBase,"没有该模板");
        }

        if (template.getContent()!=null){
            JSONObject jsonObject = JSON.parseObject(template.getContent());
            mockJsonService.saveJsonToFile(jsonObject,id);
            return SuccessTip.create(jsonObject);
        }
        return null;

    }


}
