ALTER TABLE `milisong_pos`.`order_set_detail_bf` 
ADD COLUMN `meal_address` varchar(100) NULL COMMENT '取餐点' AFTER `company_abbreviation`;