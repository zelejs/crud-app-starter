package com.jfeat.dev.devops.services.gen.crud.service.impl;
// ServiceImpl start


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jfeat.crud.plus.impl.CRUDServiceOnlyImpl;
import com.jfeat.dev.devops.services.gen.crud.service.CRUDDevVersionService;
import com.jfeat.dev.devops.services.gen.persistence.dao.DevVersionMapper;
import com.jfeat.dev.devops.services.gen.persistence.model.DevVersion;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  implementation
 * </p>
 *CRUDDevVersionService
 * @author Code generator
 * @since 2022-09-16
 */

@Service
public class CRUDDevVersionServiceImpl  extends CRUDServiceOnlyImpl<DevVersion> implements CRUDDevVersionService {





        @Resource
        protected DevVersionMapper devVersionMapper;

        @Override
        protected BaseMapper<DevVersion> getMasterMapper() {
                return devVersionMapper;
        }







}


