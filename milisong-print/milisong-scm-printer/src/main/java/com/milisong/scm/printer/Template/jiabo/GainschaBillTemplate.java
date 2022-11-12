package com.milisong.scm.printer.Template.jiabo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gp.command.EscCommand;
import com.gp.command.EscCommand.ENABLE;
import com.gp.command.EscCommand.FONT;
import com.gp.command.EscCommand.HEIGHT_ZOOM;
import com.gp.command.EscCommand.JUSTIFICATION;
import com.gp.command.EscCommand.WIDTH_ZOOM;
import com.milisong.scm.printer.dto.OrderDetailInfo;
import com.milisong.scm.printer.request.BuildingDto;
import com.milisong.scm.printer.request.DistributionOrdersetResult;
import com.milisong.scm.printer.request.OrderSetDetailDto;
import com.milisong.scm.printer.request.PickOrderGoodsSumResult;
import com.milisong.scm.printer.request.mq.OrderSetDetailGoodsDto;
import com.milisong.scm.printer.util.jiabo.StringUtils;

/**
*@author    created by benny
*@date  2018年9月14日---上午10:56:07
*
*/
@Component
public class GainschaBillTemplate {

//	//开发
	@Value("${qrcode.message-url}")
	private String qrcodeMessageUrl = "";
// 测试	
//	@Value("${qrcode.message-url}")
//	private String qrcodeMessageUrl = "http://testmls-api.jshuii.com/v1/delivery/notice/";

//  生产
//	@Value("${qrcode.message-url}")
	//private String qrcodeMessageUrl = "http://mls-api.jshuii.com/v1/delivery/notice/";

	
	/**
	 * 配送联
	 * @param map
	 * @return
	 */
	public byte[] distributedTemplate(Map<String, Object> map) {
		EscCommand esc = new EscCommand();
		esc.addSelectJustification(JUSTIFICATION.CENTER);
		esc.addSelectCharacterFont(FONT.FONTA);
		esc.addTurnEmphasizedModeOnOrOff(ENABLE.ON);
		//esc.addText(map.get("detailSetNoDescription").toString());\
		esc.addSetCharcterSize(WIDTH_ZOOM.MUL_2, HEIGHT_ZOOM.MUL_2);
		esc.addText("（配送联）");
		esc.addPrintAndLineFeed();
		esc.addText("配送单号: "+map.get("distributionDescription").toString());
		esc.addPrintAndLineFeed();
		esc.addPrintAndFeedLines((byte) 1);
		//esc.addText("# "+map.get("detailDeliveryDate").toString()+"配送订单");
		esc.addSelectJustification(JUSTIFICATION.LEFT);
		esc.addSetCharcterSize(WIDTH_ZOOM.MUL_1, HEIGHT_ZOOM.MUL_1);
		esc.addText("---------------------------------------------");
		esc.addPrintAndLineFeed();
		esc.addSelectJustification(JUSTIFICATION.CENTER);
		esc.addText("# "+map.get("deliveryDate")+"配送订单");
		esc.addPrintAndLineFeed();
		esc.addSelectJustification(JUSTIFICATION.LEFT);
		esc.addText("---------------------------------------------");
		esc.addPrintAndLineFeed();
		
		Map<Long,Set<String>> mapResult = (Map<Long,Set<String>>) map.get("distributionOrdersetResultMap");
		Object mapBuildingObject =  map.get("buildingMaps");
		Map<Long, Object> mapBuilding = new HashMap<Long, Object>();
		if(mapBuildingObject !=null) {
			mapBuilding = (Map<Long, Object>) mapBuildingObject;
		}
		for (Long buildingId : mapResult.keySet()) {
			Object obj = mapBuilding.get(buildingId);
			BuildingDto buildingDto = new BuildingDto();
			if(obj != null) {
				buildingDto = (BuildingDto) obj;
			}
			esc.addSetCharcterSize(WIDTH_ZOOM.MUL_2, HEIGHT_ZOOM.MUL_2);
			esc.addText(buildingDto.getAbbreviation(),"UTF-8");
			esc.addPrintAndLineFeed();
			esc.addSetCharcterSize(WIDTH_ZOOM.MUL_1, HEIGHT_ZOOM.MUL_1);
			esc.addPrintAndLineFeed();
			esc.addText("地址："+buildingDto.getDetailAddress(),"UTF-8");
			esc.addPrintAndLineFeed();
			esc.addText("---------------------------------------------");
			int i = 1;
			for (String distributionOrderSet : mapResult.get(buildingId)) {
				esc.addPrintAndFeedLines((byte) 1);
				esc.addText(i+"、 "+distributionOrderSet,"UTF-8");
				esc.addPrintAndLineFeed();
				i++;
			}
			esc.addText("_____________________________________________","UTF-8");
			esc.addPrintAndLineFeed();
			esc.addTurnEmphasizedModeOnOrOff(ENABLE.OFF);
		}
		esc.addPrintAndLineFeed();
		esc.addPrintAndFeedLines((byte) 10);
		esc.addCutPaper();
		return getMessage(esc);
	}
	
