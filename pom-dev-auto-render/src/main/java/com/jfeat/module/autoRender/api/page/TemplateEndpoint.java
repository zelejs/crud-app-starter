package com.jfeat.module.autoRender.api.page;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jfeat.am.module.ioJson.services.domain.service.MockJsonService;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.module.autoRender.service.gen.persistence.model.AutoPageSimpleInfo;
import com.jfeat.module.frontPage.services.domain.dao.QueryFrontPageDao;
import com.jfeat.module.frontPage.services.domain.model.FrontPageRecord;
import com.jfeat.module.frontPage.services.gen.persistence.dao.FrontPageMapper;
import com.jfeat.module.frontPage.services.gen.persistence.model.FrontPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api("Template")
@RequestMapping("/dev/auto/template")
public class TemplateEndpoint {

    @Resource
    QueryFrontPageDao queryFrontPageDao;

    @Resource
    MockJsonService mockJsonService;

    @Resource
    FrontPageMapper frontPageMapper;

    //    获取模板列表
    @GetMapping
    @ApiOperation(value = "获取模板列表")
    public Tip getTemplate(Page<FrontPageRecord> page,
                           @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                           @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                           @RequestParam(name = "search", required = false) String search,
                           @RequestParam(name = "pageId", required = false) String pageId,
                           @RequestParam(name = "title", required = false) String title,
                           @RequestParam(name = "appid", required = false) String appid) {
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        FrontPageRecord record = new FrontPageRecord();
        record.setPageId(pageId);
        record.setTitle(title);
        record.setAppid(appid);
        record.setTemplateStatus(FrontPage.TEMPLATE_STATUS_YES);
        List<FrontPageRecord> frontPagePage = queryFrontPageDao.findFrontPagePage(page, record, null, search, null, null, null);
        page.setRecords(frontPagePage);
        return SuccessTip.create(page);
    }

    //    获取所有form页面列表
    @GetMapping("/simple")
    @ApiOperation(value = "获取所有form页面列表")
    public Tip getAllSimplePages(Page<FrontPageRecord> page,
                                 @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                 @RequestParam(name = "pageSize", required = false, defaultValue = "50") Integer pageSize,
                                 // for tag feature query
                                 @RequestParam(name = "pageId", required = false) String pageId,
                                 @RequestParam(name = "appid", required = false) String appid,
                                 @RequestParam(name = "title", required = false) String title) {


        page.setCurrent(pageNum);
        page.setSize(pageSize);

        FrontPageRecord record = new FrontPageRecord();
        record.setPageId(pageId);
        record.setTitle(title);
        record.setAppid(appid);
        record.setTemplateStatus(FrontPage.TEMPLATE_STATUS_YES);

        List<FrontPageRecord> frontPagePage = queryFrontPageDao.findFrontPagePage(page, record, null, null, null, null, null);
        page.setRecords(frontPagePage);

        List<AutoPageSimpleInfo> autoPageSimpleInfoList = new ArrayList<>();

        for (int i = 0; i < frontPagePage.size(); i++) {
            AutoPageSimpleInfo autoPageSimpleInfo = new AutoPageSimpleInfo();
            autoPageSimpleInfo.setPageId(frontPagePage.get(i).getPageId());
            autoPageSimpleInfo.setTitle(frontPagePage.get(i).getTitle());
            autoPageSimpleInfo.setAppid(frontPagePage.get(i).getAppid());
            autoPageSimpleInfoList.add(autoPageSimpleInfo);
        }

        Page<AutoPageSimpleInfo> result = new Page<>();
        result.setCurrent(pageNum);
        result.setSize(pageSize);
        result.setRecords(autoPageSimpleInfoList);
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());

        return SuccessTip.create(result);
    }

    @GetMapping("/form")
    public Tip getJson(@RequestParam(value = "pageId") Long id) {
        return SuccessTip.create(mockJsonService.readJsonFile(id));
    }

//    设置模板
    @PutMapping("/confirmTemplate/{id}")
    @ApiOperation(value = "设置模板")
    public Tip setTemplateStatus(@PathVariable("id") String id){
        QueryWrapper<FrontPage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(FrontPage.TEMPLATE_STATUS,id);
        FrontPage frontPage =  frontPageMapper.selectOne(queryWrapper);
        if (frontPage!=null){
            frontPage.setTemplateStatus(FrontPage.TEMPLATE_STATUS_YES);
            return SuccessTip.create(frontPageMapper.updateById(frontPage));
        }
        return SuccessTip.create();
    }

//    取消模板
    @PutMapping("/cancelTemplate/{id}")
    @ApiOperation(value = "修改模板")
    public Tip cancelTemplateStatus(@PathVariable("id") String id){
        QueryWrapper<FrontPage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(FrontPage.TEMPLATE_STATUS,id);
        FrontPage frontPage =  frontPageMapper.selectOne(queryWrapper);
        if (frontPage!=null){
            frontPage.setTemplateStatus(FrontPage.TEMPLATE_STATUS_NO);
            return SuccessTip.create(frontPageMapper.updateById(frontPage));
        }
        return SuccessTip.create();
    }
}
