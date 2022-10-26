package com.jfeat.module.autoRender.api.page;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jfeat.am.module.ioJson.services.domain.service.MockJsonService;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.module.autoRender.service.gen.persistence.model.AutoPage;
import com.jfeat.module.autoRender.service.gen.persistence.model.AutoPageSimpleInfo;
import com.jfeat.module.frontPage.services.domain.dao.QueryFrontPageDao;
import com.jfeat.module.frontPage.services.domain.model.FrontPageRecord;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Api("PreviewPage")
@RequestMapping("/dev/auto/preview")
public class PreviewPageEndpoint {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    private final Log logger = LogFactory.getLog(getClass());

    private final String currentPageFromId = "currentPageFromId";

    private final String pageJsonKey = "page:";

    @Resource
    QueryFrontPageDao queryFrontPageDao;

    @Resource
    MockJsonService mockJsonService;

    //    设置当前预览页ID
    @PutMapping("/current/{id}")
    @ApiOperation(value = "设置当前预览页ID")
    public Tip updateCurrentPageId(@PathVariable("id") Long id) {
        stringRedisTemplate.opsForValue().set(currentPageFromId, String.valueOf(id), 24, TimeUnit.HOURS);
        return SuccessTip.create(1);
    }

    //    获取当前预览页ID
    @GetMapping("/current")
    @ApiOperation(value = "获取当前预览页ID")
    public Tip getCurrentPageId() {
        if (stringRedisTemplate.hasKey(currentPageFromId) && stringRedisTemplate.opsForValue().getOperations().getExpire(currentPageFromId) > 0) {
            logger.info("返回缓存数据");
            String value = (String) stringRedisTemplate.opsForValue().get(currentPageFromId);
            return SuccessTip.create(value);
        } else {
            throw new BusinessException(BusinessCode.BadRequest, "没有设置当前页面id");
        }
    }

    @GetMapping("/form")
    @ApiOperation(value = "获取pageidJson")
    public Tip getJson(@RequestParam(value = "pageId") Long id) {
        return SuccessTip.create(mockJsonService.readJsonFile(id));
    }

    @PostMapping("/form/{id}")
    @ApiOperation(value = "提交pageid 的json配置")
    public Tip addJson(@PathVariable Long id, @RequestBody JSONObject json, @RequestParam(value = "appid", required = false) String appid) {
        String originAppid = mockJsonService.getAppId();
        if (appid != null && !appid.equals("")) {
            mockJsonService.setAppId(appid);
        }
        Integer integer = mockJsonService.saveJsonToFile(json, id);
        mockJsonService.setAppId(originAppid);
        return SuccessTip.create(integer);
    }

    @GetMapping("/appList")
    @ApiOperation(value = "页面配置列表")
    public Tip getAppIdMap() {
        Map<String, String> idMap = mockJsonService.getAppIdMap();
        List<String> appList = new ArrayList<>();
        for (String key : idMap.keySet()) {
            appList.add(key);
        }
        return SuccessTip.create(appList);
    }

    //    获取所有form页面列表
    @GetMapping("/forms")
    @ApiOperation(value = "获取所有form页面列表")
    public Tip getAllPages(Page<FrontPageRecord> page,
                           @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                           @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                           // for tag feature query
                           @RequestParam(name = "tag", required = false) String tag,
                           // end tag
                           @RequestParam(name = "search", required = false) String search,

                           @RequestParam(name = "pageId", required = false) String pageId,

                           @RequestParam(name = "title", required = false) String title,

                           @RequestParam(name = "pageDescrip", required = false) String pageDescrip,

                           @RequestParam(name = "content", required = false) String content,

                           @RequestParam(name = "appid", required = false) String appid,

                           @RequestParam(name = "jsonName", required = false) String jsonName,

                           @RequestParam(name = "jsonPath", required = false) String jsonPath,

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

        FrontPageRecord record = new FrontPageRecord();
        record.setPageId(pageId);
        record.setTitle(title);
        record.setPageDescrip(pageDescrip);
        record.setContent(content);
        record.setAppid(appid);
        record.setJsonName(jsonName);
        record.setJsonPath(jsonPath);
        record.setCreateTime(createTime);
        record.setUpdateTime(updateTime);
        List<FrontPageRecord> frontPagePage = queryFrontPageDao.findFrontPagePage(page, record, tag, search, orderBy, null, null);

        page.setRecords(frontPagePage);

        return SuccessTip.create(page);
    }

