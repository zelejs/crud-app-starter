package com.jfeat.dev.connection.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jfeat.crud.base.tips.*;

import javax.sql.DataSource;

/**
 * 依赖处理接口
 *
 * @author zxchengb
 * @date 2020-08-05
 */
@RestController
@Api("dependency-api")
@RequestMapping("/dev/connection")
public class DevConnectionEndpoint {
    protected final static Logger logger = LoggerFactory.getLogger(DevConnectionEndpoint.class);

    @Autowired
    DataSource dataSource;

    @PostMapping
    @ApiOperation(value = "执行数据库查询", response = SqlRequest.class)
    public Tip query(@RequestBody SqlRequest sql, @PathVariable("landId") Long landId) {

        return SuccessTip.create();
    }
}
