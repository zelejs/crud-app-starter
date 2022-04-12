package com.jfeat.dev.logs.api;

//import com.jfeat.dev.connection.api.request.ForeignKeyRequest;
import com.alibaba.fastjson.JSONObject;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
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
import java.util.ArrayList;
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
    private List<String> getLogFiles() throws IOException {
        List<String> list =new ArrayList<>() ;
        File fileDir = new File("logs");
        if (!fileDir.exists()) {
            String logPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "\\logs";
            fileDir = new File(logPath);
        }
        File[] files = fileDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) continue;
            list.add(file.getName());
        }
        return list;
    }

    private Map getLogContent(String logFiles) throws IOException {
        File file = new File("logs/"+logFiles);
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
    private Tip getLogContext(@RequestParam(name = "pattern",required = false) String pattern,
                              @RequestParam(name = "filter", defaultValue = " ") String filter,
                              @RequestParam(name = "time", defaultValue = " ") String time,
                              @RequestParam(name = "logNumber", defaultValue = "0") int logNumber,
                              HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=utf-8");
        if(pattern ==null){
            return SuccessTip.create(this.getLogFiles());
        }

        Map<Integer,String> map = this.getLogContent(pattern);
        boolean flag = false;
        int flagNum=0;
        StringBuilder logKeywordTextArea=new StringBuilder();
        for(int key:map.keySet()){
            String string = map.get(key);
            if(!string.contains(filter))continue;
            if (flag) {
                flagNum++;
                //System.out.println("stringMap--后7段--" + line);
                //显示在页面
                if (logNumber != 0) logKeywordTextArea.append( string+ "\n");
                if (flagNum >= logNumber) {
                    //logKeywordTextArea.append("==========下一个搜索结果============" + "\n\n");
                    flag = false;
                }
            }
            //开始进行关键字检索
            //TODO 只要文件中包含有该关键字就输出
            if (string.contains(time)) {
                if (key <= logNumber) {
                    for (int i = 1; i < key; i++) {
                        logKeywordTextArea.append(map.get(key - i) + "\n");
                    }
                } else {
                    for (int i = 1; i < logNumber; i++) {
                        logKeywordTextArea.append(map.get(key - i) + "\n");
                    }
                }
                flag = true;
                time = "/t//////";
            }
        }
        return SuccessTip.create(logKeywordTextArea);
    }
}
