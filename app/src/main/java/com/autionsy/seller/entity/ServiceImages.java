package com.autionsy.seller.entity;


import java.io.Serializable;

public class ServiceImages implements Serializable {

    private String serviceImagesUrl;

    private String serviceCode;

    public String getServiceImagesUrl() {
        return serviceImagesUrl;
    }

    public void setServiceImagesUrl(String serviceImagesUrl) {
        this.serviceImagesUrl = serviceImagesUrl;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }
}
