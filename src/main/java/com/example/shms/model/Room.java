package com.example.shms.model;

public class Room {
    private int id;
    private int number;
    private String department;
    private String roomtype;
    private String roomStatus;
    private int assignedPatientId;

    public Room(int assignedPatientId, String department, int id, int number, String roomStatus, String roomtype) {
        this.assignedPatientId = assignedPatientId;
        this.department = department;
        this.id = id;
        this.number = number;
        this.roomStatus = roomStatus;
        this.roomtype = roomtype;
    }

    public int getAssignedPatientId() {
        return assignedPatientId;
    }

    public String getDepartment() {
        return department;
    }

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public String getRoomStatus() {
        return roomStatus;
    }

    public String getRoomtype() {
        return roomtype;
    }

    public void setAssignedPatientId(int assignedPatientId) {
        this.assignedPatientId = assignedPatientId;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }
}
