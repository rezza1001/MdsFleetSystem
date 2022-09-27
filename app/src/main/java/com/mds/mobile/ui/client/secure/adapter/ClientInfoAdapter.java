package com.mds.mobile.ui.client.secure.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.mds.mobile.R;

public class ClientInfoAdapter extends RecyclerView.Adapter<ClientInfoAdapter.OptionView>{

    private final ArrayList<Bundle> listData;
    private Context mContext;

    public ClientInfoAdapter(Context context , ArrayList<Bundle> data ){
        listData = data;
        mContext = context;
    }

    @NonNull
    @Override
    public OptionView onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_clientinfo, parent, false);
        return new OptionView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionView view, int i) {
        Bundle data = listData.get(i);

        view.txvw_contract.setText(data.getString("contract"));
        view.txvw_tenor.setText(data.getString("tenor"));
        view.txvw_remainTenor.setText(data.getString("remaining"));
        view.txvw_overDue.setText(data.getString("over_due"));

        view.lnly_action.setOnClickListener(v -> {
            if (onSelectedListener != null){
                onSelectedListener.onView(data);
            }
        });



    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    static class OptionView extends RecyclerView.ViewHolder {
        TextView txvw_contract, txvw_tenor, txvw_remainTenor, txvw_overDue;
        LinearLayout lnly_action;
        public OptionView(View itemView) {
            super(itemView);
            txvw_contract = itemView.findViewById(R.id.txvw_contract);
            txvw_tenor = itemView.findViewById(R.id.txvw_tenor);
            txvw_remainTenor = itemView.findViewById(R.id.txvw_remainTenor);
            txvw_overDue = itemView.findViewById(R.id.txvw_overDue);
            lnly_action = itemView.findViewById(R.id.lnly_action);
        }
    }


    private OnSelectedListener onSelectedListener;
    public void setOnSelectedListener(OnSelectedListener onSelectedListener){
        this.onSelectedListener = onSelectedListener;
    }
    public interface OnSelectedListener{
        void onView(Bundle data);
    }

}
