package com.example.sss.goodlife.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProgramIdsStatus {

    @SerializedName("status")
    String Status;

    @SerializedName("message")
    List<ProgramIds> Message;

    public ProgramIdsStatus(String status, List<ProgramIds> message) {
        Status = status;
        Message = message;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<ProgramIds> getMessage() {
        return Message;
    }

    public void setMessage(List<ProgramIds> message) {
        Message = message;
    }
}
