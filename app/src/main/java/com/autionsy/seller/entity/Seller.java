package com.autionsy.seller.entity;

import java.io.Serializable;

public class Seller implements Serializable {
    /**id*/
    private long sellerId;

    /**用户名（手机号）*/
    private String username;

    /**密码*/
    private String password;

    /**sms_validate_code*/
    private String smsValidateCode;

    /**身份证号码*/
    private String identifyCardNumber;

    /**身份证正面照片*/
    private String identifyFrontImageUrl;

    /**身份证背面照片*/
    private String identifyBackImageUrl;

    /**营业执照照片*/
    private String businessLicenceImageUrl;

    private String nickName;

    /**商家地址*/
    private String sellerAddress;

    /**是否认证*/
    private String isAuthentication;

    /**商家手机号码*/
    private String sellerMobilePhoneNumber;

    /**商家简介*/
    private String describe;

    private String score;

    /**头像*/
    private String headUrl;

    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSmsValidateCode() {
        return smsValidateCode;
    }

    public void setSmsValidateCode(String smsValidateCode) {
        this.smsValidateCode = smsValidateCode;
    }

    public String getIdentifyCardNumber() {
        return identifyCardNumber;
    }

    public void setIdentifyCardNumber(String identifyCardNumber) {
        this.identifyCardNumber = identifyCardNumber;
    }

    public String getIdentifyFrontImageUrl() {
        return identifyFrontImageUrl;
    }

    public void setIdentifyFrontImageUrl(String identifyFrontImageUrl) {
        this.identifyFrontImageUrl = identifyFrontImageUrl;
    }

    public String getIdentifyBackImageUrl() {
        return identifyBackImageUrl;
    }

    public void setIdentifyBackImageUrl(String identifyBackImageUrl) {
        this.identifyBackImageUrl = identifyBackImageUrl;
    }

    public String getBusinessLicenceImageUrl() {
        return businessLicenceImageUrl;
    }

    public void setBusinessLicenceImageUrl(String businessLicenceImageUrl) {
        this.businessLicenceImageUrl = businessLicenceImageUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public String getIsAuthentication() {
        return isAuthentication;
    }

    public void setIsAuthentication(String isAuthentication) {
        this.isAuthentication = isAuthentication;
    }

    public String getSellerMobilePhoneNumber() {
        return sellerMobilePhoneNumber;
    }

    public void setSellerMobilePhoneNumber(String sellerMobilePhoneNumber) {
        this.sellerMobilePhoneNumber = sellerMobilePhoneNumber;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }
}
