package com.jfeat.module.autoRender.service.gen.persistence.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;

public class PageSimpleInfo extends Model<PageSimpleInfo> {
    private static final long serialVersionUID=1L;

    private String pageId;

    private String title;

    private String appid;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
