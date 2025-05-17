package com.kaiasia.app.service.Auth_api.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;

@Configuration
public class KafkaProducerConfig {

	@Autowired
	private KafkaConfig kafkaConfig;

	@Bean
	public KafkaTemplate<String , Object> kafkaTemplate(){
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	public ProducerFactory<String , Object> producerFactory(){
		HashMap<String , Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaConfig.getBootstrapServers());
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,kafkaConfig.getKeySerializer());
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,kafkaConfig.getValueSerializer());

		return new DefaultKafkaProducerFactory<>(configProps);
	}
}
