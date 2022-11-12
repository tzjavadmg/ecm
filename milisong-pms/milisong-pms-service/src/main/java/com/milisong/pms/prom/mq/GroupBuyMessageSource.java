package com.milisong.pms.prom.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author sailor wang
 * @date 2019/5/22 2:08 PM
 * @description
 */
public interface GroupBuyMessageSource {

    /**
     * 取消拼团消息
     */
    final String CANCEL_GROUPBUY_OUTPUT = "cancel_groupbuy_output";

    final String WECHAT_MSG_GROUPBUY_OUTPUT = "wechatmsg_groupbuy_output";

    final String UPDATE_USER_GROUPBUY_OUTPUT = "update_user_groupbuy_output";

    /**
     * 取消拼团消息
     *
     * @return
     */
    @Output(CANCEL_GROUPBUY_OUTPUT)
    MessageChannel cancelOutput();


    /**
     * 拼团微信消息
     *
     * @return
     */
    @Output(WECHAT_MSG_GROUPBUY_OUTPUT)
    MessageChannel wechatMsgGroupBuyOutput();


    @Output(UPDATE_USER_GROUPBUY_OUTPUT)
    MessageChannel updateUserGroupbuyOutput();
}
