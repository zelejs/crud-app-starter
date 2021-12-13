package com.jfeat.jar.dependency.api;

import com.alibaba.fastjson.JSONArray;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.jar.dependency.DecompileUtils;
import com.jfeat.jar.dependency.ZipFileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 依赖处理接口
 *
 * @author zxchengb
 * @date 2020-08-05
 */
@RestController
@Api("dependency-api")
@RequestMapping("/api/dev/dependency")
public class DependencyEndpoint {
    protected final static Logger logger = LoggerFactory.getLogger(DependencyEndpoint.class);

    @GetMapping("/json")
    @ApiOperation(value = "反回所有的依赖文件")
    public Tip getDependencyJson(@RequestParam(value = "pattern", required = false) String pattern,
                                 HttpServletResponse response) throws IOException {
        String jarPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        if(jarPath.contains("!")){
            jarPath = jarPath.substring("file:/".length(), jarPath.indexOf("!"));
        }else{
            jarPath = new File(".").getCanonicalPath() + "/target/dev-dependency-0.0.1-standalone.jar";
        }
        logger.info("jarPath: "+jarPath);
        File jarFile = new File(jarPath);
        Assert.isTrue(jarFile.exists(), jarPath + " not exits !");

        var tree = ZipFileUtils.getJarArchiveTreeData(jarFile, pattern,false);

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

        return SuccessTip.create(jsonArray);
    }


    @GetMapping("/decompile")
    @ApiOperation(value = "反编译指定的文件(pattern空,即显示jar所有文件")
    public void decompileJarFile(@RequestParam(value = "pattern", required = false) String pattern,
                                HttpServletResponse response
    ) throws IOException {
        String jarPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        if(jarPath.contains("!")){
            jarPath = jarPath.substring("file:/".length(), jarPath.indexOf("!"));
        }else{
            jarPath = new File(".").getCanonicalPath() + "/target/dev-dependency-0.0.1-standalone.jar";
        }
        File jarFile = new File(jarPath);
        if(!jarFile.exists()){
            ServletOutputStream out = response.getOutputStream();
            out.println(jarPath + " not exists!");
            out.close();
            return;
        }

        List<String> entries = ZipFileUtils.searchWithinJarArchive(jarFile, pattern, true);

        if(entries.size()==1) {
            String singleEntryPattern = entries.get(0);
            String filesOrContent = ZipFileUtils.inspectJarEntryContentWithinArchive(jarFile, singleEntryPattern);

            boolean requiredDecompile = filesOrContent.lines().count()==1 && new File(filesOrContent.trim()).exists();

            // start to decompile
            List<String> lines = requiredDecompile ? DecompileUtils.decompileFiles(filesOrContent, false) : filesOrContent.lines().collect(Collectors.toList());

            // output to browser
            PrintWriter writer = new PrintWriter(response.getOutputStream());
            for (String line : lines) {
                writer.println(line);
            }
            writer.flush();


        }else {
            PrintWriter writer = new PrintWriter(response.getOutputStream());
            for (String line : entries) {
                writer.println(line);
            }
            writer.flush();
        }

//            ServletOutputStream out = response.getOutputStream();
//            for(String line : decompiles){
//                out.println(line);
//            }
//            out.close();
    }
}
