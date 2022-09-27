package com.mds.mobile.ui.driver.secure;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.List;

import com.mds.mobile.R;
import com.mds.mobile.base.Global;
import com.mds.mobile.model.ApplicationError;
import com.mds.mobile.module.dialog.MyDialog;
import com.mds.mobile.module.loading.Loading;
import com.mds.mobile.module.mylog.MyLog;
import com.mds.mobile.remote.ServiceGenerator;
import com.mds.mobile.remote.entity.driverinsurance.request.AddFleetInsuranceClaimRequestEntity;
import com.mds.mobile.remote.entity.driverinsurance.response.AddFleetInsuranceClaimResponseEntity;
import com.mds.mobile.remote.service.DriverServiceClient;
import com.mds.mobile.ui.imagepicker.ImagePickerActivity;
import com.mds.mobile.util.GlobalHelper;
import retrofit2.Call;

public abstract class DriverAssuranceBaseActivity extends DriverBaseUi {

    public static final int REQUEST_IMAGE = 100;

    @Override
    protected void onErrorReceived(ApplicationError applicationError) {
        Loading.cancelLoading();

        MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT, getString(R.string.title_assuranceclaim),
                applicationError.getMessage(), getString(R.string.ok), this);
    }

    @Override
    protected void onSuccessReceived(Object entity) {

        AddFleetInsuranceClaimResponseEntity resp = (AddFleetInsuranceClaimResponseEntity) entity;

        MyLog.info("InformationActivity respEntity getResponseCode " + resp.getResponseCode());
        MyLog.info("InformationActivity respEntity getResponseType " + resp.getResponseType());
        MyLog.info("InformationActivity respEntity getResponseMessage " + resp.getResponseMessage());

        MyDialog.showDialogSucceed(this, MyDialog.DIALOG_ID_CLOSE, getString(R.string.title_assuranceclaim),
                getString(R.string.text_succeed), getString(R.string.close), this);

        Loading.cancelLoading();

    }

    protected void callJSONAddFleetInsurance(AddFleetInsuranceClaimRequestEntity ent) {

        MyLog.info("getRequestType : "+ent.getRequestType());
        MyLog.info("getAuthorize : "+ent.getAuthorize());
        MyLog.info("getUserCode : "+ent.getUserCode());
        MyLog.info("getFleetId : "+ent.getFleetId());
        MyLog.info("getClaimType : "+ent.getClaimType());
        MyLog.info("getDriverName : "+ent.getDriverName());
        MyLog.info("getDriverPhone : "+ent.getDriverPhone());
        MyLog.info("getIncidentDate : "+ent.getIncidentDate());
        MyLog.info("getIncidentLocation : "+ent.getIncidentLocation());
        MyLog.info("getIncidentSpeed : "+ent.getIncidentSpeed());
        MyLog.info("getIncidentDescription : "+ent.getIncidentDescription());
        MyLog.info("getIncidentPhoto1 : "+ent.getIncidentPhoto1());
        MyLog.info("getIncidentPhoto2 : "+ent.getIncidentPhoto2());
        MyLog.info("getIncidentPhoto3 : "+ent.getIncidentPhoto3());
        MyLog.info("getSimPhoto1 : "+ent.getSimPhoto1());
        MyLog.info("getSimPhoto2 : "+ent.getSimPhoto2());
        MyLog.info("getStnkPhoto1 : "+ent.getStnkPhoto1());
        MyLog.info("getStnkPhoto2 : "+ent.getStnkPhoto2());
        MyLog.info("getChassisNoPhoto1 : "+ent.getChassisNoPhoto1());
        MyLog.info("getLetterPolicePhoto1 : "+ent.getLetterPolicePhoto1());
        MyLog.info("getLetterWarrantPhoto1 : "+ent.getLetterWarrantPhoto1());

        DriverServiceClient serviceClient = ServiceGenerator.createService(DriverServiceClient.class);
        Call<AddFleetInsuranceClaimResponseEntity> call = serviceClient.addFleetInsuranceClaim(ent);
        call.enqueue(callback);
    }

    protected void runningCamera(){
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    protected void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
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

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    protected void launchGalleryIntent() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    protected void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    protected void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    protected void loadProfile(Uri uri, ImageView ivCamera) {
        MyLog.info("Image cache path: " + uri.toString());

        // adi.ori
//        Glide.with(this).load(url)
//                .into(imgProfile);

        File bitmapFile = new File(uri.getPath());

//        RequestOptions myOptions = new RequestOptions().signature()

        Glide.with(this)
                .load(bitmapFile)
                .signature(new ObjectKey(bitmapFile.lastModified()))
                .into(ivCamera);

//        imgProfile.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
    }

    protected void loadProfileDefault(ImageView ivCamera) {

        // todo. get from folder

        Glide.with(this).load(R.drawable.ic_camera)
                .into(ivCamera);
//        imgProfile.setColorFilter(ContextCompat.getColor(this, R.color.profile_default_tint));
    }

}
