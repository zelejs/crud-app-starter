package com.jfeat.dev.connection.api;

import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.dev.connection.services.domain.dao.QueryTablesDao;
import com.jfeat.dev.connection.services.domain.service.TableServer;
import com.jfeat.dev.connection.util.DataSourceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * 依赖处理接口
 *
 * @author zxchengb
 * @date 2020-08-05
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.net.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    @Resource
    QueryTablesDao queryTablesDao;

    @Resource
    TableServer tableServer;

    @GetMapping
    @ApiOperation(value = "执行数据库查询", response = SqlRequest.class)
    public Tip query(HttpServletResponse response) throws IOException {
        var list = queryTablesDao.queryAllTables();
        PrintWriter writer = new PrintWriter(response.getOutputStream());
        List<String> file = new ArrayList<>();
        for (String tableName:list) {
//            var line = queryTablesDao.queryCreateTableSql(tableName);
//            writer.println(line);
            String sql = "show create table " +tableName;
            var str = tableServer.handleResult(sql)+";";
            file.add(str);
            writer.println(str);
            String sql1 = "SELECT * FROM " +tableName;
            var test = tableServer.handleResult2(sql1);
            for (String st:test) {
                writer.println(st);
                file.add(st);
            }
            writer.flush();
        }
        byte[] data = file.toString().getBytes(StandardCharsets.UTF_8);
//        response.setHeader(HttpHeaders.CONTENT_DISPOSITION);
        IOUtils.write(data,response.getOutputStream());
        return SuccessTip.create();
    }
}
