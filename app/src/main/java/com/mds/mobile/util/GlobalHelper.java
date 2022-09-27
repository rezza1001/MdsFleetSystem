package com.mds.mobile.util;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Vector;

import com.mds.mobile.base.ErrorCode;
import com.mds.mobile.base.Global;
import com.mds.mobile.model.ApplicationError;
import com.mds.mobile.module.mylog.MyLog;
import com.mds.mobile.remote.entity.BaseResponseEntity;

/**
 * Created by pho0890910 on 8/5/2018.
 */
public class GlobalHelper {

    public static final String C_STATUS_SUCCEED = "00";
    public static final String C_USER_TYPE_CLIENT = "CLIENT";
    public static final String C_USER_TYPE_COORDINATOR = "COORDINATOR";
    public static final String C_USER_TYPE_DRIVER = "DRIVER";

    public static ApplicationError constructError(String responseCode, String responseMessage){

        MyLog.info("ApplicationError constructError. responseCode: "+responseCode);
        MyLog.info("ApplicationError constructError. responseMessage: "+responseMessage);

        StringBuffer sbMessage = new StringBuffer();

        if(GlobalHelper.isEmpty(responseCode)){
            responseCode = ErrorCode.C_ERROR_2009;
        }
        if(GlobalHelper.isEmpty(responseMessage)) {
            responseMessage = ErrorCode.C_ERROR_MESSAGE_2009;
        }

        responseMessage = responseCode + ". "+ responseMessage;

        return new ApplicationError(responseCode, responseMessage);
    }

    public static ApplicationError getApplicationError(BaseResponseEntity entity){

        if(!isStatusSucceed(entity)){
            if(entity==null){
                return constructError(ErrorCode.C_ERROR_2003, ErrorCode.C_ERROR_MESSAGE_2003);
            }
            return constructError(entity.getResponseCode(), entity.getResponseMessage());
        }
        return null;
    }

    public static boolean isEmpty(String temp){
        if(temp==null||temp.isEmpty()||temp.trim().equals("")){
            return true;
        }
        return false;
    }

    public static boolean isStatusSucceed(BaseResponseEntity entity){

        if(entity!=null){
            if(C_STATUS_SUCCEED.equals(entity.getResponseCode())){
                return true;
            }
        }

        return false;
    }

    public static String formatGermanCurrency(double number) {
        NumberFormat nf = NumberFormat.getInstance(Locale.GERMANY);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return nf.format(number);
    }

    public static String formatGermanCurrencyWithoutDigit(double number) {
        NumberFormat nf = NumberFormat.getInstance(Locale.GERMANY);
        nf.setMaximumFractionDigits(0);
        nf.setMinimumFractionDigits(0);
        return nf.format(number);
    }

    public static String bitmapToBase64String(Bitmap bmp, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