    //    获取所有form页面列表
    @GetMapping("/forms/simple")
    @ApiOperation(value = "获取所有form页面列表")
    public Tip getAllSimplePages(Page<FrontPageRecord> page,
                                 @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                 @RequestParam(name = "pageSize", required = false, defaultValue = "50") Integer pageSize,
                                 // for tag feature query
                                 @RequestParam(name = "count", required = false) String pageId,
                                 @RequestParam(name = "appid", required = false) String appid,
                                 @RequestParam(name = "title", required = false) String title) {


        page.setCurrent(pageNum);
        page.setSize(pageSize);

        FrontPageRecord record = new FrontPageRecord();
        record.setPageId(pageId);
        record.setTitle(title);
        record.setAppid(appid);

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


    //    获取当前预览页配置
    @GetMapping("/current/form")
    @ApiOperation(value = "获取当前预览页配置")
    public Tip getCurrentPageJson() {
        if (stringRedisTemplate.hasKey(currentPageFromId) && stringRedisTemplate.opsForValue().getOperations().getExpire(currentPageFromId) > 0) {
            logger.info("返回缓存数据");
            String value = (String) stringRedisTemplate.opsForValue().get(currentPageFromId);
            Long fromId = Long.valueOf(value);

            if (stringRedisTemplate.hasKey(pageJsonKey + value) && stringRedisTemplate.opsForValue().getOperations().getExpire(pageJsonKey + value) > 0) {
                String data = stringRedisTemplate.opsForValue().get(pageJsonKey + value);
                JSONObject json = JSON.parseObject(data);
                return SuccessTip.create(json);
            }
            return SuccessTip.create(mockJsonService.readJsonFile(fromId));
        } else {
            throw new BusinessException(BusinessCode.BadRequest, "没有设置当前页面id");
        }
    }

    //    设置当前预览页配置
    @PutMapping("/current/from")
    @ApiOperation(value = "设置当前预览页配置")
    public Tip updateCurrentPageJson(@RequestBody JSONObject json,@RequestParam(value = "id",required = false) Long id) {
        System.out.println("===============");
        if (stringRedisTemplate.hasKey(currentPageFromId) && stringRedisTemplate.opsForValue().getOperations().getExpire(currentPageFromId) > 0) {
            logger.info("返回缓存数据");
            String value = (String) stringRedisTemplate.opsForValue().get(currentPageFromId);
            Long fromId = Long.valueOf(value);

            stringRedisTemplate.opsForValue().set(pageJsonKey + value, json.toJSONString(), 24, TimeUnit.HOURS);

            if (id!=null){
                fromId = id;
            }
            Integer integer = mockJsonService.saveJsonToFile(json, fromId);
            return SuccessTip.create(integer);
        } else {
            throw new BusinessException(BusinessCode.BadRequest, "没有设置当前页面id");
        }
    }

    @PostMapping("/setAppId/{id}")
    @ApiOperation(value = "设置 appId")
    public Tip setAppId(@PathVariable(name = "id") String id) {
        mockJsonService.setAppId(id);
        return SuccessTip.create(mockJsonService.getAppId());
    }

    @GetMapping("/getAppId")
    @ApiOperation(value = "查看 当前appId")
    public Tip getAppId() {
        return SuccessTip.create(mockJsonService.getAppId());
    }


}
