package com.example.sss.goodlife.Models;

import java.util.ArrayList;

public class FinanceApplication {

    ArrayList<String> vendor_name,location,phone,total_amount,account_name,payment_type,
            bank_acno,bank_ifsc_code,expenditure_desc,quation_image;

    public FinanceApplication(ArrayList<String> vendor_name, ArrayList<String> location, ArrayList<String> phone, ArrayList<String> total_amount, ArrayList<String> account_name, ArrayList<String> payment_type, ArrayList<String> bank_acno, ArrayList<String> bank_ifsc_code, ArrayList<String> expenditure_desc, ArrayList<String> quation_image) {
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

    public ArrayList<String> getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(ArrayList<String> vendor_name) {
        this.vendor_name = vendor_name;
    }

    public ArrayList<String> getLocation() {
        return location;
    }

    public void setLocation(ArrayList<String> location) {
        this.location = location;
    }

    public ArrayList<String> getPhone() {
        return phone;
    }

    public void setPhone(ArrayList<String> phone) {
        this.phone = phone;
    }

    public ArrayList<String> getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(ArrayList<String> total_amount) {
        this.total_amount = total_amount;
    }

    public ArrayList<String> getAccount_name() {
        return account_name;
    }

    public void setAccount_name(ArrayList<String> account_name) {
        this.account_name = account_name;
    }

    public ArrayList<String> getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(ArrayList<String> payment_type) {
        this.payment_type = payment_type;
    }

    public ArrayList<String> getBank_acno() {
        return bank_acno;
    }

    public void setBank_acno(ArrayList<String> bank_acno) {
        this.bank_acno = bank_acno;
    }

    public ArrayList<String> getBank_ifsc_code() {
        return bank_ifsc_code;
    }

    public void setBank_ifsc_code(ArrayList<String> bank_ifsc_code) {
        this.bank_ifsc_code = bank_ifsc_code;
    }

    public ArrayList<String> getExpenditure_desc() {
        return expenditure_desc;
    }

    public void setExpenditure_desc(ArrayList<String> expenditure_desc) {
        this.expenditure_desc = expenditure_desc;
    }

    public ArrayList<String> getQuation_image() {
        return quation_image;
    }

    public void setQuation_image(ArrayList<String> quation_image) {
        this.quation_image = quation_image;
    }
}


