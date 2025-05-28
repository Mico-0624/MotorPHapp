package com.example.Motor.PHapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
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

    public MotorPHGUI() {
        setTitle("MotorPH Payroll System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Apply a professional Look and Feel (Nimbus)
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

        // Load employee data
        loadEmployeeData();

        // Main panel with gradient background
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

        // Tabbed pane with custom styling
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

    private void logout() {
        isLoggedIn = false;
        user = null;
        for (int i = 1; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.setEnabledAt(i, false);
        }
        tabbedPane.setSelectedIndex(0);
        JOptionPane.showMessageDialog(this, "Logged out successfully!", "Logout", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadEmployeeData() {
        employees.add(new Employee(10001, "Garcia", "Manuel III", "10/11/1983", "Valero Carpark Building Valero Street 1227, Makati City", "966-860-270", "44-4506057-3", "820126853951", "442-605-657-000", "691295330870", "Regular", "Chief Executive Officer", "N/A", 90000, 1500, 2000, 1000, 45000, 535.71));
        employees.add(new Employee(10002, "Lim", "Antonio", "06/19/1988", "San Antonio De Padua 2, Block 1 Lot 8 and 2, Dasmarinas, Cavite", "171-867-411", "52-2061274-9", "331735646338", "683-102-776-000", "663904995411", "Regular", "Chief Operating Officer", "Garcia, Manuel III", 60000, 1500, 2000, 1000, 30000, 357.14));
        employees.add(new Employee(10003, "Aquino", "Bianca Sofia", "08/04/1989", "Rm. 402 4/F Jiao Building Timog Avenue Cor. Quezon Avenue 1100, Quezon City", "966-889-370", "30-8870406-2", "177451189665", "971-711-280-000", "171519773969", "Regular", "Chief Finance Officer", "Garcia, Manuel III", 60000, 1500, 2000, 1000, 30000, 357.14));
        employees.add(new Employee(10004, "Reyes", "Isabella", "06/16/1994", "460 Solanda Street Intramuros 1000, Manila", "786-868-477", "40-2511815-0", "341911411254", "876-809-437-000", "416946776041", "Regular", "Chief Marketing Officer", "Garcia, Manuel III", 60000, 1500, 2000, 1000, 30000, 357.14));
        employees.add(new Employee(10005, "Hernandez", "Eduard", "09/23/1989", "National Highway, Gingoog, Misamis Occidental", "088-861-012", "50-5577638-1", "957436191812", "031-702-374-000", "952347222457", "Regular", "IT Operations and Systems", "Lim, Antonio", 52670, 1500, 1000, 1000, 26335, 313.51));
        employees.add(new Employee(10006, "Villanueva", "Andrea Mae", "02/14/1988", "17/85 Stracke Via Suite 042, Poblacion, Las Piñas 4783 Dinagat Islands", "918-621-603", "49-1632020-8", "382189453145", "317-674-022-000", "441093369646", "Regular", "HR Manager", "Lim, Antonio", 52670, 1500, 1000, 1000, 26335, 313.51));
        employees.add(new Employee(10007, "San Jose", "Brad", "03/15/1996", "99 Strosin Hills, Poblacion, Bislig 5340 Tawi-Tawi", "797-009-261", "40-2400714-1", "239192926939", "672-474-690-000", "210850209964", "Regular", "HR Team Leader", "Villanueva, Andrea Mae", 42975, 1500, 800, 800, 21488, 255.80));
        employees.add(new Employee(10008, "Romualdez", "Alice", "05/14/1992", "12A/33 Upton Isle Apt. 420, Roxas City 1814 Surigao del Norte", "983-606-799", "55-4476527-2", "545652640232", "888-572-294-000", "211385556888", "Regular", "HR Rank and File", "San Jose, Brad", 22500, 1500, 500, 500, 11250, 133.93));
        employees.add(new Employee(10009, "Atienza", "Rosie", "09/24/1948", "90A Dibbert Terrace Apt. 190, San Lorenzo 6056 Davao del Norte", "266-036-427", "41-0644692-3", "708988234853", "604-997-793-000", "260107732354", "Regular", "HR Rank and File", "San Jose, Brad", 22500, 1500, 500, 500, 11250, 133.93));
        employees.add(new Employee(10010, "Alvaro", "Roderick", "03/30/1988", "#284 T. Morato corner, Scout Rallos Street, Quezon City", "053-381-386", "64-7605054-4", "578114853194", "525-420-419-000", "799254095212", "Regular", "Accounting Head", "Aquino, Bianca Sofia", 52670, 1500, 1000, 1000, 26335, 313.51));
        employees.add(new Employee(10011, "Salcedo", "Anthony", "09/14/1993", "93/54 Shanahan Alley Apt. 183, Santo Tomas 1572 Masbate", "070-766-300", "26-9647608-3", "126445315651", "210-805-911-000", "218002473454", "Regular", "Payroll Manager", "Alvaro, Roderick", 50825, 1500, 1000, 1000, 25413, 302.53));
        employees.add(new Employee(10012, "Lopez", "Josie", "01/14/1987", "49 Springs Apt. 266, Poblacion, Taguig 3200 Occidental Mindoro", "478-355-427", "44-8563448-3", "431709011012", "218-489-737-000", "113071293354", "Regular", "Payroll Team Leader", "Salcedo, Anthony", 38475, 1500, 800, 800, 19238, 229.02));
        employees.add(new Employee(10013, "Farala", "Martha", "01/11/1942", "42/25 Sawayn Stream, Ubay 1208 Zamboanga del Norte", "329-034-366", "45-5656375-0", "233693897247", "210-835-851-000", "631130283546", "Regular", "Payroll Rank and File", "Salcedo, Anthony", 24000, 1500, 500, 500, 12000, 142.86));
        employees.add(new Employee(10014, "Martinez", "Leila", "07/11/1970", "37/46 Kulas Roads, Maragondon 0962 Quirino", "877-110-749", "27-2090996-4", "515741057496", "275-792-513-000", "101205445886", "Regular", "Payroll Rank and File", "Salcedo, Anthony", 24000, 1500, 500, 500, 12000, 142.86));
        employees.add(new Employee(10015, "Romualdez", "Fredrick", "03/10/1985", "22A/52 Lubowitz Meadows, Pililla 4895 Zambales", "023-079-009", "26-8768374-1", "308366860059", "598-065-761-000", "223057707853", "Regular", "Account Manager", "Lim, Antonio", 53500, 1500, 1000, 1000, 26750, 318.45));
        employees.add(new Employee(10016, "Mata", "Christian", "10/21/1987", "90 O'Keefe Spur Apt. 379, Catigbian 2772 Sulu", "783-776-744", "49-2959312-6", "824187961962", "103-100-522-000", "631052853464", "Regular", "Account Team Leader", "Romualdez, Fredrick", 42975, 1500, 800, 800, 21488, 255.80));
        employees.add(new Employee(10017, "De Leon", "Selena", "02/20/1975", "89A Armstrong Trace, Compostela 7874 Maguindanao", "975-432-139", "27-2090208-8", "587272469938", "482-259-498-000", "719007608464", "Regular", "Account Team Leader", "Romualdez, Fredrick", 41850, 1500, 800, 800, 20925, 249.11));
        employees.add(new Employee(10018, "San Jose", "Allison", "06/24/1986", "08 Grant Drive Suite 406, Poblacion, Iloilo City 9186 La Union", "179-075-129", "45-3251383-0", "745148459521", "121-203-336-000", "114901859343", "Regular", "Account Rank and File", "Mata, Christian", 22500, 1500, 500, 500, 11250, 133.93));
        employees.add(new Employee(10019, "Rosario", "Cydney", "10/06/1996", "93A/21 Berge Points, Tapaz 2180 Quezon", "868-819-912", "49-1629900-2", "579253435499", "122-244-511-000", "265104358643", "Regular", "Account Rank and File", "Mata, Christian", 22500, 1500, 500, 500, 11250, 133.93));
        employees.add(new Employee(10020, "Bautista", "Mark", "02/12/1991", "65 Murphy Center Suite 094, Poblacion, Palayan 5636 Quirino", "683-725-348", "49-1647342-5", "399665157135", "273-970-941-000", "260054585575", "Regular", "Account Rank and File", "Mata, Christian", 23250, 1500, 500, 500, 11625, 138.39));
        employees.add(new Employee(10021, "Lazaro", "Darlene", "11/25/1985", "47A/94 Larkin Plaza Apt. 179, Poblacion, Caloocan 2751 Quirino", "740-721-558", "45-5617168-2", "606386917510", "354-650-951-000", "104907708845", "Probationary", "Account Rank and File", "Mata, Christian", 23250, 1500, 500, 500, 11625, 138.39));
        employees.add(new Employee(10022, "Delos Santos", "Kolby", "02/26/1980", "06A Gulgowski Extensions, Bongabon 6085 Zamboanga del Sur", "739-443-033", "52-0109570-6", "357451271274", "187-500-345-000", "113017988667", "Probationary", "Account Rank and File", "Mata, Christian", 24000, 1500, 500, 500, 12000, 142.86));
        employees.add(new Employee(10023, "Santos", "Vella", "12/31/1983", "99A Padberg Spring, Poblacion, Mabalacat 3959 Lanao del Sur", "955-879-269", "52-9883524-3", "548670482885", "101-558-994-000", "360028104576", "Probationary", "Account Rank and File", "Mata, Christian", 22500, 1500, 500, 500, 11250, 133.93));
        employees.add(new Employee(10024, "Del Rosario", "Tomas", "12/18/1978", "80A/48 Ledner Ridges, Poblacion, Kabankalan 8870 Marinduque", "882-550-989", "45-5866331-6", "953901539995", "560-735-732-000", "913108649964", "Probationary", "Account Rank and File", "Mata, Christian", 22500, 1500, 500, 500, 11250, 133.93));
        employees.add(new Employee(10025, "Tolentino", "Jacklyn", "05/19/1984", "96/48 Watsica Flats Suite 734, Poblacion, Malolos 1844 Ifugao", "675-757-366", "47-1692793-0", "753800654114", "841-177-857-000", "210546661243", "Probationary", "Account Rank and File", "De Leon, Selena", 24000, 1500, 500, 500, 12000, 142.86));
        employees.add(new Employee(10026, "Gutierrez", "Percival", "12/18/1970", "58A Wilderman Walks, Poblacion, Digos 5822 Davao del Sur", "512-899-876", "40-9504657-8", "797639382265", "502-995-671-000", "210897095686", "Probationary", "Account Rank and File", "De Leon, Selena", 24750, 1500, 500, 500, 12375, 147.32));
        employees.add(new Employee(10027, "Manalaysay", "Garfield", "08/28/1986", "60 Goyette Valley Suite 219, Poblacion, Tabuk 3159 Lanao del Sur", "948-628-136", "45-3298166-4", "810909286264", "336-676-445-000", "211274476563", "Probationary", "Account Rank and File", "De Leon, Selena", 24750, 1500, 500, 500, 12375, 147.32));
        employees.add(new Employee(10028, "Villegas", "Lizeth", "12/12/1981", "66/77 Mann Views, Luisiana 1263 Dinagat Islands", "332-372-215", "40-2400719-4", "934389652994", "210-395-397-000", "122238077997", "Probationary", "Account Rank and File", "De Leon, Selena", 24000, 1500, 500, 500, 12000, 142.86));
        employees.add(new Employee(10029, "Ramos", "Carol", "08/20/1978", "72/70 Stamm Spurs, Bustos 4550 Iloilo", "250-700-389", "60-1152206-4", "351830469744", "395-032-717-000", "212141893454", "Probationary", "Account Rank and File", "De Leon, Selena", 22500, 1500, 500, 500, 11250, 133.93));
        employees.add(new Employee(10030, "Maceda", "Emelia", "04/14/1973", "50A/83 Bahringer Oval Suite 145, Kiamba 7688 Nueva Ecija", "973-358-041", "54-1331005-0", "465087894112", "215-973-013-000", "515012579765", "Probationary", "Account Rank and File", "De Leon, Selena", 22500, 1500, 500, 500, 11250, 133.93));
        employees.add(new Employee(10031, "Aguilar", "Delia", "01/27/1989", "95 Cremin Junction, Surallah 2809 Cotabato", "529-705-439", "52-1859253-1", "136451303068", "599-312-588-000", "110018813465", "Probationary", "Account Rank and File", "De Leon, Selena", 22500, 1500, 500, 500, 11250, 133.93));
        employees.add(new Employee(10032, "Castro", "John Rafael", "02/09/1989", "Hi-way, Yati, Liloan Cebu", "332-424-955", "26-7145133-4", "601644902402", "404-768-309-000", "697764000000", "Regular", "Sales & Marketing", "Reyes, Isabella", 52670, 1500, 1000, 1000, 26335, 313.51));
        employees.add(new Employee(10033, "Martinez", "Carlos Ian", "11/16/1990", "Bulala, Camalaniugan", "078-854-208", "11-5062972-7", "380685387212", "256-436-296-000", "993372337726", "Regular", "Supply Chain and Logistics", "Reyes, Isabella", 52670, 1500, 1000, 1000, 26335, 313.51));
        employees.add(new Employee(10034, "Santos", "Beatriz", "08/07/1990", "Agapita Building, Metro Manila", "526-639-511", "20-2987501-5", "918460050077", "911-529-713-441", "874042259378", "Regular", "Customer Service and Relations", "Reyes, Isabella", 52670, 1500, 1000, 1000, 26335, 313.51));
    }

    private void createLoginTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; panel.add(titleLabel, gbc);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField usernameField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JPasswordField passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");
        styleButton(loginButton);
        JLabel statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        gbc.gridwidth = 1; gbc.gridy = 1; panel.add(usernameLabel, gbc);
        gbc.gridx = 1; panel.add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(passwordLabel, gbc);
        gbc.gridx = 1; panel.add(passwordField, gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(loginButton, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; panel.add(statusLabel, gbc);

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
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 248, 255));

        // Table for listing employees
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

        // Panel for selecting and viewing employee profile
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBorder(BorderFactory.createTitledBorder(null, "Employee Profile", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(70, 130, 180)));
        profilePanel.setBackground(new Color(240, 248, 255));

        JComboBox<Employee> employeeSelector = new JComboBox<>();
        for (Employee emp : employees) {
            employeeSelector.addItem(emp);
        }
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
                StringBuilder profile = new StringBuilder();
                profile.append("Employee Number: ").append(selectedEmployee.getEmployeeNumber()).append("\n");
                profile.append("Last Name: ").append(selectedEmployee.getLastName()).append("\n");
                profile.append("First Name: ").append(selectedEmployee.getFirstName()).append("\n");
                profile.append("Birth Date: ").append(selectedEmployee.getBirthDate()).append("\n");
                profile.append("Address: ").append(selectedEmployee.getAddress()).append("\n");
                profile.append("Phone Number: ").append(selectedEmployee.getPhoneNumber()).append("\n");
                profile.append("SSS Number: ").append(selectedEmployee.getSssNumber()).append("\n");
                profile.append("PhilHealth Number: ").append(selectedEmployee.getPhilhealthNumber()).append("\n");
                profile.append("TIN Number: ").append(selectedEmployee.getTinNumber()).append("\n");
                profile.append("Pag-IBIG Number: ").append(selectedEmployee.getPagibigNumber()).append("\n");
                profile.append("Status: ").append(selectedEmployee.getStatus()).append("\n");
                profile.append("Position: ").append(selectedEmployee.getPosition()).append("\n");
                profile.append("Immediate Supervisor: ").append(selectedEmployee.getImmediateSupervisor()).append("\n");
                profile.append("Basic Salary: ").append(String.format("%,.2f", selectedEmployee.getBasicSalary())).append("\n");
                profile.append("Rice Subsidy: ").append(String.format("%,.2f", selectedEmployee.getRiceSubsidy())).append("\n");
                profile.append("Phone Allowance: ").append(String.format("%,.2f", selectedEmployee.getPhoneAllowance())).append("\n");
                profile.append("Clothing Allowance: ").append(String.format("%,.2f", selectedEmployee.getClothingAllowance())).append("\n");
                profile.append("Hourly Rate: ").append(String.format("%,.2f", selectedEmployee.getHourlyRate())).append("\n");
                profileArea.setText(profile.toString());
            }
        });

        profilePanel.add(selectorPanel, BorderLayout.NORTH);
        profilePanel.add(profileScrollPane, BorderLayout.CENTER);

        // Use split pane with custom divider
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
        gbc.gridwidth = 2; gbc.gridy = 0; inputPanel.add(titleLabel, gbc);

        JLabel employeeLabel = new JLabel("Select Employee:");
        employeeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JComboBox<Employee> attendanceEmployeeSelector = new JComboBox<>();
        for (Employee emp : employees) {
            attendanceEmployeeSelector.addItem(emp);
        }
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

        gbc.gridwidth = 1; gbc.gridy = 1; panel.add(inputPanel, BorderLayout.NORTH);
        gbc.gridx = 0; gbc.gridy = 2; inputPanel.add(employeeLabel, gbc);
        gbc.gridx = 1; inputPanel.add(attendanceEmployeeSelector, gbc);
        gbc.gridx = 0; gbc.gridy = 3; inputPanel.add(dateLabel, gbc);
        gbc.gridx = 1; inputPanel.add(dateField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; inputPanel.add(logInLabel, gbc);
        gbc.gridx = 1; inputPanel.add(logInField, gbc);
        gbc.gridx = 0; gbc.gridy = 5; inputPanel.add(logOutLabel, gbc);
        gbc.gridx = 1; inputPanel.add(logOutField, gbc);
        gbc.gridx = 1; gbc.gridy = 6; gbc.anchor = GridBagConstraints.CENTER; inputPanel.add(addButton, gbc);
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; inputPanel.add(statusLabel, gbc);

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
        gbc.gridwidth = 2; gbc.gridy = 0; panel.add(titleLabel, gbc);

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

        gbc.gridwidth = 1; gbc.gridy = 1; panel.add(sssLabel, gbc);
        gbc.gridx = 1; panel.add(sssField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(pagibigLabel, gbc);
        gbc.gridx = 1; panel.add(pagibigField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(philhealthLabel, gbc);
        gbc.gridx = 1; panel.add(philhealthField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; panel.add(taxLabel, gbc);
        gbc.gridx = 1; panel.add(taxField, gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.anchor = GridBagConstraints.CENTER; panel.add(setButton, gbc);
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; panel.add(statusLabel, gbc);

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
        gbc.gridwidth = 2; gbc.gridy = 0; inputPanel.add(titleLabel, gbc);

        JLabel employeeLabel = new JLabel("Select Employee:");
        employeeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JComboBox<Employee> salaryEmployeeSelector = new JComboBox<>();
        for (Employee emp : employees) {
            salaryEmployeeSelector.addItem(emp);
        }
        salaryEmployeeSelector.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        salaryEmployeeSelector.setBackground(Color.WHITE);
        JButton calculateButton = new JButton("Calculate Salary");
        styleButton(calculateButton);

        gbc.gridwidth = 1; gbc.gridy = 1; inputPanel.add(employeeLabel, gbc);
        gbc.gridx = 1; inputPanel.add(salaryEmployeeSelector, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.CENTER; inputPanel.add(calculateButton, gbc);

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
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237));
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MotorPHGUI gui = new MotorPHGUI();
            gui.setVisible(true);
        });
    }
}