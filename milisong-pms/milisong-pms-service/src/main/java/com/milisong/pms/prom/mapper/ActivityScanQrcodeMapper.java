package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.ActivityScanQrcode;
import com.milisong.pms.prom.dto.scanqrcode.ActivityScanQrcodeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ActivityScanQrcodeMapper {

    int insert(ActivityScanQrcode record);

    int insertSelective(ActivityScanQrcode record);

    ActivityScanQrcode selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActivityScanQrcode record);

    int updateByPrimaryKey(ActivityScanQrcode record);

    int qrcodeHasBindActivity(@Param("qrcode") String qrcode);

    ActivityScanQrcodeDto getScanQrcodeActivityDetialByQrcode(@Param("qrcode") String qrcode);
}