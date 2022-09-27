package com.mds.mobile.remote.entity.odometer.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.mds.mobile.remote.entity.BaseEntity;

public class AddFleetOdometerRequestEntity extends BaseEntity {

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
    @SerializedName("fleet_odometer")
    @Expose
    private String fleetOdometer;
    @SerializedName("photo_1")
    @Expose
    private String photo1;

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

    public String getFleetOdometer() {
        return fleetOdometer;
    }

    public void setFleetOdometer(String fleetOdometer) {
        this.fleetOdometer = fleetOdometer;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }
}