package com.jfeat.module.autoRender.api.component;


import com.alibaba.fastjson.JSON;
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
import com.jfeat.module.autoRender.service.gen.persistence.model.AutoTableItemColumns;
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
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "创建页面模板",response = AutoPage.class)
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

    @PostMapping("/{id}/modules")
    @ApiOperation(value = "创建组件item",response = AutoModule.class)
    public Tip addComponent(@PathVariable("id")Long id,@RequestBody AutoModule autoModule){

        JSONObject json = autoPageService.getPageConfigJsonByPageId(id);


        String moduleName = autoModule.getModuleName();
//        QueryWrapper<LowAutoModule> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("module_name",moduleName);
//        LowAutoModule lowAutoModule =  lowAutoModuleMapper.selectOne(queryWrapper);
//
//        JSONObject module = new JSONObject();
//        module.put("type",lowAutoModule.getModuleName());
//        String key = UUID.randomUUID().toString();
//        module.put("key",key);
//        json = moduleService.addModule(json,module);
//
//
//        LowAutoModulePropModel lowAutoModulePropModels = queryLowAutoModulePropDao.queryProModelChile(-1L, lowAutoModule.getId());
//        Map<String, Object> map = lowAutoModulePropService.autoModulePropToJson(lowAutoModulePropModels);
//
//        Map<String,Object> moduleDataMap = (Map<String, Object>) map.get("data");
//
//        JSONObject moduleData = new JSONObject(moduleDataMap);


        String data  = "{\n" +
                "\t\"navlist\": {\n" +
                "\t\t\"itemModule\": {\n" +
                "\t\t\t\"name\": \"MagicItem\"\n" +
                "\t\t},\n" +
                "\t\t\"itemGroupModule\": {\n" +
                "\t\t\t\"container\": {\n" +
                "\t\t\t\t\"fontWeight\": \"bold\",\n" +
                "\t\t\t\t\"fontSize\": \"35rpx\",\n" +
                "\t\t\t\t\"background\": \"white\",\n" +
                "\t\t\t\t\"color\": \"#707070\"\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t\"navList\": [{\n" +
                "\t\t\t\t\"group\": \"组名\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"img\": \"图标\",\n" +
                "\t\t\t\t\"color\": \"#ff844b\",\n" +
                "\t\t\t\t\"title\": \"\",\n" +
                "\t\t\t\t\"nav\": \"跳转路径\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"group\": \"用户配置\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"img\": \"\",\n" +
                "\t\t\t\t\"color\": \"#f21137\",\n" +
                "\t\t\t\t\"title\": \"\",\n" +
                "\t\t\t\t\"nav\": \"\"\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t},\n" +
                "\t\"autolist\": {\n" +
                "\t\t\"loadApi\": \"\",\n" +
                "\t\t\"itemNavigation\": \"\",\n" +
                "\t\t\"response\": {\n" +
                "\t\t\t\"list\": \"\"\n" +
                "\t\t},\n" +
                "\t\t\"request\": {\n" +
                "\t\t\t\"default\": {},\n" +
                "\t\t\t\"ps\": \"pageSize\",\n" +
                "\t\t\t\"pn\": \"pageNum\"\n" +
                "\t\t},\n" +
                "\t\t\"itemModule\": {\n" +
                "\t\t\t\"name\": \"saleHouseItem\"\n" +
                "\t\t},\n" +
                "\t\t\"columns\": 2\n" +
                "\t},\n" +
                "\t\"banner\": {\n" +
                "\t\t\"banners\": [{\n" +
                "\t\t\t\t\"img\": \"\"\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t],\n" +
                "\t\t\"outStyle\": {},\n" +
                "\t\t\"control\": {\n" +
                "\t\t\t\"autoplay\": \"false\",\n" +
                "\t\t\t\"interval\": \"4000\",\n" +
                "\t\t\t\"duration\": \"600\",\n" +
                "\t\t\t\"circular\": \"100\",\n" +
                "\t\t\t\"indicatorDot\": \"true\",\n" +
                "\t\t\t\"indicator_color\": \"rgba(0, 0, 0, .3)\",\n" +
                "\t\t\t\"indicator_active_color\": \"#000000\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"autoform\": {\n" +
                "\t\t\"fields\": [{\n" +
                "\t\t\t\t\"__config__\": {\n" +
                "\t\t\t\t\t\"label\": \"标签\",\n" +
                "\t\t\t\t\t\"labelWidth\": null,\n" +
                "\t\t\t\t\t\"showLabel\": true,\n" +
                "\t\t\t\t\t\"tag\": \"el-input\",\n" +
                "\t\t\t\t\t\"tagIcon\": \"input\",\n" +
                "\t\t\t\t\t\"defaultValue\": \"\",\n" +
                "\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\"layout\": \"colFormItem\",\n" +
                "\t\t\t\t\t\"regList\": [],\n" +
                "\t\t\t\t\t\"formId\": 101,\n" +
                "\t\t\t\t\t\"renderKey\": 1621415022993\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"__slot__\": {\n" +
                "\t\t\t\t\t\"prepend\": \"\",\n" +
                "\t\t\t\t\t\"append\": \"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"placeholder\": \"请输入\",\n" +
                "\t\t\t\t\"style\": {\n" +
                "\t\t\t\t\t\"width\": \"100%\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"clearable\": true,\n" +
                "\t\t\t\t\"prefix-icon\": \"\",\n" +
                "\t\t\t\t\"suffix-icon\": \"\",\n" +
                "\t\t\t\t\"maxlength\": null,\n" +
                "\t\t\t\t\"show-word-limit\": false,\n" +
                "\t\t\t\t\"readonly\": false,\n" +
                "\t\t\t\t\"disabled\": false,\n" +
                "\t\t\t\t\"__vModel__\": \"code\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"__config__\": {\n" +
                "\t\t\t\t\t\"label\": \"标签\",\n" +
                "\t\t\t\t\t\"showLabel\": true,\n" +
                "\t\t\t\t\t\"labelWidth\": null,\n" +
                "\t\t\t\t\t\"tag\": \"el-select\",\n" +
                "\t\t\t\t\t\"tagIcon\": \"select\",\n" +
                "\t\t\t\t\t\"layout\": \"colFormItem\",\n" +
                "\t\t\t\t\t\"required\": false,\n" +
                "\t\t\t\t\t\"regList\": [],\n" +
                "\t\t\t\t\t\"formId\": 103,\n" +
                "\t\t\t\t\t\"renderKey\": 1621415255861\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"__slot__\": {\n" +
                "\t\t\t\t\t\"options\": [{\n" +
                "\t\t\t\t\t\t\t\"label\": \"选项一\",\n" +
                "\t\t\t\t\t\t\t\"value\": 1\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\"label\": \"选项二\",\n" +
                "\t\t\t\t\t\t\t\"value\": 2\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"placeholder\": \"请选择\",\n" +
                "\t\t\t\t\"style\": {\n" +
                "\t\t\t\t\t\"width\": \"100%\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"clearable\": true,\n" +
                "\t\t\t\t\"disabled\": false,\n" +
                "\t\t\t\t\"filterable\": false,\n" +
                "\t\t\t\t\"multiple\": false,\n" +
                "\t\t\t\t\"__vModel__\": \"绑定api字段\"\n" +
                "\t\t\t}\n" +
                "\t\t],\n" +
                "\t\t\"formRef\": \"elForm\",\n" +
                "\t\t\"formModel\": \"formData\",\n" +
                "\t\t\"size\": \"medium\",\n" +
                "\t\t\"labelPosition\": \"right\",\n" +
                "\t\t\"labelWidth\": 100,\n" +
                "\t\t\"formRules\": \"rules\",\n" +
                "\t\t\"gutter\": 15,\n" +
                "\t\t\"disabled\": false,\n" +
                "\t\t\"span\": 24,\n" +
                "\t\t\"formBtns\": true,\n" +
                "\t\t\"saveApi\": \"提交api\",\n" +
                "\t\t\"saveMethod\": \"POST\",\n" +
                "\t\t\"loadApi\": \"\",\n" +
                "\t\t\"isLawForm\": false\n" +
                "\t}\n" +
                "\n" +
                "\n" +
                "}";

        JSONObject jsonObject = JSON.parseObject(data);
        if (jsonObject.containsKey(moduleName)){
            JSONObject module = new JSONObject();
            module.put("type",moduleName);
            String key = UUID.randomUUID().toString();
            module.put("key",key);
            json = moduleService.addModule(json,module);
            JSONObject moduleData = jsonObject.getJSONObject(moduleName);
            json = moduleDataService.addModuleData(json,moduleData,key);
            mockJsonService.saveJsonToFile(json,id);
        }
        return SuccessTip.create(json);
    }

    @PostMapping("/{id}/module/op/arrange")
    @ApiOperation(value = "移动组件顺序",response = AutoModule.class)
    public Tip arrangeComponent(@PathVariable("id")Long id,@RequestBody AutoModule autoModule){
        JSONObject json  = autoPageService.getPageConfigJsonByPageId(id);
        json =  moduleService.moveModule(json,autoModule.getFrom(),autoModule.getTo());
        mockJsonService.saveJsonToFile(json,id);
        return SuccessTip.create(json);
    }

    @PostMapping("/{id}/module/op/remove")
    @ApiOperation(value = "移除组件",response = AutoModule.class)
    public Tip removeComponent(@PathVariable("id")Long id,@RequestBody AutoModule autoModule){
        JSONObject json  = autoPageService.getPageConfigJsonByPageId(id);
        String key = moduleService.getModuleKey(json,autoModule.getIndex());
        json = moduleService.removeModule(json,key);
        mockJsonService.saveJsonToFile(json,id);
        return SuccessTip.create(json);
    }

    @PostMapping("/{id}/module/op/copy")
    @ApiOperation(value = "复制组件",response = AutoModule.class)
    public Tip copyComponent(@PathVariable("id")Long id,@RequestBody AutoModule autoModule){
        JSONObject json  = autoPageService.getPageConfigJsonByPageId(id);
        json = moduleService.copyModule(json,autoModule.getIndex());
        mockJsonService.saveJsonToFile(json,id);
        return SuccessTip.create(json);
    }

}
