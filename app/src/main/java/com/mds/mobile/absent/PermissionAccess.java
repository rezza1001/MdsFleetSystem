package com.mds.mobile.absent;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.mds.mobile.remote.post.FileProcessing;
import com.mds.mobile.remote.post.Utility;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

    public static Map<String,Object> openCamera(Activity mActivity, String photoPath, String fileName){
        Map<String,Object> data = new HashMap<>();

        String[] PERMISSIONS = {Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        if(checkPermission(mActivity, PERMISSIONS)){
            data.put("permission",false);
            Utility.showToastError(mActivity,"Membutuhkan Permission");
            ActivityCompat.requestPermissions(Objects.requireNonNull(mActivity), PERMISSIONS, 101);
        }
        else {
            data.put("permission",true);
            String mediaPath = FileProcessing.getMainPath(mActivity).getAbsolutePath()+photoPath;
            String file =mediaPath+ fileName;
            File newfile = new File(file);
            Log.d("PermissionAccess","newfile "+file);
            data.put("create_file_0",file);
            try {
                boolean a = newfile.createNewFile();
                if (a){
                    data.put("create_file_1","Create File SUCCESS");
                    Log.d("PermissionAccess","createNewFile");
                }
                else{
                    data.put("create_file_1","Create File FAILED");
                    Log.e("PermissionAccess","createNewFile Failed");
                }
            }
            catch (IOException e) {
                data.put("create_file_2","IOException "+ e.getMessage());
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

        return data;
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
