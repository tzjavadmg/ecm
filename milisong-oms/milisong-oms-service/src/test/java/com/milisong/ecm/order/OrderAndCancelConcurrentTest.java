package com.milisong.ecm.order;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.milisong.oms.api.StockService;
import com.milisong.oms.constant.GoodsStockSource;
import com.milisong.oms.param.*;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/11/8 13:53
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = TestApplication.class)
public class OrderAndCancelConcurrentTest {

    private final String baseUrl = "http://localhost:8888";

    @Test
    public void testVirtualOrderAndSetStock() throws Exception {

        final int threads = Runtime.getRuntime().availableProcessors() * 3;
        final int iterations = 2;
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(iterations);
        ExecutorService executor = Executors.newScheduledThreadPool(threads);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    startGate.await();
                    createVirtualOrder();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    endGate.countDown();
                }
            }
        });
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    startGate.await();
                    setShopOnSaleStock();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    endGate.countDown();
                }
            }
        });

        startGate.countDown();
        endGate.await();
    }

    @Test
    public void testOrderAndCancel() throws Exception {
        final int threads = Runtime.getRuntime().availableProcessors() * 3;
        final int iterations = 2;
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(iterations);

        ExecutorService executor = Executors.newScheduledThreadPool(threads);
        Map<String, Long> retMap = createVirtualOrder();
        long orderId = retMap.get("orderId");
        long deliveryId = retMap.get("deliveryId");
        System.out.println("订单ID：" + orderId + " 配送ID：" + deliveryId);

        //createOrder(orderId,orderId);
        //cancelVirtualOrder(orderId);

        //executor.execute(new Runnable() {
        //    @Override
        //    public void run() {
        //        try {
        //        //    startGate.await();
        //            createOrder(orderId,orderId);
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
        //        //finally {
        //        //    endGate.countDown();
        //        //}
        //    }
        //});
        //executor.execute(new Runnable() {
        //    @Override
        //    public void run() {
        //        try {
        //            //startGate.await();
        //            //cancelVirtualOrder(orderId);
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        } finally {
        //            //endGate.countDown();
        //        }
        //    }
        //});

        //startGate.countDown();
        //endGate.await();
    }

    private void setShopOnSaleStock() throws Exception {
        List<StockService.ShopDailyStock> shopDailyStockList = new ArrayList<>();
        StockService.ShopDailyStock shopDailyStock = new StockService.ShopDailyStock();
        shopDailyStock.setShopCode("31010001");
        shopDailyStock.setSaleDate(DateUtils.parseDate("2018-11-14", "yyyy-MM-dd"));
        shopDailyStock.setSource(GoodsStockSource.SET_STOCK.getValue());
        shopDailyStock.setGoodsStocks(Lists.newArrayList(new StockService.GoodsStock("9450823411", -1)));
        shopDailyStockList.add(shopDailyStock);

        ResponseEntity<ResponseResult> responseEntity = restTemplate().postForEntity(baseUrl + "/v1/goods/set-onsale-stock-list", shopDailyStockList, ResponseResult.class);
        ResponseResult responseResult = responseEntity.getBody();
        System.out.println(JSON.toJSONString(responseResult));
    }

    private Map<String, Long> createVirtualOrder() throws ParseException {
        VirtualOrderParam virtualOrderParam = new VirtualOrderParam();
        virtualOrderParam.setBuildingId(123456L);
        virtualOrderParam.setShopCode("31010001");
        virtualOrderParam.setConfirm(true);

        VirtualOrderDeliveryParam virtualOrderDeliveryParam = new VirtualOrderDeliveryParam();
        virtualOrderDeliveryParam.setDeliveryDate(DateUtils.parseDate("2018-11-14", "yyyy-MM-dd"));
        virtualOrderParam.setDeliveries(Lists.newArrayList(virtualOrderDeliveryParam));

        VirtualOrderDeliveryGoodsParam virtualOrderDeliveryGoodsParam = new VirtualOrderDeliveryGoodsParam();
        virtualOrderDeliveryGoodsParam.setGoodsCode("9450823411");
        virtualOrderDeliveryGoodsParam.setGoodsCount(1);
        virtualOrderDeliveryParam.setDeliveryGoods(Lists.newArrayList(virtualOrderDeliveryGoodsParam));

        ResponseEntity<ResponseResult> responseEntity = restTemplate().postForEntity(baseUrl + "/v2/order/order", virtualOrderParam, ResponseResult.class);
        ResponseResult responseResult = responseEntity.getBody();
        //return BeanMapper.map(Objects.requireNonNull(responseResult).getData(), VirtualOrderDto.class);
        Map responseMap = (Map) Objects.requireNonNull(responseResult).getData();
        Map retMap = Maps.newHashMap();
        retMap.put("orderId", (Long) responseMap.get("id"));
        if (responseMap.get("deliveries") != null) {
            Map deliveryMap = (Map) ((List) responseMap.get("deliveries")).get(0);
            retMap.put("deliveryId", (Long) deliveryMap.get("id"));
        }
        return retMap;
    }

    private void createOrder(Long orderId, Long deliveryId) throws Exception {
        OrderPaymentParam orderPaymentParam = new OrderPaymentParam();
        orderPaymentParam.setOrderId(orderId);
        orderPaymentParam.setDeliveryBuildingId(1448557259001830L);
        orderPaymentParam.setDeliveryAddressId(1488576817463296L);
        orderPaymentParam.setDeliveryCompany("米立");
        orderPaymentParam.setDeliveryFloor("11");
        orderPaymentParam.setRealName("盖伦");
        orderPaymentParam.setMobileNo("15316388732");
        orderPaymentParam.setRedPacketId(1521682722538041L);
        orderPaymentParam.setNotifyType((short) 1);
        orderPaymentParam.setFormId("======");
        orderPaymentParam.setSex((short) 1);

        DeliveryTimezoneParam deliveryTimezoneParam = new DeliveryTimezoneParam();
        deliveryTimezoneParam.setDeliveryId(deliveryId);
        deliveryTimezoneParam.setId(1685751398858752L);
        deliveryTimezoneParam.setShopCode("31010001");

        orderPaymentParam.setDeliveryTimezones(Lists.newArrayList(deliveryTimezoneParam));
        ResponseEntity<ResponseResult> responseEntity = restTemplate().postForEntity(baseUrl + "/v2/order/wechat-mini-pay", orderPaymentParam, ResponseResult.class);

    }

    private void cancelVirtualOrder(Long orderId) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", String.valueOf(orderId));
        ResponseEntity<ResponseResult> responseEntity = restTemplate().postForEntity(baseUrl + "/v2/order/virtual-order-cancel", params, ResponseResult.class);
    }

    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(30000);
        httpRequestFactory.setConnectTimeout(30000);
        httpRequestFactory.setReadTimeout(30000);

        return new RestTemplate(httpRequestFactory);
    }
}
