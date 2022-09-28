package com.jfeat.module.autoRender.service.domain.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jfeat.module.autoRender.service.domain.service.AutoPageService;
import com.jfeat.module.frontPage.services.gen.persistence.dao.FrontPageMapper;
import com.jfeat.module.frontPage.services.gen.persistence.model.FrontPage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AutoPageServiceImp implements AutoPageService {

    @Resource
    FrontPageMapper frontPageMapper;

    @Override
    public JSONObject getPageConfigJsonByPageId(Long pageId) {

        QueryWrapper<FrontPage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(FrontPage.PAGE_ID,pageId);
        FrontPage frontPage = frontPageMapper.selectOne(queryWrapper);
        if (frontPage!=null){
            String content = frontPage.getContent();
            return JSONObject.parseObject(content);

        }
        return null;
    }
}
