/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Motor.PHapp;

/**
 *
 * @author Mico
 */
public class Deductions {
    private double sss;
    private double pagibig;
    private double philhealth;
    private double withholdingTax;

    public Deductions(double sss, double pagibig, double philhealth, double withholdingTax) {
        this.sss = sss;
        this.pagibig = pagibig;
        this.philhealth = philhealth;
        this.withholdingTax = withholdingTax;
    }

    public double calculateTotalDeductions() {
        return sss + pagibig + philhealth + withholdingTax;
    }

    public double getSss() {
        return sss;
    }

    public double getPagibig() {
        return pagibig;
    }

    public double getPhilhealth() {
        return philhealth;
    }

    public double getWithholdingTax() {
        return withholdingTax;
    }
}
