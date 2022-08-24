package com.jfeat.dev.devops.services.services;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface ParseRequestArgument{

    /**
     * 获取请求参数
     * @param request
     * @return 返回键对
     */
    Map<String,String> parseGetRequestArgument(HttpServletRequest request);
}
