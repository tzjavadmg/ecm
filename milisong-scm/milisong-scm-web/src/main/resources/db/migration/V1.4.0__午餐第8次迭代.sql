CREATE TABLE IF NOT EXISTS  `milisong_scm`.`goods_lunch` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `code` varchar(20) DEFAULT NULL COMMENT '商品code/sku',
  `name` varchar(50) DEFAULT NULL COMMENT '商品名称',
  `category_code` varchar(20) DEFAULT NULL COMMENT '类目code',
  `category_name` varchar(50) DEFAULT NULL COMMENT '类目名称',
  `describe` varchar(500) DEFAULT NULL COMMENT '商品详情',
  `advise_price` decimal(12,2) DEFAULT NULL COMMENT '建议零售价',
  `preferential_price` decimal(12,2) DEFAULT NULL COMMENT '实际售价',
  `discount` decimal(5,1) DEFAULT NULL COMMENT '折扣',
  `picture` varchar(100) DEFAULT NULL COMMENT '小图片',
  `big_picture` varchar(100) DEFAULT NULL COMMENT '大图片',
  `spicy` tinyint(2) DEFAULT '0' COMMENT '辣度 0不辣 1微辣 2中辣 3重辣 4变态辣',
  `taste` varchar(100) DEFAULT NULL COMMENT '口味',
  `weight` tinyint(3) unsigned DEFAULT NULL COMMENT '排序号(权重)',
  `status` tinyint(2) DEFAULT NULL COMMENT '商品状态 1使用中 2已停用',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 1是 0否',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='午餐-商品表';

CREATE TABLE IF NOT EXISTS  `milisong_scm`.`goods_schedule_lunch` (
  `id` bigint(20) NOT NULL,
  `shop_id` bigint(20) DEFAULT NULL COMMENT '门店d',
  `shop_code` varchar(20) DEFAULT NULL COMMENT '门店code',
  `shop_name` varchar(100) DEFAULT NULL COMMENT '门店名称',
  `year` int(4) DEFAULT NULL COMMENT '年份',
  `week_of_year` int(4) DEFAULT NULL COMMENT 'W多少周',
  `schedule_date` date DEFAULT NULL COMMENT '档期日期',
  `week` int(2) DEFAULT NULL COMMENT '周几',
  `status` tinyint(1) DEFAULT NULL COMMENT '是可可售 1可售 0不可售',
  `publish_time` timestamp NULL DEFAULT NULL COMMENT '发布时间(生效时间)',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 1是 0否',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_shop_id` (`shop_id`) USING BTREE,
  KEY `idx_year_week_of_year` (`year`,`week_of_year`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='午餐-商品档期主表';

CREATE TABLE IF NOT EXISTS  `milisong_scm`.`goods_schedule_detail_lunch` (
  `id` bigint(20) NOT NULL,
  `shop_id` bigint(20) DEFAULT NULL COMMENT '门店d',
  `shop_code` varchar(20) DEFAULT NULL COMMENT '门店code',
  `shop_name` varchar(100) DEFAULT NULL COMMENT '门店名称',
  `year` int(4) DEFAULT NULL COMMENT '年份',
  `week_of_year` int(4) DEFAULT NULL COMMENT 'W多少周',
  `goods_code` varchar(20) DEFAULT NULL COMMENT '商品编码',
  `goods_name` varchar(50) DEFAULT NULL COMMENT '商品名字',
  `flag_1` tinyint(1) DEFAULT NULL COMMENT '第1天是否可售 1可售 0不可售',
  `flag_2` tinyint(1) DEFAULT NULL COMMENT '第2天是否可售 1可售 0不可售',
  `flag_3` tinyint(1) DEFAULT NULL COMMENT '第3天是否可售 1可售 0不可售',
  `flag_4` tinyint(1) DEFAULT NULL COMMENT '第4天是否可售 1可售 0不可售',
  `flag_5` tinyint(1) DEFAULT NULL COMMENT '第5天是否可售 1可售 0不可售',
  `flag_6` tinyint(1) DEFAULT NULL COMMENT '第6天是否可售 1可售 0不可售',
  `flag_7` tinyint(1) DEFAULT NULL COMMENT '第7天是否可售 1可售 0不可售',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 1是 0否',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_shop_id` (`shop_id`) USING BTREE,
  KEY `idx_year_week_of_year` (`year`,`week_of_year`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='午餐-商品档期明细';

CREATE TABLE IF NOT EXISTS  `milisong_scm`.`goods_salable_quantity_lunch` (
  `id` bigint(20) NOT NULL,
  `shop_id` bigint(20) DEFAULT NULL COMMENT '门店id',
  `shop_code` varchar(20) DEFAULT NULL COMMENT '门店code',
  `shop_name` varchar(20) DEFAULT NULL COMMENT '门店name',
  `goods_code` varchar(20) DEFAULT NULL COMMENT '商品code',
  `goods_name` varchar(20) DEFAULT NULL COMMENT '商品name',
  `sale_date` date DEFAULT NULL COMMENT '可售日期',
  `available_volume` int(5) DEFAULT NULL COMMENT '可售数量',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 1是 0否',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人  账号_名字组合',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '最后修改人 账号_名字组合',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='午餐-门店可售商品量配置表';






