package com.mds.mobile.absent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mds.mobile.R;
import com.mds.mobile.base.MyView;
import com.mds.mobile.remote.post.FileProcessing;
import com.mds.mobile.remote.post.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageChooserView extends MyView {

    private RoundedImageView imvw_photo;
    private ImageView imvw_photoReal;
    private TextView txvw_watermark;
    private RelativeLayout lnly_watermark,rvly_view,rvly_loading,rvly_photo,rvly_selectPhoto;
    private boolean hasImage = false;
    private String mPath = "";
    private String imageName = "";

    public ImageChooserView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int setlayout() {
        return R.layout.component_view_imagechooser;
    }

    @Override
    protected void initLayout() {
        imvw_photo      = findViewById(R.id.imvw_photo);
        txvw_watermark  = findViewById(R.id.txvw_watermark);
        lnly_watermark  = findViewById(R.id.lnly_watermark);
        rvly_view       = findViewById(R.id.rvly_view);
        imvw_photoReal  = findViewById(R.id.imvw_photoReal);
        rvly_loading    = findViewById(R.id.rvly_loading);
        rvly_photo      = findViewById(R.id.rvly_photo);
        rvly_selectPhoto  = findViewById(R.id.rvly_selectPhoto);
        lnly_watermark.setVisibility(GONE);
        rvly_selectPhoto.setVisibility(GONE);
        imvw_photoReal.setVisibility(INVISIBLE);
        txvw_watermark.setText("");
    }

    @Override
    protected void initListener() {
        findViewById(R.id.rvly_selectPhoto).setOnClickListener(view -> takePhoto());

        findViewById(R.id.imvw_camera).setOnClickListener(v -> takePhoto());
    }
    private void takePhoto(){
        if (mPath.isEmpty()){
            Utility.showToastError(mActivity,"Path not found");
            return;
        }
        imageName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()) +".jpg";
        PermissionAccess.openCamera(mActivity,mPath,imageName);
    }

    public void setPath(String path){
        mPath = path;
    }

    public boolean hasImage(){
        return hasImage;
    }

    public void registerActivityResult(int requestCode, int resultCode, @Nullable Intent resultData) {
        if (requestCode == PermissionAccess.OPEN_CAMERA && resultCode == Activity.RESULT_OK){
            rvly_selectPhoto.setVisibility(VISIBLE);
            hasImage = true;

            Bitmap fileImage = FileProcessing.openImage(mActivity,mPath+imageName);
            int width = fileImage.getWidth();
            int height = fileImage.getHeight();
            int mWidth = width;
            int mHeight = height;
            if (width > 1000 && width < 2000){
                mWidth = (int) (width * 0.2);
                mHeight = (int) (height * 0.2);
            }
            else  if (width > 2000 ){
                mWidth = (int) (width * 0.1);
                mHeight = (int) (height * 0.1);
            }



            LayoutParams lp = (LayoutParams) rvly_photo.getLayoutParams();
            lp.width = Utility.dpToPx(mActivity, mWidth);
            lp.height = Utility.dpToPx(mActivity, mHeight);
            rvly_photo.setLayoutParams(lp);
            imvw_photoReal.setLayoutParams(lp);

            Glide.with(mActivity).load(fileImage).into(imvw_photo);
            Glide.with(mActivity).load(fileImage).into(imvw_photoReal);
        }
    }

    public void setWaterMark(String message){
        txvw_watermark.setText(message);
    }

    public RelativeLayout getViewImage(){
        if (!txvw_watermark.getText().toString().isEmpty()){
            lnly_watermark.setVisibility(VISIBLE);
        }
        rvly_loading.setVisibility(VISIBLE);
        imvw_photo.setVisibility(GONE);
        imvw_photoReal.setVisibility(VISIBLE);

        new Handler().postDelayed(() -> {
            imvw_photo.setVisibility(VISIBLE);
            imvw_photoReal.setVisibility(GONE);
            rvly_loading.setVisibility(GONE);
            lnly_watermark.setVisibility(GONE);
        },1000);
        return rvly_view;
    }




}
