package com.mili.oss.service;

import com.alibaba.fastjson.JSON;
import com.mili.oss.mq.message.WaveOrderMessage;
import com.mili.oss.util.MqProducerUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * @description: TODO
 * @program: milisong-workspace
 * @author: codyzeng@163.com
 * @date: 2019/5/18 21:59
 */
@Slf4j
public class WaveOrderDispatcher {

    public static void dispatch(List<WaveOrderMessage> waveOrderMessageList, Map<String, String> printer) {
        try {
//            Map<String, Integer> printerCounter = printer.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getKey, 0));
            Map<String, Integer> printerCounter = new HashMap<>();
            printer.entrySet().stream().forEach(e -> {
                printerCounter.put(e.getKey(), 0);
            });

            //波次单分发
            waveOrderMessageList.forEach(waveOrder -> {
                Entry<String, Integer> counter = printerCounter.entrySet().stream().min((counter1, counter2) -> counter1.getValue().compareTo(counter2.getValue())).orElseGet(null);
                waveOrder.setPrinterId(counter.getKey());
                log.info("---------打印机计数器：{}", JSON.toJSONString(counter));
                counter.setValue(counter.getValue() + waveOrder.getUserPackageCount());
                //TODO 读配置
                waveOrder.setPrinterIp(printer.get(counter.getKey()));
                MqProducerUtil.sendWaveOrder(waveOrder);
                log.info("--------------------发送波次单成功！--{}", JSON.toJSONString(waveOrder));
            });
        } catch (Exception e) {
            log.error("波次单下发异常：{}", e.getMessage());
            e.printStackTrace();
        }
    }
}
