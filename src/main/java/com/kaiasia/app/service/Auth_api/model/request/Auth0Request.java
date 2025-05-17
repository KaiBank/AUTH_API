package com.kaiasia.app.service.Auth_api.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kaiasia.app.service.Auth_api.model.validation.Auth0Validation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Auth0Request {
	@NotBlank(message = "Username is required", groups = Auth0Validation.class)
	private String username;

	@NotBlank(message = "Username is required", groups = Auth0Validation.class)
	private String password;
}
