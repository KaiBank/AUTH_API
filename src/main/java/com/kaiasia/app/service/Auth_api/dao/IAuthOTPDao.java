package com.kaiasia.app.service.Auth_api.dao;

import com.kaiasia.app.service.Auth_api.model.entity.Auth2InsertDb;
import com.kaiasia.app.service.Auth_api.model.entity.OTP;

import java.sql.Timestamp;

public interface IAuthOTPDao {
    OTP getOTP(String sessionId, String username, String transId ) throws Exception;

    void setConfirmTime( Timestamp now, OTP otp) throws Exception;

    int insertOTP(Auth2InsertDb auth2InsertDb) throws Exception;
}
