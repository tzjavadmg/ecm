package com.milisong.breakfast.scm.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.farmland.core.util.BeanMapper;
import com.milisong.breakfast.scm.constant.SysConstant;
import com.milisong.breakfast.scm.order.constant.OrderTypeEnum;
import com.milisong.breakfast.scm.properties.OssUrlProperties;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.breakfast.scm.common.properties.SystemProperties;
import com.milisong.breakfast.scm.common.util.DateUtil;
import com.milisong.breakfast.scm.common.util.ExportUtil;
import com.milisong.breakfast.scm.common.util.ExportWorkbook;
import com.milisong.breakfast.scm.order.api.OrderService;
import com.milisong.breakfast.scm.order.dto.param.OrderReserveSearchParamDto;
import com.milisong.breakfast.scm.order.dto.param.OrderSearchParam;
import com.milisong.breakfast.scm.order.dto.result.OrderExportResult;
import com.milisong.breakfast.scm.order.dto.result.OrderReserveSearchResult;
import com.milisong.breakfast.scm.order.dto.result.OrderSearchResult;
import com.milisong.upms.constant.SsoErrorConstant;
import com.milisong.upms.utils.RestClient;
import com.milisong.upms.utils.UserInfoUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * SCM????????????????????????rest
 * @author yangzhilong
 *
 */
@Api(tags = "????????????")
@Slf4j
@RestController
@RequestMapping("/order")
@RefreshScope
public class OrderRest {
	@Autowired
	private OrderService orderService;
	@Resource
    private SystemProperties systemProperties;
	@Autowired
	private OssUrlProperties ossUrlProperties;
	/**
	 * ????????????????????????????????????
	 * @param param
	 * @return
	 */
	@PostMapping("/page-reserve-group-list")
	@ApiOperation("????????????????????????????????????")
	public ResponseResult<Pagination<OrderReserveSearchResult>> pageSearchReserveGroupInfoList(@RequestBody OrderReserveSearchParamDto param) {
		try {
			ResponseResult<String> checkResult = checkShopPermission(param.getShopId());
			if(!checkResult.isSuccess()){
				return ResponseResult.buildFailResponse(checkResult.getCode(),checkResult.getMessage());
			}
			return ResponseResult.buildSuccessResponse(orderService.pageSearchReserveOrder(param));
		} catch (Exception e) {
			log.error("??????????????????????????????????????????", e);
			throw e;
		}
	}

	/**
	 * ????????????????????????
	 * @param param
	 * @return
	 */
	@PostMapping("/get-order-details")
	@ApiOperation("????????????????????????")
	public ResponseResult<Pagination<OrderSearchResult>> getOrderDetails(@RequestBody OrderSearchParam param){
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
		param.setOrderType(OrderTypeEnum.BREAKFAST.getCode());
		log.info("??????????????????????????????{}", JSON.toJSONString(param));
		String ossResult = RestClient.post(ossUrlProperties.getSearchOrder(), param);
		log.info("??????oss????????????????????????:{}", ossResult);
		ResponseResult result = JSONObject.parseObject(ossResult, ResponseResult.class);
		if (result.isSuccess()) {
			return result;
		}
		return ResponseResult.buildFailResponse(result.getCode(), result.getMessage());
	}

	/**
	 * ??????????????????
	 * @param param
	 * @param response
	 */
	@GetMapping("/export")
	@ApiOperation("??????????????????")
	public void exportOrder(OrderSearchParam param,HttpServletResponse response){
		if ((StringUtils.isBlank(param.getDeliveryStartDate()) && StringUtils.isBlank(param.getDeliveryEndDate()))
				&& (StringUtils.isBlank(param.getOrderStartDate()) && StringUtils.isBlank(param.getOrderEndDate()))) {
			log.error("??????????????????????????????????????????");
			return;
		}
		List<String> shopIdList = UserInfoUtil.getCurrentUserShopIdList();
		if (null == shopIdList || shopIdList.isEmpty()) {
			log.error("???????????????????????????");
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
		param.setOrderType(OrderTypeEnum.BREAKFAST.getCode());
		String fileName = "??????????????????" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
		FileOutputStream outputStream;
		try {
			String filePath = systemProperties.getFileOss().getExportPath() + fileName + ".xlsx";
			File file = new File(filePath);
			if (!file.exists()) {
				new File(systemProperties.getFileOss().getExportPath()).mkdirs();
				file.createNewFile();
			}
			outputStream = new FileOutputStream(file);

			String sheetName = "??????????????????";
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
			log.error("????????????????????????", e);
		}

	}
	
	@GetMapping("/check")
	public ResponseResult<Object> checkOrder() {
		String url = systemProperties.getOmsOrder().getCheckUrl().concat("?businessLine=0&deliveryDate=").concat(DateUtil.getTodayYyyyMmDdStr());
		String result = RestClient.post(url, null);
		if(StringUtils.isNotBlank(result)) {
			JSONObject json = JSONObject.parseObject(result);
			if(json.getBooleanValue("success")) {
				JSONArray array = json.getJSONArray("data");
				if(null!=array && !array.isEmpty()) {
					List<String> omsList = getOmsOrderList(JSONObject.parseArray(array.toJSONString(), String.class));
					
					final List<String> lost = new ArrayList<>();
					
					List<String> list = getTodayOrderList();
					if(CollectionUtils.isEmpty(list)) {
						lost.addAll(omsList);
					} else {
						omsList.forEach(item -> {
							if(!list.contains(item)) {
								lost.add(item);
							}
						});
					}
					
					if(!CollectionUtils.isEmpty(lost)) {
						log.error("oms????????????{}?????????B????????????", JSONObject.toJSONString(lost));
						RestClient.post(systemProperties.getOmsOrder().getNotifyUrl(), lost);
					}
				}
			} else {
				log.error("??????oms???????????????????????????:" + result);
			}
		}
		return ResponseResult.buildSuccessResponse();
	}
	
	/**
	 * ??????oms????????????
	 * @param omsList
	 * @return
	 */
	private List<String> getOmsOrderList(List<String> omsList) {
		if(!CollectionUtils.isEmpty(omsList)) {
			List<String> orderList = new ArrayList<>(omsList.size());
			omsList.forEach(item -> {
				orderList.add(item.split("_")[0]);
			});
			return orderList;
		}
		return null;
	}
	
	/**
	 * ???????????????????????????
	 * @return
	 */
	private List<String> getTodayOrderList() {
		List<String> list = this.orderService.getTodayAllOrderNo();
		if(!CollectionUtils.isEmpty(list)) {
			List<String> orderList = new ArrayList<>(list.size());
			list.forEach(item -> {
				orderList.add(item.split("_")[0]);
			});
			return orderList;
		}
		return null;
	}

	
	private ResponseResult<String> checkShopPermission(Long shopId){
		if(!UserInfoUtil.checkShopPermission(shopId)){
			return ResponseResult.buildFailResponse(SsoErrorConstant.SYSTEM_INFO.NO_SHOP_PERMISSION.getCode(),SsoErrorConstant.SYSTEM_INFO.NO_SHOP_PERMISSION.getDesc());
		}else{
			return ResponseResult.buildSuccessResponse();
		}
	}


	private List<OrderExportResult> getOrderListForExport(OrderSearchParam param) {
		List<OrderExportResult> orderList = new ArrayList<>();
		log.info("??????????????????????????????{}", JSON.toJSONString(param));
		String ossResult = RestClient.post(ossUrlProperties.getSearchOrder(), param);
		log.info("??????oss????????????????????????:{}", ossResult);
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


}
