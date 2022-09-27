package com.mds.mobile.remote.entity.driverinsurance.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.mds.mobile.remote.entity.BaseEntity;

public class AddFleetInsuranceClaimRequestEntity extends BaseEntity {

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
    @SerializedName("claim_type")
    @Expose
    private String claimType;
    @SerializedName("driver_name")
    @Expose
    private String driverName;
    @SerializedName("driver_phone")
    @Expose
    private String driverPhone;
    @SerializedName("incident_date")
    @Expose
    private String incidentDate;
    @SerializedName("incident_location")
    @Expose
    private String incidentLocation;
    @SerializedName("incident_speed")
    @Expose
    private String incidentSpeed;
    @SerializedName("incident_description")
    @Expose
    private String incidentDescription;
    @SerializedName("incident_photo_1")
    @Expose
    private String incidentPhoto1;
    @SerializedName("incident_photo_2")
    @Expose
    private String incidentPhoto2;
    @SerializedName("incident_photo_3")
    @Expose
    private String incidentPhoto3;
    @SerializedName("sim_photo_1")
    @Expose
    private String simPhoto1;
    @SerializedName("sim_photo_2")
    @Expose
    private String simPhoto2;
    @SerializedName("stnk_photo_1")
    @Expose
    private String stnkPhoto1;
    @SerializedName("stnk_photo_2")
    @Expose
    private String stnkPhoto2;
    @SerializedName("chassis_no_photo_1")
    @Expose
    private String chassisNoPhoto1;
    @SerializedName("letter_police_photo_1")
    @Expose
    private String letterPolicePhoto1;
    @SerializedName("letter_warrant_photo_1")
    @Expose
    private String letterWarrantPhoto1;
    @SerializedName("damage_description")
    @Expose
    private String damageDescription;

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

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(String incidentDate) {
        this.incidentDate = incidentDate;
    }

    public String getIncidentLocation() {
        return incidentLocation;
    }

    public void setIncidentLocation(String incidentLocation) {
        this.incidentLocation = incidentLocation;
    }

    public String getIncidentSpeed() {
        return incidentSpeed;
    }

    public void setIncidentSpeed(String incidentSpeed) {
        this.incidentSpeed = incidentSpeed;
    }

    public String getIncidentDescription() {
        return incidentDescription;
    }

    public void setIncidentDescription(String incidentDescription) {
        this.incidentDescription = incidentDescription;
    }

    public String getIncidentPhoto1() {
        return incidentPhoto1;
    }

    public void setIncidentPhoto1(String incidentPhoto1) {
        this.incidentPhoto1 = incidentPhoto1;
    }

    public String getIncidentPhoto2() {
        return incidentPhoto2;
    }

    public void setIncidentPhoto2(String incidentPhoto2) {
        this.incidentPhoto2 = incidentPhoto2;
    }

    public String getIncidentPhoto3() {
        return incidentPhoto3;
    }

    public void setIncidentPhoto3(String incidentPhoto3) {
        this.incidentPhoto3 = incidentPhoto3;
    }

    public String getSimPhoto1() {
        return simPhoto1;
    }

    public void setSimPhoto1(String simPhoto1) {
        this.simPhoto1 = simPhoto1;
    }

    public String getSimPhoto2() {
        return simPhoto2;
    }

    public void setSimPhoto2(String simPhoto2) {
        this.simPhoto2 = simPhoto2;
    }

    public String getStnkPhoto1() {
        return stnkPhoto1;
    }

    public void setStnkPhoto1(String stnkPhoto1) {
        this.stnkPhoto1 = stnkPhoto1;
    }

    public String getStnkPhoto2() {
        return stnkPhoto2;
    }

    public void setStnkPhoto2(String stnkPhoto2) {
        this.stnkPhoto2 = stnkPhoto2;
    }

    public String getChassisNoPhoto1() {
        return chassisNoPhoto1;
    }

    public void setChassisNoPhoto1(String chassisNoPhoto1) {
        this.chassisNoPhoto1 = chassisNoPhoto1;
    }

    public String getLetterPolicePhoto1() {
        return letterPolicePhoto1;
    }

    public void setLetterPolicePhoto1(String letterPolicePhoto1) {
        this.letterPolicePhoto1 = letterPolicePhoto1;
    }

    public String getLetterWarrantPhoto1() {
        return letterWarrantPhoto1;
    }

    public void setLetterWarrantPhoto1(String letterWarrantPhoto1) {
        this.letterWarrantPhoto1 = letterWarrantPhoto1;
    }

    public String getDamageDescription() {
        return damageDescription;
    }

    public void setDamageDescription(String damageDescription) {
        this.damageDescription = damageDescription;
    }
}
