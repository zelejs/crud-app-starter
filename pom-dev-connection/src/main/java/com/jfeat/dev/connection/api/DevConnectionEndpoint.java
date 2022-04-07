package com.jfeat.dev.connection.api;

import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.base.tips.ErrorTip;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
//import com.jfeat.dev.connection.api.request.ForeignKeyRequest;
import com.jfeat.dev.connection.services.domain.dao.QueryTablesDao;
import com.jfeat.dev.connection.services.domain.model.Ruler;
import com.jfeat.dev.connection.services.domain.model.RulerRequest;
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
import java.io.*;
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
                        writer.print(test.get(1)+";");
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
                var str = tableServer.handleResult(createSql);
                writer.println(str.get(1)+";");
                writer.flush();
            } else {
                var list = queryTablesDao.queryAllTables();
                for (String tableName : list) {
                    String dropSql = "DROP TABLE IF EXISTS " + tableName + ";";
                    writer.println(dropSql);
                    String createSql = "show create table " + tableName;
                    var str = tableServer.handleResult(createSql);
                    writer.println(str.get(1)+";");
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
                    var str = tableServer.handleResult(createSql);
                    writer.println(str.get(1)+";");
                    for(String st:str) {
                        writer.println(st);
                    }
                    writer.flush();
                }
            } else {
                for (String tableName : list) {
                    String dropSql = "DROP TABLE IF EXISTS " + tableName + ";";
                    writer.println(dropSql);
                    String createSql = "show create table " + tableName;
                    var str = tableServer.handleResult(createSql);
                    writer.println(str.get(1)+";");
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
                var str = tableServer.handleResult(createSql);
                file.add(str.get(1)+";");
            }
        }else {
            for (String tableName : list) {
                String dropSql = "\nDROP TABLE IF EXISTS " + tableName + ";\n";
                file.add(dropSql);
                String createSql = "show create table " + tableName;
                var str = tableServer.handleResult(createSql);
                file.add(str.get(1)+";");
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

    @GetMapping("/snapshot")
    @ApiOperation(value = "执行数据库查询", response = SqlRequest.class)
    public Tip rulers(@RequestParam(name = "filter", required = false) String filter,
                    @RequestParam(name = "sign", required = true) String sign,
                    @RequestParam(name = "rule", required = false) String rule,
                      @RequestParam(name = "ruler", required = false) String ruler,
                    HttpServletResponse response) throws IOException {
        PrintWriter writer = new PrintWriter(response.getOutputStream());
        response.setContentType("application/octet-stream");
        response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
//        this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()+"\\ruler";
        //获取文件夹位置
        File[] files = tableServer.getAllFile();

        //判断是否有想要获取的ruler文件
        boolean flag = false;
        for (File file : files) {
            String a =file.getName();
            if (file.isDirectory()) continue;
            if (a.equals(ruler+".ruler")) {
                flag = true;
            }
        }
        //有就调用规则，没有就返回无
        if(flag) {
            String content = "";
            StringBuilder builder = new StringBuilder();
            //把内容写入builder参数
            String fileName = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "ruler/" + ruler+".ruler";
            File rulerFile = new File(fileName);
            InputStreamReader streamReader = new InputStreamReader(new FileInputStream(rulerFile), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(streamReader);

            while ((content = bufferedReader.readLine()) != null)
                builder.append(content);

            String value = builder.toString();

            //拆分识别内容
            var rulerArray = value.split("],");
            List<String> file = new ArrayList<String>();
            List<String> sqlList = new ArrayList<>();
            List<String> nameList = new ArrayList<>();
            List<String> limitList = new ArrayList<>();
            if(rule.equals("defined")) {
                for (int i = 0; i < rulerArray.length; i++) {
                    var tableArray = rulerArray[i].replace("\"", "")
                            .replace("[", "").replace("]", "").replace("/r", "").replace("/n", "").trim()
                            .split(":");
                    nameList.add(tableArray[0]);
                    limitList.add(tableArray[1]);
                }
                response.setContentType("application/octet-stream");
                response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
                for (int j = 0; j < nameList.size(); j++) {
                    if (limitList.get(j).contains(",")) {
                        if (limitList.get(j).contains("-")) {
                            var table1 = limitList.get(j).split(",");
                            for (int k = 0; k < table1.length; k++) {
                                var table2 = table1[k].split("-");
                                var limit = Integer.parseInt(table2[1].trim()) - Integer.parseInt(table2[0].trim());
                                String insertSql = "SELECT * FROM " + nameList.get(j) + " limit " + table2[0] + "," + limit + ";";
                                sqlList.add(insertSql);
                            }
                        } else {
                            var table1 = limitList.get(j).split(",");
                            for (int k = 0; k < table1.length; k++) {
                                String insertSql = "SELECT * FROM " + nameList.get(j) + " limit " + table1[k] + ",1;";
                                sqlList.add(insertSql);
                            }
                        }
                    } else {
                        if (limitList.get(j).contains("*")) {
                            String insertSql = "SELECT * FROM " + nameList.get(j) + ";";
                            sqlList.add(insertSql);
                        } else {
                            String insertSql = "SELECT * FROM " + nameList.get(j) + " limit " + limitList.get(j) + ",1;";
                            sqlList.add(insertSql);
                        }
                    }
                }
                for (String sql : sqlList) {
                    var test = tableServer.handleResult2(sql);
                    for (String st : test) {
                        file.add(st + "\n");
                    }
                }
            }else if(rule.equals("full")){
                for (int i = 0; i < rulerArray.length; i++) {
                    var tableArray = rulerArray[i].replace("\"", "")
                            .replace("[", "").replace("]", "").replace("/r", "").replace("/n", "").trim()
                            .split(":");
                    nameList.add(tableArray[0]);
                    limitList.add(tableArray[1]);
                }
                for (String tableName : nameList) {
                    String sql = "SELECT * FROM " + tableName;
                    var test = tableServer.handleResult2(sql);
                    for (String st : test) {
                        file.add(st + "\n");
                    }
                }
            }
            var data = tableServer.changToByte(file);
            response.setHeader(CONTENT_DISPOSITION, "attachment; filename=" + "a" + ".sql");
            IOUtils.write(data, response.getOutputStream());
            return null;
        }else{
            return null;
        }
    }

    @GetMapping("/snapshot/rulers")
    @ApiOperation(value = "获取所有规则", response = SqlRequest.class)
    public Tip allRulers(HttpServletResponse response) throws IOException {
        PrintWriter writer = new PrintWriter(response.getOutputStream());
//        this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()+"\\ruler";
        //获取文件夹位置
        File[] files = tableServer.getAllFile();

        for (File file : files) {
            if (file.isDirectory()) continue;
            writer.print(file.getName()+"\n");
            writer.flush();
        }
        return null;
    }

    @GetMapping("/snapshot/rulers/{ruler_file_name}")
    @ApiOperation(value = "查看具体的命名规则的配置详情", response = SqlRequest.class)
    public Tip rulerInfo(@PathVariable String ruler_file_name,
            HttpServletResponse response) throws IOException {
        PrintWriter writer = new PrintWriter(response.getOutputStream());
//        this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()+"\\ruler";
        //获取文件夹位置
        File[] files = tableServer.getAllFile();

        //判断是否有想要获取的ruler文件
        boolean flag = false;
        for (File file : files) {
            if (file.isDirectory()) continue;
            if (file.getName().equals(ruler_file_name+".ruler")){
                flag=true;
            }

        }
        if (flag){
            String content = "";
            StringBuilder builder = new StringBuilder();
            //把内容写入builder参数
            String fileName = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "ruler/" + ruler_file_name+".ruler";
            File rulerFile = new File(fileName);
            InputStreamReader streamReader = new InputStreamReader(new FileInputStream(rulerFile), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(streamReader);

            while ((content = bufferedReader.readLine()) != null)
                builder.append(content+"\n");

            String value = builder.toString();
            writer.print(value);
            writer.flush();
        }else{
            throw new BusinessException(BusinessCode.BadRequest,"没有找到此规则");
        }
        return null;
    }

    @PostMapping("/snapshot/rulers/{ruler_file_name}")
    @ApiOperation(value = "执行数据库查询", response = SqlRequest.class)
    public Tip rulerInfo(@PathVariable String ruler_file_name,
                         @RequestBody RulerRequest rulerRequest,
                         HttpServletResponse response) throws IOException {
//        this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()+"\\ruler";
        //获取文件夹位置
        File[] files = tableServer.getAllFile();

        boolean flag = true;
        for (File file : files) {
            if (file.isDirectory()) continue;
            if (file.getName().equals(ruler_file_name+".ruler")){
                flag=false;
            }
        }
        if(flag){
            FileWriter writer = null;
            File checkFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "ruler/" + ruler_file_name + ".ruler");
            checkFile.createNewFile();// 创建目标文件

            writer = new FileWriter(checkFile, true);
            writer.append(rulerRequest.getValue());
            writer.flush();
        }else{
            FileWriter writer = null;
            File checkFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "ruler/" + ruler_file_name + ".ruler");
            checkFile.canWrite();

            writer = new FileWriter(checkFile, true);
            writer.append(",\n"+rulerRequest.getValue());
            writer.flush();


        }
        return SuccessTip.create();
    }

    @DeleteMapping("/snapshot/rulers/{ruler_file_name}")
    @ApiOperation(value = "执行数据库查询", response = SqlRequest.class)
    public Tip rulerInfo(@PathVariable String ruler_file_name){
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "ruler/"+ruler_file_name+".ruler";
        File file = new File(path);
        file.deleteOnExit();
        return SuccessTip.create();
    }
}
