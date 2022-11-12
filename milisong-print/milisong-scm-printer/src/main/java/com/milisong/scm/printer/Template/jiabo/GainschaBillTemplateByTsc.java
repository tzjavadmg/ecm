package com.milisong.scm.printer.Template.jiabo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.gp.command.LabelCommand;
import com.gp.command.LabelCommand.DIRECTION;
import com.gp.command.LabelCommand.FONTMUL;
import com.gp.command.LabelCommand.FONTTYPE;
import com.gp.command.LabelCommand.MIRROR;
import com.gp.command.LabelCommand.ROTATION;
import com.milisong.scm.printer.dto.OrderDetailInfo;
import com.milisong.scm.printer.request.mq.OrderSetDetailGoodsDto;
import com.milisong.scm.printer.util.jiabo.StringUtils;

/**
*@author    created by benny
*@date  2018年10月23日---下午4:37:05
*
*/
@Component
public class GainschaBillTemplateByTsc {


 
	/**
	 * 打印后厨单据
	 * @param map
	 * @param deliveryDate
	 * @param setNo
	 * @return
	 */
	public byte[]  printProductionUnion(Map<String,Integer> map,String deliveryDate,String setNo) {
		LabelCommand label = new LabelCommand();
		label.addSize(80, 90);
		label.addGap(2);
		label.addCls();
		label.addDirection(DIRECTION.BACKWARD, MIRROR.NORMAL);
		label.addReference(10,10);
		label.addText(210, 30, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_2, FONTMUL.MUL_2,
				StringUtils.subStringSetNo(setNo));
		label.addText(180, 90, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"配送日期： "+deliveryDate);
		label.addText(1, 110, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"_____________________________________________________");
		int row = 130;
		for (String goodsName : map.keySet()) {
			row = row +40;
			label.addText(180, row, FONTTYPE.SIMPLIFIED_CHINESE,
					ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
					map.get(goodsName)+" * "+goodsName );
		}
		label.addPrint(1);
		label.addFeed(2);
		return getMessage(label);
	}
	/**
	 * 打印顾客联
	 * @param listCustomer
	 * @param map
	 * @param listPick 
	 * @param copiesNumber 
	 * @param i 
	 * @return
	 */
	public byte[] customerTemplate(List<OrderSetDetailGoodsDto> listCustomer, Map<String, Object> map, int i, int copiesNumber) {
		OrderSetDetailGoodsDto  orderSetDetailGoodsDto = listCustomer.get(0);
		
		LabelCommand label = new LabelCommand();
		label.addSize(80, 90);
		label.addGap(2);
		label.addCls();
		label.addDirection(DIRECTION.BACKWARD, MIRROR.NORMAL);
		label.addReference(10,10);
		label.addText(10, 85, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"第"+i+"/"+copiesNumber+"张");
		label.addText(260, 85, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_2, FONTMUL.MUL_2,
				orderSetDetailGoodsDto.getUserName());
		label.addText(80, 150, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				map.get("deliveryTime")+" 送达   手机尾号:("+StringUtils.convertMobileLas(orderSetDetailGoodsDto.getUserPhone())+")");
		label.addText(1, 170, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"_____________________________________________________");
		int row = 190;
		for (OrderSetDetailGoodsDto orderSetDetailGoodsDto2 : listCustomer) {
			row = row +30;
			label.addText(180, row, FONTTYPE.SIMPLIFIED_CHINESE,
					ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
					orderSetDetailGoodsDto2.getGoodsNumber()+" * "+orderSetDetailGoodsDto2.getGoodsName());
		}
//		//打印小菜
//		if(!CollectionUtils.isEmpty(listPick)) {
//			for (OrderSetDetailGoodsDto orderSetDetailGoodsDto2 : listPick) {
//				row = row +30;
//				label.addText(180, row, FONTTYPE.SIMPLIFIED_CHINESE,
//						ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
//						orderSetDetailGoodsDto2.getGoodsNumber()+" * "+orderSetDetailGoodsDto2.getGoodsName());
//			}
//		}

		label.addText(1, row+30, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"_____________________________________________________");
		label.addText(10, (row+90), FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_2, FONTMUL.MUL_2,
				"公司："+map.get("company").toString());
		label.addText(10, (row+150), FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_2, FONTMUL.MUL_2,
				"配送至："+map.get("mealAddress").toString());
		label.addText(10, row+200, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"地址："+map.get("detailDeliveryAddress").toString());
		label.addText(10, row+230, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"订单号："+orderSetDetailGoodsDto.getCoupletNo());
		label.addText(190, row+230, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"配送日期:"+map.get("deliveryDate").toString());
		label.addText(10, row+260, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"配送单号：");
		label.addText(160, row+260, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_2, FONTMUL.MUL_2,
				StringUtils.subStringSetNo(map.get("setNo").toString()));
		label.addPrint(1);
		label.addFeed(2);
		return getMessage(label);
	}
	
