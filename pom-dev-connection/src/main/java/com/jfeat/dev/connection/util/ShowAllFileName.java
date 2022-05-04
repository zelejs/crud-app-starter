package com.jfeat.dev.connection.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: HTAO
 * @CreateDate: 2022/4/29 18:15
 */
public class ShowAllFileName {
    public static void queryAllFileName(File files,List<String> fileNameList){
        //获取该文件夹对象下的所有文件和子目录
        String[] allFilesName = files.list();
        // 循环获取文件名
        for (String fileName : allFilesName){
            File file = new File(files.getPath() + "\\" + fileName);
            if (file.isDirectory()){
                // 如果是一个文件夹则调用自身，继续获取文件名
                queryAllFileName(file,fileNameList);
            }else {
                // 获取当前项目的根路径
                String project = new File("").getAbsolutePath();
                fileNameList.add(files.getPath().replace(project+"\\","") + "\\" + fileName);
            }
        }
    }
}
