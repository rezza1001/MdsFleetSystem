package com.mds.mobile.remote.entity.stnk.response;

import com.mds.mobile.remote.entity.BaseResponseEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddFleetTaxResponseEntity extends BaseResponseEntity {

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
