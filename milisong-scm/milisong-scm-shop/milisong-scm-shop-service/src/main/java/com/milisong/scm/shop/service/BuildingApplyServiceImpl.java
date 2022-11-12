package com.milisong.scm.shop.service;

import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.milisong.scm.shop.api.BuildingApplyService;
import com.milisong.scm.shop.domain.BuildingApply;
import com.milisong.scm.shop.dto.building.BuildingApplyDto;
import com.milisong.scm.shop.mapper.BuildingApplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildingApplyServiceImpl implements BuildingApplyService {
    @Autowired
    private BuildingApplyMapper buildingApplyMapper;

    @Override
    public void save(BuildingApplyDto dto) {
        BuildingApply domain = BeanMapper.map(dto, BuildingApply.class);
        domain.setId(IdGenerator.nextId());
        this.buildingApplyMapper.insertSelective(domain);
    }

}
