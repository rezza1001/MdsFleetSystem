package com.mds.mobile.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import java.util.Objects;

import com.mds.mobile.R;

public abstract class MyDialog extends Dialog {

    protected Activity mActivity;
    protected View mView;
    protected MyDialog(@NonNull Context context) {
        super(context, R.style.AppTheme_transparent);
        WindowManager.LayoutParams wlmp = Objects.requireNonNull(getWindow()).getAttributes();

        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        mActivity = (Activity) context;


        View view = LayoutInflater.from(context).inflate(setLayout(), null);
        setContentView(view);
        mView = view;
        initLayout(view);
    }

    protected void setWhiteNavbar(){
        Objects.requireNonNull(getWindow()).setStatusBarColor(mActivity.getResources().getColor(R.color.colorPrimary));
    }

    protected void setColorNavbarDark(){
        Objects.requireNonNull(getWindow()).setStatusBarColor(mActivity.getResources().getColor(R.color.colorPrimary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    protected View getmView(){
        return mView;
    }

    protected abstract int setLayout();
    protected abstract void initLayout(View view);
}
