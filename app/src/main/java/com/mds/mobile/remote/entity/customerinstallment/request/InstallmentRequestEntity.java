package com.mds.mobile.remote.entity.customerinstallment.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.mds.mobile.remote.entity.BaseEntity;

public class InstallmentRequestEntity extends BaseEntity {

    @SerializedName("request_type")
    @Expose
    private String requestType;
    @SerializedName("authorize")
    @Expose
    private String authorize;
    @SerializedName("user_code")
    @Expose
    private String userCode;

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

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

}