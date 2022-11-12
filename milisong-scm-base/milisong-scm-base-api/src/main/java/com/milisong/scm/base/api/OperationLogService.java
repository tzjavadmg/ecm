package com.milisong.scm.base.api;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.farmland.core.api.ResponseResult;
import com.milisong.scm.base.dto.OperationLogDto;
import com.milisong.scm.base.dto.OperationLogQueryDto;
import com.milisong.scm.base.dto.SimpleOperationLogDto;

/**
 * 操作日志服务类
 * @author yangzhilong
 *
 */
@FeignClient("milisong-scm-base-service")
public interface OperationLogService {
	/**
	 * 获取基本的操作日志记录
	 * @param modular 模块
	 * @param bussinessId 业务标识id
	 * @return
	 */
	@PostMapping("/base/log/query-simple-list")
	ResponseResult<List<SimpleOperationLogDto>> querySimpleOperationLogList(@RequestParam("modular")String modular, @RequestParam("bussinessId")String bussinessId);
	
	/**
	 * 根据业务id数组获取基本的操作日志
	 * @param dto
	 * @return
	 */
	@PostMapping("/base/log/query-simple-list-by-ids")
	ResponseResult<List<SimpleOperationLogDto>> querySimpleOperationLogListByIds(@RequestBody OperationLogQueryDto dto);
	
	/**
	 * 获取完整的操作日志记录
	 * @param modular 模块
	 * @param bussinessId 业务标识id
	 * @return
	 */
	@PostMapping("/base/log/query-list")
	ResponseResult<List<OperationLogDto>> queryOperationLogList(@RequestParam("modular")String modular, @RequestParam("bussinessId")String bussinessId);
	
	/**
	 * 发送操作日志MQ
	 * @param mqDto
	 * @return
	 */
	@PostMapping("/base/log/send-mq")
	ResponseResult<Object> sendMq(@RequestBody com.milisong.scm.base.dto.mq.OperationLogDto mqDto);
}

