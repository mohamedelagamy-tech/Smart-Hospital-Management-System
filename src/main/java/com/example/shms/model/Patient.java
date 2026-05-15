package com.example.shms.model;

public class Patient extends Person implements Comparable<Patient>{
    private int patientID;
    private int age;
    private String gender;
    private String phone;
    private int priority;
    private String department;
    private String bloodType;
    private String address;
    private String status;

    public Patient(int ID, String name, String email, String password, String role, int age, String gender, String phone, int priority, String department, String bloodType, String address,String status) {
        super(ID, name, email, password, role);
        this.patientID=ID;
        this.age=age;
        this.gender=gender;
        this.phone=phone;
        this.priority=priority;
        this.department=department;
        this.bloodType=bloodType;
        this.address=address;
        this.status=status;
    }
    public Patient(){
        super();
    }

    @Override
    public String getDetails(){
        return "Patient ID: "+patientID+"\nName: "+getName()+ "\nAge: "+age+"\nGender: "+gender+"\nPhone: "+phone+"\nDepartment: "+department;
    }
    @Override
    public String toString() {
        return "Patient [patientID=" + patientID + "-" + getName();
    }
    @Override
    public int compareTo(Patient p){
        return Integer.compare(this.patientID,p.patientID);
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

