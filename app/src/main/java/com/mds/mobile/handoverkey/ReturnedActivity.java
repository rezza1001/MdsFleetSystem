package com.mds.mobile.handoverkey;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.mds.mobile.R;
import com.mds.mobile.absent.ErrorDialog;
import com.mds.mobile.absent.ImageChooserView;
import com.mds.mobile.base.Global;
import com.mds.mobile.model.UserProfile;
import com.mds.mobile.module.loading.Loading;
import com.mds.mobile.remote.ServiceGenerator;
import com.mds.mobile.remote.post.ErrorCode;
import com.mds.mobile.remote.post.PostManager;
import com.mds.mobile.remote.post.Utility;
import com.mds.mobile.ui.driver.secure.DriverBaseUi;
import com.mds.mobile.util.ViewToImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ReturnedActivity extends DriverBaseUi {

    private TextView txvw_date,txvw_time;
    private Spinner spnr_car;
    private ImageChooserView imvw_chooser;
    private EditText edtx_name;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id"));
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", new Locale("id"));
    private final SimpleDateFormat dateFormatApi = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("id"));

    private UserProfile userProfile;
    private Timer mTimer;
    private HashMap<Integer, String> fleetMap = new HashMap<>();

    @Override
    protected int getContentViewId() {
        return R.layout.handover_activity_recveived;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onMyCreate() {
        TextView txvw_title = findViewById(R.id.txvw_title);
        txvw_title.setText("Pengembalian Kunci");

        TextView txvw_titleName = findViewById(R.id.txvw_titleName);
        txvw_titleName.setText("Penerima Kunci");

        findViewById(R.id.rvly_back).setOnClickListener(v -> onBackPressed());

        txvw_date = findViewById(R.id.txvw_date);
        txvw_time = findViewById(R.id.txvw_time);
        spnr_car = findViewById(R.id.spnr_car);
        imvw_chooser = findViewById(R.id.imvw_chooser);
        edtx_name = findViewById(R.id.edtx_name);
        edtx_name.setHint("Nama penerima..");

        findViewById(R.id.btn_verification).setOnClickListener(v -> verification());
        userProfile = Global.userProfile;

        runTimer();
        initCar();

        imvw_chooser.post(() -> {
            int height = imvw_chooser.getHeight();
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imvw_chooser.getLayoutParams();
            lp.height = height;
            imvw_chooser.setLayoutParams(lp);
        });
        imvw_chooser.setPath(Global.PATH_IMAGES);
    }

    @Override
    protected Integer getNavigationMenuItemIndex() {
        return null;
    }

    @Override
    protected void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }

    private void initCar(){
        PostManager post = new PostManager(this, ServiceGenerator.HANDOVER_LIST_CAR);
        post.addHeaderParam("x_rentas_key", ServiceGenerator.API_SECRET_KEY);
        post.addHeaderParam("user_id", userProfile.getUserId());
        post.exGet();
        post.setOnReceiveListener((obj, code, message) -> {
            ArrayList<String> listCar = new ArrayList<>();
            if (code == ErrorCode.OK_200){
                try {
                    JSONObject jo = obj.getJSONObject("data");
                    JSONArray fleet = jo.getJSONArray("fleet");
                    JSONObject detail = jo.getJSONObject("detail");
                    JSONArray merk = detail.getJSONArray("merk");
                    JSONArray model = detail.getJSONArray("model");
                    JSONArray color = detail.getJSONArray("color");

                    for (int i=0; i<fleet.length(); i++){

                        String car = fleet.getString(i)+" "+merk.getString(i)+" "+model.getString(i)+" "+color.getString(i);
                        listCar.add(car);
                        fleetMap.put(i, fleet.getString(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                ErrorDialog dialog = new ErrorDialog(this);
                dialog.show("Gagal",message);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,  android.R.layout.simple_spinner_dropdown_item, listCar);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnr_car.setAdapter(adapter);
            spnr_car.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        });
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

    private void initTime(){
        Date mDate = new Date();
        txvw_date.setText(dateFormat.format(mDate));
        txvw_time.setText(timeFormat.format(mDate));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imvw_chooser.registerActivityResult(requestCode, resultCode, data);
    }

    private void verification(){
        String name = edtx_name.getText().toString();
        if (name.isEmpty()){
            Utility.showToastError(this,"Nama pemberi kunci harus diisi");
            return;
        }
        if (!imvw_chooser.hasImage()){
            Utility.showToastError(this,"Photo penerima kunci harus diisi");
            return;
        }


        ViewToImage fileImage = new ViewToImage(this,Global.PATH_ABSENT,imvw_chooser.getViewImage(),System.currentTimeMillis()+"");

        PostManager post = new PostManager(this, ServiceGenerator.HANDOVER_RETURN);
        post.addParam("x_rentas_key", ServiceGenerator.API_SECRET_KEY2);
        JSONObject data = new JSONObject();
        try {
            data.put("user_id",userProfile.getUserId());
            data.put("time",dateFormatApi.format(new Date()));
            data.put("pic", name);
            data.put("fleet", fleetMap.get(spnr_car.getSelectedItemPosition()));
            data.put("note", "");
            data.put("photo", fileImage.getBase64String());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        post.addParam("data",data);
        post.exPost();
        post.setOnReceiveListener((obj, code, message) -> {
            if (code == ErrorCode.OK_200){
                Utility.showToastSuccess(this,"Pengembalian Kunci Berhasil");
                finish();
            }
            else {
                ErrorDialog dialog = new ErrorDialog(this);
                dialog.show("Gagal",message);
            }
        });
    }
}
