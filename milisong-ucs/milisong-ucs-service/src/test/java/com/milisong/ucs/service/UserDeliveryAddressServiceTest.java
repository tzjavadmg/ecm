package com.milisong.ucs.service;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.milisong.ucs.dto.Pagenation;
import com.milisong.ucs.dto.SendBreakCouponRequest;
import com.milisong.ucs.dto.UserDeliveryAddressDto;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author sailor wang
 * @date 2019/1/14 5:33 PM
 * @description
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class UserDeliveryAddressServiceTest {

    @Autowired
    UserDeliveryAddressServiceImpl userDeliveryAddressService;

    @Test
    public void queryUserByDevlieryAddr(){
        String json = "{\"businessLine\":0,\"couponids\":[2137655815634944],\"id\":2144386114134016,\"mobiles\":[\"15802112942\"],\"operatorId\":2,\"operatorName\":\"管理员\",\"pageNo\":1,\"pageSize\":500,\"sendAllUser\":false,\"sendTime\":1547456829541,\"smsMsg\":\"测试发送早餐券\",\"startRow\":0}";
        SendBreakCouponRequest sendBreakCouponRequest = JSONObject.parseObject(json,SendBreakCouponRequest.class);
        sendBreakCouponRequest.setExcludeUserIds(Lists.newArrayList(1927921580507136L));
        ResponseResult<Pagenation<UserDeliveryAddressDto>> result = userDeliveryAddressService.queryUserByDevlieryAddr(sendBreakCouponRequest);
        System.out.println(JSONObject.toJSONString(result));
    }
}