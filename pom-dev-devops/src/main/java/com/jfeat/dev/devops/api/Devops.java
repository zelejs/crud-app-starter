package com.jfeat.dev.devops.api;

import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
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
    ParseRequestArgument parseRequestArgument;

    @Resource
    ParseSql parseSql;

    @GetMapping("/{sqlFile}")
    public Tip getResultList(@PathVariable("sqlFile") String sqlFile, HttpServletRequest request){

//        获取参数键值对
        Map<String,String> map = parseRequestArgument.parseGetRequestArgument(request);
        String sql = parseSql.readSqlFile(sqlFile);
        if (sql==null || sql.equals("")){
            return SuccessTip.create();
        }

        return SuccessTip.create(parseSql.querySql(parseSql.sqlParameters(sql,map)));
    }

    @PostMapping("/{sqlFile}")
    public Tip executeSql(@PathVariable("sqlFile") String sqlFile,HttpServletRequest request){
        Map<String,String> map = parseRequestArgument.parseGetRequestArgument(request);
        String sql = parseSql.readSqlFile(sqlFile);
        if (sql==null || sql.equals("")){
            return SuccessTip.create();
        }
        return SuccessTip.create(parseSql.executeSql(parseSql.sqlParameters(sql,map)));
    }



}
