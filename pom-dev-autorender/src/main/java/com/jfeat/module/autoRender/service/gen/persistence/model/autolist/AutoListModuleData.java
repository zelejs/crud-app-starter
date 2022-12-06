package com.jfeat.module.autoRender.service.gen.persistence.model.autolist;

import com.alibaba.fastjson.JSONObject;

public class AutoListModuleData {


    private Integer currentModule;

    private String loadApi;

    private String itemNavigation;

    private JSONObject binging;

    private Integer columns;


    private AutoListModuleDataResponse response;



    public JSONObject getBinging() {
        return binging;
    }

    public void setBinging(JSONObject binging) {
        this.binging = binging;
    }




    public Integer getCurrentModule() {
        return currentModule;
    }

    public void setCurrentModule(Integer currentModule) {
        this.currentModule = currentModule;
    }

    public String getLoadApi() {
        return loadApi;
    }

    public void setLoadApi(String loadApi) {
        this.loadApi = loadApi;
    }

    public String getItemNavigation() {
        return itemNavigation;
    }

    public void setItemNavigation(String itemNavigation) {
        this.itemNavigation = itemNavigation;
    }

    public AutoListModuleDataResponse getResponse() {
        return response;
    }

    public void setResponse(AutoListModuleDataResponse response) {
        this.response = response;
    }
}
