DROP TABLE IF EXISTS `db_snapshot_message`;
CREATE TABLE `db_snapshot_message` (
`id`  bigint(20)  NOT NULL  AUTO_INCREMENT,
`event_time` datetime NOT NULL COMMENT '操作时间',
`type`  varchar(255)  NOT NULL COMMENT '操作类型{Backup,Restore}',
`author`  varchar(255)  NOT NULL COMMENT '操作者',
`sql_path` varchar(26) NOT NULL COMMENT'sql文件名',
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;