package com.milisong.scm.printer.log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

/**
*@author    created by benny
*@date  2018年11月6日---下午4:42:37
*
*/
public class LoggerAppender extends UnsynchronizedAppenderBase<LoggingEvent>{

 
	@Override
	protected void append(LoggingEvent eventObject) {
		String logUrl = context.getProperty("uploadLogUrl");
		String appName = context.getProperty("springAppName");
//		System.out.println(logUrl);
//		System.out.println(appName);
		Map<String, Object> log = new HashMap<String, Object>();
		String level = eventObject.getLevel().levelStr;
		StringBuilder builder = new StringBuilder();
		if("ERROR".equals(level)) {
			IThrowableProxy thProxy = eventObject.getThrowableProxy();
			while (thProxy != null) {
				builder.append(thProxy.getClassName() + ": " + thProxy.getMessage() );
				builder.append(CoreConstants.LINE_SEPARATOR);
				
		        for (StackTraceElementProxy step : eventObject.getThrowableProxy().getStackTraceElementProxyArray()) {
		            String string = step.toString();
		            builder.append(CoreConstants.TAB).append(string);
		            ThrowableProxyUtil.subjoinPackagingData(builder, step);
		            builder.append(CoreConstants.LINE_SEPARATOR);
		        }
		         
		        thProxy = thProxy.getCause();
			}

		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		log.put("sysTime", simpleDateFormat.format(new Date(eventObject.getTimeStamp())));
		log.put("className", eventObject.getLoggerName());
		log.put("message", eventObject.getFormattedMessage());
		log.put("thrown", builder.toString());
		log.put("level", level);
		log.put("host", "localhost");
		log.put("appName", appName);
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		log.put("@timestamp", simpleDateFormat2.format(new Date(eventObject.getTimeStamp())));
		List<String> listParam = new ArrayList<String>();
		Map<String,Object> messageMap = new HashMap<String,Object>();
		listParam.add(JSON.toJSONString(log));
		messageMap.put("type", "milisong-print");
		messageMap.put("content",listParam);
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders requestHeaders = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");

		requestHeaders.setContentType(type);
		requestHeaders.add("Accept", MediaType.APPLICATION_JSON.toString());
		requestHeaders.add("Access-Token", "milisongmilisong");
		requestHeaders.add("Content-Type", "application/json");
		HttpEntity<String> formEntity = new HttpEntity<String>(JSON.toJSONString(messageMap), requestHeaders);
		try {
			restTemplate.postForObject(logUrl, formEntity, ResponseResult.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
	}

}
