package com.mds.mobile.ui.driver.secure;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mds.mobile.BuildConfig;
import com.mds.mobile.R;
import com.mds.mobile.base.Global;
import com.mds.mobile.model.ApplicationError;
import com.mds.mobile.module.dialog.MyDialog;
import com.mds.mobile.module.loading.Loading;
import com.mds.mobile.module.mylog.MyLog;
import com.mds.mobile.remote.ServiceGenerator;
import com.mds.mobile.remote.entity.drivervehiclelist.request.GetFleetRequestEntity;
import com.mds.mobile.remote.entity.drivervehiclelist.response.GetFleetResponseEntity;
import com.mds.mobile.remote.entity.drivervehiclelist.response.ResponseDatum;
import com.mds.mobile.remote.entity.fleetservice.request.AddFleetServiceRequestEntity;
import com.mds.mobile.remote.entity.fleetservice.response.AddFleetServiceResponseEntity;
import com.mds.mobile.remote.service.DriverServiceClient;
import com.mds.mobile.ui.imagepicker.ImagePickerActivity;
import com.mds.mobile.ui.imagepicker.MyCameraActivity;
import com.mds.mobile.util.GlobalHelper;
import retrofit2.Call;

public class DriverFleetServiceActivity4 extends DriverBaseUi implements AdapterView.OnItemSelectedListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    Spinner spVehicle;
    Button btnSave;
    Button btnCancel;
    RadioGroup categoryGroup;
    RadioButton categoryButton;
    EditText etKm;
    EditText etKeluhan;

    List<String> fleets;
    List<String> textFleets;
    String selectedFleetId;

    ImageView ivCamera1;
    ImageView ivCamera2;

    ImageView clickedCamera;
    int cameraChoice = 0;
    Bitmap bitmap1;
    Bitmap bitmap2;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_driver_fleet_service4;
    }

    @Override
    protected Integer getNavigationMenuItemIndex() {
        return null;
    }

    // CAMERA
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {


            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
//                Uri photoURI = Uri.fromFile(createImageFile());
                Uri photoURI = FileProvider.getUriForFile(DriverFleetServiceActivity4.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);

                MyLog.info("MY URI 1 = "+photoURI);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }


            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        MyLog.info("MASUKKKKKKK onActivityResult");

        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK && data.hasExtra("data")) {

                    try {

                        File file = new File(currentPhotoPath);
                        Bitmap bitmap = MediaStore.Images.Media
                                .getBitmap(this.getContentResolver(), Uri.fromFile(file));
                        if (bitmap != null) {
                            ivCamera1.setImageBitmap(bitmap);
                        }

                    } catch (Exception error) {
                        error.printStackTrace();
                    }
                }
                break;
        }

