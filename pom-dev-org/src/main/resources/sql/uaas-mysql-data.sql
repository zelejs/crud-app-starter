SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


INSERT INTO `t_sys_org` (`id`, `pid`, `org_type`,`org_code`,`name`, `full_name`, `node_level`, `left_num`, `right_num`, `note`, `status`)
VALUES ('1', null, 0,'A1','SmallSaaS', 'SmallSaaS', '1', '1', '16', '平台','NORMAL');


INSERT INTO `t_sys_org` (`id`, `pid`, `name`, `org_code`, `full_name`, `node_level`, `left_num`, `right_num`, `note`, `status`, `org_type`, `create_time`, `update_time`) VALUES
('100000000000000010', '1', '总公司', 'System', '总公司', '2', '2', '13', '测试', 'NORMAL', '2', '2020-04-27 01:10:22', '2020-07-29 14:35:02');
INSERT INTO `t_sys_org` (`id`, `pid`, `name`, `org_code`, `full_name`, `node_level`, `left_num`, `right_num`, `note`, `status`, `org_type`, `create_time`, `update_time`) VALUES
('100000000000000011', '100000000000000010', 'AAA', 'AAA', 'AAA', '3', '3', '8', NULL, 'NORMAL', '4', '2020-07-29 14:32:25', '2020-07-29 14:35:02');
INSERT INTO `t_sys_org` (`id`, `pid`, `name`, `org_code`, `full_name`, `node_level`, `left_num`, `right_num`, `note`, `status`, `org_type`, `create_time`, `update_time`) VALUES
('100000000000000012', '100000000000000010', 'BBB', 'BBB', 'BBB', '3', '9', '12', NULL, 'NORMAL', '4', '2020-07-29 14:33:09', '2020-07-29 14:35:02');
INSERT INTO `t_sys_org` (`id`, `pid`, `name`, `org_code`, `full_name`, `node_level`, `left_num`, `right_num`, `note`, `status`, `org_type`, `create_time`, `update_time`) VALUES
('100000000000000013', '100000000000000011', 'A-1', 'A-1', 'A-1', '4', '4', '7', NULL, 'NORMAL', '4', '2020-07-29 14:34:32', '2020-07-29 14:35:02');
INSERT INTO `t_sys_org` (`id`, `pid`, `name`, `org_code`, `full_name`, `node_level`, `left_num`, `right_num`, `note`, `status`, `org_type`, `create_time`, `update_time`) VALUES
('100000000000000014', '100000000000000012', 'B-1', 'B-1', 'B-1', '4', '10', '11', NULL, 'NORMAL', '4', '2020-07-29 14:34:46', '2020-07-29 14:35:02');
INSERT INTO `t_sys_org` (`id`, `pid`, `name`, `org_code`, `full_name`, `node_level`, `left_num`, `right_num`, `note`, `status`, `org_type`, `create_time`, `update_time`) VALUES
('100000000000000015', '100000000000000013', 'A-1-1', 'A-1-1', 'A-1-1', '5', '5', '6', NULL, 'NORMAL', '4', '2020-07-29 14:35:02', '2020-07-29 14:35:02');

-- 其他租户
INSERT INTO `t_sys_org` (`id`, `pid`, `name`, `org_code`, `full_name`, `node_level`, `left_num`, `right_num`, `note`, `status`, `org_type`, `create_time`, `update_time`) VALUES
('100001000000000001', '1', '租户2', 't2', '租户2', '2', '14', '15', NULL, 'NORMAL', '0', '2020-08-14 14:28:27', '2020-08-14 14:31:27');

