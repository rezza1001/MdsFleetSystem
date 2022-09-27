package com.mds.mobile.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.mds.mobile.R;
import com.mds.mobile.base.Global;
import com.mds.mobile.base.Shared;

public abstract class BaseSecureUi extends RetrofitBaseUi implements View.OnClickListener {

    protected abstract int getContentViewId();
    protected abstract void onMyBaseCreate();
    protected abstract void onMyCreate();
    protected abstract void showFaq();
    protected abstract void showNotification();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        // logout
        ImageView ivLogout = findViewById(R.id.iv_logout);
        if(ivLogout!=null) ivLogout.setOnClickListener(this);
        ImageView ivInfo = findViewById(R.id.iv_info);
        if(ivInfo!=null) ivInfo.setOnClickListener(this);
        ImageView ivNotif = findViewById(R.id.in_notif);
        if(ivNotif!=null) ivNotif.setOnClickListener(this);

        onMyBaseCreate();
        onMyCreate();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void showAlertDialogButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Anda yakin ingin keluar dari aplikasi ?");
        // add the buttons
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) dialog.dismiss();
                Intent loginScreen = new Intent(BaseSecureUi.this, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                loginScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                loginScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                Shared.clear();
                Global.clearGlobalData();
                startActivity(loginScreen);

                finish();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
//                    finish();
                if (dialog != null) dialog.dismiss();
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_logout) {
            showAlertDialogButtonClicked();
        } else if (view.getId() == R.id.iv_info) {
            showFaq();
        } else if (view.getId() == R.id.in_notif) {
            showNotification();
        }
    }

}
