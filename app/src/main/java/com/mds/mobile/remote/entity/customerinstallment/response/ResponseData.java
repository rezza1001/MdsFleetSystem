package com.mds.mobile.remote.entity.customerinstallment.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseData implements Serializable {

    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("agreement_id")
    @Expose
    private String agreementId;
    @SerializedName("branch")
    @Expose
    private String branch;
    @SerializedName("tenor")
    @Expose
    private String tenor;
    @SerializedName("contract_date")
    @Expose
    private String contractDate;
    @SerializedName("installment")
    @Expose
    private String installment;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("last_payment_date")
    @Expose
    private String lastPaymentDate;
    @SerializedName("next_installment_date")
    @Expose
    private String nextInstallmentDate;
    @SerializedName("asset_description")
    @Expose
    private String assetDescription;
    @SerializedName("contract_status")
    @Expose
    private String contractStatus;

    @SerializedName("last_payment_period")
    @Expose
    private String lastPaymentPeriod;

    @SerializedName("next_installment_period")
    @Expose
    private String nextInstallmentPeriod;

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

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getTenor() {
        return tenor;
    }

    public void setTenor(String tenor) {
        this.tenor = tenor;
    }

    public String getContractDate() {
        return contractDate;
    }

    public void setContractDate(String contractDate) {
        this.contractDate = contractDate;
    }

    public String getInstallment() {
        return installment;
    }

    public void setInstallment(String installment) {
        this.installment = installment;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(String lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    public String getNextInstallmentDate() {
        return nextInstallmentDate;
    }

    public void setNextInstallmentDate(String nextInstallmentDate) {
        this.nextInstallmentDate = nextInstallmentDate;
    }

    public String getAssetDescription() {
        return assetDescription;
    }

    public void setAssetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getLastPaymentPeriod() {
        return lastPaymentPeriod;
    }

    public void setLastPaymentPeriod(String lastPaymentPeriod) {
        this.lastPaymentPeriod = lastPaymentPeriod;
    }

    public String getNextInstallmentPeriod() {
        return nextInstallmentPeriod;
    }

    public void setNextInstallmentPeriod(String nextInstallmentPeriod) {
        this.nextInstallmentPeriod = nextInstallmentPeriod;
    }
}