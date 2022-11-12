package com.milisong.ecm.breakfast.web.task;


import com.milisong.wechat.miniapp.api.MiniAppService;
import com.milisong.oms.constant.OrderType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信任务
 */
@Slf4j
@RestController
@RequestMapping("/v1/miniapp/task/")
public class MiniAppTaskRest {
    @Autowired
    MiniAppService miniAppService;

    /**
     * 每小时执行 小程序刷新accessToken
     */
    @GetMapping(value = "/fresh-accesstoken")
    private void refreshWechatMiniAccessTokenTask() {
        miniAppService.refreshAccessToken(OrderType.BREAKFAST.getValue());
    }

}
