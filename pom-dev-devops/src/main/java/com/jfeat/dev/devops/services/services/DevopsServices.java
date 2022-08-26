package com.jfeat.dev.devops.services.services;

import com.alibaba.fastjson.JSONArray;

import javax.servlet.http.HttpServletRequest;

public interface DevopsServices {
    JSONArray querySql(HttpServletRequest request,String sqlFile);

    int executeSql(HttpServletRequest request,String sqlFile);
}
