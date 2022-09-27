package com.mds.mobile.ui.client.secure;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.mds.mobile.R;
import com.mds.mobile.model.ApplicationError;
import com.mds.mobile.model.ClientContactUsItem;
import com.mds.mobile.model.ClientPromoItem;
import com.mds.mobile.module.dialog.MyDialog;
import com.mds.mobile.module.loading.Loading;
import com.mds.mobile.module.mylog.MyLog;
import com.mds.mobile.remote.ServiceGenerator;
import com.mds.mobile.remote.entity.contactus.request.GetContactUsRequestEntity;
import com.mds.mobile.remote.entity.contactus.response.GetContactUsResponseEntity;
import com.mds.mobile.remote.entity.contactus.response.ResponseDatum;
import com.mds.mobile.remote.service.CustomerInformationServiceClient;
import retrofit2.Call;

public class ClientContactUsActivity extends ClientBaseUi {

    ListView lvContactUs;
    MyArrayAdapter adapter;
    ArrayList<ClientContactUsItem> listBranch;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_client_contact_us;
    }

    @Override
    protected Integer getNavigationMenuItemIndex() {
        return R.id.nav_contact_us;
    }

    @Override
    protected void onMyCreate() {

        lvContactUs = findViewById(R.id.lv_client_contactus);

        Loading.showLoading(this, "Loading ...");
        callJSONGetContactUs();

        // hardcode
//        ClientContactUsItem item1 = new ClientContactUsItem();
//        item1.setBranch("HEAD OFFICE");
//        item1.setAddress("MNC TOWER 23rd Floor,\n" +
//                "Jl. Kebon Sirih No. 17 - 19,\n" +
//                "Jakarta Pusat 10340");
//        item1.setPhone("tlp. 021 - 391 - 0993");
//
//        ClientContactUsItem item2 = new ClientContactUsItem();
//        item2.setBranch("MAKASSAR");
//        item2.setAddress("JL. Gunung Latimojong 46B Lantai 3\n" +
//                "Kel. Maradekaya Selatan Makassar,\n" +
//                "Sulawesi Selatan 90141");
//        item2.setPhone("T. 0411 - 3690915");
//
//        ClientContactUsItem item4 = new ClientContactUsItem();
//        item4.setBranch("BANJARMASIN");
//        item4.setAddress("JL. Pangeran Hidayatullah No. 18 Lantai 3\n" +
//                "Kel. Sungai Jingah Kec. Banjarmasin Utara,\n" +
//                "Kalimantan Selatan 70121");
//        item4.setPhone("T. 0511 - 6741287");
//
//        ClientContactUsItem item5 = new ClientContactUsItem();
//        item5.setBranch("BALIKPAPAN");
//        item5.setAddress("Gedung MNC Bank Lantai 2, Jalan Jend Sudirman\n" +
//                "No. 327, Kel. Damai, Kec. Balikpapan Selatan,\n" +
//                "Balikpapan 76114");
//        item5.setPhone("T. 0542 - 8519923");
//
//        listContUs = new ArrayList<ClientContactUsItem>();
//        listContUs.add(item1);
//        listContUs.add(item2);
//        listContUs.add(item4);
//        listContUs.add(item5);
//
//        adapter = new MyArrayAdapter(this, listContUs);
//        lvContactUs.setAdapter(adapter);

    }

    private void callJSONGetContactUs() {
        GetContactUsRequestEntity ent = new GetContactUsRequestEntity();
        ent.setAuthorize("RT006");
        ent.setRequestType("customer_contactus");
        ent.setUserCode(getUserProfile().getUserCode());

        CustomerInformationServiceClient serviceClient = ServiceGenerator.createService(CustomerInformationServiceClient.class);

        Call<GetContactUsResponseEntity> call = serviceClient.contactUs(ent);
        call.enqueue(callback);
    }

    @Override
    protected void onErrorReceived(ApplicationError applicationError) {
        Loading.cancelLoading();

        MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT, getString(R.string.title_contactus),
                applicationError.getMessage(), getString(R.string.ok), this);
    }

    @Override
    protected void onSuccessReceived(Object entity) {

        if (entity instanceof GetContactUsResponseEntity) {
            GetContactUsResponseEntity resp = (GetContactUsResponseEntity) entity;

            MyLog.info("Promo respEntity getResponseCode " + resp.getResponseCode());
            MyLog.info("Promo respEntity getResponseType " + resp.getResponseType());
            MyLog.info("Promo respEntity getResponseMessage " + resp.getResponseMessage());

            List<ResponseDatum>  list = resp.getResponseData();

            listBranch = new ArrayList<ClientContactUsItem>();
            for (ResponseDatum temp : list) {
                MyLog.info("Temp : " + temp.getBranch());

                ClientContactUsItem item = new ClientContactUsItem();
                item.setBranch(temp.getBranch());
                item.setAddress(temp.getAddress());
                listBranch.add(item);
            }

            adapter = new MyArrayAdapter(this, listBranch);
            lvContactUs.setAdapter(adapter);

        }

        Loading.cancelLoading();

    }

    private class MyArrayAdapter extends ArrayAdapter<ClientContactUsItem> {
        private final Context context;
        private final List<ClientContactUsItem> list;

        public MyArrayAdapter(Context context, List<ClientContactUsItem> objects) {
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
                convertView = getLayoutInflater().inflate(R.layout.list_client_contactus, parent, false);
            }

            TextView tvTitle = convertView.findViewById(R.id.tv_branch);
            TextView tvAddr = convertView.findViewById(R.id.tv_address);
//            TextView tvPhone = convertView.findViewById(R.id.tv_phone);

            ClientContactUsItem item = list.get(position);

            tvTitle.setText(item.getBranch());
            tvAddr.setText(item.getAddress());
//            tvPhone.setText(item.getPhone());

            return convertView;
        }

        public void refresh(){
            //manipulate list
            notifyDataSetChanged();
        }
    }

}
