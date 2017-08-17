package com.allinpay.bigdata.service;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaMsgHandler {
	
	@Autowired
	private MsgDistributionManager msgDistributionManager;
	
	//消息处理方法
	@Async
	public void streamHandler(KafkaStream<String, String> stream)
	{
		log.info("开始处理kafka消息");
		ConsumerIterator<String, String> it = stream.iterator();
        while (it.hasNext()) {
        	MessageAndMetadata<String, String> message = it.next();
        	String key = message.key();
        	String value = message.message();
        	log.info("Kafka receive message:{} - {}",key,value);
        	msgDistributionManager.messageHandler(key,value);
        }
	}
	

}
