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

    // Getters (already assumed to exist)
    public double getSss() { return sss; }
    public double getPagibig() { return pagibig; }
    public double getPhilhealth() { return philhealth; }
    public double getWithholdingTax() { return withholdingTax; }

    // Add setters for update functionality
    public void setSss(double sss) { this.sss = sss; }
    public void setPagibig(double pagibig) { this.pagibig = pagibig; }
    public void setPhilhealth(double philhealth) { this.philhealth = philhealth; }
    public void setWithholdingTax(double withholdingTax) { this.withholdingTax = withholdingTax; }

    // Method to calculate total deductions (assumed to exist)
    public double calculateTotalDeductions() { return sss + pagibig + philhealth + withholdingTax; }
}