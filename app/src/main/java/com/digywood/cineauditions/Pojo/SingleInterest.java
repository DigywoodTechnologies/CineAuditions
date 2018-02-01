package com.digywood.cineauditions.Pojo;

/**
 * Created by Shashank on 22-01-2018.
 */

public class SingleInterest {


//    private int advtRefNo;
    private String advtId,userId,comment,status;

    public SingleInterest(String advtId, String userId, String comment,String status) {
        this.advtId = advtId;
        this.userId = userId;
        this.comment=comment;
        this.status = status;
    }

    public String getAdvtId() {
        return advtId;
    }

    public void setAdvtId(String advtId){
        this.advtId = advtId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
