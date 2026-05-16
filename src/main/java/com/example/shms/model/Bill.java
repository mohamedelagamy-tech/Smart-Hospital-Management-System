package com.example.shms.model;
public class Bill implements Billable {

    private final String billNumber;
    private int patientId;
    private int appointmentId;
    private double amount;
    private String status; 
    private String date;
    private String patientName;
    private String service;

    public Bill(int billNumber, int patientId, int appointmentId, String date) {
        this.billNumber = billNumber;
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.date = date;
    }

    public String getBillNumber() { return billNumber; }
    public int getPatientId() { return patientId; }
    public int getAppointmentId() { return appointmentId; }
    public double getAmount() { return amount; }
    public String getBillStatus() { return status; }
    public String getBILLDate() { return date; }
    public String getPatientName() {return patientName;}
    public String getService() {return service;}
    public String getBillStatus() {return status;}

    public void setBillStatus(String status) { this.status = status; }
    public void setAmount(double amount) { this.amount = amount; }

}
