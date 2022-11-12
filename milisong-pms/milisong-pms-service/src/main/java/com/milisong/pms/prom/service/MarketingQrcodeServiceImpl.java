package com.milisong.pms.prom.service;

import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.milisong.pms.prom.api.MarketingQrcodeService;
import com.milisong.pms.prom.domain.MarketingQrcode;
import com.milisong.pms.prom.dto.MarketingQrcodeDto;
import com.milisong.pms.prom.enums.QrcodeType;
import com.milisong.pms.prom.mapper.MarketingQrcodeMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.milisong.pms.prom.enums.PromotionResponseCode.REQUEST_PARAM_EMPTY;

/**
 * @author sailor wang
 * @date 2018/10/26 1:49 PM
 * @description
 */
@Slf4j
@Service
@RestController
public class MarketingQrcodeServiceImpl implements MarketingQrcodeService {

    private static final String MARKETING_QRCODE_COUNT = "marketing_qrcode_count:%s";

    @Autowired
    MarketingQrcodeMapper marketingQrcodeMapper;

    @Override
    @PostMapping(value = "/v1/MarketingQrcodeService/createQrcode")
    public ResponseResult<Boolean> createQrcode(@RequestBody MarketingQrcodeDto marketingQrcode) {
        log.info("---- createQrcode -> {}",marketingQrcode);
        if (marketingQrcode == null || marketingQrcode.getType() == null || StringUtils.isBlank(marketingQrcode.getName()) ||
                StringUtils.isEmpty(marketingQrcode.getUserName()) || marketingQrcode.getBusinessLine() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        String code = "";
        String dateStr = new DateTime().toString("yyMMdd");
        String key = String.format(MARKETING_QRCODE_COUNT,dateStr);
        try {
            Long count = RedisCache.incrBy(key, 1);
            String countFlag = dateStr+count;
            code = QrcodeType.geneQrcode(marketingQrcode.getType(), countFlag);
        } finally {
            RedisCache.expire(key, 1, TimeUnit.DAYS);
        }

        log.info("qrcode -> {}",code);
        MarketingQrcode qrcode = BeanMapper.map(marketingQrcode, MarketingQrcode.class);
        qrcode.setQrcode(code);
        qrcode.setId(IdGenerator.nextId());
        qrcode.setIsDeleted(false);
        int rst = marketingQrcodeMapper.insertSelective(qrcode);
        return ResponseResult.buildSuccessResponse(rst > 0);
    }

    @Override
    @PostMapping(value = "/v1/MarketingQrcodeService/updateQrcode")
    public ResponseResult<Boolean> updateQrcode(@RequestBody MarketingQrcodeDto marketingQrcode) {
        log.info("---- updateQrcode -> {}",marketingQrcode);
        MarketingQrcode qrcode = BeanMapper.map(marketingQrcode,MarketingQrcode.class);
        int rst = marketingQrcodeMapper.updateByPrimaryKeySelective(qrcode);
        return ResponseResult.buildSuccessResponse(rst > 0);
    }

    @Override
    @PostMapping(value = "/v1/MarketingQrcodeService/getQrcode")
    public ResponseResult<MarketingQrcodeDto> getQrcode(@RequestParam("id") Long id) {
        log.info("id -> {}",id);
        MarketingQrcode qrcode = marketingQrcodeMapper.selectByPrimaryKey(id);
        if (qrcode != null){
            MarketingQrcodeDto qrcodeDto = BeanMapper.map(qrcode,MarketingQrcodeDto.class);
            return ResponseResult.buildSuccessResponse(qrcodeDto);
        }
        return ResponseResult.buildFailResponse();
    }

    @Override
    @PostMapping(value = "/v1/MarketingQrcodeService/qrcodeList")
    public ResponseResult<List<MarketingQrcodeDto>> qrcodeList(@RequestBody MarketingQrcodeDto marketingQrcode) {
        Byte buzLine = marketingQrcode.getBusinessLine();
        log.info("marketingQrcode -> {}",marketingQrcode);
        List<MarketingQrcodeDto> list = marketingQrcodeMapper.qrcodeList(marketingQrcode);
        return ResponseResult.buildSuccessResponse(list);
    }

    @Override
    @PostMapping(value = "/v1/MarketingQrcodeService/deleteQrcode")
    public ResponseResult<Boolean> deleteQrcode(@RequestParam("id") Long id) {
        log.info("id -> {}",id);
        MarketingQrcode qrcode = new MarketingQrcode();
        qrcode.setId(id);
        qrcode.setIsDeleted(true);
        int rst = marketingQrcodeMapper.updateByPrimaryKeySelective(qrcode);
        return ResponseResult.buildSuccessResponse(rst > 0);
    }

    @Override
    @PostMapping(value = "/v1/MarketingQrcodeService/queryQrcode")
    public ResponseResult<MarketingQrcodeDto> queryQrcode(@RequestParam("id") Long id) {
        log.info("id -> {}",id);
        MarketingQrcode marketingQrcode = marketingQrcodeMapper.selectByPrimaryKey(id);
        if (marketingQrcode != null) {
            return ResponseResult.buildSuccessResponse(BeanMapper.map(marketingQrcode, MarketingQrcodeDto.class));
        }
        return ResponseResult.buildSuccessResponse();
    }

    /**
     * 根据code查询二维码详情
     *
     * @param qrcode
     * @return
     */
    @Override
    public ResponseResult<MarketingQrcodeDto> getDetailByCode(@RequestParam("qrcode") String qrcode) {
        if (StringUtils.isBlank(qrcode)) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        MarketingQrcodeDto marketingQrcode = marketingQrcodeMapper.getDetailByCode(qrcode.trim());
        return ResponseResult.buildSuccessResponse(marketingQrcode);
    }
}