package com.jfeat.module.dev.api;

import com.alibaba.fastjson.JSONObject;
import com.jfeat.am.core.jwt.JWTKit;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.module.dev.model.Dummy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import ch.qos.logback.core.joran.spi.RuleStore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController

@Api("Dummy")
@RequestMapping("/api")
public class DummyEndpoint {

    @ApiOperation(value = "dummy api")
    @GetMapping("/dummy")
    public Tip dummyService(HttpServletRequest request, HttpServletResponse response){
        Dummy dummy = new Dummy();
        dummy.setStatus(response.getStatus());
        dummy.setUri(request.getRequestURI().toString());
        dummy.setMethod(request.getMethod());
        dummy.setContent("dummy");
        
        return SuccessTip.create(dummy);
    }

    @ApiOperation(value = "request info")
    @GetMapping("/pub/dummy")
    public Tip devService(HttpServletRequest request, HttpServletResponse response){

        JSONObject result = new JSONObject();

        String host = request.getHeader("Host");
        String uriString = request.getRequestURI();
        String urlString = request.getRequestURL().toString();
        String schema = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String remoteUser = request.getRemoteUser();

        result.put("Host", host);
        result.put("URI", uriString);
        result.put("URL", urlString);
        result.put("schema", schema);
        result.put("serverName", serverName);
        result.put("serverPort", serverPort);
        result.put("remoteUser", remoteUser);

        // get endpoint
        String endpoint = urlString.contains("?")?urlString.substring(0, urlString.indexOf("?")-1) : urlString;
        endpoint.replace(endpoint, uriString);
        result.put("endpoint", endpoint);

        return SuccessTip.create(result);
    }
}
