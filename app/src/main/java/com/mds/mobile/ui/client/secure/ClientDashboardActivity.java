package com.mds.mobile.ui.client.secure;

import android.content.Intent;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.mds.mobile.R;
import com.mds.mobile.model.ApplicationError;
import com.mds.mobile.module.dialog.MyDialog;
import com.mds.mobile.module.loading.Loading;
import com.mds.mobile.module.mylog.MyLog;
import com.mds.mobile.module.slideindicator.SliderAdapter;
import com.mds.mobile.remote.ServiceGenerator;
import com.mds.mobile.remote.entity.drivervehiclelist.request.GetFleetRequestEntity;
import com.mds.mobile.remote.entity.drivervehiclelist.response.GetFleetResponseEntity;
import com.mds.mobile.remote.entity.promo.request.GetPromoRequestEntity;
import com.mds.mobile.remote.entity.promo.response.GetPromoResponseEntity;
import com.mds.mobile.remote.entity.promo.response.ResponseDatum;
import com.mds.mobile.remote.service.ClientServiceClient;
import com.mds.mobile.remote.service.DriverServiceClient;
import retrofit2.Call;

public class ClientDashboardActivity extends ClientBaseUi implements View.OnClickListener{

    ImageView ivInfo;
    ImageView ivPromo;
    ImageView ivContactUs;
    ImageView ivProfile;
    TextView tvUsername;
    ViewPager viewPager;
    TabLayout indicator;

    static List<String> imgResources = null;
    static List<ResponseDatum> imgData = null;
//    static int isGetPromoDone = 0;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_client_dashboard;
    }
    @Override
    protected Integer getNavigationMenuItemIndex() {
        return null;
    }

    @Override
    protected void onMyCreate() {

        // dashboard menu
        tvUsername = findViewById(R.id.tvUsername);
        tvUsername.setText("Hi, "+getUserProfile().getName());
        ivInfo = findViewById(R.id.iv_info_client);
        ivPromo = findViewById(R.id.iv_promo);
        ivContactUs = findViewById(R.id.iv_contactus);
        ivProfile = findViewById(R.id.iv_profile);
        ivInfo.setOnClickListener(this);
        ivPromo.setOnClickListener(this);
        ivContactUs.setOnClickListener(this);
        ivProfile.setOnClickListener(this);

        // slide show
        viewPager = (ViewPager) findViewById(R.id.vp_slider);
        indicator = (TabLayout) findViewById(R.id.tl_slider);

//        imgResources = new ArrayList<>();
//        imgResources.add("http://117.53.46.75/mncleasing-carservices/middleware/uploads/promo/promo01.png");
//        imgResources.add("http://117.53.46.75/mncleasing-carservices/middleware/uploads/promo/promo02.png");
//
//        viewPager.setAdapter(new SliderAdapter(this, imgResources));
//        indicator.setupWithViewPager(viewPager, true);
//
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);



//        this.runOnUiThread(new Runnable() {
//            public void run() {
//                MyLog.info("UI thread - I am the UI thread");

                //        Loading.showLoading(this, "Loading ...");


                if(imgResources!=null)  {
                   MyLog.info("not nullllllllllllllllllllllllllll");

                    viewPager.setAdapter(new SliderAdapter(this, imgResources, imgData));
                    indicator.setupWithViewPager(viewPager, true);

                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);

                } else {
                    MyLog.info("GET PROMOOOOOOOOOOOOOo");
                    Loading.showLoading(this, "Loading ...");
                    callJSONGetPromo();

                }
//            }
//        });

    }

    private void callJSONGetPromo() {
        GetPromoRequestEntity ent = new GetPromoRequestEntity();
        ent.setAuthorize("RT006");
        ent.setRequestType("promo");

        ClientServiceClient serviceClient = ServiceGenerator.createService(ClientServiceClient.class);

        Call<GetPromoResponseEntity> call = serviceClient.getPromo(ent);
        call.enqueue(callback);
    }

    @Override
    protected void onErrorReceived(ApplicationError applicationError) {
        Loading.cancelLoading();

        MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT, getString(R.string.title_dashboard),
                applicationError.getMessage(), getString(R.string.ok), this);
    }

    @Override
    protected void onSuccessReceived(Object entity) {

        if (entity instanceof GetPromoResponseEntity) {
            GetPromoResponseEntity resp = (GetPromoResponseEntity) entity;

            MyLog.info("InformationActivity respEntity getResponseCode " + resp.getResponseCode());
            MyLog.info("InformationActivity respEntity getResponseType " + resp.getResponseType());
            MyLog.info("InformationActivity respEntity getResponseMessage " + resp.getResponseMessage());

//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            List<ResponseDatum> datums = resp.getResponseData();
            imgResources = new ArrayList<>();
            imgData = new ArrayList<ResponseDatum>();

            // 11-11-2019 . maximal 4 dan desc
            if(datums!=null){
                for (ResponseDatum temp: datums){
                    Log.d("TAGRZ","resp "+ServiceGenerator.API_PHOTO_URL + temp.getPhoto());
                    imgResources.add(ServiceGenerator.API_PHOTO_URL + temp.getPhoto());
                    imgData.add(temp);
                }
            }

            viewPager.setAdapter(new SliderAdapter(this, imgResources, imgData));
            indicator.setupWithViewPager(viewPager, true);

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);

        }

        Loading.cancelLoading();

    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            ClientDashboardActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < imgResources.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        if (isFastDoubleClick()) return;

        if (view.getId() == R.id.iv_info_client){
            startActivity(new Intent(getApplicationContext(), ClientInformationActivity.class));
        } else if (view.getId() == R.id.iv_promo){
            startActivity(new Intent(getApplicationContext(), ClientPromoActivity.class));
        } else if (view.getId() == R.id.iv_contactus){
            startActivity(new Intent(getApplicationContext(), ClientContactUsActivity.class));
        } else if (view.getId() == R.id.iv_profile){
            startActivity(new Intent(getApplicationContext(), ClientProfileActivity.class));
        } else {
            super.onClick(view);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        MyLog.info("ON RESUME");

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        MyLog.info("ON RESTART");

    }

    @Override
    protected void onStart() {
        super.onStart();

        MyLog.info("ON START");
    }
}
