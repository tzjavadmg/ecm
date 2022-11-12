/* 
米立送早餐 1.0版本数据库变更
*/
CREATE TABLE IF NOT EXISTS `milisong_scm`.`company` (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '公司名字',
  `building_name` varchar(100) DEFAULT NULL COMMENT '楼宇名字',
  `shop_id` bigint(20) DEFAULT NULL COMMENT '门店id',
  `shop_code` varchar(20) DEFAULT NULL COMMENT '门店code',
  `shop_name` varchar(100) DEFAULT NULL COMMENT '门店名称',
  `city_code` varchar(10) DEFAULT NULL COMMENT '所在城市code',
  `city_name` varchar(50) DEFAULT NULL COMMENT '所在城市名称',
  `region_code` varchar(10) DEFAULT NULL COMMENT '所在区code',
  `region_name` varchar(50) DEFAULT NULL COMMENT '所在区名称',
  `detail_address` varchar(200) DEFAULT NULL COMMENT '不需要包含城市和区县的公司详细地址',
  `floor` varchar(10) DEFAULT NULL COMMENT '楼层',
  `delivery_time_begin` time DEFAULT NULL COMMENT '配送时间begin',
  `delivery_time_end` time DEFAULT NULL COMMENT '配送时间end',
  `working_hours` time DEFAULT NULL COMMENT '上班时间',
  `weight` int(3) NULL COMMENT '权重（排序号）',
  `open_status` tinyint(2) DEFAULT NULL COMMENT '开通状态 1开通 0关闭',
  `contact_person` varchar(100) DEFAULT NULL COMMENT '联络人信息',
  `meal_address_count` int(2) DEFAULT NULL COMMENT '取餐点数量',
  `lon_baidu` decimal(20,15) DEFAULT NULL COMMENT '经度-百度',
  `lat_baidu` decimal(20,15) DEFAULT NULL COMMENT '纬度-百度',
  `lon_gaode` decimal(20,15) DEFAULT NULL COMMENT '经度-高德',
  `lat_gaode` decimal(20,15) DEFAULT NULL COMMENT '纬度-高德',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='早餐-公司信息';

CREATE TABLE IF NOT EXISTS `milisong_scm`.`company_meal_address`  (
  `id` bigint(20) NOT NULL,
  `company_id` bigint(20) NULL COMMENT '公司id',
  `meal_address` varchar(100) NULL COMMENT '取餐点名称',
  `picture` varchar(255) NULL COMMENT '取餐点图片',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  INDEX `idx_company_id`(`company_id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '早餐-公司取餐点信息';

CREATE TABLE IF NOT EXISTS `goods` (
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
  `shelf_life` tinyint(3) unsigned DEFAULT NULL COMMENT '保质期(天)',
  `taste` varchar(100) DEFAULT NULL COMMENT '口味',
  `weight` tinyint(3) unsigned DEFAULT NULL COMMENT '排序号(权重)',
  `status` tinyint(2) DEFAULT NULL COMMENT '商品状态 2使用中 5已停用',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 1是 0否',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='早餐-商品表';

CREATE TABLE IF NOT EXISTS `goods_category` (
  `id` int(11) NOT NULL COMMENT '主键',
  `code` varchar(20) DEFAULT NULL COMMENT '类目code',
  `name` varchar(50) DEFAULT NULL COMMENT '类目名称',
  `picture` varchar(100) DEFAULT NULL COMMENT '类目图片',
  `describe` varchar(100) DEFAULT NULL COMMENT '类目简介',
  `weight` tinyint(3) unsigned DEFAULT NULL COMMENT '排序号(权重)',
  `pid` bigint(20) DEFAULT NULL COMMENT '父类目id',
  `status` tinyint(2) DEFAULT NULL COMMENT '状态1有效 0无效',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='早餐-类目表';

CREATE TABLE IF NOT EXISTS `milisong_scm`.`goods_schedule`  (
  `id` bigint(20) NOT NULL,
  `shop_id` bigint(20) NULL COMMENT '门店d',
  `shop_code` varchar(20) NULL COMMENT '门店code',
  `shop_name` varchar(100) NULL COMMENT '门店名称',
  `year` int(4) NULL COMMENT '年份',
  `week_of_year` int(4) NULL COMMENT 'W多少周',
  `schedule_date` date NULL COMMENT '档期日期',
  `week` int(2) NULL COMMENT '周几',
  `status` tinyint(1) NULL COMMENT '是可可售 1可售 0不可售',
  `publish_time` timestamp(0) NULL COMMENT '发布时间(生效时间)',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  INDEX `idx_shop_id`(`shop_id`) USING BTREE,
  INDEX `idx_year_week_of_year`(`year`, `week_of_year`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '早餐-商品档期主表';

CREATE TABLE IF NOT EXISTS `milisong_scm`.`goods_schedule_detail`  (
  `id` bigint(20) NOT NULL,
  `shop_id` bigint(20) NULL COMMENT '门店d',
  `shop_code` varchar(20) NULL COMMENT '门店code',
  `shop_name` varchar(100) NULL COMMENT '门店名称',
  `year` int(4) NULL COMMENT '年份',
  `week_of_year` int(4) NULL COMMENT 'W多少周',
  `goods_code` varchar(20) NULL COMMENT '商品编码',
  `goods_name` varchar(50) NULL COMMENT '商品名字',
  `flag_1` tinyint(1) DEFAULT NULL COMMENT '第1天是否可售 1可售 0不可售',
  `flag_2` tinyint(1) DEFAULT NULL COMMENT '第2天是否可售 1可售 0不可售',
  `flag_3` tinyint(1) DEFAULT NULL COMMENT '第3天是否可售 1可售 0不可售',
  `flag_4` tinyint(1) DEFAULT NULL COMMENT '第4天是否可售 1可售 0不可售',
  `flag_5` tinyint(1) DEFAULT NULL COMMENT '第5天是否可售 1可售 0不可售',
  `flag_6` tinyint(1) DEFAULT NULL COMMENT '第6天是否可售 1可售 0不可售',
  `flag_7` tinyint(1) DEFAULT NULL COMMENT '第7天是否可售 1可售 0不可售',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  INDEX `idx_shop_id`(`shop_id`) USING BTREE,
  INDEX `idx_year_week_of_year`(`year`, `week_of_year`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '早餐-商品档期明细';

CREATE TABLE IF NOT EXISTS `order` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `order_no` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '订单编号',
  `shop_id` bigint(20) DEFAULT NULL COMMENT '门店id',
  `shop_code` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '门店编码',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `real_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '用户姓名',
  `mobile_no` varchar(11) COLLATE utf8_bin DEFAULT NULL COMMENT '手机号',
  `delivery_start_date` datetime DEFAULT NULL COMMENT '配送起始时间:预定的第二天配送',
  `delivery_end_date` datetime DEFAULT NULL COMMENT '配送结束时间',
  `delivery_office_building_id` bigint(20) DEFAULT NULL COMMENT '配送楼宇:写字楼名称ID',
  `delivery_company_id` bigint(20) DEFAULT NULL COMMENT '配送公司：ID',
  `delivery_company` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '配送公司:公司名称',
  `delivery_address` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '配送地址',
  `delivery_floor` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '楼层',
  `delivery_room` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '房间号',
  `goods_sum` int(3) DEFAULT NULL COMMENT '订单中商品总数量',
  `total_amount` decimal(10,2) DEFAULT NULL COMMENT '订单总金额',
  `actual_pay_amount` decimal(10,2) DEFAULT NULL COMMENT '实际支付金额',
  `delivery_cost_amount` decimal(10,2) DEFAULT NULL COMMENT '配送费金额',
  `discount_amount` decimal(10,2) DEFAULT NULL COMMENT '折扣后金额',
  `package_amount` decimal(10,2) DEFAULT '0.00' COMMENT '打包费',
  `red_packet_amount` decimal(10,2) DEFAULT '0.00' COMMENT '红包金额',
  `complete_time` timestamp NULL DEFAULT NULL COMMENT '配送完成时间',
  `order_status` tinyint(2) DEFAULT NULL COMMENT '2:已支付 3:已申请退款 4:已退款',
  `delivery_status` tinyint(2) DEFAULT '-1' COMMENT '-1备餐中 0 已派单 1已接单 2已到店  3已取餐 4已完成',
  `orderset_process_status` tinyint(1) DEFAULT '0' COMMENT '集单处理状态 1已处理 0未处理',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `order_type` tinyint(2) DEFAULT '0' COMMENT '订单类型0:早餐;1:中餐',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单表-早餐';

CREATE TABLE IF NOT EXISTS `order_detail` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单ID',
  `order_no` varchar(50) DEFAULT NULL COMMENT '订单编号',
  `goods_code` varchar(20) DEFAULT NULL COMMENT '商品ID',
  `goods_name` varchar(50) DEFAULT NULL COMMENT '商品名称',
  `goods_price` decimal(10,2) DEFAULT NULL COMMENT '商品价格',
  `goods_count` int(11) DEFAULT NULL COMMENT '商品数量',
  `goods_discount_price` decimal(10,2) DEFAULT NULL COMMENT '商品折扣后价格',
  `actual_pay_amount` decimal(10,2) DEFAULT NULL COMMENT '商品实际支付总金额',
  `delivery_cost_amount` decimal(10,2) DEFAULT NULL COMMENT '平摊商品配送费',
  `package_amount` decimal(10,2) DEFAULT NULL COMMENT '平摊打包费',
  `red_packet_amount` decimal(10,2) DEFAULT NULL COMMENT '平摊红包金额',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单明细表-早餐';

-- ----------------------------
-- Table structure for global_config
-- ----------------------------
CREATE TABLE IF NOT EXISTS `global_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service_line` varchar(20) DEFAULT '0' COMMENT '业务线：1-ecm，2-scm，3-pos，0-common',
  `config_key` varchar(50) DEFAULT NULL COMMENT '配置key',
  `config_describe` varchar(50) DEFAULT NULL COMMENT '配置key描述',
  `config_value` varchar(200) DEFAULT NULL COMMENT '配置value',
  `config_value_type` tinyint(2) DEFAULT '1' COMMENT '配置值的类型 1字符串 2整数 3小数 4日期 5时间 6日期+时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 1是 0否',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='门店全局配置表';

-- ----------------------------
-- Table structure for shop_banner
-- ----------------------------
CREATE TABLE IF NOT EXISTS `shop_banner` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `shop_id` bigint(20) DEFAULT NULL COMMENT '门店id',
  `shop_code` varchar(20) DEFAULT NULL COMMENT '门店code',
  `shop_name` varchar(50) DEFAULT NULL COMMENT '门店名称',
  `picture` varchar(100) DEFAULT NULL COMMENT '门店轮播图片',
  `link_url` varchar(100) DEFAULT NULL COMMENT '链接地址',
  `weight` tinyint(2) DEFAULT '0' COMMENT 'banner权重，大优先',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 1是 0否',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '最后修改人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='门店轮播图片表';

-- ----------------------------
-- Table structure for shop_intercept_config
-- ----------------------------
CREATE TABLE IF NOT EXISTS `shop_intercept_config` (
  `id` bigint(20) NOT NULL DEFAULT '1',
  `shop_id` bigint(20) DEFAULT NULL COMMENT '门店id',
  `shop_code` varchar(20) DEFAULT NULL COMMENT '门店code',
  `shop_name` varchar(50) DEFAULT NULL COMMENT '门店名称',
  `order_intercept_time` time DEFAULT NULL COMMENT '订单截单时间',
  `first_orderset_time` time DEFAULT NULL COMMENT '第一次集单时间',
  `second_orderset_time` time DEFAULT NULL COMMENT '第二次集单时间',
  `delivery_time_begin` time DEFAULT NULL COMMENT '配送开始时间',
  `delivery_time_end` time DEFAULT NULL COMMENT '配送结束时间',
  `is_default` tinyint(1) DEFAULT '0' COMMENT '是否默认配送设置 1是 0否',
  `max_output` int(5) DEFAULT NULL COMMENT '最大生产量',
  `status` tinyint(2) DEFAULT '1' COMMENT '状态 1有效 0无效',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 1是 0否',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建时间',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='门店截单配置表';

-- ----------------------------
-- Table structure for shop_single_config
-- ----------------------------
CREATE TABLE IF NOT EXISTS `shop_single_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shop_id` bigint(20) DEFAULT NULL COMMENT '门店d',
  `shop_code` varchar(20) DEFAULT NULL COMMENT '门店code',
  `shop_name` varchar(50) DEFAULT NULL COMMENT '门店名称',
  `service_line` varchar(20) DEFAULT '0' COMMENT '业务线：1-ecm，2-scm，3-pos，0-common',
  `config_key` varchar(50) DEFAULT NULL COMMENT '配置key',
  `config_describe` varchar(50) DEFAULT NULL COMMENT '配置key描述',
  `config_value` varchar(50) DEFAULT NULL COMMENT '配置value',
  `config_value_type` tinyint(2) DEFAULT '1' COMMENT '配置值的类型 1字符串 2整数 3小数 4日期 5时间 6日期+时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 1是 0否',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='门店单个配置表';

CREATE TABLE IF NOT EXISTS `order_set_detail` (
  `id` bigint(20) NOT NULL,
  `detail_set_no` varchar(50) DEFAULT NULL COMMENT '集单单号',
  `detail_set_no_description` varchar(100) DEFAULT NULL COMMENT '集单号描述',
  `detail_delivery_date` date DEFAULT NULL COMMENT '配送日期',
  `delivery_start_time` time DEFAULT NULL COMMENT '配送开始时间',
  `delivery_end_time` time DEFAULT NULL COMMENT '配送结束时间',
  `detail_delivery_address` varchar(200) DEFAULT NULL COMMENT '订单详细配送地址',
  `shop_id` bigint(20) DEFAULT NULL COMMENT '门店id',
  `building_id` bigint(20) DEFAULT NULL COMMENT '楼宇id',
  `building_abbreviation` varchar(50) DEFAULT NULL COMMENT '楼宇简称',
  `delivery_floor` varchar(50) DEFAULT NULL COMMENT '楼层',
  `company_id` bigint(20) DEFAULT NULL COMMENT '公司id',
  `company_abbreviation` varchar(50) DEFAULT NULL COMMENT '公司简称/房间号',
  `goods_sum` int(4) DEFAULT NULL COMMENT '子集单中商品总数量',
  `actual_pay_amount` decimal(10,2) DEFAULT NULL COMMENT '实付总金额',
  `is_print` tinyint(1) DEFAULT '0' COMMENT '是否已打印 1是 0否',
  `status` tinyint(2) DEFAULT '1' COMMENT '状态 1待打包 2已打包 3配送中 4已通知',
  `distribution_status` tinyint(2) DEFAULT NULL COMMENT '配送状态 -1初始化 0已派单 1已接单 2已到店 3已取件 4已送达',
  `is_push` tinyint(1) DEFAULT '0' COMMENT '是否已发MQ 1是 0否',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 1是 0否',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人  账号_名字组合',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '最后修改人 账号_名字组合',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='集单表-早餐';

CREATE TABLE IF NOT EXISTS `order_set_detail_goods` (
  `id` bigint(20) NOT NULL,
  `detail_set_no` varchar(50) DEFAULT NULL COMMENT '子集单单号',
  `order_no` varchar(50) DEFAULT NULL COMMENT '订单编号',
  `couplet_no` varchar(50) DEFAULT NULL COMMENT '订单联的编号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '订餐用户id',
  `user_name` varchar(50) DEFAULT NULL COMMENT '订餐用户名字',
  `user_phone` varchar(20) DEFAULT NULL COMMENT '订餐用户手机号',
  `goods_code` varchar(20) DEFAULT NULL COMMENT '商品编号',
  `goods_name` varchar(50) DEFAULT NULL COMMENT '商品名字',
  `goods_number` int(5) DEFAULT NULL COMMENT '数量',
  `actual_pay_amount` decimal(10,2) DEFAULT NULL COMMENT '单件商品实付金额',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 1是 0否',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人  账号_名字组合',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '最后修改人 账号_名字组合',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_detail_set_no` (`detail_set_no`) USING BTREE,
  KEY `idx_order_no` (`order_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='集单商品明细表-早餐';

CREATE TABLE IF NOT EXISTS `shop_goods_salable_quantity` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='门店可售商品量配置表';

INSERT INTO `milisong_scm`.`global_config` (`id`, `service_line`, `config_key`, `config_describe`, `config_value`, `config_value_type`) VALUES ('1', '0', 'sharePictureUrl', '小程序分享图片地址', 'http://images.jshuii.com/0/d50477b93f6741318c9646a8888f10dd.jpg', '1');
INSERT INTO `milisong_scm`.`global_config` (`id`, `service_line`, `config_key`, `config_describe`, `config_value`, `config_value_type`) VALUES ('2', '0', 'shareTitle', '小程序分享标题', '【外卖】品质便当，放心精选', '1');
INSERT INTO `milisong_scm`.`global_config` (`id`, `service_line`, `config_key`, `config_describe`, `config_value`, `config_value_type`) VALUES ('3', '0', 'sameCompanyHintCount', '同名公司提示次数', '2', '2');
INSERT INTO `milisong_scm`.`global_config` (`id`, `service_line`, `config_key`, `config_describe`, `config_value`, `config_value_type`) VALUES ('4', '0', 'unPayExpiredTime', '待支付超时时间', '5', '2');
INSERT INTO `milisong_scm`.`global_config` (`id`, `service_line`, `config_key`, `config_describe`, `config_value`, `config_value_type`) VALUES ('6', '0', 'lbsDistance', 'LBS附近公司范围(KM)', '3', '2');
INSERT INTO `milisong_scm`.`global_config` (`id`, `service_line`, `config_key`, `config_describe`, `config_value`, `config_value_type`) VALUES ('7', '0', 'goodsPosterUrl', '餐品海报图地址', 'http://images.jshuii.com/3/goods/201811/c1bf0705e6f04c9aaddaaedea9bdcd21.jpg', '1');
INSERT INTO `milisong_scm`.`global_config` (`id`, `service_line`, `config_key`, `config_describe`, `config_value`, `config_value_type`) VALUES ('8', '0', 'addMiniAppTips', '添加小程序提示', '{\"imgUrl\":\"http://images.jshuii.com/0/d1a468abc2b44e379a89567aef548e1b.jpg\",\"showTimes\":1}', '1');
INSERT INTO `milisong_scm`.`global_config` (`id`, `service_line`, `config_key`, `config_describe`, `config_value`, `config_value_type`) VALUES ('9', '0', 'packageOriginalAmount', ' 打包费原价', '3', '2');
INSERT INTO `milisong_scm`.`global_config` (`id`, `service_line`, `config_key`, `config_describe`, `config_value`, `config_value_type`) VALUES ('10', '0', 'packageDiscountAmount', '打包费优惠价', '2', '2');
INSERT INTO `milisong_scm`.`global_config` (`id`, `service_line`, `config_key`, `config_describe`, `config_value`, `config_value_type`) VALUES ('11', '0', 'deliveryOriginalAmount', '配送费原价', '8', '2');
INSERT INTO `milisong_scm`.`global_config` (`id`, `service_line`, `config_key`, `config_describe`, `config_value`, `config_value_type`) VALUES ('12', '0', 'deliveryDiscountAmount', '配送费优惠价', '0', '2');
INSERT INTO `milisong_scm`.`global_config` (`id`, `service_line`, `config_key`, `config_describe`, `config_value`, `config_value_type`) VALUES ('13', '0', 'orderEndTime', '订单完成不再展示时间', '60', '2');

INSERT INTO `milisong_scm`.`shop_single_config` (`id`, `service_line`, `shop_id`, `shop_code`, `shop_name`, `config_key`, `config_describe`, `config_value`, `config_value_type`) VALUES ('1', '0', '1', '31010001', '1号门店', 'singleMealTime', '单份饭的操作时间(秒)', '18', '2');
INSERT INTO `milisong_scm`.`shop_single_config` (`id`, `service_line`, `shop_id`, `shop_code`, `shop_name`, `config_key`, `config_describe`, `config_value`, `config_value_type`) VALUES ('2', '0', '1', '31010001', '1号门店', 'benchmarkTime', '单个集单的基础时间(秒)', '4', '2');
INSERT INTO `milisong_scm`.`shop_single_config` (`id`, `service_line`, `shop_id`, `shop_code`, `shop_name`, `config_key`, `config_describe`, `config_value`, `config_value_type`) VALUES ('3', '0', '1', '31010001', '1号门店', 'ordersetMaxAmount', '单个集单的商品最大金额(元)', '100', '2');
INSERT INTO `milisong_scm`.`shop_single_config` (`id`, `service_line`, `shop_id`, `shop_code`, `shop_name`, `config_key`, `config_describe`, `config_value`, `config_value_type`) VALUES ('4', '0', '1', '31010001', '1号门店', 'maxProduction', '门店最大生产量', '10', '2');
INSERT INTO `milisong_scm`.`shop_single_config` (`id`, `service_line`, `shop_id`, `shop_code`, `shop_name`, `config_key`, `config_describe`, `config_value`, `config_value_type`) VALUES ('5', '0', '1', '31010001', '1号门店', 'ordersetMaxMember', '集单最大客户数量', '1', '2');

INSERT INTO `milisong_scm`.`shop_intercept_config` (`id`, `shop_id`, `shop_code`, `shop_name`, `order_intercept_time`, `first_orderset_time`, `second_orderset_time`, `delivery_time_begin`, `delivery_time_end`, `is_default`, `max_output`, `status`, `is_deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1', '1', '31010001', '1号门店', '07:00:00', '23:37:00', '10:35:00', '07:00:00', '07:30:00', '0', '100', '1', '0', NULL, '2018-12-06 16:17:16', '2_管理员', '2018-12-07 10:38:37');
INSERT INTO `milisong_scm`.`shop_intercept_config` (`id`, `shop_id`, `shop_code`, `shop_name`, `order_intercept_time`, `first_orderset_time`, `second_orderset_time`, `delivery_time_begin`, `delivery_time_end`, `is_default`, `max_output`, `status`, `is_deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2', '1', '31010001', '1号门店', '07:30:00', '07:30:00', '11:05:00', '07:30:00', '08:00:00', '1', '100', '1', '0', NULL, '2018-12-06 16:17:16', '2_管理员', '2018-12-07 10:38:36');
INSERT INTO `milisong_scm`.`shop_intercept_config` (`id`, `shop_id`, `shop_code`, `shop_name`, `order_intercept_time`, `first_orderset_time`, `second_orderset_time`, `delivery_time_begin`, `delivery_time_end`, `is_default`, `max_output`, `status`, `is_deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3', '1', '31010001', '1号门店', '08:00:00', '08:00:00', '11:25:00', '08:00:00', '08:30:00', '0', '100', '1', '0', NULL, '2018-12-06 16:17:16', NULL, '2018-12-06 19:43:30');
INSERT INTO `milisong_scm`.`shop_intercept_config` (`id`, `shop_id`, `shop_code`, `shop_name`, `order_intercept_time`, `first_orderset_time`, `second_orderset_time`, `delivery_time_begin`, `delivery_time_end`, `is_default`, `max_output`, `status`, `is_deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4', '1', '31010001', '1号门店', '08:30:00', '08:30:00', '11:25:00', '08:30:00', '09:00:00', '0', '100', '1', '0', NULL, '2018-12-06 16:17:16', NULL, '2018-12-06 19:43:30');
INSERT INTO `milisong_scm`.`shop_intercept_config` (`id`, `shop_id`, `shop_code`, `shop_name`, `order_intercept_time`, `first_orderset_time`, `second_orderset_time`, `delivery_time_begin`, `delivery_time_end`, `is_default`, `max_output`, `status`, `is_deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5', '1', '31010001', '1号门店', '09:00:00', '09:00:00', '11:25:00', '09:00:00', '09:30:00', '0', '100', '1', '0', NULL, '2018-12-06 16:17:16', NULL, '2018-12-06 19:43:30');
INSERT INTO `milisong_scm`.`shop_intercept_config` (`id`, `shop_id`, `shop_code`, `shop_name`, `order_intercept_time`, `first_orderset_time`, `second_orderset_time`, `delivery_time_begin`, `delivery_time_end`, `is_default`, `max_output`, `status`, `is_deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('6', '1', '31010001', '1号门店', '09:30:00', '09:30:00', '11:25:00', '09:30:00', '10:00:00', '0', '100', '1', '0', NULL, '2018-12-06 16:17:16', NULL, '2018-12-06 19:43:30');


