
SET FOREIGN_KEY_CHECKS=0;

drop table if exists t_navigation;
CREATE TABLE `t_navigation` (
                                `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                `name` varchar(50) DEFAULT NULL,
                                `url` text,
                                `path` text,
                                `title` varchar(50) DEFAULT NULL,
                                `desc` varchar(255) DEFAULT NULL,
                                `org_id` bigint(20) DEFAULT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;