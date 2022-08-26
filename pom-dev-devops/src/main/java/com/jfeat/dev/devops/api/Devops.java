package com.jfeat.dev.devops.api;

import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.dev.devops.services.services.DevopsServices;
import com.jfeat.dev.devops.services.services.ParseRequestArgument;
import com.jfeat.dev.devops.services.services.ParseSql;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/dev/devops")
public class Devops {

   @Resource
    DevopsServices devopsServices;

    @GetMapping("/{sqlFile}")
    public Tip getResultList(@PathVariable("sqlFile") String sqlFile, HttpServletRequest request){
        return SuccessTip.create(devopsServices.querySql(request,sqlFile));
    }

    @PostMapping("/{sqlFile}")
    public Tip executeSql(@PathVariable("sqlFile") String sqlFile,HttpServletRequest request){
        return SuccessTip.create(devopsServices.executeSql(request,sqlFile));
    }



}
