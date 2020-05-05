package com.autionsy.seller.entity;

import java.io.Serializable;

public class GoodsType implements Serializable {

    private String mainTypeCode;

    /**
     * 主分类
     * */
    private String mainType;

    /**
     * 商品子分类标签
     * */
    private String subTypeCode;

    /**
     * 子分类
     * */
    private String subType;

    /**
     * 子分类图片
     * */
    private String subTypeImage;

    public String getSubTypeImage() {
        return subTypeImage;
    }

    public void setSubTypeImage(String subTypeImage) {
        this.subTypeImage = subTypeImage;
    }

    public String getMainTypeCode() {
        return mainTypeCode;
    }

    public void setMainTypeCode(String mainTypeCode) {
        this.mainTypeCode = mainTypeCode;
    }

    public String getMainType() {
        return mainType;
    }

    public void setMainType(String mainType) {
        this.mainType = mainType;
    }

    public String getSubTypeCode() {
        return subTypeCode;
    }

    public void setSubTypeCode(String subTypeCode) {
        this.subTypeCode = subTypeCode;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }
}
