package com.milisong.scm.stock;

import com.alibaba.fastjson.JSON;
import com.milisong.scm.stock.domain.ShopOnsaleGoodsStock;
import com.milisong.scm.stock.mapper.ShopOnsaleGoodsStockExtMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/8 11:36
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class StockTest {

    @Autowired
    private ShopOnsaleGoodsStockExtMapper shopOnsaleGoodsStockExtMapper;

    @Test
    public void testCreateOrder(){
        ShopOnsaleGoodsStock shopOnsaleGoodsStock= shopOnsaleGoodsStockExtMapper.selectByPrimaryKey(1468624102621184L);
        System.out.println(JSON.toJSONString(shopOnsaleGoodsStock));
    }

}

