package com.mds.mobile.ui;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import com.mds.mobile.base.Global;
import com.mds.mobile.base.Shared;
import com.mds.mobile.model.UserProfile;
import com.mds.mobile.module.dialog.IMyDialog;
import com.mds.mobile.module.dialog.MyDialog;

public abstract class BaseUi extends AppCompatActivity implements IMyDialog {

    static long lastClickTime;
    static long fastClickTime = 400;

    public static boolean isFastDoubleClick()
    {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < fastClickTime){
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static void hideSoftKeyboard( Activity activity ){
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        try{
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e){}
    }

    @Override
    public void aDialogLeft(int p_intDialogId, Object p_obj) {

    }

    @Override
    public void aDialogRight(int p_intDialogId, Object p_obj) {

    }

    @Override
    public void aDialogMid(int p_intDialogId, Object p_obj) {
        if(p_intDialogId == MyDialog.DIALOG_ID_ALERT){
            MyDialog.dialogDismiss();
        } else if(p_intDialogId == MyDialog.DIALOG_ID_CLOSE) {
            MyDialog.dialogDismiss();
            backToMainScreen();
        }

//        else if (p_intDialogId == MyDialog.DIALOG_ID_ERROR){
//            MyDialog.dialogDismiss();
//            if (getScreenType() == AC_TYPE_TRANS_CHILD)
//            {
//                backToMainScreen();
//            }
//            else if(getScreenType() == AC_TYPE_NORMAL)
//            {
//                onBackPressed();
//            }
//        } else if(p_intDialogId == MyDialog.DIALOG_ID_FORCE_LOGOUT){
//            MyDialog.dialogDismiss();
//            logoutToLoginScreen();
//        }

        // close App
        // adi. note : jika nama button adalah CLOSE maka akan close App -- ide bagus
        // sekarang belum dipakai
        if (p_obj != null){
            if (p_obj instanceof String){
                String l_strClose = (String) p_obj;
                if (MyDialog.C_STR_CLOSE.equalsIgnoreCase(l_strClose)){
                    MyDialog.dialogDismiss();

                    // todo
//                    closeApp();
                }
            }
        }

    }

    protected void backToMainScreen(){
        Intent loginScreen = new Intent(BaseUi.this, LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        loginScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        loginScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loginScreen);
    }

    protected UserProfile getUserProfile(){

        UserProfile temp = Global.userProfile;

        // something wrong, static userprofile gone
        // return login
        if(temp == null) {

//            Global.startAppIniData(this);

            Intent loginScreen = new Intent(getApplicationContext(), LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            loginScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            loginScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginScreen);
            // todo. perlu ini ndak ya ?
            this.finish();
        }

        return temp;
    }


}
