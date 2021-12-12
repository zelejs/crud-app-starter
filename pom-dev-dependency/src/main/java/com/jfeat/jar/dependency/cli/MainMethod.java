package com.jfeat.jar.dependency.cli;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jfeat.jar.dep.util.DecompileUtils;
import com.jfeat.jar.dependency.DependencyUtils;
import com.jfeat.jar.dependency.ZipFileUtils;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

/**
 * @author zxchengb
 * @date 2020-08-05
 *
 */
public class MainMethod {
    /**
     * Parse Dependency list
     */
    private static final String PARSE_OPTION = "p";
    /**
     * get the groupId of the jar lib
     */
    private static final String GROUP_ID_OPTION = "g";


    /**
     * 输出为JSON
     */
    private static final String JSON_OPTION = "j";
    /**
     * 带checksum输出
     */
    private static final String CHECKSUM_OPTION = "c";
    /**
     * 输出相同项 （默认不同项）
     */
    private static final String MATCH_OPTION = "m";
    /**
     * 详细比较
     */
    private static final String VERBOSE_OPTION = "v";



    /*
    List all entries from archive
     */
    private static final String LIST_OPTION = "t";
    /*
    Show all entries from archive as tree
     */
    private static final String TREE_OPTION = "T";
    /*
    search entry from archive and show the entry path
     */
    private static final String SEARCH_OPTION = "s";

    /*
     输出CHECKSUM不同项
    */
    private static final String DIFF_OPTION = "d";

    /*
      inspect the entry content from archiva, if the entry is jar, list all its entries
    */
    private static final String INSPECT_OPTION = "i";