-- 用户
INSERT INTO `t_sys_user` (`id`, `avatar`, `account`, `org_id`, `openid`, `password`, `salt`, `name`, `birthday`, `sex`, `email`, `email_validated`, `phone`, `status`, `createtime`, `version`, `delete_flag`, `is_app_user`, `password_reset`,`user_type`,`extra_user_type`) VALUES
('876708082437197912', NULL, 'A-1_U1', '100000000000000013', NULL, '90f7ae641987c3935bcfd32dc20d850d', 'yqb1m', 'A-1_U1', NULL, NULL, NULL, '0', NULL, '1', '2020-07-29 14:37:08', NULL, '0', '0', '0',1,1);
INSERT INTO `t_sys_user` (`id`, `avatar`, `account`, `org_id`, `openid`, `password`, `salt`, `name`, `birthday`, `sex`, `email`, `email_validated`, `phone`, `status`, `createtime`, `version`, `delete_flag`, `is_app_user`, `password_reset`,`user_type`,`extra_user_type`) VALUES
('876708082437197913', NULL, 'A-1-1_U1', '100000000000000015', NULL, 'da853564706d8874cb6392d5e6b03439', 'mp6qs', 'A-1-1_U1', NULL, NULL, NULL, '0', NULL, '1', '2020-07-29 14:37:23', NULL, '1', '0', '0',1,1);
INSERT INTO `t_sys_user` (`id`, `avatar`, `account`, `org_id`, `openid`, `password`, `salt`, `name`, `birthday`, `sex`, `email`, `email_validated`, `phone`, `status`, `createtime`, `version`, `delete_flag`, `is_app_user`, `password_reset`,`user_type`,`extra_user_type`) VALUES
('876708082437197914', NULL, 'B-1_U1', '100000000000000014', NULL, '19ba0a4c6b1942143c795166548e9768', 'o3n8k', 'B-1_U1', NULL, NULL, NULL, '0', NULL, '1', '2020-07-29 14:37:43', NULL, '0', '0', '0',1,1);
INSERT INTO `t_sys_user` (`id`, `avatar`, `account`, `org_id`, `openid`, `password`, `salt`, `name`, `birthday`, `sex`, `email`, `email_validated`, `phone`, `status`, `createtime`, `version`, `delete_flag`, `is_app_user`, `password_reset`,`user_type`,`extra_user_type`) VALUES
('876708082437197915', NULL, 'BBB_U1', '100000000000000012', NULL, '884eaae6d50f8875858c4cc57754a261', 'l2z75', 'BBB_U1', NULL, NULL, NULL, '0', NULL, '1', '2020-07-29 14:37:58', NULL, '0', '0', '0',1,1);
INSERT INTO `t_sys_user` (`id`, `avatar`, `account`, `org_id`, `openid`, `password`, `salt`, `name`, `birthday`, `sex`, `email`, `email_validated`, `phone`, `status`, `createtime`, `version`, `delete_flag`, `is_app_user`, `password_reset`,`user_type`,`extra_user_type`) VALUES
('876708082437197916', NULL, 'AAA_U1', '100000000000000011', NULL, '25ccd0b84de7cc7f74af76093fa06f8f', 'wjjsy', 'AAA_U1', NULL, '0', NULL, '0', NULL, '1', '2020-07-29 14:38:17', NULL, '0', '0', '0',1,1);

INSERT INTO `t_sys_user` (`id`, `avatar`, `account`, `org_id`, `openid`, `password`, `salt`, `name`, `birthday`, `sex`, `email`, `email_validated`, `phone`, `status`, `createtime`, `version`, `delete_flag`, `is_app_user`, `password_reset`, `user_type`, `tenant_org_id`, `extra_user_type`) VALUES
('876708082437197917', NULL, 'CCC_U1', '100001000000000001', NULL, '25ccd0b84de7cc7f74af76093fa06f8f', 'wjjsy', 'CCC_U1', NULL, NULL, NULL, '0', NULL, '1', '2020-08-14 14:44:02', NULL, '0', '0', '0', 1, NULL, 1);


INSERT INTO `t_tenant` (`id`, `name`, `org_id`, `app_id`, `domain`, `status`, `start_time`) VALUES ('1', 'admin', '1', '1', 'test.com', '0', NULL);
INSERT INTO `t_tenant` (`id`, `name`, `org_id`, `app_id`, `domain`, `status`, `start_time`) VALUES ('2', 'git.smallsaas.cn', '1', '1', '120.79.77.207', '1', '2020-04-22 15:54:30');
INSERT INTO `t_tenant` (`id`, `name`, `org_id`, `app_id`, `domain`, `status`, `start_time`) VALUES ('3', 'localhost', '100000000000000010', '1', 'localhost', '1', '2020-04-27 01:19:37');
INSERT INTO `t_tenant` (`id`, `name`, `org_id`, `app_id`, `domain`, `status`, `start_time`) VALUES ('8', 'name', '100001000000000001', NULL, 'ceshi1', '1', '2020-05-18 17:58:51');
