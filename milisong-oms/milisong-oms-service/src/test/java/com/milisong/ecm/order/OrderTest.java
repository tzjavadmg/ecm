package com.milisong.ecm.order;

import com.farmland.core.db.IdGenerator;
import com.milisong.oms.domain.Order;
import com.milisong.oms.mapper.OrderMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/8 11:36
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class OrderTest {

    @Resource
    private OrderMapper orderMapper;

    @Test
    public void testCreateOrder(){
        Order order=new Order();
        order.setId(IdGenerator.nextId());

        orderMapper.insert(order);
    }

}

