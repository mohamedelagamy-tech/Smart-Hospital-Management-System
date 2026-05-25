package com.example.shms.utils;

public class SessionManager {

    private static SessionManager instance;

    private String loggedInUser;
    private String loggedInRole;
    private int loginAttempts;

    private SessionManager(){
        loginAttempts=0;
    }

    public static SessionManager getInstance(){
        if(instance==null){
            instance=new SessionManager();
        }
        return instance;
    }

    public void login(String username,String role){
        this.loggedInUser=username;
        this.loggedInRole=role;
        this.loginAttempts=0;
    }

    public void logout(){
        this.loggedInUser=null;
        this.loggedInRole=null;
        this.loginAttempts=0;
    }

    public void addAttempts(){
        loginAttempts++;
    }
    public void resetAttempts(){
        loginAttempts=0;
    }
    public boolean locked(){
       return loginAttempts>=5;
    }

    public boolean isLoggedIn(){
        return loggedInUser !=null;
    }

    public String getLoggedInUser(){
        return loggedInUser;
    }
    public void setLoggedInUser(String loggedInUser){
        this.loggedInUser=loggedInUser;
    }

    public String getLoggedInRole(){
        return loggedInRole;
    }
    public void setLoggedInRole(String loggedInRole){
        this.loggedInRole = loggedInRole;
    }

    public int getLoginAttempts(){
        return loginAttempts;
    }
    public void setLoginAttempts(int loginAttempts){
        this.loginAttempts = loginAttempts;
    }

    private Thread timeoutThread;
    private Runnable onTimeout;
    private static final int timeoutSeconds = 300;

    public void startSessionTimer(Runnable onTimeout){
        this.onTimeout = onTimeout;
        stopSessionTimer();
        timeoutThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(timeoutSeconds * 1000L);
                    if (onTimeout != null) {
                        javafx.application.Platform.runLater(onTimeout);
                    }
                } catch (InterruptedException e) {}
            }
        });
        timeoutThread.setDaemon(true);
        timeoutThread.start();
    }

    public void stopSessionTimer() {
        if (timeoutThread != null && timeoutThread.isAlive()) {
            timeoutThread.interrupt();
        }
    }

    public void resetSessionTimer(Runnable onTimeout) {
        startSessionTimer(onTimeout);
    }

private int loggedInPatientId= -1;
    public int getLoggedInPatientId() {
        return loggedInPatientId;
    }
    public void setLoggedInPatientId(int id) {
        this.loggedInPatientId = id;
    }
    }


