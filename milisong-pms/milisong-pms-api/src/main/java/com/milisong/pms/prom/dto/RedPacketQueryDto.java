package com.milisong.pms.prom.dto;

import com.farmland.core.api.PageParam;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * 红包列表查询参数
 *
 * @author sailor wang
 * @date 2019/2/18 3:42 PM
 * @description
 */
@Data
public class RedPacketQueryDto extends PageParam implements Serializable {
    private static final long serialVersionUID = -1401763062315362621L;

    /**
     * 红包id
     */
    private Long id;

    /**
     * 红包名称
     */
    private String name;

    /**
     * 发布状态：0 下线、1 上线
     */
    private Byte status;

    /**
     * 业务线：0 早餐、1  午餐、2 下午茶、3 晚餐、4 夜宵
     */
    private Byte businessLine;

    public String getName() {
        return StringUtils.isEmpty(this.name)?this.name:this.name.trim();
    }
}
