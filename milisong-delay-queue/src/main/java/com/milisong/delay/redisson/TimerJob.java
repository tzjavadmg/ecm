package com.milisong.delay.redisson;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TimerJob {
	
	static final String jobsTag = "customer_jobtimer_jobs";
	
	@Autowired
	private RedissonClient redissonClient;
	
	@Autowired
	private ApplicationContext context;
	
    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    @PostConstruct
    public void startJobTimer() {
    	RBlockingQueue<DelayJobDto> blockingQueue = redissonClient.getBlockingQueue(jobsTag);
    	new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                    	//取走BlockingQueue里排在首位的对象,若BlockingQueue为空,阻断进入等待状态直到Blocking有新的对象被加入为止
                    	DelayJobDto job = blockingQueue.take();
                        executorService.execute(new ExecutorTask(context, job));
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            TimeUnit.SECONDS.sleep(60);
                        } catch (Exception ex) {
                        }
                    }
                }
            }
        }.start();
    }
    
    class ExecutorTask implements Runnable {

        private ApplicationContext context;

        private DelayJobDto delayJob;

        public ExecutorTask(ApplicationContext context, DelayJobDto delayJob) {
            this.context = context;
            this.delayJob = delayJob;
        }

        @Override
        public void run() {
        	log.info("消费延迟队列任务{}",JSON.toJSONString(delayJob));
            ExecuteJob service = (ExecuteJob) context.getBean(delayJob.getAClass());
            service.execute(delayJob);
        }
    }

}
