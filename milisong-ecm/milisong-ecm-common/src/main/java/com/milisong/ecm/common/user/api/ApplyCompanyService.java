package com.milisong.ecm.common.user.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.common.user.domain.ApplyCompany;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/1/18   14:05
 *    desc    : 用户申请开通公司或楼宇业务接口
 *    version : v1.0
 * </pre>
 */

public interface ApplyCompanyService {

    /**
     * 保存用户申请开通信息
     * @param dto
     * @return
     */
    ResponseResult<Boolean> save(@RequestBody ApplyCompany dto);
}
