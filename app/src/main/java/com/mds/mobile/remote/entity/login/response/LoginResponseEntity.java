package com.mds.mobile.remote.entity.login.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.mds.mobile.remote.entity.BaseResponseEntity;

public class LoginResponseEntity extends BaseResponseEntity {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}