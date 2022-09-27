package com.mds.mobile.remote.entity.history.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.mds.mobile.remote.entity.BaseEntity;

public class GetFleetOdometerHistoryRequestEntity extends BaseEntity {

    @SerializedName("request_type")
    @Expose
    private String requestType;
    @SerializedName("authorize")
    @Expose
    private String authorize;
    @SerializedName("user_code")
    @Expose
    private String userCode;
    @SerializedName("fleet_id")
    @Expose
    private String fleetId;

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

    public String getFleetId() {
        return fleetId;
    }

    public void setFleetId(String fleetId) {
        this.fleetId = fleetId;
    }

}
