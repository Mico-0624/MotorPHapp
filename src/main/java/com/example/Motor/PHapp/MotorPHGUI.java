package com.example.Motor.PHapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MotorPHGUI extends JFrame {
    private User user;
    private Employee employee;
    private List<Attendance> attendanceRecords;
    private Deductions deductions;
    private SalaryCalculator salaryCalculator;
    private JTabbedPane tabbedPane;
    private boolean isLoggedIn;

    public MotorPHGUI() {
        setTitle("MotorPH Payroll System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        attendanceRecords = new ArrayList<>();
        salaryCalculator = new SalaryCalculator();
        isLoggedIn = false;

        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        createLoginTab();
        createEmployeeTab();
        createAttendanceTab();
        createDeductionsTab();
        createSalaryTab();

        for (int i = 1; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.setEnabledAt(i, false);
        }
    }

    private void createLoginTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(70, 130, 180)); // Steel blue for contrast
        loginButton.setForeground(Color.WHITE);
        JLabel statusLabel = new JLabel("");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(statusLabel, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            user = new User("admin", "password123");
            if (user.verifyLogin(username, password)) {
                isLoggedIn = true;
                statusLabel.setText("Login successful!");
                statusLabel.setForeground(Color.GREEN);
                for (int i = 1; i < tabbedPane.getTabCount(); i++) {
                    tabbedPane.setEnabledAt(i, true);
                }
                tabbedPane.setSelectedIndex(1);
            } else {
                statusLabel.setText("Invalid credentials!");
                statusLabel.setForeground(Color.RED);
            }
        });

        tabbedPane.addTab("Login", panel);
    }

    private void createEmployeeTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel nameLabel = new JLabel("Employee Name:");
        JTextField nameField = new JTextField(15);
        JLabel numberLabel = new JLabel("Employee Number:");
        JTextField numberField = new JTextField(15);
        JLabel birthDateLabel = new JLabel("Birth Date (YYYY-MM-DD):");
        JTextField birthDateField = new JTextField(15);
        JLabel positionLabel = new JLabel("Position:");
        JTextField positionField = new JTextField(15);
        JLabel hourlyRateLabel = new JLabel("Hourly Rate:");
        JTextField hourlyRateField = new JTextField(15);
        JButton addButton = new JButton("Add Employee");
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.WHITE);
        JLabel statusLabel = new JLabel("");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(numberLabel, gbc);

        gbc.gridx = 1;
        panel.add(numberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(birthDateLabel, gbc);

        gbc.gridx = 1;
        panel.add(birthDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(positionLabel, gbc);

        gbc.gridx = 1;
        panel.add(positionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(hourlyRateLabel, gbc);

        gbc.gridx = 1;
        panel.add(hourlyRateField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(addButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(statusLabel, gbc);

        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                int number = Integer.parseInt(numberField.getText());
                String birthDate = birthDateField.getText();
                String position = positionField.getText();
                double hourlyRate = Double.parseDouble(hourlyRateField.getText());

                employee = new Employee(name, number, birthDate, position, hourlyRate);
                statusLabel.setText("Employee added: " + name);
                statusLabel.setForeground(Color.GREEN);
            } catch (NumberFormatException ex) {
                statusLabel.setText("Invalid number format!");
                statusLabel.setForeground(Color.RED);
            }
        });

        tabbedPane.addTab("Employee", panel);
    }

    private void createAttendanceTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        JTextField dateField = new JTextField(15);
        JLabel logInLabel = new JLabel("Log In Time (e.g., 8.0):");
        JTextField logInField = new JTextField(15);
        JLabel logOutLabel = new JLabel("Log Out Time (e.g., 17.0):");
        JTextField logOutField = new JTextField(15);
        JButton addButton = new JButton("Add Attendance");
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.WHITE);
        JLabel statusLabel = new JLabel("");

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(dateLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(logInLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(logInField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(logOutLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(logOutField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(addButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(statusLabel, gbc);

        String[] columns = {"Date", "Log In", "Log Out", "Hours Worked"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        addButton.addActionListener(e -> {
            if (employee == null) {
                statusLabel.setText("Add an employee first!");
                statusLabel.setForeground(Color.RED);
                return;
            }
            try {
                String date = dateField.getText();
                double logInTime = Double.parseDouble(logInField.getText());
                double logOutTime = Double.parseDouble(logOutField.getText());

                Attendance attendance = new Attendance(date, logInTime, logOutTime);
                employee.addAttendance(attendance);
                attendanceRecords.add(attendance);

                double hours = attendance.calculateHoursWorked();
                tableModel.addRow(new Object[]{date, logInTime, logOutTime, hours});
                statusLabel.setText("Attendance added for " + date);
                statusLabel.setForeground(Color.GREEN);
            } catch (NumberFormatException ex) {
                statusLabel.setText("Invalid time format!");
                statusLabel.setForeground(Color.RED);
            }
        });

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("Attendance", panel);
    }

    private void createDeductionsTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel sssLabel = new JLabel("SSS:");
        JTextField sssField = new JTextField(15);
        JLabel pagibigLabel = new JLabel("Pag-IBIG:");
        JTextField pagibigField = new JTextField(15);
        JLabel philhealthLabel = new JLabel("PhilHealth:");
        JTextField philhealthField = new JTextField(15);
        JLabel taxLabel = new JLabel("Withholding Tax:");
        JTextField taxField = new JTextField(15);
        JButton setButton = new JButton("Set Deductions");
        setButton.setBackground(new Color(70, 130, 180));
        setButton.setForeground(Color.WHITE);
        JLabel statusLabel = new JLabel("");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(sssLabel, gbc);

        gbc.gridx = 1;
        panel.add(sssField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(pagibigLabel, gbc);

        gbc.gridx = 1;
        panel.add(pagibigField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(philhealthLabel, gbc);

        gbc.gridx = 1;
        panel.add(philhealthField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(taxLabel, gbc);

        gbc.gridx = 1;
        panel.add(taxField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(setButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(statusLabel, gbc);

        setButton.addActionListener(e -> {
            try {
                double sss = Double.parseDouble(sssField.getText());
                double pagibig = Double.parseDouble(pagibigField.getText());
                double philhealth = Double.parseDouble(philhealthField.getText());
                double tax = Double.parseDouble(taxField.getText());

                deductions = new Deductions(sss, pagibig, philhealth, tax);
                statusLabel.setText("Deductions set successfully!");
                statusLabel.setForeground(Color.GREEN);
            } catch (NumberFormatException ex) {
                statusLabel.setText("Invalid number format!");
                statusLabel.setForeground(Color.RED);
            }
        });

        tabbedPane.addTab("Deductions", panel);
    }

    private void createSalaryTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextArea resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        JScrollPane textScrollPane = new JScrollPane(resultArea);
        JButton calculateButton = new JButton("Calculate Salary");
        calculateButton.setBackground(new Color(70, 130, 180));
        calculateButton.setForeground(Color.WHITE);

        calculateButton.addActionListener(e -> {
            if (!isLoggedIn) {
                resultArea.setText("Please log in first!");
                return;
            }
            if (employee == null) {
                resultArea.setText("Please add an employee first!");
                return;
            }
            if (attendanceRecords.isEmpty()) {
                resultArea.setText("Please add attendance records first!");
                return;
            }
            if (deductions == null) {
                resultArea.setText("Please set deductions first!");
                return;
            }

            StringBuilder result = new StringBuilder();
            result.append("Employee: ").append(employee.getEmployeeName())
                  .append(" (ID: ").append(employee.getEmployeeNumber()).append(")\n");
            result.append("Hourly Rate: ").append(employee.getHourlyRate()).append("\n");
            result.append("Attendance Records:\n");
            double totalHours = 0;
            for (Attendance att : attendanceRecords) {
                double hours = att.calculateHoursWorked();
                result.append(" - Date: ").append(att.getDate())
                      .append(", Hours Worked: ").append(hours).append("\n");
                totalHours += hours;
            }
            result.append("Total Hours Worked: ").append(totalHours).append("\n");
            result.append("Deductions:\n");
            result.append(" - SSS: ").append(deductions.getSss()).append("\n");
            result.append(" - Pag-IBIG: ").append(deductions.getPagibig()).append("\n");
            result.append(" - PhilHealth: ").append(deductions.getPhilhealth()).append("\n");
            result.append(" - Withholding Tax: ").append(deductions.getWithholdingTax()).append("\n");
            result.append(" - Total Deductions: ").append(deductions.calculateTotalDeductions()).append("\n");

            salaryCalculator.calculateSalary(attendanceRecords, employee.getHourlyRate(), deductions);
            result.append("Gross Salary: ").append(salaryCalculator.getGrossSalary()).append("\n");
            result.append("Net Salary: ").append(salaryCalculator.getNetSalary()).append("\n");

            resultArea.setText(result.toString());
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(calculateButton);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(textScrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("Salary", panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MotorPHGUI gui = new MotorPHGUI();
            gui.setVisible(true);
        });
    }
}