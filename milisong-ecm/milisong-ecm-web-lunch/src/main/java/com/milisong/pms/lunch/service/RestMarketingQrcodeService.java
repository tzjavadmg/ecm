package com.milisong.pms.lunch.service;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.milisong.wechat.miniapp.api.MiniAppService;
import com.milisong.wechat.miniapp.dto.MiniQrcodeRequest;
import com.milisong.wechat.miniapp.dto.WxMaCodeLineColor;
import com.milisong.pms.lunch.enums.BusinessLine;
import com.milisong.pms.prom.api.MarketingQrcodeService;
import com.milisong.pms.prom.dto.MarketingQrcodeDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author sailor wang
 * @date 2018/11/7 4:41 PM
 * @description
 */
@Slf4j
@Service
public class RestMarketingQrcodeService {

    @Autowired
    MarketingQrcodeService marketingQrcodeService;

    @Autowired
    MiniAppService miniAppService;

    @Autowired
    RestTemplate restTemplate;

    @Value("${wechat.query-lunch-accesstoken}")
    String queryLunchAccesstokenUrl;


    public ResponseResult<List<MarketingQrcodeDto>> qrcodeList() {
        log.info("午餐获取二维码列表");
        MarketingQrcodeDto qrcodeDto = new MarketingQrcodeDto();
        qrcodeDto.setBusinessLine(BusinessLine.LUNCH.getCode());
        return marketingQrcodeService.qrcodeList(qrcodeDto);
    }

    public ResponseResult<Boolean> deleteQrcode(Long id) {
        log.info("午餐删除二维码列表 id -> {}",id);
        return marketingQrcodeService.deleteQrcode(id);
    }

    public ResponseEntity<byte[]> downloadQrcode(Long id) {
        try {
            log.info("下载二维码 id -> {}",id);
            ResponseResult<MarketingQrcodeDto> qrcodeResponseResult = marketingQrcodeService.queryQrcode(id);
            if (qrcodeResponseResult.isSuccess() && qrcodeResponseResult.getData() != null) {
                String scene = qrcodeResponseResult.getData().getQrcode();
                if (qrcodeResponseResult.getData().getCompanyId() != null) {
                    if (!scene.contains("&")) {
                        scene = "q=" + scene + "&c=" + qrcodeResponseResult.getData().getCompanyId();
                    }
                }

                int width = 430;
                MiniQrcodeRequest qrcodeRequest = MiniQrcodeRequest.builder().scene(scene).width(width).autoColor(false).lineColor(new WxMaCodeLineColor("0", "0", "0")).build();

                String accessToken = accesstoken();
                log.info("accesstoken -> {}", accessToken);
                if (StringUtils.isNotBlank(accessToken)) {
                    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                    HttpPost httpPost = new HttpPost("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken);  // 接口
                    httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
                    String body = JSON.toJSONString(qrcodeRequest.toMap());           //必须是json模式的 post
                    StringEntity entity = new StringEntity(body);
                    entity.setContentType("image/png");

                    httpPost.setEntity(entity);
                    HttpResponse response = httpClient.execute(httpPost);
                    InputStream inputStream = response.getEntity().getContent();

                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.add("Content-Disposition", "attachment;filename=" + new String((scene + ".png").getBytes()));
                    ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(inputStreamToByte(inputStream), httpHeaders, HttpStatus.OK);
                    return responseEntity;
                }
                return null;
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public static byte[] inputStreamToByte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inputStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

    public ResponseResult<Boolean> createOrUpdateQrcode(MarketingQrcodeDto marketingQrcode) {
        log.info("午餐编辑or修改二维码 marketingQrcode -> {}", marketingQrcode);
        marketingQrcode.setBusinessLine(BusinessLine.LUNCH.getCode());
        if (marketingQrcode != null && marketingQrcode.getId() == null) {
            return marketingQrcodeService.createQrcode(marketingQrcode);
        }
        return marketingQrcodeService.updateQrcode(marketingQrcode);
    }

    public ResponseResult<MarketingQrcodeDto> getQrcode(Long id) {
        return marketingQrcodeService.getQrcode(id);
    }

    private String accesstoken() {
        log.info("queryLunchAccesstokenUrl -> {}", queryLunchAccesstokenUrl);
        ResponseResult result = restTemplate.getForObject(queryLunchAccesstokenUrl, ResponseResult.class);
        return (String) result.getData();
    }
}