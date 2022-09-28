package com.jfeat.module.autoRender.service.gen.persistence.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;

public class AutoModule extends Model<AutoModule> {
    private static final long serialVersionUID=1L;

    private String moduleName;
    private Integer from;
    private Integer to;
    private Integer index;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
