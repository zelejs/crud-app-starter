package com.jfeat.module.autoRender.service.domain.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jfeat.am.module.ioJson.services.domain.service.MockJsonService;
import com.jfeat.am.module.ioJson.services.domain.util.FileUtil;
import com.jfeat.module.autoRender.service.domain.service.AutoPageService;
import com.jfeat.module.frontPage.services.gen.persistence.dao.FrontPageMapper;
import com.jfeat.module.frontPage.services.gen.persistence.model.FrontPage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

@Service
public class AutoPageServiceImp implements AutoPageService {

    //文件的每一行
    private String[] lines;
    private static String dir = "jsonMock";
    private static final String DEFAULT_APP_ID = "DEFAULT";
    private static String appId = DEFAULT_APP_ID;

    @Resource
    FrontPageMapper frontPageMapper;

    @Resource
    MockJsonService mockJsonService;

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

    @Override
    public int deletePageById(Long pageId) {

        //删除数据库数据
//        QueryWrapper<FrontPage> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq(FrontPage.PAGE_ID,pageId);
//        int value = frontPageMapper.delete(queryWrapper);

        //删除properties配置数据



        Map<String, String> valueMap = FileUtil.readProperties(FileUtil.getFile( dir + File.separator + appId
                , dir + File.separator + appId + File.separator + "appSite.properties"));

        valueMap.keySet().removeIf(key -> key == pageId.toString());
        System.out.println(valueMap);
//        ArrayList<String> lineList = new ArrayList<>(Arrays.asList(lines));
//        lineList.removeIf(line -> hasKey(line, key));
//        return commit(lineList.toArray(new String[0]));


        return 0;
    }
}
