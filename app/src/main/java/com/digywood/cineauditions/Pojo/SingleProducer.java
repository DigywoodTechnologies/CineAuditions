package com.digywood.cineauditions.Pojo;

/**
 * Created by Shashank on 24-01-2018.
 */

public class SingleProducer {

    private String name,address, city, state, contact_Person, phno,emailId, otp,regDate, status;
    public SingleProducer() {

    }
    public SingleProducer(String name, String address,String city,String state,String contact_Person,String phno,String emailId,String otp,
                          String regDate, String status){
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.contact_Person = contact_Person;
        this.otp = otp;
        this.regDate = regDate;
        this.status = status;
        this.status = status;
        this.phno = phno;
        this.emailId = emailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getContact_Person() {
        return contact_Person;
    }

    public void setContact_Person(String contact_Person) {
        this.contact_Person = contact_Person;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
