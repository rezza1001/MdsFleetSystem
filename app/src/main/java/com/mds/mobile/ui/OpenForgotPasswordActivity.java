package com.mds.mobile.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.mds.mobile.R;
import com.mds.mobile.model.ApplicationError;
import com.mds.mobile.module.dialog.MyDialog;
import com.mds.mobile.module.loading.Loading;
import com.mds.mobile.module.mylog.MyLog;
import com.mds.mobile.remote.ServiceGenerator;
import com.mds.mobile.remote.entity.odometer.request.AddFleetOdometerRequestEntity;
import com.mds.mobile.remote.entity.odometer.response.AddFleetOdometerResponseEntity;
import com.mds.mobile.remote.entity.password.request.Data;
import com.mds.mobile.remote.entity.password.request.GetForgotPasswordRequestEntity;
import com.mds.mobile.remote.entity.password.response.GetForgotPasswordResponseEntity;
import com.mds.mobile.remote.service.ClientServiceClient;
import com.mds.mobile.remote.service.DriverServiceClient;
import com.mds.mobile.util.GlobalHelper;
import retrofit2.Call;

public class OpenForgotPasswordActivity extends AppCompatActivity {
//        extends RetrofitBaseUi implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_forgot_password);
    }

//    Button btnSave;
//    Button btnCancel;
//    EditText etUsername;
//    EditText etPassword;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_open_forgot_password);
//
////        etUsername = findViewById(R.id.et_username);
////        etPassword = findViewById(R.id.et_new_password);
////        btnSave = findViewById(R.id.btn_save);
////        btnCancel = findViewById(R.id.btn_cancel);
////        btnSave.setOnClickListener(this);
////        btnCancel.setOnClickListener(this);
//
//        // default
//        message = getString(R.string.text_forgotpwd_succeed);
//
//    }

//    @Override
//    public void onClick(View view) {
//
//        if (isFastDoubleClick()) return;
//
//        if (view.getId() == R.id.btn_cancel) {
//            this.finish();
//        } else if (view.getId() == R.id.btn_save) {
//
//            if(isValid()){
//                Loading.showLoading(this, "Loading ...");
//                callJSONForgotPassword();
//            }
//
//        }
//    }

//    private boolean isValid(){
//
//        if(GlobalHelper.isEmpty(etUsername.getText().toString())){
//
//            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,getString(R.string.title_forgotpwd),
//                    getString(R.string.alert_username_empty), getString(R.string.ok), this );
//
//            etUsername.requestFocus();
//            return false;
//        }
//        if(GlobalHelper.isEmpty(etPassword.getText().toString())){
//
//            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,getString(R.string.title_forgotpwd),
//                    getString(R.string.alert_password_empty), getString(R.string.ok), this );
//
//            etPassword.requestFocus();
//            return false;
//        }
//
//        return true;
//    }
//
//    private void callJSONForgotPassword() {
//
//        GetForgotPasswordRequestEntity ent = new GetForgotPasswordRequestEntity();
//        ent.setAuthorize("RT006");
//        ent.setRequestType("authenticate_forget_password");
//        Data data = new Data();
//        data.setPassword(etPassword.getText().toString());
//        data.setUsername(etUsername.getText().toString());
//        ent.setData(data);
//
//        ClientServiceClient serviceClient = ServiceGenerator.createService(ClientServiceClient.class);
//
//        Call<GetForgotPasswordResponseEntity> call = serviceClient.forgotPassword(ent);
//        call.enqueue(callback);
//    }
//
//    @Override
//    protected void onErrorReceived(ApplicationError applicationError) {
//        etUsername.setText("");
//        etPassword.setText("");
//        Loading.cancelLoading();
//
//        MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT, getString(R.string.title_forgotpwd),
//                applicationError.getMessage(), getString(R.string.ok), this);
//    }

//    private String message = "";
//    @Override
//    protected void onSuccessReceived(Object entity) {
//        GetForgotPasswordResponseEntity resp = (GetForgotPasswordResponseEntity) entity;
//
//        MyLog.info("InformationActivity respEntity getResponseCode " + resp.getResponseCode());
//        MyLog.info("InformationActivity respEntity getResponseType " + resp.getResponseType());
//        MyLog.info("InformationActivity respEntity getResponseMessage " + resp.getResponseMessage());
//
//        message = resp.getResponseMessage();
//
//        MyDialog.showDialogSucceed(this, MyDialog.DIALOG_ID_CLOSE, getString(R.string.title_forgotpwd),
//                message, getString(R.string.close), this);
//
//        Loading.cancelLoading();
//    }
}
