package com.milisong.scm.orderset.util;

import com.milisong.scm.orderset.annotation.Export;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExportUtil {

    public static Map<String, String> getHeadTitles(Class cls) {
        Field[] fields = cls.getDeclaredFields();
        List<ExportColumn> columns = new ArrayList<>();
        for (Field field : fields) {
            Export export = field.getAnnotation(Export.class);
            if (export != null) {
                ExportColumn column = new ExportColumn();
                column.setFieldName(field.getName());
                column.setTitle(export.value());
                column.setRank(export.rank());
                columns.add(column);
            }
        }
        columns.sort((e1, e2) -> e1.getRank() - e2.getRank());

        Map<String, String> headMap = new LinkedHashMap<>();
        for (ExportColumn column : columns) {
            headMap.put(column.getFieldName(), column.getTitle());
        }
        return headMap;
    }

    public static void downExcel(String filePath, String fileName, HttpServletResponse response) {

        File file = new File(filePath);
        ServletOutputStream out = null;
        FileInputStream inputStream = null;
        try {
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            fileName = fileName + ".xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "iso-8859-1"));
            inputStream = new FileInputStream(file);
            out = response.getOutputStream();
            int b = 0;
            byte[] buffer = new byte[512];
            while ((b = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, b);
            }
            inputStream.close();
            out.close();
            out.flush();
        } catch (IOException e) {
            log.error("export download " + e.getMessage(), e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (out != null) {
                    out.close();
                    out.flush();
                }
            } catch (IOException e) {
            }
        }
    }

    public static void main(String[] args) {
        Map<String, String> map = getHeadTitles(Class.class);
        System.out.println(map);
    }

    @Data
    private static class ExportColumn {
        private String title;
        private String fieldName;
        private int rank;
    }
}
