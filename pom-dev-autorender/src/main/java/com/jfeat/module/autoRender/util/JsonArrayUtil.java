package com.jfeat.module.autoRender.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.base.tips.Tip;

import java.util.Set;

public class JsonArrayUtil {


    //    增加json
    public static JSONArray addJson(JSONArray jsonArray, JSONObject jsonObject, Integer index) {

        if (index==null){
            jsonArray.add(jsonObject);
        }else {
            if (index < 0 || index > jsonArray.size()) {
                throw new BusinessException(BusinessCode.OutOfRange, "Index 越界");
            }
            jsonArray.add(index, jsonObject);
        }

        return jsonArray;
    }

    //    移除json
    public static JSONArray remove(JSONArray jsonArray, Integer index) {
        if (index < 0 || index >= jsonArray.size()) {
            throw new BusinessException(BusinessCode.OutOfRange, "Index 越界");
        }
        jsonArray.remove(index.intValue());
        return jsonArray;
    }


    //    修改json元素
    public static JSONArray modifyItemJson(JSONArray jsonArray, JSONObject jsonObject, Integer index) {
        if (index < 0 || index >= jsonArray.size()) {
            throw new BusinessException(BusinessCode.OutOfRange, "Index 越界");
        }
        JSONObject json = jsonArray.getJSONObject(index);

        Set<String> strings = jsonObject.keySet();
        for (String s : strings) {
            json.put(s, jsonObject.get(s));
        }
        return jsonArray;
    }

//        复制json元素
    public static JSONArray copy(JSONArray jsonArray,Integer from,Integer to){
        if (from<0||from>=jsonArray.size()){
            throw new BusinessException(BusinessCode.OutOfRange, "from 越界");
        }

        Object json =  JSON.parse(JSON.toJSONString(jsonArray.get(from), SerializerFeature.DisableCircularReferenceDetect));
        if (to==null){
            jsonArray.add(json);
        }else {
            if (to<0 || to>jsonArray.size()){
                throw new BusinessException(BusinessCode.OutOfRange, "to 越界");
            }
            jsonArray.add(to,json);
        }

        return jsonArray;
    }


//         移动json 元素
    public static JSONArray move(JSONArray jsonArray,Integer from,Integer to){
        if (from<0||from>=jsonArray.size()){
            throw new BusinessException(BusinessCode.OutOfRange, "from 越界");
        }
        if (to<0 || to>jsonArray.size()){
            throw new BusinessException(BusinessCode.OutOfRange, "to 越界");
        }
        Object json =  JSON.parse(JSON.toJSONString(jsonArray.get(from), SerializerFeature.DisableCircularReferenceDetect));
        jsonArray.add(to.intValue(),json);
        jsonArray.remove(from.intValue());
        return jsonArray;
    }
}
