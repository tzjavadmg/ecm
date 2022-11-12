package com.milisong.breakfast.scm.orderset.task;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.milisong.breakfast.scm.order.api.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.util.BeanMapper;
import com.milisong.breakfast.scm.common.util.DateUtil;
import com.milisong.breakfast.scm.configuration.api.ConfigService;
import com.milisong.breakfast.scm.configuration.dto.ShopInterceptConfigDto;
import com.milisong.breakfast.scm.orderset.api.OrderSetService;
import com.milisong.breakfast.scm.orderset.service.OrderSetProcessService;
import com.milisong.scm.base.api.ShopService;
import com.milisong.scm.base.dto.ShopDto;

import lombok.extern.slf4j.Slf4j;

/**
 * 集单处理的定时任务
 * 
 * @author yangzhilong
 *
 */
@Slf4j
@RestController
@RequestMapping("/orderset")
public class OrderSetProcessTask {
	// 线程池
	private static ExecutorService pool = null;
	// 线程池最大大小
	private static final int MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors();
	
	@Autowired
	private OrderSetService orderSetService;
	@Autowired
	private OrderSetProcessService orderSetProcessService;
	@Autowired(required=false)
	private ShopService shopService;
	@Autowired
	private ConfigService configService;
	
	@GetMapping("/task")
	public void run() {
		ResponseResult<List<ShopDto>> shopResp = shopService.getShopList();
		if(null==shopResp || !shopResp.isSuccess()) {
			log.error("查询门店列表失败：{}", JSONObject.toJSONString(shopResp));
			throw new RuntimeException("查询门店列表失败");
		}
		
		// 查询所有的门店
		List<ShopDto> shopList = shopResp.getData();
		if(CollectionUtils.isEmpty(shopList)) {
			log.warn("查询有效的门店list时为空");
			return;
		}
		
		// 计算实际需要的线程池大小
		int poolSize = (shopList.size()>MAX_POOL_SIZE) ? MAX_POOL_SIZE : shopList.size();
		
		// 计算时间
		Date nowTime = DateUtil.getNowDateTime();
		if(null == nowTime) {
			return;
		}
		
		// 循序门店->判断是否是否到截单时间
		shopList.stream().forEach(item -> {
			// 取门店的截单配置
			List<ShopInterceptConfigDto> configList = null;
			try {
				configList = configService.queryInterceptConfigByShopId(item.getId());
			} catch (Exception e2) {
				log.error("获取截单配置异常", e2);
				return;
			}
			
			// 如果门店配置为空则忽略
			if(CollectionUtils.isEmpty(configList)) {
				log.error("门店：{}未查询截单配置信息", item.getId());
				return;
			}
			
			configList.stream().forEach(config -> {
				boolean run = false;
				if(calculationCanStart(nowTime, config.getFirstOrdersetTime())) {
					log.info("开始执行第一次集单，shopId：{}", item.getId());
					run = true;
				}
				if(run) {
					// 到了截单时间则调用service的方法去执行集单逻辑
					if(null==pool || pool.isShutdown() || pool.isTerminated()) {
						pool = Executors.newFixedThreadPool(poolSize);
					}
					pool.submit(new Runnable() {
						@Override
						public void run() {
							try {
								// 执行集单
								orderSetProcessService.executeShopSet(item.getId(), BeanMapper.map(config, ShopInterceptConfigDto.class));
								
								// 发门店生产MQ
								orderSetService.sendOrderSetMq(item.getId(), false);
							} catch (Exception e) {
								log.error("执行门店" + item.getId() + "的集单操作时错误", e);
							}
						}
					});
					
				}
			});
		});
		
	}

	@GetMapping("/push")
	public void push() {
		ResponseResult<List<ShopDto>> shopResp = shopService.getShopList();
		if(null==shopResp || !shopResp.isSuccess()) {
			log.error("查询门店列表失败：{}", JSONObject.toJSONString(shopResp));
			throw new RuntimeException("查询门店列表失败");
		}

		// 查询所有的门店
		List<ShopDto> shopList = shopResp.getData();
		if(CollectionUtils.isEmpty(shopList)) {
			log.warn("查询有效的门店list时为空");
			return;
		}

		// 计算实际需要的线程池大小
		int poolSize = (shopList.size()>MAX_POOL_SIZE) ? MAX_POOL_SIZE : shopList.size();

		// 计算时间
		Date nowTime = DateUtil.getNowDateTime();
		if(null == nowTime) {
			return;
		}

		// 循序门店->判断是否是否到派单时间
		shopList.stream().forEach(item -> {
			// 取门店的截单配置
			List<ShopInterceptConfigDto> configList = null;
			try {
				configList = configService.queryInterceptConfigByShopId(item.getId());
			} catch (Exception e2) {
				log.error("获取派单配置异常", e2);
				return;
			}

			// 如果门店配置为空则忽略
			if(CollectionUtils.isEmpty(configList)) {
				log.error("门店：{}未查询派单配置信息", item.getId());
				return;
			}

			configList.stream().forEach(config -> {
				boolean run = false;
				if(calculationCanStart(nowTime, config.getDispatchTime())) {
					log.info("开始执行派单，shopId：{}", item.getId());
					run = true;
				}
				if(run) {
					// 到了截单时间则调用service的方法去执行集单逻辑
					if(null==pool || pool.isShutdown() || pool.isTerminated()) {
						pool = Executors.newFixedThreadPool(poolSize);
					}
					pool.submit(new Runnable() {
						@Override
						public void run() {
							try {
								orderSetService.pushSfOrderSetMq(BeanMapper.map(config, ShopInterceptConfigDto.class));
							} catch (Exception e) {
								log.error("执行门店" + item.getId() + "的派单操作时错误", e);
							}
						}
					});

				}
			});
		});

	}
	
	/**
	 * 把线程池停掉,释放资源
	 */
	@GetMapping("/destroy-pool")
	public void destroyPool() {
		if(null!=pool) {
			pool.shutdown();
		}
	}
	
	/**
	 * 门店生产的MQ补偿
	 * @param shopId
	 * @return
	 */
	@GetMapping("/compensate-send-mq")
	public ResponseResult<String> compensateSendMq(@RequestParam(name="shopId",required=false)Long shopId) {
		log.info("补偿门店生产的集单，shopId：{}", shopId);
		if(null != shopId) {
			orderSetService.sendOrderSetMq(shopId, true);
		} else {
			ResponseResult<List<ShopDto>> shopResp = shopService.getShopList();
			if(null==shopResp || !shopResp.isSuccess()) {
				log.error("查询门店列表失败：{}", JSONObject.toJSONString(shopResp));
				throw new RuntimeException("查询门店列表失败");
			}
			
			// 查询所有的门店
			List<ShopDto> shopList = shopResp.getData();
			if(!CollectionUtils.isEmpty(shopList)) {
				shopList.stream().forEach(item -> {
					log.info("开始补偿门店：{}", item.getId());
					orderSetService.sendOrderSetMq(item.getId(), true);
				});
			}
		}
		return ResponseResult.buildSuccessResponse();
	}
	
	/**
	 * 计算时间是否可以进行集单操作了
	 * @param finishTime
	 * @return
	 */
	private boolean calculationCanStart(Date nowTime, Date finishTime) {
		if (finishTime == null) {
			return false;
		}
		if(nowTime.compareTo(finishTime) == 0) {
			return true;
		}
		
		return false;
	}
}
