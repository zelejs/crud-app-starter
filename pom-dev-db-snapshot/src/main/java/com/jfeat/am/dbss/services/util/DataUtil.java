package com.jfeat.am.dbss.services.util;
import com.jfeat.am.dbss.services.model.DataModel;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by Silent-Y on 2017/11/14.
 */
public class DataUtil {

    public  final static String datasourceProperties = "datasource.properties";
    public  final static String datasourceLocalProperties = "datasource-local.properties";


    private void fileGet(String filePath,Properties properties) throws IOException {
        File file = new File(filePath);
        if(file.exists()){
            FileInputStream fileInputStream = new FileInputStream(file);
            properties.load(fileInputStream);
        }else{
            ClassPathResource classPathResource = new ClassPathResource(filePath);
            properties.load(classPathResource.getInputStream());
        }
    }

    private BasicDataSource getDatasourceServer(String dataproperties) throws Exception {
        Properties properties = new Properties();
        fileGet(dataproperties,properties);
        BasicDataSource dataSource =BasicDataSourceFactory.createDataSource(properties);
        return dataSource;
    }


    private Connection getConnectionServer(BasicDataSource basicDataSource) throws SQLException {
        Connection connection = null;
        connection = basicDataSource.getConnection();
        connection.setAutoCommit(true);
        return connection;
    }


    private DataModel changeDatasourceServer(DataModel dataModel, String dataproperties, String author) throws IOException {
        Properties  properties = new Properties();
        File file = new File(dataproperties);
        fileGet(dataproperties,properties);
        if(!file.exists()){
            file.createNewFile();
        }
        DataModel model = new DataModel();
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        String url = properties.getProperty("url");

        String parameter = "";
        if(url.contains("?")){
            parameter = url.substring(url.lastIndexOf("?"));
        }

        String password =properties.getProperty("password");
        String username =properties.getProperty("username");


        if(dataModel.getPassword()!=null&&dataModel.getPassword().trim()!=""){
            password = dataModel.getPassword();
            properties.setProperty("password",password);
        }
        if(dataModel.getUsername()!=null&&dataModel.getUsername().trim()!=""){
            username = dataModel.getUsername();
            properties.setProperty("username",username);
        }

        String temp = url.substring(13,url.length());
        String[] strings = temp.split("/");
        String dbName = strings[1];

        String host = "";
        String port = "";

        if(strings[0].contains(":")) {
            host = strings[0].split(":")[0];
            port = strings[0].split(":")[1];
        }else{
            host = strings[0];
        }

        if(dataModel.getDbName()!=null&&dataModel.getDbName().trim()!=""){
            dbName = dataModel.getDbName();
        }
        if(dataModel.getHost()!=null&&dataModel.getHost().trim()!=""){
            host = dataModel.getHost();
        }
        if(dataModel.getPort()!=null&&dataModel.getPort().trim()!=""){
            port = dataModel.getPort();
        }

        List<String> strs = new ArrayList<>();
        url = "url=jdbc:mysql://"+host+":"+port+"/"+dbName+parameter;
       // System.out.printf(url);
        //properties.setProperty("url",url);

        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        String format = dateFormat.format(date);
        properties.store(fileOutputStream,"change by "+author+" at "+format);
        fileOutputStream.close();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line;

        while((line=bufferedReader.readLine())!=null){
            if(!line.contains("url")){
                strs.add(line);
            }
        }
        strs.add(2,url);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        for(String str:strs){
            bufferedWriter.write(str);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
        bufferedWriter.close();

        model.setHost(host);
        model.setDbName(dbName);
        model.setPassword(password);
        model.setUsername(username);
        model.setPort(port);
        model.setUrl(dataModel.getUrl());
        return model;
    }


    public DataModel changeDatasource(DataModel dataModel,String author) throws IOException {
        return changeDatasourceServer(dataModel, datasourceProperties, author);
    }

    public DataModel changeLocalDatasource(DataModel dataModel,String author) throws IOException {
        return changeDatasourceServer(dataModel, datasourceLocalProperties, author);
    }

    public BasicDataSource getLocalDatasource() throws Exception {
        return  getDatasourceServer(datasourceLocalProperties);
    }

    public BasicDataSource getDatasource() throws Exception {
        return  getDatasourceServer(datasourceProperties);
    }

    public Connection getConnection() throws Exception {
        return getConnectionServer(getDatasource());
    }

    public Connection getLocalConnection() throws Exception {
        return getConnectionServer(getLocalDatasource());
    }

    public DataModel getPropertiesMessageServer(String propertiesPath) throws IOException {
        Properties  properties = new Properties();
        fileGet(propertiesPath,properties);
        String url = properties.getProperty("url");
        String password =properties.getProperty("password");
        String username =properties.getProperty("username");
        String temp = url.substring(13,url.length());
        String[] strings = temp.split("/");
        String dbName = strings[1];
        String[] hosts = strings[0].split(":");
        String host = hosts[0];
        String port = hosts.length==2?hosts[1] : "3306";
        DataModel dataModel = new DataModel();
        dataModel.setPassword(password);
        dataModel.setUsername(username);
        dataModel.setDbName(dbName.contains("?")?dbName.substring(0,dbName.lastIndexOf("?")):dbName);
        dataModel.setHost(host);
        dataModel.setPort(port);
        dataModel.setUrl(url);
        return dataModel;
    }
}
