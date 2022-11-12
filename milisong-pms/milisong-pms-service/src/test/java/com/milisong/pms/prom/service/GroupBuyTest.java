package com.milisong.pms.prom.service;

import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.distrib.LockProvider;
import com.milisong.pms.prom.dto.MarketingQrcodeDto;
import com.milisong.pms.prom.dto.groupbuy.GroupBuyEntryResponse;
import com.milisong.pms.prom.dto.groupbuy.GroupBuyRequest;
import com.milisong.pms.prom.dto.groupbuy.GroupBuyResponse;
import com.milisong.pms.prom.enums.QrcodeType;
import com.milisong.ucs.enums.BusinessLineEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

/**
 * @author sailor wang
 * @date 2019/5/10 9:33 PM
 * @description
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class GroupBuyTest {

    @Autowired
    private GroupBuyServiceImpl groupBuyService;

    /**
     * 拼团入口
     */
    @Test
    public void groupBuyEntry(){
        GroupBuyRequest groupBuyRequest = GroupBuyRequest.builder().build();
        ResponseResult<GroupBuyEntryResponse> responseResult = groupBuyService.groupBuyEntry(groupBuyRequest);
        log.info("data -> {}",responseResult.getData());
        Assert.assertNotNull(responseResult.getData());//PASS
    }

    /**
     * 查询自己的拼团详情
     */
    @Test
    public void querySelfJoinedGroupBuy(){
        GroupBuyRequest groupBuyRequest = GroupBuyRequest.builder().activityGroupBuyId(1L).userId(2721863869210624L).build();
        ResponseResult<GroupBuyResponse> result = groupBuyService.querySelfJoinedGroupBuy(groupBuyRequest);
        log.info("querySelfJoinedGroupBuy -> {}",result.getData());
        Assert.assertNotNull(result.getData());//PASS
    }

    /**
     * 创建拼团
     */
    @Test
    public void queryOtherJoinedGroupBuy(){
        GroupBuyRequest request = GroupBuyRequest.builder().userGroupBuyId(2857532174041088L).userId(2721863869210624L).build();
        ResponseResult<GroupBuyResponse> response = groupBuyService.queryOtherJoinedGroupBuy(request);
        log.info("queryOtherJoinedGroupBuy -> {}",response.getData());
        Assert.assertNotNull(response.getData());//PASS
    }

//    @Test
    public void latchLock(){
        String key = "latch_lock";
        RCountDownLatch countDownLatch = lockProvider().getCountDownLatch(key);
        countDownLatch.trySetCount(1);
    }

//    @Test
    public void latchWait() throws InterruptedException {
        String key = "latch_lock";
        RCountDownLatch countDownLatch = lockProvider().getCountDownLatch(key);
        long count = countDownLatch.getCount();
        System.out.println("count -> "+count);
        if (count > 0){
            System.out.println("等待");
            countDownLatch.await();
        }else {
            System.out.println("跳过等待");
        }
    }

//    @Test
    public void latchUnlock() throws InterruptedException {
        String key = "latch_lock";
        RCountDownLatch countDownLatch = lockProvider().getCountDownLatch(key);
        long count = countDownLatch.getCount();
        System.out.println("count -> "+count);
        if (count > 0){
            System.out.println("释放锁");
            countDownLatch.countDown();
        }else {
            System.out.println("没有释放锁");
        }
    }

    public LockProvider lockProvider(){
        LockProvider.setRedissonClient(Redisson.create());
        return new LockProvider();
    }



}
