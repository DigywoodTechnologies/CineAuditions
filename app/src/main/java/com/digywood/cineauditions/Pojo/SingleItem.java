package com.digywood.cineauditions.Pojo;

public class SingleItem {

    private int itemId;
    private String shortItemName, longItemName, price, description, tax, grpId, status;
    private byte[] image;

    public SingleItem(int itemId, String shortItemName, String longItemName, String price, String description, byte[] image, String tax, String grpId, String status) {
        this.itemId = itemId;
        this.shortItemName = shortItemName;
        this.longItemName=longItemName;
        this.price = price;
        this.description=description;
        this.image=image;
        this.tax = tax;
        this.grpId=grpId;
        this.status = status;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getShortItemName() {
        return shortItemName;
    }

    public void setShortItemName(String shortItemName) {
        this.shortItemName = shortItemName;
    }

    public String getLongItemName() {
        return longItemName;
    }

    public void setLongItemName(String longItemName) {
        this.longItemName = longItemName;
    }

    public String getPrice() { return price;  }

    public void setPrice(String price) {
        this.price = price;
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

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getGrpId() {
        return grpId;
    }

    public void setGrpId(String grpId) {
        this.grpId = grpId;
    }

    public String getName() {
        return status;
    }

    public void setName(String name) {
        this.status = name;
    }
}
