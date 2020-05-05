package com.autionsy.seller.entity;


import java.io.Serializable;

public class Recruit implements Serializable {

    private String recruitId;

    /**
     * 招聘标题
     * */
    private String title;

    /**
     * 公司名称
     * */
    private String companyName;

    /**
     *最低薪资
     * */
    private String startSalary;

    /**
     * 商家地址
     * */
    private String sellerAddress;

    /**
     * 招聘人数
     * */
    private String recruitPersonNumber;

    /**
     * 学历要求
     * */
    private String educationRequirement;

    /**
     * 工作经验
     * */
    private String experience;

    /**
     * 职位
     * */
    private String position;

    private String positionDescribe;

    /**
     * 发布时间
     * */
    private String publishTime;

    /**
     * 最高薪资
     * */
    private String endSalary;

    /**
     * 手机号码
     * */
    private String mobilePhoneNum;

    /**
     * 是否认证
     * */
    private String isAuthentication;

    /**
     * 企业人数范围
     * */
    private String companyPeopleNum;

    /**
     * 招聘编号
     * */
    private String recruitCode;

    private String username;

    public String getRecruitId() {
        return recruitId;
    }

    public void setRecruitId(String recruitId) {
        this.recruitId = recruitId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRecruitCode() {
        return recruitCode;
    }

    public void setRecruitCode(String recruitCode) {
        this.recruitCode = recruitCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStartSalary() {
        return startSalary;
    }

    public void setStartSalary(String startSalary) {
        this.startSalary = startSalary;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public String getRecruitPersonNumber() {
        return recruitPersonNumber;
    }

    public void setRecruitPersonNumber(String recruitPersonNumber) {
        this.recruitPersonNumber = recruitPersonNumber;
    }

    public String getEducationRequirement() {
        return educationRequirement;
    }

    public void setEducationRequirement(String educationRequirement) {
        this.educationRequirement = educationRequirement;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getEndSalary() {
        return endSalary;
    }

    public void setEndSalary(String endSalary) {
        this.endSalary = endSalary;
    }

    public String getMobilePhoneNum() {
        return mobilePhoneNum;
    }

    public void setMobilePhoneNum(String mobilePhoneNum) {
        this.mobilePhoneNum = mobilePhoneNum;
    }

    public String getIsAuthentication() {
        return isAuthentication;
    }

    public void setIsAuthentication(String isAuthentication) {
        this.isAuthentication = isAuthentication;
    }

    public String getCompanyPeopleNum() {
        return companyPeopleNum;
    }

    public void setCompanyPeopleNum(String companyPeopleNum) {
        this.companyPeopleNum = companyPeopleNum;
    }

    public String getPositionDescribe() {
        return positionDescribe;
    }

    public void setPositionDescribe(String positionDescribe) {
        this.positionDescribe = positionDescribe;
    }
}
