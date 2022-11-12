package com.milisong.delay.redisson;

/**
 * 所有业务实现该接口
 * @author songmulin
 *
 */
public interface ExecuteJob {
	
	void execute(DelayJobDto job);
}
