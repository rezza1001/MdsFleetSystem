package com.mds.mobile.remote.entity.faq.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.mds.mobile.remote.entity.BaseResponseEntity;

public class GetFaqResponseEntity extends BaseResponseEntity {

    @SerializedName("response_data")
    @Expose
    private List<ResponseDatum> responseData = null;

    public List<ResponseDatum> getResponseData() {
        return responseData;
    }

    public void setResponseData(List<ResponseDatum> responseData) {
        this.responseData = responseData;
    }
}
