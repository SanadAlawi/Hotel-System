package com.example.mainactivity;

import java.io.Serializable;

public class Receptionist implements Serializable {

    private String receptionistID;
    private String receptionistName;
    private String receptionistUid;
    private String receptionistPwd;
    private String receptionistSalary;
    private String receptionistPhone;
    private String receptionistHourWork;
    private String receptionistAddress;
    private String receptionistEmail;
    private String receptionistService;

    public Receptionist(String receptionistID, String receptionistName, String receptionistUid, String receptionistPwd, String receptionistSalary, String receptionistPhone, String receptionistHourWork, String receptionistAddress, String receptionistEmail, String receptionistService) {
        this.receptionistID = receptionistID;
        this.receptionistName = receptionistName;
        this.receptionistUid = receptionistUid;
        this.receptionistPwd = receptionistPwd;
        this.receptionistSalary = receptionistSalary;
        this.receptionistPhone = receptionistPhone;
        this.receptionistHourWork = receptionistHourWork;
        this.receptionistAddress = receptionistAddress;
        this.receptionistEmail = receptionistEmail;
        this.receptionistService = receptionistService;
    }

    public String getReceptionistID() {
        return receptionistID;
    }

    public void setReceptionistID(String receptionistID) {
        this.receptionistID = receptionistID;
    }

    public String getReceptionistName() {
        return receptionistName;
    }

    public void setReceptionistName(String receptionistName) {
        this.receptionistName = receptionistName;
    }

    public String getReceptionistUid() {
        return receptionistUid;
    }

    public void setReceptionistUid(String receptionistUid) {
        this.receptionistUid = receptionistUid;
    }

    public String getReceptionistPwd() {
        return receptionistPwd;
    }

    public void setReceptionistPwd(String receptionistPwd) {
        this.receptionistPwd = receptionistPwd;
    }

    public String getReceptionistSalary() {
        return receptionistSalary;
    }

    public void setReceptionistSalary(String receptionistSalary) {
        this.receptionistSalary = receptionistSalary;
    }

    public String getReceptionistPhone() {
        return receptionistPhone;
    }

    public void setReceptionistPhone(String receptionistPhone) {
        this.receptionistPhone = receptionistPhone;
    }

    public String getReceptionistHourWork() {
        return receptionistHourWork;
    }

    public void setReceptionistHourWork(String receptionistHourWork) {
        this.receptionistHourWork = receptionistHourWork;
    }

    public String getReceptionistAddress() {
        return receptionistAddress;
    }

    public void setReceptionistAddress(String receptionistAddress) {
        this.receptionistAddress = receptionistAddress;
    }

    public String getReceptionistEmail() {
        return receptionistEmail;
    }

    public void setReceptionistEmail(String receptionistEmail) {
        this.receptionistEmail = receptionistEmail;
    }

    public String getReceptionistService() {
        return receptionistService;
    }

    public void setReceptionistService(String receptionistService) {
        this.receptionistService = receptionistService;
    }

    @Override
    public String toString() {
        return "Receptionist{" +
                "receptionistID='" + receptionistID + '\'' +
                ", receptionistName='" + receptionistName + '\'' +
                ", receptionistUid='" + receptionistUid + '\'' +
                ", receptionistPwd='" + receptionistPwd + '\'' +
                ", receptionistSalary='" + receptionistSalary + '\'' +
                ", receptionistPhone='" + receptionistPhone + '\'' +
                ", receptionistHourWork='" + receptionistHourWork + '\'' +
                ", receptionistAddress='" + receptionistAddress + '\'' +
                ", receptionistEmail='" + receptionistEmail + '\'' +
                ", receptionistService='" + receptionistService + '\'' +
                '}';
    }
}
