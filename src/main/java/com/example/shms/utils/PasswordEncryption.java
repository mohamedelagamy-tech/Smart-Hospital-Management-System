package com.example.shms.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncryption {
    public static String hash(String password){
        try {
            MessageDigest md= MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes= md.digest(password.getBytes());
            StringBuilder sb=new StringBuilder();
            for(byte b:hashedBytes){
                sb.append(String.format("%02x",b));
            }
            return sb.toString();
        }catch(NoSuchAlgorithmException e){
            System.out.println("Hashing failed"+e.getMessage());
            return null;
        }
    }
}
