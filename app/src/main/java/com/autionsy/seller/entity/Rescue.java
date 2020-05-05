package com.autionsy.seller.entity;

import java.io.Serializable;

public class Rescue implements Serializable {
    private String rescueId;
    private String rescueTitle;
    private String rescuePhoneNumber;
    private String rescueAddressDetail;
    private String rescueServiceScope;
    private String rescueCompanyIntroduce;
    private String rescueCode;
    private String rescueCompanyName;
    private String username;
    private String rescueImageUrl;

    public String getRescueImageUrl() {
        return rescueImageUrl;
    }

    public void setRescueImageUrl(String rescueImageUrl) {
        this.rescueImageUrl = rescueImageUrl;
    }

    public String getRescueId() {
        return rescueId;
    }

    public void setRescueId(String rescueId) {
        this.rescueId = rescueId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRescueTitle() {
        return rescueTitle;
    }

    public void setRescueTitle(String rescueTitle) {
        this.rescueTitle = rescueTitle;
    }

    public String getRescuePhoneNumber() {
        return rescuePhoneNumber;
    }

    public void setRescuePhoneNumber(String rescuePhoneNumber) {
        this.rescuePhoneNumber = rescuePhoneNumber;
    }

    public String getRescueAddressDetail() {
        return rescueAddressDetail;
    }

    public void setRescueAddressDetail(String rescueAddressDetail) {
        this.rescueAddressDetail = rescueAddressDetail;
    }

    public String getRescueServiceScope() {
        return rescueServiceScope;
    }

    public void setRescueServiceScope(String rescueServiceScope) {
        this.rescueServiceScope = rescueServiceScope;
    }

    public String getRescueCompanyIntroduce() {
        return rescueCompanyIntroduce;
    }

    public void setRescueCompanyIntroduce(String rescueCompanyIntroduce) {
        this.rescueCompanyIntroduce = rescueCompanyIntroduce;
    }

    public String getRescueCode() {
        return rescueCode;
    }

    public void setRescueCode(String rescueCode) {
        this.rescueCode = rescueCode;
    }

    public String getRescueCompanyName() {
        return rescueCompanyName;
    }

    public void setRescueCompanyName(String rescueCompanyName) {
        this.rescueCompanyName = rescueCompanyName;
    }
}
