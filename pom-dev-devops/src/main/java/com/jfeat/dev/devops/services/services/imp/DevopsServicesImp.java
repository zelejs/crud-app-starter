package com.jfeat.dev.devops.services.services.imp;

import com.alibaba.fastjson.JSONArray;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.dev.devops.services.services.DevopsServices;
import com.jfeat.dev.devops.services.services.ParseRequestArgument;
import com.jfeat.dev.devops.services.services.ParseSql;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@Service
public class DevopsServicesImp implements DevopsServices {

    @Resource
    ParseRequestArgument parseRequestArgument;

    @Resource
    ParseSql parseSql;

    protected final Log logger = LogFactory.getLog(getClass());




    @Override
    public JSONArray querySql(HttpServletRequest request, String sqlFile) {
        //        获取参数键值对
        Map<String,String> map = parseRequestArgument.parseGetRequestArgument(request);
        String sql = parseSql.readSqlFile(sqlFile);
        if (sql==null || sql.equals("")){
            logger.error("读取sql文件失败"+sql);
            throw new BusinessException(BusinessCode.FileReadingError,"读取sql文件失败");
        }

        return parseSql.querySql(parseSql.sqlParameters(sql,map));

    }

    @Override
    public int executeSql(HttpServletRequest request, String sqlFile) {
        Map<String,String> map = parseRequestArgument.parseGetRequestArgument(request);
        String sql = parseSql.readSqlFile(sqlFile);
        if (sql==null || sql.equals("")){
            logger.error("读取sql文件失败"+sql);
            throw new BusinessException(BusinessCode.FileReadingError,"读取sql文件失败");
        }
        return parseSql.executeSql(parseSql.sqlParameters(sql,map));
    }
}
