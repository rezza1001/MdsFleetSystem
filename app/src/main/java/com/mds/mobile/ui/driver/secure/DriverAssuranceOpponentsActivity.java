package com.mds.mobile.ui.driver.secure;

import androidx.annotation.Nullable;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import com.mds.mobile.R;
import com.mds.mobile.base.Global;
import com.mds.mobile.module.dialog.MyDialog;
import com.mds.mobile.module.loading.Loading;
import com.mds.mobile.module.mylog.MyLog;
import com.mds.mobile.remote.entity.driverinsurance.request.AddFleetInsuranceClaimRequestEntity;
import com.mds.mobile.ui.imagepicker.ImagePickerActivity;
import com.mds.mobile.util.GlobalHelper;
import retrofit2.Call;

public class DriverAssuranceOpponentsActivity extends DriverAssuranceBaseActivity {

    String fleetId;
    String fleetDetail;
    String typeInsurance;

    Button btnSave;
    Button btnCancel;
    EditText etDateAccident;
    EditText etTimeAccident;

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private Calendar c;
    private Context ctx = this;

    EditText etDriverName;
    EditText etDriverPhone;
    EditText etLocation;
    EditText etSpeed;
    EditText etKronology;

    ImageView ivFotoKendaraan1;
    ImageView ivFotoKendaraan2;
    ImageView ivFotoKendaraan3;
    ImageView ivFotoSimPengendara;
    ImageView ivFotoSimLawan;
    ImageView ivFotoStnk;
    ImageView ivFotoStnkLawan;
    ImageView ivFotoNoRangka;
    ImageView ivFotoKepolisian;
    ImageView ivFotoTuntutan;

    ImageView clickedCamera;

