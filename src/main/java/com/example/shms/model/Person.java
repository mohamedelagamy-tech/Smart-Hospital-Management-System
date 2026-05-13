package com.example.shms.model;

public abstract class Person {
    private int ID;
    private String name;
    private String email;
    private String password;
    private String role;

    public Person(int ID,String name,String email,String password,String role){
        this.ID=ID;
        this.name=name;
        this.email=email;
        this.password=password;
        this.role=role;
    }

    public abstract String getDetails();

    public int getID(){
        return ID;
    }
    public void setID(int ID){
        this.ID=ID;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email=email;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password=password;
    }

    public String getRole(){
        return role;
    }
    public void setRole(String role){
        this.role=role;
    }
    @Override
    public String toString(){
        return "ID: "+ID+", Name: "+name+", role: "+role;
    }
}
