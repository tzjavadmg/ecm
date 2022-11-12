package com.mili.oss.service;

import com.mili.oss.OssApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @program: mili-oss
 * @description: TODO
 * @author: liuyy
 * @date: 2019/5/28 20:55
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes={OssApplication.class})
@Slf4j
public class ReprintSeiviceTest {

    @Autowired
    ReprintServiceImpl reprintService;
    /**
     * 早餐打印测试
     */
    @Test
    public void test2(){
        String setNo = "F20190529000004";
        Integer printType = 1;
        String coupletNo ="default";
        reprintService.reprintSetMqBySetNo(setNo,coupletNo,printType);
    }

    /**
     * 午餐打印测试
     */
    @Test
    public void test3(){
        String setNo = "B20190521000020";
        Integer printType = 1;
        String coupletNo ="";
        reprintService.reprintSetMqBySetNo(setNo,coupletNo,printType);
    }



}
