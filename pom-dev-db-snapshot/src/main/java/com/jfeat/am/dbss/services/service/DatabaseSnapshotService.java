package com.jfeat.am.dbss.services.service;

import com.jfeat.am.dbss.services.model.DataModel;
import com.jfeat.am.dbss.services.model.Message;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;


/**
 * Created by Silent-Y on 2017/11/2.
 */
public interface DatabaseSnapshotService {
    DataModel getDataSource() throws Exception;
    DataModel setDataSource(DataModel dataModel, String author) throws IOException;
    DataModel getDataSourceLocal() throws Exception;
    DataModel setDataSourceLocal(DataModel dataModel,String author) throws IOException;
    String getSqlRepositoryPath();
    List<Message> createSnapshotBy(String author) throws Exception;
    List<Message> restoreSnapshotBy(String author, String sql) throws Exception;
    List<Message> deleteSnapshot(long id, String sql) throws Exception;
    List<Message> getLogRecords() throws Exception;
}
