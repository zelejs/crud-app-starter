package com.jfeat.dev.devops.api;

import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.dev.devops.services.domain.dao.QueryDevVersionDao;
import com.jfeat.dev.devops.services.domain.model.DevVersionRecord;
import com.jfeat.dev.devops.services.domain.service.DevopsServices;
import com.jfeat.dev.devops.services.gen.persistence.model.DevVersion;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/dev/devops")
public class Devops {

    @Resource
    DevopsServices devopsServices;

    @Resource
    QueryDevVersionDao queryDevVersionDao;

    @GetMapping("/{sqlFile}")
    public Tip getResultList(@PathVariable("sqlFile") String sqlFile, HttpServletRequest request) {
        return SuccessTip.create(devopsServices.querySql(request, sqlFile));
    }

    @PostMapping("/{sqlFile}")
    public Tip executeSql(@PathVariable("sqlFile") String sqlFile, HttpServletRequest request) {
        return SuccessTip.create(devopsServices.executeSql(request, sqlFile));
    }

    //    获取当前app数据 运维操作
    @GetMapping("/appidOperationList")
    public Tip getAppidOperationList(@RequestParam("appid") String appid) {

        DevVersionRecord record = new DevVersionRecord();
        record.setAppid(appid);
        List<DevVersionRecord> devVersionRecordList = queryDevVersionDao.queryVersionDetail(null,record,null);
//        System.out.println(devVersionRecordList);
        if (devVersionRecordList!=null && devVersionRecordList.size()==1){
            return SuccessTip.create(devVersionRecordList.get(0));
        }
        return SuccessTip.create();
    }


}
