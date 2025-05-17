package com.kaiasia.app.service.Auth_api.model.response;

import lombok.Data;

@Data
public class Auth0Response {

    private String transId;
    private String sessionId;
    private String responseCode;
    private String packageUser;
    private String phone;
    private String customerID;
    private String customerName;
    private String companyCode;
    private String username;

}
