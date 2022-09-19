package com.jfeat.dev.devops.services.domain.service.impl;

import com.jfeat.dev.devops.services.domain.service.DevDevelopService;
import com.jfeat.dev.devops.services.gen.crud.service.impl.CRUDDevDevelopServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author admin
 * @since 2017-10-16
 */

@Service("devDevelopService")
public class DevDevelopServiceImpl extends CRUDDevDevelopServiceImpl implements DevDevelopService {

    @Override
    protected String entityName() {
        return "DevDevelop";
    }


}
