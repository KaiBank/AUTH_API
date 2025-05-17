package com.kaiasia.app.service.Auth_api.model.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OTP {
    private String validate_code;
    private String trans_id;
    private String username;
    private String channel;
    private Timestamp start_time;
    private String location;
    private Timestamp end_time;
    private String status;
    private String session_id;
    private Timestamp confirm_time;
    private String trans_info;
    private String trans_time;
}
