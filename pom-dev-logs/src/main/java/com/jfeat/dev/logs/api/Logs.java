package com.jfeat.dev.logs.api;

//import com.jfeat.dev.connection.api.request.ForeignKeyRequest;
import com.alibaba.fastjson.JSONObject;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
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
import java.util.List;
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
    private List<String> getLogFiles() throws IOException {
        List<String> list = null;
        String logPath1 = new File(".").getCanonicalPath() + "/log";
        File fileDir = new File(logPath1);
        if (!fileDir.exists()) {
            String logPath2 = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "\\log";
            fileDir = new File(logPath2);
        }
        File[] files = fileDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) continue;
            list.add(file.getName());
        }
        return list;
    }

    private Map getLogContent(String logFiles) throws IOException {
        String logPath1 = new File(".").getCanonicalPath() + "/logs/" + logFiles;
        File file = new File(logPath1);
        if (!file.exists()) {
            String logPath2 = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "/logs/" + logFiles;
            file = new File(logPath2);
        }
        Map<Integer, String> frontMap = new HashMap<>();
        //Map<Integer, String> afterMap = new HashMap<>();

        //定义一个标志位，往后的3行数据是我们想要的
        //boolean flag = false;
        int lineNum = 0;
        String line = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        while ((line = br.readLine()) != null) {
            lineNum++;
            frontMap.put(lineNum, line);
        }
        return frontMap;
    }
    @GetMapping()
    private Tip getLogContext(@RequestParam(name = "pattern") String pattern,
                              @RequestParam(name = "filter", defaultValue = " ") String filter,
                              @RequestParam(name = "time", defaultValue = " ") String time,
                              @RequestParam(name = "logNumber", defaultValue = "0") int logNumber,
                              HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=utf-8");
        JSONObject jsonObject =new JSONObject();
        if(pattern ==null){
            int i=0;
            for(String str : this.getLogFiles()){
                jsonObject.put(i+" ",str);
            }
            return SuccessTip.create(jsonObject);
        }
        Map<Integer,String> map = this.getLogContent(pattern);
        boolean flag = false;
        int flagNum=0;
        StringBuilder logKeywordTextArea=null;
        for(Map.Entry entry:map.entrySet()){
            if(!map.values().contains(filter))continue;
            if (flag) {
                flagNum++;
                //System.out.println("stringMap--后7段--" + line);
                //显示在页面
                if (logNumber != 0) logKeywordTextArea.append(map.values() + "\n");
                if (flagNum >= logNumber) {
                    //logKeywordTextArea.append("==========下一个搜索结果============" + "\n\n");
                    flag = false;
                }
            }
            //开始进行关键字检索
            //TODO 只要文件中包含有该关键字就输出
            if (map.values().contains(time)) {
                if ((int)entry.getKey() <= logNumber) {
                    for (int i = 1; i < (int)entry.getKey(); i++) {
                        logKeywordTextArea.append(map.get((int)entry.getKey() - i) + "\n");
                    }
                } else {
                    for (int i = 1; i < logNumber; i++) {
                        logKeywordTextArea.append(map.get((int)entry.getKey() - i) + "\n");
                    }
                }
                flag = true;
                time = "/t//////";
            }
            jsonObject.put("",logKeywordTextArea);
        }
        return SuccessTip.create(jsonObject);
    }
}
