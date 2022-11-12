package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.MarketingQrcode;
import com.milisong.pms.prom.dto.MarketingQrcodeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MarketingQrcodeMapper{

    /**
     *
     * @mbg.generated 2018-10-26
     */
    int insert(MarketingQrcode record);

    /**
     *
     * @mbg.generated 2018-10-26
     */
    int insertSelective(MarketingQrcode record);

    /**
     *
     * @mbg.generated 2018-10-26
     */
    MarketingQrcode selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-10-26
     */
    int updateByPrimaryKeySelective(MarketingQrcode record);

    /**
     *
     * @mbg.generated 2018-10-26
     */
    int updateByPrimaryKey(MarketingQrcode record);

    List<MarketingQrcodeDto> qrcodeList(@Param("qrcode") MarketingQrcodeDto marketingQrcode);

    MarketingQrcodeDto getDetailByCode(@Param("qrcode") String qrcode);
}