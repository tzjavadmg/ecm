package com.milisong.ecm.breakfast.web.wechat;

import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.breakfast.service.RestUserService;
import com.milisong.wechat.miniapp.dto.MiniContractDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author sailor wang
 * @date 2018/9/2 下午4:51
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/v1/miniapp/")
public class MiniAppRest {

    @Autowired
    private RestUserService restUserService;

    private final String CONTRACT_NOTIFY_SUCCESS = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";

    /**
     * 签约号是否有效
     * @return
     */
    @GetMapping("validate-contractno")
    ResponseResult<MiniContractDto> validateContractNo(@RequestParam("contractId") String contractNo){
        return restUserService.validateContractNo(contractNo);
    }

    /**
     * 签约回调
     * @param request
     * @param response
     */
    @RequestMapping(value = "contract-callback")
    public void contractCallBack(HttpServletRequest request, HttpServletResponse response) {
        try {
            log.info("签约回调 ------> ");
            restUserService.contractCallBack(request);
            //返回成功
            response.getWriter().write(CONTRACT_NOTIFY_SUCCESS);
        } catch (IOException e) {
            log.error("",e);
        }
    }

    /**
     * 解约回调
     * @param request
     * @param response
     */
    @RequestMapping(value = "cancel-contract")
    public void cancelContract(HttpServletRequest request, HttpServletResponse response) {
        try {
            restUserService.cancelContract(request);
            //返回成功
            response.getWriter().write(CONTRACT_NOTIFY_SUCCESS);
        } catch (IOException e) {
            log.error("",e);
        }
    }

    /**
     * 代扣签约
     * @return
     */
    @GetMapping(value = "sign-contract")
    public ResponseResult<Map<String, Object>> signContract() {
        return restUserService.signContract();
    }

    /**
     * 获取access_token
     * @return
     */
    @GetMapping(value = "query-accesstoken")
    public ResponseResult<String> queryAccessToken(){
        return restUserService.queryAccessToken();
    }


}