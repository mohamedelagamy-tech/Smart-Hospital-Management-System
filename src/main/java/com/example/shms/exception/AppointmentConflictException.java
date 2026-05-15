package com.example.shms.exception;

public class AppointmentConflictException extends RuntimeException {
    public AppointmentConflictException(int doctorId , String date , String time )
    {
        super(" Doctor "+doctorId+" already has an appointment on "+date +" at "+time);
    }
}
