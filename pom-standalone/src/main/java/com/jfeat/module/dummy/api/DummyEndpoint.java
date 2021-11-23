package com.jfeat.module.dev.api;

import com.alibaba.fastjson.JSONObject;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.module.dev.model.Dummy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@RestController

@Api("Dummy")
@RequestMapping("/api")
public class DummyEndpoint {
    Logger logger = LoggerFactory.getLogger(DummyEndpoint.class.getName());

    @ApiOperation(value = "dummy get api")
    @GetMapping("/dummy")
    public Tip dummyService(HttpServletRequest request, HttpServletResponse response){
        Dummy dummy = new Dummy();
        dummy.setStatus(response.getStatus());
        dummy.setUri(request.getRequestURI().toString());
        dummy.setMethod(request.getMethod());
        dummy.setContent("dummy");
        
        return SuccessTip.create(dummy);
    }

    @ApiOperation(value = "dummy post json api")
    @PostMapping(value = "/dummy", produces = "application/json;charset=UTF-8")
    public Tip doPostDummy(@RequestBody Dummy dummy){
        logger.info("receive dummy json data: {}", dummy.toString());
        return SuccessTip.create(dummy);
    }

    @ApiOperation(value = "dummy post x-www-urlencoded-form api")
    @PostMapping(value = "/dummy/form",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public Tip doPostXWWWFormUrlencoded(@RequestParam Dummy dummy){
        logger.info("receive dummy x-www-urlencoded-form data: {}", dummy.toString());
        return SuccessTip.create(dummy);
    }

    @ApiOperation(value = "dummy post file api")
    @PostMapping( value = "/dummy/upload/{path}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Tip doPostFileDummy(@RequestPart("file") MultipartFile file,
                               @PathVariable(value = "path", required = false) String path) throws IOException {
        Assert.isTrue(!ObjectUtils.isEmpty(file), "file should not be null");
        
        Long fileSize = file.getSize();
        Assert.isTrue(file.getSize()>0, "file should not be empty");                          

        File uploadPath = new File(path);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        String originalFileName = file.getOriginalFilename();
        File uploadedFile = new File(String.join(File.separator, uploadPath.getAbsolutePath(), originalFileName));

        FileUtils.copyInputStreamToFile(file.getInputStream(), uploadedFile);

        logger.info("file uploaded to: {}", uploadedFile.getAbsolutePath());
        return SuccessTip.create(uploadedFile);
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
        endpoint = endpoint.replace(uriString, "");
        result.put("endpoint", endpoint);

        return SuccessTip.create(result);
    }
}
