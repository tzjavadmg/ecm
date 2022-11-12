package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.ActivityScanQrcodeDetail;

public interface ActivityScanQrcodeDetailMapper {

    int insert(ActivityScanQrcodeDetail record);

    int insertSelective(ActivityScanQrcodeDetail record);

    ActivityScanQrcodeDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActivityScanQrcodeDetail record);

    int updateByPrimaryKey(ActivityScanQrcodeDetail record);
}