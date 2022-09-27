package com.mds.mobile.ui.client.secure;

import android.app.Activity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.mds.mobile.R;
import com.mds.mobile.remote.post.ErrorCode;
import com.mds.mobile.remote.post.PostManager;
import com.mds.mobile.ui.DynamicDialog;
import com.mds.mobile.ui.client.secure.adapter.InstalmentAdapter;

/**
 * Created by Mochamad Rezza Gumilang on 20/Oct/2021.
 * Class Info :
 */

public class DetailInstallmentActivity extends ClientBaseUi {

    private Activity mActivity;
    private final ArrayList<Bundle> allData = new ArrayList<>();
    private InstalmentAdapter adapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_client_installment_detail;
    }

    @Override
    protected void onMyCreate() {
        mActivity = DetailInstallmentActivity.this;
        findViewById(R.id.rvly_back).setOnClickListener(view -> onBackPressed());

        RecyclerView rcvw_data = findViewById(R.id.rcvw_data);
        rcvw_data.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new InstalmentAdapter(allData);
        rcvw_data.setAdapter(adapter);

        loadData();
    }

    @Override
    protected Integer getNavigationMenuItemIndex() {
        return null;
    }

    private void loadData(){
        allData.clear();
        PostManager post = new PostManager(mActivity,"document/getinstallment");
        post.addParam("AgreementNo",getIntent().getStringExtra("agreement"));
        post.exPost();
        post.setOnReceiveListener((obj, code, message) -> {
            if (code == ErrorCode.OK){
                try {
                    JSONArray ja = obj.getJSONArray("data");
                    for (int i=0; i<ja.length(); i++){
                        JSONObject jo = ja.getJSONObject(i);
                        Bundle bundle = new Bundle();
                        bundle.putString("period", jo.getString("period"));
                        String amount = jo.getString("rental").isEmpty() ? "0" : jo.getString("rental").split("\\.")[0];
                        bundle.putLong("amount", Long.parseLong(amount));
                        String penalty = jo.getString("penalty").isEmpty() ? "0" : jo.getString("penalty").split("\\.")[0];
                        bundle.putLong("penalty", Long.parseLong(penalty));
                        bundle.putString("duedate", jo.getString("duedate"));
                        bundle.putString("payment_date", jo.getString("paydate"));
                        bundle.putString("over_due", jo.getString("overdue"));
                        bundle.putString("restru", jo.getString("restru"));
                        allData.add(bundle);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                DynamicDialog dialog = new DynamicDialog(mActivity);
                dialog.showError("Data kosng","Detail untuk kontrak ini tidak ditemukan");
            }
            adapter.notifyDataSetChanged();
        });
    }
}
