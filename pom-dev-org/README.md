# 如何为所有测试用户分配所有接口访问权限

## 调用开放API即可 [仅对测试用户有效]

```
参数： accounts 为用户登录账号(account), 不提交 Body 默认对所有测试用户生效
```

* POST /openapi/dev/setAllPerms BODY  {"accounts":["AAA","A-1_U1"]}
    
