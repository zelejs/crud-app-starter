package com.jfeat.module.dev.api;

import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.module.dev.model.Dummy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

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
}
