package com.milisong.ecm.common.notify.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.common.notify.dto.SmsSendDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/9/21   18:33
 *    desc    : 短信服务
 *    version : v1.0
 * </pre>
 */
public interface SmsService {

	@PostMapping(value = "/v1/SmsService/sendMsg")
    ResponseResult<String> sendMsg(@RequestBody SmsSendDto dto);

}