	/**
	 * 拣货单
	 * @param map
	 * @return
	 */
	public byte[] pickOrderTemplate(Map<String, Object> map) {
		EscCommand esc = new EscCommand();
		esc.addSelectJustification(JUSTIFICATION.CENTER);
		esc.addSelectCharacterFont(FONT.FONTA);
		esc.addTurnEmphasizedModeOnOrOff(ENABLE.ON);
		esc.addSetCharcterSize(WIDTH_ZOOM.MUL_2, HEIGHT_ZOOM.MUL_2);
		esc.addText("（生产联）");
		esc.addPrintAndLineFeed();
		esc.addText("配送单号: "+map.get("distributionDescription").toString());
		esc.addPrintAndLineFeed();
		esc.addSetCharcterSize(WIDTH_ZOOM.MUL_1, HEIGHT_ZOOM.MUL_1);
		esc.addSelectJustification(JUSTIFICATION.LEFT);
		esc.addText("---------------------------------------------");
		esc.addPrintAndLineFeed();
		esc.addSelectJustification(JUSTIFICATION.CENTER);
		esc.addText("# "+map.get("deliveryDate").toString()+"配送订单");
		esc.addPrintAndLineFeed();
		esc.addSelectJustification(JUSTIFICATION.LEFT);
		esc.addText("---------------------------------------------");
		esc.addPrintAndLineFeed();
		esc.addText("集单号: ");
		List<DistributionOrdersetResult> listOrderSetNo = (List<DistributionOrdersetResult>) map.get("orderSetNo");
		for (DistributionOrdersetResult distributionOrdersetResult : listOrderSetNo) {
			esc.addText(" "+distributionOrdersetResult.getDetailSetNoDescription()+"  ");
		}
		esc.addPrintAndLineFeed();
		esc.addTurnEmphasizedModeOnOrOff(ENABLE.OFF);
		esc.addText("---------------------------------------------");
		esc.addPrintAndLineFeed();
		esc.addPrintAndFeedLines((byte) 1);
		List<PickOrderGoodsSumResult> listGoodsCount = (List<PickOrderGoodsSumResult>) map.get("goodsList");
		int sunGoodsCount = 0;
		int i = 1;
		for (PickOrderGoodsSumResult pickOrderGoodsSumResult : listGoodsCount) {
			sunGoodsCount += pickOrderGoodsSumResult.getGoodsCount();
			esc.addText(i+"、 "+pickOrderGoodsSumResult.getGoodsName()+" *"+pickOrderGoodsSumResult.getGoodsCount(),"UTF-8");
			esc.addPrintAndLineFeed();
			i++;
		}
		esc.addText("---------------------------------------------");
		esc.addPrintAndLineFeed();
		esc.addPrintAndFeedLines((byte) 1);
		esc.addText("合计： "+sunGoodsCount+"份");
		esc.addPrintAndLineFeed();
		esc.addPrintAndFeedLines((byte) 10);
		esc.addCutPaper();
		return getMessage(esc);
	}
	
