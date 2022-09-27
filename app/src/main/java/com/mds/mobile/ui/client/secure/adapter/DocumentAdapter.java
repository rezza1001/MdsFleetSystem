package com.mds.mobile.ui.client.secure.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.mds.mobile.R;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.OptionView>{

    private final ArrayList<Bundle> nominal;

    public DocumentAdapter( ArrayList<Bundle> data ){
        nominal = data;
    }


    @NonNull
    @Override
    public OptionView onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_document, parent, false);
        return new OptionView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionView view, int i) {
        Bundle data = nominal.get(i);
        view.txvw_document.setText(data.getString("document"));
        view.imvw_download.setOnClickListener(v -> {
            if (onSelectedListener != null){
                onSelectedListener.onDownload(data);
            }
        });
        view.txvw_document.setOnClickListener(v -> {
            if (onSelectedListener != null){
                onSelectedListener.onDownload(data);
            }
        });
      
    }

    @Override
    public int getItemCount() {
        return nominal.size();
    }

    class OptionView extends RecyclerView.ViewHolder {
        TextView txvw_document;
        ImageView imvw_download;
        public OptionView(View itemView) {
            super(itemView);
            txvw_document = itemView.findViewById(R.id.txvw_document);
            imvw_download = itemView.findViewById(R.id.imvw_download);
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
