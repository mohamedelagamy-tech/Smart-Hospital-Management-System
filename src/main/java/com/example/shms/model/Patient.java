package com.example.shms.model;


public class Patient extends Person implements Comparable<Patient>{
    private int patientID;
    private int age;
    private String gender;
    private String phone;
    private int priority;

    public Patient(int ID, String name, String email, String password, String role, int age, String gender, String phone, int priority) {
        super(ID, name, email, password, role);
        this.patientID=ID;
        this.age=age;
        this.gender=gender;
        this.phone=phone;
        this.priority=priority;
    }

    @Override
    public String getDetails(){
        return "Patient ID: "+patientID+"\nName: "+getName()+ "\nAge: "+age+"\nGender: "+gender+"\nPhone: "+phone;
    }
    @Override
    public String toString() {
        return "Patient [patientID=" + patientID + "-" + getName();
    }
    @Override
    public int compareTo(Patient p){
        return Integer.compare(this.patientID,p.patientID);
    }
}
