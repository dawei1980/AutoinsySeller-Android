package com.autionsy.seller.entity;

import java.io.Serializable;

public class GoodsImage implements Serializable {

    private String imageUrl;

    private String goodsCode;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }
}
