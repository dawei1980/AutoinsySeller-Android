package com.autionsy.seller.entity;

import java.io.Serializable;

public class Lease implements Serializable {
    private String leaseId;
    private String leaseType;
    private String acreage;
    private String stallPosition;
    private String price;
    private String leaseTerm;
    private String title;
    private String describe;
    private String contacts;
    private String mobilePhoneNum;
    private String watchNumber;
    private String publishDate;
    private String leaseInfoSourceCode;
    private String username;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLeaseId() {
        return leaseId;
    }

    public void setLeaseId(String leaseId) {
        this.leaseId = leaseId;
    }

    public String getLeaseType() {
        return leaseType;
    }

    public void setLeaseType(String leaseType) {
        this.leaseType = leaseType;
    }

    public String getAcreage() {
        return acreage;
    }

    public void setAcreage(String acreage) {
        this.acreage = acreage;
    }

    public String getStallPosition() {
        return stallPosition;
    }

    public void setStallPosition(String stallPosition) {
        this.stallPosition = stallPosition;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLeaseTerm() {
        return leaseTerm;
    }

    public void setLeaseTerm(String leaseTerm) {
        this.leaseTerm = leaseTerm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getMobilePhoneNum() {
        return mobilePhoneNum;
    }

    public void setMobilePhoneNum(String mobilePhoneNum) {
        this.mobilePhoneNum = mobilePhoneNum;
    }

    public String getWatchNumber() {
        return watchNumber;
    }

    public void setWatchNumber(String watchNumber) {
        this.watchNumber = watchNumber;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getLeaseInfoSourceCode() {
        return leaseInfoSourceCode;
    }

    public void setLeaseInfoSourceCode(String leaseInfoSourceCode) {
        this.leaseInfoSourceCode = leaseInfoSourceCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
