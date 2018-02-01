package com.digywood.cineauditions.Pojo;

public class SinglePreference {
    private int keyId;
    private String orgId,userId,category, subCategory, createdBy, createdDate, modifiedBy, modifiedDate, status;

    public SinglePreference(int keyId, String orgId, String userId, String category, String subCategory, String createdBy,  String createdDate, String modifiedBy, String modifiedDate , String status) {
        this.keyId = keyId;
        this.orgId = orgId;
        this.userId = userId;
        this.category = category;
        this.subCategory=subCategory;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.modifiedBy=modifiedBy;
        this.modifiedDate = modifiedDate;
        this.status = status;
    }
    public int getKeyId() {
        return keyId;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(int advtRefNo) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() { return createdDate;  }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
