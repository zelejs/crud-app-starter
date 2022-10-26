package com.jfeat.module.autoRender.api.page;

import com.alibaba.fastjson.JSONObject;
import com.jfeat.am.module.ioJson.services.domain.service.MockJsonService;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.module.autoRender.service.domain.service.AutoPageService;
import com.jfeat.module.autoRender.service.domain.service.ModuleService;
import com.jfeat.module.autoRender.service.gen.persistence.model.AutoPage;
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


    @PutMapping("/{id}/page")
    @ApiOperation(value = "修改页面属性id",response = AutoPage.class)
    public Tip updatePageProp(@PathVariable("id") Long id,
                              @RequestBody JSONObject prop){
        JSONObject json  = autoPageService.getPageConfigJsonByPageId(id);
        autoPageService.updatePageProp(json,prop);
        mockJsonService.saveJsonToFile(json,id);
        return SuccessTip.create(json);
    }

}
