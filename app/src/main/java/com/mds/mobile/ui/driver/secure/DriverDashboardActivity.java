package com.mds.mobile.ui.driver.secure;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mds.mobile.R;
import com.mds.mobile.absent.AbsentActivity;
import com.mds.mobile.handoverkey.MainHandoverActivity;

public class DriverDashboardActivity extends DriverBaseUi implements View.OnClickListener{

    ImageView ivKm;
    ImageView ivService;
    ImageView ivAssurance;
    ImageView ivStnk;
    ImageView ivVehicle;
    ImageView ivReport;
    ImageView ivContactUs;
    ImageView ivProfile;
    TextView tvUsername;
    RelativeLayout rvly_absent,rvly_handoverKey;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_driver_dashboard;
    }

    @Override
    protected Integer getNavigationMenuItemIndex() {
        return null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onMyCreate() {

        tvUsername = findViewById(R.id.tvUsername);
        tvUsername.setText("Hi, "+getUserProfile().getName());

        ivKm = findViewById(R.id.iv_km);
        ivService = findViewById(R.id.iv_service);
        ivAssurance = findViewById(R.id.iv_assurance);
        ivStnk = findViewById(R.id.iv_stnk);
        ivVehicle = findViewById(R.id.iv_vehicle);
        ivReport = findViewById(R.id.iv_report);
        ivContactUs = findViewById(R.id.iv_contact_us);
        ivProfile = findViewById(R.id.iv_profile);
        rvly_absent = findViewById(R.id.rvly_absent);
        rvly_handoverKey = findViewById(R.id.rvly_handoverKey);

        ivKm.setOnClickListener(this);
        ivService.setOnClickListener(this);
        ivAssurance.setOnClickListener(this);
        ivStnk.setOnClickListener(this);
        ivVehicle.setOnClickListener(this);
        ivReport.setOnClickListener(this);
        ivContactUs.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        rvly_absent.setOnClickListener(this);
        rvly_handoverKey.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (isFastDoubleClick()) return;

        if (view.getId() == R.id.iv_km){
            startActivity(new Intent(getApplicationContext(), DriverUpdateKmActivity.class));
        } else if (view.getId() == R.id.iv_service){
            startActivity(new Intent(getApplicationContext(), DriverFleetServiceActivity.class));
        } else if (view.getId() == R.id.iv_assurance){
            startActivity(new Intent(getApplicationContext(), DriverAssuranceActivity.class));
        } else if (view.getId() == R.id.iv_stnk){
            startActivity(new Intent(getApplicationContext(), DriverStnkActivity.class));
        } else if (view.getId() == R.id.iv_vehicle){
            startActivity(new Intent(getApplicationContext(), DriverFleetListActivity.class));
        } else if (view.getId() == R.id.iv_report){
            startActivity(new Intent(getApplicationContext(), DriverReportActivity.class));
        } else if (view.getId() == R.id.iv_contact_us){
            startActivity(new Intent(getApplicationContext(), DriverContactUsActivity.class));
        } else if (view.getId() == R.id.iv_profile){
            startActivity(new Intent(getApplicationContext(), DriverProfileActivity.class));
        } else if (view.getId() == R.id.rvly_absent){
            startActivity(new Intent(getApplicationContext(), AbsentActivity.class));
        } else if (view.getId() == R.id.rvly_handoverKey){
            startActivity(new Intent(getApplicationContext(), MainHandoverActivity.class));
        } else {
            super.onClick(view);
        }
    }

}
