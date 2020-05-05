package com.autionsy.seller.entity;

import java.io.Serializable;

public class RotationAdvertisement implements Serializable {
    private String advertisementName;
    private String advertisementUrl;

    public String getAdvertisementName() {
        return advertisementName;
    }

    public void setAdvertisementName(String advertisementName) {
        this.advertisementName = advertisementName;
    }

    public String getAdvertisementUrl() {
        return advertisementUrl;
    }

    public void setAdvertisementUrl(String advertisementUrl) {
        this.advertisementUrl = advertisementUrl;
    }
}
