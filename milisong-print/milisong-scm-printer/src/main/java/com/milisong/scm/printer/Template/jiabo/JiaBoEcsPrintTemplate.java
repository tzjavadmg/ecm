package com.milisong.scm.printer.Template.jiabo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.gp.command.EscCommand;
import com.gp.command.EscCommand.ENABLE;
import com.gp.command.EscCommand.FONT;
import com.gp.command.EscCommand.JUSTIFICATION;
import com.milisong.scm.printer.dto.OrderDetailInfo;
import com.milisong.scm.printer.request.mq.OrderSetDetailGoodsDto;

/**
*@author    created by benny
*@date  2018年9月7日---下午1:48:52
*
*/
public class JiaBoEcsPrintTemplate {

//	//开发
//	@Value("${qrcode.message-url}")
	//private String qrcodeMessageUrl = "http://devmls-api.jshuii.com/v1/delivery/notice/";
// 测试	
//	@Value("${qrcode.message-url}")
//	private String qrcodeMessageUrl = "http://testmls-api.jshuii.com/v1/delivery/notice/";

//  生产
//	@Value("${qrcode.message-url}")
	private String qrcodeMessageUrl = "http://mls-api.jshuii.com/v1/delivery/notice/";

	/**
	 * 门店联打印模版
	 * @param map 
	 */
	public byte[] shopTemplate(Map<String, Object> map) {
		EscCommand esc = new EscCommand();
		esc.addSelectJustification(JUSTIFICATION.CENTER);
		esc.addSelectCharacterFont(FONT.FONTA);
		esc.addTurnEmphasizedModeOnOrOff(ENABLE.ON);
		esc.addText(map.get("detailSetNoDescription").toString());
		esc.addPrintAndLineFeed();
		esc.addText("（门店联）");
		esc.addPrintAndLineFeed();
		esc.addPrintAndFeedLines((byte) 2);
		esc.addText("# "+map.get("detailDeliveryDate").toString()+"配送订单");
		esc.addPrintAndLineFeed();
		esc.addSelectJustification(JUSTIFICATION.LEFT);
		esc.addText("-----------------------------------------------");
		esc.addPrintAndLineFeed();
		esc.addText("配送地址："+map.get("detailDeliveryAddress").toString());
		esc.addPrintAndLineFeed();
		esc.addTurnEmphasizedModeOnOrOff(ENABLE.OFF);
		esc.addText("-----------------------------------------------");
		esc.addPrintAndLineFeed();
		esc.addPrintAndFeedLines((byte) 2);
		Object obj = map.get("goodsCountMap");
		Map<String,Integer> goodsCount = new HashMap<String,Integer>();
		if(obj != null) {
			goodsCount = (Map<String,Integer>) obj;
		}
		for (String goodsName : goodsCount.keySet()) {
			esc.addText(goodsName+"*"+goodsCount.get(goodsName));
			//esc.addText("赵先生（13112341234）  大排饭*5");
			esc.addPrintAndLineFeed();
			esc.addPrintAndFeedLines((byte) 1);
		}
		esc.addPrintAndFeedLines((byte) 10);
		esc.addCutPaper();
		return getMessage(esc);
	}
	
	public byte[] distributedTemplate(Map<String, Object> map) {
		EscCommand esc = new EscCommand();
		esc.addSelectJustification(JUSTIFICATION.CENTER);
		esc.addSelectCharacterFont(FONT.FONTA);
		esc.addTurnEmphasizedModeOnOrOff(ENABLE.ON);
		esc.addText(map.get("detailSetNoDescription").toString());
		esc.addPrintAndLineFeed();
		esc.addText("（配送联）");
		esc.addPrintAndLineFeed();
		esc.addPrintAndFeedLines((byte) 2);
		esc.addText("# "+map.get("detailDeliveryDate").toString()+"配送订单");
		esc.addPrintAndLineFeed();
		esc.addSelectJustification(JUSTIFICATION.LEFT);
		esc.addText("-----------------------------------------------");
		esc.addPrintAndLineFeed();
		esc.addText("配送地址："+map.get("detailDeliveryAddress").toString());
		esc.addPrintAndLineFeed();
		esc.addText("-----------------------------------------------");
		esc.addPrintAndLineFeed();
		esc.addPrintAndFeedLines((byte) 2);
		esc.addTurnEmphasizedModeOnOrOff(ENABLE.OFF);
		Object obj = map.get("detailsList");
		List<OrderDetailInfo> listInfo = new ArrayList<OrderDetailInfo>();
		if(obj != null) {
			listInfo = (List<OrderDetailInfo>) obj;
		}
		for (OrderDetailInfo orderDetailInfo : listInfo) {
			esc.addText(orderDetailInfo.getName()+"("+orderDetailInfo.getPhone()+")");
			esc.addText(orderDetailInfo.getGoodsName()+"*"+orderDetailInfo.getGoodsCount());
			//esc.addText("赵先生（13112341234）  大排饭*5");
			esc.addPrintAndLineFeed();
			esc.addPrintAndFeedLines((byte) 1);
		}
		esc.addText("-----------------------------------------------");
		esc.addPrintAndLineFeed();
		esc.addPrintAndFeedLines((byte) 3);
		esc.addSelectJustification(JUSTIFICATION.CENTER);
		esc.addSelectErrorCorrectionLevelForQRCode((byte) 0x31); // 设置纠错等级
		esc.addSelectSizeOfModuleForQRCode((byte) 6);// 设置qrcode模块大小
		esc.addStoreQRCodeData(qrcodeMessageUrl+map.get("detailSetNo").toString());// 设置qrcode内容
		esc.addPrintQRCode();// 打印QRCode
		esc.addPrintAndLineFeed();
		esc.addPrintAndFeedLines((byte) 10);
		esc.addCutPaper();
		return getMessage(esc);
	}
	
