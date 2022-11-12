package com.milisong.delay.redisevent;


import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisMessageListenerContainerConfig {

	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Autowired
	private TopicMessageListener messageListener;
	
	@Value("${redisexpire.topic}")
	private String topic;
	

	@Bean
	public RedisMessageListenerContainer configRedisMessageListenerContainer(Executor executor){
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		// 设置Redis的连接工厂
		container.setConnectionFactory(redisTemplate.getConnectionFactory());
		// 设置监听使用的线程池
		container.setTaskExecutor(executor);
		// 设置监听的Topic
		ChannelTopic channelTopic = new ChannelTopic(topic);
		// 设置监听器
		container.addMessageListener(messageListener, channelTopic);
		return container;

	}
}
