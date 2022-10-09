package com.jfeat.module.autoRender.service.domain.service.imp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.module.autoRender.service.domain.service.ModuleDataService;
import org.springframework.stereotype.Service;

@Service
public class ModuleDataServiceImp implements ModuleDataService {
    @Override
    public JSONObject getModuleDataByKey(JSONObject json, String key) {
        if (json==null){
            throw new BusinessException(BusinessCode.BadRequest,"json配置不能为空");
        }
        if (json.containsKey("moduleData") && json.get("moduleData")!=null){
            JSONObject moduleData = json.getJSONObject("moduleData");

            if (moduleData.containsKey(key)&&moduleData.get(key)!=null){
                return moduleData.getJSONObject(key);
            }
        }
        return null;
    }



    @Override
    public JSONObject replaceModuleData(JSONObject json, JSONObject moduleData, String key) {
        if (json==null){
            throw new BusinessException(BusinessCode.BadRequest,"json配置不能为空");
        }
        if (json.containsKey("moduleData") && json.get("moduleData")!=null){
            JSONObject moduleDataList = json.getJSONObject("moduleData");

            if (moduleDataList.containsKey(key)&&moduleDataList.get(key)!=null){
                moduleDataList.put(key,moduleData);
                json.put("moduleData",moduleDataList);
                return json;
            }
        }
        return null;
    }

    @Override
    public JSONObject addModuleData(JSONObject json, JSONObject moduleData,String key) {
        if (json==null){
            throw new BusinessException(BusinessCode.BadRequest,"json配置不能为空");
        }
        if (json.containsKey("moduleData") && json.get("moduleData")!=null){
            JSONObject jsonObject = json.getJSONObject("moduleData");
            jsonObject.put(key,moduleData);
            json.put("moduleData",jsonObject);
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(key,moduleData);
            json.put("moduleData",jsonObject);
        }
        return json;
    }

}
