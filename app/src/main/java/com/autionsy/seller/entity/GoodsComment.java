package com.autionsy.seller.entity;

import java.io.Serializable;

public class GoodsComment implements Serializable {

    private String userCode;

    private String commentContent;

    private String goodsCode;

    private String pulishTime;

    private String describeScore;

    private String deliverySpeedScore;

    private String serviceMannerScore;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getPulishTime() {
        return pulishTime;
    }

    public void setPulishTime(String pulishTime) {
        this.pulishTime = pulishTime;
    }

    public String getDescribeScore() {
        return describeScore;
    }

    public void setDescribeScore(String describeScore) {
        this.describeScore = describeScore;
    }

    public String getDeliverySpeedScore() {
        return deliverySpeedScore;
    }

    public void setDeliverySpeedScore(String deliverySpeedScore) {
        this.deliverySpeedScore = deliverySpeedScore;
    }

    public String getServiceMannerScore() {
        return serviceMannerScore;
    }

    public void setServiceMannerScore(String serviceMannerScore) {
        this.serviceMannerScore = serviceMannerScore;
    }
}
