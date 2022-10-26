package com.jfeat.module.autoRender.service.domain.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jfeat.module.autoRender.service.domain.service.AutoPageService;
import com.jfeat.module.frontPage.services.gen.persistence.dao.FrontPageMapper;
import com.jfeat.module.frontPage.services.gen.persistence.model.FrontPage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;

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

    @Override
    public JSONObject updatePageProp(JSONObject json, JSONObject prop) {
        if (prop==null){
            return json;
        }
        Iterator<String> propKeys = prop.keySet().iterator();
        while (propKeys.hasNext()){
            String key = propKeys.next();
            json.put(key,prop.get(key));
        }
        return json;
    }

    @Override
    public JSONObject removePageProp(JSONObject json, JSONObject prop) {
        if (prop==null){
            return json;
        }
        Iterator<String> propKeys = prop.keySet().iterator();
        while (propKeys.hasNext()){
            String key = propKeys.next();
            json.remove(key);
        }
        return json;
    }
}
