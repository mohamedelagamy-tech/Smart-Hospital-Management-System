package com.example.shms.model;

public class MedicalRecord {

    private int id;
    private int patientId;
    private int doctorId;
    private String date;
    private String diagnosis;
    private String treatment;
    private String notes;

    public MedicalRecord(int id,int patientId,int doctorId,String date,String diagnosis,String treatment,String notes){
        this.id=id;
        this.patientId=patientId;
        this.doctorId=doctorId;
        this.date=date;
        this.diagnosis=diagnosis;
        this.treatment=treatment;
        this.notes=notes;
    }
    public int getId(){
        return id;
    }
    public int getPatientId(){
        return patientId;
    }
    public int getDoctorId(){
        return doctorId;
    }
    public String getDate(){
        return date;
    }
    public String getDiagnosis(){
        return diagnosis;
    }
    public String getTreatment(){
        return treatment;
    }
    public String getNotes(){
        return notes;
    }

    public void setNotes(String notes){
        this.notes=notes;
    }
    public void setTreatment(String t){
        this.treatment=t;
    }
    public void setDiagnosis(String d){
        this.diagnosis=d;
    }
}