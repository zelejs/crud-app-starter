package com.jfeat.dev.devops.services.gen.crud.service.impl;
// ServiceImpl start


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jfeat.crud.plus.impl.CRUDServiceOnlyImpl;
import com.jfeat.dev.devops.services.gen.crud.service.CRUDDevDevelopService;
import com.jfeat.dev.devops.services.gen.persistence.dao.DevDevelopMapper;
import com.jfeat.dev.devops.services.gen.persistence.model.DevDevelop;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  implementation
 * </p>
 *CRUDDevDevelopService
 * @author Code generator
 * @since 2022-09-16
 */

@Service
public class CRUDDevDevelopServiceImpl  extends CRUDServiceOnlyImpl<DevDevelop> implements CRUDDevDevelopService {





        @Resource
        protected DevDevelopMapper devDevelopMapper;

        @Override
        protected BaseMapper<DevDevelop> getMasterMapper() {
                return devDevelopMapper;
        }







}


