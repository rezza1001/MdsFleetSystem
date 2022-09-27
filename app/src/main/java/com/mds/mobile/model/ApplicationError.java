package com.mds.mobile.model;

/**
 * Created by pho0890910 on 8/4/2018.
 */
public class ApplicationError {

    private String responseCode;
    private String message;

    public ApplicationError(String responseCode, String message) {
        this.responseCode = responseCode;
        this.message = message;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
