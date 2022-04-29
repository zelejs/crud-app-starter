package com.jfeat.dev.connection.api;

import com.alibaba.fastjson.JSONObject;
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
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

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

    /**
     * 数据库查询
     * @param pattern
     * @param sign
     * @param format
     * @param sql
     * @param response
     * @return
     * @throws IOException
     */
    @GetMapping
    @ApiOperation(value = "数据库查询", response = SqlRequest.class)
    public Tip query(@RequestParam(name = "pattern", required = false) String pattern,
                     // 测试环境，注释签名设为false，测试完务必还原
                     @RequestParam(name = "sign", required = false) String sign,
                     @RequestParam(name = "format", required = false) String format,
            @RequestParam(name = "sql", required = false) String sql,HttpServletResponse response) throws IOException {
        // 测试环境，注释签名，测试完务必还原
        /*if (! SignatureKit.parseSignature(sign, key,ttl) ){
            return ErrorTip.create(9010,"身份验证错误");
        }*/
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter writer = new PrintWriter(response.getOutputStream());
        if(sql!=null) {
            // startsWith() 方法用于检测字符串是否以指定的前缀开始
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
                //获取所有的表名
                var list = queryTablesDao.queryAllTables();
                return SuccessTip.create(list);
                }
            }

        return SuccessTip.create();
    }

    @GetMapping("/schema")
    @ApiOperation(value = "执行数据库查询", response = SqlRequest.class)
    public void queryTableSchema(@RequestParam(name = "pattern", required = false) String pattern,
                                 // 测试环境，注释签名设为false，测试完务必还原
                                 @RequestParam(name = "sign", required = false) String sign,
                                 HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter writer = new PrintWriter(response.getOutputStream());
        // 测试环境，注释签名，测试完务必还原
        /*if (!SignatureKit.parseSignature(sign, key,ttl) ) {
            writer.println("身份信息错误");
            writer.flush();*/
        if (false){

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

    @GetMapping("/sqls")
    @ApiOperation(value = "获取便捷sql查询语句", response = SqlRequest.class)
    public void queryAllConvenientSql(@RequestParam(name = "pattern", required = false) String pattern,
                              @RequestParam(name = "query", required = false) String query,
                              HttpServletResponse response) throws IOException {
        // 响应内容类型
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter writer = new PrintWriter(response.getOutputStream());

        File file = new File("sqls.json");
        String text= FileUtils.readFileToString(file,"UTF-8");
        JSONObject jsonObject = JSONObject.parseObject(text);
        Iterator it = jsonObject.keySet().iterator();
        boolean flag = true;
        if(query != null){
            while (it.hasNext()) {
                Object name = it.next();
                if(name.equals(query)) {
                    flag=false;
                    Object value = jsonObject.get(name);
                    var str = tableServer.show(value.toString());
                    if(str == null){
                        throw new BusinessException(BusinessCode.BadRequest,"value值查询错误，请检查查询语句");
                    }
                    for(String st:str) {
                        writer.println(st);
                    }
                }

            }
            if(flag) {
                throw new BusinessException(BusinessCode.BadRequest,"没有找到此query名");
            }
        }else {
            if (pattern != null) {
                while (it.hasNext()) {
                    Object name = it.next();
                    if (pattern.equals(name)) {
                        writer.println(jsonObject.get(name));
                    }
                }
            } else {
                while (it.hasNext()) {
                    Object name = it.next();
                    Object value = jsonObject.get(name);
                    writer.println(name + "：" + value);
                }
            }
        }
        writer.flush();
    }

    @GetMapping("/print")
    @ApiOperation(value = "执行数据库查询", response = SqlRequest.class)
    public void queryAllTable(@RequestParam(name = "filter", required = false) String filter,
                              // 测试环境，注释签名设为false，测试完务必还原
                              @RequestParam(name = "sign", required = false) String sign,
                              HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter writer = new PrintWriter(response.getOutputStream());
        // 测试环境，注释签名，测试完务必还原
        /*if (!SignatureKit.parseSignature(sign, key,ttl) ) {
            writer.println("身份信息错误");
            writer.flush();*/
        if (false){

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
                    // 测试环境，注释签名设为false，测试完务必还原
                    @RequestParam(name = "sign", required = false) String sign,
                    HttpServletResponse response) throws IOException {
        // 测试环境，注释签名，测试完务必还原
        /*if (!SignatureKit.parseSignature(sign, key,ttl) ) {
            return ErrorTip.create(9010,"身份验证错误");
        }*/
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

    /**
     * 按照规则下载数据库数据
     * @param sign 签名
     * @param rule full
     * @param ruler 规则文件名，不带后缀
     * @param response 响应
     * @return
     * @throws IOException
     */
    @GetMapping("/snapshot/instant")
    @ApiOperation(value = "按照规则下载数据库数据", response = SqlRequest.class)
                                    // 测试环境，设为false，测试完务必设回true
    public Tip rulers(@RequestParam(name = "sign", required = false) String sign,
                      @RequestParam(name = "rule", defaultValue = "defined") String rule,
                      @RequestParam(name = "ruler",required = true) String ruler,
                    HttpServletResponse response) throws IOException {
        // 测试环境，注释签名，测试完务必还原
        /*if (!SignatureKit.parseSignature(sign, key, ttl)) {
            return ErrorTip.create(9010, "身份验证错误");
        }*/
        response.setContentType("application/octet-stream");
        response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
        // 获取当前类所在根路径
        String projectPath = new File("").getAbsolutePath();
        //获取文件夹位置
        File[] files = tableServer.getAllFile();
        //判断是否有想要获取的ruler文件
        boolean flag = false;
        for (File file : files) {
            String a = file.getName();
            if (file.isDirectory()) continue;
            if (a.equals(ruler + ".ruler")) {
                flag = true;
            }
        }
        //有就调用规则，没有就返回无
        if (flag) {
            String content = "";
            StringBuilder builder = new StringBuilder();
            //把内容写入builder参数
            String fileName = projectPath + "/.rulers/" + ruler + ".ruler";
            File rulerFile = new File(fileName);
            String text = FileUtils.readFileToString(rulerFile, "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(text);
            Iterator it = jsonObject.keySet().iterator();
            List<String> file = new ArrayList<String>();
            List<String> sqlList = new ArrayList<>();
            List<String> nameList = new ArrayList<>();
            // 循环取出下载规则内容
            while (it.hasNext()) {
                // 获得表名
                Object name = it.next();
                // 获取所有的表名
                List<String> allTableName = queryTablesDao.queryAllTables();
                logger.info(allTableName.toString());
                // 判断库中是否存在该表，不存在则跳过
                if (!allTableName.contains(name.toString())) continue;
                nameList.add(name.toString());

                response.setContentType("application/octet-stream");
                response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
                String createSql = "show create table " + name;
                sqlList.add(createSql);
                // 根据name获取value
                Object value = jsonObject.get(name);
                String strValue = value.toString().replace("[", "").replace("]", "").replace("\"","");
                if (value == null) {
                    continue;
                } else {
                    if(strValue.contains("select") || strValue.contains("SELECT")){
                        sqlList.add(strValue);
                    }else {
                        if (strValue.contains(",")) {
                            if (strValue.contains("-")) {
                                var table1 = strValue.split(",");
                                for (int k = 0; k < table1.length; k++) {
                                    var table2 = table1[k].split("-");
                                    var limit = Integer.parseInt(table2[1].trim());
                                    String insertSql = "SELECT * FROM " + name + " limit " + (Integer.parseInt(table2[0]) - 1) + "," + limit + ";";
                                    sqlList.add(insertSql);
                                }
                            } else {
                                var table1 = strValue.split(",");
                                for (int k = 0; k < table1.length; k++) {
                                    String insertSql = "SELECT * FROM " + name + " limit " + table1[k] + ",1;";
                                    sqlList.add(insertSql);
                                }
                            }
                        } else if (strValue.contains("-")) {
                            var table1 = strValue.split(",");
                            for (int k = 0; k < table1.length; k++) {
                                var table2 = table1[k].split("-");
                                var limit = Integer.parseInt(table2[1].trim());
                                String insertSql = "SELECT * FROM " + name + " limit " + (Integer.parseInt(table2[0]) - 1) + "," + limit + ";";
                                sqlList.add(insertSql);
                            }
                        } else {
                            if (strValue.contains("*")) {
                                String insertSql = "SELECT * FROM " + name + ";";
                                sqlList.add(insertSql);
                            } else {
                                String insertSql = "SELECT * FROM " + name + " limit " + strValue + ",1;";
                                sqlList.add(insertSql);
                            }
                        }
                    }
                }
            }


                if (rule.equals("full")) {
                    var list = queryTablesDao.queryAllTables();
                    for (String tableName : list) {
                        int exflag = 0;
                        for (String basicName : nameList) {
                            if (tableName.equals(basicName)) {
                                exflag = 1;
                            }
                        }
                        if (exflag == 0) {
                            String insertSql = "SELECT * FROM " + tableName;
                            sqlList.add(insertSql);
                        }
                    }
                }
                for (String sql : sqlList) {
                    if (sql.contains("show")){
                        var list = tableServer.handleResult(sql);
                            file.add(list.get(1) + ";\n");
                    }else {
                        var test = tableServer.handleResult2(sql);
                        for (String st : test) {
                            file.add(st + "\n");
                        }
                    }
                }
            var data = tableServer.changToByte(file);
            response.setHeader(CONTENT_DISPOSITION, "attachment; filename=" + rulerFile.getName() + ".sql");
            IOUtils.write(data, response.getOutputStream());
            return null;
        }else{
            return ErrorTip.create(200,"没有该规则");
        }

    }

    /**
     * Post请求方式保存文件到本地
     * @param sign 签名
     * @param rule full
     * @param ruler 规则文件名（不带后缀）
     * @return
     * @throws IOException
     */
    @PostMapping("/snapshot")
    @ApiOperation(value = "执行数据库查询", response = SqlRequest.class)
                                // 测试环境，注释签名设为false，测试完务必还原
    public Tip saveFileToLocal(@RequestParam(name = "sign", required = false) String sign,
                               @RequestParam(name = "rule", defaultValue = "defined") String rule,
                               @RequestParam(name = "ruler", required = false) String ruler) throws IOException {
        // 测试环境，注释签名，测试完务必还原
        /*if (!SignatureKit.parseSignature(sign, key, ttl)) {
            return ErrorTip.create(9010, "身份验证错误");
        }*/
        // 获取当前类所在根路径
        String projectPath = new File("").getAbsolutePath();
        //获取文件夹位置
        File[] files = tableServer.getAllFile();
        //判断是否有想要获取的ruler文件
        boolean flag = false;
        for (File file : files) {
            String a = file.getName();
            if (a.equals(ruler + ".ruler")) {
                flag = true;
            }
        }
        //有就调用规则，没有就返回无
        if (flag) {
            String fileName = projectPath + "/.rulers/" + ruler + ".ruler";
            File rulerFile = new File(fileName);
            // 将指定文件读取出来以String显示，”utf-8“设置写入时的编码格式
            String text = FileUtils.readFileToString(rulerFile, "UTF-8");
            // 转换为jsonObject对象
            JSONObject jsonObject = JSONObject.parseObject(text);
            // 获得jsonObject.keySet()的迭代器
            Iterator it = jsonObject.keySet().iterator();
            // 数据列表
            List<String> file = new ArrayList<>();
            // sql列表
            List<String> sqlList = new ArrayList<>();
            List<String> nameList = new ArrayList<>();
            while (it.hasNext()) {
                // 获得表名
                Object name = it.next();
                // 获得库中的所有表名
                List<String> allTableName = queryTablesDao.queryAllTables();
                // 判断库中是否存在该表，不存在则跳过
                nameList.add(name.toString());
                if (!allTableName.contains(name.toString())) continue;
                // 通过表名获得value
                Object value = jsonObject.get(name);
                // 拼接sql语句，获得建表语句
                String createSql = "show create table " + name;
                sqlList.add(createSql);
                String strValue = value.toString().replace("[", "").replace("]", "").replace("\"","");
                if (value == null) {
                    continue;
                } else {
                    if(strValue.contains("select") || strValue.contains("SELECT")){
                        sqlList.add(strValue);
                    }else {
                        if (strValue.contains(",")) {
                            if (strValue.contains("-")) {
                                var table1 = strValue.split(",");
                                for (int k = 0; k < table1.length; k++) {
                                    var table2 = table1[k].split("-");
                                    var limit = Integer.parseInt(table2[1].trim());
                                    String insertSql = "SELECT * FROM " + name + " limit " + (Integer.parseInt(table2[0]) - 1) + "," + limit + ";";
                                    sqlList.add(insertSql);
                                }
                            } else {
                                var table1 = strValue.split(",");
                                for (int k = 0; k < table1.length; k++) {
                                    String insertSql = "SELECT * FROM " + name + " limit " + table1[k] + ",1;";
                                    sqlList.add(insertSql);
                                }
                            }
                        } else if (strValue.contains("-")) {
                            var table1 = strValue.split(",");
                            for (int k = 0; k < table1.length; k++) {
                                var table2 = table1[k].split("-");
                                var limit = Integer.parseInt(table2[1].trim());
                                String insertSql = "SELECT * FROM " + name + " limit " + (Integer.parseInt(table2[0]) - 1) + "," + limit + ";";
                                sqlList.add(insertSql);
                            }
                        } else {
                            if (strValue.contains("*")) {
                                String insertSql = "SELECT * FROM " + name + ";";
                                sqlList.add(insertSql);
                            } else {
                                String insertSql = "SELECT * FROM " + name + " limit " + strValue + ",1;";
                                sqlList.add(insertSql);
                            }
                        }
                    }
                }
            }


           if (rule.equals("full")) {
                var list = queryTablesDao.queryAllTables();
                for (String tableName : list) {
                    int exflag = 0;
                    for (String basicName : nameList) {
                        if (tableName.equals(basicName)) {
                            exflag = 1;
                        }
                    }
                    if (exflag == 0) {
                        String insertSql = "SELECT * FROM " + tableName;
                        sqlList.add(insertSql);
                    }
                }
            }
            for (String sql : sqlList) {
                if (sql.contains("show")){
                    var list = tableServer.handleResult(sql);
                        file.add(list.get(1) + ";\n");
                }else {
                    var test = tableServer.handleResult2(sql);
                    for (String st : test) {
                        file.add(st + "\n");
                    }
                }
            }

            // 创建文件夹对象
            File fileDir = new File(".dbsnapshot");
            // 如果文件夹不存在就创建
            if (!fileDir.exists()){
                fileDir.mkdir();
            }
            // 创建文件对象
            String everyDate = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
            File everyDaydata = new File(".dbsnapshot/" + everyDate +".sql");
            // 写入数据
            try (FileWriter fileWriter = new FileWriter(everyDaydata,false)){
                for (String adata : file){
                    fileWriter.write(adata);
                }
                fileWriter.flush();
            }catch (Exception e){
                e.printStackTrace();
            }
            return SuccessTip.create();
        }else{
            return ErrorTip.create(200,"没有该规则");
        }

    }

    /**
     * 显示所有的数据库快照保存文件
     * @param sign:签名
     * @return
     * @throws IOException
     */
    @GetMapping("/snapshot")
    @ApiOperation(value = "显示所有的数据库快照保存文件", response = SqlRequest.class)
                                            // 方便测试，将sign设为false，测试结束请设回true
    public Tip allDbSnapshot(@RequestParam(name = "sign",required = false) String sign) throws IOException {
        // 方便测试注释sign验证，测试结束请还原
        /*if (!SignatureKit.parseSignature(sign, key, ttl)) {
            return ErrorTip.create(9010, "身份验证错误");
        }*/
        List<String> dbFileNameList = new ArrayList<>();
        // 获取目录对象
        File fileDir = new File(".dbsnapshot");
        // 如果目录不存在就创建
        if (!fileDir.exists()) fileDir.mkdir();
        // 获取目录中的文件列表
        File[] dbFileList = fileDir.listFiles();
        for (File file : dbFileList) {
            dbFileNameList.add(file.getName());
        }
        return SuccessTip.create(dbFileNameList);
    }
    @GetMapping("snapshot/dl")
    @ApiOperation(value = "下载dbsnapshot本地文件",response = SqlRequest.class)
                                    // 测试环境，注释签名设为false，测试完务必还原
    public Tip downloadSnapshotFile(@RequestParam(value = "sign",required = false) String sign,
                                    @RequestParam(value = "pattern",required = true) String pattern,
                                    HttpServletResponse response) throws IOException {
        OutputStream outputStream = response.getOutputStream();
        // 验证签名
        // 测试环境，注释签名，测试完务必还原
        /*if (!SignatureKit.parseSignature(sign, key, ttl)) {
            return ErrorTip.create(9010, "身份验证错误");
        }*/
        // 创建.dbsnapshot目录下的pattern文件对象
        File snapshotFile = new File(".dbsnapshot/" + pattern);
        if (!snapshotFile.exists()) return ErrorTip.create(200,"文件不存在");
        // 读取文件内容
        String aline = null;
        List<String> contentList = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(snapshotFile)))){
            while ((aline = br.readLine()) != null){
                contentList.add(aline+"\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        // contentList掉开头和结尾的 "[","]"并转换成字节数组
        byte[] contentByte = contentList.toString().replace("[","").replace("]","").getBytes(StandardCharsets.UTF_8);
        // 设置响应头，告诉浏览器以附件的形式下载
        response.setHeader("Content-Disposition","attachment;filename="+pattern);
        // 通过流输出给浏览器
        outputStream.write(contentByte);
        outputStream.flush();
        return null;
    }

    /**
     * 返回规则文件列表
     * @return
     * @throws IOException
     */
    @GetMapping("/snapshot/rulers")
    @ApiOperation(value = "获取规则文件列表", response = SqlRequest.class)
                        // 测试环境，注释签名设为false，测试完务必还原
    public Tip allRulers(@RequestParam(name = "sign",required = false) String sign) throws IOException {
        // 验证签名
        // 测试环境，注释签名，测试完务必还原
        /*if (!SignatureKit.parseSignature(sign, key, ttl)) {
            return ErrorTip.create(9010, "身份验证错误");
        }*/
        // 创建数组，接收文件名称
        List<String> rulersList = new ArrayList<>();
        //获取文件夹位置
        File[] files = tableServer.getAllFile();

        for (File file : files) {
            if (file.isDirectory()) continue;
            rulersList.add(file.getName());
        }
        return SuccessTip.create(rulersList);
    }

    /**
     * 查看具体的命名规则的配置详情
     * @param ruler_file_name:要查看的规则名
     * @param sign:签名
     * @param response:响应
     * @return
     * @throws IOException
     */
    @GetMapping("/snapshot/rulers/{ruler_file_name}")
    @ApiOperation(value = "查看具体的命名规则的配置详情", response = SqlRequest.class)
    public Tip rulerInfo(@PathVariable String ruler_file_name,
                         // 测试环境，注释签名设为false，测试完务必还原
                         @RequestParam(name = "sign",required = false) String sign,
                         HttpServletResponse response) throws IOException {
        // 验证签名
        // 测试环境，注释签名，测试完务必还原
        /*if (!SignatureKit.parseSignature(sign, key, ttl)) {
            return ErrorTip.create(9010, "身份验证错误");
        }*/
        PrintWriter writer = new PrintWriter(response.getOutputStream());
        //获取项目根路径
        String projectPath = new File("").getAbsolutePath();
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
            String fileName = projectPath + "/.rulers/" + ruler_file_name+".ruler";
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

    /**
     * 更新/新建规则文件
     * @param ruler_file_name:规则文件名
     * @param jsonObject:json格式的规则内容
     * @param sign:签名
     * @param request:请求
     * @param response:响应
     * @return
     * @throws IOException
     */
    @PostMapping("/snapshot/rulers/{ruler_file_name:.*}")
    @ApiOperation(value = "更新/新建规则文件", response = SqlRequest.class)
    public Tip rulerInfo(@PathVariable String ruler_file_name,
                         @RequestBody JSONObject jsonObject,
                         // 测试环境，注释签名设为false，测试完务必还原
                         @RequestParam(name = "sign",required = false) String sign,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        // 验证签名
        // 测试环境，注释签名，测试完务必还原
        /*if (!SignatureKit.parseSignature(sign, key, ttl)) {
            return ErrorTip.create(9010, "身份验证错误");
        }*/
        // 获取项目当前路径
        String projectPath = new File("").getAbsolutePath();
        //判断文件名是否带有后缀
        if (ruler_file_name.lastIndexOf(".") != -1){
            // 判断后缀名是否为 “.ruler" 是的话进行报错
                Assert.isTrue(ruler_file_name.substring(ruler_file_name.lastIndexOf(".")).equals(".ruler"),"暂不支持ruler以外的文件格式");

                //获取文件夹位置
            File fileDir = new File(".rulers");
            // 判断文件夹是否存在，不存在则创建
            if(!fileDir.exists()){
                fileDir.mkdirs();
            }
            // 创建文件对象
            File checkFile = new File(projectPath + "/.rulers/" + ruler_file_name);
            // 创建文件字符流输出流,new FileWriter(checkFile,true),如果给出true参数则是在文件原来的内容后面添加，false则是覆盖
                try (FileWriter writer = new FileWriter(checkFile,false)){

                    // 调整json排版
                    String date = jsonObject.toString().replace("],", "],\n").replace("{", "{\n").replace("}", "\n}");
                    // 写入数据
                    writer.append(date);
                    writer.flush();
                }catch (Exception e){
                    e.printStackTrace();
                }
        }else {
        // ruler_file_name 没有后缀名的情况
        // 获取文件夹位置
        File fileDir = new File(".rulers");
        // 判断文件夹是否存在，不存在则创建
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }
            // 创建文件对象
            File checkFile = new File(projectPath + "/.rulers/" + ruler_file_name + ".ruler");
        // 创建文件输出流,new FileWriter(checkFile,true),如果给出true参数则是在文件原来的内容后面添加，false则是覆盖
            try (FileWriter writer = new FileWriter(checkFile,false);){

                // 调整json排版
                String data = jsonObject.toString().replace("],", "],\n").replace("{", "{\n").replace("}", "\n}");
                // 向文件写入数据
                writer.append(data);
                writer.flush();
            }catch (IOException e){
                e.printStackTrace();
            }
    }
        return SuccessTip.create();
    }

    /**
     * 删除规则
     * @param sign:签名
     * @param ruler_file_name:规则名
     * @return
     */
    @DeleteMapping("/snapshot/rulers/{ruler_file_name}")
    @ApiOperation(value = "删除规则", response = SqlRequest.class)
    public Tip rulerInfo(@PathVariable String ruler_file_name,
                         // 测试环境，注释签名设为false，测试完务必还原
                         @RequestParam(name = "sign",required = false) String sign){
        // 验证签名
        // 测试环境，注释签名，测试完务必还原
        /*if (!SignatureKit.parseSignature(sign, key, ttl)) {
            return ErrorTip.create(9010, "身份验证错误");
        }*/
        //获取项目根路径
        String projectPath = new File("").getAbsolutePath();
        // 拼接文件路径
        String filePath = projectPath + "/.rulers/"+ruler_file_name+".ruler";
        File file = new File(filePath);
        if(file.delete()){
            return SuccessTip.create("删除成功");
        }else {
            return SuccessTip.create("删除失败");
        }
    }


}
