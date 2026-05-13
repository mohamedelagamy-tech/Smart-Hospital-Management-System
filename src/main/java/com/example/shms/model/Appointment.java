package com.example.shms.model;

public class Appointment implements Comparable<Appointment> {
    private int appointmentId;
    private int patientId;
    private int doctorId;
    private java.time.LocalDate date;
    private java.time.LocalTime time;
    private String status;
    private String notes;

    public Appointment(int appointmentId, int patientId, int doctorId,
                       java.time.LocalDate date, java.time.LocalTime time,
                       String status, String notes) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
        this.status  = status;
        this.notes  = notes;
    }
    public Appointment() {}
    @Override
    public int compareTo(Appointment other) {
        int dateComparison = this.date.compareTo(other.date);
        if (dateComparison != 0) return dateComparison;
            return this.time.compareTo(other.time);
    }
    public int getAppointmentId()
    {
        return appointmentId;
    }
    public void setAppointmentId(int id)
    {
        this.appointmentId = id;
    }
    public int getPatientId()
    {
        return patientId;
    }
    public void setPatientId(int x)
    {
        this.patientId = x;
    }
    public int getDoctorId()
    {
        return doctorId;
    }
    public void setDoctorId(int y)
    {
        this.doctorId = y;
    }
    public java.time.LocalDate getDate()
    {
        return date;
    }
    public void setDate(java.time.LocalDate date)
    {
        this.date = date;
    }
    public java.time.LocalTime getTime()
    {
        return time;
    }
    public void setTime(java.time.LocalTime time)
    {
        this.time = time;
    }
    public String getStatus()
    {
        return status;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }
    public String getNotes()
    {
        return notes;
    }
    public void setNotes(String notes)
    {
        this.notes = notes;
    }
    @Override
    public String toString() {
        return "AppointmentId=" + appointmentId +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", date=" + date +
                ", time=" + time +
                ", status='" + status + '\'' +
                ", notes='" + notes + '\'' + '}';
    }
}
