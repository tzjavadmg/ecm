package com.milisong.scm.printer.api;

/**
*@author    created by benny
*@date  2018年10月26日---下午8:00:52
*
*/
public interface PosPrintService {

	void printOrdeSet(String msg,Integer printType);
	
	void printOrdeBreakfastSet(String msg,Integer printType);
}
