package com.example.shms.model;

public class Hospital {
    private static Hospital instance;

    private String hospitalName;
    private String address;
    private String hotline;

    private Hospital(){
        this.hospitalName="SHMS";
        this.address="Alexandria, Egypt";
        this.hotline="+20 123 456 7890";
    }

    public static Hospital getInstance(){
        if(instance==null){
            instance=new Hospital();
        }
        return instance;
    }

    public String getHospitalName(){
        return hospitalName;
    }
    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getAddress(){
        return address;
    }
    public void setAddress(String address){
        this.address = address;
    }

    public String getHotline(){
        return hotline;
    }
    public void setHotline(String hotline){
        this.hotline = hotline;
    }
    @Override
    public String toString(){
        return hospitalName+" | "+address+" | "+hotline;
    }
}