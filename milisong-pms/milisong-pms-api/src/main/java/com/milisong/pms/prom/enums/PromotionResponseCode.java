package com.milisong.pms.prom.enums;

/**
 * @author sailor wang
 * @date 2018/8/31 下午3:34
 * @description
 */
public enum PromotionResponseCode {
    SUCCESS("80000", "操作成功", "请求操作成功"),
    REQUEST_PARAM_EMPTY("80001", "请求参数为空", "请求参数为空"),
    NOT_EXIST_ACTIVITY_RED_PACKET("80002", "不存在红包活动", "不存在红包活动"),
    QUERY_USER_RED_PACKET_EXCEPTION("80003", "查询用户红包异常", "查询用户红包异常"),
    ACTIVITY_TYPE_IS_EMPTY("80004", "活动类型为空", "活动类型为空"),
    ACTIVITY_STATUS_INVALID("80005", "活动状态不合法", "活动状态不合法"),
    EXIST_CONFLICT_ACTIVITY_RED_PACKET("80006", "存在冲突红包活动", "存在冲突红包活动"),
    QUERY_ACTIVITY_RED_PACKET_EXCEPTION("80007", "查询红包活动异常", "查询红包活动异常"),
    NEW_RED_PACKET_REDIS_EMPTY("80008", "新人红包缓存数据为空", "新人红包缓存数据为空"),
    MULTI_SHARE_RED_PACKET_REDIS_EMPTY("80009", "多人分享红包缓存数据为空", "多人分享红包缓存数据为空"),
    RED_PACKET_EXPIRED("80010", "红包活动已失效", "红包活动已失效"),
    RECEIVE_RED_PACKET_FAIL("80011", "领取红包失败", "领取红包失败"),
    INVALID_BUDGET_RED_PACKET_AMOUT("80012", "预算红包金额不合法", "预算红包金额不合法"),
    INVALID_RECIVED_RED_PACKET_AMOUT("80013", "领取红包金额不合法", "领取红包金额不合法"),
    CREATE_MULTI_SHARE_RED_PACKET_EXCEPTION("80014", "创建多人分享红包异常", "创建多人分享红包异常"),
    ACTIVITY_USER_RED_PACKET_NOT_EXIST("80015", "用户发起红包活动不存在", "用户发起红包活动不存在"),
    USER_NOT_AUTHORIZATION("80016", "用户未授权", "用户未授权"),
    RECEIVE_TIMES_LIMIT("80017", "红包领取次数超限", "红包领取次数超限"),
    USER_RED_PACKET_EXPIRED("80018", "用户红包已失效", "用户红包已失效"),
    USER_RED_PACKET_NOT_EXIST("80019", "用户红包不存在", "用户红包不存在"),
    UPDATE_USER_RED_PACKET_EXCEPTION("80020", "修改用户红包异常", "修改用户红包异常"),
    USER_RED_PACKET_EXISTED("80021", "该红包已存在", "该红包已存在"),
    ACTIVE_RED_PACKET_NOT_EXIST("80022", "促活红包不存在", "促活红包不存在"),
    REDIS_DATA_NOT_EXIST("80023", "REDIS缓存数据不存在", "REDIS缓存数据不存在"),
    SWITCH_OFF("80024", "开关未开启", "开关未开启"),
    FULL_REDUCE_CONFIG_EXCEPTION("80025", "满减配置异常", "满减配置异常"),
    CALC_PAY_AMOUNT_INVALID("80026", "计算支付金额不合法", "计算支付金额不合法"),
    CALC_PAY_AMOUNT_EXCEPTION("80027", "计算支付金额异常", "计算支付金额异常"),
    ACTIVE_RED_PACKET_EXPIRED("80029", "促活红包已过期", "促活红包已过期"),
    ACTIVE_RED_PACKET_EXCEPTION("80030", "发送促活红包异常", "发送促活红包异常"),
    FULL_REDUCE_ACTIVITY_EXPIRED("80031", "满减活动已过期", "满减活动已过期"),
    REPEAT_EXECUTIVE("80032", "重复执行", "重复执行"),
    RECEIVE_BREAKFAST_COUPON_FAIL("80033", "领取早餐券失败", "领取早餐券失败"),
    BREAKFAST_COUPON_NOT_EXIST("80034", "早餐券数据不存在", "早餐券数据不存在"),
    BREAKFAST_COUPON_EXPIRE("80035", "早餐券活动已过期", "早餐券活动已过期"),
    SENDING_BREAKFAST_COUPON("80036", "发券中，请稍后重试", "发券中，请稍后重试"),
    UPDATE_USER_COUPON_EXCEPTION("80037", "修改用户券异常", "修改用户券异常"),
    SENNG_BREAKFAST_COUPON_EXCEPTION("80038", "批量推送早餐券失败", "批量推送早餐券失败"),
    SENDING_BREAKFAST_DONE("80039", "早餐券发送完成", "早餐券发送完成"),
    GREAT_THAN_ACTUAL_AMOUNT("80040","积分数量大于实际商品金额","积分数量大于实际商品金额"),
    SEND_RED_PACKET_CONFLICT("80041","全量和指定人员发送互斥","全量和指定人员发送互斥"),
    BREAKFAST_COUPON_OFFLINE("80042", "早餐券已下架", "早餐券已下架"),
    NOT_FOUND_USERS("80043", "未查询到用户数据", "未查询到用户数据"),
    DATA_NOT_EXISTS("80044", "数据不存在", "数据不存在"),
    MULTI_BREAKFAST_COUPON_CONFIG_NOT_EXISTS("80045", "多人分享早餐券配置数据不存在", "多人分享早餐券配置数据不存在"),
    ACTIVITY_USER_COUPON_NOT_EXIST("80046", "用户发起券活动不存在", "用户发起券活动不存在"),
    CREATE_MULTI_BREAKFAST_SHARE_COUPON_EXCEPTION("80047", "创建早餐多人分享折扣券异常", "创建早餐多人分享折扣券异常"),
    USER_COUPON_ACTIVITY_EXISTED("80048", "用户发起券活动已存在", "用户发起券活动已存在"),
    HAS_RECEIVED_BREAKFAST_COUPON("80049", "已领取过早餐券", "已领取过早餐券"),
    BUSINESS_LINE_IS_EMPTY("80050", "缺少业务线", "缺少业务线"),
    RED_PACKET_NOT_EXIST("80051", "红包数据不存在", "红包数据不存在"),
    RED_PACKET_OFFLINE("80052", "红包已下线", "红包已下线"),


