package com.jfeat.module.autoRender.service.domain.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface ModuleService {

    /**
     * 获取Module 类型列表
     * @param json
     * @param type
     * @return
     */
    JSONArray getModuleListByType(JSONObject json,String type);

    /**
     * 获取某一个类型module key 列表
     * @param json
     * @param type
     * @return
     */
    List<String> getModuleKeyByType(JSONObject json,String type);


    /**
     * 获取module 对应数据
     * @param json
     * @param key
     * @return
     */
    JSONObject getModuleDataByKey(JSONObject json,String key);

    /**
     * 替换module 数据
     * @param json
     * @param moduleData
     * @param key
     * @return
     */
    JSONObject replaceModuleData(JSONObject json,JSONObject moduleData,String key);


    JSONObject addModule(JSONObject json,JSONObject module);

    JSONObject addModuleData(JSONObject json,JSONObject moduleData,String key);




    Boolean existModule(JSONObject json,String key);

    Integer getModuleIndex(JSONObject json,String key);

    String getModuleKey(JSONObject json,Integer index);

    JSONObject removeModule(JSONObject json,String key);

    JSONObject moveModule(JSONObject json,Integer from,Integer to);

    JSONObject copyModule(JSONObject json,String key);

    JSONObject copyModule(JSONObject json,Integer index);

    JSONObject getModuleByIndex(JSONObject json,Integer index);

    String getModuleKeyByIndex(JSONObject json,Integer integer);



}
