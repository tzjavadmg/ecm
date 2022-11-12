package com.milisong.scm.base.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.farmland.core.api.ResponseResult;
import com.farmland.core.util.BeanMapper;
import com.milisong.scm.base.api.OperationLogService;
import com.milisong.scm.base.constant.ErrorEnum;
import com.milisong.scm.base.domain.OperationLog;
import com.milisong.scm.base.domain.OperationLogExample;
import com.milisong.scm.base.domain.OperationLogExample.Criteria;
import com.milisong.scm.base.dto.OperationLogDto;
import com.milisong.scm.base.dto.OperationLogQueryDto;
import com.milisong.scm.base.dto.SimpleOperationLogDto;
import com.milisong.scm.base.mapper.OperationLogExtMapper;
import com.milisong.scm.base.mapper.OperationLogMapper;
import com.milisong.scm.base.mq.MqProducerUtil;
import com.milisong.scm.base.result.CompleteOperationLogSqlResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class OperationLogServiceImpl implements OperationLogService {
	@Autowired
	private OperationLogMapper operationLogMapper;
	@Autowired
	private OperationLogExtMapper operationLogExtMapper;

	@Override
	public ResponseResult<List<SimpleOperationLogDto>> querySimpleOperationLogList(String modular, String bussinessId) {
		if (StringUtils.isBlank(modular) || StringUtils.isBlank(bussinessId)) {
			log.error("modular或bussinessId为空！！");
			return ResponseResult.buildFailResponse(ErrorEnum.PARAMETER_CHECK_FAIL.getCode(), ErrorEnum.PARAMETER_CHECK_FAIL.getDesc());
		}
		OperationLogExample example = new OperationLogExample();
		example.createCriteria().andModularEqualTo(modular).andBussinessIdEqualTo(bussinessId);
		example.setOrderByClause(" operation_time desc ");
		
		List<OperationLog> list = operationLogMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(list)) {
			return ResponseResult.buildSuccessResponse();
		}
		return ResponseResult.buildSuccessResponse(BeanMapper.mapList(list, SimpleOperationLogDto.class));
	}

	@Override
	public ResponseResult<List<SimpleOperationLogDto>> querySimpleOperationLogListByIds(@RequestBody OperationLogQueryDto dto) {
		if(CollectionUtils.isEmpty(dto.getBussinessIds())) {
			log.error("业务id的集合为空！！");
			return ResponseResult.buildFailResponse(ErrorEnum.PARAMETER_CHECK_FAIL.getCode(), ErrorEnum.PARAMETER_CHECK_FAIL.getDesc());
		}
		OperationLogExample example = new OperationLogExample();
		Criteria criteria = example.createCriteria().andBussinessIdIn(dto.getBussinessIds()).andModularEqualTo(dto.getModular());
		if(!StringUtils.isEmpty(dto.getOperationType())) {
			criteria.andOperationTypeEqualTo(dto.getOperationType());
		}
		example.setOrderByClause(" operation_time desc ");
		
		List<OperationLog> list = operationLogMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(list)) {
			return ResponseResult.buildSuccessResponse();
		}
		return ResponseResult.buildSuccessResponse(BeanMapper.mapList(list, SimpleOperationLogDto.class));
	}

	@Override
	public ResponseResult<List<OperationLogDto>> queryOperationLogList(String modular, String bussinessId) {
		if (StringUtils.isBlank(modular) || StringUtils.isBlank(bussinessId)) {
			log.error("modular或bussinessId为空！！");
			return ResponseResult.buildFailResponse(ErrorEnum.PARAMETER_CHECK_FAIL.getCode(), ErrorEnum.PARAMETER_CHECK_FAIL.getDesc());
		}
		OperationLog log = new OperationLog();
		log.setModular(modular);
		log.setBussinessId(bussinessId);
		List<CompleteOperationLogSqlResult> list = operationLogExtMapper.listCompleteOperationLog(log);
		if(CollectionUtils.isEmpty(list)) {
			return ResponseResult.buildSuccessResponse();
		}
		return ResponseResult.buildSuccessResponse(BeanMapper.mapList(list, OperationLogDto.class));
	}

	@Override
	public ResponseResult<Object> sendMq(@RequestBody com.milisong.scm.base.dto.mq.OperationLogDto mqDto) {
		MqProducerUtil.sendOperationLog(mqDto);
		return ResponseResult.buildSuccessResponse();
	}

}
