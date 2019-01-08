package com.example.sss.goodlife.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProgramEventDates {

    @SerializedName("date")
    ArrayList<String> date;

    @SerializedName("from_time")
    ArrayList<String> from_time;

    @SerializedName("to_time")
    ArrayList<String> to_time;

    public ProgramEventDates(ArrayList<String> date, ArrayList<String> from_time, ArrayList<String> to_time) {
        this.date = date;
        this.from_time = from_time;
        this.to_time = to_time;
    }

    public ArrayList<String> getDate() {
        return date;
    }

    public void setDate(ArrayList<String> date) {
        this.date = date;
    }

    public ArrayList<String> getFrom_time() {
        return from_time;
    }

    public void setFrom_time(ArrayList<String> from_time) {
        this.from_time = from_time;
    }

    public ArrayList<String> getTo_time() {
        return to_time;
    }

    public void setTo_time(ArrayList<String> to_time) {
        this.to_time = to_time;
    }
}
