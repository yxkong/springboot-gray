SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_account
-- ----------------------------
DROP TABLE IF EXISTS `t_account`;
CREATE TABLE `t_account` (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT,
                             `mobile` varchar(11) COLLATE utf8_bin NOT NULL COMMENT '手机号码',
                             `nick_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '昵称',
                             `age` int(4) DEFAULT NULL COMMENT '年龄',
                             `sex` tinyint(1) DEFAULT NULL COMMENT '性别',
                             `create_time` datetime DEFAULT NULL COMMENT '注册时间',
                             `status` tinyint(1) DEFAULT NULL COMMENT '状态',
                             `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `unq_idx_mobile` (`mobile`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=903 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_account
-- ----------------------------
BEGIN;
INSERT INTO `t_account` VALUES (1, '15600000269', 'yxkong', 21, 1, '2021-06-18 19:11:53', NULL, NULL);
INSERT INTO `t_account` VALUES (901, '15600000270', '鱼翔空', 30, 1, '2021-06-21 19:11:53', NULL, '符合灰度规则');
INSERT INTO `t_account` VALUES (902, '15600000279', 'xxx', 25, 1, '2021-06-26 19:11:53', NULL, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
