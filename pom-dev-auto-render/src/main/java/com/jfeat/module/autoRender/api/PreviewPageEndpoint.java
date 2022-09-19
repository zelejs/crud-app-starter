package com.jfeat.module.autoRender.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jfeat.am.module.ioJson.services.domain.service.MockJsonService;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.module.frontPage.services.domain.dao.QueryFrontPageDao;
import com.jfeat.module.frontPage.services.domain.model.FrontPageRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/dev/auto/preview")
public class PreviewPageEndpoint {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    private final Log logger = LogFactory.getLog(getClass());

    private final String currentPageFromId = "currentPageFromId";

    private final String pageJsonKey="page:";

    @Resource
    QueryFrontPageDao queryFrontPageDao;

    @Resource
    MockJsonService mockJsonService;

    //    设置当前预览页ID
    @PutMapping("/current/{id}")
    public Tip updateCurrentPageId(@PathVariable("id") Long id) {
        stringRedisTemplate.opsForValue().set(currentPageFromId, String.valueOf(id), 24, TimeUnit.HOURS);
        return SuccessTip.create(1);
    }

    //    获取当前预览页ID
    @GetMapping("/current")
    public Tip getCurrentPageId() {
        if (stringRedisTemplate.hasKey(currentPageFromId) && stringRedisTemplate.opsForValue().getOperations().getExpire(currentPageFromId) > 0) {
            logger.info("返回缓存数据");
            String value = (String) stringRedisTemplate.opsForValue().get(currentPageFromId);
            return SuccessTip.create(value);
        }else {
            throw new BusinessException(BusinessCode.BadRequest,"没有设置当前页面id");
        }
    }

    //    获取所有form页面列表
    @GetMapping("/forms")
    public Tip getAllPages(Page<FrontPageRecord> page,
                           @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                           @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                           // for tag feature query
                           @RequestParam(name = "tag", required = false) String tag,
                           // end tag
                           @RequestParam(name = "search", required = false) String search,

                           @RequestParam(name = "count", required = false) String count,

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
        record.setCount(count);
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

    //    获取当前预览页配置
    @GetMapping("/current/form")
    public Tip getCurrentPageJson() {
        if (stringRedisTemplate.hasKey(currentPageFromId) && stringRedisTemplate.opsForValue().getOperations().getExpire(currentPageFromId) > 0) {
            logger.info("返回缓存数据");
            String value = (String) stringRedisTemplate.opsForValue().get(currentPageFromId);
            Long fromId = Long.valueOf(value);

            if (stringRedisTemplate.hasKey(pageJsonKey+value) && stringRedisTemplate.opsForValue().getOperations().getExpire(pageJsonKey+value) > 0){
                String data = stringRedisTemplate.opsForValue().get(pageJsonKey+value);
                JSONObject json = JSON.parseObject(data);
                return SuccessTip.create(json);
            }
            return SuccessTip.create(mockJsonService.readJsonFile(fromId));
        }else {
            throw new BusinessException(BusinessCode.BadRequest,"没有设置当前页面id");
        }
    }

    //    设置当前预览页配置
    @PutMapping("/current/from")
    public Tip updateCurrentPageJson(@RequestBody JSONObject json) {
        System.out.println("===============");
        if (stringRedisTemplate.hasKey(currentPageFromId) && stringRedisTemplate.opsForValue().getOperations().getExpire(currentPageFromId) > 0) {
            logger.info("返回缓存数据");
            String value = (String) stringRedisTemplate.opsForValue().get(currentPageFromId);
            Long fromId = Long.valueOf(value);

            stringRedisTemplate.opsForValue().set(pageJsonKey+value, json.toJSONString(), 24, TimeUnit.HOURS);
            Integer integer = mockJsonService.saveJsonToFile(json, fromId);
            return SuccessTip.create(integer);
        }else {
            throw new BusinessException(BusinessCode.BadRequest,"没有设置当前页面id");
        }


    }


}
