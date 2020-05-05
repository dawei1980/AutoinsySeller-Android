package com.autionsy.seller.entity;

import java.io.Serializable;
import java.util.Objects;

public class RescueImages implements Serializable {

    private String imageUrl;

    private String imageTitle;

    private long rescueId;


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public long getRescueId() {
        return rescueId;
    }

    public void setRescueId(long rescueId) {
        this.rescueId = rescueId;
    }

}
