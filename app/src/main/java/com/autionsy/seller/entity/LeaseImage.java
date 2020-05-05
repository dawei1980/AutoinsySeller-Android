package com.autionsy.seller.entity;

import java.io.Serializable;

public class LeaseImage implements Serializable {

    private long leaseImageId;

    private String leaseCode;

    private String leaseImageUrl;

    public long getLeaseImageId() {
        return leaseImageId;
    }

    public void setLeaseImageId(long leaseImageId) {
        this.leaseImageId = leaseImageId;
    }

    public String getLeaseCode() {
        return leaseCode;
    }

    public void setLeaseCode(String leaseCode) {
        this.leaseCode = leaseCode;
    }

    public String getLeaseImageUrl() {
        return leaseImageUrl;
    }

    public void setLeaseImageUrl(String leaseImageUrl) {
        this.leaseImageUrl = leaseImageUrl;
    }
}
