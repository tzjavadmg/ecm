package com.mili.oss.service;

import com.farmland.core.util.BeanMapper;
import com.mili.oss.OssApplication;
import com.mili.oss.domain.OrderSet;
import com.mili.oss.domain.OrderSetExample;
import com.mili.oss.domain.OrderSetGoods;
import com.mili.oss.domain.OrderSetGoodsExample;
import com.mili.oss.dto.NotifyOrderSetQueryResult;
import com.mili.oss.dto.OrderSetDetailDto;
import com.mili.oss.dto.OrderSetDetailGoodsDto;
import com.mili.oss.dto.config.InterceptConfigDto;
import com.mili.oss.mapper.OrderSetGoodsMapper;
import com.mili.oss.mapper.OrderSetMapper;
import com.mili.oss.service.OrderSetServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @program: mili-oss
 * @description: 打印测试
 * @author: liuyy
 * @date: 2019/5/22 18:38
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes={OssApplication.class})
@Slf4j
public class OrderSetServiceTest {
    @Autowired
    OrderSetServiceImpl orderSetService;

    @Autowired
    OrderServiceImpl orderService;

    @Autowired
    private OrderSetGoodsMapper orderSetGoodsMapper;

    @Autowired
    private OrderSetMapper orderSetMapper;

    @Test
    public void test1(){
        String setNo = "F20190502000001";

        NotifyOrderSetQueryResult result = new NotifyOrderSetQueryResult();
        OrderSetExample example = new OrderSetExample();
        example.createCriteria().andDetailSetNoEqualTo(setNo);
        List<OrderSet> orderSetList = orderSetMapper.selectByExample(example);

        result.setOrderSet(BeanMapper.map(orderSetList.get(0), OrderSetDetailDto.class));
        OrderSetGoodsExample exampleGoods = new OrderSetGoodsExample();
        exampleGoods.createCriteria().andDetailSetNoEqualTo(setNo);
        List<OrderSetGoods> listGoods = orderSetGoodsMapper.selectByExample(exampleGoods);

        result.setGoodsList(BeanMapper.mapList(listGoods, OrderSetDetailGoodsDto.class));

    }

    /**
     * 早餐打印测试
     */
    @Test
    public void BUG() throws ParseException {
        Long shopId = 1L;//门店
        Byte orderType = 0;//订单类型 0 早餐 1午餐
        String beginString = "2019-05-27 08:00:00";
        String endString = "2019-05-27 08:30:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        InterceptConfigDto map = new InterceptConfigDto();
        map.setShopId(shopId);
/*        map.getFirstOrdersetTime();
        map.setSecondOrdersetTime();*/
        map.setDeliveryTimeBegin(sdf.parse(beginString));
        map.setDeliveryTimeEnd(sdf.parse(endString));
        orderService.executeShopSet(shopId,map,orderType);
    }

}
