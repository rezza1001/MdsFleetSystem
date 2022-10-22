package com.mds.mobile.ui.driver.secure;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

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
import com.mds.mobile.base.Global;
import com.mds.mobile.model.ApplicationError;
import com.mds.mobile.module.dialog.MyDialog;
import com.mds.mobile.module.loading.Loading;
import com.mds.mobile.module.mylog.MyLog;
import com.mds.mobile.remote.ServiceGenerator;
import com.mds.mobile.remote.entity.customerinstallment.response.DetailResponseEntity;
import com.mds.mobile.remote.entity.customerinstallment.response.InstallmentResponseEntity;
import com.mds.mobile.remote.entity.drivervehiclelist.request.GetFleetRequestEntity;
import com.mds.mobile.remote.entity.drivervehiclelist.response.GetFleetResponseEntity;
import com.mds.mobile.remote.entity.drivervehiclelist.response.ResponseDatum;
import com.mds.mobile.remote.entity.odometer.request.AddFleetOdometerRequestEntity;
import com.mds.mobile.remote.entity.odometer.response.AddFleetOdometerResponseEntity;
import com.mds.mobile.remote.service.DriverServiceClient;
import com.mds.mobile.ui.imagepicker.ImagePickerActivity;
import com.mds.mobile.util.GlobalHelper;
import retrofit2.Call;

public class DriverUpdateKmActivity extends DriverBaseUi implements AdapterView.OnItemSelectedListener {

    Spinner spVehicle;
    TextView tvInsertDate;
    EditText etKm;
    List<String> fleets;
    List<String> textFleets;
    Button btnSave;
    Button btnCancel;

    String selectedFleetId;

    String fleetIdBundle=null;
    String fleetContentBundle=null;

