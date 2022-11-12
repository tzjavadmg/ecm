package com.mili.oss.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.util.BeanMapper;
import com.mili.oss.api.ConfigService;
import com.mili.oss.api.OrderInsideService;
import com.mili.oss.api.OrderService;
import com.mili.oss.constant.OrderTypeEnum;
import com.mili.oss.dto.config.InterceptConfigDto;
import com.mili.oss.dto.config.ShopInterceptConfigDto;
import com.mili.oss.mq.RedisMqDto;
import com.mili.oss.util.DateUtil;
import com.mili.oss.util.RedisConfigUtil;
import com.milisong.scm.base.api.ShopService;
import com.milisong.scm.base.dto.ShopDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 订单处理的定时任务
 *
 * @author yangzhilong
 */
@Slf4j
@RestController
@RequestMapping("/task/order")
public class OrderProcessTask {

    @Autowired
	private OrderInsideService orderInsideService;

	/**
	 * 发送缺货通知短信
	 */
	@GetMapping("/send-out-of-stock-msg")
	public void sendOutOfStockMsg(){
		orderInsideService.sendOutOfStockMsgTask();
	}

}
