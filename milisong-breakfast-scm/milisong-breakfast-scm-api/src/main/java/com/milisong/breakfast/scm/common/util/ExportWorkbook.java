package com.milisong.breakfast.scm.common.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 导出文件辅助类
 *
 * @author ZhaoZhongHui
 * @Date 2017/12/28 17:16
 */
@Data
@Slf4j
public class ExportWorkbook implements Serializable {
    private static final long serialVersionUID = 1175304028791502465L;
    /**
     * excel文件类
     */
    private SXSSFWorkbook workbook;
    /**
     * 数据开始的索引
     */
    private int rowIndex;

    private int DEFAULT_COLOUMN_WIDTH = 17;
    public String DEFAULT_DATE_PATTERN = "yyyy年MM月dd日";//默认日期格式

    private String[] sheetNames;

    /**
     * 构造器生成excel文件类
     */
    public ExportWorkbook() {
        workbook = new SXSSFWorkbook(1000);//缓存
        workbook.setCompressTempFiles(true);
        //表头样式
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 20);
        titleFont.setBoldweight((short) 700);
        titleStyle.setFont(titleFont);
    }

    /**
     * 创建头部
     *
     * @param headList   列表头Map的集合，一个sheet对应一个列头Map
     * @param sheetNames sheet名字的数组
     * @param colWidth   自定义列宽，默认17
     */
    public void createHead(List<Map<String, String>> headList, String[] sheetNames, int colWidth) {

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(headerFont);

        for (int j = 0; j < headList.size(); j++) {
            Map<String, String> headMap = headList.get(j);
            // 生成一个(带标题)表格
            Sheet sheet = workbook.createSheet(sheetNames[j]);
            //设置列宽
            int minBytes = colWidth < DEFAULT_COLOUMN_WIDTH ? DEFAULT_COLOUMN_WIDTH : colWidth;//至少字节数
            int[] arrColWidth = new int[headMap.size()];
            // 产生表格标题行,以及设置列宽
            String[] headers = new String[headMap.size()];
            int ii = 0;
            for (Iterator<String> iter = headMap.keySet().iterator(); iter
                    .hasNext(); ) {
                String fieldName = iter.next();

                headers[ii] = headMap.get(fieldName);
                int bytes = fieldName.getBytes().length;
                arrColWidth[ii] = bytes < minBytes ? minBytes : bytes;
                sheet.setColumnWidth(ii, arrColWidth[ii] * 256);
                ii++;
            }

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
                headerRow.getCell(i).setCellStyle(headerStyle);
            }
        }
    }

    /**
     * 创建头部
     *
     * @param headMap   列表头Map
     * @param sheetName sheet名字
     * @param colWidth  自定义列宽，默认17
     */
    public void createHead(Map<String, String> headMap, String sheetName, int colWidth) {

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(headerFont);

        // 生成一个(带标题)表格
        Sheet sheet = workbook.createSheet(sheetName);
        //设置列宽
        int minBytes = colWidth < DEFAULT_COLOUMN_WIDTH ? DEFAULT_COLOUMN_WIDTH : colWidth;//至少字节数
        int[] arrColWidth = new int[headMap.size()];
        // 产生表格标题行,以及设置列宽
        String[] headers = new String[headMap.size()];
        int ii = 0;
        for (Iterator<String> iter = headMap.keySet().iterator(); iter
                .hasNext(); ) {
            String fieldName = iter.next();

            headers[ii] = headMap.get(fieldName);
            int bytes = fieldName.getBytes().length;
            arrColWidth[ii] = bytes < minBytes ? minBytes : bytes;
            sheet.setColumnWidth(ii, arrColWidth[ii] * 256);
            ii++;
        }

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
            headerRow.getCell(i).setCellStyle(headerStyle);
        }
        rowIndex = 1;
    }

    /**
     * 录入数据
     *
     * @param sheetNames
     * @param headList
     * @param dataList
     * @param datePattern
     * @param rowIndex
     */
    public void inputData(String[] sheetNames, List<Map<String, String>> headList, List<JSONArray> dataList, String datePattern, int rowIndex) {
        if (datePattern == null) datePattern = DEFAULT_DATE_PATTERN;
        for (int j = 0; j < headList.size(); j++) {
            Map<String, String> headMap = headList.get(j);
            // 生成一个表格
            Sheet sheet = workbook.getSheet(sheetNames[j]);
            // 产生表格标题行,以及设置列宽
            String[] properties = new String[headMap.size()];
            int ii = 0;
            for (Iterator<String> iter = headMap.keySet().iterator(); iter
                    .hasNext(); ) {
                String fieldName = iter.next();

                properties[ii] = fieldName;
                ii++;
            }
            // 遍历集合数据，产生数据行
            JSONArray jsonArray = dataList.get(j);
            for (Object obj : jsonArray) {
                JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
                Row dataRow = sheet.createRow(rowIndex);
                for (int i = 0; i < properties.length; i++) {
                    Cell newCell = dataRow.createCell(i);

                    Object o = jo.get(properties[i]);
                    String cellValue = "";
                    if (o == null) {
                        newCell.setCellValue(cellValue);
                    } else if (o instanceof Date) {
                        cellValue = new SimpleDateFormat(datePattern).format(o);
                        newCell.setCellValue(cellValue);
                    } else if (o instanceof Float || o instanceof Double || o instanceof BigDecimal) {
                        newCell.setCellValue(new BigDecimal(o.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    } else if (o instanceof Integer || o instanceof Long) {
                        newCell.setCellValue(NumberUtils.toDouble(o + ""));
                    } else {
                        cellValue = o.toString();
                        newCell.setCellValue(cellValue);
                    }
                }
                rowIndex++;
            }
        }
    }


    @SuppressWarnings("rawtypes")
	public void inputData(String sheetName, Map<String, String> headMap, List list, String datePattern) {
//        try {
        if (datePattern == null) datePattern = DEFAULT_DATE_PATTERN;
        // 生成一个表格
        Sheet sheet = workbook.getSheet(sheetName);
        // 产生表格标题行,以及设置列宽
        String[] properties = new String[headMap.size()];
        int ii = 0;
        for (Iterator<String> iter = headMap.keySet().iterator(); iter
                .hasNext(); ) {
            String fieldName = iter.next();

            properties[ii] = fieldName;
            ii++;
        }
        // 遍历集合数据，产生数据行
        for (Object obj : list) {
            JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
            Row dataRow = sheet.createRow(rowIndex);
            for (int i = 0; i < properties.length; i++) {
                Cell newCell = dataRow.createCell(i);

                Object o = jo.get(properties[i]);
                String cellValue = "";
                if (o == null) {
                    newCell.setCellValue(cellValue);
                } else if (o instanceof Date) {
                    cellValue = new SimpleDateFormat(datePattern).format(o);
                    newCell.setCellValue(cellValue);
                } else if (o instanceof Float || o instanceof Double || o instanceof BigDecimal) {
                    newCell.setCellValue(new BigDecimal(o.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                } else if (o instanceof Integer || o instanceof Long) {
                    newCell.setCellValue(NumberUtils.toDouble(o + ""));
                } else {
                    cellValue = o.toString();
                    newCell.setCellValue(cellValue);
                }
            }
            rowIndex++;
        }
    }

    public void closeWorkbook(OutputStream os) {
        try {
            workbook.write(os);
            workbook.close();
            workbook.dispose();
        } catch (IOException e) {
            log.error("IO异常:" + e.getMessage(), e);
        }
    }
}