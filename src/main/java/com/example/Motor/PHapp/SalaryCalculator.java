package com.example.Motor.PHapp;

import java.util.List;

public class SalaryCalculator {
    private double grossSalary;
    private double netSalary;

    public void calculateSalary(List<Attendance> attendanceRecords, double hourlyRate, Deductions deductions) {
        double totalHours = 0;
        for (Attendance att : attendanceRecords) {
            totalHours += att.calculateHoursWorked();
        }
        grossSalary = totalHours * hourlyRate;
        netSalary = grossSalary - deductions.calculateTotalDeductions();
    }

    public double getGrossSalary() { return grossSalary; }
    public double getNetSalary() { return netSalary; }
}