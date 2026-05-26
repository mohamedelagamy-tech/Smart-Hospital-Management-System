package com.example.shms.model;
public class Bill implements Billable {

    private final String billNumber;
    private int patientId;
    private int appointmentId;
    private double amount;
    private String status;
    private String date;
    private String patientName;
    private String doctorName;
    private String service;
    private String paymentMethod;

    public Bill(int id, int patientId, String patientName, String doctorName, String service, double amount, String status, String paymentMethod,String date) {
        this.billNumber ="#B-" + String.format("%03d", id);
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.service = service;
        this.amount = amount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.date=date;
        this.appointmentId=0;
    }
    public Bill(String billNumber, String patientName, String service, String date,double amount, String status) {
        this.billNumber = billNumber;
        this.patientName = patientName;
        this.service = service;
        this.date = date;
        this.amount = amount;
        this.status = status;
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

    public String getDoctorName() {
        return doctorName;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}