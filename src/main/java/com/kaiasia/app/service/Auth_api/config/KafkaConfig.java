package com.kaiasia.app.service.Auth_api.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
	private String bootstrapServers;
	private String keySerializer;
	private String valueSerializer;

	public String getBootstrapServers() {
		return bootstrapServers;
	}

	public void setBootstrapServers(String bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}

	public String getKeySerializer() {
		return keySerializer;
	}

	public void setKeySerializer(String keySerializer) {
		this.keySerializer = keySerializer;
	}

	public String getValueSerializer() {
		return valueSerializer;
	}

	public void setValueSerializer(String valueSerializer) {
		this.valueSerializer = valueSerializer;
	}
}
