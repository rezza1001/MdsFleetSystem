package com.mds.mobile.ui.client.secure;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.mds.mobile.R;
import com.mds.mobile.model.ApplicationError;
import com.mds.mobile.model.NotificationItem;
import com.mds.mobile.module.dialog.MyDialog;
import com.mds.mobile.module.loading.Loading;
import com.mds.mobile.module.mylog.MyLog;
import com.mds.mobile.remote.ServiceGenerator;
import com.mds.mobile.remote.entity.notif.request.GetNotifRequestEntity;
import com.mds.mobile.remote.entity.notif.response.GetNotifResponseEntity;
import com.mds.mobile.remote.entity.notif.response.ResponseDatum;
import com.mds.mobile.remote.entity.profile.request.GetProfileRequestEntity;
import com.mds.mobile.remote.entity.profile.response.Data;
import com.mds.mobile.remote.entity.profile.response.GetProfileResponseEntity;
import com.mds.mobile.remote.service.ClientServiceClient;
import com.mds.mobile.ui.imagepicker.ImagePickerActivity;
import retrofit2.Call;

public class ClientProfileActivity extends ClientBaseUi {

    public static final int REQUEST_IMAGE = 100;

//    ImageView imgProfile;
    Button btnChangePassword;

    TextView tvClientName;
    TextView tvNasabah;
    TextView tvAddress;
    TextView tvPhone;
    TextView tvHp;
    TextView tvEmail;
    TextView tvContact;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_client_profile;
    }

    @Override
    protected Integer getNavigationMenuItemIndex() {
        return R.id.nav_profile;
    }

    @Override
    protected void onMyCreate() {

        btnChangePassword = findViewById(R.id.btn_change_password);
        btnChangePassword.setOnClickListener(this);
//        imgProfile = findViewById(R.id.img_profile);
//        imgProfile.setOnClickListener(this);

//        loadProfileDefault();

        tvClientName = findViewById(R.id.tv_client_name);
        tvNasabah = findViewById(R.id.tv_name);
        tvAddress = findViewById(R.id.tv_address);
        tvPhone = findViewById(R.id.tv_phone);
        tvHp = findViewById(R.id.tv_hp);
        tvEmail = findViewById(R.id.tv_email);
        tvContact = findViewById(R.id.tv_contact);

        Loading.showLoading(this, "Loading ...");
        callJSONGetProfile();

    }

    private void callJSONGetProfile() {
        GetProfileRequestEntity ent = new GetProfileRequestEntity();
        ent.setAuthorize("RT006");
        ent.setRequestType("user_information");
        ent.setUserCode(getUserProfile().getUserCode());
        ent.setUserId(getUserProfile().getUserId());

        ClientServiceClient serviceClient = ServiceGenerator.createService(ClientServiceClient.class);

        Call<GetProfileResponseEntity> call = serviceClient.getProfile(ent);
        call.enqueue(callback);
    }

    @Override
    protected void onErrorReceived(ApplicationError applicationError) {
        Loading.cancelLoading();

        MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT, getString(R.string.title_profile),
                applicationError.getMessage(), getString(R.string.ok), this);
    }

    @Override
    protected void onSuccessReceived(Object entity) {

        if (entity instanceof GetProfileResponseEntity) {
            GetProfileResponseEntity resp = (GetProfileResponseEntity) entity;

            Data data = resp.getData();

            tvClientName.setText(data.getClientName());
            tvNasabah.setText(getUserProfile().getUserCode());
            tvAddress.setText(data.getAddress());
            tvPhone.setText(data.getPhoneNo());
            tvHp.setText(data.getPhoneMobile());
            tvContact.setText(data.getContactPerson());
            tvEmail.setText(data.getEmail());

        }

        Loading.cancelLoading();

    }

    @Override
    public void onClick(View view) {
        if (isFastDoubleClick()) return;

        if (view.getId() == R.id.btn_change_password) {
            startActivity(new Intent(getApplicationContext(), ClientChangePasswordActivity.class));

//        } else if (view.getId() == R.id.img_profile) {
//            Dexter.withActivity(this)
//                    .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    .withListener(new MultiplePermissionsListener() {
//                        @Override
//                        public void onPermissionsChecked(MultiplePermissionsReport report) {
//                            if (report.areAllPermissionsGranted()) {
//                                showImagePickerOptions();
//                            }
//
//                            if (report.isAnyPermissionPermanentlyDenied()) {
//                                showSettingsDialog();
//                            }
//                        }
//                        @Override
//                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                            token.continuePermissionRequest();
//                        }
//                    }).check();
        } else {
            super.onClick(view);
        }

    }

//    private void loadProfile(Uri uri) {
//        MyLog.info("Image uri: " + uri.toString());
//
//        // adi. ori
////        Glide.with(this).load(url)
////                .into(imgProfile);
//
//        File bitmapFile = new File(uri.getPath());
//
//        Glide.with(this)
//                .load(bitmapFile)
//                .signature(new ObjectKey(bitmapFile.lastModified()))
//                .into(imgProfile);
//
////        imgProfile.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
//    }

//    private void loadProfileDefault() {
//        // todo. get from folder
//
////        Glide.with(this).load(R.drawable.ic_user)
////                .into(imgProfile);
////        imgProfile.setColorFilter(ContextCompat.getColor(this, R.color.profile_default_tint));
//    }
//
//    private void showImagePickerOptions() {
//        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
//            @Override
//            public void onTakeCameraSelected() {
//                launchCameraIntent();
//            }
//
//            @Override
//            public void onChooseGallerySelected() {
//                launchGalleryIntent();
//            }
//        });
//    }
//
//    private void launchCameraIntent() {
//        Intent intent = new Intent(ClientProfileActivity.this, ImagePickerActivity.class);
//        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);
//
//        // setting aspect ratio
//        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
//
//        // setting maximum bitmap width and height
//        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
//        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
//        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);
//
//        startActivityForResult(intent, REQUEST_IMAGE);
//    }
//
//    private void launchGalleryIntent() {
//        Intent intent = new Intent(ClientProfileActivity.this, ImagePickerActivity.class);
//        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
//
//        // setting aspect ratio
//        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
//        startActivityForResult(intent, REQUEST_IMAGE);
//    }
//
//    /**
//     * Showing Alert Dialog with Settings option
//     * Navigates user to app settings
//     * NOTE: Keep proper title and message depending on your app
//     */
//    private void showSettingsDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(ClientProfileActivity.this);
//        builder.setTitle(getString(R.string.dialog_permission_title));
//        builder.setMessage(getString(R.string.dialog_permission_message));
//        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
//            dialog.cancel();
//            openSettings();
//        });
//        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
//        builder.show();
//
//    }
//
//    // navigating user to app settings
//    private void openSettings() {
//        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        Uri uri = Uri.fromParts("package", getPackageName(), null);
//        intent.setData(uri);
//        startActivityForResult(intent, 101);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_IMAGE) {
//            if (resultCode == Activity.RESULT_OK) {
//                Uri uri = data.getParcelableExtra("path");
//                try {
//                    // You can update this bitmap to your server
//                    // adi.ori
////                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//
//                    // loading profile image from local cache
//                    // adi.ori
////                    loadProfile(uri.toString());
//                    loadProfile(uri);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

}
