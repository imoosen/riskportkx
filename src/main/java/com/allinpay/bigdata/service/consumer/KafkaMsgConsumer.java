package com.allinpay.bigdata.service.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.allinpay.bigdata.service.KafkaMsgHandler;


import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KafkaMsgConsumer {
	
	@Autowired
	private ConsumerConnector consumerConnector;
	
	@Autowired
	private KafkaMsgHandler kafkaMsgHandler;
	
	@Value("${kafka.consumer.default.topic}")
	private String topic;
	
	
	//初始化Consumer
	@PostConstruct
	public void ConsumerInit()
	{
		log.info("Kafka 消费者初始化");
		StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
		StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());
		
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, new Integer(1)); // 一次从主题中获取一个数据  
		
		Map<String, List<KafkaStream<String, String>>> consumerMap =
				consumerConnector.createMessageStreams(topicCountMap,keyDecoder,valueDecoder);
		
        KafkaStream<String, String> stream = consumerMap.get(topic).get(0);
        kafkaMsgHandler.streamHandler(stream);
        log.debug("Consumer 初始化完成");
	}
	
}
