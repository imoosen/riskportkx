package com.allinpay.bigdata.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.consumer.ZookeeperConsumerConnector;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;
import lombok.Data;

@Data
@Component
public class KafkaConfig {
	
	@Value("${kafka.consumer.zookeeper.connect}")
    private String zookeeperConnect;
    
	@Value("${kafka.consumer.group.id}")
	private String groupId;
    
	@Value("${kafka.consumer.zookeeper.session.timeout.ms}")
	private String zookeeperSessionTimeoutms;
    
	@Value("${kafka.consumer.zookeeper.sync.time.ms}")
	private String zookeeperSyncTimems;
    
	@Value("${kafka.consumer.auto.commit.interval.ms}")
	private String autoCommitIntervalms;
    
	@Value("${kafka.consumer.auto.offset.reset}")
	private String autoOffsetReset;
    
	@Value("${kafka.consumer.serializer.class}")
	private String serializerClass;
	
	@Value("${kafka.producer.broker.list}")
	private String brokerList;
	
	@Value("${kafka.producer.serializer.class}")
	private String producerSerializer;
	
	@Value("${kafka.producer.key.serializer.class}")
	private String producerKeySerializer;
	
	@Value("${kafka.producer.partitioner.class}")
	private String partionerClass ;
	
	@Value("${kafka.producer.request.required.acks}")
	private String requestAck;
	
	
	@Autowired
	private ConsumerConfig consumerConfig;
	@SuppressWarnings("deprecation")
	@Autowired
	private ProducerConfig producerConfig;
	
	@Bean(name="consumerConfig")
	public ConsumerConfig consumerConfig()
	{
		Properties props = new Properties();
		//zookeeper 配置
		props.put("zookeeper.connect", zookeeperConnect);
		//group 代表一个消费组
		props.put("group.id",groupId );
//        //zk连接超时
		props.put("zookeeper.session.timeout.ms", zookeeperSessionTimeoutms);
		props.put("zookeeper.sync.time.ms", zookeeperSyncTimems);
		props.put("auto.commit.interval.ms",autoCommitIntervalms);
		props.put("auto.offset.reset", autoOffsetReset);
//        //序列化类
		props.put("serializer.class", serializerClass);
		ConsumerConfig config = new ConsumerConfig(props);
		return config;
	}
	
	@SuppressWarnings("deprecation")
	@Bean(name="producerConfig")
	public ProducerConfig puoduceConfig()
	{
	    Properties props = new Properties();
	    props.put("metadata.broker.list", brokerList);
        props.put("serializer.class", producerSerializer);
        // key.serializer.class默认为serializer.class
        props.put("key.serializer.class", producerKeySerializer);
        // 可选配置，如果不配置，则使用默认的partitioner
        props.put("partitioner.class", partionerClass);
        // 触发acknowledgement机制，否则是fire and forget，可能会引起数据丢失
        // 值为0,1,-1,可以参考
        // http://kafka.apache.org/08/configuration.html
        props.put("request.required.acks", requestAck);
        ProducerConfig config = new ProducerConfig(props);
		return config;
	}
	
	@Bean(name="consumerConnector")
	public ConsumerConnector getConsumerConnector() {
		ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);
		return consumerConnector;
	}
	
	
	@Bean(name="producer")
	public Producer<String,String> getProducer() {
		Producer<String, String> producer = new Producer<String, String>(producerConfig);
		return producer;
	}
	
}