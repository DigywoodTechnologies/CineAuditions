package com.digywood.cineauditions.Pojo;

/**
 * Created by prasa on 2018-01-25.
 */

public class Candidate {

    private String name,number,mail,comment;

    public Candidate(){

    }

    public Candidate(String name, String number, String mail, String comment){

        this.name=name;
        this.number=number;
        this.mail=mail;
        this.comment=comment;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
