package com.example.sss.goodlife.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ParticipantsTypeStatus {

    @SerializedName("status")
    String Status;

    @SerializedName("message")
    List<ParticipantsTypeIds> Message;

    public ParticipantsTypeStatus(String status, List<ParticipantsTypeIds> message) {
        Status = status;
        Message = message;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<ParticipantsTypeIds> getMessage() {
        return Message;
    }

    public void setMessage(List<ParticipantsTypeIds> message) {
        Message = message;
    }
}
