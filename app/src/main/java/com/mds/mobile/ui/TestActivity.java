package com.mds.mobile.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.util.Log;

import java.io.File;

import com.mds.mobile.BuildConfig;
import com.mds.mobile.R;
import com.mds.mobile.remote.post.FileProcessing;
import com.mds.mobile.remote.post.Utility;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        findViewById(R.id.bbtn_check).setOnClickListener(view -> {
            String[] PERMISSIONS = new String[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                PERMISSIONS = new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                };
            }
            if (!Utility.hasPermission(TestActivity.this,PERMISSIONS)){
//                try {
//                    Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
//                    startActivity(intent);
//                } catch (Exception ex){
//                    Intent intent = new Intent();
//                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//                    startActivity(intent);
//                }
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                    return;
                }
            }

//            String path = "/storage/emulated/0/Android/data/com.mds.mobile/files/MNC/document/DOKUMEN KONTRAK.pdf";
            File mediaPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/DOKUMEN POLIS ASURANSI.pdf");
            Log.d("TAGRZ","mediaPath "+ mediaPath.getAbsolutePath());
//            Uri uri = FileProvider.getUriForFile(TestActivity.this, "com.mds.mobile.fileprovider", mediaPath);
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(uri, "application/pdf");
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            startActivity(intent);
            FileProcessing.openFile(TestActivity.this,mediaPath.getAbsolutePath());

//            openFile(uri);
        });
    }

    private void openFile(Uri pickerInitialUri) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
        }

        TestActivity.this.startActivity(intent);
    }
}