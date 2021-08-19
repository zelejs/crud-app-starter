package com.jfeat.module.test.services.gen.crud.service.impl;
// ServiceImpl start

            
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jfeat.crud.plus.FIELD;
import com.jfeat.module.test.services.gen.persistence.model.TableTest;
import com.jfeat.module.test.services.gen.persistence.dao.TableTestMapper;
import com.jfeat.module.test.services.gen.crud.service.CRUDTableTestService;
import org.springframework.stereotype.Service;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import javax.annotation.Resource;
import com.jfeat.crud.plus.impl.CRUDServiceOnlyImpl;

/**
 * <p>
 *  implementation
 * </p>
 *CRUDTableTestService
 * @author Code generator
 * @since 2021-08-19
 */

@Service
public class CRUDTableTestServiceImpl  extends CRUDServiceOnlyImpl<TableTest> implements CRUDTableTestService {





        @Resource
        protected TableTestMapper tableTestMapper;

        @Override
        protected BaseMapper<TableTest> getMasterMapper() {
                return tableTestMapper;
        }







}


