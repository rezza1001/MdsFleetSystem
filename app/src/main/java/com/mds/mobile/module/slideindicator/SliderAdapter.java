package com.mds.mobile.module.slideindicator;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.mds.mobile.R;
import com.mds.mobile.remote.entity.promo.response.ResponseDatum;
import com.mds.mobile.ui.client.secure.ClientPromoActivity;
import com.mds.mobile.ui.client.secure.ClientPromoDetailActivity;
import com.mds.mobile.ui.driver.secure.DriverStnkActivity;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private List<String> imgResources;
    private List<ResponseDatum> imgData;

    public SliderAdapter(Context context, List<String> imgResources, List<ResponseDatum> imgData) {
        this.context = context;
        this.imgResources = imgResources;
        this.imgData = imgData;
    }

    @Override
    public int getCount() {
        return imgResources.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);

//        int imageResourceId = context.getResources().getIdentifier(imgResources.get(position),
//                "drawable", context.getPackageName());

        ImageView imageView = view.findViewById(R.id.iv_item_slider);
//        imageView.setImageResource(imageResourceId);

        String temp = imgResources.get(position);

        Glide.with(context).load(temp)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ClientPromoDetailActivity.class);
                intent.putExtra("data_promo",imgData.get(position));
                context.startActivity(intent);
            }
        });

//        TextView textView = (TextView) view.findViewById(R.id.tv_item_slider);
//        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.ll_item_slider);

//        textView.setText(colorName.get(position));
//        linearLayout.setBackgroundColor(color.get(position));

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
