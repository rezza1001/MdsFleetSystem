package com.mds.mobile.remote.entity.fleetservice.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.mds.mobile.remote.entity.BaseEntity;

public class AddFleetServiceRequestEntity extends BaseEntity {

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
    @SerializedName("service_type")
    @Expose
    private String serviceType;
    @SerializedName("fleet_odometer")
    @Expose
    private String fleetOdometer;
    @SerializedName("complaint")
    @Expose
    private String complaint;
    @SerializedName("photo_1")
    @Expose
    private String photo1;
    @SerializedName("photo_2")
    @Expose
    private String photo2;

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

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getFleetOdometer() {
        return fleetOdometer;
    }

    public void setFleetOdometer(String fleetOdometer) {
        this.fleetOdometer = fleetOdometer;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

}