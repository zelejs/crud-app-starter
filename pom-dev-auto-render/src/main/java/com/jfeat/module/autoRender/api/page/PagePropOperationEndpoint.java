package com.jfeat.module.autoRender.api.page;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.module.autoRender.service.domain.service.AutoPageService;
import com.jfeat.module.autoRender.service.domain.service.ModuleService;
import com.jfeat.module.autoRender.service.gen.persistence.model.AutoRoute;
import com.jfeat.module.autoRender.util.ParameterUtil;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/dev/auto/forms")
public class PagePropOperationEndpoint {

    @Resource
    AutoPageService autoPageService;

    @Resource
    ModuleService moduleService;


    @PutMapping("/{id}/page")
    public Tip updatePageProp(Long id){
        JSONObject json  = autoPageService.getPageConfigJsonByPageId(id);
        return SuccessTip.create();
    }
}
