package com.example.shms.utils;
import com.example.shms.database.DatabaseManager;
import com.example.shms.exception.AppointmentConflictException;
import com.example.shms.model.Appointment;
public class AppointmentValidator {
    private DatabaseManager db;
    public  AppointmentValidator(DatabaseManager db){
        this.db=db;
    }
    public void bookAppointment(Appointment appt) throws AppointmentConflictException{
        if ( db.hasConflict(appt.getDoctorId(),appt.getDate(),appt.getTime())) {
            throw new AppointmentConflictException(
                    appt.getDoctorId(),
                    appt.getDate().toString(),
                    appt.getTime().toString()
            );
        }
        db.addAppointment(appt);
        System.out.println("Appointment booked successfully");

        final Appointment finalAppt = appt;
        Thread emailThread = new Thread(new Runnable() {
            @Override
            public void run(){
                try{
                    String patientEmail=db.getPatientEmail(finalAppt.getPatientId());
                    String patientName=db.getPatientName(finalAppt.getPatientId());
                    String doctorName=db.getDoctorName(finalAppt.getDoctorId());
                    if(patientEmail != null && !patientEmail.isEmpty()){
                        EmailService.sendAppointmentConfirmation(patientEmail,patientName,doctorName,finalAppt.getDate().toString(),finalAppt.getTime().toString());
                    }
                }catch(Exception e){
                    System.out.println("Email notification failed: "+e.getMessage());
                }
            }
        });
        emailThread.setDaemon(true);
        emailThread.start();
    }
}