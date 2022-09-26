package com.jfeat.dev.devops.api;

import com.alibaba.fastjson.JSONObject;
import com.jfeat.am.core.jwt.JWTKit;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.dev.devops.services.domain.dao.QueryDevVersionDao;
import com.jfeat.dev.devops.services.domain.model.DevVersionRecord;
import com.jfeat.dev.devops.services.domain.service.DevopsServices;
import com.jfeat.dev.devops.services.gen.persistence.model.DevVersion;
import com.jfeat.users.account.services.gen.persistence.dao.UserAccountMapper;
import com.jfeat.users.account.services.gen.persistence.model.UserAccount;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/dev/devops")
public class Devops {

    @Resource
    DevopsServices devopsServices;

    @Resource
    QueryDevVersionDao queryDevVersionDao;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    UserAccountMapper userAccountMapper;

//    存储当前用户选择的 用户条件key  dev:userId:id
    private static final String redisKeyPrefix = "dev:userId:";

    @GetMapping("/{sqlFile}")
    public Tip getResultList(@PathVariable("sqlFile") String sqlFile, HttpServletRequest request) {
        return SuccessTip.create(devopsServices.querySql(request, sqlFile));
    }

    @PostMapping("/{sqlFile}")
    public Tip executeSql(@PathVariable("sqlFile") String sqlFile, HttpServletRequest request) {
        return SuccessTip.create(devopsServices.executeSql(request, sqlFile));
    }

    //    获取当前app数据 运维操作
    @GetMapping("/appidOperationList")
    public Tip getAppidOperationList(@RequestParam("appid") String appid) {

        DevVersionRecord record = new DevVersionRecord();
        record.setAppid(appid);
        List<DevVersionRecord> devVersionRecordList = queryDevVersionDao.queryVersionDetail(null,record,null);
//        System.out.println(devVersionRecordList);
        if (devVersionRecordList!=null && devVersionRecordList.size()==1){
            return SuccessTip.create(devVersionRecordList.get(0));
        }
        return SuccessTip.create();
    }

    @GetMapping("/users/current")
    public Tip getCurrentUser(){
        Long userId = JWTKit.getUserId();
        String redisKey = redisKeyPrefix+String.valueOf(userId);
        if (userId==null){
            throw new BusinessException(BusinessCode.NoPermission,"没有登录");
        }
        if (stringRedisTemplate.hasKey(redisKey)){
            Long targetUser = Long.parseLong(stringRedisTemplate.opsForValue().get(redisKey)==null?"0":stringRedisTemplate.opsForValue().get(redisKey));
            if (targetUser!=null && targetUser>0){
                return SuccessTip.create(userAccountMapper.selectById(targetUser));
            }
        }

        return SuccessTip.create();
    }


    @PutMapping("/users/current")
    public Tip setCurrentUser(@RequestBody JSONObject jsonObject){
        Long userId = JWTKit.getUserId();
        String redisKey = redisKeyPrefix+String.valueOf(userId);
        if (userId==null){
            throw new BusinessException(BusinessCode.NoPermission,"没有登录");
        }

        if (jsonObject==null || jsonObject.get("id")==null){
           throw new BusinessException(BusinessCode.BadRequest,"id必填项");
        }
        stringRedisTemplate.opsForValue().set(redisKey, jsonObject.get("id").toString());
        return SuccessTip.create(1);
    }

    @DeleteMapping("/users/current")
    public Tip deleteCurrentUser(){
        Long userId = JWTKit.getUserId();
        String redisKey = redisKeyPrefix+String.valueOf(userId);
        if (userId==null){
            throw new BusinessException(BusinessCode.NoPermission,"没有登录");
        }
        stringRedisTemplate.delete(redisKey);
        return SuccessTip.create(1);
    }


}
