package com.kawal.cablebillmanagement;

/**
 * Created by kawaldeep on 5/3/2017.
 */

public class AdminBean {

    String aName;
    String aPhone;
    String aEmail;
    String aPassword;
    String aReEnterPass;
    String aAddress;

    public AdminBean() {
    }

    public AdminBean(String aName, String aPhone, String aEmail, String aPassword, String aReEnterPass, String aAddress) {
        this.aName = aName;
        this.aPhone = aPhone;
        this.aEmail = aEmail;
        this.aPassword = aPassword;
        this.aReEnterPass = aReEnterPass;
        this.aAddress = aAddress;
    }

    public String getaName() {
        return aName;
    }

    public void setaName(String aName) {
        this.aName = aName;
    }

    public String getaPhone() {
        return aPhone;
    }

    public void setaPhone(String aPhone) {
        this.aPhone = aPhone;
    }

    public String getaEmail() {
        return aEmail;
    }

    public void setaEmail(String aEmail) {
        this.aEmail = aEmail;
    }

    public String getaPassword() {
        return aPassword;
    }

    public void setaPassword(String aPassword) {
        this.aPassword = aPassword;
    }

    public String getaReEnterPass() {
        return aReEnterPass;
    }

    public void setaReEnterPass(String aReEnterPass) {
        this.aReEnterPass = aReEnterPass;
    }

    public String getaAddress() {
        return aAddress;
    }

    public void setaAddress(String aAddress) {
        this.aAddress = aAddress;
    }

    @Override
    public String toString() {
        return "AdminBean{" +
                "aName='" + aName + '\'' +
                ", aPhone='" + aPhone + '\'' +
                ", aEmail='" + aEmail + '\'' +
                ", aPassword='" + aPassword + '\'' +
                ", aReEnterPass='" + aReEnterPass + '\'' +
                ", aAddress='" + aAddress + '\'' +
                '}';
    }
}
