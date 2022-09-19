package com.jfeat.dev.devops.services.domain.dao;

import com.jfeat.dev.devops.services.domain.model.DevDevelopRecord;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jfeat.crud.plus.QueryMasterDao;
import com.jfeat.dev.devops.services.domain.model.DevVersionRecord;
import org.apache.ibatis.annotations.Param;
import com.jfeat.dev.devops.services.gen.persistence.model.DevDevelop;
import com.jfeat.dev.devops.services.gen.crud.model.DevDevelopModel;

import java.util.Date;
import java.util.List;

/**
 * Created by Code generator on 2022-09-16
 */
public interface QueryDevDevelopDao extends QueryMasterDao<DevDevelop> {
   /*
    * Query entity list by page
    */
    List<DevDevelopRecord> findDevDevelopPage(Page<DevDevelopRecord> page, @Param("record") DevDevelopRecord record,
                                            @Param("tag") String tag,
                                            @Param("search") String search, @Param("orderBy") String orderBy,
                                            @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /*
     * Query entity model for details
     */
    DevDevelopModel queryMasterModel(@Param("id") Long id);


    /*
     * Query entity model list for slave items
     */
    List<DevDevelopModel> queryMasterModelList(@Param("masterId") Object masterId);


}