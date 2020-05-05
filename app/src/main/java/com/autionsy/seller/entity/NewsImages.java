package com.autionsy.seller.entity;

import java.io.Serializable;

public class NewsImages implements Serializable {

    private String newsImageUrl;

    private String newsCode;

    public String getNewsImageUrl() {
        return newsImageUrl;
    }

    public void setNewsImageUrl(String newsImageUrl) {
        this.newsImageUrl = newsImageUrl;
    }

    public String getNewsCode() {
        return newsCode;
    }

    public void setNewsCode(String newsCode) {
        this.newsCode = newsCode;
    }
}
