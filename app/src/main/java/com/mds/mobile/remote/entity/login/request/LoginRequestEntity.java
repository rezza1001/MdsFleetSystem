package com.mds.mobile.remote.entity.login.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.mds.mobile.remote.entity.BaseEntity;

public class LoginRequestEntity extends BaseEntity {

    @SerializedName("request_type")
    @Expose
    private String requestType;
    @SerializedName("authorize")
    @Expose
    private String authorize;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getAuthorize() {
        return authorize;
    }

    public void setAuthorize(String authorize) {
        this.authorize = authorize;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}