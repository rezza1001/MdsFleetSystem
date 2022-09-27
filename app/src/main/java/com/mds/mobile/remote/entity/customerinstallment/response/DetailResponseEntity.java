package com.mds.mobile.remote.entity.customerinstallment.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.mds.mobile.remote.entity.BaseResponseEntity;

public class DetailResponseEntity extends BaseResponseEntity {

    @SerializedName("response_data")
    @Expose
    private ResponseData responseData;

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }

}