	/**
	 * 打印配送单
	 * @param map
	 * @return
	 */
	public byte[] setOrderTemplate(Map<String, Object> map) {
		LabelCommand label = new LabelCommand();
		label.addSize(80, 90);
		label.addGap(2);
		label.addCls();
		label.addDirection(DIRECTION.BACKWARD, MIRROR.NORMAL);
		label.addReference(10,10);
		int row = 95;
		label.addText(275, 105, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_2, FONTMUL.MUL_2,
				StringUtils.subStringSetNo(map.get("setNo").toString()));
		
		label.addText(215, 155, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				map.get("deliveryTime")+" 送达   ("+map.get("copiesNumber")+"份)");
		
		label.addText(1, 165, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"_____________________________________________________");
		label.addText(10,205, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_2, FONTMUL.MUL_2,
				"公司："+map.get("company").toString());
		label.addText(10, 265, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_2, FONTMUL.MUL_2,
				"配送至："+map.get("mealAddress").toString());
		label.addText(10, 300, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"_____________________________________________________");
		label.addText(10, 340, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"地址："+map.get("detailDeliveryAddress").toString());
		
		label.addText(10,370, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"配送日期: "+map.get("deliveryDate").toString()+"");
		label.addText(10, 380, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"_____________________________________________________");
		int i = 0;
		List<OrderDetailInfo> list = (List<OrderDetailInfo>) map.get("customerGoodsInfo");
		List<OrderDetailInfo> newlist = fmoatDateList(list);
		for (int k = 0;k<newlist.size();k++) {
			
			i++;
			if(k != newlist.size()) {
			label.addText(60, 390+(i*35), FONTTYPE.SIMPLIFIED_CHINESE,
					ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
					newlist.get(k).getName() );
			label.addText(160, 390+(i*35), FONTTYPE.SIMPLIFIED_CHINESE,
					ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
					"尾号");
			label.addText(200, 390+(i*35), FONTTYPE.SIMPLIFIED_CHINESE,
					ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
					"（"+StringUtils.convertMobileLas(newlist.get(k).getPhone())+"）");
			}
			k = k+1;
			if(k<newlist.size()) {
			
			label.addText(320, 390+(i*35), FONTTYPE.SIMPLIFIED_CHINESE,
					ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
					newlist.get(k).getName() );
			label.addText(420, 390+(i*35), FONTTYPE.SIMPLIFIED_CHINESE,
					ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
					"尾号");
			label.addText(460, 390+(i*35), FONTTYPE.SIMPLIFIED_CHINESE,
					ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
					"（"+StringUtils.convertMobileLas(newlist.get(k).getPhone())+"）");
			}else {
				break;
			}
		}
		label.addPrint(1);
		label.addFeed(2);
		return getMessage(label);
	}
	
	 byte[] getMessage(LabelCommand tsc) {
			Vector<Byte> datas = tsc.getCommand();
			byte[] bdatas = new byte[datas.size()];
			for (int i = 0; i < datas.size(); i++) {
				bdatas[i] = datas.get(i);
			}
			return bdatas;
	}
	 
	 private List<OrderDetailInfo> fmoatDateList(List<OrderDetailInfo> list){
			Map<String,OrderDetailInfo> map = new HashMap<String,OrderDetailInfo>();
			for (OrderDetailInfo orderDetailInfo : list) {
				String key = orderDetailInfo.getName()+orderDetailInfo.getPhone();
				map.put(key, orderDetailInfo);
			}
			list.clear();
			list.addAll(map.values());
			return list;
		}
}
