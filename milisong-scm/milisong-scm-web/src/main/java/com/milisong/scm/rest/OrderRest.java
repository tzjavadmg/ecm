package com.milisong.scm.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.util.BeanMapper;
import com.milisong.scm.constant.SysConstant;
import com.milisong.scm.orderset.api.OrderService;
import com.milisong.scm.orderset.dto.param.OrderReserveSearchParamDto;
import com.milisong.scm.orderset.dto.param.OrderSearchParam;
import com.milisong.scm.orderset.dto.result.OrderExportResult;
import com.milisong.scm.orderset.dto.result.OrderReserveSearchResultDto;
import com.milisong.scm.orderset.dto.result.OrderSearchResult;
import com.milisong.scm.orderset.util.DateUtil;
import com.milisong.scm.orderset.util.ExportUtil;
import com.milisong.scm.orderset.util.ExportWorkbook;
import com.milisong.scm.properties.OmsOrderCheckProperties;
import com.milisong.scm.properties.OssUrlProperties;
import com.milisong.upms.utils.RestClient;
import com.milisong.upms.utils.UserInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SCM这边的订单相关的rest
 *
 * @author yangzhilong
 */
@Slf4j
@RestController
@RequestMapping("/order")
@RefreshScope
public class OrderRest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OssUrlProperties ossUrlProperties;
    @Value("${file.export.path}")
    private String exportPath;
    @Autowired
    private OmsOrderCheckProperties omsOrderCheckProperties;

    /**
     * 分页查询门店商品预定总量
     *
     * @param param
     * @return
     */
    @PostMapping("/page-reserve-group-list")
    public ResponseResult<Pagination<OrderReserveSearchResultDto>> pageSearchReserveGroupInfoList(@RequestBody OrderReserveSearchParamDto param) {
        try {
            ResponseResult<String> checkResult = checkShopPermission(param.getShopId());
            if (!checkResult.isSuccess()) {
                return ResponseResult.buildFailResponse(checkResult.getCode(), checkResult.getMessage());
            }
            log.info("分页查询商品汇总:{}", JSONObject.toJSONString(param));
            String ossResult = RestClient.post(ossUrlProperties.getSeartchReserveGroupInfo(), param);
            log.info("分页查询商品汇总返回:{}", JSONObject.toJSONString(ossResult));
            ResponseResult result = JSONObject.parseObject(ossResult, ResponseResult.class);
            if (result.isSuccess()) {
                return result;
            }
            return ResponseResult.buildFailResponse(result.getCode(), result.getMessage());
        } catch (Exception e) {
            log.error("分页查询门店商品汇总异常", e);
            throw e;
        }
    }

    @PostMapping("/get-order-details")
    public ResponseResult<Pagination<OrderSearchResult>> getOrderDetails(@RequestBody OrderSearchParam param) {
        List<String> shopIdList = UserInfoUtil.getCurrentUserShopIdList();
        if (null == shopIdList || shopIdList.isEmpty()) {
            return ResponseResult.buildFailResponse(SysConstant.SYSTEM_INFO.NO_SHOP_PERMISSION.getCode(), SysConstant.SYSTEM_INFO.NO_SHOP_PERMISSION.getDesc());
        }
        if (StringUtils.isBlank(param.getShopId())) {
            StringBuffer sb = new StringBuffer();
            shopIdList.stream().forEach(shopId -> {
                sb.append(shopId).append(",");
            });
            sb.deleteCharAt(sb.lastIndexOf(","));
            param.setShopId(sb.toString());
        }
        log.info("分页条件查询订单信息{}", JSON.toJSONString(param));
        String ossResult = RestClient.post(ossUrlProperties.getSearchOrder(), param);
        log.info("请求oss订单查询接口返回:{}", ossResult);
        ResponseResult result = JSONObject.parseObject(ossResult, ResponseResult.class);
        if (result.isSuccess()) {
            return result;
        }
        return ResponseResult.buildFailResponse(result.getCode(), result.getMessage());
    }


    @GetMapping("/export")
    public void exportOrder(OrderSearchParam param, HttpServletResponse response) {
        if ((StringUtils.isBlank(param.getDeliveryStartDate()) && StringUtils.isBlank(param.getDeliveryEndDate()))
                && (StringUtils.isBlank(param.getOrderStartDate()) && StringUtils.isBlank(param.getOrderEndDate()))) {
            log.error("查询订单的日期参数不能为空！");
            return;
        }
        List<String> shopIdList = UserInfoUtil.getCurrentUserShopIdList();
        if (null == shopIdList || shopIdList.isEmpty()) {
            log.error("用户没有门店权限！");
            return;
        }
        if (StringUtils.isBlank(param.getShopId())) {
            StringBuffer sb = new StringBuffer();
            shopIdList.stream().forEach(shopId -> {
                sb.append(shopId).append(",");
            });
            sb.deleteCharAt(sb.lastIndexOf(","));
            param.setShopId(sb.toString());
        }

        String fileName = "门店订单明细" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        FileOutputStream outputStream;
        try {
            String filePath = exportPath + fileName + ".xlsx";
            File file = new File(filePath);
            if (!file.exists()) {
                new File(exportPath).mkdirs();
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);

            String sheetName = "门店订单明细";
            param.setPageSize(500);
            List<OrderExportResult> orderList = getOrderListForExport(param);
            Map<String, String> headTitles = ExportUtil.getHeadTitles(OrderExportResult.class);
            ExportWorkbook workbook = new ExportWorkbook();
            workbook.createHead(headTitles, sheetName, 0);
            while (orderList.size() > 0) {
                workbook.inputData(sheetName, headTitles, orderList, null);
                param.setPageNo(param.getPageNo() + 1);
                orderList = getOrderListForExport(param);
            }
            workbook.closeWorkbook(outputStream);
            ExportUtil.downExcel(filePath, fileName, response);
        } catch (Exception e) {
            log.error("导出订单明细出错", e);
        }
    }

    private List<OrderExportResult> getOrderListForExport(OrderSearchParam param) {
        List<OrderExportResult> orderList = new ArrayList<>();
        log.info("分页条件查询订单信息{}", JSON.toJSONString(param));
        String ossResult = RestClient.post(ossUrlProperties.getSearchOrder(), param);
        log.info("请求oss订单查询接口返回:{}", ossResult);
        ResponseResult result = JSONObject.parseObject(ossResult, ResponseResult.class);
        if (result.isSuccess()) {
            Pagination pagination = JSONObject.parseObject(JSONObject.toJSONString(result.getData()), Pagination.class);
            List<OrderSearchResult> list = (List<OrderSearchResult>) pagination.getDataList();
            orderList = BeanMapper.mapList(list, OrderExportResult.class);
            orderList.stream().forEach(exportOrder -> {
                if (exportOrder.getGoodsResultList().size() > 0) {
                    StringBuffer sbuffer = new StringBuffer();
                    exportOrder.getGoodsResultList().stream().forEach(goods -> {
                        sbuffer.append(goods.getGoodsName().concat("*").concat(String.valueOf(goods.getGoodsNumber())));
                        sbuffer.append(",");
                    });
                    sbuffer.deleteCharAt(sbuffer.lastIndexOf(","));
                    exportOrder.setGoodsInfo(sbuffer.toString());
                }
            });
        }
        return orderList;
    }

    @GetMapping("/check")
    public ResponseResult<Object> checkOrder() {
        String url = omsOrderCheckProperties.getCheckUrl().concat("?businessLine=1&deliveryDate=").concat(DateUtil.getTodayYyyyMmDdStr());
        String result = RestClient.post(url, null);
        if (StringUtils.isNotBlank(result)) {
            JSONObject json = JSONObject.parseObject(result);
            if (json.getBooleanValue("success")) {
                JSONArray array = json.getJSONArray("data");
                if (null != array && !array.isEmpty()) {
                    List<String> omsList = getOmsOrderList(JSONObject.parseArray(array.toJSONString(), String.class));

                    final List<String> lost = new ArrayList<>();

                    List<String> list = getTodayOrderList();
                    if (CollectionUtils.isEmpty(list)) {
                        lost.addAll(omsList);
                    } else {
                        omsList.forEach(item -> {
                            if (!list.contains(item)) {
                                lost.add(item);
                            }
                        });
                    }

                    if (!CollectionUtils.isEmpty(lost)) {
                        log.error("oms订单号：{}数据在B端不存在", JSONObject.toJSONString(lost));
                        RestClient.post(omsOrderCheckProperties.getNotifyUrl(), lost);
                    }
                }
            } else {
                log.error("调用oms的订单查询返回失败:" + result);
            }
        }
        return ResponseResult.buildSuccessResponse();
    }

    /**
     * 获取oms订单列表
     *
     * @param omsList
     * @return
     */
    private List<String> getOmsOrderList(List<String> omsList) {
        if (!CollectionUtils.isEmpty(omsList)) {
            List<String> orderList = new ArrayList<>(omsList.size());
            omsList.forEach(item -> {
                orderList.add(item.split("_")[0]);
            });
            return orderList;
        }
        return null;
    }

    /**
     * 获取今天配送的订单
     *
     * @return
     */
    private List<String> getTodayOrderList() {
        List<String> list = this.orderService.getTodayAllOrderNo();
        if (!CollectionUtils.isEmpty(list)) {
            List<String> orderList = new ArrayList<>(list.size());
            list.forEach(item -> {
                orderList.add(item.split("_")[0]);
            });
            return orderList;
        }
        return null;
    }

    private ResponseResult<String> checkShopPermission(Long shopId) {
        if (!UserInfoUtil.checkShopPermission(shopId)) {
            return ResponseResult.buildFailResponse(SysConstant.SYSTEM_INFO.NO_SHOP_PERMISSION.getCode(), SysConstant.SYSTEM_INFO.NO_SHOP_PERMISSION.getDesc());
        } else {
            return ResponseResult.buildSuccessResponse();
        }
    }
}
