package com.mds.mobile.absent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.mds.mobile.R;
import com.mds.mobile.base.Global;
import com.mds.mobile.model.UserProfile;
import com.mds.mobile.remote.ServiceGenerator;
import com.mds.mobile.remote.post.ErrorCode;
import com.mds.mobile.remote.post.PostManager;
import com.mds.mobile.remote.post.Utility;
import com.mds.mobile.ui.driver.secure.DriverBaseUi;
import com.mds.mobile.util.GPSTracker;
import com.mds.mobile.util.ViewToImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class VerificationActivity extends DriverBaseUi {

    private TextView txvw_type,txvw_date,txvw_time,txvw_location,tvw_gps,txvw_note;
    private LinearLayout lnly_photo;
    private Date mDate;
    private Timer mTimer;
    private ImageChooserView imvw_chooser;
    private Button btn_verification;
    private EditText edtx_note;
    private CheckBox checkbox_usedCar;
    private UserProfile userProfile;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id"));
    private final SimpleDateFormat watermarkFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("id"));
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", new Locale("id"));
    private final SimpleDateFormat dateFormatApi = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("id"));

    private AbsentParam absentParam;

    @Override
    protected int getContentViewId() {
        return R.layout.absent_activity_verification;
    }

    @Override
    protected void onMyCreate() {
        txvw_type = findViewById(R.id.txvw_type);
        txvw_date = findViewById(R.id.txvw_date);
        txvw_time = findViewById(R.id.txvw_time);
        tvw_gps             = findViewById(R.id.tvw_gps);
        lnly_photo          = findViewById(R.id.lnly_photo);
        txvw_location       = findViewById(R.id.txvw_location);
        imvw_chooser        = findViewById(R.id.imvw_chooser);
        txvw_note           = findViewById(R.id.txvw_note);
        btn_verification    = findViewById(R.id.btn_verification);
        edtx_note           = findViewById(R.id.edtx_note);
        checkbox_usedCar    = findViewById(R.id.checkbox_usedCar);
        findViewById(R.id.rvly_back).setOnClickListener(v -> onBackPressed());
        checkbox_usedCar.setVisibility(View.GONE);
        imvw_chooser.post(() -> {
           int height = imvw_chooser.getHeight();
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imvw_chooser.getLayoutParams();
            lp.height = height;
            imvw_chooser.setLayoutParams(lp);
        });

        btn_verification.setOnClickListener(v -> {
            if (absentParam.absentStatus == AbsentStatus.ABSENT_OUT){
                if (checkbox_usedCar.getVisibility() == View.VISIBLE){
                    if (!checkbox_usedCar.isChecked()){
                        ErrorDialog dialog = new ErrorDialog(this);
                        dialog.show("Gagal","Anda belum mengembalikan kunci kendaraan.");
                        return;
                    }
                }
            }

            if (lnly_photo.getVisibility() == View.GONE){
                verificationOffice();
                         }
            else {
                verificationOutOffice();
            }
        });

        init();
    }

    @Override
    protected Integer getNavigationMenuItemIndex() {
        return null;
    }

    public void init(){
        userProfile = Global.userProfile;
        absentParam = (AbsentParam) getIntent().getSerializableExtra("data");

        imvw_chooser.setPath(Global.PATH_IMAGES);
        mDate = new Date();
        String typeValue = "Informasi "+absentParam.absentStatusValue;
        txvw_type.setText(typeValue);
        txvw_location.setText(absentParam.absentPosition);
        runTimer();
        initTime();

        if (absentParam.inOffice){
            lnly_photo.setVisibility(View.GONE);
            senEnableButton(false);
        }
        else {
            lnly_photo.setVisibility(View.VISIBLE);
            senEnableButton(true);
        }

        if (absentParam.absentStatus == AbsentStatus.ABSENT_OUT){
            checkReturnKeyStatus();
        }
    }

    private void initTime(){
        mDate = new Date();
        txvw_date.setText(dateFormat.format(mDate));
        txvw_time.setText(timeFormat.format(mDate));
        if (absentParam.absentStatus == AbsentStatus.ABSENT_IN){
            checkInLate();
        }
        else {
            checkOutLate();
        }
    }

    private void runTimer(){
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    GPSTracker gpsTracker = new GPSTracker(VerificationActivity.this);
                    tvw_gps.setText(gpsTracker.getAddress());
                    imvw_chooser.setWaterMark(gpsTracker.getAddress()
                            +" | "+gpsTracker.getLatitude()+" "+gpsTracker.getLongitude()
                            +" | "+watermarkFormat.format(mDate));
                    if (lnly_photo.getVisibility() == View.GONE){
                        calculateDistance();
                    }
                    absentParam.latitudeLive = gpsTracker.getLatitude();
                    absentParam.longitudeLive = gpsTracker.getLongitude();
                    absentParam.address = gpsTracker.getAddress();

                    initTime();
                });
            }
        },0,2000);

    }

    private void calculateDistance(){
        if (absentParam.latitudeLive == 0){
            return;
        }
        double distance = calcDistance(absentParam.latitudeOrigin, absentParam.longitudeOrigin, absentParam.latitudeLive,absentParam.longitudeLive);
        senEnableButton(distance < absentParam.radius);
    }

    @Override
    protected void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imvw_chooser.registerActivityResult(requestCode, resultCode, data);
    }

    private void senEnableButton(boolean enable){
        if (enable){
            btn_verification.setTag(1);
            btn_verification.setBackgroundResource(R.drawable.ripple_button);
            btn_verification.setTextColor(Color.WHITE);
        }
        else {
            btn_verification.setTag(0);
            btn_verification.setBackgroundResource(R.drawable.ripple_button_disable);
            btn_verification.setTextColor(Color.DKGRAY);
        }
    }

    private void verificationOffice(){
        if (btn_verification.getTag().toString().equals("1")){
            PostManager post = new PostManager(this, ServiceGenerator.ABSENT_IN);
            if (absentParam.absentStatus == AbsentStatus.ABSENT_OUT){
                post = new PostManager(this,  ServiceGenerator.ABSENT_OUT);
            }
            post.addParam("x_rentas_key",ServiceGenerator.API_SECRET_KEY2);
            try {
                JSONObject data = new JSONObject();
                data.put("user_id",userProfile.getUserId());
                data.put("time",dateFormatApi.format(mDate));
                data.put("location","In Office");
                data.put("note",edtx_note.getText().toString());
                data.put("photo","");
                JSONObject position = new JSONObject();
                position.put("longitude",absentParam.longitudeLive);
                position.put("latitude",absentParam.latitudeLive);
                position.put("address",absentParam.address);
                data.put("position",position);

                post.addParam("data",data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            post.exPost();
            post.setOnReceiveListener((obj, code, message) -> {
                if (code == ErrorCode.OK_200){
                    Utility.showToastSuccess(this,"Absensi berhasil");
                    finish();
                }
                else {
                    ErrorDialog dialog = new ErrorDialog(this);
                    dialog.show("Gagal","Gagal melakukan absensi\n"+message);
                }
            });
        }
        else {
            ErrorDialog dialog = new ErrorDialog(this);
            dialog.show("Gagal","Anda berada di luar kantor. Lakukan absensi di dalam kantor");
        }
    }
    private void verificationOutOffice(){
        if (!imvw_chooser.hasImage()){
            ErrorDialog dialog = new ErrorDialog(this);
            dialog.show("Gagal","Anda belum mengambil photo absensi. Pastikan wajah anda terlihat jelas saat photo absensi");
            return;
        }
        ViewToImage fileImage = new ViewToImage(this,Global.PATH_ABSENT,imvw_chooser.getViewImage(),System.currentTimeMillis()+"");
        PostManager post = new PostManager(this,  ServiceGenerator.ABSENT_IN);
        if (absentParam.absentStatus == AbsentStatus.ABSENT_OUT){
            post = new PostManager(this,  ServiceGenerator.ABSENT_OUT);
        }
        post.addParam("x_rentas_key",ServiceGenerator.API_SECRET_KEY2);
        try {
            JSONObject data = new JSONObject();
            data.put("user_id",userProfile.getUserId());
            data.put("time",dateFormatApi.format(mDate));
            data.put("location","Out Office");
            data.put("note",edtx_note.getText().toString());
            data.put("photo",fileImage.getBase64String());
                JSONObject position = new JSONObject();
                position.put("longitude",absentParam.longitudeLive);
                position.put("latitude",absentParam.latitudeLive);
                position.put("address",absentParam.address);
            data.put("position",position);

            post.addParam("data",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        post.exPost();
        post.setOnReceiveListener((obj, code, message) -> {
            if (code == ErrorCode.OK_200){
                Utility.showToastSuccess(this,"Absensi berhasil");
                finish();
            }
            else {
                ErrorDialog dialog = new ErrorDialog(this);
                dialog.show("Gagal","Gagal melakukan absensi\n"+message);
            }
        });
    }



    @SuppressLint("SetTextI18n")
    private void checkInLate(){
        if (mDate.after(absentParam.workStart) && mDate.before(absentParam.workEnd)){
            txvw_note.setText("Alasan Keterlambatan");
            edtx_note.setVisibility(View.VISIBLE);
            txvw_note.setVisibility(View.VISIBLE);
        }
        else {
            txvw_note.setVisibility(View.GONE);
            edtx_note.setVisibility(View.GONE);
        }
    }
    @SuppressLint("SetTextI18n")
    private void checkOutLate(){
        if (mDate.after(absentParam.workStart) && mDate.before(absentParam.workEnd)){
            txvw_note.setText("Alasan Pulang");
            edtx_note.setVisibility(View.VISIBLE);
            txvw_note.setVisibility(View.VISIBLE);
        }
        else {
            txvw_note.setVisibility(View.GONE);
            edtx_note.setVisibility(View.GONE);
        }
    }

    private double calcDistance(double lat1, double lon1, double lat2, double lon2) {
        double distance;
        Location point1 = new Location("locationA");
        point1.setLatitude(lat1);
        point1.setLongitude(lon1);

        Location point2 = new Location("locationB");
        point2.setLatitude(lat2);
        point2.setLongitude(lon2);

        Log.d(VerificationActivity.class.getSimpleName(),"point1 "+ lat1+","+lon1+"  to "+lat2+","+lon2);

        distance = point1.distanceTo(point2);
        return distance;
    }

    private void checkReturnKeyStatus() {
        PostManager post = new PostManager(this, ServiceGenerator.HANDOVER_STATUS);
        post.addHeaderParam("x_rentas_key", ServiceGenerator.API_SECRET_KEY);
        post.addHeaderParam("user_id", userProfile.getUserId());
        post.addHeaderParam("date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        post.exGet();
        post.setOnReceiveListener((obj, code, message) -> {
            if (code == ErrorCode.OK_200){
                try {
                    JSONObject data = obj.getJSONObject("data");
                    JSONObject returned  = data.getJSONObject("key_return");
                    if (returned.getString("time").isEmpty()){
                        checkbox_usedCar.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
