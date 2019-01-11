package com.example.sss.goodlife.Models;

public class ProgramIds {

    String program_id,location_id,area;

    public ProgramIds(String program_id, String location_id, String area) {
        this.program_id = program_id;
        this.location_id = location_id;
        this.area = area;
    }

    public String getProgram_id() {
        return program_id;
    }

    public void setProgram_id(String program_id) {
        this.program_id = program_id;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}


