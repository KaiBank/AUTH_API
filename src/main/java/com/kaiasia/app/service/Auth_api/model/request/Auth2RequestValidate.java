package com.kaiasia.app.service.Auth_api.model.request;

import com.kaiasia.app.service.Auth_api.model.validation.Auth2Validation;

import javax.validation.constraints.NotBlank;

public class Auth2RequestValidate {
	@NotBlank(message = "SessionId is required", groups = Auth2Validation.class)
	private String sessionId;

	@NotBlank(message = "Username is required", groups = Auth2Validation.class)
	private String username;

	@NotBlank(message = "Email is required", groups = Auth2Validation.class)
	private String gmail;

	@NotBlank(message = "Transaction time is required", groups = Auth2Validation.class)
	private String transTime;

	@NotBlank(message = "Transaction ID is required", groups = Auth2Validation.class)
	private String transId;

	@NotBlank(message = "Transaction info is required", groups = Auth2Validation.class)
	private String transInfo;

	@NotBlank(message = "Transaction description is required", groups = Auth2Validation.class)
	private String transDesc;
}
