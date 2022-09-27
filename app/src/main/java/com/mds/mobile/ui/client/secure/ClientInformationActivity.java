package com.mds.mobile.ui.client.secure;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.mds.mobile.R;
import com.mds.mobile.model.ApplicationError;
import com.mds.mobile.module.dialog.MyDialog;
import com.mds.mobile.module.loading.Loading;
import com.mds.mobile.module.mylog.MyLog;
import com.mds.mobile.remote.ServiceGenerator;
import com.mds.mobile.remote.entity.customerinstallment.request.DetailRequestEntity;
import com.mds.mobile.remote.entity.customerinstallment.request.InstallmentRequestEntity;
import com.mds.mobile.remote.entity.customerinstallment.response.DetailResponseEntity;
import com.mds.mobile.remote.entity.customerinstallment.response.InstallmentResponseEntity;
import com.mds.mobile.remote.entity.customerinstallment.response.ResponseDatum;
import com.mds.mobile.remote.service.CustomerInformationServiceClient;
import com.mds.mobile.ui.client.secure.adapter.ClientInfoAdapter;
import retrofit2.Call;

public class ClientInformationActivity extends ClientBaseUi implements View.OnClickListener {

    private ArrayList<Bundle> bundles = new ArrayList<>();
    private ClientInfoAdapter adapter;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_client_information;
    }

    @Override
    protected Integer getNavigationMenuItemIndex() {
        return 0;
    }

    @Override
    protected void onMyCreate() {

        RecyclerView rcvw_data = findViewById(R.id.rcvw_data);
        rcvw_data.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ClientInfoAdapter(this, bundles);
        rcvw_data.setAdapter(adapter);

        adapter.setOnSelectedListener(data -> {
            informationDetail(data.getString("agreement"),data.getString("remaining"));
        });

        findViewById(R.id.ibBack).setOnClickListener(view -> onBackPressed());

        Loading.showLoading(this, "Loading ...");
        callJSONInstallment();

    }

    @Override
    public void onClick(View view) {
        if (isFastDoubleClick()) return;

        super.onClick(view);
    }

    @Override
    protected void onErrorReceived(ApplicationError applicationError) {
        Loading.cancelLoading();

        MyDialog.showDialog1Btn(this, MyDialog.DIALOG_ID_ALERT, getString(R.string.information),
                applicationError.getMessage(), getString(R.string.ok), this);
    }

    @Override
    protected void onSuccessReceived(Object entity) {
        if (entity instanceof InstallmentResponseEntity) {
            InstallmentResponseEntity respEntity = (InstallmentResponseEntity) entity;

            MyLog.info("InformationActivity respEntity getResponseCode " + respEntity.getResponseCode());
            MyLog.info("InformationActivity respEntity getResponseType " + respEntity.getResponseType());
            MyLog.info("InformationActivity respEntity getResponseMessage " + respEntity.getResponseMessage());

            List<ResponseDatum> list = respEntity.getResponseData();

            for (ResponseDatum temp : list) {
                MyLog.info("Temp : " + temp.getAgreementId());
            }

//            TableLayout tl = findViewById(R.id.tl_installment);

            loadData(list);
        } else if (entity instanceof DetailResponseEntity) {
            DetailResponseEntity respEntity = (DetailResponseEntity) entity;

            MyLog.info("InformationActivity respEntity getResponseCode " + respEntity.getResponseCode());
            MyLog.info("InformationActivity respEntity getResponseType " + respEntity.getResponseType());
            MyLog.info("InformationActivity respEntity getResponseMessage " + respEntity.getResponseMessage());

            Intent intent = new Intent(getApplicationContext(), ClientInformationDetailActivity.class);
            intent.putExtra("my_detail", respEntity);
            intent.putExtra("sisa_tenor", clickedSisaTenor);
            startActivity(intent);
        }

        Loading.cancelLoading();
    }

    private void callJSONInstallment() {

        InstallmentRequestEntity ent = new InstallmentRequestEntity();
        ent.setAuthorize("RT006");
        ent.setUserCode(getUserProfile().getUserCode());
        ent.setRequestType("customer_installmen");

        CustomerInformationServiceClient serviceClient = ServiceGenerator.createService(CustomerInformationServiceClient.class);

        Call<InstallmentResponseEntity> call = serviceClient.installment(ent);
        call.enqueue(callback);
    }

    private void callJSONInstallmentDetail(String agreementId) {

        DetailRequestEntity ent = new DetailRequestEntity();
        ent.setAuthorize("RT006");
        ent.setInstallmentCode(agreementId);
        ent.setRequestType("customer_installment_detail");

        CustomerInformationServiceClient serviceClient = ServiceGenerator.createService(CustomerInformationServiceClient.class);

        Call<DetailResponseEntity> call = serviceClient.installmentDetail(ent);
        call.enqueue(callback);
    }

    public void loadData(List<ResponseDatum> list) {
        bundles.clear();
        if (list != null) {
            for (ResponseDatum temp : list) {
                String contract = temp.getAgreementId() + "\n" + temp.getAssetDescription();
                String tenor = temp.getTenor();
                String remainTenor = temp.getSisaTenor();
                String overDue = temp.getOverDueDays();

                Bundle bundle = new Bundle();
                bundle.putString("contract",contract);
                bundle.putString("tenor",tenor);
                bundle.putString("remaining",remainTenor);
                bundle.putString("over_due",overDue);
                bundle.putString("agreement",temp.getAgreementId());
                bundles.add(bundle);

            }
        }
        adapter.notifyDataSetChanged();

    }

    private String clickedSisaTenor;

    private void informationDetail(String agreementId, String sisaTenor) {

        clickedSisaTenor = sisaTenor;

        Loading.showLoading(this, "Loading ...");
        callJSONInstallmentDetail(agreementId);

//        startActivity(new Intent(getApplicationContext(), ClientInformationDetailActivity.class));
    }

}
