package com.example.sss.goodlife.Models;

import com.google.gson.annotations.SerializedName;

public class Locations {

    @SerializedName("location_id")
    int location_id;
    @SerializedName("area")
    String area;

    public Locations(int location_id, String area) {
        this.location_id = location_id;
        this.area = area;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
