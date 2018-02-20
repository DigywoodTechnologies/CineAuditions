package com.digywood.cineauditions.Pojo;

/**
 * Created by prasa on 2018-02-19.
 */

public class SingleFAQ {

    private String ques,anws;
    private boolean click;

    public SingleFAQ(){

    }

    public SingleFAQ(String ques,String ans,boolean click){
        this.ques=ques;
        this.anws=ans;
        this.click=click;

    }

    public String getQues() {
        return ques;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

    public String getAnws() {
        return anws;
    }

    public void setAnws(String anws) {
        this.anws = anws;
    }

    public boolean isClick() {
        return click;
    }

    public void setClick(boolean click) {
        this.click = click;
    }
}
