package com.mds.mobile.ui;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.mds.mobile.R;
import com.mds.mobile.base.ErrorCode;
import com.mds.mobile.base.Global;
import com.mds.mobile.base.Shared;
import com.mds.mobile.model.ApplicationError;
import com.mds.mobile.model.UserProfile;
import com.mds.mobile.module.dialog.MyDialog;
import com.mds.mobile.module.loading.Loading;
import com.mds.mobile.module.mylog.MyLog;
import com.mds.mobile.remote.ServiceGenerator;
import com.mds.mobile.remote.entity.login.request.Authenticate;
import com.mds.mobile.remote.entity.login.request.Data;
import com.mds.mobile.remote.entity.login.request.Device;
import com.mds.mobile.remote.entity.login.request.LoginRequestEntity;
import com.mds.mobile.remote.entity.login.response.LoginResponseEntity;
import com.mds.mobile.remote.service.LoginServiceClient;
import com.mds.mobile.ui.client.secure.ClientDashboardActivity;
import com.mds.mobile.ui.driver.secure.DriverDashboardActivity;
import com.mds.mobile.util.GlobalHelper;
import retrofit2.Call;

public class LoginActivity extends RetrofitBaseUi implements View.OnClickListener {

    TextInputEditText etUsername;
    TextInputEditText etPassword;
    Button btnLogin;
    TextView tvChangePassword;
    TextView tvContactUs;

    String username=null;
    String password=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btn_login);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        tvChangePassword = findViewById(R.id.tv_change_password);
        tvContactUs = findViewById(R.id.tv_contact_us);

        btnLogin.setOnClickListener(this);
        tvChangePassword.setOnClickListener(this);
        tvContactUs.setOnClickListener(this);

        clearInputField();
    }

    void clearInputField(){
        etUsername.setText("");
        etPassword.setText("");
    }

    @Override
    public void onClick(View view) {

        if (isFastDoubleClick()) return;

        if(view.getId() == R.id.btn_login) {

            username = etUsername.getText().toString();
            password = etPassword.getText().toString();
//
//            username = "driver";
//            password = "Jakarta2018";

//            MyLog.info("LoginActivity username "+username);
//            MyLog.info("LoginActivity password "+password);

            if(isValid()){
                // network call. login
//                Loading.showLoading(this, "Loading", this);
                Loading.showLoading(this, "Loading ...");

                callJSONLogin();
            }
        } else if(view.getId() == R.id.tv_contact_us) {
            startActivity(new Intent(getApplicationContext(), OpenContactUsActivity.class));
        } else if(view.getId() == R.id.tv_change_password) {
            startActivity(new Intent(getApplicationContext(), OpenForgotPasswordActivity.class));
        }

    }

    private boolean isValid(){

        // empty username
        if(GlobalHelper.isEmpty(username)){

            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,getString(R.string.login),
                    getString(R.string.alert_username_empty), getString(R.string.ok), this );

            etUsername.requestFocus();
            return false;
        }
        // empty password
        if(GlobalHelper.isEmpty(password)){

            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,getString(R.string.login),
                    getString(R.string.alert_password_empty), getString(R.string.ok), this );

            etPassword.requestFocus();
            return false;
        }
        return true;
    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