    ImageView ivFotoKm;
    ImageView clickedCamera;
    Bitmap bmpKm;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_driver_update_km;
    }

    @Override
    protected Integer getNavigationMenuItemIndex() {
        return null;
    }

    @Override
    protected void onMyCreate() {

        MyLog.info("onMyCreate()");

        Intent intent = getIntent();
        fleetIdBundle = intent.getStringExtra("fleet_id");
        fleetContentBundle = intent.getStringExtra("fleet_content");

        MyLog.info("onMyCreate() , fleetIdBundle: "+fleetIdBundle);
        MyLog.info("onMyCreate() , fleetContentBundle: "+fleetContentBundle);

        spVehicle = findViewById(R.id.sp_vehicle);
        tvInsertDate = findViewById(R.id.tv_insertdate);
        etKm = findViewById(R.id.et_km);
        btnCancel = findViewById(R.id.btn_cancel);
        btnSave = findViewById(R.id.btn_save);

        spVehicle.setOnItemSelectedListener(this);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        ivFotoKm = findViewById(R.id.iv_foto_km);
        ivFotoKm.setOnClickListener(this);

        SimpleDateFormat ddMMMyyyy = new SimpleDateFormat("dd-MMM-yyyy", new Locale("in", "ID"));

        tvInsertDate.setText(ddMMMyyyy.format(new Date()));

        if(fleetIdBundle==null || fleetIdBundle.isEmpty()){
            Loading.showLoading(this, "Loading ...");
            callJSONGetFleet();
        } else {
            MyLog.info("Call Intent form other Intent");

            selectedFleetId = fleetIdBundle;
            fleets = new ArrayList<String>();
            fleets.add(fleetIdBundle);
            textFleets = new ArrayList<String>();
            textFleets.add(fleetContentBundle);

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, textFleets);
            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // attaching data adapter to spinner
            spVehicle.setAdapter(dataAdapter);
        }

        ImagePickerActivity.clearCache(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedFleetId = fleets.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
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
                Loading.showLoading(DriverUpdateKmActivity.this, "Loading ...");
                callJSONAddFleetOdometer();
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

            MyLog.info("DriverUpdateKmActivity fleedId "+selectedFleetId);
            MyLog.info("DriverUpdateKmActivity odometer "+etKm.getText().toString());

            if(isValid()){
//                Loading.showLoading(this, "Loading ...");
//                callJSONAddFleetOdometer();
                showAlertDialogSaveClicked();
            }
        } else if (view.getId() == R.id.iv_foto_km) {
            clickedCamera = ivFotoKm;
            runningCamera();

        } else {
            super.onClick(view);
        }
    }

    private boolean isValid(){

        if(GlobalHelper.isEmpty(selectedFleetId)){

            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,getString(R.string.title_updatekm),
                    getString(R.string.alert_fleetid_empty), getString(R.string.ok), this );

            return false;
        }
        if(GlobalHelper.isEmpty(etKm.getText().toString())){

            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,getString(R.string.title_updatekm),
                    getString(R.string.alert_odometer_empty), getString(R.string.ok), this );

            etKm.requestFocus();
            return false;
        }
        // image
        if(bmpKm==null
        ){
            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,getString(R.string.title_updatekm),
                    "Foto harus diisi.", getString(R.string.ok), this );

            return false;
        }

        return true;
    }

    private void callJSONGetFleet() {

        GetFleetRequestEntity ent = new GetFleetRequestEntity();
        ent.setAuthorize("RT006");
        ent.setUserCode(getUserProfile().getUserId());
        ent.setRequestType("user_fleet");

        MyLog.debug("getUserProfile().getUserCode() "+getUserProfile().getUserCode());

        DriverServiceClient serviceClient = ServiceGenerator.createService(DriverServiceClient.class);

        Call<GetFleetResponseEntity> call = serviceClient.getFleet(ent);
        call.enqueue(callback);
    }

    private void callJSONAddFleetOdometer() {

        String strBitmap1="";
        if(bmpKm!=null){
            strBitmap1 = GlobalHelper.bitmapToBase64String(bmpKm,100);
        }

//        MyLog.info("BITMAP*********** KM: "+strBitmap1);

//        System.out.print("ADIKRISNA: "+strBitmap1);

        AddFleetOdometerRequestEntity ent = new AddFleetOdometerRequestEntity();
        ent.setAuthorize("RT006");
        ent.setUserCode(getUserProfile().getUserCode());
        ent.setRequestType("user_fleet_add_odometer");
//        ent.setFleetId(spVehicle.getSelectedItem().toString());
        ent.setFleetId(selectedFleetId);
        ent.setFleetOdometer(etKm.getText().toString());
        ent.setPhoto1(strBitmap1);

        DriverServiceClient serviceClient = ServiceGenerator.createService(DriverServiceClient.class);

        Call<AddFleetOdometerResponseEntity> call = serviceClient.addFleetOdometer(ent);
        call.enqueue(callback);
    }

    @Override
    protected void onErrorReceived(ApplicationError applicationError) {
        Loading.cancelLoading();

        MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT, getString(R.string.title_updatekm),
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
                textFleets.add(temp.getLicensePlate() + " " + temp.getMerk() + " " + temp.getModel() +" "+ temp.getColor());
            }

            if(fleets!=null && fleets.size()>0){
                selectedFleetId = fleets.get(0);
            }

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, textFleets);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spVehicle.setAdapter(dataAdapter);


        } else if (entity instanceof AddFleetOdometerResponseEntity) {

            AddFleetOdometerResponseEntity resp = (AddFleetOdometerResponseEntity) entity;

            MyLog.info("InformationActivity respEntity getResponseCode " + resp.getResponseCode());
            MyLog.info("InformationActivity respEntity getResponseType " + resp.getResponseType());
            MyLog.info("InformationActivity respEntity getResponseMessage " + resp.getResponseMessage());

            MyDialog.showDialogSucceed(this, MyDialog.DIALOG_ID_CLOSE, getString(R.string.title_updatekm),
                    getString(R.string.text_succeed), getString(R.string.close), this);

        }

        Loading.cancelLoading();

    }

    public static final int REQUEST_IMAGE = 100;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    if(clickedCamera!=null){
                        if(clickedCamera.equals(ivFotoKm)){
                            MyLog.info("BITMAP DI ivFotoKm");
                            bmpKm = bitmap;
                        }
                    }

//                    MyLog.info("BITMAP : "+bitmap.toString());

                    // loading profile image from local cache
                    // adi.ori
//                    loadProfile(uri.toString());
                    loadProfile(uri, clickedCamera);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void loadProfile(Uri uri, ImageView ivCamera) {
        MyLog.info("Image cache path: " + uri.toString());

        // adi.ori
//        Glide.with(this).load(url)
//                .into(imgProfile);

        File bitmapFile = new File(uri.getPath());

        Glide.with(this)
                .load(bitmapFile)
                .signature(new ObjectKey(bitmapFile.lastModified()))
                .into(ivCamera);

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

}
