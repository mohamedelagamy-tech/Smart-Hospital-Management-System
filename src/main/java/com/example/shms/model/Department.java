package com.example.shms.model;

import java.util.ArrayList;

public class Department {
    private int departmentID;
    private String departmentName;
    private String location;
    private String description;
    private ArrayList<Doctor> doctors;
    private ArrayList<String> listOfRooms;

    public Department(int departmentID, String departmentName, String location, String description){
        this.departmentID=departmentID;
        this.departmentName=departmentName;
        this.location=location;
        this.description=description;

        doctors= new ArrayList<>();
        listOfRooms= new ArrayList<>();

    }

    public int getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(ArrayList<Doctor> doctors) {
        this.doctors = doctors;
    }

    public ArrayList<String> getListOfRooms() {
        return listOfRooms;
    }

    public void setListOfRooms(ArrayList<String> listOfRooms) {
        this.listOfRooms = listOfRooms;
    }

    public void addDoctor(Doctor doctor){
        doctors.add(doctor);
    }
    public void removeDoctor(Doctor doctor){
        doctors.remove(doctor);
    }

    public void addRoom(String room) {
        listOfRooms.add(room);
    }
    public void removeRoom(String room) {
        listOfRooms.remove(room);
    }

    @Override
    public String toString() {
        return departmentName;
    }
}