//    private static String md5(String input) {
//
//        try {
//            MessageDigest digest = MessageDigest.getInstance("MD5");
//            digest.update(input.getBytes());
//            byte messageDigest[] = digest.digest();
//
//            StringBuffer hexString = new StringBuffer();
//            for (int i=0; i<messageDigest.length; i++)
//                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
//            return hexString.toString();
//        } catch (NoSuchAlgorithmException e){
//            MyLog.error("error saat generate md5. ",e);
//        }
//        return "";
//    }

    private void callJSONLogin(){

        //dummy
//        client
//        password = "Jakarta2018";
//        username = "0812";

        //        driver
//        password = "Jakarta2018";
//        username = "08123456789";

        //        driver
//        password = "Jakarta2018";
//        username = "08126656901";

        // adi. 21-04-2020. md5
        password = md5(password);

//        MyLog.info("LoginActivity password "+password);

        Authenticate aut = new Authenticate();
        aut.setPassword(password);
        aut.setUsername(username);
        Device dev = new Device();
        dev.setImei("");
        dev.setLocation("");
        dev.setPhoneNo("");
        dev.setType("");
        Data data = new Data();
        data.setAuthenticate(aut);
        data.setDevice(dev);
        LoginRequestEntity ent = new LoginRequestEntity();
        ent.setAuthorize("RT006");
        ent.setData(data);
        ent.setRequestType("authenticate_login");

        LoginServiceClient serviceClient = ServiceGenerator.createService(LoginServiceClient.class);

        Call<LoginResponseEntity> call = serviceClient.login(ent);
        call.enqueue(callback);

    }

    @Override
    protected void onErrorReceived(ApplicationError applicationError) {
        etPassword.setText("");
        Loading.cancelLoading();

        MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,getString(R.string.login),
                applicationError.getMessage(), getString(R.string.ok), this );
    }

    @Override
    protected void onSuccessReceived(Object entity) {
        String eMessage = null;
        UserProfile po = null;
        LoginResponseEntity respEntity = (LoginResponseEntity) entity;

        MyLog.info("LoginActivity onSuccessReceived username : "+username);
        MyLog.info("LoginActivity onSuccessReceived getResponseCode : "+respEntity.getResponseCode());
        MyLog.info("LoginActivity onSuccessReceived getResponseMessage : "+respEntity.getResponseMessage());
        MyLog.info("LoginActivity onSuccessReceived getResponseType : "+respEntity.getResponseType());

        com.mds.mobile.remote.entity.login.response.Data data = respEntity.getData();

        if(data != null){
            MyLog.info("LoginActivity onSuccessReceived data getName : "+data.getName());
            MyLog.info("LoginActivity onSuccessReceived data getUserCode : "+data.getUserCode());
            MyLog.info("LoginActivity onSuccessReceived data getUserRole : "+data.getUserRole());
            MyLog.info("LoginActivity onSuccessReceived data getUserId : "+data.getUserId());

            po = new UserProfile();
            po.setName(data.getName());
            po.setUserCode(data.getUserId());
            po.setUserRole(data.getUserRole());
            po.setUsername(username);
            po.setUserId(data.getUserId());

            Global.userProfile = po;
            // save login session
//            try {
//                Shared.createLoginSession(po);
//
////                Global.userProfile = po;
//                Global.setUserProfile(po);
//            } catch (Exception e){
//                eMessage = ErrorCode.C_ERROR_2006 + ". " + ErrorCode.C_ERROR_MESSAGE_2006;
//                MyLog.error("LoginActivity onSuccessReceived save to shared preference failed. ", e);
//            }
        } else {
            eMessage = ErrorCode.C_ERROR_2007 + ". " + ErrorCode.C_ERROR_MESSAGE_2007;

            MyLog.warn("ERROR : LoginActivity onSuccessReceived Data Response is null. ");
        }

        Loading.cancelLoading();

        if(eMessage!=null){
            // show alert
//            Toast.makeText(this, eMessage, Toast.LENGTH_LONG).show();
            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,getString(R.string.login),
                    eMessage, getString(R.string.ok), this );

        } else {

            // client dashboard
            if(GlobalHelper.C_USER_TYPE_CLIENT.equalsIgnoreCase(po.getUserRole())){
                startActivity(new Intent(getApplicationContext(), ClientDashboardActivity.class));
                // close login screen from history
                finish();

            } else if(GlobalHelper.C_USER_TYPE_COORDINATOR.equalsIgnoreCase(po.getUserRole())){
                startActivity(new Intent(getApplicationContext(), DriverDashboardActivity.class));
//                // close login screen from history
                finish();
            } else if(GlobalHelper.C_USER_TYPE_DRIVER.equalsIgnoreCase(po.getUserRole())){
                startActivity(new Intent(getApplicationContext(), DriverDashboardActivity.class));
//                // close login screen from history
                finish();

            } else {
                MyLog.warn("ERROR : LoginActivity onSuccessReceived Data User Role is unknown : "+po.getUserRole());

                eMessage = ErrorCode.C_ERROR_2008 + ". " + ErrorCode.C_ERROR_MESSAGE_2008;
                MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,getString(R.string.login),
                        eMessage, getString(R.string.ok), this );
            }

        }
    }
}
