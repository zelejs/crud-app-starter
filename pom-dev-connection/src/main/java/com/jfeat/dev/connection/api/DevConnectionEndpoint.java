package com.jfeat.dev.connection.api;

import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.dev.connection.util.DataSourceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 查询数据库
 * @author vincent huang
 * @date 2022-01-20
 */
@RestController
@Api("dev-connection")
@RequestMapping("/dev/connection")
public class DevConnectionEndpoint {
    protected final static Logger logger = LoggerFactory.getLogger(DevConnectionEndpoint.class);

    @Autowired
    DataSource dataSource;

    @PostMapping("/query")
    @ApiOperation(value = "执行查询")
    public Tip querySql(@RequestBody(required = true) String sql) throws SQLException {
        var result = DataSourceUtil.querySQL(dataSource.getConnection(), sql);
        return SuccessTip.create(result);
    }
}
