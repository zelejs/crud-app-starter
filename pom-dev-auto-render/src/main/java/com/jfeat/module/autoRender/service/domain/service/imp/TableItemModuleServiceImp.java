package com.jfeat.module.autoRender.service.domain.service.imp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.module.autoRender.service.domain.service.AutoPageService;
import com.jfeat.module.autoRender.service.domain.service.ModuleDataService;
import com.jfeat.module.autoRender.service.domain.service.ModuleService;
import com.jfeat.module.autoRender.service.domain.service.TableItemModuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TableItemModuleServiceImp implements TableItemModuleService {

    @Resource
    ModuleService moduleService;

    @Resource
    ModuleDataService moduleDataService;

    @Resource
    AutoPageService autoPageService;


    @Override
    public JSONObject getTableItemModule(JSONObject json, Integer index) {

        String key =  moduleService.getModuleKey(json,index);

        JSONObject moduleData =  moduleDataService.getModuleDataByKey(json,key);
        if (moduleData !=null){
            if (moduleData.containsKey("tableItemModule") && moduleData.get("tableItemModule")!=null){
                JSONObject tableItemModule = moduleData.getJSONObject("tableItemModule");
                return tableItemModule;
            }

        }
        return null;
    }

    @Override
    public JSONArray getALlColumns(JSONObject json, Integer index) {
        JSONObject tableItemModule = getTableItemModule(json,index);
        if (tableItemModule!=null){
            if (tableItemModule.containsKey("name") && tableItemModule.getString("name")!=null && tableItemModule.getString("name").equals("default")){

                if (tableItemModule.containsKey("columns") && tableItemModule.get("columns")!=null){
                    return tableItemModule.getJSONArray("columns");
                }

            }
        }
        return null;
    }

    @Override
    public JSONObject setColumns(JSONObject json, JSONArray columns,Integer index) {

        String key =  moduleService.getModuleKey(json,index);
        JSONObject moduleData =  moduleDataService.getModuleDataByKey(json,key);
        if (moduleData !=null){
            if (moduleData.containsKey("tableItemModule") && moduleData.get("tableItemModule")!=null){
                JSONObject tableItemModule = moduleData.getJSONObject("tableItemModule");
                tableItemModule.put("columns",columns);
                return json;
            }

        }
        return null;
    }
}
