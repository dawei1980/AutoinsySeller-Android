package com.autionsy.seller.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Discover implements Serializable {
    private String headerUrl;
    private String sellerName;
    private String publishTime;
    private ArrayList<DiscoverImage> imageList;

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public ArrayList<DiscoverImage> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<DiscoverImage> imageList) {
        this.imageList = imageList;
    }
}
