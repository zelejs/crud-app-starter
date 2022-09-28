package com.jfeat.module.autoRender.service.domain.service.imp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.module.autoRender.service.domain.service.ModuleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Override
    public JSONObject addModule(JSONObject json, JSONObject module) {

        if (json==null){
            throw new BusinessException(BusinessCode.BadRequest,"json配置不能为空");
        }
        JSONArray result = new JSONArray();
        if (json.containsKey("modules") && json.get("modules")!=null){
            JSONArray moduleList = json.getJSONArray("modules");
            moduleList.add(module);
            json.put("modules",moduleList);
        }else {
            JSONArray moduleList = new JSONArray();
            moduleList.add(module);
            json.put("modules",moduleList);
        }
        return json;
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

    @Override
    public Boolean existModule(JSONObject json, String key) {
        if (json==null){
            throw new BusinessException(BusinessCode.BadRequest,"json配置不能为空");
        }
        if (json.containsKey("modules") && json.get("modules")!=null){
            JSONArray moduleList = json.getJSONArray("modules");
            if (moduleList!=null && moduleList.size()>0){
                for (int i=0;i<moduleList.size();i++){
                    JSONObject module = moduleList.getJSONObject(i);
                    if (module!=null && module.containsKey("key") && module.getString("key").equals(key)){
                       return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Integer getModuleIndex(JSONObject json, String key) {
        if (json==null){
            throw new BusinessException(BusinessCode.BadRequest,"json配置不能为空");
        }
        if (json.containsKey("modules") && json.get("modules")!=null){
            JSONArray moduleList = json.getJSONArray("modules");
            if (moduleList!=null && moduleList.size()>0){
                for (int i=0;i<moduleList.size();i++){
                    JSONObject module = moduleList.getJSONObject(i);
                    if (module!=null && module.containsKey("key") && module.getString("key").equals(key)){
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    @Override
    public String getModuleKey(JSONObject json, Integer index) {
        if (json==null){
            throw new BusinessException(BusinessCode.BadRequest,"json配置不能为空");
        }
        if (json.containsKey("modules") && json.get("modules")!=null){
            JSONArray moduleList = json.getJSONArray("modules");
           JSONObject module = moduleList.getJSONObject(index);
           if (module.containsKey("key")&&module.getString("key")!=null){
               return module.getString("key");
           }
        }
        return null;
    }

    @Override
    public JSONObject removeModule(JSONObject json, String key) {
        if (json==null){
            throw new BusinessException(BusinessCode.BadRequest,"json配置不能为空");
        }
        if (json.containsKey("modules") && json.get("modules")!=null){
            JSONArray moduleList = json.getJSONArray("modules");
            if (moduleList!=null && moduleList.size()>0){
                for (int i=0;i<moduleList.size();i++){
                    JSONObject module = moduleList.getJSONObject(i);
                    if (module!=null && module.containsKey("key") && module.getString("key").equals(key)){
                        moduleList.remove(module);
                    }
                }
            }
            json.put("modules",moduleList);
        }
        if (json.containsKey("moduleData") && json.get("moduleData")!=null){
            JSONObject jsonObject = json.getJSONObject("moduleData");
            jsonObject.remove(key);
            json.put("moduleData",jsonObject);
        }
        return json;
    }

    @Override
    public JSONObject moveModule(JSONObject json, Integer from, Integer to) {
        if (json==null){
            throw new BusinessException(BusinessCode.BadRequest,"json配置不能为空");
        }
        if (json.containsKey("modules") && json.get("modules")!=null){
            JSONArray moduleList = json.getJSONArray("modules");
            if (from<0 || from>=moduleList.size()){
                throw new BusinessException(BusinessCode.OutOfRange,"from超出范围");
            }
            if (to<0 || from>=moduleList.size()){
                throw new BusinessException(BusinessCode.OutOfRange,"to超出范围");
            }
            JSONObject module = JSONObject.parseObject(moduleList.getJSONObject(from).toJSONString());
            moduleList.remove(from.intValue());
            moduleList.add(to.intValue(),module);

            json.put("modules",moduleList);
        }
        return json;
    }

    @Override
    public JSONObject copyModule(JSONObject json, String key) {
        if (json==null){
            throw new BusinessException(BusinessCode.BadRequest,"json配置不能为空");
        }
        if (json.containsKey("modules") && json.get("modules")!=null){
            JSONArray moduleList = json.getJSONArray("modules");

            json.put("modules",moduleList);
        }
        return null;
    }

    @Override
    public JSONObject copyModule(JSONObject json, Integer index) {
        if (json==null){
            throw new BusinessException(BusinessCode.BadRequest,"json配置不能为空");
        }
        if (json.containsKey("modules") && json.get("modules")!=null){
            JSONArray moduleList = json.getJSONArray("modules");
            if (index>=0&&index<moduleList.size()){
                JSONObject module = JSONObject.parseObject(moduleList.getJSONObject(index).toJSONString());
                String key = module.getString("key");

                String uuid = UUID.randomUUID().toString();
                module.put("key",uuid);
                moduleList.add(module);
                json.put("modules",moduleList);

                JSONObject moduleDataItem = JSONObject.parseObject(getModuleDataByKey(json,key).toJSONString());
                System.out.println(moduleDataItem);
                JSONObject moduleData = json.getJSONObject("moduleData");
                moduleData.put(uuid,moduleDataItem);
                json.put("moduleData",moduleData);
            }

        }
        return json;
    }

    @Override
    public JSONObject getModuleByIndex(JSONObject json,Integer index) {
        if (json==null){
            throw new BusinessException(BusinessCode.BadRequest,"json配置不能为空");
        }
        if (json.containsKey("modules") && json.get("modules")!=null){
            JSONArray moduleList = json.getJSONArray("modules");
            if (index>=0&&index<moduleList.size()){
                JSONObject module = JSONObject.parseObject(moduleList.getJSONObject(index).toJSONString());
                return module;
            }
        }
        return null;
    }

    @Override
    public String getModuleKeyByIndex(JSONObject json,Integer index) {
        if (json==null){
            throw new BusinessException(BusinessCode.BadRequest,"json配置不能为空");
        }
        if (json.containsKey("modules") && json.get("modules")!=null){
            JSONArray moduleList = json.getJSONArray("modules");
            if (index>=0&&index<moduleList.size()){
                JSONObject module = JSONObject.parseObject(moduleList.getJSONObject(index).toJSONString());
                if (module.containsKey("key")&&module.getString("key")!=null){
                    return module.getString("key");
                }

            }
        }
        return null;
    }
}
