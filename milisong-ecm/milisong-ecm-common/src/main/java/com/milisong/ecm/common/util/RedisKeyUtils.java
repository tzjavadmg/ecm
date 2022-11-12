package com.milisong.ecm.common.util;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.farmland.core.cache.RedisCache;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

public class RedisKeyUtils {


    /****************************早餐start******************************/
    //可售档期
    public static final String SHOP_SCHEDULE_DATE_PREFIX = "shop_schedule_date";

    //类目基础信息
    public static final String CATALOG_BASIC_PREFIX = "catalog_basic";

    //公司
    public static final String COMPANY_PREFIX = "company";

    //可售商品
    public static final String DAILY_SHOP_GOODS_PREFIX = "daily_shop_goods";


    /****************************早餐end******************************/

    //商品基本信息
    public static final String GOODS_BASIC_PREFIX = "goods_basic";


    /****************************午餐start******************************/


    public static String GOODS_INFO_PREFIX = "goods_info";

    public static String SHOP_ONSALE_DATE_PREFIX = "shop_onsale_date";

    public static final String BULIDING_PREFIX = "building";

    public static final String SALABLE_PREFIX = "salable_shop_goods";

    /****************************午餐end******************************/


    public static String DELIVERY_COUNTER_PREFIX = "delivery_counter";

    public static String DELIVERY_ADDRESS_PREFIX = "delivery_address";


    //获取类目信息key
    public static String getCatglogBasicKey() {
        return CATALOG_BASIC_PREFIX;
    }

    //获取公司信息key
    public static String getCompanyKey(Long companyId) {
        return COMPANY_PREFIX + ":" + companyId;
    }

    //获取公司信息集合key
    public static String getCompanyListKey() {
        return COMPANY_PREFIX + "_list";
    }

    public static String getDailyShopGoodsKey(String shopCode, Date date) {
        String dateString = DateFormatUtils.format(date, "yyMMdd");
        return DAILY_SHOP_GOODS_PREFIX + ":" + shopCode + ":" + dateString;

    }

    static public String getLatestDeliveryAddressKey(Long userId) {
        return "latest_delivery_address:" + userId;
    }


    static public String getCompanyKey(Long buildId, String floor, String companyName) {
        return "company:" + buildId + ":" + floor + ":" + companyName;
    }

    static public String getUserCountKey(Long userId) {
        return "same_company_hint_count:" + userId;
    }

    static public String getCompanySearchKey(Long buildId, String floor) {
        return "company:" + buildId + ":" + floor + ":*";
    }

    /****************************早餐end******************************/

    //获取商品基本信息
    public static String getGoodsBasicKey(String goodsCode) {
        return GOODS_BASIC_PREFIX + ":" + goodsCode;
    }

    //获取可售档期key
    public static String getShopScheduleDateKey(String shopCode) {
        return SHOP_SCHEDULE_DATE_PREFIX + ":" + shopCode;
    }


    /**************************午餐start***********************************/

    public static String getBuildingLbsKey() {
        return BULIDING_PREFIX + "_lbs";
    }

    public static String getBuildingKey(Long buildingId) {
        return BULIDING_PREFIX + ":" + buildingId;
    }

    public static String getBuildingSearchKey() {
        return BULIDING_PREFIX + ":*";
    }

    public static String getBuildingListKey() {
        return BULIDING_PREFIX + "_list";
    }

    public static String getOnsaleGoodsStockKey(String shopCode, Date saleDate, String goodsCode) {
        String saleDateString = DateFormatUtils.format(saleDate, "yyMMdd");
        return "shop_stock:" + shopCode + "_" + saleDateString + ":" + goodsCode;
    }

    public static String getOnsaleGoodsStockLockKey(String shopCode, Date saleDate, String goodsCode) {
        String saleDateString = DateFormatUtils.format(saleDate, "yyMMdd");
        return "L_" + shopCode + saleDateString + ":" + goodsCode;
    }

    /**
     * 门店配置
     * 包括截单时间，配送起止时间等。使用hash结构存储
     *
     * @param shopCode
     * @return
     */
    public static String getShopConfigKey(String shopCode) {
        return "config:" + shopCode;
    }

    public static String getGlobalConfigKey() {
        return "config:global";
    }

    public static String getShopGoodsDayKey(String shopCode, String categoryCode, Date date) {
        String saleDateString = DateFormatUtils.format(date, "yyMMdd");
        return "daily_shop_goods:" + shopCode + "_" + saleDateString + ":" + categoryCode;
    }

    public static String getGoodsInfoKey(String goodsCode) {
        return "goods_info:" + goodsCode;
    }

