package com.jfeat.dev.connection.api;

import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
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
import org.w3c.dom.Text;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * 依赖处理接口
 *
 * @author zxchengb
 * @date 2020-08-05
 */
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.net.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;
import static com.google.common.net.HttpHeaders.CONTENT_DISPOSITION;

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
    public Tip query(@RequestParam(name = "pattern", required = false) String pattern,
            @RequestParam(name = "sql", required = false) String sql,HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter writer = new PrintWriter(response.getOutputStream());
        if(sql!=null) {
            var test = tableServer.handleResult2(sql);
            if(test.size()==0){
                throw new BusinessException(BusinessCode.BadRequest,"请输入正确的SELECT语句");
            }
            for (String st : test) {
                writer.println(st);
            }
            writer.flush();
        }else{
            if (pattern != null) {
                String dropSql = "DROP TABLE IF EXISTS " +pattern +";";
                writer.println(dropSql);
                String sql1 = "show create table " + pattern;
                var str = tableServer.handleResult(sql1) + ";";
                writer.println(str);
                String sql2 = "SELECT * FROM " + pattern;
                var test = tableServer.handleResult2(sql2);
                for (String st : test) {
                    writer.println(st);
                }
                writer.flush();
            } else {
                var list = queryTablesDao.queryAllTables();
                List<String> file = new ArrayList<>();
                for (String tableName : list) {
//            var line = queryTablesDao.queryCreateTableSql(tableName);
//            writer.println(line);
                    String dropSql = "DROP TABLE IF EXISTS " +tableName +";";
                    writer.println(dropSql);
                    String sql1 = "show create table " + tableName;
                    var str = tableServer.handleResult(sql1) + ";";
                    file.add(str);
                    writer.println(str);
                    String sql2 = "SELECT * FROM " + tableName;
                    var test = tableServer.handleResult2(sql2);
                    for (String st : test) {
                        writer.println(st);
                        file.add(st);
                    }
                    writer.flush();
                }
            }
        }
//        String[] strs1=file.toArray(new String[file.size()]);
//        byte[] bytes=new byte[strs1.length];
//        for (int i=0; i<strs1.length; i++) {
//            System.out.println(strs1[i]);
//            bytes[i]=Byte.parseByte(strs1[i]);
//            System.out.println(bytes[i]);
//        }

        return null;
    }

    @GetMapping("/schema")
    @ApiOperation(value = "执行数据库查询", response = SqlRequest.class)
    public void queryTableSchema(@RequestParam(name = "pattern", required = false) String pattern, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter writer = new PrintWriter(response.getOutputStream());
        if(pattern!=null) {
            String dropSql = "DROP TABLE IF EXISTS " +pattern +";";
            writer.println(dropSql);
            String sql = "show create table " + pattern;
            var str = tableServer.handleResult(sql) + ";";
            writer.println(str);
            writer.flush();
        }else{
            var list = queryTablesDao.queryAllTables();
            for (String tableName : list) {
                String dropSql = "DROP TABLE IF EXISTS " +tableName +";";
                writer.println(dropSql);
                String sql = "show create table " + tableName;
                var str = tableServer.handleResult(sql) + ";";
                writer.println(str);
                writer.flush();
            }
        }
    }

    @GetMapping("/tables")
    @ApiOperation(value = "执行数据库查询", response = SqlRequest.class)
    public void queryAllTable(@RequestParam(name = "pattern", required = false) String pattern,HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter writer = new PrintWriter(response.getOutputStream());
        var list = queryTablesDao.queryAllTables();
        for (String tableName : list) {
            writer.println(tableName);
        }
        writer.flush();
    }

    @GetMapping("/down")
    @ApiOperation(value = "执行数据库查询", response = SqlRequest.class)
    public Tip down(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
//        PrintWriter writer = new PrintWriter(response.getOutputStream());
        var list = queryTablesDao.queryAllTables();
        List<String> file = new ArrayList<String>();
        for (String tableName : list) {
            String dropSql = "DROP TABLE IF EXISTS " +tableName +";\n";
            file.add(dropSql);
            String sql1 = "show create table " + tableName;
            var str = tableServer.handleResult(sql1) + ";\n";
            file.add(str);
            String sql2 = "SELECT * FROM " + tableName;
            var test = tableServer.handleResult2(sql2);
            for (String st : test) {
                file.add(st+"\n");
            }
        }
        var data = tableServer.changToByte(file);
        response.setHeader(CONTENT_DISPOSITION,"attachment; filename="+"nft"+".sql");
        IOUtils.write(data,response.getOutputStream());
//        writer.flush();
        return null;
    }
}
