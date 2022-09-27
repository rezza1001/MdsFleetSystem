package com.mds.mobile.remote.entity.drivervehiclelist.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseDatum {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("fleet_id")
    @Expose
    private String fleetId;
    @SerializedName("license_plate")
    @Expose
    private String licensePlate;
    @SerializedName("merk")
    @Expose
    private String merk;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("stnk_expire_date")
    @Expose
    private String stnkExpireDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFleetId() {
        return fleetId;
    }

    public void setFleetId(String fleetId) {
        this.fleetId = fleetId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStnkExpireDate() {
        return stnkExpireDate;
    }

    public void setStnkExpireDate(String stnkExpireDate) {
        this.stnkExpireDate = stnkExpireDate;
    }

}