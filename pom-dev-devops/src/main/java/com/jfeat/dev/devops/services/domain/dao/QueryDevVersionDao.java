package com.jfeat.dev.devops.services.domain.dao;

import com.jfeat.dev.devops.services.domain.model.DevDevelopRecord;
import com.jfeat.dev.devops.services.domain.model.DevVersionRecord;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jfeat.crud.plus.QueryMasterDao;
import com.jfeat.dev.devops.services.gen.persistence.model.DevDevelop;
import org.apache.ibatis.annotations.Param;
import com.jfeat.dev.devops.services.gen.persistence.model.DevVersion;
import com.jfeat.dev.devops.services.gen.crud.model.DevVersionModel;

import java.util.Date;
import java.util.List;

/**
 * Created by Code generator on 2022-09-16
 */
public interface QueryDevVersionDao extends QueryMasterDao<DevVersion> {
    /*
     * Query entity list by page
     */
    List<DevVersionRecord> findDevVersionPage(Page<DevVersionRecord> page, @Param("record") DevVersionRecord record,
                                              @Param("tag") String tag,
                                              @Param("search") String search, @Param("orderBy") String orderBy,
                                              @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /*
     * Query entity model for details
     */
    DevVersionModel queryMasterModel(@Param("id") Long id);


    /*
     * Query entity model list for slave items
     */
    List<DevVersionModel> queryMasterModelList(@Param("masterId") Object masterId);

    List<DevVersionRecord> queryVersionDetail(Page<DevDevelopRecord> page, @Param("record") DevVersionRecord record,
                                              @Param("search") String search);

    List<DevDevelop> devVersionsSubquery(@Param("sqlVersion") String sqlVersion);
}