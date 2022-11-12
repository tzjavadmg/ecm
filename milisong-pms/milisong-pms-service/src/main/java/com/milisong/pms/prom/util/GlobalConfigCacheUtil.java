package com.milisong.pms.prom.util;

import com.farmland.core.cache.RedisCache;
import com.milisong.pms.prom.dto.invite.InviteConfigDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/3/25   17:58
 *    desc    : 缓存工具类
 *    version : v1.0
 * </pre>
 */
@Slf4j
public class GlobalConfigCacheUtil {

    private static final String GLOBAL_CONFIG_KEY = "config_bf:global";
    //邀新首页入口图
    public static final String inviteHomeEnterPic = "inviteHomeEnterPic";
    //邀新我的 入口图
    public static final String inviteMineEnterPic = "inviteMineEnterPic";
    //我的邀请详情页面展示图
    public static final String myInvitePic = "myInvitePic";
    //我的邀新（立即邀请下面的小字文案）
    public static final String myInviteDesc = "myInviteDesc";
    //邀新小程序转发图
    public static final String inviteTransmitPic = "inviteTransmitPic";
    //邀新小程序转发文案
    public static final String inviteTransmitText = "inviteTransmitText";
    //新人点击转发连接进入 展示图
    public static final String newbieDisplayPic = "newbieDisplayPic";
    //邀新送券数量限制
    public static final String inviteSendCouponValidity = "inviteSendCouponValidity";
    //邀新开始时间
    public static final String inviteStartTime = "inviteStartTime";
    //邀新结束时间
    public static final String inviteEndTime = "inviteEndTime";
    //邀新最大获得券数量
    public static final String inviteMaxCouponCount = "inviteMaxCouponCount";
    //邀新 赠送商品json数据
    public static final String inviteGoodsInfo = "inviteGoodsInfo";
    //邀新规则
    public static final String inviteRule = "inviteRule";
    //要新结果数据展示(您已成功邀请x人，成功获取x份早餐)
    public static final String inviteDataDesc = "inviteDataDesc";
    //老拉新邀请活动名称
    public static final String inviteActivityName = "inviteActivityName";
    //老拉新邀请活动id
    public static final String inviteActivityId = "inviteActivityId";
    //新用户受邀，领券成功，券使用描述
    public static final String inviteNewbieCouponUseDesc = "inviteNewbieCouponUseDesc";

    //弹层
    private static final String inviteToastPic = "inviteToastPic";
    //首次分享标题
    private static final String inviteFirstShareTitle = "inviteFirstShareTitle";
    //首次分享文案
    private static final String inviteFirstShareDesc = "inviteFirstShareDesc";
    //二次分享标题
    private static final String inviteSecondShareTitle = "inviteSecondShareTitle";
    //二次分享文案
    private static final String inviteSecondShareDesc = "inviteSecondShareDesc";
    //提醒用户文案
    private static final String inviteRemarkWord = "inviteRemarkWord";

    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String getInviteHomeEnterPic() {
        return getCacheValue(inviteHomeEnterPic);
    }

    public static String getInviteMineEnterPic() {
        return getCacheValue(inviteMineEnterPic);
    }

    public static String getMyInvitePic() {
        return getCacheValue(myInvitePic);
    }

    public static String getMyInviteDesc() {
        return getCacheValue(myInviteDesc);
    }

    public static String getInviteTransmitPic() {
        return getCacheValue(inviteTransmitPic);
    }

    public static String getInviteDataDesc() {
        return getCacheValue(inviteDataDesc);
    }

    public static String getInviteTransmitText() {
        return getCacheValue(inviteTransmitText);
    }

    public static String getNewbieDisplayPic() {
        return getCacheValue(newbieDisplayPic);
    }

    public static Integer getInviteSendCouponValidity() {
        String cacheValue = getCacheValue(inviteSendCouponValidity);
        return parseInteger(cacheValue);
    }

    public static Date getInviteStartTime() {
        String cacheValue = getCacheValue(inviteStartTime);
        return parseDate(cacheValue,TIME_FORMAT);
    }

    public static Date getInviteEndTime() {
        String cacheValue = getCacheValue(inviteEndTime);
        return parseDate(cacheValue,TIME_FORMAT);
    }

    public static Integer getInviteMaxCouponCount() {
        String cacheValue = getCacheValue(inviteMaxCouponCount);
        return parseInteger(cacheValue);
    }

    public static String getInviteGoodsInfo() {
        return getCacheValue(inviteGoodsInfo);
    }

    public static String getInviteActivityName() {
        return getCacheValue(inviteActivityName);
    }

