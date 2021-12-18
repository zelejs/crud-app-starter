package com.jfeat.am.dbss.api;

import com.jfeat.am.dbss.api.tip.ErrorTip;
import com.jfeat.am.dbss.api.tip.SuccessTip;
import com.jfeat.am.dbss.api.tip.Tip;
import com.jfeat.am.dbss.services.model.DataModel;
import com.jfeat.am.dbss.services.service.DatabaseSnapshotService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;

/**
 * Created by Silent-Y on 2017/11/2.
 */
@RestController
@RequestMapping("/dev/db/snapshot")
public class DatabaseSnapshotEndpoint {

    @Resource
    private DatabaseSnapshotService databaseSnapshotService;

    @GetMapping("/config")
    public Tip getSnapshotConfig() {
        try {
            return SuccessTip.create(databaseSnapshotService.getDataSource());
        } catch (Exception e) {
            return ErrorTip.create(4000, e.getMessage());
        }
    }
    @PostMapping("/config")
    public Tip setSnapshotConfig(@RequestBody DataModel dataSource) {
        try {
            dataSource.setPort(dataSource.getPort()==null?"3306":dataSource.getPort());
            String repository = databaseSnapshotService.getSqlRepositoryPath();

            File repo = new File(repository);
            if(!repo.exists()){
                /// create the path
                repo.mkdirs();
            }

            return SuccessTip.create(databaseSnapshotService.setDataSource(dataSource,"管理员"));

        } catch (Exception e) {
            return ErrorTip.create(4000, e.getMessage());
        }
    }

    @GetMapping("/config/local")
    public Tip getSnapshotConfigLocal() {
        try {
            return SuccessTip.create(databaseSnapshotService.getDataSourceLocal());
        } catch (Exception e) {
            return ErrorTip.create(4000, e.getMessage());
        }
    }

    @PostMapping("/config/local")
    public Tip setSnapshotConfigLocal(@RequestBody DataModel dataSource) {
        try {
            String repository = databaseSnapshotService.getSqlRepositoryPath();

            File repo = new File(repository);
            if(!repo.exists()){
                /// create the path
                repo.mkdirs();
            }

            return SuccessTip.create(databaseSnapshotService.setDataSourceLocal(dataSource,"管理员"));

        } catch (Exception e) {
            return ErrorTip.create(4000, e.getMessage());
        }
    }

    @GetMapping("/delete")
    public Tip deleteSnapshot(@RequestParam(name= "id",required = true) Long id,
                              @RequestParam(name= "sql",required = true) String sql) {
        try {
            return SuccessTip.create(databaseSnapshotService.deleteSnapshot(id, sql));

        } catch (Exception e) {
            return ErrorTip.create(4000, e.getMessage());
        }
    }

    @GetMapping("/backup")
    public Tip backup(@RequestParam(name= "author",required = true) String author) {
        try {
            String repository = databaseSnapshotService.getSqlRepositoryPath();

            File repo = new File(repository);
            if(!repo.exists()){
                /// create the path
                repo.mkdirs();
            }

            return SuccessTip.create(databaseSnapshotService.createSnapshotBy(author));
        } catch (Exception e) {
            return ErrorTip.create(4000, e.getMessage());
        }
    }

    @GetMapping("/restore")
    public Tip restore(@RequestParam(name= "author",required = true) String author,
                       @RequestParam(name= "sql",required = true) String sql){
        try {
            return SuccessTip.create(databaseSnapshotService.restoreSnapshotBy(author, sql));
        } catch (Exception e) {
            return ErrorTip.create(4000, e.getMessage());
        }
    }

    @GetMapping("/logs")
    public Tip getLogs(){
        try {
            return SuccessTip.create(databaseSnapshotService.getLogRecords());
        } catch (Exception e) {
            return ErrorTip.create(4000, e.getMessage());
        }
    }
}
