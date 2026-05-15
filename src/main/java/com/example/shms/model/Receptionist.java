package com.example.shms.model;

public class Receptionist extends Person{
    private String shift;
    private String deskNumber;
    private String status;

    public Receptionist(int ID, String name, String email, String password, String shift, String deskNumber, String status){
        super(ID, name, email, password,"com.example.shms.model.Receptionist");
        this.shift=shift;
        this.deskNumber=deskNumber;
        this.status=status;
    }

    @Override
    public String getDetails() {
        return "com.example.shms.model.Receptionist ID: "+ getID()
                + ", Name: "+ getName()
                + ",Email: "+ getEmail()
                + ", Shift: "+ shift
                + ", Desk Number: "+ deskNumber
                + ", Status: "+ status;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getDeskNumber() {
        return deskNumber;
    }

    public void setDeskNumber(String deskNumber) {
        this.deskNumber = deskNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "com.example.shms.model.Receptionist ID: "+ getID()
                + ", Name: "+ getName()
                + ",Role: "+ getRole()
                + ", Shift: "+ shift
                + ", Desk Number: "+ deskNumber
                + ", Status: "+ status;
    }
}

