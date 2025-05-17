package com.kaiasia.app.service.Auth_api.model.request;


import com.kaiasia.app.service.Auth_api.model.validation.Auth1Validation;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Auth1Request {
	@NotBlank(message = "Username is required", groups = Auth1Validation.class)
	private String sessionId;

    private String username;
    private Date startTime;
    private Date endTime;
    private String channel;
    private String location;
    private String phone;
    private String email;
    private String companyCode;
    private String customerId;
    
}
