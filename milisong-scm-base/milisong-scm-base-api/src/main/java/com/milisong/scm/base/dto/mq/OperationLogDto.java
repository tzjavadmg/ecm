package com.milisong.scm.base.dto.mq;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 操作日志的mq dto类
 * @author yangzhilong
 *
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationLogDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7575991273721518124L;

	private Long id;

    private String modular;

    private String bussinessId;

    private String operationType;

    private String operationUser;

    private Date operationTime;

    private String operationResume;

    private String diffData;
    
    private String beforeData;

    private String afterData;
}