	public byte[] customerTemplate(List<OrderSetDetailGoodsDto> listCustomer, Map<String, Object> map){
		OrderSetDetailGoodsDto customerInfo = listCustomer.get(0);
		EscCommand esc = new EscCommand();
		esc.addSelectCharacterFont(FONT.FONTA);
		esc.addTurnEmphasizedModeOnOrOff(ENABLE.ON);
		esc.addSelectJustification(JUSTIFICATION.CENTER);
		esc.addText(map.get("detailSetNoDescription").toString());
		esc.addPrintAndLineFeed();
		esc.addText("（客户联）");
		esc.addPrintAndLineFeed();
		esc.addPrintAndFeedLines((byte) 2);
		esc.addText("# "+map.get("detailDeliveryDate").toString()+"配送订单");
		esc.addPrintAndLineFeed();
		esc.addSelectJustification(JUSTIFICATION.LEFT);
		esc.addText("-----------------------------------------------");
		esc.addPrintAndLineFeed();
		esc.addText("顾客姓名："+customerInfo.getUserName());
		esc.addPrintAndLineFeed();
		esc.addText("顾客电话："+customerInfo.getUserPhone());
		esc.addPrintAndLineFeed();
		esc.addText("配送地址："+map.get("detailDeliveryAddress").toString());
		esc.addPrintAndLineFeed();
		esc.addTurnEmphasizedModeOnOrOff(ENABLE.OFF);
		esc.addText("-----------------------------------------------");
		esc.addPrintAndLineFeed();
		esc.addText("下单时间："+DateFormatUtils.format(customerInfo.getOrderTime(), "yyyy-MM-dd HH:mm:ss"));
		esc.addPrintAndLineFeed();
		esc.addText("-----------------------------------------------");
		esc.addPrintAndFeedLines((byte) 2);
		esc.addPrintAndLineFeed();
		BigDecimal countPrice = BigDecimal.ZERO;
		BigDecimal price = BigDecimal.ZERO;
		for (OrderSetDetailGoodsDto orderSetDetailGoodsDto : listCustomer) {
			 BigDecimal count = new BigDecimal(orderSetDetailGoodsDto.getGoodsNumber());
			 BigDecimal sumPrice = orderSetDetailGoodsDto.getGoodsDiscountPrice().multiply(count).setScale(2,BigDecimal.ROUND_DOWN);
			//double sumPrice = orderSetDetailGoodsDto.getGoodsDiscountPrice().doubleValue()*orderSetDetailGoodsDto.getGoodsNumber();
			esc.addText(orderSetDetailGoodsDto.getGoodsName()+"    ");
			esc.addText("￥"+orderSetDetailGoodsDto.getGoodsDiscountPrice()+"(原价￥"+orderSetDetailGoodsDto.getGoodsPrice()+")"+"*"+orderSetDetailGoodsDto.getGoodsNumber()+"   ");
			esc.addText("￥"+sumPrice);
			esc.addPrintAndLineFeed();
			esc.addPrintAndFeedLines((byte) 1);
			countPrice = countPrice.add(orderSetDetailGoodsDto.getGoodsPrice());
			price = price.add(sumPrice);
			
		}
		esc.addText("---------------------其他----------------------");
		esc.addPrintAndLineFeed();
		esc.addSelectJustification(JUSTIFICATION.LEFT);
		esc.addText("配送费:  0元（原价￥4.0）");
		esc.addPrintAndLineFeed();
		esc.addText("-----------------------------------------------");
		esc.addPrintAndLineFeed();
		esc.addSelectJustification(JUSTIFICATION.RIGHT);
		esc.addText("原价:  "+countPrice+"元");
		esc.addPrintAndLineFeed();
		esc.addText("优惠金额:  "+(countPrice.subtract(price))+"元");
		esc.addPrintAndLineFeed();
		esc.addText("实际支付:  "+price+"元");
		esc.addPrintAndLineFeed();
		esc.addSelectJustification(JUSTIFICATION.LEFT);
		esc.addText("-----------------------------------------------");
		esc.addPrintAndLineFeed();
		esc.addPrintAndFeedLines((byte) 10);
		esc.addCutPaper();
		return getMessage(esc);
	}
	
	public byte[] getMessage(EscCommand esc) {
		Vector<Byte> datas = esc.getCommand();
		byte[] bdatas = new byte[datas.size()];
		for (int i = 0; i < datas.size(); i++) {
			bdatas[i] = datas.get(i);
		}
		return bdatas;
	}
}
