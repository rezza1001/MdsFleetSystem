package com.mds.mobile.absent;

import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.mds.mobile.R;
import com.mds.mobile.base.Global;
import com.mds.mobile.model.UserProfile;
import com.mds.mobile.remote.ServiceGenerator;
import com.mds.mobile.remote.post.ErrorCode;
import com.mds.mobile.remote.post.FileProcessing;
import com.mds.mobile.remote.post.PostManager;
import com.mds.mobile.ui.driver.secure.DriverBaseUi;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AbsentActivity extends DriverBaseUi {

    private Spinner spnr_type;
    private TextView txvw_date,txvw_time,txvw_timeIn,txvw_timeOut,
            txvw_dateIn, txvw_inTime,txvw_location,txvw_outDate,txvw_outTime,txvw_outLocation,
            txvw_outAddress,txvw_inAddress;
    private RelativeLayout rvly_status;
    private CardView card_absentIn,card_absentOut;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id"));
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", new Locale("id"));
    private final SimpleDateFormat dateFormatApi = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("id"));

    private Timer mTimer;
    private final AbsentParam absentParam = new AbsentParam();
    private AbsentStatus absentStatus = AbsentStatus.NO_ABSENT;
    private UserProfile userProfile;

    @Override
    protected int getContentViewId() {
        return R.layout.absent_activity_main;
    }

    @Override
    protected void onMyCreate() {
        findViewById(R.id.rvly_back).setOnClickListener(v -> onBackPressed());

        PermissionAccess.requestMain(this);
        FileProcessing.createFolder(this, Global.PATH_IMAGES);
        FileProcessing.clearImage(this,Global.PATH_IMAGES);

        spnr_type = findViewById(R.id.spnr_type);
        txvw_date = findViewById(R.id.txvw_date);
        txvw_time = findViewById(R.id.txvw_time);
        txvw_dateIn = findViewById(R.id.txvw_dateIn);
        txvw_inTime = findViewById(R.id.txvw_inTime);
        txvw_timeIn = findViewById(R.id.txvw_timeIn);
        rvly_status = findViewById(R.id.rvly_status);
        txvw_timeOut = findViewById(R.id.txvw_timeOut);
        txvw_location = findViewById(R.id.txvw_location);
        txvw_outDate = findViewById(R.id.txvw_outDate);
        txvw_outTime = findViewById(R.id.txvw_outTime);
        txvw_outLocation = findViewById(R.id.txvw_outLocation);
        txvw_outAddress = findViewById(R.id.txvw_outAddress);
        txvw_inAddress = findViewById(R.id.txvw_inAddress);
        card_absentIn = findViewById(R.id.card_absentIn);
        card_absentOut = findViewById(R.id.card_absentOut);
        txvw_date.setTextColor(Color.WHITE);
        txvw_time.setTextColor(Color.WHITE);

        txvw_dateIn.setText("-");
        txvw_inTime.setText("-");
        txvw_location.setText("-");
        txvw_outDate.setText("-");
        txvw_outTime.setText("-");
        txvw_outLocation.setText("-");
        txvw_outAddress.setText("-");
        txvw_inAddress.setText("-");
        absentStatus = AbsentStatus.NO_ABSENT;
        card_absentIn.getChildAt(0).setVisibility(View.GONE);
        card_absentIn.getChildAt(1).setVisibility(View.VISIBLE);
        card_absentOut.getChildAt(0).setVisibility(View.GONE);
        card_absentOut.getChildAt(1).setVisibility(View.VISIBLE);

        TextView txvw_name = findViewById(R.id.txvw_name);
        txvw_name.setText("-");
        TextView txvw_mail = findViewById(R.id.txvw_mail);
        txvw_mail.setText("-");
        userProfile = Global.userProfile;
        if (userProfile != null){
            txvw_name.setText(userProfile.getName());
            txvw_mail.setText(userProfile.getPhoneNo() == null ? userProfile.getUserRole() : userProfile.getPhoneNo());
        }

        initType();
        initTime();
        requestParam();

        if (isTimeAutomatic()){
            runTimer();
        }

        Button btn_checkIn = findViewById(R.id.btn_checkIn);
        btn_checkIn.setOnClickListener(v -> absentIn());

        Button btn_checkOut = findViewById(R.id.btn_checkOut);
        btn_checkOut.setOnClickListener(v -> absentOut());
    }

    @Override
    protected Integer getNavigationMenuItemIndex() {
        return null;
    }

    private void initType(){
        String [] absentType =  getResources().getStringArray(R.array.absent_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.absent_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_type.setAdapter(adapter);
        spnr_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                absentParam.absentPosition = absentType[position];
                absentParam.inOffice = position == 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initTime(){
        if (!isTimeAutomatic()){
            showSettingTimeAlert();
        }
        Date mDate = new Date();
        txvw_date.setText(dateFormat.format(mDate));
        txvw_time.setText(timeFormat.format(mDate));

        if (mDate.after(absentParam.workStart) && mDate.before(absentParam.workEnd)){
            rvly_status.setBackgroundColor(Color.parseColor("#ac0800"));
        }
        else {
            rvly_status.setBackgroundColor(Color.parseColor("#00c853"));
        }
    }

    private boolean isTimeAutomatic() {
        return Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
    }

    public void showSettingTimeAlert() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Pengaturan Waktu");
        builder.setMessage("Pengaturan waktu anda tidak Otomatis, Silahkan ubah pengaturan waktu");
        builder.setPositiveButton("Pengaturan", (dialog, which) -> {
            if (dialog != null) dialog.dismiss();
            startActivityForResult(new Intent(Settings.ACTION_DATE_SETTINGS), 1);
            AbsentActivity.this.finish();
        });
        builder.setNegativeButton("Batal", (dialog, which) -> {
            if (dialog != null) dialog.dismiss();
            AbsentActivity.this.finish();
        });
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void runTimer(){
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> initTime());
            }
        },1000,1000);

    }

    @Override
    protected void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkIsAbsentIn();
    }

    private void absentIn(){
        if (absentStatus == AbsentStatus.ABSENT_IN){
            ErrorDialog dialog = new ErrorDialog(this);
            dialog.show("Gagal","Anda belum melakukan absensi pulang. Silahkan melakukan absensi pulang");
            return;
        }
        if (absentStatus == AbsentStatus.ABSENT_OUT){
            ErrorDialog dialog = new ErrorDialog(this);
            dialog.show("Gagal","Anda sudah melakukan absensi untuk hari ini");
            return;
        }
        absentParam.absentStatusValue = "Absen Masuk";
        absentParam.absentStatus = AbsentStatus.ABSENT_IN;
        Intent intent = new Intent(this, VerificationActivity.class);
        intent.putExtra("data", absentParam);
        startActivityForResult(intent,1);
    }

    private void absentOut(){
        if (absentStatus == AbsentStatus.NO_ABSENT){
            ErrorDialog dialog = new ErrorDialog(this);
            dialog.show("Gagal","Anda belum melakukan absensi masuk. Silahkan melakukan absensi masuk");
            return;
        }
        if (absentStatus == AbsentStatus.ABSENT_OUT){
            ErrorDialog dialog = new ErrorDialog(this);
            dialog.show("Gagal","Anda sudah melakukan absensi untuk hari ini");
            return;
        }
        absentParam.absentStatus = AbsentStatus.ABSENT_OUT;
        absentParam.absentStatusValue = "Absen Pulang";
        Intent intent = new Intent(this, VerificationActivity.class);
        intent.putExtra("data", absentParam);
        startActivityForResult(intent,1);
    }

    private void requestParam(){
        PostManager post = new PostManager(this, ServiceGenerator.ABSENT_PARAM);
        post.addHeaderParam("x_rentas_key",ServiceGenerator.API_SECRET_KEY);
        post.addHeaderParam("user_id",userProfile.getUserId());
        post.exGet();
        post.setOnReceiveListener((obj, code, message) -> {
            if (code == ErrorCode.OK_200){
                try {
                    JSONObject data = obj.getJSONObject("data");
                    String workStart = data.getString("work_start");
                    String workOut = "17:00";

                    txvw_timeIn.setText(workStart);
                    absentParam.workStart = getTime(workStart);
                    txvw_timeOut.setText(workOut);
                    absentParam.workEnd = getTime(workOut);
                    JSONObject location = data.getJSONObject("location");
                    absentParam.longitudeOrigin = Double.parseDouble(location.getString("longitude"));
                    absentParam.latitudeOrigin = Double.parseDouble(location.getString("latitude"));
                    absentParam.radius = data.getInt("radius");

                    initTime();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                ErrorDialog dialog = new ErrorDialog(this);
                dialog.show("System Bermasalah","Silahkan coba beberapa saat lagi atau hubungi call center");
                dialog.setOnFinishListener(AbsentActivity.this::finish);
            }
        });
    }

    private Date getTime(String time){
        Calendar calendar = Calendar.getInstance();
        int hour = Integer.parseInt(time.split(":")[0]);
        int minute = Integer.parseInt(time.split(":")[1]);
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        return calendar.getTime();
    }

    private void checkIsAbsentIn(){
        PostManager post  = new PostManager(this,ServiceGenerator.ABSENT_STATUS);
        post.addHeaderParam("x_rentas_key",ServiceGenerator.API_SECRET_KEY);
        post.addHeaderParam("user_id",userProfile.getUserId());
        post.addHeaderParam("date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        post.exGet();
        post.setOnReceiveListener((obj, code, message) -> {
            if (code == ErrorCode.OK_200){
                String [] absentType =  getResources().getStringArray(R.array.absent_type);
                try {
                    JSONObject data = obj.getJSONObject("data");
                    {
                        card_absentIn.getChildAt(0).setVisibility(View.VISIBLE);
                        card_absentIn.getChildAt(1).setVisibility(View.GONE);
                        absentStatus = AbsentStatus.ABSENT_IN;
                        JSONObject work = data.getJSONObject("start_work");
                        JSONObject position = work.getJSONObject("position");
                        Date inTime = dateFormatApi.parse(work.getString("time"));
                        if (inTime != null){
                            txvw_dateIn.setText(dateFormat.format(inTime));
                            txvw_inTime.setText(timeFormat.format(inTime));
                        }
                        txvw_location.setText(work.getString("location").equalsIgnoreCase("In Office") ?absentType[0] : absentType[1] );
                        txvw_inAddress.setText(position.getString("address"));
                    }
                    {
                        card_absentOut.getChildAt(0).setVisibility(View.VISIBLE);
                        card_absentOut.getChildAt(1).setVisibility(View.GONE);
                        JSONObject work = data.getJSONObject("end_work");
                        if (work.getString("time").isEmpty()){
                            return;
                        }
                        absentStatus = AbsentStatus.ABSENT_OUT;
                        JSONObject position = work.getJSONObject("position");
                        Date inTime = dateFormatApi.parse(work.getString("time"));
                        if (inTime != null){
                            txvw_outDate.setText(dateFormat.format(inTime));
                            txvw_outTime.setText(timeFormat.format(inTime));
                        }
                        txvw_outLocation.setText(work.getString("location").equalsIgnoreCase("In Office") ?absentType[0] : absentType[1] );
                        txvw_outAddress.setText(position.getString("address"));
                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        });
//
    }
}
