package com.example.Motor.PHapp;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.opencsv.exceptions.CsvValidationException;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.awt.event.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MotorPHGUI extends JFrame {
    private User user;
    private final List<Employee> employees;
    private final List<Attendance> attendanceRecords;
    private Deductions deductions;
    private final SalaryCalculator salaryCalculator;
    private final JTabbedPane tabbedPane;
    private boolean isLoggedIn;
    private static final String USERS_FILE = "users.csv";
    private static final String EMPLOYEES_FILE = "employees.csv";

    private class EmployeeComboBoxRenderer extends JLabel implements ListCellRenderer<Employee> {
        @Override
        public Component getListCellRendererComponent(JList<? extends Employee> list, Employee value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value != null) {
                setText(value.getLastName() + ", " + value.getFirstName());
            } else {
                setText("");
            }
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setOpaque(true);
            return this;
        }
    }

    public MotorPHGUI() {
        setTitle("MotorPH Payroll System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        employees = new ArrayList<>();
        attendanceRecords = new ArrayList<>();
        salaryCalculator = new SalaryCalculator();
        isLoggedIn = false;

        loadUserData();
        loadEmployeeData();

        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(240, 248, 255), 0, getHeight(), new Color(173, 216, 230));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabbedPane.setBackground(new Color(240, 248, 255));
        tabbedPane.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 1));
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        createLoginTab();
        createEmployeeTab();
        createAttendanceTab();
        createDeductionsTab();
        createSalaryTab();

        for (int i = 1; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.setEnabledAt(i, false);
        }
    }

    private void loadUserData() {
    File file = new File(USERS_FILE);
    boolean isValid = true;
    if (file.exists()) {
        try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                if (nextRecord.length >= 2) {
                    // Basic validation for BCrypt hash (starts with $2a$ or similar and is ~60 chars)
                    if (!nextRecord[1].matches("^\\$2[ayb]\\$.{56}$")) {
                        System.out.println("Invalid hash format for user: " + nextRecord[0]);
                        isValid = false;
                        break;
                    }
                    System.out.println("Loaded user: " + nextRecord[0]);
                } else {
                    System.out.println("Invalid CSV row: " + String.join(",", nextRecord));
                    isValid = false;
                    break;
                }
            }
        } catch (IOException | CsvValidationException e) {
            System.out.println("Failed to load users.csv: " + e.getMessage());
            isValid = false;
        }
    } else {
        isValid = false;
    }

    if (!isValid) {
        System.out.println("users.csv is missing or invalid, creating default user.");
        file.delete(); // Delete invalid file
        saveUserData("admin", "password");
    }
}

    private void saveUserData(String username, String password) {
    try (CSVWriter writer = new CSVWriter(new FileWriter(USERS_FILE))) { // Remove "true" to overwrite
        String[] record = {username, BCrypt.withDefaults().hashToString(12, password.toCharArray())};
        writer.writeNext(record);
    } catch (IOException e) {
        System.err.println("Failed to save user data: " + e.getMessage());
        e.printStackTrace();
    }
}

    private void loadEmployeeData() {
    try (CSVReader csvReader = new CSVReader(new FileReader(EMPLOYEES_FILE))) {
        String[] headers = csvReader.readNext(); // Skip or use headers if present
        String[] nextRecord;
        while ((nextRecord = csvReader.readNext()) != null) {
            if (nextRecord.length >= 19) {
                employees.add(new Employee(
                    Integer.parseInt(nextRecord[0]), // employeeNumber
                    nextRecord[1], // lastName
                    nextRecord[2], // firstName
                    nextRecord[3], // birthDate
                    nextRecord[4], // address
                    nextRecord[5], // phoneNumber
                    nextRecord[6], // sssNumber
                    nextRecord[7], // philhealthNumber
                    nextRecord[8], // tinNumber
                    nextRecord[9], // pagibigNumber
                    nextRecord[10], // status
                    nextRecord[11], // position
                    nextRecord[12], // immediateSupervisor
                    Double.parseDouble(nextRecord[13].replace(",", "")), // basicSalary
                    Double.parseDouble(nextRecord[14].replace(",", "")), // riceSubsidy
                    Double.parseDouble(nextRecord[15].replace(",", "")), // phoneAllowance
                    Double.parseDouble(nextRecord[16].replace(",", "")), // clothingAllowance
                    Double.parseDouble(nextRecord[17].replace(",", "")), // grossSemiMonthlySalary
                    Double.parseDouble(nextRecord[18].replace(",", "")) // hourlyRate
                ));
            }
        }
    } catch (IOException | CsvValidationException e) {
            // Create default employees if file doesn't exist or is invalid
            saveEmployeeData();
        }
    }

    private void saveEmployeeData() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(EMPLOYEES_FILE))) {
            String[] headers = {"employeeNumber", "lastName", "firstName", "birthDate", "address", "phoneNumber", "sssNumber",
                              "philhealthNumber", "tinNumber", "pagibigNumber", "status", "position", "immediateSupervisor",
                              "basicSalary", "riceSubsidy", "phoneAllowance", "clothingAllowance", "grossSemiMonthlySalary", "hourlyRate"};
            writer.writeNext(headers);
            for (Employee emp : employees) {
                String[] record = {
                    String.valueOf(emp.getEmployeeNumber()), emp.getLastName(), emp.getFirstName(), emp.getBirthDate(),
                    emp.getAddress(), emp.getPhoneNumber(), emp.getSssNumber(), emp.getPhilhealthNumber(), emp.getTinNumber(),
                    emp.getPagibigNumber(), emp.getStatus(), emp.getPosition(), emp.getImmediateSupervisor(),
                    String.valueOf(emp.getBasicSalary()), String.valueOf(emp.getRiceSubsidy()),
                    String.valueOf(emp.getPhoneAllowance()), String.valueOf(emp.getClothingAllowance()),
                    String.valueOf(emp.getGrossSemiMonthlySalary()), String.valueOf(emp.getHourlyRate())
                };
                writer.writeNext(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createLoginTab() {
    JPanel loginPanel = new JPanel(new GridBagLayout());
    loginPanel.setBackground(new Color(240, 248, 255));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.anchor = GridBagConstraints.CENTER;

    JLabel titleLabel = new JLabel("MotorPH Payroll System Login");
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
    titleLabel.setForeground(new Color(70, 130, 180));
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    loginPanel.add(titleLabel, gbc);

    JLabel usernameLabel = new JLabel("Username:");
    usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    JTextField usernameField = new JTextField(15);
    usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    JLabel passwordLabel = new JLabel("Password:");
    passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    JPasswordField passwordField = new JPasswordField(15);
    passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    JButton loginButton = new JButton("Login");
    styleButton(loginButton);
    JLabel loginStatusLabel = new JLabel("");
    loginStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

    gbc.gridwidth = 1;
    gbc.gridy = 1;
    gbc.gridx = 0;
    loginPanel.add(usernameLabel, gbc);
    gbc.gridx = 1;
    loginPanel.add(usernameField, gbc);
    gbc.gridx = 0;
    gbc.gridy = 2;
    loginPanel.add(passwordLabel, gbc);
    gbc.gridx = 1;
    loginPanel.add(passwordField, gbc);
    gbc.gridx = 1;
    gbc.gridy = 3;
    loginPanel.add(loginButton, gbc);
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    loginPanel.add(loginStatusLabel, gbc);

    loginButton.addActionListener(e -> {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        user = new User(username, ""); // Note: Empty password here
        System.out.println("Attempting login for username: " + username + ", password: " + password);
        try (CSVReader csvReader = new CSVReader(new FileReader(USERS_FILE))) {
            String[] nextRecord;
            boolean found = false;
            System.out.println("Reading users.csv at: " + new File(USERS_FILE).getAbsolutePath());
            while ((nextRecord = csvReader.readNext()) != null) {
                System.out.println("CSV row: " + String.join(",", nextRecord));
                if (nextRecord.length >= 2 && nextRecord[0].equals(username)) {
                    found = true;
                    System.out.println("Found user: " + username + ", hashed password: " + nextRecord[1]);
                    if (BCrypt.verifyer().verify(password.toCharArray(), nextRecord[1]).verified) {
                        loginStatusLabel.setText("Login successful!");
                        loginStatusLabel.setForeground(Color.GREEN);
                        isLoggedIn = true;
                        for (int i = 1; i < tabbedPane.getTabCount(); i++) {
                            tabbedPane.setEnabledAt(i, true);
                        }
                        tabbedPane.setSelectedIndex(1);
                        System.out.println("Login successful for " + username);
                        break;
                    } else {
                        System.out.println("Password verification failed for " + username);
                    }
                }
            }
            if (!found) {
                loginStatusLabel.setText("User not found!");
                loginStatusLabel.setForeground(Color.RED);
                System.out.println("User not found: " + username);
            } else if (!isLoggedIn) {
                loginStatusLabel.setText("Invalid password!");
                loginStatusLabel.setForeground(Color.RED);
                System.out.println("Invalid password for " + username);
            }
        } catch (IOException | CsvValidationException ex) {
            loginStatusLabel.setText("Error loading user data!");
            loginStatusLabel.setForeground(Color.RED);
            System.out.println("Error loading user data: " + ex.getMessage());
            ex.printStackTrace();
        }
    });

    tabbedPane.addTab("Login", loginPanel);
}

    private void createEmployeeTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 248, 255));

        String[] columns = {"Employee #", "Last Name", "First Name", "Position", "Basic Salary"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable employeeTable = new JTable(tableModel);
        employeeTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        employeeTable.setRowHeight(25);
        employeeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        employeeTable.getTableHeader().setBackground(new Color(70, 130, 180));
        employeeTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane tableScrollPane = new JScrollPane(employeeTable);

        for (Employee emp : employees) {
            tableModel.addRow(new Object[]{emp.getEmployeeNumber(), emp.getLastName(), emp.getFirstName(), emp.getPosition(), emp.getBasicSalary()});
        }

        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBorder(BorderFactory.createTitledBorder(null, "Employee Profile", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(70, 130, 180)));
        profilePanel.setBackground(new Color(240, 248, 255));

        JComboBox<Employee> employeeSelector = new JComboBox<>();
        for (Employee emp : employees) {
            employeeSelector.addItem(emp);
        }
        employeeSelector.setRenderer(new EmployeeComboBoxRenderer());
        employeeSelector.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        employeeSelector.setBackground(Color.WHITE);
        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectorPanel.setBackground(new Color(240, 248, 255));
        selectorPanel.add(new JLabel("Select Employee:"));
        selectorPanel.add(employeeSelector);

        JTextArea profileArea = new JTextArea(15, 50);
        profileArea.setEditable(false);
        profileArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        profileArea.setBackground(Color.WHITE);
        profileArea.setBorder(BorderFactory.createLineBorder(new Color(200, 221, 242)));
        JScrollPane profileScrollPane = new JScrollPane(profileArea);

        employeeSelector.addActionListener(e -> {
            Employee selectedEmployee = (Employee) employeeSelector.getSelectedItem();
            if (selectedEmployee != null) {
                profileArea.setText(getEmployeeProfile(selectedEmployee));
            }
        });

        profilePanel.add(selectorPanel, BorderLayout.NORTH);
        profilePanel.add(profileScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(240, 248, 255));
        JButton updateButton = new JButton("Update Employee");
        JButton deleteButton = new JButton("Delete Employee");
        JButton addButton = new JButton("Add Employee");
        styleButton(updateButton);
        styleButton(deleteButton);
        styleButton(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(addButton);

        updateButton.addActionListener(e -> {
            Employee selectedEmployee = (Employee) employeeSelector.getSelectedItem();
            if (selectedEmployee != null) {
                showEditEmployeeDialog(selectedEmployee);
                int selectedRow = employeeTable.getSelectedRow();
                if (selectedRow != -1) {
                    tableModel.setValueAt(selectedEmployee.getEmployeeNumber(), selectedRow, 0);
                    tableModel.setValueAt(selectedEmployee.getLastName(), selectedRow, 1);
                    tableModel.setValueAt(selectedEmployee.getFirstName(), selectedRow, 2);
                    tableModel.setValueAt(selectedEmployee.getPosition(), selectedRow, 3);
                    tableModel.setValueAt(selectedEmployee.getBasicSalary(), selectedRow, 4);
                }
                profileArea.setText(getEmployeeProfile(selectedEmployee));
                saveEmployeeData();
            }
        });

        deleteButton.addActionListener(e -> {
            Employee selectedEmployee = (Employee) employeeSelector.getSelectedItem();
            if (selectedEmployee != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + selectedEmployee.getEmployeeName() + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    employees.remove(selectedEmployee);
                    tableModel.removeRow(employeeTable.getSelectedRow());
                    employeeSelector.removeItem(selectedEmployee);
                    profileArea.setText("");
                    saveEmployeeData();
                }
            }
        });

        addButton.addActionListener(e -> {
            showAddEmployeeDialog(tableModel, employeeSelector, profileArea);
            saveEmployeeData();
        });

        profilePanel.add(buttonPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tableScrollPane, profilePanel);
        splitPane.setDividerLocation(400);
        splitPane.setDividerSize(10);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(splitPane, BorderLayout.CENTER);

        tabbedPane.addTab("Employees", panel);

        if (employeeSelector.getItemCount() > 0) {
            employeeSelector.setSelectedIndex(0);
        }
    }

    private void showAddEmployeeDialog(DefaultTableModel tableModel, JComboBox<Employee> employeeSelector, JTextArea profileArea) {
        JTextField empNumberField = new JTextField(10);
        JTextField lastNameField = new JTextField(10);
        JTextField firstNameField = new JTextField(10);
        JTextField positionField = new JTextField(10);
        JTextField basicSalaryField = new JTextField(10);

        JPanel dialogPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        dialogPanel.add(new JLabel("Employee #:"));
        dialogPanel.add(empNumberField);
        dialogPanel.add(new JLabel("Last Name:"));
        dialogPanel.add(lastNameField);
        dialogPanel.add(new JLabel("First Name:"));
        dialogPanel.add(firstNameField);
        dialogPanel.add(new JLabel("Position:"));
        dialogPanel.add(positionField);
        dialogPanel.add(new JLabel("Basic Salary:"));
        dialogPanel.add(basicSalaryField);

        int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Add New Employee", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int empNumber = Integer.parseInt(empNumberField.getText());
                String lastName = lastNameField.getText();
                String firstName = firstNameField.getText();
                String position = positionField.getText();
                double basicSalary = Double.parseDouble(basicSalaryField.getText());
                double riceSubsidy = 1500;
                double phoneAllowance = 500;
                double clothingAllowance = 500;
                double grossSemiMonthlySalary = basicSalary / 2;
                double hourlyRate = grossSemiMonthlySalary / 173.33;

                Employee newEmployee = new Employee(empNumber, lastName, firstName, "", "", "", "", "", "", "", "Regular", position, "N/A", basicSalary, riceSubsidy, phoneAllowance, clothingAllowance, grossSemiMonthlySalary, hourlyRate);
                employees.add(newEmployee);
                tableModel.addRow(new Object[]{empNumber, lastName, firstName, position, basicSalary});
                employeeSelector.addItem(newEmployee);
                employeeSelector.setSelectedItem(newEmployee);
                profileArea.setText(getEmployeeProfile(newEmployee));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number format!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void createAttendanceTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 248, 255));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("Attendance Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(70, 130, 180));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(titleLabel, gbc);

        JLabel employeeLabel = new JLabel("Select Employee:");
        employeeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JComboBox<Employee> attendanceEmployeeSelector = new JComboBox<>();
        for (Employee emp : employees) {
            attendanceEmployeeSelector.addItem(emp);
        }
        attendanceEmployeeSelector.setRenderer(new EmployeeComboBoxRenderer());
        attendanceEmployeeSelector.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        attendanceEmployeeSelector.setBackground(Color.WHITE);
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField dateField = new JTextField(15);
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JLabel logInLabel = new JLabel("Log In Time (e.g., 8.0):");
        logInLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField logInField = new JTextField(15);
        logInField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JLabel logOutLabel = new JLabel("Log Out Time (e.g., 17.0):");
        logOutLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField logOutField = new JTextField(15);
        logOutField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JButton addButton = new JButton("Add Attendance");
        styleButton(addButton);
        JLabel statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        inputPanel.add(employeeLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(attendanceEmployeeSelector, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(dateLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(dateField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(logInLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(logInField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(logOutLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(logOutField, gbc);
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(addButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(statusLabel, gbc);

        String[] columns = {"Date", "Log In", "Log Out", "Hours Worked"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(table);

        addButton.addActionListener(e -> {
            Employee selectedEmployee = (Employee) attendanceEmployeeSelector.getSelectedItem();
            if (selectedEmployee == null) {
                statusLabel.setText("No employee selected!");
                statusLabel.setForeground(Color.RED);
                return;
            }
            try {
                String date = dateField.getText();
                double logInTime = Double.parseDouble(logInField.getText());
                double logOutTime = Double.parseDouble(logOutField.getText());

                Attendance attendance = new Attendance(date, logInTime, logOutTime);
                selectedEmployee.addAttendance(attendance);
                attendanceRecords.add(attendance);

                double hours = attendance.calculateHoursWorked();
                tableModel.addRow(new Object[]{date, logInTime, logOutTime, String.format("%.2f", hours)});
                statusLabel.setText("Attendance added for " + selectedEmployee.getEmployeeName());
                statusLabel.setForeground(Color.GREEN);

                dateField.setText("");
                logInField.setText("");
                logOutField.setText("");
            } catch (NumberFormatException ex) {
                statusLabel.setText("Invalid time format!");
                statusLabel.setForeground(Color.RED);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(240, 248, 255));
        JButton updateAttendanceButton = new JButton("Update Attendance");
        JButton deleteAttendanceButton = new JButton("Delete Attendance");
        styleButton(updateAttendanceButton);
        styleButton(deleteAttendanceButton);
        buttonPanel.add(updateAttendanceButton);
        buttonPanel.add(deleteAttendanceButton);

        updateAttendanceButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                Employee selectedEmployee = (Employee) attendanceEmployeeSelector.getSelectedItem();
                String date = (String) tableModel.getValueAt(selectedRow, 0);
                double logInTime = (double) tableModel.getValueAt(selectedRow, 1);
                double logOutTime = (double) tableModel.getValueAt(selectedRow, 2);
                showEditAttendanceDialog(selectedEmployee, selectedRow, date, logInTime, logOutTime, tableModel);
            } else {
                statusLabel.setText("Select an attendance record to update!");
                statusLabel.setForeground(Color.RED);
            }
        });

        deleteAttendanceButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this attendance record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Employee selectedEmployee = (Employee) attendanceEmployeeSelector.getSelectedItem();
                    Attendance attendanceToRemove = null;
                    for (Attendance att : selectedEmployee.getAttendanceRecords()) {
                        if (att.getDate().equals(tableModel.getValueAt(selectedRow, 0))) {
                            attendanceToRemove = att;
                            break;
                        }
                    }
                    if (attendanceToRemove != null) {
                        selectedEmployee.getAttendanceRecords().remove(attendanceToRemove);
                        attendanceRecords.remove(attendanceToRemove);
                        tableModel.removeRow(selectedRow);
                        statusLabel.setText("Attendance record deleted!");
                        statusLabel.setForeground(Color.GREEN);
                    }
                }
            } else {
                statusLabel.setText("Select an attendance record to delete!");
                statusLabel.setForeground(Color.RED);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(buttonPanel, gbc);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("Attendance", panel);
    }

    private void createDeductionsTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("Deductions Setup");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(70, 130, 180));
        gbc.gridwidth = 2;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        JLabel sssLabel = new JLabel("SSS:");
        sssLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField sssField = new JTextField(15);
        sssField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JLabel pagibigLabel = new JLabel("Pag-IBIG:");
        pagibigLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField pagibigField = new JTextField(15);
        pagibigField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JLabel philhealthLabel = new JLabel("PhilHealth:");
        philhealthLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField philhealthField = new JTextField(15);
        philhealthField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JLabel taxLabel = new JLabel("Withholding Tax:");
        taxLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField taxField = new JTextField(15);
        taxField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JButton setButton = new JButton("Set Deductions");
        styleButton(setButton);
        JLabel statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(sssLabel, gbc);
        gbc.gridx = 1;
        panel.add(sssField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(pagibigLabel, gbc);
        gbc.gridx = 1;
        panel.add(pagibigField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(philhealthLabel, gbc);
        gbc.gridx = 1;
        panel.add(philhealthField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(taxLabel, gbc);
        gbc.gridx = 1;
        panel.add(taxField, gbc);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(setButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(statusLabel, gbc);

        JButton updateDeductionsButton = new JButton("Update Deductions");
        JButton deleteDeductionsButton = new JButton("Delete Deductions");
        styleButton(updateDeductionsButton);
        styleButton(deleteDeductionsButton);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.add(updateDeductionsButton);
        buttonPanel.add(deleteDeductionsButton);
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        updateDeductionsButton.addActionListener(e -> {
            if (deductions != null) {
                showEditDeductionsDialog();
                statusLabel.setText("Deductions updated successfully!");
                statusLabel.setForeground(Color.GREEN);
            } else {
                statusLabel.setText("No deductions set to update!");
                statusLabel.setForeground(Color.RED);
            }
        });

        deleteDeductionsButton.addActionListener(e -> {
            if (deductions != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete deductions?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    deductions = null;
                    statusLabel.setText("Deductions deleted!");
                    statusLabel.setForeground(Color.GREEN);
                }
            } else {
                statusLabel.setText("No deductions set to delete!");
                statusLabel.setForeground(Color.RED);
            }
        });

        setButton.addActionListener(e -> {
            try {
                double sss = Double.parseDouble(sssField.getText());
                double pagibig = Double.parseDouble(pagibigField.getText());
                double philhealth = Double.parseDouble(philhealthField.getText());
                double tax = Double.parseDouble(taxField.getText());

                deductions = new Deductions(sss, pagibig, philhealth, tax);
                statusLabel.setText("Deductions set successfully!");
                statusLabel.setForeground(Color.GREEN);

                sssField.setText("");
                pagibigField.setText("");
                philhealthField.setText("");
                taxField.setText("");
            } catch (NumberFormatException ex) {
                statusLabel.setText("Invalid number format!");
                statusLabel.setForeground(Color.RED);
            }
        });

        tabbedPane.addTab("Deductions", panel);
    }

    private void createSalaryTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 248, 255));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("Salary Calculation");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(70, 130, 180));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(titleLabel, gbc);

        JLabel employeeLabel = new JLabel("Select Employee:");
        employeeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JComboBox<Employee> salaryEmployeeSelector = new JComboBox<>();
        for (Employee emp : employees) {
            salaryEmployeeSelector.addItem(emp);
        }
        salaryEmployeeSelector.setRenderer(new EmployeeComboBoxRenderer());
        salaryEmployeeSelector.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        salaryEmployeeSelector.setBackground(Color.WHITE);
        JButton calculateButton = new JButton("Calculate Salary");
        styleButton(calculateButton);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        inputPanel.add(employeeLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(salaryEmployeeSelector, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(calculateButton, gbc);

        JTextArea resultArea = new JTextArea(15, 50);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        resultArea.setBackground(Color.WHITE);
        resultArea.setBorder(BorderFactory.createLineBorder(new Color(200, 221, 242)));
        JScrollPane textScrollPane = new JScrollPane(resultArea);

        calculateButton.addActionListener(e -> {
            if (!isLoggedIn) {
                resultArea.setText("Please log in first!");
                return;
            }
            Employee selectedEmployee = (Employee) salaryEmployeeSelector.getSelectedItem();
            if (selectedEmployee == null) {
                resultArea.setText("Please select an employee first!");
                return;
            }
            if (selectedEmployee.getAttendanceRecords().isEmpty()) {
                resultArea.setText("Please add attendance records first!");
                return;
            }
            if (deductions == null) {
                resultArea.setText("Please set deductions first!");
                return;
            }

            StringBuilder result = new StringBuilder();
            result.append("Employee: ").append(selectedEmployee.getEmployeeName())
                  .append(" (ID: ").append(selectedEmployee.getEmployeeNumber()).append(")\n");
            result.append("Position: ").append(selectedEmployee.getPosition()).append("\n");
            result.append("Hourly Rate: ").append(String.format("%,.2f", selectedEmployee.getHourlyRate())).append("\n");
            result.append("Basic Salary: ").append(String.format("%,.2f", selectedEmployee.getBasicSalary())).append("\n");
            result.append("Rice Subsidy: ").append(String.format("%,.2f", selectedEmployee.getRiceSubsidy())).append("\n");
            result.append("Phone Allowance: ").append(String.format("%,.2f", selectedEmployee.getPhoneAllowance())).append("\n");
            result.append("Clothing Allowance: ").append(String.format("%,.2f", selectedEmployee.getClothingAllowance())).append("\n");
            result.append("Attendance Records:\n");
            double totalHours = 0;
            for (Attendance att : selectedEmployee.getAttendanceRecords()) {
                double hours = att.calculateHoursWorked();
                result.append(" - Date: ").append(att.getDate())
                      .append(", Hours Worked: ").append(String.format("%.2f", hours)).append("\n");
                totalHours += hours;
            }
            result.append("Total Hours Worked: ").append(String.format("%.2f", totalHours)).append("\n");
            result.append("Deductions:\n");
            result.append(" - SSS: ").append(String.format("%,.2f", deductions.getSss())).append("\n");
            result.append(" - Pag-IBIG: ").append(String.format("%,.2f", deductions.getPagibig())).append("\n");
            result.append(" - PhilHealth: ").append(String.format("%,.2f", deductions.getPhilhealth())).append("\n");
            result.append(" - Withholding Tax: ").append(String.format("%,.2f", deductions.getWithholdingTax())).append("\n");
            result.append("Total Deductions: ").append(String.format("%,.2f", deductions.calculateTotalDeductions())).append("\n");

            salaryCalculator.calculateSalary(selectedEmployee.getAttendanceRecords(), selectedEmployee.getHourlyRate(), deductions);
            result.append("Gross Salary: ").append(String.format("%,.2f", salaryCalculator.getGrossSalary())).append("\n");
            result.append("Net Salary: ").append(String.format("%,.2f", salaryCalculator.getNetSalary())).append("\n");

            resultArea.setText(result.toString());
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(240, 248, 255));
        JButton updateSalaryButton = new JButton("Update Salary Record");
        JButton deleteSalaryButton = new JButton("Delete Salary Record");
        styleButton(updateSalaryButton);
        styleButton(deleteSalaryButton);
        buttonPanel.add(updateSalaryButton);
        buttonPanel.add(deleteSalaryButton);

        updateSalaryButton.addActionListener(e -> {
            Employee selectedEmployee = (Employee) salaryEmployeeSelector.getSelectedItem();
            if (selectedEmployee != null) {
                showEditEmployeeDialog(selectedEmployee);
                resultArea.setText(getSalaryDetails(selectedEmployee));
            }
        });

        deleteSalaryButton.addActionListener(e -> {
            Employee selectedEmployee = (Employee) salaryEmployeeSelector.getSelectedItem();
            if (selectedEmployee != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete salary records for " + selectedEmployee.getEmployeeName() + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    selectedEmployee.getAttendanceRecords().clear();
                    resultArea.setText("Salary records for " + selectedEmployee.getEmployeeName() + " deleted!");
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(buttonPanel, gbc);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(textScrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("Salary", panel);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) { button.setBackground(new Color(100, 149, 237)); }
            @Override
            public void mouseExited(MouseEvent evt) { button.setBackground(new Color(70, 130, 180)); }
        });
    }

    private void showEditEmployeeDialog(Employee employee) {
        JTextField empNumberField = new JTextField(String.valueOf(employee.getEmployeeNumber()), 10);
        JTextField lastNameField = new JTextField(employee.getLastName(), 10);
        JTextField firstNameField = new JTextField(employee.getFirstName(), 10);
        JTextField positionField = new JTextField(employee.getPosition(), 10);
        JTextField basicSalaryField = new JTextField(String.valueOf(employee.getBasicSalary()), 10);

        JPanel dialogPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        dialogPanel.add(new JLabel("Employee #:"));
        dialogPanel.add(empNumberField);
        dialogPanel.add(new JLabel("Last Name:"));
        dialogPanel.add(lastNameField);
        dialogPanel.add(new JLabel("First Name:"));
        dialogPanel.add(firstNameField);
        dialogPanel.add(new JLabel("Position:"));
        dialogPanel.add(positionField);
        dialogPanel.add(new JLabel("Basic Salary:"));
        dialogPanel.add(basicSalaryField);

        int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Update Employee", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                employee.setEmployeeNumber(Integer.parseInt(empNumberField.getText()));
                employee.setLastName(lastNameField.getText());
                employee.setFirstName(firstNameField.getText());
                employee.setPosition(positionField.getText());
                employee.setBasicSalary(Double.parseDouble(basicSalaryField.getText()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number format!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditAttendanceDialog(Employee employee, int row, String date, double logInTime, double logOutTime, DefaultTableModel tableModel) {
        JTextField dateField = new JTextField(date, 10);
        JTextField logInField = new JTextField(String.valueOf(logInTime), 10);
        JTextField logOutField = new JTextField(String.valueOf(logOutTime), 10);

        JPanel dialogPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        dialogPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        dialogPanel.add(dateField);
        dialogPanel.add(new JLabel("Log In Time:"));
        dialogPanel.add(logInField);
        dialogPanel.add(new JLabel("Log Out Time:"));
        dialogPanel.add(logOutField);

        int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Update Attendance", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String newDate = dateField.getText();
                double newLogInTime = Double.parseDouble(logInField.getText());
                double newLogOutTime = Double.parseDouble(logOutField.getText());
                Attendance attendance = employee.getAttendanceRecords().get(row);
                attendance.setDate(newDate);
                attendance.setLogInTime(newLogInTime);
                attendance.setLogOutTime(newLogOutTime);
                tableModel.setValueAt(newDate, row, 0);
                tableModel.setValueAt(newLogInTime, row, 1);
                tableModel.setValueAt(newLogOutTime, row, 2);
                tableModel.setValueAt(String.format("%.2f", attendance.calculateHoursWorked()), row, 3);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid time format!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditDeductionsDialog() {
        JTextField sssField = new JTextField(String.valueOf(deductions.getSss()), 10);
        JTextField pagibigField = new JTextField(String.valueOf(deductions.getPagibig()), 10);
        JTextField philhealthField = new JTextField(String.valueOf(deductions.getPhilhealth()), 10);
        JTextField taxField = new JTextField(String.valueOf(deductions.getWithholdingTax()), 10);

        JPanel dialogPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        dialogPanel.add(new JLabel("SSS:"));
        dialogPanel.add(sssField);
        dialogPanel.add(new JLabel("Pag-IBIG:"));
        dialogPanel.add(pagibigField);
        dialogPanel.add(new JLabel("PhilHealth:"));
        dialogPanel.add(philhealthField);
        dialogPanel.add(new JLabel("Withholding Tax:"));
        dialogPanel.add(taxField);

        int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Update Deductions", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                deductions.setSss(Double.parseDouble(sssField.getText()));
                deductions.setPagibig(Double.parseDouble(pagibigField.getText()));
                deductions.setPhilhealth(Double.parseDouble(philhealthField.getText()));
                deductions.setWithholdingTax(Double.parseDouble(taxField.getText()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number format!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String getEmployeeProfile(Employee employee) {
        StringBuilder profile = new StringBuilder();
        profile.append("Employee Number: ").append(employee.getEmployeeNumber()).append("\n");
        profile.append("Last Name: ").append(employee.getLastName()).append("\n");
        profile.append("First Name: ").append(employee.getFirstName()).append("\n");
        profile.append("Birth Date: ").append(employee.getBirthDate()).append("\n");
        profile.append("Address: ").append(employee.getAddress()).append("\n");
        profile.append("Phone Number: ").append(employee.getPhoneNumber()).append("\n");
        profile.append("SSS Number: ").append(employee.getSssNumber()).append("\n");
        profile.append("PhilHealth Number: ").append(employee.getPhilhealthNumber()).append("\n");
        profile.append("TIN Number: ").append(employee.getTinNumber()).append("\n");
        profile.append("Pag-IBIG Number: ").append(employee.getPagibigNumber()).append("\n");
        profile.append("Status: ").append(employee.getStatus()).append("\n");
        profile.append("Position: ").append(employee.getPosition()).append("\n");
        profile.append("Immediate Supervisor: ").append(employee.getImmediateSupervisor()).append("\n");
        profile.append("Basic Salary: ").append(String.format("%,.2f", employee.getBasicSalary())).append("\n");
        profile.append("Rice Subsidy: ").append(String.format("%,.2f", employee.getRiceSubsidy())).append("\n");
        profile.append("Phone Allowance: ").append(String.format("%,.2f", employee.getPhoneAllowance())).append("\n");
        profile.append("Clothing Allowance: ").append(String.format("%,.2f", employee.getClothingAllowance())).append("\n");
        profile.append("Gross Semi-Monthly Salary: ").append(String.format("%,.2f", employee.getGrossSemiMonthlySalary())).append("\n");
        profile.append("Hourly Rate: ").append(String.format("%,.2f", employee.getHourlyRate())).append("\n");
        return profile.toString();
    }

    private String getSalaryDetails(Employee employee) {
        StringBuilder result = new StringBuilder();
        result.append("Employee: ").append(employee.getEmployeeName()).append(" (ID: ").append(employee.getEmployeeNumber()).append(")\n");
        result.append("Position: ").append(employee.getPosition()).append("\n");
        result.append("Basic Salary: ").append(String.format("%,.2f", employee.getBasicSalary())).append("\n");
        result.append("Hourly Rate: ").append(String.format("%,.2f", employee.getHourlyRate())).append("\n");
        return result.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MotorPHGUI gui = new MotorPHGUI();
            gui.setVisible(true);
        });
    }
}