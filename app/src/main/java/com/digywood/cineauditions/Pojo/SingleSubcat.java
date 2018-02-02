package com.digywood.cineauditions.Pojo;

/**
 * Created by prasa on 2018-02-02.
 */

public class SingleSubcat {

    private String catid,subcatid;

    public SingleSubcat(){

    }

    public SingleSubcat(String catid,String subcatid){

        this.catid=catid;
        this.subcatid=subcatid;

    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getSubcatid() {
        return subcatid;
    }

    public void setSubcatid(String subcatid) {
        this.subcatid = subcatid;
    }
}
