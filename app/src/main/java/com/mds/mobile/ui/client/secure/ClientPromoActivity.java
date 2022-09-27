package com.mds.mobile.ui.client.secure;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mds.mobile.R;
import com.mds.mobile.model.ApplicationError;
import com.mds.mobile.model.ClientPromoItem;
import com.mds.mobile.model.DriverReportItem;
import com.mds.mobile.module.dialog.MyDialog;
import com.mds.mobile.module.loading.Loading;
import com.mds.mobile.module.mylog.MyLog;
import com.mds.mobile.remote.ServiceGenerator;
import com.mds.mobile.remote.entity.promo.request.GetPromoRequestEntity;
import com.mds.mobile.remote.entity.promo.response.GetPromoResponseEntity;
import com.mds.mobile.remote.entity.promo.response.ResponseDatum;
import com.mds.mobile.remote.service.ClientServiceClient;
import com.mds.mobile.ui.driver.secure.DriverReportActivity;
import retrofit2.Call;

public class ClientPromoActivity extends ClientBaseUi {

    ListView lvPromo;
    MyArrayAdapter adapter;
    ArrayList<ClientPromoItem> listPromo;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_client_promo;
    }

    @Override
    protected Integer getNavigationMenuItemIndex() {
        return R.id.nav_promo;
    }

    @Override
    protected void onMyCreate() {

        lvPromo = findViewById(R.id.lv_client_promo);

        Loading.showLoading(this, "Loading ...");
        callJSONGetPromo();

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

        MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT, getString(R.string.title_promo),
                applicationError.getMessage(), getString(R.string.ok), this);
    }

    @Override
    protected void onSuccessReceived(Object entity) {

        if (entity instanceof GetPromoResponseEntity) {
            GetPromoResponseEntity resp = (GetPromoResponseEntity) entity;

            MyLog.info("Promo respEntity getResponseCode " + resp.getResponseCode());
            MyLog.info("Promo respEntity getResponseType " + resp.getResponseType());
            MyLog.info("Promo respEntity getResponseMessage " + resp.getResponseMessage());

            List<ResponseDatum>  list = resp.getResponseData();

            listPromo = new ArrayList<ClientPromoItem>();
            for (ResponseDatum temp : list) {
                MyLog.info("Temp : " + temp.getTitle());

                ClientPromoItem item = new ClientPromoItem();
                item.setTitle(temp.getTitle());
                item.setDescription(temp.getDescription());
                item.setPhoto(ServiceGenerator.API_PHOTO_URL + temp.getPhoto());
                listPromo.add(item);
            }

            adapter = new MyArrayAdapter(this, listPromo);
            lvPromo.setAdapter(adapter);

        }

        Loading.cancelLoading();

    }

    private class MyArrayAdapter extends ArrayAdapter<ClientPromoItem> {

        private final Context context;
        private final List<ClientPromoItem> list;

        public MyArrayAdapter(Context context, List<ClientPromoItem> objects) {
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
                convertView = getLayoutInflater().inflate(R.layout.list_client_promo, parent, false);
            }

            TextView tvTitle = convertView.findViewById(R.id.tv_title);
            TextView tvDescription = convertView.findViewById(R.id.tv_content);
            ImageView ivPhoto = convertView.findViewById(R.id.iv_promo);

            ClientPromoItem item = list.get(position);

//            String des = item.getDescription();
            String des = item.getDescription();
            String[] arrdes = des.split("\\r\\n\\r\\n");
            if(arrdes != null){
                des = arrdes[0];
            } else {
                des = "";
            }

            tvTitle.setText(item.getTitle());
            tvDescription.setText(des);

            Glide
                    .with(context)
                    .load(item.getPhoto())
                    .fitCenter()
                    .placeholder(R.drawable.ic_camera)
                    .into(ivPhoto);

            TextView tvNext = convertView.findViewById(R.id.tv_next);
            tvNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ClientPromoDetailActivity.class);
                    intent.putExtra("data_promo_2",item);
                    context.startActivity(intent);
                }
            });

            return convertView;
        }

        public void refresh(){
            //manipulate list
            notifyDataSetChanged();
        }

    }


}