	/**
	 * 集单
	 * @param map
	 * @return
	 */
	public byte[] setOrderTemplate(Map<String, Object> map) {
		OrderSetDetailDto orderSetDetailsInfo = (OrderSetDetailDto) map.get("orderSetDetailsInfo");
		String orderSetNo = orderSetDetailsInfo.getDetailSetNoDescription();
		Map<String,DistributionOrdersetResult> distributionOrdersetResultsMap = (Map<String, DistributionOrdersetResult>) map.get("distributionOrdersetResultsMap");
		List<OrderDetailInfo> list = (List<OrderDetailInfo>) map.get("detailsList");
		DistributionOrdersetResult distributionOrdersetResult = distributionOrdersetResultsMap.get(orderSetNo);
		
		EscCommand esc = new EscCommand();
		esc.addSelectJustification(JUSTIFICATION.CENTER);
		esc.addSelectCharacterFont(FONT.FONTA);
		esc.addTurnEmphasizedModeOnOrOff(ENABLE.ON);
		esc.addSetCharcterSize(WIDTH_ZOOM.MUL_2, HEIGHT_ZOOM.MUL_2);
		esc.addText("（集单联）");
		esc.addPrintAndLineFeed();
		esc.addText("配送单号："+map.get("distributionDescription").toString());
		esc.addPrintAndLineFeed();
		esc.addText("集单号："+orderSetNo);
		esc.addPrintAndLineFeed();
		esc.addSetCharcterSize(WIDTH_ZOOM.MUL_1, HEIGHT_ZOOM.MUL_1);
		esc.addSelectJustification(JUSTIFICATION.LEFT);
		esc.addText("---------------------------------------------");
		esc.addPrintAndLineFeed();
		esc.addSelectJustification(JUSTIFICATION.CENTER);
		esc.addText("# "+map.get("detailDeliveryDate")+"配送订单");
		esc.addPrintAndLineFeed();
		esc.addSelectJustification(JUSTIFICATION.LEFT);
		esc.addText("---------------------------------------------");
		esc.addPrintAndLineFeed();
		esc.addSelectJustification(JUSTIFICATION.CENTER);
		esc.addSetCharcterSize(WIDTH_ZOOM.MUL_1, HEIGHT_ZOOM.MUL_1);
		esc.addText("楼宇: "+distributionOrdersetResult.getBuildingAbbreviation());
		esc.addPrintAndLineFeed();
		esc.addText("层/公司："+distributionOrdersetResult.getDeliveryFloor()+"层/"+distributionOrdersetResult.getCompanyAbbreviation(),"UTF-8");
		esc.addPrintAndLineFeed();
		esc.addSelectJustification(JUSTIFICATION.LEFT);
		esc.addTurnEmphasizedModeOnOrOff(ENABLE.OFF);
		esc.addText("---------------------------------------------");
		esc.addPrintAndLineFeed();
		for (OrderDetailInfo orderDetailInfo : list) {
			esc.addText(orderDetailInfo.getName()+"（"+orderDetailInfo.getPhone()+"）  "+orderDetailInfo.getGoodsName()+"*"+orderDetailInfo.getGoodsCount(),"UTF-8");
			esc.addPrintAndLineFeed();
		}
		esc.addText("---------------------------------------------");
		esc.addPrintAndLineFeed();
		esc.addSelectJustification(JUSTIFICATION.CENTER);
		esc.addSelectErrorCorrectionLevelForQRCode((byte) 0x31); // 设置纠错等级
		esc.addSelectSizeOfModuleForQRCode((byte) 6);// 设置qrcode模块大小
		esc.addStoreQRCodeData(qrcodeMessageUrl+orderSetDetailsInfo.getDetailSetNo());// 设置qrcode内容
		esc.addPrintQRCode();// 打印QRCode
		esc.addPrintAndLineFeed();
		esc.addPrintAndFeedLines((byte)10);
		esc.addCutPaper();
		return getMessage(esc);
	}
	
