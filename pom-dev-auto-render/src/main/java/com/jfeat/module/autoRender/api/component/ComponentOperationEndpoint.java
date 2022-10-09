package com.jfeat.module.autoRender.api.component;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jfeat.am.module.ioJson.services.domain.service.MockJsonService;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.module.autoRender.service.domain.service.AutoPageService;
import com.jfeat.module.autoRender.service.domain.service.ModuleDataService;
import com.jfeat.module.autoRender.service.domain.service.ModuleService;
import com.jfeat.module.autoRender.service.gen.persistence.model.AutoModule;
import com.jfeat.module.autoRender.service.gen.persistence.model.AutoPage;
import com.jfeat.module.frontPage.services.gen.persistence.dao.FrontPageMapper;
import com.jfeat.module.frontPage.services.gen.persistence.model.FrontPage;
import com.jfeat.module.lc.lc_low_auto_module_prop.services.domain.dao.QueryLowAutoModulePropDao;
import com.jfeat.module.lc.lc_low_auto_module_prop.services.domain.service.LowAutoModulePropService;
import com.jfeat.module.lc.lc_low_auto_module_prop.services.gen.crud.model.LowAutoModulePropModel;
import com.jfeat.module.lc_low_auto_module.services.domain.dao.QueryLowAutoModuleDao;
import com.jfeat.module.lc_low_auto_module.services.domain.service.LowAutoModuleOverModelService;
import com.jfeat.module.lc_low_auto_module.services.gen.persistence.dao.LowAutoModuleMapper;
import com.jfeat.module.lc_low_auto_module.services.gen.persistence.model.LowAutoModule;
import io.swagger.annotations.Api;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@Api("组件操作")
@RequestMapping("/dev/auto/forms")
public class ComponentOperationEndpoint {

    @Resource
    FrontPageMapper frontPageMapper;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    MockJsonService mockJsonService;


    private final String currentPageFromId = "currentPageFromId";

    private final String pageJsonKey = "page:";

    @Resource
    QueryLowAutoModulePropDao queryLowAutoModulePropDao;

    @Resource
    LowAutoModulePropService lowAutoModulePropService;

    @Resource
    LowAutoModuleOverModelService lowAutoModuleOverModelService;

    @Resource
    QueryLowAutoModuleDao queryLowAutoModuleDao;

    @Resource
    LowAutoModuleMapper lowAutoModuleMapper;

    @Resource
    AutoPageService autoPageService;

    @Resource
    ModuleService moduleService;

    @Resource
    ModuleDataService moduleDataService;



    @PostMapping("/current")
    public Tip createPageTemplate(@RequestBody AutoPage autoPage){
        QueryWrapper<FrontPage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(FrontPage.PAGE_ID,"module").eq(FrontPage.TITLE,autoPage.getPageType());
        FrontPage frontPage = frontPageMapper.selectOne(queryWrapper);
        if (frontPage==null){
            throw new BusinessException(BusinessCode.CodeBase,"未找到该页面配置");
        }
        String content = frontPage.getContent();
        JSONObject json = JSONObject.parseObject(content);
        if (stringRedisTemplate.hasKey(currentPageFromId) && stringRedisTemplate.opsForValue().getOperations().getExpire(currentPageFromId) > 0) {

            String value = (String) stringRedisTemplate.opsForValue().get(currentPageFromId);
            Long fromId = Long.valueOf(value);

            stringRedisTemplate.opsForValue().set(pageJsonKey + value, json.toJSONString(), 24, TimeUnit.HOURS);


            Integer integer = mockJsonService.saveJsonToFile(json, fromId);
            return SuccessTip.create(integer);
        } else {
            throw new BusinessException(BusinessCode.BadRequest, "没有设置当前页面id");
        }
    }

    @RequestMapping("/{id}/modules")
    public Tip addComponent(@PathVariable("id")Long id,@RequestBody AutoModule autoModule){

        JSONObject json = autoPageService.getPageConfigJsonByPageId(id);

        String moduleName = autoModule.getModuleName();
        QueryWrapper<LowAutoModule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("module_name",moduleName);
        LowAutoModule lowAutoModule =  lowAutoModuleMapper.selectOne(queryWrapper);

        JSONObject module = new JSONObject();
        module.put("type",lowAutoModule.getModuleName());
        String key = UUID.randomUUID().toString();
        module.put("key",key);
        json = moduleService.addModule(json,module);



        LowAutoModulePropModel lowAutoModulePropModels = queryLowAutoModulePropDao.queryProModelChile(-1L, lowAutoModule.getId());
        Map<String, Object> map = lowAutoModulePropService.autoModulePropToJson(lowAutoModulePropModels);

        Map<String,Object> moduleDataMap = (Map<String, Object>) map.get("data");

        JSONObject moduleData = new JSONObject(moduleDataMap);
        json = moduleDataService.addModuleData(json,moduleData,key);


        mockJsonService.saveJsonToFile(json,id);
        return SuccessTip.create(json);
    }

    @PostMapping("/{id}/module/op/arrange")
    public Tip arrangeComponent(@PathVariable("id")Long id,@RequestBody AutoModule autoModule){
        JSONObject json  = autoPageService.getPageConfigJsonByPageId(id);
        json =  moduleService.moveModule(json,autoModule.getFrom(),autoModule.getTo());
        mockJsonService.saveJsonToFile(json,id);
        return SuccessTip.create(json);
    }

    @PostMapping("/{id}/module/op/remove")
    public Tip removeComponent(@PathVariable("id")Long id,@RequestBody AutoModule autoModule){
        JSONObject json  = autoPageService.getPageConfigJsonByPageId(id);
        String key = moduleService.getModuleKey(json,autoModule.getIndex());
        json = moduleService.removeModule(json,key);
        mockJsonService.saveJsonToFile(json,id);
        return SuccessTip.create(json);
    }

    @PostMapping("/{id}/module/op/copy")
    public Tip copyComponent(@PathVariable("id")Long id,@RequestBody AutoModule autoModule){
        JSONObject json  = autoPageService.getPageConfigJsonByPageId(id);
        json = moduleService.copyModule(json,autoModule.getIndex());
        mockJsonService.saveJsonToFile(json,id);
        return SuccessTip.create(json);
    }

}
