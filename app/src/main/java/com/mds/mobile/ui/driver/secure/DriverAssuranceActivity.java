package com.mds.mobile.ui.driver.secure;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

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
import com.mds.mobile.remote.service.DriverServiceClient;
import retrofit2.Call;

public class DriverAssuranceActivity extends DriverBaseUi implements AdapterView.OnItemSelectedListener {

    Spinner spTypeAssurance;
    Spinner spVehicle;
    Button btnSave;
    Button btnCancel;
    List<String> fleets;
    List<String> textFleets;
    List<String> categories;
    int selectedTypeAssurance = 0;
    String selectedFleetId;
//    String selectedFleetDetail;

    String fleetIdBundle=null;
    String fleetContentBundle=null;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_driver_assurance;
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

        MyLog.info("onMyCreate() , fleetIdBundle: "+fleetIdBundle);
        MyLog.info("onMyCreate() , fleetContentBundle: "+fleetContentBundle);

        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        spVehicle = findViewById(R.id.sp_vehicle_as);
        spTypeAssurance = findViewById(R.id.sp_type_assurance);
        spVehicle.setOnItemSelectedListener(this);
        spTypeAssurance.setOnItemSelectedListener(this);

        categories = new ArrayList<String>();
        categories.add("Tunggal");
        categories.add("Dengan Lawan");
        categories.add("Kehilangan / Pengrusakan");
        categories.add("Bencana Alam");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spTypeAssurance.setAdapter(dataAdapter2);

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

    @Override
    public void onClick(View view) {

        if (isFastDoubleClick()) return;

        if(view.getId() == R.id.btn_cancel) {
//            this.finish();
            showAlertDialogCancelClicked();
        } else if(view.getId() == R.id.btn_save) {

            String selectedStrType = categories.get(selectedTypeAssurance);
            MyLog.info("DriverAssuranceActivity fleedId "+selectedFleetId);
            MyLog.info("DriverAssuranceActivity type "+selectedStrType);

            // next screen
            Intent intent;
            switch (selectedTypeAssurance) {
                case 0 :
                    intent = new Intent(getApplicationContext(), DriverAssuranceSingleActivity.class);
                    intent.putExtra("fleet_id", selectedFleetId);
                    intent.putExtra("fleet_string", spVehicle.getSelectedItem().toString());
                    intent.putExtra("type_insurance", selectedStrType);
                    startActivity(intent);
                    break;
                case 1 :
                    intent = new Intent(getApplicationContext(), DriverAssuranceOpponentsActivity.class);
                    intent.putExtra("fleet_id", selectedFleetId);
                    intent.putExtra("fleet_string", spVehicle.getSelectedItem().toString());
                    intent.putExtra("type_insurance", selectedStrType);
                    startActivity(intent);
                    break;
                case 2 :
                    intent = new Intent(getApplicationContext(), DriverAssuranceLossActivity.class);
                    intent.putExtra("fleet_id", selectedFleetId);
                    intent.putExtra("fleet_string", spVehicle.getSelectedItem().toString());
                    intent.putExtra("type_insurance", selectedStrType);
                    startActivity(intent);
                    break;
                case 3 :
                    intent = new Intent(getApplicationContext(), DriverAssuranceForceNatureActivity.class);
                    intent.putExtra("fleet_id", selectedFleetId);
                    intent.putExtra("fleet_string", spVehicle.getSelectedItem().toString());
                    intent.putExtra("type_insurance", selectedStrType);
                    startActivity(intent);
                    break;
            }


        } else {
            super.onClick(view);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        MyLog.info("position "+position);
        MyLog.info("view "+view.getId());
        MyLog.info("parent "+parent.getId());
        MyLog.info("id "+id);
        MyLog.info("R.id.sp_vehicle "+R.id.sp_vehicle_as);
        MyLog.info("R.id.sp_type_assurance "+R.id.sp_type_assurance);

        if(parent.getId() == R.id.sp_vehicle_as) {
            selectedFleetId = fleets.get(position);
        } else if(parent.getId() == R.id.sp_type_assurance) {
            selectedTypeAssurance = position;
        }

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

    @Override
    protected void onErrorReceived(ApplicationError applicationError) {
        Loading.cancelLoading();

        MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT, getString(R.string.title_assuranceclaim),
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


        }

        Loading.cancelLoading();

    }

}
