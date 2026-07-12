package com.example.shms.utils;

import com.example.shms.database.DatabaseManager;
import java.time.LocalDateTime;
import java.util.Random;

public class TwoFactorService {

    private static final int codeLength=6;
    private static final int expiryMins=10;
    private final DatabaseManager db=DatabaseManager.getInstance();

    public String generateAndSendCode(String username, String email){
        Random random=new Random();
        int code=100000+random.nextInt(900000);
        String codeStr=String.valueOf(code);

        String expiresAt=LocalDateTime.now().plusMinutes(expiryMins).toString();

        db.saveTwoFactorCode(username, codeStr, expiresAt);

        Thread emailThread=new Thread(new Runnable(){
            @Override
            public void run(){
                EmailService.sendTwoFactorCode(email,username,codeStr);
            }
        });
        emailThread.setDaemon(true);
        emailThread.start();

        return codeStr;
    }

    public boolean verifyCode(String username, String enteredCode){
        String storedCode=db.getTwoFactorCode(username);
        if(storedCode==null){
            return false;
        }
        if(storedCode.equals(enteredCode)){
            db.deleteTwoFactorCode(username);
            return true;
        }
        return false;
    }

    public boolean requiresTwoFactor(String username, String role){
        if(!role.equals("PATIENT")){
            return false;
        }
        return db.isTwoFactorEnabled(username);
    }
}