package com.jfeat.module.autoRender.service.domain.service;

import com.alibaba.fastjson.JSONObject;

public interface AutoPageService {

    JSONObject getPageConfigJsonByPageId(Long pageId);

    JSONObject updatePageProp(JSONObject json,JSONObject prop);

    JSONObject removePageProp(JSONObject json,JSONObject prop);
}
