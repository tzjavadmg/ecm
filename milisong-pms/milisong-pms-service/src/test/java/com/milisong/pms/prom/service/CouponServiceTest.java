package com.milisong.pms.prom.service;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.milisong.oms.constant.OrderType;
import com.milisong.pms.prom.dto.CouponDto;
import com.milisong.pms.prom.dto.CouponQueryDto;
import com.milisong.pms.prom.dto.Pagenation;
import com.milisong.pms.prom.enums.CouponEnum;
import com.milisong.ucs.enums.BusinessLineEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/1/11   18:39
 *    desc    : 折扣业务测试
 *    version : v1.0
 * </pre>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class CouponServiceTest {

    @Autowired
    private CouponServiceImpl couponService;


    @Test
    public void saveOrUpdate(){
        log.info("----开始测试----");
        CouponDto couponDto = new CouponDto();
//        couponDto.setId(2127932195930112L);
        couponDto.setBusinessLine(OrderType.BREAKFAST.getValue());
        couponDto.setName("测试商品套餐券2");
        couponDto.setType(CouponEnum.TYPE.GOODS.getCode());
        couponDto.setLabel(CouponEnum.LABEL.PACKAGE.getCode());
        couponDto.setLimitDays(null);
        couponDto.setGoodsCode("00002");
        couponDto.setGoodsName("测试套餐2");
        couponDto.setDiscount(new BigDecimal("2"));
        couponDto.setIsShare(CouponEnum.IS_SHARE.YES.getCode());
        couponDto.setValidDays(10);
        couponDto.setRule("测试规则2222");
        couponDto.setStatus(CouponEnum.STATUS.ONLINE.getCode());
        couponDto.setRemark("测试描述");
        couponDto.setLimitDays(3);
        log.info("---param----{}",JSON.toJSONString(couponDto));
        ResponseResult<String> responseResult = couponService.saveOrUpdate(couponDto);
        log.info("---result----{}",JSON.toJSONString(responseResult));

    }

    @Test
    public void query(){
        ResponseResult<CouponDto> result = couponService.query(2160849163911168L);
        log.info("---result--->{}", JSON.toJSONString(result));
    }
    @Test
    public void queryName(){
        CouponQueryDto couponQueryDto = new CouponQueryDto();
        couponQueryDto.setType(CouponEnum.TYPE.GOODS.getCode());
        couponQueryDto.setBusinessLine(BusinessLineEnum.BREAKFAST.getCode());
        couponQueryDto.setStatus(CouponEnum.STATUS.ONLINE.getCode());
        couponQueryDto.setName("套餐券");
        log.info("---param--->{}", JSON.toJSONString(couponQueryDto));

        ResponseResult<List<CouponDto>> result = couponService.queryByName(couponQueryDto);
        log.info("---result--->{}", JSON.toJSONString(result));
        queryByCondition();
    }


    @Test
    public void queryByCondition(){
        CouponQueryDto couponQueryDto = new CouponQueryDto();
        couponQueryDto.setType(CouponEnum.TYPE.GOODS.getCode());
        couponQueryDto.setPageNo(1);
        couponQueryDto.setName("测试");
        couponQueryDto.setBusinessLine(BusinessLineEnum.BREAKFAST.getCode());
        log.info("----param--->{}",JSON.toJSONString(couponQueryDto));
        ResponseResult<Pagenation<CouponDto>> responseResult = couponService.queryByCondition(couponQueryDto);
        log.info("---result--->{}", JSON.toJSONString(responseResult));
    }

    @Test
    public void offline(){
        CouponDto couponDto = new CouponDto();
        couponDto.setId(2127932195930112L);
        ResponseResult<Boolean> result = couponService.offline(couponDto);
        log.info("---result--->{}", JSON.toJSONString(result));
    }
}
