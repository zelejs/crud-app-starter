package com.jfeat.module.autoRender.service.domain.service.imp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.module.autoRender.service.domain.service.ModuleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModuleServiceImp implements ModuleService {


//    获取给定的type module列表
    @Override
    public JSONArray getModuleListByType(JSONObject json,String type) {
        if (json==null){
            throw new BusinessException(BusinessCode.BadRequest,"json配置不能为空");
        }
        JSONArray result = new JSONArray();
        if (json.containsKey("modules") && json.get("modules")!=null){
            JSONArray moduleList = json.getJSONArray("modules");
            if (moduleList!=null && moduleList.size()>0){
                for (int i=0;i<moduleList.size();i++){
                    JSONObject module = moduleList.getJSONObject(i);
                    if (module!=null&&module.containsKey("type")&&module.get("type").equals(type)){
                        result.add(module);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<String> getModuleKeyByType(JSONObject json, String type) {
        if (json==null){
            throw new BusinessException(BusinessCode.BadRequest,"json配置不能为空");
        }
        List<String> result = new ArrayList<>();
        if (json.containsKey("modules") && json.get("modules")!=null){
            JSONArray moduleList = json.getJSONArray("modules");
            if (moduleList!=null && moduleList.size()>0){
                for (int i=0;i<moduleList.size();i++){
                    JSONObject module = moduleList.getJSONObject(i);
                    if (module!=null && module.containsKey("type") && module.getString("type").equals(type)){

                        if (module.containsKey("key")&&module.get("key")!=null && ! module.get("key").equals("")){
                            String key = module.getString("key");
                            result.add(key);
                        }
                    }
                }
            }
        }
        return result;
    }

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
}
