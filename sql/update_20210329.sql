alter table bg_bline add column real_amount DECIMAL(50,18) COMMENT '实际数量';
CREATE TABLE `bg_slip_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `start_time` bigint DEFAULT NULL COMMENT '开始时间',
  `end_time` bigint DEFAULT NULL COMMENT '结束时间',
  `open_flag` bit(1) DEFAULT b'0' COMMENT '0表示关闭1表示开启',
  `add_value` decimal(50,18) DEFAULT NULL COMMENT '增量',
  `symbol` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '交易对',
  `del_flag` bit(1) DEFAULT b'0' COMMENT '0表示未删除 1表示删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建十年就能',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `interval_time` int DEFAULT NULL COMMENT '花费时间',
  `add_amount` decimal(50,18) DEFAULT NULL COMMENT '交易数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
