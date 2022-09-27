package com.mds.mobile.ui;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.mds.mobile.R;
import com.mds.mobile.base.Global;
import com.mds.mobile.base.Shared;
import com.mds.mobile.module.dialog.IMyDialog;
import com.mds.mobile.module.dialog.MyDialog;
import com.mds.mobile.util.RootUtil;

public class SplashScreenActivity extends AppCompatActivity implements IMyDialog {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // init sharedPreference
//        Global.startAppIniData(SplashScreenActivity.this);

        boolean isRooted = RootUtil.isRootedDevice(this);

        if(isRooted){
            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_3, getString(R.string.e_device_error_title),
                    getString(R.string.e_device_error_message),
                    getString(R.string.close), this);
        } else {
            new Handler().postDelayed(new Runnable() {

                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

                @Override
                public void run() {

                    nextActivity();

                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }

    private void nextActivity(){


        // todo. dummy
//        Shared.logoutUser();

//        if(Shared.isLoggedIn()){
//            UserProfile po = Shared.getUserProfile();
//            if(po == null){

//                Shared.clear();
                Global.clearGlobalData();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//
//            } else {
//                Global.userProfile = po;
//                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
//            }
//        } else {
//            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//        }
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
