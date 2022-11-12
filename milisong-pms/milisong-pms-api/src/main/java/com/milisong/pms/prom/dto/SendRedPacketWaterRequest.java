package com.milisong.pms.prom.dto;

import com.farmland.core.api.PageParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sailor wang
 * @date 2019/1/13 4:09 PM
 * @description
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendRedPacketWaterRequest extends PageParam {
    private Long sendRedPacketRecordId;
    private String mobileNo;
    private Byte businessLine;

}