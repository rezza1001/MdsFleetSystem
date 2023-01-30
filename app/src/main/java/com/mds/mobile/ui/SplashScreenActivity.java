package com.mds.mobile.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mds.mobile.R;
import com.mds.mobile.absent.ErrorDialog;
import com.mds.mobile.base.Global;
import com.mds.mobile.database.AccountDB;
import com.mds.mobile.module.dialog.IMyDialog;
import com.mds.mobile.module.dialog.MyDialog;
import com.mds.mobile.remote.ServiceGenerator;
import com.mds.mobile.remote.post.ErrorCode;
import com.mds.mobile.remote.post.MyDevice;
import com.mds.mobile.remote.post.PostManager;
import com.mds.mobile.util.RootUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashScreenActivity extends AppCompatActivity implements IMyDialog {


    private MyDevice myDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        myDevice = new MyDevice(this);

//        boolean isRooted = RootUtil.isRootedDevice(this);
        boolean isRooted = false;

        if(isRooted){
            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_3, getString(R.string.e_device_error_title),
                    getString(R.string.e_device_error_message),
                    getString(R.string.close), this);
        } else {
            loadVersion();
        }
    }

    private void nextActivity(){
        AccountDB accountDB = new AccountDB();
        accountDB.clearData(this);

        Global.clearGlobalData();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    private void loadVersion(){
        PostManager post = new PostManager(this, ServiceGenerator.CHECK_VERSION);
        post.addHeaderParam("x_rentas_key",ServiceGenerator.API_SECRET_KEY);
        post.exGet();
        post.setOnReceiveListener((obj, code, message) -> {
            if (code == ErrorCode.OK_200){
                try {
                    JSONObject data = obj.getJSONObject("version");
                    int versionCode = data.getInt("code");

                    if (myDevice.getVersionCode() < versionCode){
                        startActivity(new Intent(SplashScreenActivity.this, UpdateAppsActivity.class));
                        SplashScreenActivity.this.finish();
                    }
                    else {
                        nextActivity();
                        SplashScreenActivity.this.finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                ErrorDialog dialog = new ErrorDialog(this);
                dialog.show("System Bermasalah","Silahkan coba beberapa saat lagi atau hubungi call center");
                dialog.setOnFinishListener(SplashScreenActivity.this::finish);
            }
        });
    }

    @Override
    public void aDialogLeft(int p_intDialogId, Object p_obj) {

    }

    @Override
    public void aDialogRight(int p_intDialogId, Object p_obj) {

    }

    @Override
    public void aDialogMid(int p_intDialogId, Object p_obj) {
        if (p_intDialogId == MyDialog.DIALOG_ID_3)
        {
            MyDialog.dialogDismiss();
            finish();
        }

        if (p_obj != null)
        {
            if (p_obj instanceof String)
            {
                String l_strClose = (String) p_obj;
                if (l_strClose.equalsIgnoreCase("close"))
                {
                    finish();
                }
            }
        }
    }

}
