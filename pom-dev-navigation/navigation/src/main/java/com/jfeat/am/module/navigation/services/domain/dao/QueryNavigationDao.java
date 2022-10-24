package com.jfeat.am.module.navigation.services.domain.dao;

import com.jfeat.am.module.navigation.services.domain.model.NavigationRecord;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jfeat.crud.plus.QueryMasterDao;
import org.apache.ibatis.annotations.Param;
import com.jfeat.am.module.navigation.services.gen.persistence.model.Navigation;
import com.jfeat.am.module.navigation.services.gen.crud.model.NavigationModel;

import java.util.Date;
import java.util.List;

/**
 * Created by Code generator on 2022-04-18
 */
public interface QueryNavigationDao extends QueryMasterDao<Navigation> {
   /*
    * Query entity list by page
    */
    List<NavigationRecord> findNavigationPage(Page<NavigationRecord> page, @Param("record") NavigationRecord record,
                                            @Param("search") String search, @Param("orderBy") String orderBy,
                                            @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /*
     * Query entity model for details
     */
    NavigationModel queryMasterModel(@Param("id") Long id);
}