	/**
	 * 客户联
	 * @param listCustomer 
	 * @param map
	 * @return
	 */
	public byte[] customerTemplate(List<OrderSetDetailGoodsDto> listCustomer, Map<String, Object> map) {
		OrderSetDetailGoodsDto customerInfo = listCustomer.get(0);
		Map<String,Integer> orderGoodsCountMap = (Map<String, Integer>) map.get("orderGoodsCountMap");
		EscCommand esc = new EscCommand();
		esc.addSelectJustification(JUSTIFICATION.CENTER);
		esc.addSelectCharacterFont(FONT.FONTA);
		esc.addTurnEmphasizedModeOnOrOff(ENABLE.ON);
		esc.addSetCharcterSize(WIDTH_ZOOM.MUL_2, HEIGHT_ZOOM.MUL_2);
		esc.addText("（顾客联）");
		esc.addPrintAndLineFeed();
		esc.addText("集单号："+map.get("orderSetNo"));
		esc.addPrintAndLineFeed();
		esc.addSetCharcterSize(WIDTH_ZOOM.MUL_1, HEIGHT_ZOOM.MUL_1);
		esc.addSelectJustification(JUSTIFICATION.LEFT);
		esc.addText("_____________________________________________","UTF-8");
		esc.addPrintAndLineFeed();
		esc.addSetCharcterSize(WIDTH_ZOOM.MUL_2, HEIGHT_ZOOM.MUL_2);
		esc.addSelectJustification(JUSTIFICATION.CENTER);
		esc.addPrintAndFeedLines((byte) 1);
		esc.addText(customerInfo.getUserName(),"UTF-8");
		esc.addPrintAndFeedLines((byte) 1);
		esc.addPrintAndLineFeed();
		esc.addSetCharcterSize(WIDTH_ZOOM.MUL_1, HEIGHT_ZOOM.MUL_1);
		esc.addText("("+StringUtils.convertMobile(customerInfo.getUserPhone())+")");
		esc.addPrintAndLineFeed();
		esc.addSelectJustification(JUSTIFICATION.LEFT);
		esc.addText("---------------------------------------------");
		esc.addPrintAndLineFeed();
		esc.addSelectJustification(JUSTIFICATION.CENTER);
		BigDecimal price = BigDecimal.ZERO;
		BigDecimal countPrice = BigDecimal.ZERO;
		String orderNo = customerInfo.getOrderNo();
		Integer goodsCout = 0;
		for (OrderSetDetailGoodsDto orderSetDetailGoodsDto : listCustomer) {
			BigDecimal count = new BigDecimal(orderSetDetailGoodsDto.getGoodsNumber());
			BigDecimal sumPrice = orderSetDetailGoodsDto.getGoodsDiscountPrice().multiply(count).setScale(2,BigDecimal.ROUND_DOWN);
			esc.addText(orderSetDetailGoodsDto.getGoodsName()+"    ","UTF-8");
//			esc.addText("￥"+orderSetDetailGoodsDto.getGoodsDiscountPrice()+"(原价￥"+orderSetDetailGoodsDto.getGoodsPrice()+")"+"*"+orderSetDetailGoodsDto.getGoodsNumber()+"   ");
//			esc.addText("￥"+sumPrice);
			esc.addText("*"+orderSetDetailGoodsDto.getGoodsNumber()+"   ");
			esc.addPrintAndLineFeed();
			esc.addPrintAndFeedLines((byte) 1);
			countPrice = countPrice.add(orderSetDetailGoodsDto.getGoodsPrice());
			price = price.add(sumPrice);
			goodsCout += orderSetDetailGoodsDto.getGoodsNumber();
		}
		esc.addSetAbsolutePrintPosition((byte) 0);
//		esc.addText("配送费");
//		esc.addSetRelativePrintPositon((byte) 600);
//		esc.addText(" 0元");
		esc.addPrintAndLineFeed();
		esc.addSelectJustification(JUSTIFICATION.CENTER);
		//esc.addText("实际支付：￥"+price+"元    原价：￥"+countPrice+"元");
		Integer orderGoodsCount = orderGoodsCountMap.get(orderNo);
		if(orderGoodsCount - goodsCout >0) {
		esc.addText("您还有 "+(orderGoodsCount - goodsCout)+" 份商品在其它配送单");
		esc.addPrintAndLineFeed();
		}
		esc.addSelectJustification(JUSTIFICATION.LEFT);
		esc.addText("---------------------------------------------");
		esc.addPrintAndLineFeed();
		esc.addText("订单号："+customerInfo.getCoupletNo());
		esc.addPrintAndFeedLines((byte) 1);
		esc.addPrintAndLineFeed();
		esc.addSelectJustification(JUSTIFICATION.LEFT);
		esc.addText("下单时间： "+DateFormatUtils.format(customerInfo.getOrderTime(), "yyyy-MM-dd HH:mm:ss"));
		esc.addPrintAndFeedLines((byte) 2);
//		esc.addPrintAndLineFeed();
//		esc.addText(map.get("detailDeliveryAddress").toString(),"UTF-8");
		esc.addPrintAndLineFeed();
		esc.addPrintAndFeedLines((byte) 10);
		esc.addPrintAndLineFeed();
		esc.addCutPaper();
		return getMessage(esc);
		
	}
	
	 byte[] getMessage(EscCommand esc) {
			Vector<Byte> datas = esc.getCommand();
			byte[] bdatas = new byte[datas.size()];
			for (int i = 0; i < datas.size(); i++) {
				bdatas[i] = datas.get(i);
			}
			return bdatas;
	}
	 
	    /*
	     * List分割
	     */
	    public static List<List<String>> groupList(List<String> list) {
	        List<List<String>> listGroup = new ArrayList<List<String>>();
	        int listSize = list.size();
	        //子集合的长度
	        int toIndex = 5;
	        for (int i = 0; i < list.size(); i += toIndex) {
	            if (i + toIndex > listSize) {
	                toIndex = listSize - i;
	            }
	            List<String> newList = list.subList(i, i + toIndex);
	            listGroup.add(newList);
	        }
	        return listGroup;
	    }
	 
	 
	 
	 
	 
	public static void main(String[] args) {
	        List<String> list = new ArrayList<>();
	        list.add("1");
	        list.add("2");
	        list.add("3");
	        list.add("4");
	        list.add("5");
	        list.add("6");
	        list.add("7");
	        List<List<String>> lists = groupList(list);
	        System.out.println("list:" + list.toString());
	        System.out.println(lists);
	}

	 
}
