package com.mds.mobile.remote.entity.stnk.request;

import com.mds.mobile.remote.entity.BaseEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddFleetTaxRequestEntity extends BaseEntity{

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
    @SerializedName("tax_type")
    @Expose
    private String taxType;
    @SerializedName("date_expired_tax")
    @Expose
    private String dateExpiredTax;
    @SerializedName("date_submitted_doc")
    @Expose
    private String dateSubmittedDoc;
    @SerializedName("description")
    @Expose
    private String description;

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

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public String getDateExpiredTax() {
        return dateExpiredTax;
    }

    public void setDateExpiredTax(String dateExpiredTax) {
        this.dateExpiredTax = dateExpiredTax;
    }

    public String getDateSubmittedDoc() {
        return dateSubmittedDoc;
    }

    public void setDateSubmittedDoc(String dateSubmittedDoc) {
        this.dateSubmittedDoc = dateSubmittedDoc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}