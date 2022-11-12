package com.milisong.delay.redisson;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class DelayJobService {

	@Autowired
	private RedissonClient redissonClient;

	/**
	 * 提交需要过期的消息
	 * @param job 传输对象
	 * @param delay 过期时间
	 * @param timeUnit 过期时间单位
	 */
	public void submitJob(DelayJobDto job, Long delay, TimeUnit timeUnit) {
		log.info("提交任务到延迟队列={},时间={},时间单位={}",JSON.toJSONString(job),delay,timeUnit);
		RBlockingQueue<DelayJobDto> blockingQueue = redissonClient.getBlockingQueue(TimerJob.jobsTag);
		RDelayedQueue<DelayJobDto> delayedQueue = redissonClient.getDelayedQueue(blockingQueue);
		//表示如果可能的话,将anObject加到BlockingQueue里,即如果BlockingQueue可以容纳,则返回true,否则返回false.
		delayedQueue.offer(job, delay, timeUnit);

	}

}