    POINT_SAVE_FAIL("40020", "保存积分流水失败"),
    POINT_POWER_CHECK_FAIL("40021", "幂等校验，已经记录过此次数据"),
    POINT_UPDATE_USER_FAIL("40022", "更新用户的总积分失败"),
    POINT_INVALID_POINT("40023", "无效的积分数据"),
    POINT_USER_ID_NULL("40024", "缺少用户ID"),
    POINT_INCOME_TYPE_NULL("40025", "缺少收支类型"),
    POINT_REF_ID_NULL("40026", "缺少业务ID"),
    POINT_REF_TYPE_ID_NULL("40027", "缺少业务类型"),
    POINT_ACTION_NULL("40028", "缺少动作类型"),
    POINT_BUSINESS_LINE_NULL("40029", "缺少业务线"),

    POINT_RULE_NULL("40030", "积分规则为空"),
    POINT_QUERY_ILLEGAL("40031", "查询用户信息非法"),
    POINT_QUERY_NO_BZL("40032", "查询缺少业务线"),
    POINT_POWER_CHECK_DB_FAIL("40033", "库已经记录过此次数据"),

    //券相关
    COUPON_SAVE_FAILED("40034","保存券失败"),
    COUPON_NO_BZL("40035","缺少业务线"),
    COUPON_NO_TYPE("40036","缺少券类型"),
    COUPON_NO_GOODS("40037","缺少商品"),
    COUPON_NO_DISCOUNT("40038","缺少折扣或优惠金额"),
    COUPON_NO_RULE("40039","缺少券使用规则"),
    COUPON_NO_REMARK("40040","缺少备注"),
    COUPON_NOT_FOUND("40041","没有查询到"),
    COUPON_LACK_PARAM("40042","查询条件缺少必要信息"),
    COUPON_LIMIT_DAYS_ERROR("40043","门槛天数(满x天) 非法参数"),
    COUPON_VALID_DAYS_ERROR("40044","有效天数 非法参数"),
    COUPON_DISCOUNT_ERROR("40045","折扣值 非法参数"),
    COUPON_LIMIT_DAYS_MUST("40046","门槛天数(满x天) 必填"),
    COUPON_GOODS_CODE_HAS_CHINESE("40047","券的商品编码含有中文？请联系管理员"),
    COUPON_GOODS_INVALID_PACKAGE("40048","商品券缺少套餐标识参数"),

