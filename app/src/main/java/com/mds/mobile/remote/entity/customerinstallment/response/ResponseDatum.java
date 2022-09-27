package com.mds.mobile.remote.entity.customerinstallment.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseDatum {

    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("agreement_id")
    @Expose
    private String agreementId;
    @SerializedName("tenor")
    @Expose
    private String tenor;
    @SerializedName("sisa_tenor")
    @Expose
    private String sisaTenor;
    @SerializedName("over_due_days")
    @Expose
    private String overDueDays;
    @SerializedName("asset_description")
    @Expose
    private String assetDescription;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public String getTenor() {
        return tenor;
    }

    public void setTenor(String tenor) {
        this.tenor = tenor;
    }

    public String getSisaTenor() {
        return sisaTenor;
    }

    public void setSisaTenor(String sisaTenor) {
        this.sisaTenor = sisaTenor;
    }

    public String getOverDueDays() {
        return overDueDays;
    }

    public void setOverDueDays(String overDueDays) {
        this.overDueDays = overDueDays;
    }

    public String getAssetDescription() {
        return assetDescription;
    }

    public void setAssetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
    }

}