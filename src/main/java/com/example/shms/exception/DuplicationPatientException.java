package com.example.shms.exception;

public class DuplicationPatientException extends Exception
{
    public DuplicationPatientException(String name)
    {
        super("Patient " + name + " already exists!");
    }
}
