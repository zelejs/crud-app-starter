package com.jfeat.module.test.services.domain.dao;

import com.jfeat.module.test.services.domain.model.TableTestRecord;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jfeat.crud.plus.QueryMasterDao;
import org.apache.ibatis.annotations.Param;
import com.jfeat.module.test.services.gen.persistence.model.TableTest;
import com.jfeat.module.test.services.gen.crud.model.TableTestModel;

import java.util.Date;
import java.util.List;

/**
 * Created by Code generator on 2021-08-19
 */
public interface QueryTableTestDao extends QueryMasterDao<TableTest> {
   /*
    * Query entity list by page
    */
    List<TableTestRecord> findTableTestPage(Page<TableTestRecord> page, @Param("record") TableTestRecord record,
                                            @Param("search") String search, @Param("orderBy") String orderBy,
                                            @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /*
     * Query entity model for details
     */
    TableTestModel queryMasterModel(@Param("id") Long id);
}