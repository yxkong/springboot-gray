SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_gray_rule
-- ----------------------------
DROP TABLE IF EXISTS `t_gray_rule`;
CREATE TABLE `t_gray_rule` (
                               `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '规则id',
                               `rule` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '规则',
                               `desc` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '规则描述',
                               `lable` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '标签',
                               `version` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '版本',
                               `status` int(1) DEFAULT NULL COMMENT '状态，0禁用，1启用',
                               `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                               `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='灰度规则表';

-- ----------------------------
-- Records of t_gray_rule
-- ----------------------------
BEGIN;
INSERT INTO `t_gray_rule` VALUES (1, '(string.startsWith(\'9,0\',mobileMod) && userId>=901 && registerTime>\'2021-06-01 00:00:00\')', '尾号为9,0,且userId>901,注册时间大于2021年6月25日', 'gray', '2.0', 1, '2021-06-28 19:05:14', '2021-06-28 23:21:37');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
