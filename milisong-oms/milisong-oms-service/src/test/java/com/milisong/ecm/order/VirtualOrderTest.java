package com.milisong.ecm.order;

import com.farmland.core.db.IdGenerator;
import com.milisong.oms.domain.VirtualOrderDelivery;
import com.milisong.oms.mapper.VirtualOrderDeliveryMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/2 10:30
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class VirtualOrderTest {

    @Resource
    private VirtualOrderDeliveryMapper virtualOrderDeliveryMapper;

    @Test
    public void testBatchInsertVirtualOrderDelivery() {
        Set<VirtualOrderDelivery> sets = new HashSet<>();

        VirtualOrderDelivery virtualOrderDelivery = new VirtualOrderDelivery();
        virtualOrderDelivery.setId(IdGenerator.nextId());
        virtualOrderDelivery.setDeliveryDate(new Date());
        sets.add(virtualOrderDelivery);
        virtualOrderDelivery = new VirtualOrderDelivery();
        virtualOrderDelivery.setId(IdGenerator.nextId());
        virtualOrderDelivery.setDeliveryDate(new Date());

        sets.add(virtualOrderDelivery);
        virtualOrderDeliveryMapper.batchSave(sets);
    }
    @Test
    public void testUpdateBySelection(){
        VirtualOrderDelivery virtualOrderDelivery = new VirtualOrderDelivery();
        virtualOrderDelivery.setId(1555128172085248L);
        virtualOrderDeliveryMapper.updateByPrimaryKeySelective(virtualOrderDelivery);
    }
}
