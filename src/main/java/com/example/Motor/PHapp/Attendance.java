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

    public String getDate() { return date; }
    public double getLogInTime() { return logInTime; }
    public double getLogOutTime() { return logOutTime; }

    public void setDate(String date) { this.date = date; }
    public void setLogInTime(double logInTime) { this.logInTime = logInTime; }
    public void setLogOutTime(double logOutTime) { this.logOutTime = logOutTime; }

    public double calculateHoursWorked() { return logOutTime - logInTime; }
}