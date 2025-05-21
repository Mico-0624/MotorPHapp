package com.example.Motor.PHapp;

import java.util.List;

public class SalaryCalculator {
    private double totalHoursWorked;
    private double hourlyRate;
    private double grossSalary;
    private double netSalary;

    public SalaryCalculator() {
        this.totalHoursWorked = 0.0;
        this.hourlyRate = 0.0;
        this.grossSalary = 0.0;
        this.netSalary = 0.0;
    }

    public double calculateGrossSalary(double totalHoursWorked, double hourlyRate) {
        this.totalHoursWorked = totalHoursWorked;
        this.hourlyRate = hourlyRate;
        this.grossSalary = totalHoursWorked * hourlyRate;
        return this.grossSalary;
    }

    public double calculateNetSalary(double grossSalary, double totalDeductions) {
        this.grossSalary = grossSalary;
        this.netSalary = grossSalary - totalDeductions;
        return this.netSalary;
    }

    public double calculateSalary(List<Attendance> attendanceRecords, double hourlyRate, Deductions deductions) {
        double totalHours = 0;
        for (Attendance attendance : attendanceRecords) {
            totalHours += attendance.calculateHoursWorked();
        }

        double gross = calculateGrossSalary(totalHours, hourlyRate);
        double totalDeductions = deductions.calculateTotalDeductions();
        double net = calculateNetSalary(gross, totalDeductions);

        this.totalHoursWorked = totalHours;
        this.hourlyRate = hourlyRate;
        this.grossSalary = gross;
        this.netSalary = net;

        return this.netSalary;
    }

    public double getTotalHoursWorked() {
        return totalHoursWorked;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public double getGrossSalary() {
        return grossSalary;
    }

    public double getNetSalary() {
        return netSalary;
    }
}