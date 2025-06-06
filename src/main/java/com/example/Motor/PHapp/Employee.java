package com.example.Motor.PHapp;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private int employeeNumber;
    private String lastName;
    private String firstName;
    private String birthDate;
    private String address;
    private String phoneNumber;
    private String sssNumber;
    private String philhealthNumber;
    private String tinNumber;
    private String pagibigNumber;
    private String status;
    private String position;
    private String immediateSupervisor;
    private double basicSalary;
    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance;
    private double grossSemiMonthlySalary;
    private double hourlyRate;
    private List<Attendance> attendanceRecords;

    public Employee(int employeeNumber, String lastName, String firstName, String birthDate, String address,
                    String phoneNumber, String sssNumber, String philhealthNumber, String tinNumber,
                    String pagibigNumber, String status, String position, String immediateSupervisor,
                    double basicSalary, double riceSubsidy, double phoneAllowance, double clothingAllowance,
                    double grossSemiMonthlySalary, double hourlyRate) {
        this.employeeNumber = employeeNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.sssNumber = sssNumber;
        this.philhealthNumber = philhealthNumber;
        this.tinNumber = tinNumber;
        this.pagibigNumber = pagibigNumber;
        this.status = status;
        this.position = position;
        this.immediateSupervisor = immediateSupervisor;
        this.basicSalary = basicSalary;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.grossSemiMonthlySalary = grossSemiMonthlySalary;
        this.hourlyRate = hourlyRate;
        this.attendanceRecords = new ArrayList<>();
    }

    public int getEmployeeNumber() { return employeeNumber; }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getBirthDate() { return birthDate; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getSssNumber() { return sssNumber; }
    public String getPhilhealthNumber() { return philhealthNumber; }
    public String getTinNumber() { return tinNumber; }
    public String getPagibigNumber() { return pagibigNumber; }
    public String getStatus() { return status; }
    public String getPosition() { return position; }
    public String getImmediateSupervisor() { return immediateSupervisor; }
    public double getBasicSalary() { return basicSalary; }
    public double getRiceSubsidy() { return riceSubsidy; }
    public double getPhoneAllowance() { return phoneAllowance; }
    public double getClothingAllowance() { return clothingAllowance; }
    public double getGrossSemiMonthlySalary() { return grossSemiMonthlySalary; }
    public double getHourlyRate() { return hourlyRate; }
    public List<Attendance> getAttendanceRecords() { return attendanceRecords; }
    public String getEmployeeName() { return firstName + " " + lastName; }

    public void setEmployeeNumber(int employeeNumber) { this.employeeNumber = employeeNumber; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setPosition(String position) { this.position = position; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }

    public void addAttendance(Attendance attendance) { attendanceRecords.add(attendance); }
}