package com.digywood.cineauditions.Pojo;

public class SingleCategory {
    private int keyId;
    private String orgId, categoryId, longName, shortName, createdBy, createdDate, modifiedBy, modifiedDate, status;

    public SingleCategory(int keyId, String orgId, String categoryId, String longName, String shortName, String createdBy, String createdDate, String modifiedBy, String modifiedDate, String status) {
        this.keyId = keyId;
        this.orgId = orgId;
        this.categoryId=categoryId;
        this.longName = longName;
        this.shortName=shortName;
        this.createdBy=createdBy;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getLongName() { return longName;  }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

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

    public String getName() {
        return status;
    }

    public void setName(String name) {
        this.status = name;
    }
}
