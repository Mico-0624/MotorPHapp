/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Motor.PHapp;

/**
 *
 * @author Mico
 */
public class Attendance {
    private double logInTime; // Using double as specified (e.g., 8.0 for 8 AM, 17.5 for 5:30 PM)
    private double logOutTime;
    private String date; // As specified, but LocalDate would be better

    // Constructor
    public Attendance(String date, double logInTime, double logOutTime) {
        this.date = date;
        this.logInTime = logInTime;
        this.logOutTime = logOutTime;
    }

    // Method from the diagram
    // '-' indicates private in diagram, but needed publicly for SalaryCalculator
    public double calculateHoursWorked() {
        // Simple calculation (assumes logout > login and within same 24hr period)
        // Real apps need more robust time handling
        if (logOutTime < logInTime) {
             // Handle cases spanning midnight - for simplicity, we'll assume same day
             System.err.println("Warning: Logout time is before login time for date: " + date);
             return 0; // Or handle as error/overnight shift
        }
        return logOutTime - logInTime;
    }

    // Optional: Getters if needed
    public double getLogInTime() {
        return logInTime;
    }

    public double getLogOutTime() {
        return logOutTime;
    }

    public String getDate() {
        return date;
    }
}
