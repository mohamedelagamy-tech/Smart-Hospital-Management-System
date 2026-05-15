package com.example.shms.model;

public class Nurse extends Person {
    private String department;
    private String shift;
    private String status;

    public Nurse(int ID, String name, String email, String password, String department,String shift,String status){
        super(ID,name,email,password,"Nurse");
        this.department=department;
        this.shift=shift;
        this.status=status;
    }

    @Override
    public String getDetails() {
        return "Nurse ID: " + getID()
                + ", Name: " + getName()
                + ", Email: " + getEmail()
                + ", Department: " + department
                + ", Shift: " + shift
                + ", Status: " + status;
    }

    public String getDepartment(){
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Nurse ID: " + getID()
                + ", Name: " + getName()
                + ", Role: " + getRole()
                + ", Department: " + department
                + ", Shift: " + shift
                + ", Status: " + status;
    }
}
