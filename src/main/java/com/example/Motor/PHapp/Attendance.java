package com.example.Motor.PHapp;

public class Attendance {
    private String date;
    private double logInTime;
    private double logOutTime;

    public Attendance(String date, double logInTime, double logOutTime) {
        this.date = date;
        this.logInTime = logInTime;
        this.logOutTime = logOutTime;
    }

    public double calculateHoursWorked() {
        return logOutTime - logInTime;
    }

    public String getDate() { return date; }
    public double getLogInTime() { return logInTime; }
    public double getLogOutTime() { return logOutTime; }
}