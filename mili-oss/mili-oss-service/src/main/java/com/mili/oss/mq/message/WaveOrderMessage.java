package com.mili.oss.mq.message;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 波次单
 * @program: mili-ofc-wos
 * @author: codyzeng@163.com
 * @date: 2019/5/15 15:34
 */
@Getter
@Setter
public class WaveOrderMessage {

    /**
     * 打包数量
     */
    private transient int userPackageCount = 0;

    /**
     * 门店编码
     */

    private transient String shopId;

    /**
     * 业务线
     */
    private Byte businessLine;

    /**
     * 本波次发送的打印机ID
     */
    private String printerId;
    /**
     * 本波次发送的打印机IP
     */
    private String printerIp;

    /**
     * 本波次对应的公司
     */
    private String company;

    /**
     * 公司详细地址
     */
    private String companyAddr;

    /**
     * 打印类型 0:打印全部 1:配送联 2:顾客联 4:自动集单
     */
    private Integer printType = 4;
    /**
     * 本波次处理的集单
     */
    private List<DeliveryPackageMessage> deliveryPackageList;

    public void addDeliveryPackage(DeliveryPackageMessage deliveryPackageMessage) {
        if (deliveryPackageList == null) {
            deliveryPackageList = new ArrayList<>();
        }
        deliveryPackageList.add(deliveryPackageMessage);
    }

    public void addUserPackageCount(int packCount) {
        this.userPackageCount += packCount;
    }
}
