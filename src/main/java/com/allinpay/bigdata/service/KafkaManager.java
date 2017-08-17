package com.allinpay.bigdata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaManager {
	
	
	@Autowired
	private Producer<String, String> producer;
	
	public void produce(String topic,String key,String partKey,String message)
	{
		KeyedMessage<String, String> keyMessage =
			new KeyedMessage<String, String>(topic, key, partKey, message);
		producer.send(keyMessage);
		log.info("Kafka message send success");
	}
	
	public boolean produce(String topic,String message) 
	{
		KeyedMessage<String, String> keyMessage = 
								new KeyedMessage<String, String>(topic, message);
		try{
			producer.send(keyMessage);
			log.info("Kafka message send success");
			return true;
		}catch(Exception e)
		{
			log.error("Kafka message send failed:{}{}",topic.concat("  "+message),e);
			return false;
		}
	}
}
