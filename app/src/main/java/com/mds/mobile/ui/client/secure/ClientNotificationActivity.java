package com.mds.mobile.ui.client.secure;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.mds.mobile.R;
import com.mds.mobile.model.ApplicationError;
import com.mds.mobile.model.NotificationItem;
import com.mds.mobile.module.dialog.MyDialog;
import com.mds.mobile.module.loading.Loading;
import com.mds.mobile.module.mylog.MyLog;
import com.mds.mobile.remote.ServiceGenerator;
import com.mds.mobile.remote.entity.notif.request.GetNotifRequestEntity;
import com.mds.mobile.remote.entity.notif.response.GetNotifResponseEntity;
import com.mds.mobile.remote.entity.notif.response.ResponseDatum;
import com.mds.mobile.remote.service.ClientServiceClient;
import retrofit2.Call;

public class ClientNotificationActivity extends ClientBaseUi {

    ListView lvNotif;
    List<NotificationItem> listNotif;
    MyArrayAdapter adapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_client_notification;
    }

    @Override
    protected Integer getNavigationMenuItemIndex() {
        return null;
    }

    @Override
    protected void onMyCreate() {

        lvNotif = findViewById(R.id.lv_notification);

        Loading.showLoading(this, "Loading ...");
        callJSONGetNotification();

//
//        // hardcode
//        NotificationItem item1 = new NotificationItem();
//        item1.setDateNotif("10 agustus 2019");
//        item1.setTitle("Kenaikan saham");
//        item1.setDescription("Kenaikan saham");
//
//        NotificationItem item2 = new NotificationItem();
//        item2.setDateNotif("11 agustus 2019");
//        item2.setTitle("MNC Leasing peduli");
//        item2.setDescription("MNC Leasing peduli");
//
//        NotificationItem item3 = new NotificationItem();
//        item3.setDateNotif("12 agustus 2019");
//        item3.setTitle("Kenaikan saham");
//        item3.setDescription("Kenaikan saham");
//
//        NotificationItem item4 = new NotificationItem();
//        item4.setDateNotif("13 agustus 2019");
//        item4.setTitle("MNC Leasing peduli");
//        item4.setDescription("MNC Leasing peduli");
//
//        NotificationItem item5 = new NotificationItem();
//        item5.setDateNotif("14 agustus 2019");
//        item5.setTitle("Kenaikan saham");
//        item5.setDescription("Kenaikan saham");
//
//        listNotif = new ArrayList<NotificationItem>();
//        listNotif.add(item1);
//        listNotif.add(item2);
//        listNotif.add(item3);
//        listNotif.add(item4);
//        listNotif.add(item5);
//
//        adapter = new MyArrayAdapter(this, listNotif);
//        lvNotif.setAdapter(adapter);

    }

    private void callJSONGetNotification() {
        GetNotifRequestEntity ent = new GetNotifRequestEntity();
        ent.setAuthorize("RT006");
        ent.setRequestType("notification");
        ent.setUserCode(getUserProfile().getUserCode());

        ClientServiceClient serviceClient = ServiceGenerator.createService(ClientServiceClient.class);

        Call<GetNotifResponseEntity> call = serviceClient.getNotification(ent);
        call.enqueue(callback);
    }

    @Override
    protected void onErrorReceived(ApplicationError applicationError) {
        Loading.cancelLoading();

        MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT, getString(R.string.title_faq),
                applicationError.getMessage(), getString(R.string.ok), this);
    }

    @Override
    protected void onSuccessReceived(Object entity) {

        if (entity instanceof GetNotifResponseEntity) {
            GetNotifResponseEntity resp = (GetNotifResponseEntity) entity;

            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat ddMMMyyyy = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "ID"));

            MyLog.info("Promo respEntity getResponseCode " + resp.getResponseCode());
            MyLog.info("Promo respEntity getResponseType " + resp.getResponseType());
            MyLog.info("Promo respEntity getResponseMessage " + resp.getResponseMessage());

            List<ResponseDatum> list = resp.getResponseData();

            listNotif = new ArrayList<NotificationItem>();

            for (ResponseDatum temp : list) {
                MyLog.info("Temp : " + temp.getTitle());

                String strDate = temp.getUpdatedDate();
                try {
                    Date tempDate = yyyyMMdd.parse(temp.getUpdatedDate());
                    strDate = ddMMMyyyy.format(tempDate);
                } catch (Exception e){
                    strDate = temp.getUpdatedDate();
                }

                NotificationItem item4 = new NotificationItem();
                item4.setDateNotif(strDate);
                item4.setTitle(temp.getTitle());
                item4.setDescription(temp.getDescription());
                listNotif.add(item4);
            }

            adapter = new MyArrayAdapter(this, listNotif);
            lvNotif.setAdapter(adapter);

        }

        Loading.cancelLoading();

    }

    private class MyArrayAdapter extends ArrayAdapter<NotificationItem> {
        private final Context context;
        private final List<NotificationItem> list;

        public MyArrayAdapter(Context context, List<NotificationItem> objects) {
            super(context, -1, objects);
            this.context = context;
            this.list = objects;

            MyLog.info("UKURANNNNNNNNNNNNN "+list.size());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            LayoutInflater inflater = (LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View rowView = inflater.inflate(R.layout.list_driver_report, parent, false);

            MyLog.info("position "+position);

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_notification, parent, false);
            }

            TextView tvTitle = convertView.findViewById(R.id.tv_title);
            TextView tvDate = convertView.findViewById(R.id.tv_date);
            TextView tvDesc = convertView.findViewById(R.id.tv_desc);

            NotificationItem item = list.get(position);

            tvTitle.setText(item.getTitle());
            tvDate.setText(item.getDateNotif());
            tvDesc.setText(item.getDescription());

            return convertView;
        }

        public void refresh(){
            //manipulate list
            notifyDataSetChanged();
        }
    }
}
