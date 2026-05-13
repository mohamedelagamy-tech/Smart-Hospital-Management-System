package com.example.shms.utils;

public class Validator {
    public static boolean checkEmpty(String... fields){
        for(String field:fields){
            if(field==null || field.trim().isEmpty()){
                return true;
            }
        }
        return false;
    }
    public static boolean checkPassStrength(String password){
        if(password.length()<8){
            return false;
        }
        boolean hasNum=false;
        boolean hasSChar=false;
        for(char c:password.toCharArray()){
            if(Character.isDigit(c)){
                hasNum=true;
            }
            if(!Character.isLetterOrDigit(c)){
                hasSChar=true;
            }
        }
        return hasNum && hasSChar;
    }
    public static boolean checkUsername(String username){
        if(username.length()<3){
            return false;
        }
        return username.matches("[a-zA-Z0-9._]+");
    }
    public static boolean checkAge(String age){
        try {
            int a=Integer.parseInt(age);
            return a>0 && a<120;
        } catch(NumberFormatException e) {
            return false;
        }
    }
    public static boolean checkPhoneNum(String phoneNumber){
        return phoneNumber.matches("^01[0125][0-9]{8}$");
    }
}
