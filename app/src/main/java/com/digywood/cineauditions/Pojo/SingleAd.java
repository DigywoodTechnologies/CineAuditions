package com.digywood.cineauditions.Pojo;

/**
 * Created by prasa on 2018-01-29.
 */

public class SingleAd {

    private String createtime,caption,starttime,endtime;
    private int advtid;

    public SingleAd(){

    }

    public SingleAd(int advtId,String createtime,String caption,String starttime,String endtime){

        this.createtime=createtime;
        this.caption=caption;
        this.starttime=starttime;
        this.endtime=endtime;
        this.advtid=advtId;

    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public int getAdvtid() {
        return advtid;
    }

    public void setAdvtid(int advtid) {
        this.advtid = advtid;
    }
}
