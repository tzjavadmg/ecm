package com.milisong.delay.service;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmland.core.db.IdGenerator;
import com.milisong.delay.domain.DelayConsumeEvent;
import com.milisong.delay.domain.DelayProductionEvent;
import com.milisong.delay.dto.MessageDto;
import com.milisong.delay.mapper.DelayConsumeEventMapper;
import com.milisong.delay.mapper.DelayProductionEventMapper;

@Service
@Transactional
public class DataService {
	@Autowired
	private DelayProductionEventMapper productionEventMapper;
	
	@Autowired
	private DelayConsumeEventMapper consumeEventMapper;
	
    public void insertProduction(MessageDto message,byte status,int type) {
        DelayProductionEvent productionEvent = new DelayProductionEvent();
        productionEvent.setId(IdGenerator.nextId());
        productionEvent.setBizId(message.getBizId());
        productionEvent.setMessageId(String.valueOf(message.getMsgId()));
        productionEvent.setMessageBody(message.getBody());
        productionEvent.setStatus(status);
        productionEvent.setSystem(message.getSystem());
        productionEvent.setModule(message.getModule());
        productionEvent.setCallbackUrl(message.getCallbackUrl());
        productionEvent.setTtl(message.getTtl());
        productionEvent.setType(type);
        productionEventMapper.insertSelective(productionEvent);
    }
    
    public void insertConsume(MessageDto message,byte status) {
        DelayConsumeEvent consumeEvent = new DelayConsumeEvent();
        consumeEvent.setId(IdGenerator.nextId());
        consumeEvent.setBizId(message.getBizId());
        consumeEvent.setMessageId(String.valueOf(message.getMsgId()));
        consumeEvent.setMessageBody(message.getBody());
        consumeEvent.setStatus(status);
        consumeEvent.setSystem(message.getSystem());
        consumeEvent.setModule(message.getModule());
        consumeEvent.setCallbackUrl(message.getCallbackUrl());
        consumeEvent.setTtl(message.getTtl());
        consumeEventMapper.insertSelective(consumeEvent);
	}
    
    public List<DelayConsumeEvent> getPostFailData(Map<String,Object> params) {
    	return consumeEventMapper.selectFailDataByCondition(params);
    }
    
	public void updateStatusById(Long id) {
		consumeEventMapper.updateStatusById(id);
	}
	
	public void updateSendNumById(Long id) {
		consumeEventMapper.updateSendNumById(id);
	}
}
