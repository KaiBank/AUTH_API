package com.kaiasia.app.service.Auth_api.utils;

import java.util.Random;

public class OtpUtils {

    public static String generateValidateCode(){
        Random random = new Random();
        int r = 100000 + random.nextInt(900000);
        return  String.valueOf(r) ;
    }

    public static String generateTempSession(){
        Random random = new Random();
        int r = 10000000 + random.nextInt(90000000);
        return  String.valueOf(r) ;
    }

    public static String generateTransID(){
        Random random = new Random();
        int tranId = 100000 + random.nextInt(900000);
        return  String.valueOf(tranId) ;
    }


}
