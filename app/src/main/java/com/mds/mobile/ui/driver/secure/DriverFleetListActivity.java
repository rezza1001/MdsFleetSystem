package com.mds.mobile.ui.driver.secure;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.mds.mobile.R;
import com.mds.mobile.base.Global;
import com.mds.mobile.model.ApplicationError;
import com.mds.mobile.model.DriverFleetListItem;
import com.mds.mobile.module.dialog.MyDialog;
import com.mds.mobile.module.loading.Loading;
import com.mds.mobile.module.mylog.MyLog;
import com.mds.mobile.remote.ServiceGenerator;
import com.mds.mobile.remote.entity.drivervehiclelist.request.GetFleetRequestEntity;
import com.mds.mobile.remote.entity.drivervehiclelist.response.GetFleetResponseEntity;
import com.mds.mobile.remote.entity.drivervehiclelist.response.ResponseDatum;
import com.mds.mobile.remote.service.DriverServiceClient;
import retrofit2.Call;

public class DriverFleetListActivity extends DriverBaseUi {

    ListView lvFleet;
    MyArrayAdapter adapter;
    ArrayList<DriverFleetListItem> listFleet;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_driver_fleet_list;
    }

    @Override
    protected Integer getNavigationMenuItemIndex() {
        return R.id.nav_vehicle;
    }

    @Override
    protected void onMyCreate() {

        lvFleet = findViewById(R.id.lv_driver_fleet);

        Loading.showLoading(this, "Loading ...");
        callJSONGetFleet();

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

        MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT, getString(R.string.title_fleetlist),
                applicationError.getMessage(), getString(R.string.ok), this);
    }

    @Override
    protected void onSuccessReceived(Object entity) {

        GetFleetResponseEntity resp = (GetFleetResponseEntity) entity;

        MyLog.info("InformationActivity respEntity getResponseCode " + resp.getResponseCode());
        MyLog.info("InformationActivity respEntity getResponseType " + resp.getResponseType());
        MyLog.info("InformationActivity respEntity getResponseMessage " + resp.getResponseMessage());

        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat ddMMMyyyy = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "ID"));

        List<ResponseDatum> list = resp.getResponseData();
        listFleet = new ArrayList<DriverFleetListItem>();
        for (ResponseDatum temp : list) {
            MyLog.info("Temp : " + temp.getMerk());

            String strDate = temp.getStnkExpireDate();
            try {
                Date tempDate = yyyyMMdd.parse(temp.getStnkExpireDate());
                strDate = ddMMMyyyy.format(tempDate);
            } catch (Exception e){
                strDate = temp.getStnkExpireDate();
            }

            DriverFleetListItem item = new DriverFleetListItem();
            item.setId(temp.getFleetId());
            item.setLicensePlate(temp.getLicensePlate());
            item.setContent(temp.getMerk() + " "+ temp.getModel() +" "+ temp.getColor() +"\nJatuh Tempo STNK " + strDate);

            // content for next intent
            // textFleets.add(temp.getLicensePlate() + " " + temp.getMerk() + " " + temp.getModel() +" "+ temp.getColor());
            item.setContentBundle(temp.getLicensePlate() + " " + temp.getMerk() + " " + temp.getModel() +" "+ temp.getColor());
            item.setStnkExpireDate(temp.getStnkExpireDate());

            listFleet.add(item);
        }

        adapter = new MyArrayAdapter(this, listFleet);
        lvFleet.setAdapter(adapter);

        Loading.cancelLoading();

    }

    private class MyArrayAdapter extends ArrayAdapter<DriverFleetListItem> {

        private final Context context;
        private final List<DriverFleetListItem> list;

        public MyArrayAdapter(Context context, List<DriverFleetListItem> objects) {
            super(context, -1, objects);
            this.context = context;
            this.list = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            LayoutInflater inflater = (LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View rowView = inflater.inflate(R.layout.list_driver_fleetlist, parent, false);

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_driver_fleetlist, parent, false);
            }

            TextView tvId = convertView.findViewById(R.id.tv_fleetlist_id);
            TextView tvContent = convertView.findViewById(R.id.tv_fleetlist_content);

            DriverFleetListItem item = list.get(position);

            tvId.setText(item.getLicensePlate());
            tvContent.setText(item.getContent());

            ImageView ivUpadeKm = convertView.findViewById((R.id.iv_updatekm));
            ivUpadeKm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), DriverUpdateKmActivity.class);
                    intent.putExtra("fleet_id", item.getId());
                    intent.putExtra("fleet_content", item.getContentBundle());
                    startActivity(intent);
                }
            });

            ImageView ivService = convertView.findViewById((R.id.iv_service));
            ivService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), DriverFleetServiceActivity.class);
                    intent.putExtra("fleet_id", item.getId());
                    intent.putExtra("fleet_content", item.getContentBundle());
                    startActivity(intent);
                }
            });

            ImageView ivAsuransi = convertView.findViewById((R.id.iv_asuransi));
            ivAsuransi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), DriverAssuranceActivity.class);
                    intent.putExtra("fleet_id", item.getId());
                    intent.putExtra("fleet_content", item.getContentBundle());
                    startActivity(intent);
                }
            });

            ImageView ivReport = convertView.findViewById((R.id.iv_report));
            ivReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), DriverReportActivity.class);
                    intent.putExtra("fleet_id", item.getId());
                    intent.putExtra("fleet_content", item.getContentBundle());
                    startActivity(intent);
                }
            });

            ImageView ivStnk = convertView.findViewById((R.id.iv_stnk));
            ivStnk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), DriverStnkActivity.class);
                    intent.putExtra("fleet_id", item.getId());
                    intent.putExtra("fleet_content", item.getContentBundle());
                    intent.putExtra("fleet_stnk", item.getStnkExpireDate());
                    startActivity(intent);
                }
            });

            return convertView;
        }

    }

}
