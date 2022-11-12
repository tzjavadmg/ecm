package com.milisong.dms.api;

/**
 * @author zhaozhonghui
 * @date 2018-10-26
 */
public interface ShunfengOrderTaskService {
    /**
     * 定时扫描请求失败的顺丰订单日志，并重新请求
     */
    void checkSfOrder();

    /**
     * 定时扫描前一天没完成的订单，将状态设为完成，并通过mq通知c端
     */
    void updateOrderStatus2Finish();

    /**
     * 定时扫描超过7分钟未接单的顺丰订单，发送短信给相关人员
     */
    void checkUnconfirmedSfOrder();

//    void sendOverTimeMail(String shopName, String description, Date pushTime);

    void checkOverTimeSfOrder();

//    /**
//     * 早餐的延迟拍单
//     *
//     * @param delayMinute
//     */
//    void runDelayTask(Integer delayMinute);
}
