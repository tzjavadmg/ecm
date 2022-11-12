CREATE TABLE IF NOT EXISTS  `milisong_scm`.`goods_combination_ref`  (
  `id` bigint(20) NOT NULL,
  `combination_code` varchar(20) NULL COMMENT '组合商品code',
  `single_code` varchar(20) NULL COMMENT '单品商品code',
  `goods_count` int(11) DEFAULT '1' COMMENT '单品数量',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  INDEX `idx_combination_code`(`combination_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '组合商品明细表';

CREATE TABLE IF NOT EXISTS `order_detail_combo` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单ID',
  `order_no` varchar(50) DEFAULT NULL COMMENT '订单编号',
  `goods_code` varchar(20) DEFAULT NULL COMMENT '商品code',
  `goods_name` varchar(50) DEFAULT NULL COMMENT '商品名称',
  `goods_count` int(11) DEFAULT NULL COMMENT '商品数量',
  `combo_goods_code` varchar(20) NULL COMMENT '组合商品code',
  `combo_goods_name` varchar(50) NULL COMMENT '组合商品名称',
  `combo_goods_count` int(5) NULL COMMENT '组合商品数量',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单明细表-组合明细-早餐';

ALTER TABLE `goods`
ADD COLUMN `is_combo`  tinyint(1) NULL DEFAULT 0 COMMENT '是否为组合套餐（1是、0否）' AFTER `weight`;

ALTER TABLE `goods_category`
ADD COLUMN `type`  tinyint(2) NULL DEFAULT 1 COMMENT '分类类型(1单品，2套餐)' AFTER `status`;

ALTER TABLE `milisong_scm`.`order` 
MODIFY COLUMN `goods_sum` int(3) NULL DEFAULT NULL COMMENT '订单中商品总数量(累计套餐内商品数)' AFTER `delivery_room`,
ADD COLUMN `sku_sum` int(3) NULL COMMENT 'SKU商品总数量(套餐记一个)' AFTER `goods_sum`;

ALTER TABLE `milisong_scm`.`order_detail` 
ADD COLUMN `is_combo` tinyint(1) NULL DEFAULT 0 COMMENT '是否组合商品 1是 0否' AFTER `red_packet_amount`;

ALTER TABLE `milisong_scm`.`order_set_detail` 
MODIFY COLUMN `goods_sum` int(4) NULL DEFAULT NULL COMMENT '子集单中商品总数量(累计套餐内商品)' AFTER `company_abbreviation`,
ADD COLUMN `sku_sum` int(4) NULL COMMENT '集单中sku总数量(套餐算一个)' AFTER `goods_sum`;

ALTER TABLE `milisong_scm`.`order_set_detail_goods` 
ADD COLUMN `is_combo` tinyint(1) NULL DEFAULT 0 COMMENT '是否组合商品 1是 0否' AFTER `actual_pay_amount`,
ADD COLUMN `combo_goods_code` varchar(20) NULL COMMENT '组合商品code' AFTER `is_combo`,
ADD COLUMN `combo_goods_name` varchar(50) NULL COMMENT '组合商品名称' AFTER `combo_goods_code`,
ADD COLUMN `combo_goods_count` int(5) NULL COMMENT '组合商品数量' AFTER `combo_goods_name`;