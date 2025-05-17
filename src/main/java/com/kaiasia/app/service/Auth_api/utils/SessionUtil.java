package com.kaiasia.app.service.Auth_api.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SessionUtil {
	public String createCustomerSessionId(String customerId) {
		long timestamp = System.currentTimeMillis();
		String uuid = UUID.randomUUID().toString().replace("-", ""); // Tạo UUID
		return customerId + "_" + timestamp + "_" + uuid;
	}
}