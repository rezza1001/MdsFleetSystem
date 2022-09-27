package com.mds.mobile.model;

import java.io.Serializable;

public class NotificationItem implements Serializable {

    private String dateNotif;
    private String title;
    private String description;

    public String getDateNotif() {
        return dateNotif;
    }

    public void setDateNotif(String dateNotif) {
        this.dateNotif = dateNotif;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
