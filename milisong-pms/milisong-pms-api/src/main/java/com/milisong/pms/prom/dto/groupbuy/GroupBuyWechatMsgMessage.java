package com.milisong.pms.prom.dto.groupbuy;

import com.milisong.pms.prom.enums.GrouyBuyEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 拼团消息通知
 *
 * @author sailor wang
 * @date 2019/5/22 2:11 PM
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupBuyWechatMsgMessage implements Serializable {

    private static final long serialVersionUID = -5391163019732044065L;

    //用户openid列表
    private List<JoinUser> joinUserList;

    //场景类型
    private GrouyBuyEnum.SEND_MSG_SCENE scene;

    //拼团实例id
    private Long userGroupBuyId;

    //送达时间GroupBuyServiceImpl
    private Date deliveryDate;

    //商品code
    private String goodsCode;

    //支付金额
    private BigDecimal price;

    //公司id
    private Long companyId;

    //剩余人数
    private Integer leftNum;

    //参团人数
    private Integer joinedNum;

    //剩余时间
    private Integer seconds;

    //失败原因
    private String failReason;

    //温馨提示
    private String warnTips;

    private Byte businessLine = 0;

}