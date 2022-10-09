package com.jfeat.module.autoRender.service.domain.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface TableItemModuleService {

    JSONObject getTableItemModule(JSONObject json,Integer index);

    JSONArray getALlColumns(JSONObject json,Integer index);

    JSONObject setColumns(JSONObject json,JSONArray columns,Integer index);
}
