package com.example.shms.utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailService {
    private static final String appEmail="noreply.shms.alerts@gmail.com";
    private static final String appPass="kyye osrm cqiq ehtk";

    private static Session createSession() {
        Properties props=new Properties();
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.port","587");

        return Session.getInstance(props,new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(appEmail, appPass);
            }
        });
    }

    public static void sendEmail(String to, String subject, String body){
        try{
            Session session=createSession();
            Message message=new MimeMessage(session);
            message.setFrom(new InternetAddress(appEmail));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            System.out.println("Email sent to: "+to);
        }catch(MessagingException e){
            System.out.println("Failed to send email: "+e.getMessage());
        }
    }

    public static void sendAppointmentConfirmation(String to,String patientName,String doctorName,String date,String time){
        String subject="SHMS — Appointment Confirmation";
        String body="Dear "+patientName+",\n\n"
                +"Your appointment has been successfully booked.\n\n"
                +"Doctor: "+doctorName+"\n"
                +"Date: "+date+"\n"
                +"Time: "+time+"\n\n"
                +"Please arrive 10 minutes early.\n\n"
                +"Thank you!\n";
        sendEmail(to,subject,body);
    }

    public static void sendDischargeSummary(String to,String patientName,String doctorName,String roomNumber,String totalBill) {
        String subject="SHMS — Discharge Summary";
        String body="Dear "+patientName+",\n\n"
                +"You have been successfully discharged from SHMS.\n\n"
                +"Doctor: "+doctorName+"\n"
                +"Room: "+roomNumber+"\n"
                +"Total Bill: EGP "+totalBill + "\n\n"
                +"We wish you a speedy recovery.\n\n"
                +"SHMS Hospital\n"
                +"Every patient matters, every moment counts.";
        sendEmail(to,subject,body);
    }
}