    public static String getShopOnsaleDate(String shopCode) {
        return SHOP_ONSALE_DATE_PREFIX + ":" + shopCode;
    }

    public static String getSalableShopGoodsKey(String shopCode, Date date) {
        String dateString = DateFormatUtils.format(date, "yyMMdd");
        return SALABLE_PREFIX + ":" + shopCode + ":" + dateString;
    }

    /**************************午餐end***********************************/


    public static String getDeliveryCounterKey(String shopCode, Date deliveryTimezoneFrom) {
        String timezone = DateFormatUtils.format(deliveryTimezoneFrom, "yyMMddHHmm");
        return DELIVERY_COUNTER_PREFIX + ":" + shopCode + ":" + timezone;
    }

    public static String getDeliveryAddressKeyLockKey(Long userId, Long deliveryOfficeBuildingId) {
        return DELIVERY_ADDRESS_PREFIX + ":" + userId + "_" + deliveryOfficeBuildingId;
    }

    /*************************************默认展示早餐门店code配置************************/
    private static final String DEFAULT_SHOPCODE_KEY = "defaultShopCode";
    private static final String DEFAULT_SHOPCODE = "31010001";//缓存失效时取的默认门店值

    public static String getDefaultShopCode() {
        Object result = RedisCache.hGet("config:global", DEFAULT_SHOPCODE_KEY);
        if (result != null) {
            return result.toString();
        } else {
            return DEFAULT_SHOPCODE;
        }
    }

    /*************************************默认展示午餐门店code配置************************/
    private static final String DEFAULT_LUNCH_SHOPCODE_KEY = "defaultShopCode";
    private static final String DEFAULT_LUNCH_SHOPCODE = "31010001";//缓存失效时取的默认门店值

    public static String getDefaultLunchShopCode() {
        Object result = RedisCache.hGet(getGlobalConfigKey(), DEFAULT_LUNCH_SHOPCODE_KEY);
        if (result != null) {
            return result.toString();
        } else {
            return DEFAULT_LUNCH_SHOPCODE;
        }
    }

    //早餐
    private static final String APPLY_COMPANY_ENTER_PIC_KEY = "applyOpenPic";

    public static String getApplyCompanyEnterPic() {
        return getGlobalConfigKeyV(APPLY_COMPANY_ENTER_PIC_KEY);
    }

    public static String getGlobalConfigKeyV(String key) {
        Object result = RedisCache.hGet(getGlobalConfigKey(), key);
        if (result != null) {
            return result.toString();
        } else {
            return null;
        }
    }


    /*************************************weChat formId************************/
    /**
     * 【发团 & 参团成功】：拼团进度通知模版id redis key
     */
    private static final String JOIN_GROUP_BUY_FORM_ID_KEY = "join_group_buy_form_id:";

    /**
     * 得到用户拼图通知formId的键值
     *
     * @param openId
     * @return
     */
    public static String getJoinGroupBuyFormIdKey(String openId) {
        return JOIN_GROUP_BUY_FORM_ID_KEY + openId;
    }

    /**
     * 存入列表尾部
     *
     * @param openId
     * @param value
     * @return
     */
    public static void setJoinGroupBuyFormIdValue(String openId, String value) {
        //过期天数
        int expireDay = 7;

        //过期的时间，防止误差，提前5秒
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, expireDay);
        calendar.add(Calendar.SECOND, -5);

        String key = getJoinGroupBuyFormIdKey(openId);
        //加上7天后的时间戳，取出时判断是否已失效
        RedisCache.lRightPush(key, value + "_" + calendar.getTime().getTime());
        RedisCache.expire(key, expireDay, TimeUnit.DAYS);
    }

    /**
     * 从列表头部取出，先进先出
     *
     * @param openId
     * @return
     */
    public static String getJoinGroupBuyFormIdValue(String openId) {
        String formId = getJoinGroupBuyFormIdValueUtil(openId);
        if (StringUtils.isNotBlank(formId)) {
            //返回时去掉后面的时间戳
            formId = formId.substring(0, formId.lastIndexOf("_"));
        }
        return formId;
    }

    private static String getJoinGroupBuyFormIdValueUtil(String openId) {
        //判断是否已失效
        String formId = RedisCache.lLeftPop(getJoinGroupBuyFormIdKey(openId));
        if (StringUtils.isEmpty(formId)) {
            return null;
        }

        Long now = Calendar.getInstance().getTime().getTime();
        Long formIdTime = Long.valueOf(formId.substring(formId.lastIndexOf("_") + 1));
        //递归判断，直到取到不过期的formId
        if (now > formIdTime) {
            formId = getJoinGroupBuyFormIdValueUtil(openId);
        }
        return formId;
    }
}
