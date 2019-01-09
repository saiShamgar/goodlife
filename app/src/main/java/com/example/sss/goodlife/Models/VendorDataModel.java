package com.example.sss.goodlife.Models;

public class VendorDataModel {

    String vendor_name,location,phone,total_amount,account_name,payment_type,bank_acno,
            bank_ifsc_code,expenditure_desc,quation_image;

    public VendorDataModel(String vendor_name, String location, String phone, String total_amount, String account_name, String payment_type, String bank_acno, String bank_ifsc_code, String expenditure_desc, String quation_image) {
        this.vendor_name = vendor_name;
        this.location = location;
        this.phone = phone;
        this.total_amount = total_amount;
        this.account_name = account_name;
        this.payment_type = payment_type;
        this.bank_acno = bank_acno;
        this.bank_ifsc_code = bank_ifsc_code;
        this.expenditure_desc = expenditure_desc;
        this.quation_image = quation_image;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getBank_acno() {
        return bank_acno;
    }

    public void setBank_acno(String bank_acno) {
        this.bank_acno = bank_acno;
    }

    public String getBank_ifsc_code() {
        return bank_ifsc_code;
    }

    public void setBank_ifsc_code(String bank_ifsc_code) {
        this.bank_ifsc_code = bank_ifsc_code;
    }

    public String getExpenditure_desc() {
        return expenditure_desc;
    }

    public void setExpenditure_desc(String expenditure_desc) {
        this.expenditure_desc = expenditure_desc;
    }

    public String getQuation_image() {
        return quation_image;
    }

    public void setQuation_image(String quation_image) {
        this.quation_image = quation_image;
    }
}
