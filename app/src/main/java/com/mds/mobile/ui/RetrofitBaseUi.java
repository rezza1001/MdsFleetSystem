package com.mds.mobile.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;

import com.mds.mobile.base.ErrorCode;
import com.mds.mobile.base.Global;
import com.mds.mobile.model.ApplicationError;
import com.mds.mobile.remote.entity.BaseResponseEntity;
import com.mds.mobile.remote.entity.ErrorResponse;
import com.mds.mobile.util.GlobalHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.mds.mobile.module.mylog.MyLog;

public abstract class RetrofitBaseUi extends BaseUi {

    protected abstract void onErrorReceived(ApplicationError applicationError);
    protected abstract void onSuccessReceived(Object entity);

    protected Callback callback = new Callback<Object>() {

        @Override
        public void onResponse(Call<Object> call, Response<Object> response) {

            MyLog.info("json onResponse response.code() "+response.code());
            MyLog.info("json onResponse response.message(): "+response.message());

            ApplicationError appError=null;

            if(response.isSuccessful()) {
                MyLog.info("json onResponse response.body() " + response.body().toString());
                MyLog.info("json onResponse isSuccessful. type : " + ((BaseResponseEntity) response.body()).getResponseType());
                MyLog.info("json onResponse isSuccessful. code : " + ((BaseResponseEntity) response.body()).getResponseCode());

                appError = GlobalHelper.getApplicationError((BaseResponseEntity) response.body());

            } else if(response.errorBody() != null){
                try {

                    String strErrorBody = response.errorBody().string();
                    MyLog.info("json onResponse isNotSuccessful response.errorBody() "+strErrorBody);

//                    ErrorResponse error = new ErrorResponse();
//                    String l_errorJson = response.errorBody().string();
//                    error = new GsonBuilder().create().fromJson(l_errorJson, ErrorResponse.class);
//
//                    BaseResponseEntity errResp = new BaseResponseEntity();
//                    errResp.setResponseCode(error.getResponseCode());
//                    errResp.setResponseMessage(error.getResponseMessage());
//                    errResp.setResponseType(error.getResponseType());


//                    Gson gson = new GsonBuilder().create();
//                    errResp= gson.fromJson(response.errorBody().string(),BaseResponseEntity .class);

                    JSONObject jObjError = new JSONObject(strErrorBody);

                    MyLog.info("errResp.getResponseCode "+jObjError.toString());

                    BaseResponseEntity errResp = new BaseResponseEntity();
                    errResp.setResponseCode(jObjError.getString("response_code"));
                    errResp.setResponseMessage(jObjError.getString("response_message"));
                    errResp.setResponseType(jObjError.getString("response_type"));

                    MyLog.info("errResp.getResponseCode "+errResp.getResponseCode());
                    MyLog.info("errResp.getResponseMessage "+errResp.getResponseMessage());
                    MyLog.info("errResp.getResponseType "+errResp.getResponseType());

                    appError = GlobalHelper.getApplicationError(errResp);

                } catch (Exception e) {
                    MyLog.error("json onResponse isNotSuccessful exception occur while trying to convert errorBody ", e);
                    appError = GlobalHelper.constructError(ErrorCode.C_ERROR_2004, ErrorCode.C_ERROR_MESSAGE_2004);
                }
            } else {
                MyLog.warn("json onResponse isNotSuccessful errorBody() is null");
                appError = GlobalHelper.constructError(ErrorCode.C_ERROR_2005, ErrorCode.C_ERROR_MESSAGE_2005);
            }

            if( appError == null ){
                onSuccessReceived(response.body());
            } else {

                // is session Id invalid
                // logout and redirect to login screen
//                if(isSessionIdInvalid(appError.getResponseCode())){
//
//                    MyLog.info("SessionId is Invalid. Redirect to Login Screen");
//
//                    Loading.cancelLoading();
//                    logoutToLoginScreen();
//
//                    return;
//                }

                onErrorReceived(appError);
            }

        }

        @Override
        public void onFailure(Call<Object> call, Throwable t) {
            MyLog.error("json onFailure exception occur", t);

            String responseCode=null;
            String responseMessage=null;

            MyLog.info("onfailure. " +t.toString());

            if(t instanceof ConnectException){
                responseCode = ErrorCode.C_ERROR_2001;
                responseMessage = ErrorCode.C_ERROR_MESSAGE_2001;
            }

            // debug
//            ApplicationError appError = new ApplicationError("d2005","d2005.");
//            ApplicationError appError = new ApplicationError("d2005", Global.C_ERROR_MESSAGE_2003);
            ApplicationError appError = null;
            if(responseCode!=null){
                appError = GlobalHelper.constructError(responseCode, responseMessage);
            } else {
                appError = GlobalHelper.constructError(ErrorCode.C_ERROR_2002,  ErrorCode.C_ERROR_MESSAGE_2002);
//                appError = GlobalHelper.constructError("f2006", "f2006." + t.getMessage());
            }


            // is session Id invalid
            // logout and redirect to login screen
//            if(isSessionIdInvalid(appError.getResponseCode())){
//
//                MyLog.info("SessionId is Invalid. Redirect to Login Screen");
//
//                Loading.cancelLoading();
//                logoutToLoginScreen();
//
//                return;
//            } else {
                onErrorReceived(appError);
//            }
//
//            MyLog.info("WHY HERE ??");

        }
    };

//    boolean isSessionIdInvalid(String responseCode){
//        if( ("s"+Global.C_ERR_CODE_INVALID_SESSION_ID).equalsIgnoreCase(responseCode)){
//            return true;
//        }
//        return false;
//    }

}
