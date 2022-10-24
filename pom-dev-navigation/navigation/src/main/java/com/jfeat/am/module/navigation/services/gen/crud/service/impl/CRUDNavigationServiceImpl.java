package com.jfeat.am.module.navigation.services.gen.crud.service.impl;
// ServiceImpl start

            
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jfeat.crud.plus.FIELD;
import com.jfeat.am.module.navigation.services.gen.persistence.model.Navigation;
import com.jfeat.am.module.navigation.services.gen.persistence.dao.NavigationMapper;
import com.jfeat.am.module.navigation.services.gen.crud.service.CRUDNavigationService;
import org.springframework.stereotype.Service;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import javax.annotation.Resource;
import com.jfeat.crud.plus.impl.CRUDServiceOnlyImpl;

/**
 * <p>
 *  implementation
 * </p>
 *CRUDNavigationService
 * @author Code generator
 * @since 2022-04-18
 */

@Service
public class CRUDNavigationServiceImpl  extends CRUDServiceOnlyImpl<Navigation> implements CRUDNavigationService {





        @Resource
        protected NavigationMapper navigationMapper;

        @Override
        protected BaseMapper<Navigation> getMasterMapper() {
                return navigationMapper;
        }







}


