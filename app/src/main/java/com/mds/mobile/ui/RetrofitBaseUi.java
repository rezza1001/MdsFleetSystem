package com.mds.mobile.ui;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import com.mds.mobile.base.ErrorCode;
import com.mds.mobile.base.Global;
import com.mds.mobile.model.ApplicationError;
import com.mds.mobile.model.UserProfile;
import com.mds.mobile.remote.entity.BaseResponseEntity;
import com.mds.mobile.remote.entity.ErrorResponse;
import com.mds.mobile.remote.post.MyDevice;
import com.mds.mobile.util.GlobalHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.mds.mobile.module.mylog.MyLog;
import com.mds.mobile.util.NetworkUtil;

public abstract class RetrofitBaseUi extends BaseUi {

    protected abstract void onErrorReceived(ApplicationError applicationError);
    protected abstract void onSuccessReceived(Object entity);


    protected Callback callback = new Callback<Object>() {

        @Override
        public void onResponse(Call<Object> call, Response<Object> response) {

            MyLog.info("json onResponse response.code() "+response.code());
            MyLog.info("json onResponse response.message(): "+response.message());

            ApplicationError appError=null;
            String responseBody = "";

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

                    responseBody = strErrorBody;
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
                    sendError(e,responseBody);
                }
            } else {
                MyLog.warn("json onResponse isNotSuccessful errorBody() is null");
                appError = GlobalHelper.constructError(ErrorCode.C_ERROR_2005, ErrorCode.C_ERROR_MESSAGE_2005);
                sendError(null,"json onResponse isNotSuccessful errorBody() is null");
            }

            if( appError == null ){
                onSuccessReceived(response.body());
            } else {
                onErrorReceived(appError);
            }

        }

        @Override
        public void onFailure(Call<Object> call, Throwable t) {
            MyLog.error("json onFailure exception occur", t);

            String responseCode=null;
            String responseMessage=null;

            MyLog.info("onfailure. " +t.getMessage());

            if(t instanceof ConnectException){
                responseCode = ErrorCode.C_ERROR_2001;
                responseMessage = ErrorCode.C_ERROR_MESSAGE_2001;

            }
            ApplicationError appError = null;
            if(responseCode!=null){
                appError = GlobalHelper.constructError(responseCode, responseMessage);
            } else {
                appError = GlobalHelper.constructError(ErrorCode.C_ERROR_2002,  ErrorCode.C_ERROR_MESSAGE_2002);
                responseMessage = " onFailure exception occur";
                if (t.getMessage() != null){
                    responseMessage = t.getMessage();
                }
                sendError(null,responseMessage);
            }



                onErrorReceived(appError);

        }
    };

    public void sendError(Exception e, String responseData){
        UserProfile userProfile = getUserProfile();
        MyDevice device = new MyDevice(getApplicationContext());
        JSONObject joDevice = new JSONObject();
        try {
            joDevice.put("name",device.getDeviceName());
            joDevice.put("version",device.getVersion());
            joDevice.put("os",device.getOs());
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        DateFormat format = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss", Locale.getDefault());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> data = new HashMap<>();
//        data.put("apiUrl", mainUrl +""+ apiUrl);
//        data.put("param", mData.toString());
        data.put("network", NetworkUtil.getConnection(getApplication()).toStringData());
        data.put("user_id", userProfile.getUserId());
        data.put("username", userProfile.getUsername());
        data.put("versionApps",device.getVersion());
        data.put("device",joDevice.toString());
        data.put("time", format.format(new Date()));
        data.put("timemillis", System.currentTimeMillis());
        data.put("date", format.format(new Date()));
        String response = "-";

        if (e != null){
            if (e.getMessage() != null){
                if (Objects.requireNonNull(e.getMessage()).length() > 100){
                    response = e.getMessage().substring(0,100);
                }
                else {
                    response = e.getMessage();
                }
            }
            if (responseData.length() > 500){
                responseData = responseData.substring(0,500);
            }
        }

        Calendar calendar = Calendar.getInstance();
        data.put("error", response);
        data.put("response", responseData);
        db.collection("DBUG_"+calendar.get(Calendar.YEAR)+"_"+calendar.get(Calendar.MONTH))
                .add(data)
                .addOnSuccessListener(documentReference -> Log.d("RetrofitBaseUi", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e1 -> Log.w("RetrofitBaseUi", "Error adding document", e1));
    }


}
