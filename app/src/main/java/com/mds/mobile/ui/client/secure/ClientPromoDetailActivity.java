package com.mds.mobile.ui.client.secure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import com.mds.mobile.R;
import com.mds.mobile.model.ClientPromoItem;
import com.mds.mobile.remote.ServiceGenerator;
import com.mds.mobile.remote.entity.promo.response.ResponseDatum;

public class ClientPromoDetailActivity extends ClientBaseUi {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_client_promo_detail;
    }

    @Override
    protected Integer getNavigationMenuItemIndex() {
        return R.id.nav_promo;
    }

    ImageButton ibBack;

    ResponseDatum imgData;
    ClientPromoItem imgItem;

    @Override
    protected void onMyCreate() {

        ibBack = findViewById(R.id.ibBack);
        ibBack.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();

        imgData = (ResponseDatum)bundle.getSerializable("data_promo");
        imgItem = (ClientPromoItem)bundle.getSerializable("data_promo_2");

        TextView title = findViewById(R.id.tv_title);
        TextView content = findViewById(R.id.tv_content);
        ImageView img = findViewById(R.id.iv_promo);

        if(imgData!=null){
            title.setText(imgData.getTitle());
            content.setText(imgData.getDescription());
            Glide
                    .with(this)
                    .load(ServiceGenerator.API_PHOTO_URL + imgData.getPhoto())
                    .fitCenter()
                    .placeholder(R.drawable.ic_camera)
                    .into(img);
        } else {
            title.setText(imgItem.getTitle());
            content.setText(imgItem.getDescription());
            Glide
                    .with(this)
                    .load(imgItem.getPhoto())
                    .fitCenter()
                    .placeholder(R.drawable.ic_camera)
                    .into(img);
        }

    }

    @Override
    public void onClick(View view) {

        if (isFastDoubleClick()) return;

        if (view.getId() == R.id.ibBack) {

//            if(imgItem!=null){
                onBackPressed();
//            } else {
//                Intent intent = new Intent(getApplicationContext(), ClientPromoActivity.class);
//                startActivity(intent);
//                this.finish();
//            }

        } else {
            super.onClick(view);
        }
    }

    @Override
    public void onBackPressed() {

        if(imgItem!=null){
            super.onBackPressed();
        } else {
            Intent intent = new Intent(getApplicationContext(), ClientPromoActivity.class);
            startActivity(intent);
            this.finish();
        }

    }
}