    public static Long getInviteActivityId() {
        return parseLong(getCacheValue(inviteActivityId));
    }

    public static String getInviteRule() {
        return getCacheValue(inviteRule);
    }

    public static String getInviteNewbieCouponUseDesc() {
        return getCacheValue(inviteNewbieCouponUseDesc);
    }

    public static InviteConfigDto getConfig(){
        Map<Object, Object> all = RedisCache.hGetAll(GLOBAL_CONFIG_KEY);
        InviteConfigDto inviteConfigDto = new InviteConfigDto();
        all.entrySet().stream().forEach(o->{
            String key = (String)o.getKey();
            Object val = o.getValue();
            if(val == null){
                return;
            }
            String v = val.toString();
            if(key.equals(inviteHomeEnterPic)){
                inviteConfigDto.setInviteHomeEnterPic(v);
            }else if(key.equals(inviteMineEnterPic)){
                inviteConfigDto.setInviteMineEnterPic(v);
            }else if(key.equals(myInvitePic)){
                inviteConfigDto.setMyInvitePic(v);
            }else if(key.equals(myInviteDesc)){
                inviteConfigDto.setMyInviteDesc(v);
            }else if(key.equals(inviteTransmitPic)){
                inviteConfigDto.setInviteTransmitPic(v);
            }else if(key.equals(inviteTransmitText)){
                inviteConfigDto.setInviteTransmitText(v);
            }else if(key.equals(newbieDisplayPic)){
                inviteConfigDto.setNewbieDisplayPic(v);
            }else if(key.equals(inviteSendCouponValidity)){
                inviteConfigDto.setInviteSendCouponValidity(parseInteger(v));
            }else if(key.equals(inviteStartTime)){
                inviteConfigDto.setInviteStartTime(parseDate(v,TIME_FORMAT));
            }else if(key.equals(inviteEndTime)){
                inviteConfigDto.setInviteEndTime(parseDate(v,TIME_FORMAT));
            }else if(key.equals(inviteMaxCouponCount)){
                inviteConfigDto.setInviteMaxCouponCount(parseInteger(v));
            }else if(key.equals(inviteGoodsInfo)){
                inviteConfigDto.setInviteGoodsInfo(v);
            }else if(key.equals(inviteRule)){
                inviteConfigDto.setInviteRule(v);
            }else if(key.equals(inviteDataDesc)){
                inviteConfigDto.setInviteDataDesc(v);
            }else if(key.equals(inviteActivityName)){
                inviteConfigDto.setInviteActivityName(v);
            }else if(key.equals(inviteActivityId)){
                inviteConfigDto.setInviteActivityId(parseLong(v));
            }else if(key.equals(inviteNewbieCouponUseDesc)){
                inviteConfigDto.setInviteNewbieCouponUseDesc(v);
            }else if (key.equals(inviteToastPic)){
                inviteConfigDto.setInviteToastPic(v);
            }else if (key.equals(inviteFirstShareTitle)){
                inviteConfigDto.setInviteFirstShareTitle(v);
            }else if (key.equals(inviteFirstShareDesc)){
                inviteConfigDto.setInviteFirstShareDesc(v);
            }else if (key.equals(inviteSecondShareTitle)){
                inviteConfigDto.setInviteSecondShareTitle(v);
            }else if (key.equals(inviteSecondShareDesc)){
                inviteConfigDto.setInviteSecondShareDesc(v);
            }else if (key.equals(inviteRemarkWord)){
                inviteConfigDto.setInviteRemarkWord(v);
            }
        });
        return inviteConfigDto;
    }

    private static String getCacheValue(String key){
        Object result = RedisCache.hGet(GLOBAL_CONFIG_KEY, key);
        if(result == null){
            log.error("邀新工具，没有拿到配置信息->{}",key);
            return null;
        }else{
            return result.toString();
        }
    }

    private static Date parseDate(String value,String format){
        if(value == null){
            return null;
        }else{
            Date date = null;
            try {
                date = DateUtils.parseDate(value, format);
            } catch (ParseException e) {
                log.error("解析时间错误"+value,e);
            }
            return date;
        }
    }

    private static Integer parseInteger(String value){
        if(value == null){
            return null;
        }else{
            Integer result =null;
            try {
                result =  Integer.parseInt(value);
            } catch (Exception e) {
                log.error("整型变量解析错误->"+value,e);
            }
            return result;
        }
    }

    private static Long parseLong(String value){
        if(value == null){
            return null;
        }else{
            Long result =null;
            try {
                result =  Long.parseLong(value);
            } catch (Exception e) {
                log.error("长整型变量解析错误->"+value,e);
            }
            return result;
        }
    }
}
