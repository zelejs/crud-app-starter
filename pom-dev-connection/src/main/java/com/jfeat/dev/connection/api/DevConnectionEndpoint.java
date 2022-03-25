package com.jfeat.dev.connection.api;

import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.base.tips.ErrorTip;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
//import com.jfeat.dev.connection.api.request.ForeignKeyRequest;
import com.jfeat.dev.connection.services.domain.dao.QueryTablesDao;
import com.jfeat.dev.connection.services.domain.service.TableServer;
//import com.jfeat.dev.connection.util.DataSourceUtil;
import com.jfeat.signature.SignatureKit;
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
    private final String SCHEDULE = "schema";
    private static final Long ttl = 600000L;
    private static final String key = "514528";
    @Autowired
    DataSource dataSource;

    @Resource
    QueryTablesDao queryTablesDao;

    @Resource
    TableServer tableServer;

    @GetMapping
    @ApiOperation(value = "执行数据库查询", response = SqlRequest.class)
    public Tip query(@RequestParam(name = "pattern", required = false) String pattern,
                     @RequestParam(name = "sign", required = true) String sign,
                     @RequestParam(name = "format", required = false) String format,
            @RequestParam(name = "sql", required = false) String sql,HttpServletResponse response) throws IOException {
        if (! SignatureKit.parseSignature(sign, key,ttl) ){
            return ErrorTip.create(9010,"身份验证错误");
        }
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter writer = new PrintWriter(response.getOutputStream());
        if(sql!=null) {
            if(sql.startsWith("SELECT") || sql.startsWith("select")){
                if(format!=null && format.equals("md")) {
                    var test = tableServer.showMd(sql);
                    for (String st : test) {writer.print(st+"\n");}
                }else{
                    var test = tableServer.show(sql);
                    for (String st : test) {writer.print(st+"\n");}
                }
                writer.flush();
            }else if(sql.startsWith("SHOW") || sql.startsWith("show")) {
                if (sql.startsWith("SHOW CREATE") || sql.startsWith("show create") || sql.startsWith("SHOW create") || sql.startsWith("show Create")){
                    if(format!=null && format.equals("md")) {
                        var test = tableServer.showMd(sql);
                        for (String st : test) {writer.print(st+"\n");}
                    }else{
                        var test = tableServer.handleResult(sql);
                        for (String st : test) {writer.print(st+"\n");}
                    }
                writer.flush();
                }else{
                    if(format!=null && format.equals("md")) {
                        var test = tableServer.showMd(sql);
                        for (String st : test) {
                            st = st.replace("'","");
                            writer.print(st+"\n");
                        }
                    }else{
                        var test = tableServer.show(sql);
                        for (String st : test) {
                            st = st.replace("'","");
                            writer.print(st+"\n");
                        }
                    }
                writer.flush();
                }
            }else{
                throw new BusinessException(BusinessCode.BadRequest,"请输入正确的SELECT查询语句");
            }
        }else{
            if (pattern != null) {
                String dropSql = "DROP TABLE IF EXISTS " + pattern + ";";
                writer.println(dropSql);
                String createSql = "show create table " + pattern;
                var str = tableServer.handleResult(createSql);
                var createStr = str.get(1).replace("'","");
                writer.print(createStr+";\n");
                String insertSql = "SELECT * FROM " + pattern;
                var test = tableServer.handleResult2(insertSql);
                for (String st : test) {
                    writer.println(st);
                }
                writer.flush();
            } else {
                var list = queryTablesDao.queryAllTables();
                for (String tableName : list) {
                    writer.println(tableName);
                }
                writer.flush();
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
    public void queryTableSchema(@RequestParam(name = "pattern", required = false) String pattern,
                                 @RequestParam(name = "sign", required = true) String sign,
                                 HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter writer = new PrintWriter(response.getOutputStream());
        if (!SignatureKit.parseSignature(sign, key,ttl) ) {
            writer.println("身份信息错误");
            writer.flush();
        }else {
            if (pattern != null) {
                String dropSql = "DROP TABLE IF EXISTS " + pattern + ";";
                writer.println(dropSql);
                String createSql = "show create table " + pattern;
                var str = tableServer.handleResult(createSql) + ";";
                writer.println(str);
                writer.flush();
            } else {
                var list = queryTablesDao.queryAllTables();
                for (String tableName : list) {
                    String dropSql = "DROP TABLE IF EXISTS " + tableName + ";";
                    writer.println(dropSql);
                    String createSql = "show create table " + tableName;
                    var str = tableServer.handleResult(createSql) + ";";
                    writer.println(str);
                    writer.flush();
                }
            }
        }
    }

    @GetMapping("/print")
    @ApiOperation(value = "执行数据库查询", response = SqlRequest.class)
    public void queryAllTable(@RequestParam(name = "filter", required = false) String filter,
                              @RequestParam(name = "sign", required = true) String sign,
                              HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter writer = new PrintWriter(response.getOutputStream());
        if (!SignatureKit.parseSignature(sign, key,ttl) ) {
            writer.println("身份信息错误");
            writer.flush();
        }else {
            var list = queryTablesDao.queryAllTables();
            if (filter != null && filter.equals(SCHEDULE)) {
                for (String tableName : list) {
                    String dropSql = "DROP TABLE IF EXISTS " + tableName + ";";
                    writer.println(dropSql);
                    String createSql = "show create table " + tableName;
                    var str = tableServer.handleResult(createSql) + ";";
                    writer.println(str);
                    writer.flush();
                }
            } else {
                for (String tableName : list) {
                    String dropSql = "DROP TABLE IF EXISTS " + tableName + ";";
                    writer.println(dropSql);
                    String createSql = "show create table " + tableName;
                    var str = tableServer.handleResult(createSql) + ";";
                    writer.println(str);
                    String insertSql = "SELECT * FROM " + tableName;
                    var test = tableServer.handleResult2(insertSql);
                    for (String st : test) {
                        writer.println(st);
                    }
                    writer.flush();
                }
            }
        }
    }

    @GetMapping("/sql")
    @ApiOperation(value = "执行数据库查询", response = SqlRequest.class)
    public Tip down(@RequestParam(name = "filter", required = false) String filter,
                    @RequestParam(name = "sign", required = true) String sign,
                    HttpServletResponse response) throws IOException {
        if (!SignatureKit.parseSignature(sign, key,ttl) ) {
            return ErrorTip.create(9010,"身份验证错误");
        }
        response.setContentType("application/octet-stream");
        response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
        var dataBase = queryTablesDao.queryDataBase();
//        PrintWriter writer = new PrintWriter(response.getOutputStream());
        var list = queryTablesDao.queryAllTables();
        List<String> file = new ArrayList<String>();
        file.add("SET FOREIGN_KEY_CHECKS = 0;");
        if(filter!=null && filter.equals(SCHEDULE)){
            for (String tableName : list) {
                String dropSql = "\nDROP TABLE IF EXISTS " +tableName +";\n";
                file.add(dropSql);
                String createSql = "show create table " + tableName;
                var str = tableServer.handleResult(createSql) + ";\n\n";
                file.add(str);
            }
        }else {
            for (String tableName : list) {
                String dropSql = "\nDROP TABLE IF EXISTS " + tableName + ";\n";
                file.add(dropSql);
                String createSql = "show create table " + tableName;
                var str = tableServer.handleResult(createSql) + ";\n\n";
                file.add(str);
                String insertSql = "SELECT * FROM " + tableName;
                var test = tableServer.handleResult2(insertSql);
                for (String st : test) {
                    file.add(st + "\n");
                }
            }
        }
        file.add("SET FOREIGN_KEY_CHECKS = 1;");
        var data = tableServer.changToByte(file);
        response.setHeader(CONTENT_DISPOSITION,"attachment; filename="+dataBase+".sql");
        IOUtils.write(data,response.getOutputStream());
//        writer.flush();
        return null;
    }
}
