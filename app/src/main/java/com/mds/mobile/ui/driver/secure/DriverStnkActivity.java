package com.mds.mobile.ui.driver.secure;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.mds.mobile.remote.entity.drivervehiclelist.request.GetFleetRequestEntity;
import com.mds.mobile.remote.entity.drivervehiclelist.response.GetFleetResponseEntity;
import com.mds.mobile.remote.entity.drivervehiclelist.response.ResponseDatum;
import com.mds.mobile.remote.entity.fleetservice.response.AddFleetServiceResponseEntity;
import com.mds.mobile.remote.entity.stnk.request.AddFleetTaxRequestEntity;
import com.mds.mobile.remote.entity.stnk.response.AddFleetTaxResponseEntity;
import com.mds.mobile.remote.service.DriverServiceClient;
import com.mds.mobile.util.GlobalHelper;
import retrofit2.Call;

public class DriverStnkActivity extends DriverBaseUi implements AdapterView.OnItemSelectedListener{

//    List<String> fleets;
    List<String> textFleets;
    List<ResponseDatum> listFleets;

    ResponseDatum selectedResponseDatum;
//    String selectedFleetId;
//    String selectedExpiredStnk;

    Spinner spTypeStnk;
    Spinner spVehicle;
    Button btnSave;
    Button btnCancel;
    TextView tvExpiredStnk;
    EditText etKronology;
    EditText etDateSent;
    private int mYear;
    private int mMonth;
    private int mDay;

    private Calendar c;
    private Context ctx = this;

