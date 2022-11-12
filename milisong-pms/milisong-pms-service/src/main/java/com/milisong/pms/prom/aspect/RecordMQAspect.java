package com.milisong.pms.prom.aspect;

import com.farmland.core.db.IdGenerator;
import com.milisong.pms.prom.annotation.RecordMQ;
import com.milisong.pms.prom.domain.MQRecord;
import com.milisong.pms.prom.mapper.MQRecordMapper;
import io.netty.util.concurrent.ThreadPerTaskExecutor;
import jodd.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/1/10   11:16
 *    desc    : 记录MQ日志切面
 *    version : v1.0
 * </pre>
 */
@Slf4j
@Aspect
@Component
public class RecordMQAspect {

    @Autowired
    private MQRecordMapper recordMapper;

    @Resource(name ="initMQRecordExecutor")
    private Executor executor;

    private static final String CONSUME_KEY = "amqp_consumerQueue";
    private static final String SPLIT_REGEX = "\\.";

    @Around("@annotation(recordMQA)")
    public void record(ProceedingJoinPoint point,RecordMQ recordMQA) throws Throwable {
        boolean success = true;
        try {
            point.proceed();
        }catch (Throwable throwable){
            //标识结果
            success = false;
            throw  throwable;
        }finally {
            MQRecord mqRecord = new MQRecord();
            mqRecord.setBizType(recordMQA.value().getCode());
            mqRecord.setSuccess(success);
            executor.execute(()->{
                try {
                    Object[] args = point.getArgs();
                    if(args == null){
                        return;
                    }
                    String target = point.getTarget().toString();
                    if(target.contains("$$")){
                        target = target.split("\\$\\$")[0];
                    }
                    mqRecord.setBizMethodName(target.concat(".").concat(point.getSignature().getName()));
                    Arrays.stream(args).forEach(o->{
                        if(o instanceof Message){
                            Message<String> message = (Message<String>) o;
                            MessageHeaders headers = message.getHeaders();
                            mqRecord.setId(IdGenerator.nextId());
                            mqRecord.setMessageHeadersId(headers.getId().toString());
                            mqRecord.setMessageData(message.getPayload());
                            String amqpConsumerQueue = headers.get(CONSUME_KEY).toString();
                            mqRecord.setChannel(amqpConsumerQueue);
                            if(StringUtils.isNotBlank(amqpConsumerQueue)){
                                String[] strings = amqpConsumerQueue.split(SPLIT_REGEX);
                                if(strings!=null && strings.length > 1){
                                    mqRecord.setDestination(strings[0]);
                                    mqRecord.setMqGroup(strings[1]);
                                }
                            }
                            recordMapper.insertSelective(mqRecord);
                        }
                    });
                } catch (Exception e) {
                    log.error("记录MQ日志异常---",e);
                }
            });
        }
    }

    @Bean
    public Executor initMQRecordExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(10);
        threadPoolTaskExecutor.setCorePoolSize(3);
        threadPoolTaskExecutor.setQueueCapacity(300);
        threadPoolTaskExecutor.setKeepAliveSeconds(300);
        //设置拒绝服务策略(当前无线程可以用的情况下),CallerRunsPolicy标识由调用者所在线程来处理
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

}
