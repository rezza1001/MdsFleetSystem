package com.mds.mobile.handoverkey;

import android.content.Intent;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;

import com.mds.mobile.R;
import com.mds.mobile.absent.ErrorDialog;
import com.mds.mobile.absent.PermissionAccess;
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
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainHandoverActivity extends DriverBaseUi {

    private TextView txvw_date,txvw_time,txvw_dateIn,txvw_inTime,txvw_car1,txvw_give,
            txvw_outDate,txvw_outTime,txvw_car2,txvw_receiver;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id"));
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", new Locale("id"));
    private final SimpleDateFormat dateFormatApi = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("id"));

    private Timer mTimer;
    private UserProfile userProfile;
    private HandoverStatus mStatus;

    @Override
    protected int getContentViewId() {
        return R.layout.hanover_activity_main;
    }

    @Override
    protected void onMyCreate() {
        PermissionAccess.requestMain(this);
        FileProcessing.createFolder(this, Global.PATH_IMAGES);
        FileProcessing.clearImage(this,Global.PATH_IMAGES);

        txvw_date = findViewById(R.id.txvw_date);
        txvw_time = findViewById(R.id.txvw_time);
        txvw_dateIn = findViewById(R.id.txvw_dateIn);
        txvw_inTime = findViewById(R.id.txvw_inTime);
        txvw_car1 = findViewById(R.id.txvw_car1);
        txvw_give = findViewById(R.id.txvw_give);
        txvw_outDate = findViewById(R.id.txvw_outDate);
        txvw_outTime = findViewById(R.id.txvw_outTime);
        txvw_car2 = findViewById(R.id.txvw_car2);
        txvw_receiver = findViewById(R.id.txvw_receiver);

        txvw_inTime.setText("-");
        txvw_dateIn.setText("-");
        txvw_give.setText("-");
        txvw_car1.setText("-");
        txvw_outDate.setText("-");
        txvw_outTime.setText("-");
        txvw_car2.setText("-");
        txvw_receiver.setText("-");
        mStatus = HandoverStatus.NOT_RECEIVED;

        userProfile = getUserProfile();

        if (isTimeAutomatic()){
            runTimer();
        }

        Button btn_checkIn = findViewById(R.id.btn_checkIn);
        btn_checkIn.setOnClickListener(v -> receivedKey());

        Button btn_checkOut = findViewById(R.id.btn_checkOut);
        btn_checkOut.setOnClickListener(v -> returnedKey());
    }

    @Override
    protected Integer getNavigationMenuItemIndex() {
        return null;
    }

    private void initTime(){
        if (!isTimeAutomatic()){
            showSettingTimeAlert();
        }
        Date mDate = new Date();
        txvw_date.setText(dateFormat.format(mDate));
        txvw_time.setText(timeFormat.format(mDate));
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
            MainHandoverActivity.this.finish();
        });
        builder.setNegativeButton("Batal", (dialog, which) -> {
            if (dialog != null) dialog.dismiss();
            MainHandoverActivity.this.finish();
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
    protected void onResume() {
        super.onResume();
        checkIsAbsentIn();
    }

    @Override
    protected void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }

    private void receivedKey(){
        if (mStatus == HandoverStatus.RECEIVED){
            ErrorDialog dialog = new ErrorDialog(this);
            dialog.show("Tidak Dapat diproses","Anda sudah mengambil kunci kendaraan");
            return;
        }
        if (mStatus == HandoverStatus.RETURNED){
            ErrorDialog dialog = new ErrorDialog(this);
            dialog.show("Tidak Dapat diproses","Anda sudah melakukan pengembalian kunci untuk hari ini");
            return;
        }
        startActivityForResult(new Intent(this, ReceivedActivity.class),1);
    }

    private void returnedKey(){
        if (mStatus == HandoverStatus.NOT_RECEIVED){
            ErrorDialog dialog = new ErrorDialog(this);
            dialog.show("Tidak Dapat diproses","Anda belum mengambil kunci kendaraan");
            return;
        }
        if (mStatus == HandoverStatus.RETURNED){
            ErrorDialog dialog = new ErrorDialog(this);
            dialog.show("Tidak Dapat diproses","Anda sudah melakukan pengembalian kunci untuk hari ini");
            return;
        }
        startActivityForResult(new Intent(this, ReturnedActivity.class),1);
    }



    private void checkStatus(){
        PostManager post = new PostManager(this, ServiceGenerator.HANDOVER_STATUS);
        post.addHeaderParam("x_rentas_key", ServiceGenerator.API_SECRET_KEY);
        post.addHeaderParam("user_id", userProfile.getUserId());
        post.addHeaderParam("date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        post.exGet();
        post.setOnReceiveListener((obj, code, message) -> {
            if (code == ErrorCode.OK_200){
                try {
                    JSONObject data = obj.getJSONObject("data");
                    JSONObject Jtalking  = data.getJSONObject("key_taking");
                    if (!Jtalking.getString("time").isEmpty()){

                        mStatus = HandoverStatus.RECEIVED;
                        txvw_car1.setText(Jtalking.getString("fleet"));
                        txvw_give.setText(Jtalking.getString("pic"));
                        Date date = dateFormatApi.parse(Jtalking.getString("time"));
                        if (date != null){
                            txvw_dateIn.setText(dateFormat.format(date));
                            txvw_inTime.setText(timeFormat.format(date));
                        }
                    }

                    JSONObject returned  = data.getJSONObject("key_return");
                    if (!returned.getString("time").isEmpty()){

                        mStatus = HandoverStatus.RETURNED;
                        txvw_car2.setText(returned.getString("fleet"));
                        txvw_receiver.setText(returned.getString("pic"));
                        Date date = dateFormatApi.parse(returned.getString("time"));
                        if (date != null){
                            txvw_outDate.setText(dateFormat.format(date));
                            txvw_outTime.setText(timeFormat.format(date));
                        }
                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void checkIsAbsentIn() {
        PostManager post = new PostManager(this, ServiceGenerator.ABSENT_STATUS);
        post.addHeaderParam("x_rentas_key", ServiceGenerator.API_SECRET_KEY);
        post.addHeaderParam("user_id", userProfile.getUserId());
        post.addHeaderParam("date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        post.exGet();
        post.setOnReceiveListener((obj, code, message) -> {
            boolean isAbsent = false;
            if (code == ErrorCode.OK_200){
                try {
                    JSONObject  data = obj.getJSONObject("data");
                    JSONObject work = data.getJSONObject("start_work");
                    if (!work.getString("time").isEmpty()){
//                        if (work.getString("status").equalsIgnoreCase("Approved")){
//                            isAbsent = true;
//                        }
                        isAbsent = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (!isAbsent){
                ErrorDialog dialog = new ErrorDialog(this);
                dialog.show("Gagal","Anda belum melakukan absensi. Silahkan absensi terlebih dahulu!");
                dialog.setOnFinishListener(MainHandoverActivity.this::finish);
            }
            else {
                checkStatus();
            }

        });
    }
}