    Bitmap bmpKendaraan1;
    Bitmap bmpKendaraan2;
    Bitmap bmpKendaraan3;
    Bitmap bmpSimPengendara;
    Bitmap bmpSimLawan;
    Bitmap bmpStnk;
    Bitmap bmpStnkLawan;
    Bitmap bmpNoRangka;
    Bitmap bmpKepolisian;
    Bitmap bmpTuntutan;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_driver_assurance_opponents;
    }

    @Override
    protected Integer getNavigationMenuItemIndex() {
        return null;
    }

    @Override
    protected void onMyCreate() {

        Intent intent = getIntent();
        fleetId = intent.getStringExtra("fleet_id");
        fleetDetail = intent.getStringExtra("fleet_string");
        typeInsurance = intent.getStringExtra("type_insurance");

        TextView tvFleetId = findViewById(R.id.tv_fleet_id);
        TextView tvTypeInsurance = findViewById(R.id.tv_type_insurance);
        tvFleetId.setText(fleetDetail);
        tvTypeInsurance.setText(typeInsurance);

        etDriverName = findViewById(R.id.et_driver_name);
        etDriverPhone = findViewById(R.id.et_driver_phone);
        etLocation = findViewById(R.id.et_location_accident);
        etSpeed = findViewById(R.id.et_speed);
        etKronology = findViewById(R.id.et_kronology);

        btnCancel = findViewById(R.id.btn_cancel);
        btnSave = findViewById(R.id.btn_save);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        etDateAccident = findViewById(R.id.et_date_accident);
        etDateAccident.setOnClickListener(this);
        etTimeAccident = findViewById(R.id.et_time_accident);
        etTimeAccident.setOnClickListener(this);

        mYear= Calendar.getInstance().get(Calendar.YEAR);
        mMonth=Calendar.getInstance().get(Calendar.MONTH)+1;
        mDay=Calendar.getInstance().get(Calendar.DAY_OF_MONTH) ;
        mHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) ;
        mMinute = Calendar.getInstance().get(Calendar.MINUTE);

        ivFotoKendaraan1 = findViewById(R.id.iv_foto_kendaraan1);
        ivFotoKendaraan1.setOnClickListener(this);
        ivFotoKendaraan2 = findViewById(R.id.iv_foto_kendaraan2);
        ivFotoKendaraan2.setOnClickListener(this);
        ivFotoKendaraan3 = findViewById(R.id.iv_foto_kendaraan3);
        ivFotoKendaraan3.setOnClickListener(this);
        ivFotoSimPengendara = findViewById(R.id.iv_foto_sim_pengemudi);
        ivFotoSimPengendara.setOnClickListener(this);
        ivFotoSimLawan = findViewById(R.id.iv_foto_sim_lawan);
        ivFotoSimLawan.setOnClickListener(this);
        ivFotoStnk = findViewById(R.id.iv_foto_stnk);
        ivFotoStnk.setOnClickListener(this);
        ivFotoStnkLawan = findViewById(R.id.iv_foto_stnk_lawan);
        ivFotoStnkLawan.setOnClickListener(this);
        ivFotoNoRangka = findViewById(R.id.iv_foto_no_rangka);
        ivFotoNoRangka.setOnClickListener(this);
        ivFotoKepolisian = findViewById(R.id.iv_foto_kepolisian);
        ivFotoKepolisian.setOnClickListener(this);
        ivFotoTuntutan = findViewById(R.id.iv_foto_tuntutan);
        ivFotoTuntutan.setOnClickListener(this);

        // Clearing older images from cache directory
        // don't call this line if you want to choose multiple images in the same activity
        // call this once the bitmap(s) usage is over
        ImagePickerActivity.clearCache(this);

    }

    private boolean isValid(){

        if(GlobalHelper.isEmpty(etDriverName.getText().toString())){

            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,getString(R.string.title_assuranceclaim),
                    "Nama Pengemudi harus diisi.", getString(R.string.ok), this );

            etDriverName.requestFocus();
            return false;
        }
        if(GlobalHelper.isEmpty(etDriverPhone.getText().toString())){

            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,getString(R.string.title_assuranceclaim),
                    "No.Telp. Pengemudi harus diisi.", getString(R.string.ok), this );

            etDriverPhone.requestFocus();
            return false;
        }
        if(GlobalHelper.isEmpty(etDateAccident.getText().toString())){

            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,getString(R.string.title_assuranceclaim),
                    "Tanggal Kejadian harus diisi.", getString(R.string.ok), this );

            return false;
        }
        if(GlobalHelper.isEmpty(etTimeAccident.getText().toString())){

            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,getString(R.string.title_assuranceclaim),
                    "Jam Kejadian harus diisi.", getString(R.string.ok), this );

            return false;
        }
        if(GlobalHelper.isEmpty(etLocation.getText().toString())){

            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,getString(R.string.title_assuranceclaim),
                    "Lokasi Kejadian harus diisi.", getString(R.string.ok), this );

            etLocation.requestFocus();
            return false;
        }
        if(GlobalHelper.isEmpty(etSpeed.getText().toString())){

            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,getString(R.string.title_assuranceclaim),
                    "Kecepatan harus diisi.", getString(R.string.ok), this );

            etSpeed.requestFocus();
            return false;
        }
        if(GlobalHelper.isEmpty(etKronology.getText().toString())){

            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,getString(R.string.title_assuranceclaim),
                    "Kronologis harus diisi.", getString(R.string.ok), this );

            etKronology.requestFocus();
            return false;
        }
        // image
        if(bmpKendaraan1==null
                || bmpKendaraan2==null
                || bmpKendaraan3==null
                || bmpSimPengendara==null
                || bmpSimLawan==null
                || bmpStnk==null
                || bmpStnkLawan==null
                || bmpNoRangka==null
                || bmpKepolisian==null
                || bmpTuntutan==null
        ){

            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,getString(R.string.title_assuranceclaim),
                    "Foto harus diisi.", getString(R.string.ok), this );

            return false;
        }
        return true;
    }

    public void showAlertDialogCancelClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showAlertDialogSaveClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.save));
        builder.setMessage(getString(R.string.text_notif_save));
        // add the buttons
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) dialog.dismiss();
                Loading.showLoading(DriverAssuranceOpponentsActivity.this, "Loading ...");
                callJSONAddFleetInsurance();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) dialog.dismiss();
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View view) {

        if (isFastDoubleClick()) return;

        if(view.getId() == R.id.btn_cancel) {
//            this.finish();
            showAlertDialogCancelClicked();
        } else if(view.getId() == R.id.btn_save) {

            if(isValid()){
                // action save
//                Loading.showLoading(this, "Loading ...");
//                callJSONAddFleetInsurance();
                showAlertDialogSaveClicked();
            }

        } else if(view.getId() == R.id.et_date_accident) {
            show_Datepicker();
        } else  if(view.getId() == R.id.et_time_accident) {
            show_Timepicker();
        } else if (view.getId() == R.id.iv_foto_kendaraan1) {
            clickedCamera = ivFotoKendaraan1;
            runningCamera();
        } else if (view.getId() == R.id.iv_foto_kendaraan2) {
            clickedCamera = ivFotoKendaraan2;
            runningCamera();
        } else if (view.getId() == R.id.iv_foto_kendaraan3) {
            clickedCamera = ivFotoKendaraan3;
            runningCamera();
        } else if (view.getId() == R.id.iv_foto_sim_pengemudi) {
            clickedCamera = ivFotoSimPengendara;
            runningCamera();
        } else if (view.getId() == R.id.iv_foto_sim_lawan) {
            clickedCamera = ivFotoSimLawan;
            runningCamera();
        } else if (view.getId() == R.id.iv_foto_stnk) {
            clickedCamera = ivFotoStnk;
            runningCamera();
        } else if (view.getId() == R.id.iv_foto_stnk_lawan) {
            clickedCamera = ivFotoStnkLawan;
            runningCamera();
        } else if (view.getId() == R.id.iv_foto_no_rangka) {
            clickedCamera = ivFotoNoRangka;
            runningCamera();
        } else if (view.getId() == R.id.iv_foto_kepolisian) {
            clickedCamera = ivFotoKepolisian;
            runningCamera();
        } else if (view.getId() == R.id.iv_foto_tuntutan) {
            clickedCamera = ivFotoTuntutan;
            runningCamera();
        } else {
            super.onClick(view);
        }
    }

    private void callJSONAddFleetInsurance() {

        String strKendaraan1="-";
        String strKendaraan2="-";
        String strKendaraan3="-";
        String strSimPengendara="-";
        String strSimLawan="-";
        String strStnk="-";
        String strStnkLawan="-";
        String strNoRangka="-";
        String strKepolisian="-";
        String strTuntutan="-";

        if(bmpKendaraan1!=null){
            strKendaraan1 = GlobalHelper.bitmapToBase64String(bmpKendaraan1,100);
        }
        if(bmpKendaraan2!=null){
            strKendaraan2 = GlobalHelper.bitmapToBase64String(bmpKendaraan2,100);
        }
        if(bmpKendaraan3!=null){
            strKendaraan3 = GlobalHelper.bitmapToBase64String(bmpKendaraan3,100);
        }
        if(bmpSimPengendara!=null){
            strSimPengendara = GlobalHelper.bitmapToBase64String(bmpSimPengendara,100);
        }
        if(bmpSimLawan!=null){
            strSimLawan = GlobalHelper.bitmapToBase64String(bmpSimLawan,100);
        }
        if(bmpStnk!=null){
            strStnk = GlobalHelper.bitmapToBase64String(bmpStnk,100);
        }
        if(bmpStnkLawan!=null){
            strStnkLawan = GlobalHelper.bitmapToBase64String(bmpStnkLawan,100);
        }
        if(bmpNoRangka!=null){
            strNoRangka = GlobalHelper.bitmapToBase64String(bmpNoRangka,100);
        }
        if(bmpKepolisian!=null){
            strKepolisian = GlobalHelper.bitmapToBase64String(bmpKepolisian,100);
        }
        if(bmpTuntutan!=null){
            strTuntutan = GlobalHelper.bitmapToBase64String(bmpTuntutan,100);
        }

        AddFleetInsuranceClaimRequestEntity ent = new AddFleetInsuranceClaimRequestEntity();
        ent.setRequestType("user_fleet_add_insurance_claim");
        ent.setAuthorize("RT006");
        ent.setUserCode(getUserProfile().getUserCode());
        ent.setFleetId(fleetId);
        ent.setClaimType(typeInsurance);
        ent.setDriverName(etDriverName.getText().toString());
        ent.setDriverPhone(etDriverPhone.getText().toString());
//        yyyy-MM-dd HH:mm:ss
        String incidentDate = mYear +"-"+mMonth+"-"+mDay +" "+ mHour +":"+mMinute+":00";
        ent.setIncidentDate(incidentDate);
        ent.setIncidentLocation(etLocation.getText().toString());
        ent.setIncidentSpeed(etSpeed.getText().toString());
        ent.setDamageDescription("-");
        ent.setIncidentDescription(etKronology.getText().toString());
        ent.setIncidentPhoto1(strKendaraan1);
        ent.setIncidentPhoto2(strKendaraan2);
        ent.setIncidentPhoto3(strKendaraan3);
        ent.setSimPhoto1(strSimPengendara);
        ent.setSimPhoto2(strSimLawan);
        ent.setStnkPhoto1(strStnk);
        ent.setStnkPhoto2(strStnkLawan);
        ent.setChassisNoPhoto1(strNoRangka);
        ent.setLetterPolicePhoto1(strKepolisian);
        ent.setLetterWarrantPhoto1(strTuntutan);

        super.callJSONAddFleetInsurance(ent);
    }

    private void show_Datepicker() {
        c = Calendar.getInstance();
        int mYearParam = mYear;
        int mMonthParam = mMonth-1;
        int mDayParam = mDay;

        DatePickerDialog datePickerDialog = new DatePickerDialog(ctx,
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mMonth = monthOfYear + 1;
                    mYear=year;
                    mDay=dayOfMonth;

                    etDateAccident.setText(mDay +"/" + mMonth +"/" +mYear);

                }
            }, mYearParam, mMonthParam, mDayParam);

        datePickerDialog.show();
    }

    private void show_Timepicker() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(ctx,
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int pHour,
                                      int pMinute) {

                    mHour = pHour;
                    mMinute = pMinute;

                    etTimeAccident.setText(mHour + ":" + mMinute);

                }
            }, mHour, mMinute, true);

        timePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    if(clickedCamera!=null){
                        if(clickedCamera.equals(ivFotoKendaraan1)){
                            MyLog.info("BITMAP DI ivFotoKendaraan1");
                            bmpKendaraan1 = bitmap;
                        } else if(clickedCamera.equals(ivFotoKendaraan2)){
                            MyLog.info("BITMAP DI ivFotoKendaraan2");
                            bmpKendaraan2 = bitmap;
                        } else if(clickedCamera.equals(ivFotoKendaraan3)){
                            MyLog.info("BITMAP DI ivFotoKendaraan3");
                            bmpKendaraan3 = bitmap;
                        } else if(clickedCamera.equals(ivFotoSimPengendara)){
                            MyLog.info("BITMAP DI ivFotoSimPengendara");
                            bmpSimPengendara = bitmap;
                        } else if(clickedCamera.equals(ivFotoSimLawan)){
                            MyLog.info("BITMAP DI ivFotoSimLawan");
                            bmpSimLawan = bitmap;
                        } else if(clickedCamera.equals(ivFotoStnk)){
                            MyLog.info("BITMAP DI ivFotoStnk");
                            bmpStnk = bitmap;
                        } else if(clickedCamera.equals(ivFotoStnkLawan)){
                            MyLog.info("BITMAP DI ivFotoStnkLawan");
                            bmpStnkLawan = bitmap;
                        } else if(clickedCamera.equals(ivFotoNoRangka)){
                            MyLog.info("BITMAP DI ivFotoNoRangka");
                            bmpNoRangka = bitmap;
                        } else if(clickedCamera.equals(ivFotoKepolisian)){
                            MyLog.info("BITMAP DI ivFotoKepolisian");
                            bmpKepolisian = bitmap;
                        } else if(clickedCamera.equals(ivFotoTuntutan)){
                            MyLog.info("BITMAP DI ivFotoTuntutan");
                            bmpTuntutan = bitmap;
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

}
