package com.mds.mobile.model;

import java.io.Serializable;

public class DriverFleetListItem implements Serializable {
    private String id;
    private String licensePlate;
    private String content;
    private String contentBundle;
    private String stnkExpireDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentBundle() {
        return contentBundle;
    }

    public void setContentBundle(String contentBundle) {
        this.contentBundle = contentBundle;
    }

    public String getStnkExpireDate() {
        return stnkExpireDate;
    }

    public void setStnkExpireDate(String stnkExpireDate) {
        this.stnkExpireDate = stnkExpireDate;
    }
}
