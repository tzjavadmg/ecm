ALTER TABLE `order_set_detail_bf` 
MODIFY COLUMN `goods_sum` int(4) NULL DEFAULT NULL COMMENT '子集单中商品总数量(累计套餐内商品)' AFTER `company_abbreviation`,
ADD COLUMN `sku_sum` int(4) NULL COMMENT '集单中sku总数量(套餐算一个)' AFTER `goods_sum`;

ALTER TABLE `order_set_detail_goods_bf` 
ADD COLUMN `is_combo` tinyint(1) NULL DEFAULT 0 COMMENT '是否组合商品 1是 0否' AFTER `actual_pay_amount`,
ADD COLUMN `combo_goods_code` varchar(20) NULL COMMENT '组合商品code' AFTER `is_combo`,
ADD COLUMN `combo_goods_name` varchar(50) NULL COMMENT '组合商品名称' AFTER `combo_goods_code`,
ADD COLUMN `combo_goods_count` int(5) NULL COMMENT '组合商品数量' AFTER `combo_goods_name`;