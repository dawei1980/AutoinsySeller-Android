package com.autionsy.seller.entity;

import java.io.Serializable;

public class Advertisement implements Serializable {
    private String appRotateAdvertisementName;
    private String appRotateAdvertisementUrl;
    private String appPublishTime;

    public String getAppRotateAdvertisementName() {
        return appRotateAdvertisementName;
    }

    public void setAppRotateAdvertisementName(String appRotateAdvertisementName) {
        this.appRotateAdvertisementName = appRotateAdvertisementName;
    }

    public String getAppRotateAdvertisementUrl() {
        return appRotateAdvertisementUrl;
    }

    public void setAppRotateAdvertisementUrl(String appRotateAdvertisementUrl) {
        this.appRotateAdvertisementUrl = appRotateAdvertisementUrl;
    }

    public String getAppPublishTime() {
        return appPublishTime;
    }

    public void setAppPublishTime(String appPublishTime) {
        this.appPublishTime = appPublishTime;
    }
}
