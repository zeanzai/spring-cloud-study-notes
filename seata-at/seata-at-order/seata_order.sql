/*
 Navicat Premium Data Transfer

 Source Server         : home
 Source Server Type    : MySQL
 Source Server Version : 50736 (5.7.36)
 Source Host           : 192.168.56.10:3306
 Source Schema         : seata_order

 Target Server Type    : MySQL
 Target Server Version : 50736 (5.7.36)
 File Encoding         : 65001

 Date: 11/12/2022 11:16:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(11) DEFAULT NULL COMMENT '商品Id',
  `num` bigint(11) DEFAULT NULL COMMENT '数量',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户唯一Id',
  `create_time` datetime DEFAULT NULL,
  `status` int(1) DEFAULT NULL COMMENT '订单状态 1 未付款 2 已付款 3 已完成',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of t_order
-- ----------------------------
BEGIN;
INSERT INTO `t_order` (`id`, `product_id`, `num`, `user_id`, `create_time`, `status`) VALUES (7, 1, 2, 'abc123', '2022-11-12 17:43:38', 2);
INSERT INTO `t_order` (`id`, `product_id`, `num`, `user_id`, `create_time`, `status`) VALUES (8, 1, 2, 'abc123', '2022-11-12 17:46:24', 2);
INSERT INTO `t_order` (`id`, `product_id`, `num`, `user_id`, `create_time`, `status`) VALUES (9, 1, 2, 'abc123', '2022-11-12 17:51:13', 2);
INSERT INTO `t_order` (`id`, `product_id`, `num`, `user_id`, `create_time`, `status`) VALUES (10, 1, 2, 'abc123', '2022-11-12 17:51:15', 2);
INSERT INTO `t_order` (`id`, `product_id`, `num`, `user_id`, `create_time`, `status`) VALUES (11, 1, 2, 'abc123', '2022-11-12 17:51:17', 2);
INSERT INTO `t_order` (`id`, `product_id`, `num`, `user_id`, `create_time`, `status`) VALUES (12, 1, 2, 'abc123', '2022-11-12 17:51:48', 2);
COMMIT;

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log` (
  `branch_id` bigint(20) NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(100) NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int(11) NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='AT transaction mode undo table';

-- ----------------------------
-- Records of undo_log
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
