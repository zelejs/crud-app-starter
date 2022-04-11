package com.jfeat.dev.logs.api;

//import com.jfeat.dev.connection.api.request.ForeignKeyRequest;
import com.jfeat.dev.logs.services.domain.dao.QueryTablesDao;
import com.jfeat.dev.logs.services.domain.service.TableServer;
//import com.jfeat.dev.connection.util.DataSourceUtil;
import io.swagger.annotations.Api;
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
import java.io.*;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

/**
 * 查询数据库
 * @author vincent huang
 * @date 2022-01-20
 */
@RestController
@Api("dev-logs")
@RequestMapping("/dev/logs")
public class Logs {
    @Autowired
    DataSource dataSource;

    @Resource
    QueryTablesDao queryTablesDao;

    @Resource
    TableServer tableServer;
    @GetMapping()
    public void lookContextLogs(@RequestParam(name = "pattern", required = false) String pattern,
                                @RequestParam(name = "filter",defaultValue = " ")String filter,
                                @RequestParam(name = "time",defaultValue = " ") String time,
                                @RequestParam(name = "logNumber",defaultValue = "0")int logNumber,
                                HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter writer = new PrintWriter(response.getOutputStream());
        try{
            String logPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()+"\\log";
            File fileDir = new File(logPath);
            File[] files = fileDir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) continue;
                if(pattern == null){
                    writer.println(file.getName());
                    writer.flush();
                }
                else if (pattern.equals(file.getName())) {
                    try {
                        //定义个集合缓存10日志信息
                        Map<Integer, String> frontMap = new HashMap<>();
                        //Map<Integer, String> afterMap = new HashMap<>();

                        //定义一个标志位，往后的3行数据是我们想要的
                        boolean flag = false;
                        int flagNum = 0,lineNum=0,len=0;
                        String line=null;
                        StringBuilder logKeywordTextArea = new StringBuilder();
                        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                        while ((line = br.readLine()) != null) {
                            if(!line.contains(filter))continue;//过滤
                            lineNum++;
                            //获取读取字节长度，用于进度条显示
//                        len += line.length();// 27841772
//                        // System.out.println("文件长度为：" + len);
//
//
                            frontMap.put((int) lineNum, line);
                            //获取前7行数据
                            //如果想获取7条记录则填9，如果是想获取4条记录则填6，依次递增
                            if (frontMap.size() == (logNumber+2)) {
                                int index = (int) (lineNum + 1) - (logNumber+2);
                                frontMap.remove(index);
                            }
                            //收集后7段的值
                            if (flag) {
                                flagNum++;
                                //System.out.println("stringMap--后7段--" + line);
                                //显示在页面
                                if(logNumber!=0)logKeywordTextArea.append(line + "\n");
                                if (flagNum>=logNumber) {
                                    //logKeywordTextArea.append("==========下一个搜索结果============" + "\n\n");
                                    flag = false;
                                }
                            }
                            //开始进行关键字检索
                            //TODO 只要文件中包含有该关键字就输出
                            if (line.contains(time)) {
                                if(lineNum<=logNumber){
                                    for (int i = 1; i <lineNum; i++) {
                                        logKeywordTextArea.append(frontMap.get( lineNum - i) + "\n");
                                    }
                                }
                                else {
                                    for (int i = 1; i < logNumber; i++) {
                                        logKeywordTextArea.append(frontMap.get( lineNum - i) + "\n");
                                    }
                                }
                                flag = true;
                                time = "/t//////";
                            }

                        }
                        writer.print(logKeywordTextArea);
                        writer.flush();
                        br.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            return;
        }catch (NullPointerException e1){

        }
        try{
            String logPath1 = new File(".").getCanonicalPath() + "/log";
            File fileDir = new File(logPath1);
            File[] files = fileDir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) continue;
                if(pattern == null){
                    writer.println(file.getName());
                    writer.flush();
                }
                else if (pattern.equals(file.getName())) {
                    try {
                        //定义个集合缓存10日志信息
                        Map<Integer, String> frontMap = new HashMap<>();
                        //Map<Integer, String> afterMap = new HashMap<>();

                        //定义一个标志位，往后的3行数据是我们想要的
                        boolean flag = false;
                        int flagNum = 0,lineNum=0,len=0;
                        String line=null;
                        StringBuilder logKeywordTextArea = new StringBuilder();
                        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                        while ((line = br.readLine()) != null) {
                            if(!line.contains(filter))continue;//过滤
                            lineNum++;
                            //获取读取字节长度，用于进度条显示
//                        len += line.length();// 27841772
//                        // System.out.println("文件长度为：" + len);
//
//
                            frontMap.put((int) lineNum, line);
                            //获取前7行数据
                            //如果想获取7条记录则填9，如果是想获取4条记录则填6，依次递增
                            if (frontMap.size() == (logNumber+2)) {
                                int index = (int) (lineNum + 1) - (logNumber+2);
                                frontMap.remove(index);
                            }
                            //收集后7段的值
                            if (flag) {
                                flagNum++;
                                //System.out.println("stringMap--后7段--" + line);
                                //显示在页面
                                if(logNumber!=0)logKeywordTextArea.append(line + "\n");
                                if (flagNum>=logNumber) {
                                    //logKeywordTextArea.append("==========下一个搜索结果============" + "\n\n");
                                    flag = false;
                                }
                            }
                            //开始进行关键字检索
                            //TODO 只要文件中包含有该关键字就输出
                            if (line.contains(time)) {
                                if(lineNum<=logNumber){
                                    for (int i = 1; i <lineNum; i++) {
                                        logKeywordTextArea.append(frontMap.get( lineNum - i) + "\n");
                                    }
                                }
                                else {
                                    for (int i = 1; i < logNumber; i++) {
                                        logKeywordTextArea.append(frontMap.get( lineNum - i) + "\n");
                                    }
                                }
                                flag = true;
                                time = "/t//////";
                            }

                        }
                        writer.print(logKeywordTextArea);
                        writer.flush();
                        br.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                return;
            }
        }catch (NullPointerException E) {

        }


    }

}
