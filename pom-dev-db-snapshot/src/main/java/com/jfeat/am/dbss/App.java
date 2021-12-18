package com.jfeat.am.dbss;

import com.jfeat.am.dbss.services.model.DataModel;
import com.jfeat.am.dbss.services.service.DatabaseSnapshotService;
import com.jfeat.am.dbss.services.service.impl.DatabaseSnapshotServiceImpl;

/**
 * Created by vincenthuang on 26/11/2017.
 */
public class App {

    public static void main(String[] args) throws Exception{
        DatabaseSnapshotService databaseSnapshotService = new DatabaseSnapshotServiceImpl();
        DataModel config = databaseSnapshotService.getDataSource();
        DataModel configLocal = databaseSnapshotService.getDataSourceLocal();

        config.setDbName("abc");
        databaseSnapshotService.setDataSource(config, "admin");
        DataModel changedConfig = databaseSnapshotService.getDataSource();

        configLocal.setDbName("testdb");
        databaseSnapshotService.setDataSource(configLocal, "admin");
        DataModel changedConfigLocal = databaseSnapshotService.getDataSourceLocal();

        databaseSnapshotService.restoreSnapshotBy("admin", "1511425453037.sql");


        "".toString();
    }
}