    //扫码领券活动
    QRCODE_HAS_BIND_ACTIVITY("40050","二维码已绑定活动，请重新生成二维码"),
    SCAN_QRCODE_ACTIVITY_HAS_NOT_START("40051","扫码领券活动还未开始"),
    SCAN_QRCODE_ACTIVITY_HAS_OVER("40052","扫码领券活动已结束"),
    SCAN_QRCODE_ACTIVITY_RECEIVE_SUCCESS("40053","早餐券领取成功"),
    QRCODE_HAS_NOT_BIND_ACTIVITY("40054","二维码未绑定活动，二维码未绑定活动"),
    SCAN_QRCODE_ACTIVITY_HAS_RECEIVED("40055","你已领过这张券~"),
    RECEIVEING_BREAKFAST_COUPON("40056", "领券中，请稍后重试", "领券中，请稍后重试"),
    ONLY_NEW_USER_RECEIVE("40057", "只有新用户才可以领此券哦~", "只有新用户才可以领此券哦~"),
    RECEIVE_SCANQRCODE_COUPON_FAIL("40058", "领券失败", "领券失败"),

    //老拉新邀请活动
    INVITE_HAS_OVER("40060","活动已结束"),
    INVITE_HAS_NOT_START("40061","活动未开始"),
    INVITE_ACTIVITY_NOT_EXIST("40062","该邀请活动实例不存在"),

    //拼团
    GROUP_BUY_NOT_FOUND_GOODS("40070","拼团商品不存在"),
    ACTIVITY_GROUP_BUY_NOT_EXIST("40071","拼团活动不存在"),
    HAS_JOINED_ACTIVITY_GROUP_BUY("40072","已参过该拼团活动"),
    GROUP_BUY_OUT_OF_STOCK("40073","商品库存不足"),
    BEYOND_GROUP_BUY_JOINED_NUM("40074","拼团人数超限"),
    CREATE_GROUP_BUY_FAILED("40075","创建拼团失败"),
    ACTIVITY_GROUP_BUY_OVER("40076","拼团活动已结束"),
    UPDATE_STATUS_FAIL_AFTER_PAY_SUCCESS("40077","支付成功，拼团状态修改失败！"),
    REPEAT_JOINED_USER_GROUP_BUY("40078","重复参同一个拼团活动"),
    USER_GROUP_BUY_EXPIRED("40079","拼团活动已过期"),
    JOIN_USER_GROUP_BUY_FAILED("40080","参团失败"),
    USER_GROUP_BUY_RECORD_NOT_EXIST("40081","参团记录不存在"),
    ;


    private final String code;

    private final String message;

    private final String detailMessage;

    PromotionResponseCode(String code, String message, String detailMessage) {
        this.code = code;
        this.message = message;
        this.detailMessage = detailMessage;
    }

    PromotionResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
        this.detailMessage = "";
    }

    public String code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public String detailMessage() {
        return this.detailMessage;
    }
}
