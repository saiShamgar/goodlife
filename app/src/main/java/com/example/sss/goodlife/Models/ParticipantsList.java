package com.example.sss.goodlife.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ParticipantsList {

    @SerializedName("participant_date")
    ArrayList<String> participant_date;

    @SerializedName("participant_type")
    ArrayList<String> participant_type;

    @SerializedName("participant_name")
    ArrayList<String> participant_name;

    @SerializedName("phone")
    ArrayList<String> phone;

    @SerializedName("men")
    ArrayList<String> men;

    @SerializedName("women")
    ArrayList<String> women;

    @SerializedName("child")
    ArrayList<String> child;

    @SerializedName("description")
    ArrayList<String> description;


    public ParticipantsList(ArrayList<String> participant_date, ArrayList<String> participant_type, ArrayList<String> participant_name, ArrayList<String> phone, ArrayList<String> men, ArrayList<String> women, ArrayList<String> child, ArrayList<String> description) {
        this.participant_date = participant_date;
        this.participant_type = participant_type;
        this.participant_name = participant_name;
        this.phone = phone;
        this.men = men;
        this.women = women;
        this.child = child;
        this.description = description;
    }

    public ArrayList<String> getParticipant_date() {
        return participant_date;
    }

    public void setParticipant_date(ArrayList<String> participant_date) {
        this.participant_date = participant_date;
    }

    public ArrayList<String> getParticipant_type() {
        return participant_type;
    }

    public void setParticipant_type(ArrayList<String> participant_type) {
        this.participant_type = participant_type;
    }

    public ArrayList<String> getParticipant_name() {
        return participant_name;
    }

    public void setParticipant_name(ArrayList<String> participant_name) {
        this.participant_name = participant_name;
    }

    public ArrayList<String> getPhone() {
        return phone;
    }

    public void setPhone(ArrayList<String> phone) {
        this.phone = phone;
    }

    public ArrayList<String> getMen() {
        return men;
    }

    public void setMen(ArrayList<String> men) {
        this.men = men;
    }

    public ArrayList<String> getWomen() {
        return women;
    }

    public void setWomen(ArrayList<String> women) {
        this.women = women;
    }

    public ArrayList<String> getChild() {
        return child;
    }

    public void setChild(ArrayList<String> child) {
        this.child = child;
    }

    public ArrayList<String> getDescription() {
        return description;
    }

    public void setDescription(ArrayList<String> description) {
        this.description = description;
    }
}



