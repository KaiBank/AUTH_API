package com.kaiasia.app.service.Auth_api.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kaiasia.app.service.Auth_api.model.validation.Auth3Validation;
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
public class Auth3Request {
    @NotBlank(message = "SessionId is required", groups = Auth3Validation.class)
    private String sessionId;

    @NotBlank(message = "Username is required", groups = Auth3Validation.class)
    private String username;

    @NotBlank(message = "Otp is required", groups = Auth3Validation.class)
    private String otp;

    @NotBlank(message = "Transaction time is required", groups = Auth3Validation.class)
    private String transTime;

    @NotBlank(message = "Transaction id is required", groups = Auth3Validation.class)
    private String transId;
}
