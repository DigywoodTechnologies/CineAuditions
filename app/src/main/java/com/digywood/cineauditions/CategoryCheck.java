package com.digywood.cineauditions;

public class CategoryCheck {
    private int keyId;
    private String category, subCategory,status;

    public CategoryCheck(int keyId, String category, String subCategory, String status) {
        this.keyId = keyId;
        this.category = category;
        this.subCategory=subCategory;
        this.status = status;
    }

    public int getKeyId() {
        return keyId;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }

    public String getCategory() { return category;  }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
