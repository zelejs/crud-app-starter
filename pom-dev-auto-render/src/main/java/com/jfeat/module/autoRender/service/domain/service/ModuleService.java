package com.jfeat.module.autoRender.service.domain.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface ModuleService {

    JSONArray getModuleListByType(JSONObject json,String type);

    List<String> getModuleKeyByType(JSONObject json,String type);

    JSONObject getModuleDataByKey(JSONObject json,String key);

    JSONObject replaceModuleData(JSONObject json,JSONObject moduleData,String key);

}
