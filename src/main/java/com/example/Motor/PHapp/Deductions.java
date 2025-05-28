package com.example.Motor.PHapp;

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

    public double getSss() { return sss; }
    public double getPagibig() { return pagibig; }
    public double getPhilhealth() { return philhealth; }
    public double getWithholdingTax() { return withholdingTax; }
    public double calculateTotalDeductions() { return sss + pagibig + philhealth + withholdingTax; }
}