package com.milisong.pms.prom.dto;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * 市场推广二维码
 */
@Getter
@Setter
public class MarketingQrcodeDto extends BaseDto {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 二维码名称
     */
    private String name;

    /**
     * 二维码类型: 1 个人 2 宣传单 3 营销活动
     * @see com.milisong.pms.prom.enums.QrcodeType
     */
    private Byte type;

    /**
     * 二维码code
     */
    private String qrcode;

    /**
     * 公司ID
     */
    private Long companyId;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 推广人姓名
     */
    private String userName;

    /**
     * 跳转页面路径
     */
    private String pagePath;

    /**
     * 是否删除 1是 0否
     */
    private Boolean isDeleted;

    private Date createTime;

    private String createTimeText;

    public String getCreateTimeText() {
        if (createTime != null){
            return new DateTime(createTime).toString("yyyy-MM-dd HH:mm:ss");
        }
        return createTimeText;
    }

    public void setCreateTimeText(String createTimeText) {
        this.createTimeText = createTimeText;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "MarketingQrcodeDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", qrcode='" + qrcode + '\'' +
                ", companyId=" + companyId +
                ", companyName='" + companyName + '\'' +
                ", userName='" + userName + '\'' +
                ", isDeleted=" + isDeleted +
                ", createTime=" + createTime +
                ", createTimeText='" + createTimeText + '\'' +
                '}';
    }
}