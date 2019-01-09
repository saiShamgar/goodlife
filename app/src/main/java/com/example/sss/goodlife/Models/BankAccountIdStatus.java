package com.example.sss.goodlife.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BankAccountIdStatus {

    @SerializedName("status")
    String Status;

    @SerializedName("message")
    List<BankAccountIds> Message;

    public BankAccountIdStatus(String status, List<BankAccountIds> message) {
        Status = status;
        Message = message;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<BankAccountIds> getMessage() {
        return Message;
    }

    public void setMessage(List<BankAccountIds> message) {
        Message = message;
    }
}
