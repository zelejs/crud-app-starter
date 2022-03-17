package com.jfeat.dev.connection.services.domain.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface QueryTablesDao {

    @Select("SHOW TABLES")
    List<String> queryAllTables();

    @Select("SHOW CREATE TABLE #{tableName}")
    String queryCreateTableSql(@Param("tableName") String tableName);

    @Select("SELECT * FROM #{tableName}")
    String queryTableInfo(@Param("tableName") String tableName);

    @Select("select database()")
    String queryDataBase();

}
