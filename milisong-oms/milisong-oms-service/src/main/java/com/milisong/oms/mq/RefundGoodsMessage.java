package com.milisong.oms.mq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/30 11:42
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefundGoodsMessage {

    private String goodsCode;

    private String goodsCount;
}
