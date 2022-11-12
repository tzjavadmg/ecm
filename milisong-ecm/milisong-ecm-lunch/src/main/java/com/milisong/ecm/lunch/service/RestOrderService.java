package com.milisong.ecm.lunch.service;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.util.BeanMapper;
import com.google.common.collect.Lists;
import com.milisong.ecm.common.util.ExcelUtil;
import com.milisong.wechat.miniapp.api.MiniAppService;
import com.milisong.wechat.miniapp.dto.MiniBillRequest;
import com.milisong.wechat.miniapp.dto.MiniPayBillBaseResult;
import com.milisong.wechat.miniapp.dto.MiniPayBillResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sailor wang
 * @date 2018/9/2 下午2:40
 * @description
 */
@Slf4j
@Service
public class RestOrderService {
    @Resource
    MiniAppService miniAppService;

    public ResponseEntity<byte[]> downloadBill(String env,String billDate) {
        List<MiniPayBillBaseResult> wxPayBillBaseResultLst = fetchBillData(env,billDate);

        if (CollectionUtils.isNotEmpty(wxPayBillBaseResultLst)) {
            String columnNames[] = {"交易时间", "公众账号ID", "商户号", "子商户号", "设备号", "微信订单号", "商户订单号", "用户标识", "交易类型", "交易状态", "付款银行", "货币种类", "总金额", "企业红包金额", "微信退款单号", "商户退款单号", "退款金额", "企业红包退款金额", "退款类型", "退款状态", "商品名称", "商户数据包", "手续费", "费率"};//列明

            String keys[] = {"tradeTime", "appId", "mchId", "subMchId", "deviceInfo", "transationId", "outTradeNo", "openId", "tradeType", "tradeState", "bankType", "feeType", "totalFee", "couponFee", "refundId", "outRefundNo", "settlementRefundFee", "couponRefundFee", "refundChannel", "refundState", "body", "attach", "poundage", "poundageRate"};//map中的key

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            String fileName = billDate + "_对账单";
            if (billDate.contains("|")){
                String dates[] = billDate.split("\\|");
                fileName = dates[0] + "-" + dates[1] + "_对账单";
            }
            try {
                List<Map<String, Object>> sheetData = convertObjToMap(wxPayBillBaseResultLst);
                ExcelUtil.createWorkBook(sheetData, keys, columnNames).write(os);
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } catch (IOException e) {
                log.error("", e);
            }
            byte[] content = os.toByteArray();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Disposition", "attachment;filename=" + new String((fileName + ".xlsx").getBytes()));
            ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(content, httpHeaders, HttpStatus.OK);
            return responseEntity;
        }
        return null;
    }

    private List<MiniPayBillBaseResult> fetchBillData(String env,String billDate){
        if (billDate.contains("|")){
            String dates[] = billDate.split("\\|");
            int scope = Integer.valueOf(dates[1]);
            List<MiniPayBillBaseResult> billData = Lists.newArrayList();
            int year = Integer.valueOf(dates[0].substring(0,4));
            int month = Integer.valueOf(dates[0].substring(4));
            DateTime dateTime = new DateTime(year, month, 1, 1, 0, 0);
            int maxDays = dateTime.dayOfMonth().getMaximumValue();
            if (scope == 1){
                for (int i=1;i<=10;i++){
                    String date = dates[0]+"0"+i;
                    if (i == 10){
                        date = dates[0]+i;
                    }
                    ResponseResult<MiniPayBillResult> result = miniAppService.downloadBill(MiniBillRequest.builder().env(env).billDate(date).build());
                    if (result.isSuccess() && result.getData() != null && CollectionUtils.isNotEmpty(result.getData().getWxPayBillBaseResultLst())){
                        billData.addAll(result.getData().getWxPayBillBaseResultLst());
                    }
                }
            }else if (scope == 2){
                for (int i=11;i<=20;i++){
                    String date = dates[0]+i;
                    ResponseResult<MiniPayBillResult> result = miniAppService.downloadBill(MiniBillRequest.builder().env(env).billDate(date).build());
                    if (result.isSuccess() && result.getData() != null && CollectionUtils.isNotEmpty(result.getData().getWxPayBillBaseResultLst())){
                        billData.addAll(result.getData().getWxPayBillBaseResultLst());
                    }
                }
            }else {
                for (int i=21;i<=maxDays;i++){
                    String date = dates[0]+i;
                    ResponseResult<MiniPayBillResult> result = miniAppService.downloadBill(MiniBillRequest.builder().env(env).billDate(date).build());
                    if (result.isSuccess() && result.getData() != null && CollectionUtils.isNotEmpty(result.getData().getWxPayBillBaseResultLst())){
                        billData.addAll(result.getData().getWxPayBillBaseResultLst());
                    }
                }
            }
            return billData;
        }
        ResponseResult<MiniPayBillResult> result = miniAppService.downloadBill(MiniBillRequest.builder().env(env).billDate(billDate).build());
        return result.getData().getWxPayBillBaseResultLst();

    }


    private List<Map<String, Object>> convertObjToMap(List<MiniPayBillBaseResult> wxPayBillBaseResultLst) {
        List<Map<String, Object>> listmap = Lists.newArrayList();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "sheet1");
        listmap.add(map);
        //List<Map<String, Object>> billMapList = wxPayBillBaseResultLst.stream().map(billBase -> BeanMapper.map(billBase, Map.class)).collect(Collectors.toList());
        wxPayBillBaseResultLst.stream().forEach(billBase -> {
            log.info("--- billBase -> {}", JSONObject.toJSONString(billBase));
            listmap.add(BeanMapper.map(billBase, Map.class));
        });
        return listmap;
    }
}