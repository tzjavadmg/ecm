package com.milisong.pms.prom.util;

import com.farmland.core.cache.RedisCache;
import com.farmland.core.distrib.LockProvider;
import com.milisong.pms.prom.dto.UserPointWaterDto;
import com.milisong.pms.prom.enums.UserPointEnum;
import com.sun.javafx.binding.StringFormatter;
import jodd.util.MathUtil;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.redisson.api.RLock;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.milisong.pms.prom.enums.UserPointEnum.POINT_RATE;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/1/4   11:15
 *    desc    : 用户积分工具类
 *    version : v1.0
 * </pre>
 */
@Slf4j
public class UserPointUtil {

    private static final String POWER_LOCK = "user_point_power:%s:%s:%s";
    private static final String POWER_HASH = "%s-%s-%s";
    private static final Integer EXPIRE_TIME = 30;//并发锁控制时间 30秒
    private static final Integer CONCURRENT_TIME = 60*3;//幂等控制时间 3min
    public static final Date POINT_EXPIRE_DATE = new DateTime(2020,12,31,23,59,59).toDate();//2020-12-31 23:59:59

    //积分描述语定义
    public static final String POINT_REFUND_OR_CANCLE = "取消订单退还积分";
    public static final String POINT_CONSUME = "消费抵扣%s元";
    public static final String POINT_COMPLATE = "完成交易%s元";



    public static String pointRemark(UserPointEnum.Action action,Integer point){
        switch (action){
            case PAY_ORDER:
                return String.format(POINT_CONSUME,point/POINT_RATE);
            case FINISH_ORDER:
                return String.format(POINT_COMPLATE,point/POINT_RATE);
            case REFUND_ORDER:
                return String.format(POINT_REFUND_OR_CANCLE,point/POINT_RATE);
            case CANCLE_ORDER:
                return String.format(POINT_REFUND_OR_CANCLE,point/POINT_RATE);
        }
        return null;
    }

    /**
     * 对dto进行hash幂等计算
     * @param dto
     * @return
     */
    public static boolean checkPowerHash(UserPointWaterDto dto){
        Long refId = dto.getRefId();
        Byte refType = dto.getRefType();
        Byte incomeType = dto.getIncomeType();
        String value = StringFormatter.format(POWER_LOCK, refId, refType, incomeType).getValue();
        //防止hash校验时并发，造成重复插入记录
        RLock lock = LockProvider.getLock(value);
        try{
            lock.lock(EXPIRE_TIME,TimeUnit.SECONDS);
            String result = RedisCache.get(value);
            if(result !=null){
                return false;
            }else{
                RedisCache.setEx(value,value,CONCURRENT_TIME,TimeUnit.SECONDS);
                return true;
            }
        }finally {
            lock.unlock();
        }
    }

    public static void releasePowerHash(UserPointWaterDto dto){
        Long refId = dto.getRefId();
        Byte refType = dto.getRefType();
        Byte incomeType = dto.getIncomeType();
        String value = StringFormatter.format(POWER_LOCK, refId, refType, incomeType).getValue();
        RedisCache.delete(value);
    }

    public static String generatePowerHash(UserPointWaterDto dto) {
        Long refId = dto.getRefId();
        Byte refType = dto.getRefType();
        Byte incomeType = dto.getIncomeType();
        String value = StringFormatter.format(POWER_HASH, refId, refType, incomeType).getValue();
        return value;
    }

    public static Integer money2Point(BigDecimal bigDecimal){
        return bigDecimal.setScale(0,BigDecimal.ROUND_UP).intValue();
    }

    public static BigDecimal point2Money(Integer point){
        return new BigDecimal(point*0.1).setScale(2);
    }

}
