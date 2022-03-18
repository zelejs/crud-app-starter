package com.jfeat.dev.connection.services.domain.service;

import com.google.common.base.Charsets;
import com.google.common.base.Utf8;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.xml.stream.events.Characters;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class TableServer {

    @Resource
    DataSource dataSource;

    Connection conn = null;

    public byte[] changToByte(List<String> list){
//        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
//        try {
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
//                    arrayOutputStream);
//            objectOutputStream.writeObject(list);
////            objectOutputStream.writeUTF(list);
//            objectOutputStream.flush();
            StringBuilder b = new StringBuilder();
            for(String st:list){
                b.append(st);
            }
            byte[] data =b.toString().getBytes(StandardCharsets.UTF_8);
//            byte[] data = arrayOutputStream.toString(StandardCharsets.UTF_8).getBytes(StandardCharsets.UTF_8);
//            objectOutputStream.close();
//            arrayOutputStream.close();
            return data;
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return null;
    }

    public String handleResult(String sql){
        ResultSet rs = executeQuery(sql);
        try {
            while (rs.next()) {
                ResultSetMetaData md = rs.getMetaData();
                int cols = md.getColumnCount();

                for (int i = 1; i <= cols; i++) {
                    switch (md.getColumnType(i)) {
                        case Types.VARCHAR:
                        {
                            String val = rs.getString(i);
                            if(val==null){
//                                System.out.print("null");
                            }else {
                                val = val.replace("\r", "");
                                val = val.replace("\n", "");
                                if(i>1) {
                                    return val;
                                }else{
//                                    System.out.print(val);
                                }
                            }
                        }
                        break;
                        default:
                            System.out.print("Unknown type: " + md.getColumnType(i));
                    }

                }

                // new line
//                System.out.println("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            close();
        }
        return null;
    }

    public List<String> handleResult2(String sql){
        ResultSet rs = executeQuery(sql);
        List<String> list = new ArrayList<>();
        try {
            while (rs.next()) {
                ResultSetMetaData md = rs.getMetaData();
                int cols = md.getColumnCount();
                StringBuilder str = new StringBuilder();
                    str.append("INSERT INTO " + md.getTableName(1) + " VALUES(");
                    for (int i = 1; i <= cols; i++) {
                        switch (md.getColumnType(i)) {
                            case Types.BIT:
                            case Types.INTEGER:
                            case Types.TINYINT:
                            case Types.SMALLINT:
                            case Types.NUMERIC:
                                str.append(rs.getInt(i));
                                break;
                            case Types.BIGINT:
                                str.append(rs.getLong(i));
                                break;
                            case Types.DECIMAL:
                                str.append(rs.getBigDecimal(i));
                                break;
                            case Types.BOOLEAN:
                                str.append(rs.getBoolean(i));
                                break;
                            case Types.FLOAT:
                            case Types.REAL:
                                str.append(rs.getFloat(i));
                                break;
                            case Types.DOUBLE:
                                str.append(rs.getDouble(i));
                                break;
                            case Types.VARCHAR:
                            case Types.NVARCHAR:
                            case Types.CHAR:
                            case Types.NCHAR:
                            case Types.DATE:
                            case Types.TIMESTAMP: {
                                String val = rs.getString(i);
                                if (val == null) {
                                    str.append("null");
                                } else {
//                                    val = val.replace("\r", "");
//                                    val = val.replace("\n", "");
                                    val = val.replace("'", "");
                                    val = val.replaceAll("\'","\\\\\'");
                                    str.append("'" + val + "'");
                                }
                            }
                            break;
                            case Types.VARBINARY: {
                                byte[] bytes = rs.getBytes(i);
                                if (bytes != null) {
                                    String hex = "";
//                                for (int c = 0; c < bytes.length; c++) {
//                                    hex += String.format("%02X", bytes[c]);
//                                }
                                    str.append("'" + hex + "'");
                                }
                            }
                            break;
                            case Types.NULL:
                                str.append("null");
                                break;
                            case Types.LONGVARBINARY:
                                str.append("'" + rs.getString(i) + "'");
                                break;
                            case Types.LONGVARCHAR:
                                var st = rs.getString(i);
                                st = st.replaceAll("\"","\\\\\"");
                                st = st.replaceAll("\'","\\\\\'");
                                str.append("'" + st + "'");
                                break;
                            case Types.TIME:
                                str.append("'"+rs.getTime(i)+"'");
                                break;
                            default:
                                System.out.print("Unknown type: " + md.getColumnType(i));
                        }

                        if (i < cols) {
                            str.append(",");
                        }
                    }
                    str.append(");");
                    String st = str.toString();
                    list.add(st);
                }
            } catch(Exception e){
                e.printStackTrace();
            }finally{
                close();
            }
        return list;
    }
    public List<String> show(String sql){
        ResultSet rs = executeQuery(sql);
        List<String> list = new ArrayList<>();
        try {
            while (rs.next()) {
                ResultSetMetaData md = rs.getMetaData();
                int cols = md.getColumnCount();
                StringBuilder str = new StringBuilder();
                for (int i = 1; i <= cols; i++) {
                    switch (md.getColumnType(i)) {
                        case Types.BIT:
                        case Types.INTEGER:
                        case Types.TINYINT:
                        case Types.SMALLINT:
                        case Types.NUMERIC:
                            str.append(rs.getInt(i));
                            break;
                        case Types.BIGINT:
                            str.append(rs.getLong(i));
                            break;
                        case Types.DECIMAL:
                            str.append(rs.getBigDecimal(i));
                            break;
                        case Types.BOOLEAN:
                            str.append(rs.getBoolean(i));
                            break;
                        case Types.FLOAT:
                        case Types.REAL:
                            str.append(rs.getFloat(i));
                            break;
                        case Types.DOUBLE:
                            str.append(rs.getDouble(i));
                            break;
                        case Types.VARCHAR:
                        case Types.NVARCHAR:
                        case Types.CHAR:
                        case Types.NCHAR:
                        case Types.DATE:
                        case Types.TIMESTAMP: {
                            String val = rs.getString(i);
                            if (val == null) {
                                str.append("null");
                            } else {
                                val = val.replace("\r", "");
                                val = val.replace("\n", "");
                                str.append("'" + val + "'");
                            }
                        }
                        break;
                        case Types.VARBINARY: {
                            byte[] bytes = rs.getBytes(i);
                            if (bytes != null) {
                                String hex = "";
//                                for (int c = 0; c < bytes.length; c++) {
//                                    hex += String.format("%02X", bytes[c]);
//                                }
                                str.append("'" + hex + "'");
                            }
                        }
                        break;
                        case Types.NULL:
                            str.append("null");
                            break;
                        case Types.LONGVARBINARY:
                            str.append("'" + rs.getString(i) + "'");
                            break;
                        case Types.LONGVARCHAR:
                            str.append('"' + rs.getString(i) + '"');
                            break;
                        case Types.TIME:
                            str.append(rs.getTime(i));
                            break;
                        default:
                            System.out.print("Unknown type: " + md.getColumnType(i));
                    }
                }
                String st = str.toString();
                list.add(st);
            }
        } catch(Exception e){
            e.printStackTrace();
        }finally{
            close();
        }
        return list;
    }

    public ResultSet executeQuery(String sql) {
        ResultSet rs=null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement q1 = conn.prepareStatement(sql);
            rs = q1.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public void close() {
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
