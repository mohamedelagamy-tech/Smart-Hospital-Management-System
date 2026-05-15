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
        }
   }

