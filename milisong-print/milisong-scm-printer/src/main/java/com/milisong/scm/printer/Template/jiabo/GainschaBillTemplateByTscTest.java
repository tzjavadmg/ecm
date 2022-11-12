package com.milisong.scm.printer.Template.jiabo;

import java.util.Vector;

import com.gp.command.EscCommand.ENABLE;
import com.gp.command.LabelCommand;
import com.gp.command.LabelCommand.BITMAP_MODE;
import com.gp.command.LabelCommand.DIRECTION;
import com.gp.command.LabelCommand.EEC;
import com.gp.command.LabelCommand.FONTMUL;
import com.gp.command.LabelCommand.FONTTYPE;
import com.gp.command.LabelCommand.MIRROR;
import com.gp.command.LabelCommand.READABEL;
import com.gp.command.LabelCommand.ROTATION;
import com.gp.command.LabelCommand.TSCBARCODETYPE;
import com.gp.command.LabelCommand.TSCSPEED;

/**
*@author    created by benny
*@date  2018年10月22日---下午7:08:44
*
*/
public class GainschaBillTemplateByTscTest {

	public byte[] setOrderTempLab() {
		LabelCommand tsc = new LabelCommand();
		tsc.addSize(80, 90); // 设置标签尺寸，按照实际尺寸设置
		tsc.addGap(0); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
		tsc.addDirection(DIRECTION.BACKWARD, MIRROR.NORMAL);// 设置打印方向
		tsc.addReference(0, 0);// 设置原点坐标
		tsc.addTear(ENABLE.ON); // 撕纸模式开启
		tsc.addCls();// 清除打印缓冲区
		// 绘制简体中文
//		tsc.addText(20, 30, FONTTYPE.KOREAN, ROTATION.ROTATION_0,
//				FONTMUL.MUL_1, FONTMUL.MUL_1, "조선말");
		tsc.addText(40, 30, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1, "简体字");
//		tsc.addText(60, 30, FONTTYPE.TRADITIONAL_CHINESE,
//				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1, "繁體字");
		// 绘制二维码
//		tsc.addQRCode(65, 75, EEC.LEVEL_L, 5, ROTATION.ROTATION_0,
//				" www.smarnet.cc");
		// 绘制一维条码
		tsc.add1DBarcode(50, 50, TSCBARCODETYPE.CODE128, 100,
				READABEL.EANBEL, ROTATION.ROTATION_0, "SMARNET");
		tsc.addPrint(1); // 打印标签
		tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
		tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
		
		return getMessage(tsc);
	}
	
	
	public byte[] printTest() {
		LabelCommand label = new LabelCommand();
		label.addSize(80, 90);
		label.addGap(2);
		label.addCls();
		label.addSpeed(TSCSPEED.SPEED4);
		label.addDirection(DIRECTION.BACKWARD, MIRROR.NORMAL);
		label.addReference(10,10);
		label.addText(260, 0, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_2, FONTMUL.MUL_2,
				"B001");
		label.addText(260, 50, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"(配送联)");
		label.addText(1, 70, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"_____________________________________________________");
		label.addText(130, 100, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"# 2018-11-11 配送订单");
		label.addText(1,130, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"_____________________________________________________");
		label.addText(130, 160, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_2, FONTMUL.MUL_2,
				"宏汇国际广场B座");
		label.addText(130,230, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"地址：中山西路1602号");
		label.addText(1, 260, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"_____________________________________________________");
		label.addText(130, 290, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_2, FONTMUL.MUL_2,
				"公司：11层/米立");
		label.addText(1, 330, FONTTYPE.SIMPLIFIED_CHINESE,
				ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"_____________________________________________________");
		for (int i = 1; i < 6; i++) {
			label.addText(1, 330+(i*35), FONTTYPE.SIMPLIFIED_CHINESE,
					ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
					"赵先生（131****1234）干锅啤酒鸭便当（A款）*1");
		}
		label.addPrint(1);
		label.addFeed(2);
		//label.addFormFeed();
		//label.addHome();
		return getMessage(label);
	}
	 
	 byte[] getMessage(LabelCommand esc) {
			Vector<Byte> datas = esc.getCommand();
			byte[] bdatas = new byte[datas.size()];
			for (int i = 0; i < datas.size(); i++) {
				bdatas[i] = datas.get(i);
			}
			return bdatas;
	}
	 
}
