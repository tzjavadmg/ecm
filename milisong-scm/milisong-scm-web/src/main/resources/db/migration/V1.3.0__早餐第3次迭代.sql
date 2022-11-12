ALTER TABLE `milisong_scm`.`order` 
MODIFY COLUMN `delivery_room` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '房间号（取餐点）' AFTER `delivery_floor`;

ALTER TABLE `milisong_scm`.`order_set_detail` 
ADD COLUMN `meal_address` varchar(100) NULL COMMENT '取餐点' AFTER `company_abbreviation`;