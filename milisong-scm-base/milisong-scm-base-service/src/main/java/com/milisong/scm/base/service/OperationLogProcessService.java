package com.milisong.scm.base.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.milisong.scm.base.domain.OperationLog;
import com.milisong.scm.base.domain.OperationLogDetail;
import com.milisong.scm.base.dto.mq.OperationLogDto;
import com.milisong.scm.base.mapper.OperationLogDetailMapper;
import com.milisong.scm.base.mapper.OperationLogMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 操作日志处理服务类
 * 
 * @author yangzhilong
 *
 */
@RestController
@Slf4j
public class OperationLogProcessService {
	@Autowired
	private OperationLogMapper operationLogMapper;
	@Autowired
	private OperationLogDetailMapper operationLogDetailMapper;


	/**
	 * 保存日志
	 * @param value
	 */
	@Transactional
	public void saveLog(String value) {
		OperationLogDto opLog = JSONObject.parseObject(value, OperationLogDto.class);
		OperationLog dbLog = new OperationLog();
		BeanMapper.copy(opLog, dbLog);
		dbLog.setId(IdGenerator.nextId());
		operationLogMapper.insertSelective(dbLog);

		if (StringUtils.isNotBlank(opLog.getBeforeData()) || StringUtils.isNotBlank(opLog.getAfterData())) {
			OperationLogDetail detail = new OperationLogDetail();
			detail.setId(IdGenerator.nextId());
			detail.setAfterData(opLog.getAfterData());
			detail.setBeforeData(opLog.getBeforeData());
			detail.setLogId(dbLog.getId());
			operationLogDetailMapper.insertSelective(detail);
		}
		log.info("成功保存了一条操作日志,modular:{},operationType:{},operationTime:{}", opLog.getModular(),
				opLog.getOperationType(), opLog.getOperationTime());
	}
}
