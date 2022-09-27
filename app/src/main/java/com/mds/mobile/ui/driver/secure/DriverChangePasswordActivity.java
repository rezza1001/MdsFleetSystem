package com.mds.mobile.ui.driver.secure;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mds.mobile.R;
import com.mds.mobile.base.Global;
import com.mds.mobile.model.ApplicationError;
import com.mds.mobile.module.dialog.MyDialog;
import com.mds.mobile.module.loading.Loading;
import com.mds.mobile.module.mylog.MyLog;
import com.mds.mobile.remote.ServiceGenerator;
import com.mds.mobile.remote.entity.changepassword.request.ChangePasswordRequestEntity;
import com.mds.mobile.remote.entity.changepassword.request.Data;
import com.mds.mobile.remote.entity.changepassword.response.ChangePasswordResponseEntity;
import com.mds.mobile.remote.service.ClientServiceClient;
import com.mds.mobile.util.GlobalHelper;
import retrofit2.Call;

public class DriverChangePasswordActivity extends DriverBaseUi {

    Button btnSave;
    Button btnCancel;
    EditText etOldPassword;
    EditText etNewPassword;
    EditText etConfPassword;

    //        String header = getString(R.string.title_changepwd);
    String header = "";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_driver_change_password;
    }

    @Override
    protected Integer getNavigationMenuItemIndex() {
        return R.id.nav_profile;
    }

    @Override
    protected void onMyCreate() {
        etOldPassword = findViewById(R.id.et_old_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfPassword = findViewById(R.id.et_conf_password);

        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    public void showAlertDialogCancelClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.cancel));
        builder.setMessage(getString(R.string.text_notif_cancel));
        // add the buttons
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) dialog.dismiss();
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showAlertDialogSaveClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.save));
        builder.setMessage(getString(R.string.text_notif_save));
        // add the buttons
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) dialog.dismiss();
                Loading.showLoading(DriverChangePasswordActivity.this, "Loading ...");
                callJSONChangePassword();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) dialog.dismiss();
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View view) {

        if (isFastDoubleClick()) return;

        if (view.getId() == R.id.btn_cancel) {
//            this.finish();
            showAlertDialogCancelClicked();
        } else if (view.getId() == R.id.btn_save) {

            if(isValid()){
//                Loading.showLoading(this, "Loading ...");
//                callJSONChangePassword();
                showAlertDialogSaveClicked();

            }
        } else {
            super.onClick(view);
        }
    }

    private boolean isValid(){

        if(GlobalHelper.isEmpty(etOldPassword.getText().toString())){

            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,header,
                    getString(R.string.alert_oldpassword_empty), getString(R.string.ok), this );

            etOldPassword.requestFocus();
            return false;
        }
        if(GlobalHelper.isEmpty(etNewPassword.getText().toString())){

            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,header,
                    getString(R.string.alert_newpassword_empty), getString(R.string.ok), this );

            etNewPassword.requestFocus();
            return false;
        }
        if(GlobalHelper.isEmpty(etConfPassword.getText().toString())){

            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,header,
                    getString(R.string.alert_confpassword_empty), getString(R.string.ok), this );

            etConfPassword.requestFocus();
            return false;
        }
        if( !etConfPassword.getText().toString().equals(etNewPassword.getText().toString()) ){

            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,header,
                    getString(R.string.alert_confpassword_notequal), getString(R.string.ok), this );

            etConfPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void callJSONChangePassword() {

        ChangePasswordRequestEntity ent = new ChangePasswordRequestEntity();
        ent.setAuthorize("RT006");
        ent.setRequestType("authenticate_change_password");
        Data data = new Data();
        data.setPasswordOld(etOldPassword.getText().toString());
        data.setPasswordNew(etNewPassword.getText().toString());
        data.setUsername(getUserProfile().getUsername());
        ent.setData(data);

        ClientServiceClient serviceClient = ServiceGenerator.createService(ClientServiceClient.class);

        Call<ChangePasswordResponseEntity> call = serviceClient.changePassword(ent);
        call.enqueue(callback);
    }

    @Override
    protected void onErrorReceived(ApplicationError applicationError) {
        etOldPassword.setText("");
        etNewPassword.setText("");
        etConfPassword.setText("");
        Loading.cancelLoading();

        MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT, header,
                applicationError.getMessage(), getString(R.string.ok), this);
    }

    private String message = "";
    @Override
    protected void onSuccessReceived(Object entity) {
        ChangePasswordResponseEntity resp = (ChangePasswordResponseEntity) entity;

        MyLog.info("InformationActivity respEntity getResponseCode " + resp.getResponseCode());
        MyLog.info("InformationActivity respEntity getResponseType " + resp.getResponseType());
        MyLog.info("InformationActivity respEntity getResponseMessage " + resp.getResponseMessage());

        message = resp.getResponseMessage();

        MyDialog.showDialogSucceed(this, MyDialog.DIALOG_ID_CLOSE, header,
                message, getString(R.string.close), this);

        Loading.cancelLoading();
    }
}
