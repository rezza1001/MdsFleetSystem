package com.mds.mobile.ui.driver.secure;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.mds.mobile.R;
import com.mds.mobile.base.Global;
import com.mds.mobile.model.ApplicationError;
import com.mds.mobile.model.DriverReportItem;
import com.mds.mobile.module.dialog.MyDialog;
import com.mds.mobile.module.loading.Loading;
import com.mds.mobile.module.mylog.MyLog;
import com.mds.mobile.remote.ServiceGenerator;
import com.mds.mobile.remote.entity.drivervehiclelist.request.GetFleetRequestEntity;
import com.mds.mobile.remote.entity.drivervehiclelist.response.GetFleetResponseEntity;
import com.mds.mobile.remote.entity.drivervehiclelist.response.ResponseDatum;
import com.mds.mobile.remote.entity.history.request.GetFleetOdometerHistoryRequestEntity;
import com.mds.mobile.remote.entity.history.response.GetFleetOdometerHistoryResponseEntity;
import com.mds.mobile.remote.service.DriverServiceClient;
import retrofit2.Call;

public class DriverReportActivity extends DriverBaseUi implements AdapterView.OnItemSelectedListener {

    TextView tvNotFound;
    Spinner spVehicle;
    List<String> fleets;
    List<String> textFleets;
    ListView lvReport;
    MyArrayAdapter adapter;
    ArrayList<DriverReportItem> listReport;

    String selectedFleetId;

    String fleetIdBundle=null;
    String fleetContentBundle=null;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_driver_report;
    }

    @Override
    protected Integer getNavigationMenuItemIndex() {
        return R.id.nav_report;
    }

    @Override
    protected void onMyCreate() {

        Intent intent = getIntent();
        fleetIdBundle = intent.getStringExtra("fleet_id");
        fleetContentBundle = intent.getStringExtra("fleet_content");

        MyLog.info("onMyCreate() , fleetIdBundle: "+fleetIdBundle);
        MyLog.info("onMyCreate() , fleetContentBundle: "+fleetContentBundle);

        tvNotFound = findViewById(R.id.tv_notfound);
        spVehicle = findViewById(R.id.sp_vehicle);
        lvReport = findViewById(R.id.lv_driver_report);
        spVehicle.setOnItemSelectedListener(this);

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

//        listReport = new ArrayList<DriverReportItem>();
//        adapter = new MyArrayAdapter(this, listReport);
//        lvReport.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        selectedFleetId = fleets.get(position);

        Loading.showLoading(this, "Loading ...");
        callJSONGetFleetHistory(selectedFleetId);
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

    private void callJSONGetFleetHistory(String fleetId) {
        GetFleetOdometerHistoryRequestEntity ent = new GetFleetOdometerHistoryRequestEntity();
        ent.setAuthorize("RT006");
        ent.setUserCode(getUserProfile().getUserCode());
        ent.setRequestType("user_fleet");
        ent.setFleetId(fleetId);

        DriverServiceClient serviceClient = ServiceGenerator.createService(DriverServiceClient.class);

        Call<GetFleetOdometerHistoryResponseEntity> call = serviceClient.getFleetOdometerHistory(ent);
        call.enqueue(callback);
    }

    @Override
    protected void onErrorReceived(ApplicationError applicationError) {
        Loading.cancelLoading();

        if("01".equals(applicationError.getResponseCode())){
            tvNotFound.setVisibility(View.VISIBLE);
            lvReport.setVisibility(View.GONE);
        } else {
            MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT, getString(R.string.title_report),
                    applicationError.getMessage(), getString(R.string.ok), this);
        }
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

            // POPULATE DROP DOWN
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, textFleets);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spVehicle.setAdapter(dataAdapter);

            Loading.cancelLoading();

            // GET HISTORY BY FLEET ID
            if(fleets!=null && fleets.size()>0){
                selectedFleetId = fleets.get(0);
            }

            if(selectedFleetId !=null){
                Loading.showLoading(this, "Loading ...");
                callJSONGetFleetHistory(selectedFleetId);
            }

        } else if (entity instanceof GetFleetOdometerHistoryResponseEntity) {

            tvNotFound.setVisibility(View.GONE);
            lvReport.setVisibility(View.VISIBLE);

            GetFleetOdometerHistoryResponseEntity resp = (GetFleetOdometerHistoryResponseEntity) entity;

            MyLog.info("InformationActivity respEntity getResponseCode " + resp.getResponseCode());
            MyLog.info("InformationActivity respEntity getResponseType " + resp.getResponseType());
            MyLog.info("InformationActivity respEntity getResponseMessage " + resp.getResponseMessage());

            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat ddMMMyyyy = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "ID"));

            List<com.mds.mobile.remote.entity.history.response.ResponseDatum>  list = resp.getResponseData();

            listReport = new ArrayList<DriverReportItem>();
            for (com.mds.mobile.remote.entity.history.response.ResponseDatum temp : list) {
                MyLog.info("Temp : " + temp.getCategory());


                String tmpDate;
                if(temp.getUpdatedDate() != null){

                    String strDate = temp.getUpdatedDate();
                    try {
                        Date tempDate = yyyyMMdd.parse(temp.getUpdatedDate());
                        strDate = ddMMMyyyy.format(tempDate);
                    } catch (Exception e){
                        strDate = temp.getUpdatedDate();
                    }
                    tmpDate = strDate  + " " + temp.getCategory() +" - "+temp.getSubCategory();
                } else {
                    String strDate = temp.getTicketDate();
                    try {
                        Date tempDate = yyyyMMdd.parse(temp.getTicketDate());
                        strDate = ddMMMyyyy.format(tempDate);
                    } catch (Exception e){
                        strDate = temp.getTicketDate();
                    }
                    tmpDate = strDate + " " + temp.getCategory() +" - "+temp.getSubCategory();
                }

                String dateReport =" - ";

                DriverReportItem item = new DriverReportItem();
                item.setDate(tmpDate);
                item.setStatus(temp.getStatus());
                item.setContent(temp.getLicensePlate() + " "+ temp.getMerk() + " "+ temp.getModel() +" "+ temp.getColor());
                listReport.add(item);
            }

            adapter = new MyArrayAdapter(this, listReport);
            lvReport.setAdapter(adapter);

            Loading.cancelLoading();

//
//            this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    adapter.refresh();
//                }
//            });
//            adapter.notifyDataSetChanged();
        }
    }

    private class MyArrayAdapter extends ArrayAdapter<DriverReportItem> {

        private final Context context;
        private final List<DriverReportItem> list;

        public MyArrayAdapter(Context context, List<DriverReportItem> objects) {
            super(context, -1, objects);
            this.context = context;
            this.list = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            LayoutInflater inflater = (LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View rowView = inflater.inflate(R.layout.list_driver_report, parent, false);

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_driver_report, parent, false);
            }

            TextView tvDate = convertView.findViewById(R.id.tv_report_date);
            TextView tvStatus = convertView.findViewById(R.id.tv_report_status);

            TextView tvContent = convertView.findViewById(R.id.tv_report_content);

            DriverReportItem item = list.get(position);

            tvDate.setText(item.getDate());
            tvStatus.setText(item.getStatus());
            tvContent.setText(item.getContent());

            return convertView;
        }

        public void refresh(){
            //manipulate list
            notifyDataSetChanged();
        }

    }


}
