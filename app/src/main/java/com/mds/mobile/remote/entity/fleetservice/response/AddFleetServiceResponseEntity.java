package com.mds.mobile.remote.entity.fleetservice.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.mds.mobile.remote.entity.BaseResponseEntity;

public class AddFleetServiceResponseEntity extends BaseResponseEntity {

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
