package com.mili.oss.mq;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import com.mili.oss.util.MqProducerUtil;

@EnableBinding(MqMessageSource.class)
public class MqMessageProducer {
	@Autowired
    @Output(MqMessageSource.ORDER_SET_ORDER_STATUS_CHANGE_OUTPUT)
    private MessageChannel orderSetchannel;
	@Autowired
    @Output(MqMessageSource.OPERATION_LOG_OUTPUT)
    private MessageChannel opLogChannel;
	@Autowired
	@Output(MqMessageSource.ORDER_SET_OUTPUT)
	private MessageChannel orderSetOutputChannel;;
	@Autowired
	@Output(MqMessageSource.PUSH_SF_ORDER_OUTPUT)
	private MessageChannel pushSfORderOutputChannel;
	@Autowired
	@Output(MqMessageSource.ORDER_SET_OUTPUT_BF)
	private MessageChannel orderSetOutputChannelBF;

	@Resource
	@Output(MqMessageSource.WAVE_ORDER_OUTPUT)
	private MessageChannel waveOrderOutput;



	@PostConstruct
	public void postConstruct() {
		MqProducerUtil.setOrderSetChannel(orderSetchannel);
		MqProducerUtil.setOperationLogChannel(opLogChannel);
		MqProducerUtil.setOrderSetOutputChannel(orderSetOutputChannel);
		MqProducerUtil.setPushSfOrderOutputChannel(pushSfORderOutputChannel);
		MqProducerUtil.setOrderSetOutputChannelBF(orderSetOutputChannelBF);
		MqProducerUtil.setWaveOrderOutput(waveOrderOutput);
	}
}
