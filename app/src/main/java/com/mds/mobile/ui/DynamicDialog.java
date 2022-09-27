package com.mds.mobile.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.mds.mobile.R;

/**
 * Created by Mochamad Rezza Gumilang on 04/Oct/2021.
 * Class Info :
 */

public class DynamicDialog extends MyDialog {

    private CardView card_body_00;
    private ImageView imvw_icon_00;
    private TextView txvw_title_00,txvw_desc_00,txvw_action_00;
    private View view_status_00;

    public DynamicDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int setLayout() {
        return R.layout.component_dialog_error;
    }

    @Override
    protected void initLayout(View view) {
        imvw_icon_00 = view.findViewById(R.id.imvw_icon_00);
        card_body_00 = view.findViewById(R.id.card_body_00);
        txvw_title_00 = view.findViewById(R.id.txvw_title_00);
        txvw_desc_00 = view.findViewById(R.id.txvw_desc_00);
        txvw_action_00 = view.findViewById(R.id.txvw_action_00);
        view_status_00 = view.findViewById(R.id.view_status_00);
        card_body_00.setVisibility(View.INVISIBLE);

        view.findViewById(R.id.mrly_close_00).setOnClickListener(v -> {
            if (onCloseLister != null){
                onCloseLister.onClose(txvw_action_00.getText().toString());
            }
            dismiss();
        });
    }

    @Override
    public void show() {
        super.show();
        card_body_00.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.zoom_in));
        card_body_00.setVisibility(View.VISIBLE);
    }

    public void showError(String title, String description){
        txvw_title_00.setText(title);
        txvw_desc_00.setText(description);
        imvw_icon_00.setColorFilter(mActivity.getResources().getColor(R.color.colorPrimary));
        view_status_00.setBackgroundColor(mActivity.getResources().getColor(R.color.design_default_color_error));
        txvw_action_00.setTextColor(mActivity.getResources().getColor(R.color.design_default_color_error));
        show();

    }

    public void showInfo(String title, String description){
        txvw_title_00.setText(title);
        txvw_desc_00.setText(description);
        imvw_icon_00.setImageResource(R.drawable.ic_info_outline);
        imvw_icon_00.setColorFilter(Color.parseColor("#039be5"));
        view_status_00.setBackgroundColor(Color.parseColor("#039be5"));
        txvw_action_00.setTextColor(Color.parseColor("#039be5"));
        show();

    }

    public void setAction(String text){
        txvw_action_00.setText(text);
    }

    public void showError(String title, String description, int image){
        showError(title, description);
        imvw_icon_00.setImageResource(image);
    }

    private OnCloseLister onCloseLister;
    public void setOnCloseLister(OnCloseLister onCloseLister){
        this.onCloseLister = onCloseLister;
    }
    public interface OnCloseLister{
        void onClose(String action);
    }

}
