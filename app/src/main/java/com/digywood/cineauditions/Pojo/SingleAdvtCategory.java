package com.digywood.cineauditions.Pojo;

public class SingleAdvtCategory {
    private int keyId;
    private String orgId, advtId, category, subCategory;

    public SingleAdvtCategory(int keyId, String orgId, String advtId, String category, String subCategory) {
        this.keyId = keyId;
        this.orgId = orgId;
        this.advtId=advtId;
        this.category = category;
        this.subCategory=subCategory;

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

    public String getAdvtId() {
        return advtId;
    }

    public void setAdvtId(String advtId) {
        this.advtId = advtId;
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

}
