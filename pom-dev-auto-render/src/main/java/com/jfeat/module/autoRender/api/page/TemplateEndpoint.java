package com.jfeat.module.autoRender.api.page;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jfeat.am.module.ioJson.services.domain.service.MockJsonService;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.module.autoRender.service.gen.persistence.model.PageSimpleInfo;
import com.jfeat.module.frontPage.services.domain.dao.QueryFrontPageDao;
import com.jfeat.module.frontPage.services.domain.model.FrontPageRecord;
import com.jfeat.module.frontPage.services.gen.persistence.dao.FrontPageMapper;
import com.jfeat.module.frontPage.services.gen.persistence.model.FrontPage;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
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

        List<PageSimpleInfo> pageSimpleInfoList = new ArrayList<>();

        for (int i = 0; i < frontPagePage.size(); i++) {
            PageSimpleInfo pageSimpleInfo = new PageSimpleInfo();
            pageSimpleInfo.setPageId(frontPagePage.get(i).getPageId());
            pageSimpleInfo.setTitle(frontPagePage.get(i).getTitle());
            pageSimpleInfo.setAppid(frontPagePage.get(i).getAppid());
            pageSimpleInfoList.add(pageSimpleInfo);
        }

        Page<PageSimpleInfo> result = new Page<>();
        result.setCurrent(pageNum);
        result.setSize(pageSize);
        result.setRecords(pageSimpleInfoList);
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
