package com.digywood.cineauditions.Pojo;

public class SingleAdvt {

    private int advtRefNo;
    private String orgId,producer_id,caption, description, startDate, endDate, contactName, contactNumber, emailId, createdTime, status;
    private byte[] image;

    public SingleAdvt(int advtRefNo, String orgId, String producer_id, String caption, String description, String startDate, String endDate, String contactName, String contactNumber, String emailId, String createdTime, String status) {
        this.advtRefNo = advtRefNo;
        this.orgId = orgId;
        this.producer_id = producer_id;
        this.caption = caption;
        this.description=description;
        this.image=image;
        this.startDate = startDate;
        this.endDate = endDate;
        this.contactName=contactName;
        this.contactNumber = contactNumber;
        this.emailId = emailId;
        this.createdTime=createdTime;
        this.status = status;
    }
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getProducer_id() {
        return producer_id;
    }

    public void setProducer_id(String producer_id) {
        this.producer_id = producer_id;
    }

    public int getAdvtRefNo() {
        return advtRefNo;
    }

    public void setAdvtRefNo(int advtRefNo) {
        this.advtRefNo = advtRefNo;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getStartDate() { return startDate;  }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

}
