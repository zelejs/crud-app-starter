package com.jfeat.module.autoRender.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;

public class JsonUtil {

    public static Boolean isJsonObject(JSONObject json, String key){
        if (!json.containsKey(key)){
            throw new BusinessException(BusinessCode.BadRequest,"Not find key");
        }
        Object targetObject = json.get(key);
        if (targetObject instanceof JSONObject){
            return true;
        }else {
            return false;

        }
    }

    public static Boolean isJsonJSONArray(JSONObject json, String key){
        if (!json.containsKey(key)){
            throw new BusinessException(BusinessCode.BadRequest,"Not find key");
        }
        Object targetObject = json.get(key);
        if (targetObject instanceof JSONObject){
            return true;
        }else {
            return false;
        }
    }
}
