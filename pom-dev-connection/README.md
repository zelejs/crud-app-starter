# 新需求

### 数据库非全量下载
仅下载数据库中的部分数据，一般为可以保证程序正确启动的必要业务数据, 通过配置命名下载规则, 下载时选择下载规则名称进行非全量下载（命名规则以系统文件方式保存）。
>GET /dev/dependency/snapshot?ruler=<ruler_file_name>&rule=defined
>


### 第二部分 命名规则管理
命名规则定义, rule=defined 仅下载定义表, rule=full下载全部表
> `[*]`代表数据表全量数据
```json
{
    "t_user": [1],
    "t_shedule": [1,2],
    "t_task": ["1-10","20-30"],
    "t_task_user": ["*"]
}
```

#### 命名规则列表
表出所有命名规则文件名称
```
GET /dev/dependency/snapshot/rulers
```
#### 命名规则详情
查看具体的命名规则的配置详情
```
GET /dev/dependency/snapshot/rulers/<ruler_file_name>
```
#### 创建或更新命名规则
创建与更新同一个接口，没有命名规则文件即创建，存在即更新
```
POST /dev/dependency/snapshot/rulers/<ruler_file_name>
```
#### 删除命名规则
DELETE /dev/dependency/snapshot/rulers/<ruler_file_name>

### 第三部分 静态资源下载
`[TBD]`包括图片等
