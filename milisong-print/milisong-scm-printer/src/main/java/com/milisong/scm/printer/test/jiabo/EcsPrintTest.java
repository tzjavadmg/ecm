package com.milisong.scm.printer.test.jiabo;

import java.util.Vector;

import com.gp.command.EscCommand;
import com.gp.command.EscCommand.JUSTIFICATION;
import com.gp.port.Drive;

/**
*@author    created by benny
*@date  2018年9月7日---上午11:38:15
*
*/
public class EcsPrintTest {
	private static Drive dr = null;
	
	public static void main(String[] args) {
		Drive dr = InitPrint();
		byte[] bdatas = testPrint();
		byte[] qrCode = qrCodeTest();
		dr.sendMessage(bdatas);
		dr.sendMessage(qrCode);
	}
	
	static Drive InitPrint() {
		dr = new Drive();
		boolean b = dr.opendrive("GP-H80300 Series");
		if (b)
			System.out.println("ok");
			else {
				System.out.println("false");
			}
		return dr;
	}
	
	static byte[] testPrint() {
		EscCommand esc = new EscCommand();
		esc.addText("打印测试:换行");
		esc.addPrintAndLineFeed();
		esc.addText("打印测试:走纸30点");
		esc.addPrintAndFeedPaper((byte) 30);
		esc.addText("打印测试:走纸3行");
		esc.addPrintAndFeedLines((byte) 3);
		esc.addHorTab();
		esc.addText("打印测试:水平定位\n");
		esc.addSetAbsolutePrintPosition((byte) 30);
		esc.addText("打印测试:绝对位置");
		esc.addSetRelativePrintPositon((byte) 600);
		esc.addText("打印测试:相对位置");
		esc.addPrintAndLineFeed();
		esc.addSelectJustification(JUSTIFICATION.RIGHT);
		esc.addText("打印测试:右对齐\n");
		esc.addSelectJustification(JUSTIFICATION.CENTER);
		esc.addText("打印测试:居中\n");
		esc.addSelectJustification(JUSTIFICATION.LEFT);
		esc.addText("打印测试:左对齐\n");
		esc.addSetLeftMargin((byte) 30);
		esc.addSelectJustification(JUSTIFICATION.RIGHT);
		esc.addText("左边距30右对齐\n");
		esc.addSelectJustification(JUSTIFICATION.CENTER);
		esc.addText("左边距30居中\n");
		esc.addSelectJustification(JUSTIFICATION.LEFT);
		esc.addText("左边距30左对齐\n");
		Vector<Byte> datas = esc.getCommand();
		byte[] bdatas = new byte[datas.size()];
		for (int i = 0; i < datas.size(); i++) {
			bdatas[i] = datas.get(i);
		}
		return bdatas;
	}
	static byte[] qrCodeTest() {
		EscCommand esc = new EscCommand();
		esc.addText("Print QRcode\n"); // 打印文字
		esc.addSelectErrorCorrectionLevelForQRCode((byte) 0x31); // 设置纠错等级
		esc.addSelectSizeOfModuleForQRCode((byte) 6);// 设置qrcode模块大小
		esc.addStoreQRCodeData("http://bosstest.jshuii.com");// 设置qrcode内容
		esc.addText("\n"); // 打印文字
		esc.addText("\n"); // 打印文字
		
		esc.addPrintQRCode();// 打印QRCode
		esc.addPrintAndLineFeed();
		esc.addCutPaper();
		Vector<Byte> datas = esc.getCommand();
		byte[] bdatas = new byte[datas.size()];
		for (int i = 0; i < datas.size(); i++) {
			bdatas[i] = datas.get(i);
		}
		return bdatas;
	}
}


