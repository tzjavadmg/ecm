//package com.mili.oss.algorithm;
//
//import com.alibaba.fastjson.JSON;
//import com.google.common.collect.Maps;
//import com.mili.oss.algorithm.model.GoodsModel;
//import com.mili.oss.algorithm.model.GoodsPackModel;
//import com.mili.oss.algorithm.model.UserOrderModel;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.junit.Test;
//
//import java.io.FileInputStream;
//import java.io.InputStream;
//import java.math.BigDecimal;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * @ Description：从excel读取数据测试算法
// * @ Author     ：zengyuekang.
// * @ Date       ：2019/2/25 16:21
// */
//@Slf4j
//public class ExcelDataAlgorithmTest {
//
//    private final BigDecimal maxGoodsAmount = new BigDecimal(20);
//    private final Integer maxUserCount = 3;
//
//    @Test
//    public void testDefaultAlgorithm() throws Exception {
//        InputStream is = new FileInputStream("d:/tmp/alg2.xlsx");
//        Workbook wb = new XSSFWorkbook(is);
//        Sheet sheet = wb.getSheetAt(0);
//        int rowNum = sheet.getLastRowNum();
//        Row row = sheet.getRow(0);
//
//        Map<Long, UserOrderModel> userOrderModelMap = Maps.newHashMap();
//        // 正文内容应该从第二行开始,第一行为表头的标题
//        for (int i = 1; i <= rowNum; i++) {
//            row = sheet.getRow(i);
//            int j = 0;
//
//            String userIdString = (String) getCellFormatValue(row.getCell(1));
//            String orderIdString = (String) getCellFormatValue(row.getCell(2));
//            String goodsCodeString = (String) getCellFormatValue(row.getCell(3));
//            String amountString = (String) getCellFormatValue(row.getCell(4));
//            String countString = (String) getCellFormatValue(row.getCell(5));
//
//
//            Long userId = Long.valueOf(userIdString.split("\\.")[0]);
//            Long orderId = Long.valueOf(orderIdString.split("\\.")[0]);
//            Long goodsId = Long.valueOf(goodsCodeString.split("\\.")[0]);
//            BigDecimal amount = new BigDecimal(amountString);
//            Integer count = Integer.valueOf(countString.split("\\.")[0]);
//
//            log.info("---------------{}---{}----{}---{}", userId, orderId, goodsId, amount);
//
//            GoodsModel goodsModel = new GoodsModel();
//            goodsModel.setOrderId(orderId);
//            goodsModel.setGoodsId(goodsId);
//            goodsModel.setTotalAmount(amount);
//            goodsModel.setGoodsCount(count);
//
//            UserOrderModel userOrderModel = userOrderModelMap.get(userId);
//            if (userOrderModel == null) {
//                userOrderModel = new UserOrderModel();
//                userOrderModelMap.put(userId, userOrderModel);
//            }
//            userOrderModel.setUserId(userId);
//            userOrderModel.addGoodsModel(goodsModel);
//        }
//
//        List<UserOrderModel> overList = userOrderModelMap.values().stream().filter(userOrderModel -> userOrderModel.getTotalAmount().compareTo(maxGoodsAmount) > 0).collect(Collectors.toList());
//        List<UserOrderModel> unOverList = userOrderModelMap.values().stream().filter(userOrderModel -> userOrderModel.getTotalAmount().compareTo(maxGoodsAmount) <= 0).collect(Collectors.toList());
//
//        log.info("-------超标数量-----{}-----未超标数量-------{}", overList.size(), unOverList.size());
//
//        DefalutSortingAlgorithm algorithm = new DefalutSortingAlgorithm();
//        SortingAlgorithmContext ctx = new SortingAlgorithmContext();
//        ctx.setOrderSetMaxAmount(maxGoodsAmount);
//        ctx.setOrderSetMaxUserCount(maxUserCount);
//        ctx.setOverProofUserOrderModelList(overList);
//        ctx.setUnOverProofUserOrderModelList(unOverList);
//
//        List<GoodsPackModel> goodsPackModelList = algorithm.calculate(ctx);
//
//        goodsPackModelList.forEach(goodsPackModel -> {
//            System.out.println("---------------------------------------------------");
//            System.out.println("---集单金额--------：" + goodsPackModel.getTotalGoodsAmount());
//            System.out.println("---集单关联用户--------：" + goodsPackModel.getUserIdSet());
//            System.out.println("---集单关联订单--------：" + goodsPackModel.getOrderIdSet());
//            System.out.println("---集单关联商品--------：" + JSON.toJSONString(goodsPackModel.getGoodsModelSet()));
//        });
//    }
//
//    /**
//     * 根据Cell类型设置数据
//     *
//     * @param cell
//     * @return
//     * @author zengwendong
//     */
//    private Object getCellFormatValue(Cell cell) {
//        Object cellvalue = "";
//        if (cell != null) {
//            // 判断当前Cell的Type
//            switch (cell.getCellType()) {
//                case Cell.CELL_TYPE_NUMERIC:// 如果当前Cell的Type为NUMERIC
//                case Cell.CELL_TYPE_FORMULA: {
//                    // 判断当前的cell是否为Date
//                    if (DateUtil.isCellDateFormatted(cell)) {
//                        // 如果是Date类型则，转化为Data格式
//                        // data格式是带时分秒的：2013-7-10 0:00:00
//                        // cellvalue = cell.getDateCellValue().toLocaleString();
//                        // data格式是不带带时分秒的：2013-7-10
//                        Date date = cell.getDateCellValue();
//                        cellvalue = date;
//                    } else {// 如果是纯数字
//
//                        // 取得当前Cell的数值
//                        cellvalue = String.valueOf(cell.getNumericCellValue());
//                    }
//                    break;
//                }
//                case Cell.CELL_TYPE_STRING:// 如果当前Cell的Type为STRING
//                    // 取得当前的Cell字符串
//                    cellvalue = cell.getRichStringCellValue().getString();
//                    break;
//                default:// 默认的Cell值
//                    cellvalue = "";
//            }
//        } else {
//            cellvalue = "";
//        }
//        return cellvalue;
//    }
//
//}
