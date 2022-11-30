package com.jfeat.module.autoRender.service.domain.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface ModuleDataService {

    /**
     * 获取module 对应数据
     * @param json
     * @param key
     * @return
     */
    JSONObject getModuleDataByKey(JSONObject json, String key);



    /**
     * 替换module 数据
     * @param json
     * @param moduleData
     * @param key
     * @return
     */
    JSONObject replaceModuleData(JSONObject json,JSONObject moduleData,String key);


    /**
     * 添加module data项
     * @param json json配置文件
     * @param moduleData  要添加的json
     * @param key uuid 对应的module 的key值
     * @return
     */
    JSONObject addModuleData(JSONObject json,JSONObject moduleData,String key);







}