//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//
//            MyLog.info("MASUKKKKKKK onActivityResult 2");
//
//
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            ivCamera1.setImageBitmap(imageBitmap);
//
//            if(cameraChoice == 0){
//                bitmap1 = imageBitmap;
//            } else {
//                bitmap2 = imageBitmap;
//            }
//        }
    }

    @Override
    protected void onMyCreate() {

        MyLog.info("MASUKKKKKKK onMyCreate ");

        spVehicle = findViewById(R.id.sp_vehicle);
        btnCancel = findViewById(R.id.btn_cancel);
        btnSave = findViewById(R.id.btn_save);
        categoryGroup=findViewById(R.id.rg_category);
        etKm = findViewById(R.id.et_km);
        etKeluhan = findViewById(R.id.et_keluhan);

        spVehicle.setOnItemSelectedListener(this);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        ivCamera1 = findViewById(R.id.iv_camera14);
        ivCamera1.setOnClickListener(this);
        ivCamera2 = findViewById(R.id.iv_camera24);
        ivCamera2.setOnClickListener(this);

//        loadProfileDefault(ivCamera1);
//        loadProfileDefault(ivCamera2);

        // Clearing older images from cache directory
        // don't call this line if you want to choose multiple images in the same activity
        // call this once the bitmap(s) usage is over
//        ImagePickerActivity.clearCache(this);

        Loading.showLoading(this, "Loading ...");
        callJSONGetFleet();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedFleetId = fleets.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void callJSONGetFleet() {
        GetFleetRequestEntity ent = new GetFleetRequestEntity();
        ent.setAuthorize("RT006");
        ent.setUserCode(getUserProfile().getUserCode());
        ent.setRequestType("user_fleet");

        DriverServiceClient serviceClient = ServiceGenerator.createService(DriverServiceClient.class);

        Call<GetFleetResponseEntity> call = serviceClient.getFleet(ent);
        call.enqueue(callback);
    }

    private void callJSONAddFleetService() {

//        "request_type": "user_fleet_add_service_request",
//        "authorize": "RT006",
//        "user_code": "U0002",
//        "fleet_id": "B 2478 PFN",
//        "service_type": "Non Rutin",
//        "fleet_odometer": "500",
//        "complaint": "Ganti oli dan cek rem",
//        "photo_1": "gambar1.jpg",
//        "photo_2": "gambar2.jpg"

        String strBitmap1="";
        String strBitmap2="";
        if(bitmap1!=null){
            strBitmap1 = GlobalHelper.bitmapToBase64String(bitmap1,100);
        }
        if(bitmap2!=null){
            strBitmap2 = GlobalHelper.bitmapToBase64String(bitmap2,100);
        }

//        MyLog.info("BITMAP 1 : "+strBitmap1);
//        MyLog.info("BITMAP 2 : "+strBitmap2);

        AddFleetServiceRequestEntity ent = new AddFleetServiceRequestEntity();
        ent.setRequestType("user_fleet_add_service_request");
        ent.setAuthorize("RT006");
        ent.setUserCode(getUserProfile().getUserCode());
        ent.setFleetId(selectedFleetId);
        ent.setServiceType(categoryButton.getText().toString());
        ent.setFleetOdometer(etKm.getText().toString());
        ent.setComplaint(etKeluhan.getText().toString());
        ent.setPhoto1(strBitmap1);
        ent.setPhoto2(strBitmap2);

        MyLog.info("getRequestType : "+ent.getRequestType());
        MyLog.info("getAuthorize : "+ent.getAuthorize());
        MyLog.info("getUserCode : "+ent.getUserCode());
        MyLog.info("getFleetId : "+ent.getFleetId());
        MyLog.info("getServiceType : "+ent.getServiceType());
        MyLog.info("getFleetOdometer : "+ent.getFleetOdometer());
        MyLog.info("getComplaint : "+ent.getComplaint());
        MyLog.info("getPhoto1 : "+ent.getPhoto1());
        MyLog.info("getPhoto2 : "+ent.getPhoto2());

        DriverServiceClient serviceClient = ServiceGenerator.createService(DriverServiceClient.class);
        Call<AddFleetServiceResponseEntity> call = serviceClient.addFleetServiceRequest(ent);
        call.enqueue(callback);
    }


    @Override
    protected void onErrorReceived(ApplicationError applicationError) {
        Loading.cancelLoading();

        MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT, getString(R.string.title_servicefleet),
                applicationError.getMessage(), getString(R.string.ok), this);
    }

    @Override
    protected void onSuccessReceived(Object entity) {

        if (entity instanceof GetFleetResponseEntity) {
            GetFleetResponseEntity resp = (GetFleetResponseEntity) entity;

            MyLog.info("InformationActivity respEntity getResponseCode " + resp.getResponseCode());
            MyLog.info("InformationActivity respEntity getResponseType " + resp.getResponseType());
            MyLog.info("InformationActivity respEntity getResponseMessage " + resp.getResponseMessage());

            List<ResponseDatum> list = resp.getResponseData();
            fleets = new ArrayList<String>();
            textFleets = new ArrayList<String>();
            for (ResponseDatum temp : list) {
                MyLog.info("Temp : " + temp.getFleetId());
                fleets.add(temp.getFleetId());
                textFleets.add(temp.getLicensePlate() + " " + temp.getMerk() + " " + temp.getModel() + " " + temp.getColor());
            }

            // POPULATE DROP DOWN
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, textFleets);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spVehicle.setAdapter(dataAdapter);

            if (fleets != null && fleets.size() > 0) {
                selectedFleetId = fleets.get(0);
            }

        } else if (entity instanceof AddFleetServiceResponseEntity) {

            AddFleetServiceResponseEntity resp = (AddFleetServiceResponseEntity) entity;

            MyLog.info("InformationActivity respEntity getResponseCode " + resp.getResponseCode());
            MyLog.info("InformationActivity respEntity getResponseType " + resp.getResponseType());
            MyLog.info("InformationActivity respEntity getResponseMessage " + resp.getResponseMessage());

            MyDialog.showDialogSucceed(this, MyDialog.DIALOG_ID_CLOSE, getString(R.string.title_servicefleet),
                    getString(R.string.text_succeed), getString(R.string.close), this);

        }
        Loading.cancelLoading();

    }

    public void showAlertDialogCancelClicked() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.cancel));
        builder.setMessage(getString(R.string.text_notif_cancel));
        // add the buttons
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) dialog.dismiss();
            }
        });
        // create and show the alert dialog
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showAlertDialogSaveClicked() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.save));
        builder.setMessage(getString(R.string.text_notif_save));
        // add the buttons
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) dialog.dismiss();
                Loading.showLoading(DriverFleetServiceActivity4.this, "Loading ...");
                callJSONAddFleetService();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) dialog.dismiss();
            }
        });
        // create and show the alert dialog
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        if (isFastDoubleClick()) return;

        if(view.getId() == R.id.btn_cancel) {
//            this.finish();
            showAlertDialogCancelClicked();
        } else if(view.getId() == R.id.btn_save) {

            int selectedId=categoryGroup.getCheckedRadioButtonId();
            categoryButton=(RadioButton)findViewById(selectedId);

            MyLog.info("DriverFleetServiceActivity fleedId "+selectedFleetId);
            MyLog.info("DriverFleetServiceActivity km "+etKm.getText().toString());
            MyLog.info("DriverFleetServiceActivity etKeluhan "+etKeluhan.getText().toString());
            MyLog.info("DriverFleetServiceActivity categoryButton "+categoryButton.getText());
            MyLog.info("DriverFleetServiceActivity camera1 "+bitmap1);
            MyLog.info("DriverFleetServiceActivity camera2 "+bitmap2);

            if(isValid()){
//                Loading.showLoading(this, "Loading ...");
//                callJSONAddFleetService();
                showAlertDialogSaveClicked();
            }

        } else if (view.getId() == R.id.iv_camera14) {

            cameraChoice = 0;
            clickedCamera = ivCamera1;

            Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.CAMERA)
