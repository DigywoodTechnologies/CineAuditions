package com.digywood.cineauditions.Pojo;

public class SingleAdvt {

    private int advtRefNo;
    private String orgId,downloadUrl,producer_id,caption, description,fileType,filename, startDate, endDate, contactName, contactNumber, emailId, createdTime, status;

    public SingleAdvt(int advtRefNo,String orgId,String producer_id, String caption, String description,String fileType,String fileName,String downloadUrl,String startDate, String endDate, String contactName, String contactNumber, String emailId, String createdTime, String status) {
        this.advtRefNo = advtRefNo;
        this.orgId = orgId;
        this.producer_id = producer_id;
        this.caption = caption;
        this.description=description;
        this.fileType=fileType;
        this.filename=fileName;
        this.downloadUrl=downloadUrl;
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

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
