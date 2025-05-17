package com.kaiasia.app.service.Auth_api.dao;

import com.kaiasia.app.service.Auth_api.model.request.Auth1Request;
import com.kaiasia.app.service.Auth_api.model.response.Auth1Response;

public interface IAuthSessionDao {
    int insertSessionId(Auth1Request auth1Request) throws Exception;

    boolean isSessionUsername(String customerId)  throws Exception;

    Auth1Response getAuthSessionId(String sessionId) throws Exception;

    int updateExpireSessionId(String sessionId) throws Exception;

    int deleteExpireSessionId() throws Exception;

    int deleteSessionByCustomerId(String customerId) throws  Exception;

    int expireSessionImmediately(String sessionId);
}
