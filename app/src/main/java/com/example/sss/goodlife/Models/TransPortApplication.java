package com.example.sss.goodlife.Models;

import java.util.ArrayList;

public class TransPortApplication {

    ArrayList<String> vechile_name,register_no,driver_name,licence_id,vechicle_capacity,
            from_location,to_locatdion,google_distance,manual_distance;

    public TransPortApplication(ArrayList<String> vechile_name, ArrayList<String> register_no, ArrayList<String> driver_name, ArrayList<String> licence_id, ArrayList<String> vechicle_capacity, ArrayList<String> from_location, ArrayList<String> to_locatdion, ArrayList<String> google_distance, ArrayList<String> manual_distance) {
        this.vechile_name = vechile_name;
        this.register_no = register_no;
        this.driver_name = driver_name;
        this.licence_id = licence_id;
        this.vechicle_capacity = vechicle_capacity;
        this.from_location = from_location;
        this.to_locatdion = to_locatdion;
        this.google_distance = google_distance;
        this.manual_distance = manual_distance;
    }

    public ArrayList<String> getVechile_name() {
        return vechile_name;
    }

    public void setVechile_name(ArrayList<String> vechile_name) {
        this.vechile_name = vechile_name;
    }

    public ArrayList<String> getRegister_no() {
        return register_no;
    }

    public void setRegister_no(ArrayList<String> register_no) {
        this.register_no = register_no;
    }

    public ArrayList<String> getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(ArrayList<String> driver_name) {
        this.driver_name = driver_name;
    }

    public ArrayList<String> getLicence_id() {
        return licence_id;
    }

    public void setLicence_id(ArrayList<String> licence_id) {
        this.licence_id = licence_id;
    }

    public ArrayList<String> getVechicle_capacity() {
        return vechicle_capacity;
    }

    public void setVechicle_capacity(ArrayList<String> vechicle_capacity) {
        this.vechicle_capacity = vechicle_capacity;
    }

    public ArrayList<String> getFrom_location() {
        return from_location;
    }

    public void setFrom_location(ArrayList<String> from_location) {
        this.from_location = from_location;
    }

    public ArrayList<String> getTo_locatdion() {
        return to_locatdion;
    }

    public void setTo_locatdion(ArrayList<String> to_locatdion) {
        this.to_locatdion = to_locatdion;
    }

    public ArrayList<String> getGoogle_distance() {
        return google_distance;
    }

    public void setGoogle_distance(ArrayList<String> google_distance) {
        this.google_distance = google_distance;
    }

    public ArrayList<String> getManual_distance() {
        return manual_distance;
    }

    public void setManual_distance(ArrayList<String> manual_distance) {
        this.manual_distance = manual_distance;
    }
}
