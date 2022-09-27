package com.mds.mobile.absent;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.mds.mobile.remote.post.FileProcessing;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class PermissionAccess {

    public static final int OPEN_CAMERA = 11;

    public static void requestMain(Activity mActivity){
        String[] PERMISSIONS = {Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        if(checkPermission(mActivity, PERMISSIONS)){
            ActivityCompat.requestPermissions(Objects.requireNonNull(mActivity), PERMISSIONS, 101);
        }
    }

    public static void openCamera(Activity mActivity, String photoPath, String fileName){
        String[] PERMISSIONS = {Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        if(checkPermission(mActivity, PERMISSIONS)){
            ActivityCompat.requestPermissions(Objects.requireNonNull(mActivity), PERMISSIONS, 101);
        }
        else {
            String mediaPath = FileProcessing.getMainPath(mActivity).getAbsolutePath()+photoPath;
            String file =mediaPath+ fileName;
            File newfile = new File(file);
            Log.d("PermissionAccess","newfile "+file);
            try {
                boolean a = newfile.createNewFile();
                if (a){
                    Log.d("PermissionAccess","createNewFile");
                }
                else{
                    Log.e("PermissionAccess","createNewFile Failed");
                }
            }
            catch (IOException e) {
                Log.e("PermissionAccess","IOException "+ e.getMessage());
                e.printStackTrace();
            }
            Uri outputFileUri = FileProcessing.getUriFormFile(mActivity, newfile);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            mActivity.startActivityForResult(cameraIntent, OPEN_CAMERA);
        }
    }


    private static boolean checkPermission(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
        }
        return false;
    }

}
