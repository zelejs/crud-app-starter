package com.jfeat.module.autoRender.service.gen.persistence.model;

import com.jfeat.am.crud.tag.services.persistence.model.StockTag;

public class AutoPageTag extends StockTag {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