//                    .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
//                                showImagePickerOptions();

                                dispatchTakePictureIntent();

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
        } else  if (view.getId() == R.id.iv_camera24) {

            cameraChoice = 1;
            clickedCamera = ivCamera2;

            Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.CAMERA)
//                    .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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

        } else {
            super.onClick(view);
        }
    }

    private boolean isValid(){

        if(GlobalHelper.isEmpty(selectedFleetId)){

            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,getString(R.string.title_servicefleet),
                    getString(R.string.alert_fleetid_empty), getString(R.string.ok), this );

            return false;
        }
        if(GlobalHelper.isEmpty(etKm.getText().toString())){

            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,getString(R.string.title_servicefleet),
                    getString(R.string.alert_km_aktual_empty), getString(R.string.ok), this );

            etKm.requestFocus();
            return false;
        }
        return true;
    }

    private void showImagePickerOptions() {
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

    private void launchCameraIntent() {

        dispatchTakePictureIntent();
    }

    private void launchGalleryIntent() {
//        Intent intent = new Intent(DriverFleetServiceActivity4.this, ImagePickerActivity.class);
//        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
//
//        // setting aspect ratio
//        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
//        startActivityForResult(intent, REQUEST_IMAGE);
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DriverFleetServiceActivity4.this);
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
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


}
