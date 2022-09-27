package com.mds.mobile.model;

import java.io.Serializable;

public class ClientContactUsItem implements Serializable {

    private String branch;
    private String address;
    private String phone;

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
