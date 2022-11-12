package com.mili.oss.algorithm;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;

/**
 * @description: TODO
 * @program: milisong-workspace
 * @author: codyzeng@163.com
 * @date: 2019/6/5 21:50
 */
public class DateTest {

    @Test
    public void testDefaultAlgorithm() throws Exception {
        System.out.println(DateFormatUtils.format(1800000L,"HH:mm"));
    }
}