    public static void main(String[] args) {
        Options options = new Options();

        Option dependencyOpt = new Option(PARSE_OPTION, "parse", false, "parse the archive dependencies from standalone or pom.xml");
        options.addOption(dependencyOpt);
        Option groupIdOpt = new Option(GROUP_ID_OPTION, "group-id", false, "get the jar deployment group id");
        groupIdOpt.setRequired(false);
        options.addOption(groupIdOpt);

        /// common options
        Option jsonOpt = new Option(JSON_OPTION, "json", false, "output as json format");
        jsonOpt.setRequired(false);
        options.addOption(jsonOpt);
        Option checksumOpt = new Option(CHECKSUM_OPTION, "checksum", false, "list entries with checksum");
        checksumOpt.setRequired(false);
        options.addOption(checksumOpt);
        Option matchOpt = new Option(MATCH_OPTION, "match", false, "get matches entries for two list");
        matchOpt.setRequired(false);
        options.addOption(matchOpt);
        Option verboseOpt = new Option(VERBOSE_OPTION, "verbose", false, "compare mismatch with verbose");
        verboseOpt.setRequired(false);
        options.addOption(verboseOpt);


        /// search,list,checksum
        Option listOpt = new Option(LIST_OPTION, "list", false, "list all the entries from archive");
        listOpt.setRequired(false);
        options.addOption(listOpt);
        Option treeOpt = new Option(TREE_OPTION, "tree", false, "show all the entries from archive as tree");
        treeOpt.setRequired(false);
        options.addOption(treeOpt);
        Option searchOpt = new Option(SEARCH_OPTION, "search", true, "search criteria within archive");
        searchOpt.setRequired(false);
        options.addOption(searchOpt);
        Option diffOpt = new Option(DIFF_OPTION, "diff", false, "get diff entries with checksum");
        diffOpt.setRequired(false);
        options.addOption(diffOpt);

        // show file content
        Option inspectOpt = new Option(INSPECT_OPTION, "inspect", true, "inspect the entry file content");
        inspectOpt.setRequired(false);
        options.addOption(inspectOpt);


        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;//not a good practice, it serves it purpose
        File jar1=null, jar2=null;
        try {
            cmd = parser.parse(options, args);
            if (cmd.getArgList().size() == 0) {
                throw new ParseException("no arg!");
            }
            String jar1arg = cmd.getArgs()[0];
            jar1 = new File(jar1arg);
            if(!jar1.exists()){
                throw new ParseException(" not exits !");
            }

            if (cmd.hasOption(DIFF_OPTION)) {
                if (cmd.getArgList().size() < 2) {
                    throw new ParseException("require 2 jars to compare !");
                }
                String jar2arg = cmd.getArgs()[1];
                jar2 = new File(jar2arg);
                if(!jar2.exists()){
                    throw new ParseException(jar2arg + " not exits !");
                }
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("jar-dependency [OPTIONS] <jars...>", options);

            System.exit(1);
        }

        // 通过pom.xml获取依赖
        if (cmd.hasOption(PARSE_OPTION)) {
            try {
                List<String> d1 = cmd.hasOption(CHECKSUM_OPTION) ? getChecksumDependencies(jar1)
                        : DependencyUtils.getDependenciesByJar(jar1);
                printOut(d1, cmd.hasOption(JSON_OPTION));

            }catch (IOException e){
                e.printStackTrace();
            }
        }else if(cmd.hasOption(GROUP_ID_OPTION)){
            System.out.println(ZipFileUtils.getJarGroupId(jar1));

        }else if (cmd.hasOption(LIST_OPTION)) {
            try {
                List<String> d1 = cmd.hasOption(CHECKSUM_OPTION) ?
                        (
                                ZipFileUtils.listEntriesWithChecksum(jar1, "", "")
                                .stream()
                                        .filter(entry->entry.getValue()!=null && entry.getValue()>0)
                                        .map(entry->{
                                            return String.join("@", entry.getKey(), String.valueOf(entry.getValue()));
                                                }
                                        )
                                        .collect(Collectors.toList())
                        )
                        :
                        ZipFileUtils.listEntriesFromArchive(jar1, "", "");

                printOut(d1, cmd.hasOption(JSON_OPTION));
            }catch (IOException e){
            }
        }else if(cmd.hasOption(SEARCH_OPTION)){
            List<String> d1 = ZipFileUtils.searchWithinJarArchive(jar1, cmd.getOptionValue(SEARCH_OPTION), cmd.hasOption(CHECKSUM_OPTION));

            // start group
                String currentKey = null;
                Map<String,List<String>> map=new HashMap();
                for(int i=0;i<d1.size();i++){
                    String key=d1.get(i);
                    // 判断是否以+- 开头
                    boolean isValue = key.startsWith("+- ");
                    if(!isValue){
                        // 保存key值
                        currentKey = key;
                        // key
                        if(map.containsKey(key)){
                        }else{
                            map.put(key, null);
                        }
                    }else{
                        List<String> list = map.get(currentKey);
                        if(list==null){
                            list = new ArrayList<>();
                            map.put(currentKey, list);
                        }
                        list.add(key);
                    }
                }

                List<String> d2 = new ArrayList<>();
                for (String key: map.keySet()) {
                    d2.add(key);
                    List<String> list = map.get(key);
                    if(list!=null) {
                        for (int i = 0; i < list.size(); i++) {
                            d2.add(list.get(i));
                        }
                    }
                }
            /// end group
            printOut(d2, cmd.hasOption(JSON_OPTION));

        }else if(cmd.hasOption(TREE_OPTION)){
            List<String> d1 = ZipFileUtils.getJarArchiveTree(jar1, cmd.hasOption(CHECKSUM_OPTION));
            printOut(d1, false);
        }
        else if(cmd.hasOption(DIFF_OPTION)){
            try {
                List<String> result = new ArrayList<>();

                // first get new entries without checksum
                List<String> d1 = getChecksumEntries(jar1, true);
                List<String> d2 = getChecksumEntries(jar2, true);
                result.addAll(DependencyUtils.getDifferentDependencies(d2, d1));

                // difference version entry: file name version
                // TODO, optional

                // difference checksum entry
                List<String> c1 = getChecksumEntries(jar1, false);
                List<String> c2 = getChecksumEntries(jar2, false);
                result.addAll(getEntriesMismatchChecksum(c1, c2, FileUtils.filename(jar1.getName()), FileUtils.filename(jar2.getName())));

                // remove duplication with entry name
                // TODO,


//                if (c1.isEmpty()) {
//                    c1.add(jarToEntryWithChecksum(jar1.getAbsolutePath()));
//                }
//                if (c2.isEmpty()) {
//                    c2.add(jarToEntryWithChecksum(jar2.getAbsolutePath()));
//                }

                printOut(result, cmd.hasOption(JSON_OPTION));

            }catch (IOException e){
                e.printStackTrace();
            }
        }
        else if(cmd.hasOption(INSPECT_OPTION)) {
            String entryPattern = cmd.getOptionValue(INSPECT_OPTION);
            final String NewLine = "\n";
            var entriesContent = ZipFileUtils.getJarEntryPatternContent(jar1, entryPattern, true);
            if (entriesContent.contains(NewLine)) {
                // means multi matches, just output all entries
                if (StringUtils.isNotBlank(entriesContent)) {
                    var entries = Stream.of(entriesContent.split(NewLine)).collect(Collectors.toList());
                    printOut(entries, cmd.hasOption(JSON_OPTION));
                }
            } else {
                if (FileUtils.extension(entriesContent).equals("jar")) {
                    // jar, print out the entries
                    var entries = ZipFileUtils.getJarEntriesWithinEntry(jar1, entriesContent);
                    printOut(entries, cmd.hasOption(JSON_OPTION));
                }
                else if(FileUtils.extension(entriesContent).equals("class")){
                    // class, decompile java
                    List<String> commentClass = new ArrayList<>();
                    commentClass.add("// decompile "+ entriesContent);
                    printOut(commentClass, false);

                    List<String> classes = new ArrayList<>();
                    classes.add(entriesContent);
                    DecompileUtils.decompileFiles(classes, true);

                } else {
                    // print out the entry content
                    entriesContent = ZipFileUtils.getJarEntryPatternContent(jar1, entryPattern, false);
                    if (StringUtils.isNotBlank(entriesContent)) {
                        var entries = Stream.of(entriesContent.split(NewLine)).collect(Collectors.toList());
                        printOut(entries, cmd.hasOption(JSON_OPTION));
                    }
                }
            }
        }
    }

    /*
      不同项的详细对比
     */
    private static List<String> getEntriesMismatchChecksum(List<String> d1, List<String> d2, String jar1, String jar2) {
        List<String> diff = DependencyUtils.getDifferentDependencies(d1, d2);

        var verbose = diff.stream()
                .map(u->{
                    String entryWithoutChecksum = u.contains("@") ? u.substring(0,u.indexOf("@")):u;
                    String entryWithoutVersion  = entryWithoutChecksum.replaceAll("\\-[0-9\\.]+\\-?[a-zA-Z]*.jar", "");

                    // get the diff d1
                    String diffOne = d1.stream().filter(e->e.startsWith(entryWithoutVersion)).collect(Collectors.joining());
                    if(StringUtils.isNotBlank(diffOne)){
                        //return String.join("", jar1, "!", diffOne, "\t", u);
                        return String.join("", diffOne, " \t", u);
                    }
                    return String.join("", jar2, " \t\t", u);
                })
                .collect(Collectors.toList());

        return verbose;
    }

    /*
     获取文件的依赖以及 checksum
     */
    private static List<String> getChecksumDependencies(File jarFile) throws IOException {
        var checksums = ZipFileUtils.listEntriesWithChecksum(jarFile, "jar", "");
        return checksums.stream()
                .map(entry->{
                    return java.lang.String.join("@", entry.getKey(), java.lang.String.valueOf(entry.getValue()));
                }).collect(Collectors.toList());
//        List<String> d1 = new ArrayList<>();
//        try {
//            var checksums = ZipFileUtils.listEntriesWithChecksum(jarFile, "jar", "");
//            checksums.stream().forEach(
//                    p -> d1.add(java.lang.String.join("@", p.getKey(), java.lang.String.valueOf(p.getValue())))
//            );
//        } catch (Exception e) {
//        }
//        return d1;
    }

    private static List<String> getChecksumEntries(File jarFile, boolean excludeChecksum) throws IOException {
        var checksums = ZipFileUtils.listEntriesWithChecksum(jarFile, "", "");
        if(excludeChecksum) {
            return checksums.stream()
                    .map(entry -> {
                        return entry.getKey();
                    }).collect(Collectors.toList());
        }

        return checksums.stream()
                .map(entry -> {
                    return java.lang.String.join("@", entry.getKey(), java.lang.String.valueOf(entry.getValue()));
                }).collect(Collectors.toList());
    }

    /*
     打印依赖项
     */
    private static void printOut(List<String> result, boolean jsonFormat){
        if (jsonFormat) {
            System.out.println(
                    JSONArray.toJSONString(result, SerializerFeature.PrettyFormat)
            );
        } else {
            result.stream().forEach(
                    p->System.out.println(p)
            );
        }
    }
    /*
      由单个文件名转换为 BOOT-INF/lib/<>
     */
    private static String jarToEntry(String jarPath){
        if(FileUtils.extension(jarPath).equals("jar")){
            return String.join("", "BOOT-INF/lib/", FileUtils.filename(jarPath));
        }else if(FileUtils.extension(jarPath).equals("war")){
            return String.join("", "WEB-INF/lib/", FileUtils.filename(jarPath));
        }
        return FileUtils.basename(jarPath);
    }
    /*
      由单个文件名转换为 BOOT-INF/lib/<>, 并获取文件checksum
     */
    private static String jarToEntryWithChecksum(String jarPath){
        String entry = jarToEntry(jarPath);
        try {
            return String.join("@", entry, String.valueOf(ZipFileUtils.getFileChecksumCode(new File(jarPath), "adler32").padToLong()));
        }catch (IOException e){
        }
        return entry;
    }
}