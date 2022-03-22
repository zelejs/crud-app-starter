package com.jfeat.dev.connection.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql .*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataSourceUtil {

    protected static final Logger logger = LoggerFactory.getLogger(DataSourceUtil.class);

    /**
     * @param connection
     * @param sqlList
     * @return
     **/
    public static Integer execBulkUpdate(Connection connection, List<String> sqlList) {
        int affected = 0;
        if (sqlList == null || sqlList.isEmpty()) {
            return affected;
        }
        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            for (String sql : sqlList) {
                logger.debug("execUpdate sql={}", sql);
                statement.addBatch(sql);
            }
            int[] affectedArr = statement.executeBatch();
            for (int i = 0; i < affectedArr.length; i++) {
                affected += affectedArr[i];
            }
            connection.commit();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, null, null);
        }

        return affected;
    }

    /**
     * @param connection
     * @param sql
     * @return
     **/
    public static Integer execUpdate(Connection connection, String sql) {
        PreparedStatement preparedStatement = null;
        Integer affected = 0;
        try {
            logger.debug("execUpdate sql={}", sql);
            preparedStatement = connection.prepareStatement(sql);
            affected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, preparedStatement, null);
        }
        return affected;
    }


    /**
     * 直接查询数据库
     *
     * @param connection
     * @param sql
     * @return
     * @throws SQLException
     */
    public static List<Map<String, String>> querySQL(Connection connection, String sql) {
        List<Map<String, String>> result = new ArrayList<>();
        logger.debug("querySQL sql={}", sql);
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            while (rs.next()) {
                Map<String, String> map = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    map.put(resultSetMetaData.getColumnLabel(i), rs.getObject(i) == null ? "" : rs.getObject(i).toString());
                }
                result.add(map);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, preparedStatement, rs);
        }

        return null;
    }

    public static void closeConnection(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


