package com.jfeat.module.autoRender.service.gen.persistence.model;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.activerecord.Model;

public class AutoPage extends Model<AutoPage> {
    private static final long serialVersionUID=1L;

    private String pageType;

    private String version;

    private String name;

    private String title;

    private JSONObject container;


    public JSONObject getContainer() {
        return container;
    }

    public void setContainer(JSONObject container) {
        this.container = container;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }
}
