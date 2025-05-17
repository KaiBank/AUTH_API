package com.kaiasia.app.service.Auth_api.api.getOTP;

import com.fasterxml.jackson.databind.ObjectMapper;


import com.kaiasia.app.core.job.BaseService;
import com.kaiasia.app.core.utils.ApiConstant;
import com.kaiasia.app.core.utils.GetErrorUtils;
import com.kaiasia.app.register.KaiMethod;
import com.kaiasia.app.register.KaiService;
import com.kaiasia.app.register.Register;
import com.kaiasia.app.service.Auth_api.dao.IAuthOTPDao;
import com.kaiasia.app.service.Auth_api.dao.SessionIdDAO;
import com.kaiasia.app.service.Auth_api.model.response.Auth2Response;
import com.kaiasia.app.service.Auth_api.utils.KafkaUtils;
import com.kaiasia.app.service.Auth_api.model.entity.Auth2InsertDb;
import com.kaiasia.app.service.Auth_api.model.request.Auth2Request;
import com.kaiasia.app.service.Auth_api.utils.AuthTakeSession;
import com.kaiasia.app.service.Auth_api.utils.OtpUtils;
import com.kaiasia.app.service.Auth_api.utils.StatusOTPEnum;
import lombok.extern.slf4j.Slf4j;
import ms.apiclient.model.ApiBody;
import ms.apiclient.model.ApiError;
import ms.apiclient.model.ApiRequest;
import ms.apiclient.model.ApiResponse;
import ms.apiclient.t24util.T24Request;
import ms.apiclient.t24util.T24UserInfoResponse;
import ms.apiclient.t24util.T24UtilClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@KaiService
@Slf4j
public class GetOTPService extends BaseService {

    @Autowired
    GetErrorUtils apiErrorUtils;

    @Autowired
    private IAuthOTPDao authOTPService;

    private ApiError apiError;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaUtils kafkaUtils;

    @Autowired
    private T24UtilClient t24UtilClient;

    @Autowired
    private SessionIdDAO sessionIdDAO;

    @Autowired
    private AuthTakeSession authTakeSession;

    @Value("${kai.expireTime}")
    private int expireTime;

    @KaiMethod(name = "getOTP", type = Register.VALIDATE)
    public ApiError validate(ApiRequest req) {

        Auth2Request enquiry = objectMapper.convertValue(getEnquiry(req), Auth2Request.class);

        if (StringUtils.isBlank(enquiry.getSessionId())) {
            return apiErrorUtils.getError("706", new String[]{"sessionId"});
        }

        if (StringUtils.isBlank(enquiry.getUsername())) {
            return apiErrorUtils.getError("706", new String[]{"username"});
        }

        if (StringUtils.isBlank(enquiry.getGmail())) {
            return apiErrorUtils.getError("706", new String[]{"gmail"});
        }

        if (StringUtils.isBlank(enquiry.getTransTime())) {
            return apiErrorUtils.getError("706", new String[]{"transTime"});
        }

        if (StringUtils.isBlank(enquiry.getTransId())) {
            return apiErrorUtils.getError("706", new String[]{"transId"});
        }

        if (StringUtils.isBlank(enquiry.getTransInfo())) {
            return apiErrorUtils.getError("706", new String[]{"transDesc"});
        }


        return new ApiError(ApiError.OK_CODE, ApiError.OK_DESC);
    }

    @KaiMethod(name = "getOTP")
    public ApiResponse process(ApiRequest req) throws Exception {
        long a = System.currentTimeMillis();

        ApiResponse apiResponse = new ApiResponse();

        Auth2Request auth2Request = objectMapper.convertValue(getEnquiry(req), Auth2Request.class);

        String LOCATION = "GetOTP" + auth2Request.getSessionId() + "_" + System.currentTimeMillis();

        ApiResponse checkSessionID = authTakeSession.callTakeSessionAPI(auth2Request.getSessionId());
        if (checkSessionID.getError() != null){
            ApiError apiError = apiErrorUtils.getError("801", new String[]{auth2Request.getSessionId()});
            log.info(LOCATION + "#END#Duration:" + (System.currentTimeMillis() - a));
            apiResponse.setError(apiError);
            return apiResponse;

        }

        log.info(LOCATION + "#BEGIN CALL USER INFO");
        T24UserInfoResponse t24UserInfoResponse = t24UtilClient.getUserInfo(
                LOCATION,
                T24Request
                        .builder()
                        .username(auth2Request.getUsername())
                        .build(),
                req.getHeader()
        );

        if (t24UserInfoResponse.getError() != null){
            ApiError apiError = new ApiError(t24UserInfoResponse.getError().getCode(), t24UserInfoResponse.getError().getDesc());
            apiResponse.setError(apiError);
            log.info(LOCATION + "#END CALL USER INFO" + (System.currentTimeMillis() - a));
            return apiResponse;
        }

        if (t24UserInfoResponse.getEmail() == null && t24UserInfoResponse.getEmail().isEmpty()){
            ApiError apiError = new ApiError(t24UserInfoResponse.getError().getCode(), t24UserInfoResponse.getError().getDesc());
            apiResponse.setError(apiError);
            log.info(LOCATION + "#EMAIL DOES NOT EXIST");
            return apiResponse;
        }


        String generateOTP = OtpUtils.generateValidateCode();
        String generateTransID = auth2Request.getTransId() + "-" +auth2Request.getUsername() + OtpUtils.generateTransID();

        Auth2InsertDb auth2InsertDb = Auth2InsertDb.builder()
                .transId(generateTransID)
                .validateCode(generateOTP)
                .username(auth2Request.getUsername())
                .sessionId(auth2Request.getSessionId())
                .channel(req.getHeader().getChannel())
                .location(req.getHeader().getLocation())
                .startTime(Timestamp.valueOf(LocalDateTime.now()))
                .endTime(Timestamp.valueOf(LocalDateTime.now().plusMinutes(expireTime)))
                .status(String.valueOf(StatusOTPEnum.CONFIRM))
                .transTime(auth2Request.getTransTime())
                .transInfo(auth2Request.getTransInfo())
                .confirmTime(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        int result = authOTPService.insertOTP(auth2InsertDb);

        if (result == 0) {
            log.info("INSERT FAIL" + LOCATION);
        }else {
            log.info("INSERT SUCCESSFULLY" + LOCATION);
        }

        log.info("SEND TO KAFKA");
        kafkaUtils.sendMessage(auth2Request.getGmail(), generateOTP);

        Auth2Response response = new Auth2Response();
        response.setResponseCode("00");
        response.setTransId(generateTransID);
        ApiBody apiBody = new ApiBody();
        apiBody.put(ApiConstant.COMMAND.ENQUIRY, response);
        apiResponse.setBody(apiBody);

        log.info(LOCATION + "#END#Duration:" + (System.currentTimeMillis() - a));
        return apiResponse;

    }
}
