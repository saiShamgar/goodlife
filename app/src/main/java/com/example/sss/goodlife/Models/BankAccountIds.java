package com.example.sss.goodlife.Models;

public class BankAccountIds {

    String bank_id,bank_name;

    public BankAccountIds(String bank_id, String bank_name) {
        this.bank_id = bank_id;
        this.bank_name = bank_name;
    }

    public String getBank_id() {
        return bank_id;
    }

    public void setBank_id(String bank_id) {
        this.bank_id = bank_id;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }
}
