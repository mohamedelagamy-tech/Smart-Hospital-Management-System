package com.example.shms.model;

import com.example.shms.exception.DuplicationPatientException;

import java.util.LinkedList;
import java.util.Queue;

public class EmergencyQueue {
    private final Queue<Patient> emergencyQueue;
    private final Queue<Patient> urgentQueue;
    private final Queue<Patient> normalQueue;
    public EmergencyQueue() {
        emergencyQueue = new LinkedList<>();
        urgentQueue = new LinkedList<>();
        normalQueue = new LinkedList<>();
    }
    public Queue<Patient> getEmergencyQueue() {
        return emergencyQueue;
    }
    public Queue<Patient> getUrgentQueue() {
        return urgentQueue;
    }
    public Queue<Patient> getNormalQueue() {
        return normalQueue;
    }
    public void addPatient(Patient patient) throws DuplicationPatientException{
        if(isDuplicate(patient)){
            throw new DuplicationPatientException(patient.getName());
        }
        switch(patient.getPriority()){
            case 1->emergencyQueue.add(patient);
            case 2->urgentQueue.add(patient);
            case 3->normalQueue.add(patient);
            default -> System.out.println("Invalid Priority"+ patient.getPriority());
        }
        System.out.println("Patient: "+patient.getName()+ "added to the queue");
    }
    public Patient getNext(){
        if(!emergencyQueue.isEmpty()){
            return emergencyQueue.poll();
        }
        if(!urgentQueue.isEmpty()){
            return urgentQueue.poll();
        }
        if(!normalQueue.isEmpty()){
            return normalQueue.poll();
        }
        return null;
    }
    public Patient peekNext(){
        if(!emergencyQueue.isEmpty()){
            return emergencyQueue.peek();
        }
        if(!urgentQueue.isEmpty()){
            return urgentQueue.peek();
        }
        if(!normalQueue.isEmpty()){
            return normalQueue.peek();
        }
        return null;
    }
    private  boolean isDuplicate(Patient patient){
        for(Queue<Patient>q:new Queue[]{normalQueue,urgentQueue,emergencyQueue}){
            for(Patient p:q){
                if(p.getPatientID()==patient.getPatientID())
                    return true;
            }
        }
        return false;
    }
    public boolean isEmpty(){
        return emergencyQueue.isEmpty() && urgentQueue.isEmpty() && normalQueue.isEmpty();
    }
    public int size(){
        return emergencyQueue.size()+urgentQueue.size()+normalQueue.size();
    }
    public void printQueues(){
        System.out.println("EMERGENCY");
        emergencyQueue.forEach(p-> System.out.println("- "+p.getName()));
        System.out.println("URGENT");
        urgentQueue.forEach(p-> System.out.println("- "+p.getName()));
        System.out.println("NORMAL");
        normalQueue.forEach(p-> System.out.println("- "+p.getName()));
    }
}
