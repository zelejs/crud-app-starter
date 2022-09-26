package com.jfeat.dev.devops.services.domain.model;

import com.jfeat.dev.devops.services.gen.persistence.model.DevVersion;

import java.util.List;

/**
 * Created by Code generator on 2022-09-16
 */
public class DevVersionRecord extends DevVersion {
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
