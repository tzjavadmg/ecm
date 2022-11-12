package com.milisong.scm.base.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farmland.core.api.ResponseResult;
import com.milisong.scm.base.api.OperationLogService;
import com.milisong.scm.base.dto.OperationLogDto;
import com.milisong.scm.base.dto.SimpleOperationLogDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "操作日志")
@RestController
@RequestMapping("/operation-log")
public class OperationLogRest {
	@Autowired
	private OperationLogService operationLogService;
	
	/**
	 * 查询基本的操作日志（不分页）
	 * @param modular
	 * @param bussinessId
	 * @return
	 */
	@ApiOperation("查询基本的操作日志")
	@GetMapping("/simple")
	public ResponseResult<List<SimpleOperationLogDto>> simple(String modular, String bussinessId) {
		return this.operationLogService.querySimpleOperationLogList(modular, bussinessId);
	}
	
	/**
	 * 查询完整的操作日志（不分页）
	 * @param modular
	 * @param bussinessId
	 * @return
	 */
	@ApiOperation("查询完整的操作日志")
	@GetMapping("/complete")
	public ResponseResult<List<OperationLogDto>> complete(String modular, String bussinessId) {
		return this.operationLogService.queryOperationLogList(modular, bussinessId);
	}
}
