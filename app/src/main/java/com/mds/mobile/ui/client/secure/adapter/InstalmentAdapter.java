package com.mds.mobile.ui.client.secure.adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

import com.mds.mobile.R;
import com.mds.mobile.remote.post.Utility;

public class InstalmentAdapter extends RecyclerView.Adapter<InstalmentAdapter.OptionView>{

    private final ArrayList<Bundle> nominal;

    public InstalmentAdapter(ArrayList<Bundle> data ){
        nominal = data;
    }


    @NonNull
    @Override
    public OptionView onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_client_instalment_item, parent, false);
        return new OptionView(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OptionView view, int i) {
        Bundle data = nominal.get(i);
        String period = "Angsuran Ke - "+data.getString("period");
        long nominal = data.getLong("amount");
        long penalty = data.getLong("penalty");
        Date dueDate = Utility.getDate(data.getString("duedate").split(" ")[0],"MM/dd/yyyy");

        if (!data.getString("restru").isEmpty()){
            view.txvw_paymentDate.setText("RESTRUCTURE");
        }
        else {
            if (!data.getString("payment_date").isEmpty()){
                Date payDate = Utility.getDate(data.getString("payment_date").split(" ")[0],"MM/dd/yyyy");
                view.txvw_paymentDate.setText(Utility.getDateString(payDate, "dd MMM yyyy"));
            }
            else {
                view.txvw_paymentDate.setText("-");
            }
        }

        String overDue = data.getString("over_due")+ " Hari";
        if (data.getString("over_due").equals("0")){
            overDue = "-";
        }

        if (penalty == 0){
            view.lnly_overDue.setVisibility(View.GONE);
            view.lnly_penalty.setVisibility(View.GONE);
        }
        else {
            view.lnly_overDue.setVisibility(View.VISIBLE);
            view.lnly_penalty.setVisibility(View.VISIBLE);
        }



        view.txvw_period.setText(period);
        view.txvw_amount.setText(Utility.toCurrency("Rp", nominal));
        view.txvw_dueDate.setText(Utility.getDateString(dueDate,"dd MMM yyyy"));
        view.txvw_overDue.setText(overDue);
        view.txvw_penalty.setText(Utility.toCurrency("Rp", penalty));

      
    }

    @Override
    public int getItemCount() {
        return nominal.size();
    }

    class OptionView extends RecyclerView.ViewHolder {
        TextView txvw_period,txvw_amount,txvw_dueDate,txvw_paymentDate,txvw_overDue,txvw_penalty;
        LinearLayout lnly_overDue,lnly_penalty;
        public OptionView(View itemView) {
            super(itemView);
            txvw_period = itemView.findViewById(R.id.txvw_period);
            txvw_amount = itemView.findViewById(R.id.txvw_amount);
            txvw_dueDate = itemView.findViewById(R.id.txvw_dueDate);
            txvw_paymentDate = itemView.findViewById(R.id.txvw_paymentDate);
            txvw_overDue = itemView.findViewById(R.id.txvw_overDue);
            txvw_penalty = itemView.findViewById(R.id.txvw_penalty);
            lnly_overDue = itemView.findViewById(R.id.lnly_overDue);
            lnly_penalty = itemView.findViewById(R.id.lnly_penalty);
        }
    }


    private OnSelectedListener onSelectedListener;
    public void setOnSelectedListener(OnSelectedListener onSelectedListener){
        this.onSelectedListener = onSelectedListener;
    }
    public interface OnSelectedListener{
        void onDownload(Bundle data);
    }

}
