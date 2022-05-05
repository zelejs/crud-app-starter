package com.jfeat.dev.logs.api;

//import com.jfeat.dev.connection.api.request.ForeignKeyRequest;
import com.alibaba.fastjson.JSONObject;
import com.jfeat.AmApplication;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
//import com.jfeat.dev.connection.util.DataSourceUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.*;
import java.lang.String;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.*;

/**
 * 查询数据库
 * @author vincent huang
 * @date 2022-01-20
 */
@RestController
@Api("dev-logs")
@RequestMapping("/dev/logs")
public class Logs {

    /**
     * 获取日志文件列表
     * @return list:日志文件列表
     * @throws IOException
     */
    private List<String> getLogFiles() throws IOException {
        List<String> list = new ArrayList<>();
        File fileDir = new File("logs");
        // 如果目录不存在，则创建目录
        if (!fileDir.exists()){
            fileDir.mkdirs();
        }
        String[] logFileList = fileDir.list();
        /*如果没有文件则返回空列表*/
        if (logFileList.length == 0) {
            return list;
        }
        // 遍历数组，得出每一个文件名
        for (String logFileName : logFileList) {
            list.add(logFileName);
        }
        return list;
    }

    /**
     * 读取gz压缩文件
     * @param zipFileName 压缩文件的文件名
     * @return 文件内容数据数组
     * @throws FileNotFoundException
     */
    private Map readGzipFile(String zipFileName){

        // 创建文件
        File gzipFile = new File(zipFileName);
        // 创建返回用的map
        Map<Integer,String> gzipFileMap = new HashMap<>();
        // 判断文件是否存在
        if (!gzipFile.exists()){
            return new HashMap<Integer,String>();
        }
        // 使用gzipInputStream解压文件
        try (InputStream in = new GZIPInputStream(new FileInputStream(gzipFile))){
            Scanner sc = new Scanner(in);
            int lineNum = 0;
            while (sc.hasNextLine()){
                lineNum++;
                gzipFileMap.put(lineNum,sc.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gzipFileMap;
    }

    /**
     * 获取非压缩文件的日志内容
     * @param logFileName:日志文件名
     * @return map:日志内容集合
     * @throws IOException
     */
    private Map getLogContent(String logFileName) throws IOException {
        // 创建文件对象
        File file = new File(logFileName);
        // 判断该文件是否存在，不存在直接返回
        if (!file.exists()) {
            return new HashMap<Integer, String>();
        }

        Map<Integer, String> frontMap = new HashMap<>();
        int lineNum = 0;
        String line = null;
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
        while ((line = br.readLine()) != null) {
            lineNum++;
            frontMap.put(lineNum, line);
        }
        }catch (Exception e){
            e.printStackTrace();
        }
        return frontMap;
    }

    /**
     * 打印日志文件列表
     * @param pattern  日志文件名
     * @param filter   指定字段
     * @param n        上下文日志行数
     * @param response
     * @throws IOException
     */
    @GetMapping()
    private void getLogContext(@RequestParam(name = "pattern", required = false) String pattern,
                               @RequestParam(name = "filter", required = false) String filter,
                               @RequestParam(name = "n", defaultValue = "0") int n,
                               HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=utf-8"); //设置响应的内容类型
        PrintWriter writer = new PrintWriter(response.getOutputStream());
        StringBuilder logKeywordTextArea = new StringBuilder();

        // 如果pattern == null 则直接打印日志文件列表
        if (pattern == null || pattern.isEmpty()) {
            for (String file : getLogFiles()) {
                writer.println(file);
            }
            writer.flush();
            return;
        }

        // pattern != null 则获取文件内容
        // 判断是否为gzip压缩文件
        if (pattern.substring(pattern.lastIndexOf(".") + 1).equals("gz")) {
            // 拼接文件路径
            String gzipFilePath = "logs/" + pattern;
            // 提取压缩文件内容
            Map<Integer, String> gzipFileMap = readGzipFile(gzipFilePath);
            // 如果filter为空
            if (filter == null || filter.isEmpty()) {
                // 如果没n=0那么就默认设为100
                if (n == 0){
                    n = 100;
                }else if (n == -1){
                 //如果n=-1那么就显示该日志的所有内容
                    for (int key : gzipFileMap.keySet()) {
                        String aLog = gzipFileMap.get(key);
                        logKeywordTextArea.append(String.format("%06d", key) + " |  " + aLog + "\n");
                    }
                    // 推到浏览器显示
                    writer.println(logKeywordTextArea);
                    // 刷新流，将缓冲区的数据全部推出
                    writer.flush();
                }
                    // 如果有n参数那么就直接使用传过来的n参数的值，循环取出文件最后的 n 条数据
                    // 在文件size不足 n 行时，将文件内容全部输出
                    if (gzipFileMap.size() <= n) {
                        for (int key : gzipFileMap.keySet()) {
                            String aLog = gzipFileMap.get(key);
                            logKeywordTextArea.append(String.format("%06d", key) + " |  " + aLog + "\n");
                        }
                    } else {
                        // 文件size大于 n 行，将文件的最后 n 行输出
                        for (int i = 1; i <= n; i++) {
                            String aLog = gzipFileMap.get(gzipFileMap.size() - (n - i));
                            logKeywordTextArea.append(String.format("%06d", gzipFileMap.size() - (n - i)) + " |  " + aLog + "\n");
                        }
                    }
            } else {
                // filter不为空
                // 如果n=0则默认设为6,
                if (n == 0){
                    n = 6;
                }
                // 当filter != null时，取出n行上下文
                for (int key : gzipFileMap.keySet()) {
                    String aLog = gzipFileMap.get(key);
                    if (!aLog.toLowerCase().contains(filter.toLowerCase().trim())) continue;
                    // 获取上文的n行
                    for (int i = 0; i < n; i++) {
                        if (gzipFileMap.get(key - (n + 1 - i)) == null) continue;
                        logKeywordTextArea.append(String.format("%06d", (key - (n - i))) + " |  " + gzipFileMap.get(key - (n - i)) + "\n");
                    }

                    // 获取目标行
                    logKeywordTextArea.append(String.format("%06d", key) + " |  " + gzipFileMap.get(key) + "\n");

                    //获取下文的n行
                    for (int i = 1; i <= n; i++) {
                        if (gzipFileMap.get(key + i) == null) continue;
                        logKeywordTextArea.append(String.format("%06d", (key + i)) + " |  " + gzipFileMap.get(key + i) + "\n");
                    }
                    break;
                }
            }
        } else {
            // 文件为非GZIP压缩文件
            // 拼接文件路径
            String filePath = "logs/" + pattern;
            Map<Integer, String> map = this.getLogContent(filePath);
            // filter == null，则默认 n=100 获取该日志最新的 n 条信息
            if (filter == null || filter.isEmpty()) {
                // 如果n=0那么默认设为100
                if (n == 0){
                    n = 100;
                }else if (n == -1){
                    //如果n=-1那么就显示该日志的所有内容
                    for (int key : map.keySet()){
                        String aLog = map.get(key);
                        logKeywordTextArea.append(String.format("%06d",key) + " |  " + aLog + "\n");
                    }
                }
                    // 如果日志文件的条目数 <= n 则将文件内容全部输出
                    if (map.size() <= n) {
                        for (int key : map.keySet()) {
                            String aLog = map.get(key);
                            logKeywordTextArea.append(String.format("%06d", key) + " |  " + aLog + "\n");
                        }
                    } else {
                        // 如果日志的条目数 > n 则输出日志最新的 n 条信息
                        for (int i = 1; i <= n; i++) {
                            String aLog = map.get(map.size() - (n - i));
                            logKeywordTextArea.append(String.format("%06d", map.size() - (n - i)) + " |  " + aLog + "\n");
                        }
                    }

            } else {
                // 如果n=0则默认设为6,
                if (n == 0){
                    n = 6;
                }
                // 如果有n参数那么就直接使用传过来的n参数的值
                // 当filter != null时，取出n行上下文
                for (int key : map.keySet()) {
                    String aLog = map.get(key);
                    if (!aLog.toLowerCase().contains(filter.toLowerCase().trim())) continue;
                    // 取出上文的n行
                    for (int i = 0; i < n; i++) {
                        if (map.get(key - (n + 1 - i)) == null) continue;
                        logKeywordTextArea.append(String.format("%06d", (key - (n - i))) + " |  " + map.get(key - (n - i)) + "\n");
                    }

                    // 目标行
                    logKeywordTextArea.append(String.format("%06d", key) + " |  " + map.get(key) + "\n");

                    // 取出下文的n行
                    for (int i = 1; i <= n; i++) {
                        if (map.get(key + i) == null) continue;
                        logKeywordTextArea.append(String.format("%06d", (key + i)) + " |  " + map.get(key + i) + "\n");
                    }
                    break;
                }
            }
        }
        // 推到浏览器显示
        writer.println(logKeywordTextArea);
        // 刷新流，将缓冲区的数据全部推出
        writer.flush();
    }

    /**
     * @return json格式的日志列表
     * @throws IOException
     */
    @GetMapping(value = "/json",produces = {"application/json;charset=utf-8"})
    private Tip getLogFileList(@RequestParam(name = "pattern", required = false) String pattern,
                               @RequestParam(name = "filter",required = false) String filter,
                               @RequestParam(name = "n" , defaultValue = "0") int n) throws IOException {
        List<String> logList = new ArrayList<>();

        // pattern为空
        if (pattern == null || pattern.isEmpty()) {
            return SuccessTip.create(this.getLogFiles());
        }
        // 文件为压缩文件
        if (pattern.substring(pattern.lastIndexOf(".") + 1).equals("gz")){
            // 拼接文件路径
            String gzipFilePath = "logs/" + pattern;
            // 提取压缩文件内容
            Map<Integer, String> gzipFileMap = readGzipFile(gzipFilePath);
            // 在filter参数为null的情况下将n设为100，输出文件最后的100行
            if (filter == null || filter.isEmpty()) {
                //如果n=0那么就默认设n=100
                if (n == 0){
                    n = 100;
                }else if (n == -1){
                    //如果n == -1，那么就把该日志的内容全部输出
                    for (int key : gzipFileMap.keySet()) {
                        String aLog = gzipFileMap.get(key);
                        logList.add(String.format("%06d", key) + " |  " + aLog);
                    }
                    return SuccessTip.create(logList);
                }
                // n != 0且 != -1的情况
                // 在文件size不足 n 行时，将文件内容全部输出
                if (gzipFileMap.size() <= n) {
                    for (int key : gzipFileMap.keySet()) {
                        String aLog = gzipFileMap.get(key);
                        logList.add(String.format("%06d", key) + " |  " + aLog);
                    }
                } else {
                    // 文件size大于 n 行，将文件的最后 n 行输出
                    for (int i = 1; i <= n; i++) {
                        String aLog = gzipFileMap.get(gzipFileMap.size() - (n - i));
                        logList.add(String.format("%06d", gzipFileMap.size() - (n - i)) + " |  " + aLog);
                    }
                }
                return SuccessTip.create(logList);
            }else {
                // 当filter不为空，如果没有传n则默认n=6
                if (n == 0){
                    n = 6;
                }
                // 有n参数传入,则且传入的n参数>0就直接使用传入的n
                for (int key : gzipFileMap.keySet()){
                    String aLog = gzipFileMap.get(key);
                    if (!aLog.toLowerCase().contains(filter.toLowerCase().trim())) continue;
                    // 获取上文的n行
                    for (int i=0; i < n ;i++ ){
                        if (gzipFileMap.get(key - (n  - i)) == null) continue;
                        logList.add(String.format("%06d",(key - (n - i))) + " |  " + gzipFileMap.get(key - (n - i)));
                    }

                    // 获取目标行
                    logList.add(String.format("%06d",key) + " |  " + gzipFileMap.get(key));

                    //获取下文的n行
                    for (int i = 1; i<=n;i++){
                        if (gzipFileMap.get(key + i) == null) continue;
                        logList.add(String.format("%06d",(key + i )) + " |  " + gzipFileMap.get(key + i));
                    }
                    break;
                }
                return SuccessTip.create(logList);
            }

        }


        //非压缩文件
        // 拼接文件路径
        String filePath = "logs/" + pattern;
        Map<Integer, String> map = this.getLogContent(filePath);
        // pattern不为空，但是filter为空，则默认n=100，输出最新的100行
        if (filter == null || filter.isEmpty()) {
            // 没有传入n参数，则默认n=100，输出最新的100行
            if (n == 0){
                n = 100;
            }else if (n == -1){
                //如果n=-1那么就把该日志内容全部输出
                for ( int key : map.keySet()){
                    String aLog = map.get(key);
                    logList.add(String.format("%06d",key) + " |  " + aLog);
                }
                return SuccessTip.create(logList);
            }
            // 传入了n参数且n>0则直接使用传入的值即可
            // 在文件size不足 n 行时，将文件内容全部输出
            if (map.size() <= n){
                for ( int key : map.keySet()){
                    String aLog = map.get(key);
                    logList.add(String.format("%06d",key) + " |  " + aLog);
                }
            }else{
                // 文件size大于 n 行，将文件的最后 n 行输出
                for (int i = 1 ; i <= n ;i++){
                    String aLog = map.get(map.size() - (n - i));
                    logList.add(String.format("%06d",map.size() - (n - i)) + " |  " + aLog);
                }
            }
            return SuccessTip.create(logList);
        }else {
            // 当filter不为空，则输出filter上下文,如果没有n参数的传入，
            if (n == 0){
                n = 6;
            }
            for (int key : map.keySet()){
                String aLog = map.get(key);
                if (!aLog.toLowerCase().contains(filter.toLowerCase().trim())) continue;
                // 获取上文的n行
                for (int i=0; i<n ;i++ ){
                    if (map.get(key - (n - i)) == null) continue;
                    logList.add(String.format("%06d",(key - (n - i))) + " |  " + map.get(key - (n - i)));
                }

                // 获取目标行
                logList.add(String.format("%06d",key) + " |  " + map.get(key));

                //获取下文的n行
                for (int i = 1; i <= n;i++){
                    if (map.get(key + i) == null) continue;
                    logList.add(String.format("%06d",(key + i)) + " |  " + map.get(key + i));
                }
                break;
            }
            return SuccessTip.create(logList);
        }
    }
}
