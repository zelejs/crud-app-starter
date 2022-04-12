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
    private List<String> getLogFiles() throws IOException {
        List<String> list =new ArrayList<>() ;
        File fileDir = new File("logs");
        if (!fileDir.exists()) {
            String logPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "\\logs";
            fileDir = new File(logPath);
        }
        File[] files = fileDir.listFiles();
        for (File file : files) {
            decompression(file);

        }
        for (File file : files) {
            if(isArchiveFile(file))continue;
            if(file.isDirectory())continue;
            list.add(file.getName());
        }
        return list;
    }
    public static void unGzipFile(String sourcedir) {
        String ouputfile = "";
        try {
            //建立gzip压缩文件输入流
            FileInputStream fin = new FileInputStream(sourcedir);
            //建立gzip解压工作流
            GZIPInputStream gzin = new GZIPInputStream(fin);
            //建立解压文件输出流
            ouputfile = sourcedir.substring(0,sourcedir.lastIndexOf('.'));
//            ouputfile = ouputfile.substring(0,ouputfile.lastIndexOf('.'));
            FileOutputStream fout = new FileOutputStream(ouputfile);
            int num;
            byte[] buf=new byte[1024];

            while ((num = gzin.read(buf,0,buf.length)) != -1)
            {
                fout.write(buf,0,num);
            }
            gzin.close();
            fout.close();
            fin.close();
        } catch (Exception ex){
            System.err.println(ex.toString());
        }
        return;
    }

    /**

     * zip解压

     * @param srcFile        zip源文件

     * @param destDirPath     解压后的目标文件夹

     * @throws RuntimeException 解压失败会抛出运行时异常

     */

    public static void unZip(File srcFile, String destDirPath) throws RuntimeException {


        // 开始解压

        ZipFile zipFile = null;

        try {

                zipFile = new ZipFile(srcFile);

            Enumeration<?> entries = zipFile.entries();

            while (entries.hasMoreElements()) {

                ZipEntry entry = (ZipEntry) entries.nextElement();


                // 如果是文件夹，就创建个文件夹

                if (entry.isDirectory()) {

                    String dirPath = destDirPath + "/" + entry.getName();

                    File dir = new File(dirPath);

                    dir.mkdirs();

                } else {

                    // 如果是文件，就先创建一个文件，然后用io流把内容copy过去

                    File targetFile = new File(destDirPath + "/" + entry.getName());

                    // 保证这个文件的父文件夹必须要存在

                    if(!targetFile.getParentFile().exists()){

                        targetFile.getParentFile().mkdirs();

                    }

                    targetFile.createNewFile();

                    // 将压缩文件内容写入到这个文件中

                    InputStream is = zipFile.getInputStream(entry);

                    FileOutputStream fos = new FileOutputStream(targetFile);

                    int len;

                    byte[] buf = new byte[1024];

                    while ((len = is.read(buf)) != -1) {

                        fos.write(buf, 0, len);

                    }

                    // 关流顺序，先打开的后关闭

                    fos.close();

                    is.close();

                }

            }

        } catch (Exception e) {

            throw new RuntimeException("unzip error from ZipUtils", e);

        } finally {

            if(zipFile != null){

                try {

                    zipFile.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }

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


    private static byte[] ZIP_HEADER_1 = new byte[] { 80, 75, 3, 4 };

    private static byte[] ZIP_HEADER_2 = new byte[] { 80, 75, 5, 6 };

    private static byte[] ZIP_HEADER_3 = new byte[] { 31, -117, 8, 0 };

    /**

     * 判断文件是否为一个压缩文件

     *

     * @param file

     * @return

     */

    private static boolean isArchiveFile(File file) {

        boolean isArchive = false;

        InputStream input = null;

        try {
            input = new FileInputStream(file);
// 31 -117 8 0
            byte[] buffer = new byte[4];

            int length = input.read(buffer, 0, 4);

            if (length == 4) {
                isArchive = (Arrays.equals(ZIP_HEADER_1, buffer)) || (Arrays.equals(ZIP_HEADER_2, buffer))||(Arrays.equals(ZIP_HEADER_3, buffer));
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (input != null) {
                try {
                    input.close();

                } catch (IOException e) {
                }

            }

        }
        return isArchive;

    }

    private static void decompression(File file) {

        boolean isZip = false, isGzip = false;

        InputStream input = null;

        try {
            input = new FileInputStream(file);
// 31 -117 8 0
            byte[] buffer = new byte[4];

            int length = input.read(buffer, 0, 4);

            if (length == 4) {
                isZip = (Arrays.equals(ZIP_HEADER_1, buffer)) || (Arrays.equals(ZIP_HEADER_2, buffer));
                isGzip = Arrays.equals(ZIP_HEADER_3, buffer);
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (input != null) {
                try {
                    input.close();

                } catch (IOException e) {
                }

            }

        }
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
