package com.jfeat.dev.devops.services.domain.service.impl;
import com.jfeat.dev.devops.services.domain.service.DevVersionService;
import com.jfeat.dev.devops.services.gen.crud.service.impl.CRUDDevVersionServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2017-10-16
 */

@Service("devVersionService")
public class DevVersionServiceImpl extends CRUDDevVersionServiceImpl implements DevVersionService {

    @Override
    protected String entityName() {
        return "DevVersion";
    }


                            }
