package com.jfeat.am.dbss.services.service.impl;

import com.jfeat.am.dbss.services.model.DataModel;
import com.jfeat.am.dbss.services.model.Message;
import com.jfeat.am.dbss.services.model.MessageType;
import com.jfeat.am.dbss.services.service.DatabaseSnapshotService;
import com.jfeat.am.dbss.services.util.DataUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Created by Silent-Y on 2017/11/2.
 */
@Service
public class DatabaseSnapshotServiceImpl implements DatabaseSnapshotService {
    static final String SqlRepository = "db/snapshots";
    final static String datasourceProperties = "datasource.properties";
    final static String datasourceLocalProperties = "datasource-local.properties";


    private DataModel dataSource;
    private DataModel dataSourceLocal;

    public DatabaseSnapshotServiceImpl() {
    }

    private void reset() {
        try {
            dataSource = getDataSource();
            dataSourceLocal = getDataSourceLocal();
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public DataModel getDataSource() throws Exception {
        dataSource = new DataUtil().getPropertiesMessageServer(datasourceProperties);
        return new DataUtil().getPropertiesMessageServer(datasourceProperties);
    }

    public DataModel getDataSourceLocal() throws Exception {
        dataSourceLocal = new DataUtil().getPropertiesMessageServer(datasourceLocalProperties);
        return dataSourceLocal;
    }

    @Override
    public DataModel setDataSource(DataModel dataModel, String author) throws IOException {
        return new DataUtil().changeDatasource(dataModel, author);
    }

    @Override
    public DataModel setDataSourceLocal(DataModel dataModel, String author) throws IOException {
        return new DataUtil().changeLocalDatasource(dataModel, author);
    }

    @Override
    public String getSqlRepositoryPath() {
        reset();
        File directory = new File("");

        StringBuffer builder = new StringBuffer();

        builder.append(directory.getAbsolutePath());
        builder.append("/");
        builder.append(SqlRepository);

        return builder.toString();
    }

    public String getSqlPathName(String sql) {
        reset();
        String sqlRepository = this.getSqlRepositoryPath();

        StringBuilder builder = new StringBuilder();
        builder.append(sqlRepository);

        if (sql != null && sql.length() > 0) {
            builder.append("/");
            builder.append(sql);
        }

        return builder.toString();
    }

    public List<Message> createSnapshotBy(String author) throws Exception {
        reset();
        String sql = genSqlFileName();

        //// back database, do not need to open database
        backupDatabase(sql);

        /// create log
        List<Message> records = null;
        Connection localConnection = new DataUtil().getLocalConnection();

        Message message = new Message();
        message.setAuthor(author);
        message.setType(MessageType.Backup.toString());
        message.setTimestamp(new Date(new java.util.Date().getTime()));
        message.setSql(sql);

        insertLogRecordInternal(message, localConnection);

        records = getLogRecordsInternal(localConnection);

        return records;
    }

    public List<Message> restoreSnapshotBy(String author, String sql) throws Exception {
        reset();
        /// restore database, require to open database
        restoreDatabase(sql, new DataUtil().getConnection());

        /// create log
        List<Message> records = null;
        Connection localConnection = new DataUtil().getLocalConnection();

        Message message = new Message();
        message.setAuthor(author);
        message.setType(MessageType.Restore.toString());
        message.setSql(sql);
        message.setTimestamp(new Date(new java.util.Date().getTime()));

        insertLogRecordInternal(message, localConnection);

        // get new records
        records = getLogRecordsInternal(localConnection);

        return records;
    }

    public List<Message> deleteSnapshot(long id, String sql) throws Exception {
        reset();
        /// delete the file
        String sqlPath = getSqlPathName(sql);
        File sqlFile = new File(sqlPath);
        if (sqlFile.exists()) {
            sqlFile.delete();
        }

        /// delete the record
        List<Message> records = null;
        Connection localConnection = new DataUtil().getLocalConnection();

        deleteLogRecordInternal(id, localConnection);

        /// get new records
        records = getLogRecordsInternal(localConnection);

        return records;
    }

    public List<Message> getLogRecords() throws Exception {
        reset();
        try {
            List<Message> records = null;

            records = getLogRecordsInternal(new DataUtil().getLocalConnection());
            return records;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    protected void backupDatabase(String sql) throws Exception {
        reset();
        final String WindowsOs = "Windows";

        String commandLine = null;
        if (getOsName().contains(WindowsOs)) {
            commandLine = buildBackupCommandLineOfWindowsOs(sql);
        } else {
            commandLine = buildBackupCommandLine(sql);
        }

        /// check SqlRepository esists
        File repo = new File(SqlRepository);
        if (!repo.exists()) {
            repo.mkdirs();
        }

        System.out.println(commandLine);

        Process exec = Runtime.getRuntime().exec(commandLine);

 //       exec.waitFor();
        /// save to file
        dumpShellEchoLinesToFile(exec.getInputStream(), sql);
    }

    protected void restoreDatabase(String sql, Connection connection) throws Exception {
        String sqlPath = getSqlPathName(sql);

        File sqlFile = new File(sqlPath);
        if (sqlFile.exists()) {

            BufferedReader reader =
                    new BufferedReader(new FileReader(sqlFile));

            connection.setAutoCommit(false);
            Statement stmt = connection.createStatement();

            String line = "";
            String message = "";
            while ((line = reader.readLine()) != null) {
                if ((!line.contains("-"))&&(!line.trim().equals(""))) {
                    message += line;
                }
            }

            String[] split = message.split(";");
            for (String ad : split) {
                System.out.println(ad);
                stmt.addBatch(ad);
            }

            stmt.executeBatch();
            connection.commit();

            reader.close();
        }
    }

    private String genSqlFileName() {
        reset();
        String sql = String.valueOf(new java.util.Date().getTime()) + ".sql";
        return sql;
    }

    private String getOsName() {
        reset();
        final String WindowsOs = "Windows";
        String os = System.getProperty("os.name");
        if (os == null || os.length() == 0) {
            return WindowsOs;
        }
        return os;
    }

    private String buildBackupCommandLine(String sql) {
        reset();
        StringBuilder builder = new StringBuilder();
        builder.append("mysqldump --no-defaults ");

        String host = dataSource.getHost();
        if(host.compareTo("localhost")==0 ||
                host.compareTo("127.0.0.1")==0){
            /// do not include -hlocalhost
        }else {
            builder.append("-h");
            builder.append(dataSource.getHost());
        }
        if(dataSource.getPort().compareTo("3306")!=0) {
            builder.append(" --port ");
            builder.append(dataSource.getPort());
        }
        builder.append(" -u");
        builder.append(dataSource.getUsername());
        builder.append(" -p");
        builder.append(dataSource.getPassword());
        builder.append(" --databases ");
        builder.append(dataSource.getDbName());

        //String commandLine = "mysqldump --no-defaults -h$1 -port$2  -u$3 -p$4 --databases $5 > $6";
        String commandLine = builder.toString();
        return commandLine;
    }

    @Deprecated
    private String buildBackupCommandLineOfWindowsOs(String sql) throws Exception {
        reset();
        final String DumpScript = "db_snapshot_dump.bat";
        final String RestoreScript = "db_snapshot_restore.bat";

        ClassPathResource cpr = new ClassPathResource(DumpScript);
        File file = cpr.getFile();

        String script = file.getAbsolutePath();
        String sqlPath = getSqlPathName(sql);

        StringBuffer commandLineBuilder = new StringBuffer();
        commandLineBuilder.append("cmd /c start ");
        commandLineBuilder.append(script);
        commandLineBuilder.append(" ");
        commandLineBuilder.append(dataSource.getHost());
        commandLineBuilder.append(" ");
        commandLineBuilder.append(dataSource.getPort());
        commandLineBuilder.append(" ");
        commandLineBuilder.append(dataSource.getUsername());
        commandLineBuilder.append(" ");
        commandLineBuilder.append(dataSource.getPassword());
        commandLineBuilder.append(" ");
        commandLineBuilder.append(dataSource.getDbName());
        commandLineBuilder.append(" ");
        commandLineBuilder.append(sqlPath);

        String commandLine = commandLineBuilder.toString();
        return commandLine;
    }

    private void dumpShellEchoLinesToFile(InputStream inputStream, String sql) throws Exception {
        reset();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(inputStream));

        String targetSql = getSqlPathName(sql);

        File targetFile = new File(targetSql);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(targetFile));

        String line = "";
        while ((line = reader.readLine()) != null) {
            bufferedWriter.write(line);
            //System.out.println(line);
            bufferedWriter.newLine();
        }

        bufferedWriter.flush();
        bufferedWriter.close();
    }


    private void insertLogRecordInternal(Message message, Connection connection) throws SQLException {
        reset();
        try {
            String sql = "insert into db_snapshot_message(event_time,type,author,sql_path) values(?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setDate(1, message.getTimestamp());
            preparedStatement.setString(2, message.getType());
            preparedStatement.setString(3, message.getAuthor());
            preparedStatement.setString(4, message.getSql());
            preparedStatement.execute();
            preparedStatement.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void deleteLogRecordInternal(long id, Connection connection) throws ClassNotFoundException, SQLException {
        reset();
        if (id > 0) {
            String sql = "delete from db_snapshot_message where id=" + String.valueOf(id);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
            preparedStatement.close();
        }
    }

    private List<Message> getLogRecordsInternal(Connection connection) {
        try {
            List<Message> messages = new ArrayList<>();
            String sql = "select * from db_snapshot_message order by event_time LIMIT 0, 10";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Message message = new Message();
                message.setId(rs.getInt("id"));
                message.setTimestamp(rs.getDate("event_time"));
                message.setType(rs.getString("type"));
                message.setAuthor(rs.getString("author"));
                message.setSql(rs.getString("sql_path"));
                messages.add(message);
            }
            preparedStatement.close();
            return messages;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
