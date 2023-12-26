package com.mds.mobile.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.bumptech.glide.Glide;
import com.mds.mobile.BuildConfig;
import com.mds.mobile.R;
import com.mds.mobile.remote.post.FileProcessing;
import com.mds.mobile.remote.post.Utility;
import com.mds.mobile.ui.imagepicker.ImagePickerActivity;

public class TestActivity extends AppCompatActivity {

    ImageView iv_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        iv_image = findViewById(R.id.iv_image);

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
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                if (!Environment.isExternalStorageManager()) {
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                    Uri uri = Uri.fromParts("package", getPackageName(), null);
//                    intent.setData(uri);
//                    startActivity(intent);
//                    return;
//                }
//            }

            launchCameraIntent();

        });
    }

    protected void launchCameraIntent() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){
            if (data == null){
                Log.e("TAGRZ","Data is null");
                return;
            }
            Uri uri = data.getParcelableExtra("path");
            Log.d("TAGRZ","Uri "+ uri.getPath());
            try {
                String dest = FileProcessing.getMainPath(this)+"/camera/compressed_image.jpg";
                compressImage(uri.getPath(), dest);
                Bitmap bitmap = BitmapFactory.decodeFile(dest);
                Glide.with(this).load(bitmap).into(iv_image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void compressImage(String sourceImagePath, String destinationImagePath) throws IOException {
        Bitmap sourceBitmap = BitmapFactory.decodeFile(sourceImagePath);
        Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
        int quality = 80;
        FileOutputStream fos = new FileOutputStream(destinationImagePath);
        sourceBitmap.compress(compressFormat, quality, fos);
        fos.close();
    }

}