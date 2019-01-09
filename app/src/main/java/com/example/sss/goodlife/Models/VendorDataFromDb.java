package com.example.sss.goodlife.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VendorDataFromDb {

    @SerializedName("status")
    String Status;

    @SerializedName("message")
    List<VendorDataModel> Message;

    public VendorDataFromDb(String status, List<VendorDataModel> message) {
        Status = status;
        Message = message;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<VendorDataModel> getMessage() {
        return Message;
    }

    public void setMessage(List<VendorDataModel> message) {
        Message = message;
    }
}
