package com.jfeat.module.autoRender.service.gen.persistence.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonInclude;

public class AutoTableItemColumns extends Model<AutoTableItemColumns> {

    private static final long serialVersionUID=1L;


    private Integer currentModule;

    private Integer from;

    private Integer to;

    private Integer index;

    private String label;

    private String field;

    private Integer width;

    private String thAlign;

    private String tdAlign;

    public Integer getCurrentModule() {
        return currentModule;
    }

    public void setCurrentModule(Integer currentModule) {
        this.currentModule = currentModule;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
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

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getThAlign() {
        return thAlign;
    }

    public void setThAlign(String thAlign) {
        this.thAlign = thAlign;
    }

    public String getTdAlign() {
        return tdAlign;
    }

    public void setTdAlign(String tdAlign) {
        this.tdAlign = tdAlign;
    }
}
