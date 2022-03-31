package com.jfeat.jar.dependency.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.jar.dependency.DecompileUtils;
import com.jfeat.jar.dependency.ZipFileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/*
//            ServletOutputStream out = response.getOutputStream();
//            for(String line : decompiles){
//                out.println(line);
//            }
//            out.close();
 */

/**
 * 依赖处理接口
 *
 * @author zxchengb
 * @date 2020-08-05
 */
@RestController
@Api("dependency-api")
@RequestMapping("/dev/dependency")
public class DependencyEndpoint {
    protected final static Logger logger = LoggerFactory.getLogger(DependencyEndpoint.class);

    /**
     * 获取依赖树
     * @param jarFile
     * @param pattern
     * @param verbose
     * @return
     */
    private JSONArray getDependencyEntries(File jarFile, String pattern, Boolean verbose){
        var tree = ZipFileUtils.getJarArchiveTreeData(jarFile, pattern, verbose,false);

        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<String,List<String>> entry : tree.entrySet()){
            if(entry.getValue().size()==0){
                jsonArray.add(entry.getKey());
            }else{
                entry.getValue().stream().forEach(subEntry-> {
                    jsonArray.add(String.join("", entry.getKey(), "!", subEntry));
                });
            }
        }
        jsonArray.sort((Comparator<Object>)(s1,s2)->{return s1.toString().compareTo(s2.toString());});
        return jsonArray;
    }


    /**
     * 对单个.class decompile
     * @param jarFile
     * @param pattern
     * @return
     */
    private List<String> decompileJarEntries(File jarFile, String pattern){
        List<String>  decompileLines = new ArrayList<>();

        List<String> entries = ZipFileUtils.searchWithinJarArchive(jarFile, pattern, true);

        if(entries.size()==1) {
            String singleEntryPattern = entries.get(0);
            String filesOrContent = ZipFileUtils.inspectJarEntryContentWithinArchive(jarFile, singleEntryPattern);

            boolean requiredDecompile = filesOrContent.lines().count()==1 && new File(filesOrContent.trim()).exists();

            // start to decompile
            List<String> lines = requiredDecompile ? DecompileUtils.decompileFiles(filesOrContent, false) : filesOrContent.lines().collect(Collectors.toList());

            // output to browser
//            PrintWriter writer = new PrintWriter(response.getOutputStream());
//            for (String line : lines) {
//                writer.println(line);
//            }
//            writer.flush();
            decompileLines.addAll(lines);

        }else {
//            PrintWriter writer = new PrintWriter(response.getOutputStream());
//            for (String line : entries) {
//                writer.println(line);
//            }
//            writer.flush();
            decompileLines.addAll(entries);
        }

        return decompileLines;
    }


    @GetMapping("/json")
    @ApiOperation(value = "返回所有的依赖文件[JSON格式]")
    public Tip getDependencyJson(
            @ApiParam(name = "pattern", value = "搜索过滤条件")
            @RequestParam(value = "pattern", required = false) String pattern,
                                 @ApiParam(name = "all", value = "是否深度搜索所有文件,默认为 True")
                                 @RequestParam(value = "all", required = false) Boolean all,
                                 HttpServletResponse response) throws IOException {
        if(all==null){all=false;}
        String jarPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        if(jarPath.contains("!")){
            jarPath = jarPath.substring("file:".length(), jarPath.indexOf("!"));
        }else{
            jarPath = new File(".").getCanonicalPath() + "/target/dev-dependency-0.0.1-standalone.jar";
        }
        logger.info("jarPath: "+jarPath);
        File jarFile = new File(jarPath);
        Assert.isTrue(jarFile.exists(), jarPath + " not exits !");

        if(StringUtils.isBlank(pattern)) {
            boolean verbose = all || StringUtils.isNotBlank(pattern); // means not search, just return the dependencies
            JSONArray entries = getDependencyEntries(jarFile, pattern, verbose);
            return SuccessTip.create(entries);
        }

        // json
        var list = decompileJarEntries(jarFile, pattern);
        return SuccessTip.create(list);
    }

    @GetMapping
    @ApiOperation(value = "返回所有的依赖文件")
    public void printDependencyEntries(
            @ApiParam(name = "pattern", value = "搜索过滤条件")
            @RequestParam(value = "pattern", required = false) String pattern,
                                 @ApiParam(name = "all", value = "是否深度搜索所有文件,默认为 True")
                                 @RequestParam(value = "all", required = false) Boolean all,
            HttpServletResponse response) throws IOException {
        if(all==null){all=false;}
        String jarPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        if(jarPath.contains("!")){
            jarPath = jarPath.substring("file:".length(), jarPath.indexOf("!"));
        }else{
            jarPath = new File(".").getCanonicalPath() + "/target/dev-dependency-0.0.1-standalone.jar";
        }
        logger.info("jarPath: "+jarPath);
        File jarFile = new File(jarPath);
        Assert.isTrue(jarFile.exists(), jarPath + " not exits !");

        if(StringUtils.isBlank(pattern)) {
            boolean verbose = all || StringUtils.isNotBlank(pattern); // means not search, just return the dependencies
            JSONArray entries = getDependencyEntries(jarFile, pattern, verbose);

            /// print
            PrintWriter writer = new PrintWriter(response.getOutputStream());
            entries.stream().forEach(line -> writer.println(line));
            writer.flush();
            return;
        }

        // print
        var list = decompileJarEntries(jarFile, pattern);
        PrintWriter writer = new PrintWriter(response.getOutputStream());
        for (String line : list) {
            writer.println(line);
        }
        writer.flush();

    }


    @GetMapping("/extra")
    @ApiOperation(value = "从jar文件中获取指定的Entry文件")
    public void extraDependencyEntry(
            @ApiParam(name = "pattern", value = "搜索过滤条件")
            @RequestParam(value = "pattern", required = false) String pattern,
            HttpServletResponse response) throws IOException {

        String jarPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        if(jarPath.contains("!")){
            jarPath = jarPath.substring("file:".length(), jarPath.indexOf("!"));
        }else{
            jarPath = new File(".").getCanonicalPath() + "/target/dev-dependency-0.0.1-standalone.jar";
        }
        logger.info("jarPath: "+jarPath);
        File jarFile = new File(jarPath);
        Assert.isTrue(jarFile.exists(), jarPath + " not exits !");

        var list = ZipFileUtils.listEntriesFromArchive(jarFile, "", pattern);
        if(list.size()==0 || list.size()>1){
            PrintWriter writer = new PrintWriter(response.getOutputStream());
            for (String line : list) {
                writer.println(line);
            }
            writer.flush();
        }
        String entryName = list.get(0);

        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; " + entryName);
        ZipFileUtils.extraJarEntries(jarFile, "", pattern, response.getOutputStream());
    }

    @Autowired
    BuildProperties buildProperties;
    @GetMapping("/build")
    public Tip buildInfo() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("artifact", buildProperties.getName());
        jsonObject.put("version", buildProperties.getVersion());
        jsonObject.put("buildAt", buildProperties.getTime());
        jsonObject.put("artifactId:", buildProperties.getArtifact());
        jsonObject.put("groupId:", buildProperties.getGroup());
        return SuccessTip.create(jsonObject);
    }
}
