package com.mili.oss.algorithm.model;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Sets;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ Description：商品分拣（集单信息）
 * @ Author     ：zengyuekang.
 * @ Date       ：2019/2/18 17:36
 */
@Getter
@Setter
@Slf4j
public class GoodsPackModel {

    private Integer id;
    /**
     * 总商品实际支付金额
     */
    private BigDecimal totalGoodsAmount;

    private Set<Long> userIdSet;

    private Set<Long> orderIdSet;

    private Set<GoodsModel> goodsModelSet;

    public BigDecimal addTotalGoodsAmount(BigDecimal goodsAmount) {
        if (totalGoodsAmount == null) {
            totalGoodsAmount = goodsAmount;
        } else {
            totalGoodsAmount = totalGoodsAmount.add(goodsAmount);
        }
        return totalGoodsAmount;
    }

    public Integer addUserId(Long userId) {
        if (CollectionUtils.isEmpty(userIdSet)) {
            userIdSet = new HashSet<>();
        }
        userIdSet.add(userId);
        return userIdSet.size();
    }

    public Integer addUserIdSet(Set<Long> userIds) {
        if (CollectionUtils.isEmpty(userIdSet)) {
            userIdSet = new HashSet<>();
        }
        userIdSet.addAll(userIds);
        return orderIdSet.size();
    }

    public Integer addOrderId(Long orderId) {
        if (CollectionUtils.isEmpty(orderIdSet)) {
            orderIdSet = new HashSet<>();
        }
        orderIdSet.add(orderId);
        return orderIdSet.size();
    }

    public Integer addOrderIdSet(Set<Long> orderIds) {
        if (CollectionUtils.isEmpty(orderIdSet)) {
            orderIdSet = new HashSet<>();
        }
        orderIdSet.addAll(orderIds);
        return orderIdSet.size();
    }

    public Integer addGoodsModel(GoodsModel goodsModel) {
        if (CollectionUtils.isEmpty(goodsModelSet)) {
            goodsModelSet = Sets.newHashSet();
        }
        goodsModelSet.add(goodsModel);
        return goodsModelSet.size();
    }

    public Integer addGoodsModelSet(Set<GoodsModel> goodsModelSet) {
        if (CollectionUtils.isEmpty(this.goodsModelSet)) {
            this.goodsModelSet = Sets.newHashSet();
        }

        //FIXME 优化
        Map<Integer, GoodsModel> modelMap = this.goodsModelSet.stream().collect(Collectors.toMap(GoodsModel::hashCode, v -> v));
        log.info("-----------before addGoodsModelSet：{},merge set:{},nodeMap:{}", JSON.toJSONString(this.goodsModelSet), JSON.toJSONString(goodsModelSet), JSON.toJSONString(modelMap));
        goodsModelSet.forEach(gm -> {
            GoodsModel existsGoodsModel = modelMap.get(gm.hashCode());
            log.info("-------原始gm:{}", JSON.toJSONString(gm));
            if (existsGoodsModel != null) {
                gm.setGoodsCount(gm.getGoodsCount() + existsGoodsModel.getGoodsCount());
                gm.setTotalAmount(gm.getTotalAmount().add(existsGoodsModel.getTotalAmount()));
                log.info("------合并后gm:{}", JSON.toJSONString(gm));
            }
            modelMap.put(gm.hashCode(), gm);
        });

        this.goodsModelSet.clear();
        this.goodsModelSet.addAll(modelMap.values());
        log.info("-----------after addGoodsModelSet：{},nodeMap:{}", JSON.toJSONString(this.goodsModelSet), JSON.toJSONString(modelMap));
        return this.goodsModelSet.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        return id.equals(((GoodsPackModel) obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
