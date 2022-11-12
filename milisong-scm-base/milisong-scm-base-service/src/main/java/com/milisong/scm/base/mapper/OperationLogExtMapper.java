package com.milisong.scm.base.mapper;

import java.util.List;

import com.milisong.scm.base.domain.OperationLog;
import com.milisong.scm.base.result.CompleteOperationLogSqlResult;

/**
 * 操作日志扩展mapper
 * @author yangzhilong
 *
 */
public interface OperationLogExtMapper {
	
	/**
	 * 查询完整的操作日志
	 * @param log
	 * @return
	 */
	List<CompleteOperationLogSqlResult> listCompleteOperationLog(OperationLog log);
}
