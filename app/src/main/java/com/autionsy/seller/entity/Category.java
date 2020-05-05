package com.autionsy.seller.entity;

import java.io.Serializable;

public class Category implements Serializable {
    private String mainClassifyCode;
    private String subClassify;
    private String subClassifyId;
    private String subClassifyImage;
    private String mainClassifyName;
    private String classifyRemark;

    public String getMainClassifyName() {
        return mainClassifyName;
    }

    public void setMainClassifyName(String mainClassifyName) {
        this.mainClassifyName = mainClassifyName;
    }

    public String getClassifyRemark() {
        return classifyRemark;
    }

    public void setClassifyRemark(String classifyRemark) {
        this.classifyRemark = classifyRemark;
    }

    public String getMainClassifyCode() {
        return mainClassifyCode;
    }

    public void setMainClassifyCode(String mainClassifyCode) {
        this.mainClassifyCode = mainClassifyCode;
    }

    public String getSubClassify() {
        return subClassify;
    }

    public void setSubClassify(String subClassify) {
        this.subClassify = subClassify;
    }

    public String getSubClassifyId() {
        return subClassifyId;
    }

    public void setSubClassifyId(String subClassifyId) {
        this.subClassifyId = subClassifyId;
    }

    public String getSubClassifyImage() {
        return subClassifyImage;
    }

    public void setSubClassifyImage(String subClassifyImage) {
        this.subClassifyImage = subClassifyImage;
    }
}
