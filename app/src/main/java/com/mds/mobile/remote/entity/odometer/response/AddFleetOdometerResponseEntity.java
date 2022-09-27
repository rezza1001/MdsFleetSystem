package com.mds.mobile.remote.entity.odometer.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.mds.mobile.remote.entity.BaseResponseEntity;

public class AddFleetOdometerResponseEntity extends BaseResponseEntity {

    @SerializedName("response_data")
    @Expose
    private String responseData;

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

}