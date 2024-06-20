/*
 Navicat Premium Data Transfer

 Source Server         : 测试数据库
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : 121.37.174.177:3306
 Source Schema         : libra_2

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 16/06/2021 23:02:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for random_config
-- ----------------------------
DROP TABLE IF EXISTS `random_config`;
CREATE TABLE `random_config` (
                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                 `bar_rate` double DEFAULT NULL,
                                 `decline` double DEFAULT NULL,
                                 `lead_wire` double DEFAULT NULL,
                                 `low_deg` double DEFAULT NULL,
                                 `period` varchar(255) DEFAULT NULL,
                                 `random_rate` int DEFAULT NULL,
                                 `symbol` varchar(255) DEFAULT NULL,
                                 `start` decimal(19,2) DEFAULT NULL,
                                 `symbol_list` varchar(255) DEFAULT NULL,
                                 `symbol_rate_list` varchar(255) DEFAULT NULL,
                                 `update_rate` double DEFAULT NULL COMMENT '日涨幅',
                                 `up_rate` decimal(19,2) DEFAULT NULL,
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of random_config
-- ----------------------------
BEGIN;
INSERT INTO `random_config` VALUES (2, 1, 0.2, 2, 1.5, '1min', 1440, 'creusdt', 0.01, 'ruffeth,crebtc', '2500,38487', 0.01, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
