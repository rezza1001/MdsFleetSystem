package com.mds.mobile.absent;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.mds.mobile.R;
import com.mds.mobile.ui.MyDialog;

public class ErrorDialog extends MyDialog {

    private CardView card_00;
    private TextView txvw_title,txvw_description;
    private Button btn_verification;

    public ErrorDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int setLayout() {
        return R.layout.absent_dialog_error;
    }

    @Override
    protected void initLayout(View view) {
        card_00 = findViewById(R.id.card_00);
        txvw_title = findViewById(R.id.txvw_title);
        txvw_description = findViewById(R.id.txvw_description);
        btn_verification = findViewById(R.id.btn_verification);
        card_00.setVisibility(View.INVISIBLE);
        btn_verification.setOnClickListener(v -> {
            if (onFinishListener != null){
                onFinishListener.onFinish();
            }
            dismiss();
        });
    }

    @Override
    public void show() {
        super.show();
        card_00.setVisibility(View.VISIBLE);
        card_00.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.zoom_in));
    }

    public void show(String title, String description){
        show();
        txvw_title.setText(title);
        txvw_description.setText(description);
    }


    private OnFinishListener onFinishListener;
    public void setOnFinishListener(OnFinishListener onFinishListener){
        this.onFinishListener = onFinishListener;
    }
    public interface OnFinishListener{
        void onFinish();
    }
}