    String fleetIdBundle=null;
    String fleetContentBundle=null;
    String fleetStnkExpireBundle=null;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_driver_stnk;
    }

    @Override
    protected Integer getNavigationMenuItemIndex() {
        return null;
    }

    @Override
    protected void onMyCreate() {

        Intent intent = getIntent();
        fleetIdBundle = intent.getStringExtra("fleet_id");
        fleetContentBundle = intent.getStringExtra("fleet_content");
        fleetStnkExpireBundle = intent.getStringExtra("fleet_stnk");

        MyLog.info("onMyCreate() , fleetIdBundle: "+fleetIdBundle);
        MyLog.info("onMyCreate() , fleetContentBundle: "+fleetContentBundle);

        spTypeStnk = findViewById(R.id.sp_type_stnk);
        spVehicle = findViewById(R.id.sp_vehicle);
        btnCancel = findViewById(R.id.btn_cancel);
        btnSave = findViewById(R.id.btn_save);
        tvExpiredStnk = findViewById(R.id.tv_expired_stnk);
        etKronology = findViewById(R.id.et_kronology);
        etDateSent = findViewById(R.id.et_date_sent);

        etDateSent.setOnClickListener(this);
        spTypeStnk.setOnItemSelectedListener(this);
        spVehicle.setOnItemSelectedListener(this);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        mYear= Calendar.getInstance().get(Calendar.YEAR);
        mMonth=Calendar.getInstance().get(Calendar.MONTH)+1;
        mDay=Calendar.getInstance().get(Calendar.DAY_OF_MONTH) ;

        List<String> categories2 = new ArrayList<String>();
        categories2.add("STNK");
        categories2.add("Pajak");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTypeStnk.setAdapter(dataAdapter2);

        if(fleetIdBundle==null || fleetIdBundle.isEmpty()){
            Loading.showLoading(this, "Loading ...");
            callJSONGetFleet();
        } else {
            MyLog.info("Call Intent form other Intent");

            listFleets = new ArrayList<ResponseDatum>();
            ResponseDatum rd = new ResponseDatum();
            rd.setFleetId(fleetIdBundle);
            rd.setStnkExpireDate(fleetStnkExpireBundle);
            listFleets.add(rd);
            selectedResponseDatum = rd;

            textFleets = new ArrayList<String>();
            textFleets.add(fleetContentBundle);

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, textFleets);
            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // attaching data adapter to spinner
            spVehicle.setAdapter(dataAdapter);
        }

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

    @Override
    protected void onErrorReceived(ApplicationError applicationError) {
        Loading.cancelLoading();

        MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT, getString(R.string.title_stnk),
                applicationError.getMessage(), getString(R.string.ok), this);
    }

    @Override
    protected void onSuccessReceived(Object entity) {

        if (entity instanceof GetFleetResponseEntity) {
            GetFleetResponseEntity resp = (GetFleetResponseEntity) entity;

            MyLog.info("InformationActivity respEntity getResponseCode " + resp.getResponseCode());
            MyLog.info("InformationActivity respEntity getResponseType " + resp.getResponseType());
            MyLog.info("InformationActivity respEntity getResponseMessage " + resp.getResponseMessage());

            listFleets = resp.getResponseData();
//            fleets = new ArrayList<String>();
            textFleets = new ArrayList<String>();
            for (ResponseDatum temp : listFleets) {
                MyLog.info("Temp : " + temp.getFleetId());
//                fleets.add(temp.getFleetId());
                textFleets.add(temp.getLicensePlate() + " " + temp.getMerk() + " " + temp.getModel() + " " + temp.getColor());
            }

            // POPULATE DROP DOWN
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, textFleets);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spVehicle.setAdapter(dataAdapter);

            if (listFleets != null && listFleets.size() > 0) {
//                selectedFleetId = fleets.get(0);
                selectedResponseDatum = listFleets.get(0);
            }

        } else if (entity instanceof AddFleetTaxResponseEntity) {

            AddFleetTaxResponseEntity resp = (AddFleetTaxResponseEntity) entity;

            MyLog.info("DriverStnkActivity respEntity getResponseCode " + resp.getResponseCode());
            MyLog.info("DriverStnkActivity respEntity getResponseType " + resp.getResponseType());
            MyLog.info("DriverStnkActivity respEntity getResponseMessage " + resp.getResponseMessage());

            MyDialog.showDialogSucceed(this, MyDialog.DIALOG_ID_CLOSE, getString(R.string.title_stnk),
                    getString(R.string.text_succeed), getString(R.string.close), this);

        }
        Loading.cancelLoading();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.sp_vehicle) {

            selectedResponseDatum = listFleets.get(position);
            tvExpiredStnk.setText(selectedResponseDatum.getStnkExpireDate());

            MyLog.info("Temp id : " + selectedResponseDatum.getFleetId());
            MyLog.info("Temp expired : " + selectedResponseDatum.getStnkExpireDate());

        } else if(parent.getId() == R.id.sp_type_stnk) {

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.save));
        builder.setMessage(getString(R.string.text_notif_save));
        // add the buttons
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) dialog.dismiss();
                Loading.showLoading(DriverStnkActivity.this, "Loading ...");
                callJSONAddFleetTax();
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

            MyLog.info("DriverStnkActivity fleedId "+selectedResponseDatum.getFleetId());
            MyLog.info("DriverStnkActivity expired "+selectedResponseDatum.getStnkExpireDate());
            MyLog.info("DriverStnkActivity stnk type "+spTypeStnk.getSelectedItem().toString());

            if(isValid()){
                // action save
//                Loading.showLoading(this, "Loading ...");
//                callJSONAddFleetTax();
                showAlertDialogSaveClicked();
            }
        } else if(view.getId() == R.id.et_date_sent) {
            show_Datepicker();
        } else {
            super.onClick(view);
        }
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

                        etDateSent.setText(mDay +"/" + mMonth +"/" +mYear);

                    }
                }, mYearParam, mMonthParam, mDayParam);

        datePickerDialog.show();
    }

    private boolean isValid(){

        if(GlobalHelper.isEmpty(etDateSent.getText().toString())){

            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,getString(R.string.title_stnk),
                    "Tanggal Kirim Dokumen harus diisi.", getString(R.string.ok), this );

            return false;
        }
        if(GlobalHelper.isEmpty(etKronology.getText().toString())){

            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT ,getString(R.string.title_stnk),
                    "Keterangan / No. Resi harus diisi.", getString(R.string.ok), this );

            etKronology.requestFocus();
            return false;
        }
        return true;
    }

    protected void callJSONAddFleetTax() {

//            "request_type": "user_fleet_add_tax",
//            "authorize": "RT006",
//            "user_code": "U0002",
//            "fleet_id": "B 2478 PFN",
//            "tax_type": "Pajak",
//            "date_expired_tax": "2020-04-23",
//            "date_submitted_doc": "2019-10-07",
//            "description": "tes"

        String submitedDate = mYear +"-"+mMonth+"-"+mDay;

        AddFleetTaxRequestEntity ent = new AddFleetTaxRequestEntity();
        ent.setRequestType("user_fleet_add_tax");
        ent.setAuthorize("RT006");
        ent.setUserCode(getUserProfile().getUserCode());
        ent.setFleetId(selectedResponseDatum.getFleetId());
        ent.setTaxType(spTypeStnk.getSelectedItem().toString());
        ent.setDateExpiredTax(selectedResponseDatum.getStnkExpireDate());
        ent.setDateSubmittedDoc(submitedDate);
        ent.setDescription(etKronology.getText().toString());

        MyLog.info("getRequestType : "+ent.getRequestType());
        MyLog.info("getAuthorize : "+ent.getAuthorize());
        MyLog.info("getUserCode : "+ent.getUserCode());
        MyLog.info("getFleetId : "+ent.getFleetId());
        MyLog.info("getTaxType : "+ent.getTaxType());
        MyLog.info("getDateExpiredTax : "+ent.getDateExpiredTax());
        MyLog.info("getDateSubmittedDoc : "+ent.getDateSubmittedDoc());
        MyLog.info("getDescription : "+ent.getDescription());

        DriverServiceClient serviceClient = ServiceGenerator.createService(DriverServiceClient.class);
        Call<AddFleetTaxResponseEntity> call = serviceClient.addFleetTax(ent);
        call.enqueue(callback);
    }

}
