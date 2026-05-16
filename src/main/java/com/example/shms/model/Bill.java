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

    public Bill(String billNumber,String patientName,String service, String date,double amount,String status) {
        this.status = status;
        this.service = service;
        this.patientName = patientName;
        this.date = date;
        this.billNumber = billNumber;
        this.amount = amount;
    }

    public Bill(String status, String service, String patientName, int patientId, String date, String billNumber, int appointmentId, double amount) {
        this.status = status;
        this.service = service;
        this.patientName = patientName;
        this.patientId = patientId;
        this.date = date;
        this.billNumber = billNumber;
        this.appointmentId = appointmentId;
        this.amount = amount;
    }

    public String getBillNumber() { return billNumber; }
    public int getPatientId() { return patientId; }
    public int getAppointmentId() { return appointmentId; }
    public double getAmount() { return amount; }
    public String getBillStatus() { return status; }
    public String getBILLDate() { return date; }
    public String getPatientName() {return patientName;}
    public String getService() {return service;}
    public void setBillStatus(String status) { this.status = status; }
    public void setAmount(double amount) { this.amount = amount; }

}
