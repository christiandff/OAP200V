package com.group11.classicmodels;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import java.io.File;


import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.DefaultComboBoxModel;

/**
 * @author Christian Douglas Farnes Fancy
 * @author Even Hjerpseth Unneberg
 */


public class Employee extends ApplicationMenu{
	private static final long serialVersionUID = 1L;
    private JFrame frame;
    private JTable table;
    private JTextField txtsearchEmployeeNumber;
    private DatabaseConnection dbConnection;
    private CRUD crudOperations;
    
 // Various text fields for data input
    private JTextField txtEmployeeNumber;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtExtension;
    private JTextField txtEmail;
    private JTextField txtOfficeCode;
    private JTextField txtReportsTo;
    private JTextField txtJobTitle;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Employee window = new Employee();
                    window.frame.setVisible(true); // Make the frame visible
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Employee() {
    	super();
        initialize();
        dbConnection = new DatabaseConnection();
        crudOperations = new CRUD(dbConnection);
        tableLoad();
        frame.setLocationRelativeTo(null);
    }
    /**
     * Method to load data into the table from the database.
     * This method fetches employee data from the database and populates it in the JTable.
     */
    public void tableLoad() {
        try {
        	// Prepare and execute a query to fetch all data
            PreparedStatement pst = dbConnection.getConnection().prepareStatement("select * from Employees");
            ResultSet rs = pst.executeQuery();
         // Create a list to hold data objects
            List<EmployeeData> employeeDataList = new ArrayList<>();
            while (rs.next()) {
            	// Retrieve data from the ResultSet and add it to the list
                EmployeeData employeeData = new EmployeeData(
                        rs.getString("EmployeeNumber"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Extension"),
                        rs.getString("Email"),
                        rs.getString("OfficeCode"),
                        rs.getString("ReportsTo"),
                        rs.getString("JobTitle")
                );
                employeeDataList.add(employeeData);
            }
         // Set the custom table model with the fetched data
            EmployeeTableModel tableModel = new EmployeeTableModel(employeeDataList);
            table.setModel(tableModel);
            // Set the tooltip for all columns in the table
            for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
                TableColumn column = table.getColumnModel().getColumn(i);
                column.setCellRenderer((table, value, isSelected, hasFocus, row, col) -> {
                    TableCellRenderer defaultRenderer = table.getDefaultRenderer(table.getColumnClass(col));
                    Component rendererComponent = defaultRenderer.getTableCellRendererComponent(
                            table, value, isSelected, hasFocus, row, col);

                    if (value != null) {
                        String originalText = value.toString();
                        int charactersPerLine = 50;

                        // Insert HTML line break tags after every 'charactersPerLine' characters
                        StringBuilder formattedText = new StringBuilder("<html>");
                        for (int charIndex = 0; charIndex < originalText.length(); charIndex++) {
                            formattedText.append(originalText.charAt(charIndex));
                            if ((charIndex + 1) % charactersPerLine == 0) {
                                formattedText.append("<br>");
                            }
                        }
                        formattedText.append("</html>");

                        ((JLabel) rendererComponent).setToolTipText(formattedText.toString());
                    }

                    return rendererComponent;
                });
            }

        } catch (SQLException e) {
            // Handle SQL exception in a user-friendly way
            JOptionPane.showMessageDialog(
                    frame, "Error searching product: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Method to initialize the contents of the frame.
     * This method sets up all the UI components for the Employee management window.
     */
    private void initialize() {
    	// Initialize the main frame, Set frame size and position, Set default close operation, Use absolute layout
    	frame = new JFrame();
    	frame.setJMenuBar(this);
    	String[] queryOptions = {"All Employees", "Sales Rep", "Sales Managers", "President and VPs"};        // Define the options for the query ComboBox
    	// Initialize the query ComboBox with the defined options, Set position and size, Add ComboBox to the frame
    	final JComboBox<String> queryComboBox = new JComboBox<>(queryOptions);
    	queryComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"All Employees", "Sales Rep", "Sales Managers", "President and VPs"}));
    	queryComboBox.setBounds(859, 373, 100, 25);
    	frame.getContentPane().add(queryComboBox);
    	// Initialize and configure the 'Set Query' button
    	JButton btnExecuteQuery = new JButton("Set Query");
    	btnExecuteQuery.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent e) {
    	        String selectedQuery = (String) queryComboBox.getSelectedItem();
    	        executeSelectedQuery(selectedQuery);
    	    }
    	});
    	btnExecuteQuery.setBounds(862, 337, 115, 25);
    	frame.getContentPane().add(btnExecuteQuery);
        frame.setBounds(100, 100, 1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

     // Initialize and configure the label for details
        JLabel lblEmployees = new JLabel("Employees");
        lblEmployees.setFont(new Font("Tahoma", Font.BOLD, 30));
        lblEmployees.setHorizontalAlignment(SwingConstants.CENTER);
        lblEmployees.setBounds(331, -2, 301, 44);
        frame.getContentPane().add(lblEmployees);
     // Initialize and configure the 'Exit' button
        JButton btnExit = new JButton("Exit");
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        btnExit.setBounds(859, 7, 115, 35);
        frame.getContentPane().add(btnExit);
        
     // Initialize and configure the scroll pane for the table
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(81, 118, 771, 413);
        frame.getContentPane().add(scrollPane);

        table = new JTable(); // Initialize the table
        scrollPane.setViewportView(table);
     // Initialize and configure
        JPanel panel_1 = new JPanel();
        panel_1.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_1.setBounds(262, 33, 378, 77);
        frame.getContentPane().add(panel_1);
        panel_1.setLayout(null);

        // Key release search function
        
        txtsearchEmployeeNumber = new JTextField();
        txtsearchEmployeeNumber.setColumns(10);
        txtsearchEmployeeNumber.setBounds(174, 30, 180, 20);
        panel_1.add(txtsearchEmployeeNumber);
        
        txtsearchEmployeeNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchProduct(); // Call search method on key release
            }
        });

    
        JLabel lblsearchEmployeeNumber = new JLabel("Employees Number");
        lblsearchEmployeeNumber.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblsearchEmployeeNumber.setBounds(9, 31, 154, 14);
        panel_1.add(lblsearchEmployeeNumber);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteEmployee(); // Call deleteProduct method when clicked
            }
        });
        btnDelete.setBounds(859, 156, 115, 35);
        frame.getContentPane().add(btnDelete);

     // Initialize and configure the 'Back to Menu' button
        JButton btnBackToMenu = new JButton("Back to menu");
        btnBackToMenu.addActionListener(new ActionListener() {
            @SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
                Overview overview = new Overview();
                overview.main(null);
                frame.dispose();
            }
        });
        btnBackToMenu.setBounds(10, 11, 115, 35);
        frame.getContentPane().add(btnBackToMenu);
        
     // Initialize and configure the 'Import CSV' button
        JButton btnImportCSV = new JButton("Import CSV");
		btnImportCSV.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        importCSV();
		    }
		});
		btnImportCSV.setBounds(859, 99, 115, 35);
		frame.getContentPane().add(btnImportCSV);

        
        JButton btnAddemp = new JButton("Add/Update ");
        btnAddemp.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		openAddEmployeeWindow();
        	}
        });
        btnAddemp.setBounds(859, 53, 115, 35);
        frame.getContentPane().add(btnAddemp);
        
     // Initialize a dialog for SQL text area (e.g., for displaying SQL queries)
        JDialog sqlDialog = new JDialog();
        sqlDialog.getContentPane().setLayout(new BorderLayout());
        sqlDialog.setSize(400, 300);
     // Initialize and configure a text area for SQL
        final JTextArea sqlTextArea = new JTextArea();
        sqlTextArea.setLineWrap(true);
        JScrollPane scrollPane1 = new JScrollPane(sqlTextArea);
        sqlDialog.getContentPane().add(scrollPane1, BorderLayout.CENTER);
    };
    
   
    /**
	 * Searches based on the number entered in the search field.
	 */
    private void searchProduct() {
        try {
            String employeeNumber = txtsearchEmployeeNumber.getText();
            String query = "SELECT EmployeeNumber, FirstName, LastName, Extension, Email, OfficeCode, ReportsTo, JobTitle FROM Employees";
            
            if (!employeeNumber.isEmpty()) {
                query += " WHERE EmployeeNumber = ?";
            }

            try (PreparedStatement pst = dbConnection.getConnection().prepareStatement(query)) {
                if (!employeeNumber.isEmpty()) {
                    pst.setString(1, employeeNumber);
                }

                ResultSet rs = pst.executeQuery();

                List<EmployeeData> employeeDataList = new ArrayList<>();
                while (rs.next()) {
                    EmployeeData employeeData = new EmployeeData(
                            rs.getString("EmployeeNumber"),
                            rs.getString("FirstName"),
                            rs.getString("LastName"),
                            rs.getString("Extension"),
                            rs.getString("Email"),
                            rs.getString("OfficeCode"),
                            rs.getString("ReportsTo"),
                            rs.getString("JobTitle")
                    );
                    employeeDataList.add(employeeData);
                }

                EmployeeTableModel tableModel = new EmployeeTableModel(employeeDataList);
                table.setModel(tableModel);
            }
        } catch (SQLException e) {
	        // Handle SQL exception in a user-friendly way
	        JOptionPane.showMessageDialog(frame, "Error searching product: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
	        e.printStackTrace();
	    }
	}
    
    /**
	 * Executes a query based on the selected category.
	 * @param selectedQuery The selected category for the query.
	 */
    private void executeSelectedQuery(String selectedQuery) {
        String customQuery;
     // Switch statement to handle different region/category selections
        switch (selectedQuery) {
            case "All Employees":
                customQuery = "SELECT EmployeeNumber, FirstName, LastName, Extension, Email, OfficeCode, ReportsTo, JobTitle FROM Employees";
                break;
            case "Sales Rep":
            	// Query to select employees by Sales Rep
                customQuery = "SELECT EmployeeNumber, FirstName, LastName, Extension, Email, OfficeCode, ReportsTo, JobTitle FROM Employees WHERE JobTitle = 'Sales Rep'";
                break;
            case "Sales Managers":
            	// Query to select employees by Sales Managers
            	customQuery = "SELECT EmployeeNumber, FirstName, LastName, Extension, Email, OfficeCode, ReportsTo, JobTitle FROM Employees WHERE JobTitle LIKE 'Sales Manager%'";
                break;
            case "President and VPs":
            	// Query to select employees by President and VPs
                customQuery = "SELECT EmployeeNumber, FirstName, LastName, Extension, Email, OfficeCode, ReportsTo, JobTitle FROM Employees WHERE JobTitle IN ('President','VP Sales', 'VP Marketing')";
                break;
            default:
                customQuery = ""; // Handle default case
        }

        try (PreparedStatement pst = dbConnection.getConnection().prepareStatement(customQuery)) {
            ResultSet rs = pst.executeQuery();

            List<EmployeeData> employeeDataList = new ArrayList<>();
            while (rs.next()) {
                EmployeeData employeeData = new EmployeeData(
                        rs.getString("EmployeeNumber"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Extension"),
                        rs.getString("Email"),
                        rs.getString("OfficeCode"),
                        rs.getString("ReportsTo"),
                        rs.getString("JobTitle")
                );
                employeeDataList.add(employeeData);
            }

            EmployeeTableModel tableModel = new EmployeeTableModel(employeeDataList);
            table.setModel(tableModel);  // Update the table model
        } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
	        ex.printStackTrace();
	    }
	}
    /**
	 * Opens a new window for adding or updating information.
	 */
    private void openAddEmployeeWindow() {
        // Create and set up the window.
        final JFrame addEmployeeFrame = new JFrame("Add New Employee");
        addEmployeeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addEmployeeFrame.getContentPane().setLayout(new BorderLayout());
        addEmployeeFrame.setSize(600, 500);
        // Create a panel for the registration section
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Registration", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(10, 43, 378, 345);
        addEmployeeFrame.getContentPane().add(panel);
        panel.setLayout(null);
     // Create a panel for the search employee section
        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(new TitledBorder(null, "Search Employee", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        searchPanel.setBounds(10, 405, 460, 60); // Adjust the position and size as needed
        addEmployeeFrame.getContentPane().add(searchPanel);
        searchPanel.setLayout(null);
     // Create a text field for entering search criteria
        final JTextField txtSearch = new JTextField();
        txtSearch.setBounds(10, 405, 180, 30); // Adjust the position and size as needed
        searchPanel.add(txtSearch);
     // Create a search button and add an action listener to it
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchEmployee(txtSearch.getText(), addEmployeeFrame); // Implement this method
            }
        });
        btnSearch.setBounds(170, 405, 180, 30); // Adjust the position and size as needed
        searchPanel.add(btnSearch);
        
     // Creates a label with a font and placement
        JLabel lblEmployeeNumber = new JLabel("Employee Number");
        lblEmployeeNumber.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblEmployeeNumber.setBounds(10, 58, 168, 19);
        panel.add(lblEmployeeNumber);

        JLabel lblFirstName = new JLabel("FirstName");
        lblFirstName.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblFirstName.setBounds(10, 88, 168, 20);
        panel.add(lblFirstName);

        JLabel lblLastName = new JLabel("LastName");
        lblLastName.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblLastName.setBounds(10, 119, 168, 20);
        panel.add(lblLastName);
        
        JLabel lblextension = new JLabel("Extension");
        lblextension.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblextension.setBounds(10, 151, 168, 20);
        panel.add(lblextension);
        
        
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblEmail.setBounds(10, 182, 168, 20);
        panel.add(lblEmail);
        
        JLabel lblofficeCode = new JLabel("Office Code");
        lblofficeCode.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblofficeCode.setBounds(10, 213, 168, 20);
        panel.add(lblofficeCode);
        
        JLabel lblReportsTo = new JLabel("Reports To");
        lblReportsTo.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblReportsTo.setBounds(10, 244, 168, 20);
        panel.add(lblReportsTo);
        
        JLabel lblJobTitle = new JLabel("JobTitle");
        lblJobTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblJobTitle.setBounds(10, 273, 168, 20);
        panel.add(lblJobTitle);
        
        // Creates a txtfield with a font and placement
        txtJobTitle = new JTextField();
        txtJobTitle.setColumns(10);
        txtJobTitle.setBounds(175, 273, 180, 20);
        panel.add(txtJobTitle);
        
        txtEmployeeNumber = new JTextField();
        txtEmployeeNumber.setBounds(175, 59, 180, 20);
        panel.add(txtEmployeeNumber);
        txtEmployeeNumber.setColumns(10);

        txtFirstName = new JTextField();
        txtFirstName.setColumns(10);
        txtFirstName.setBounds(175, 88, 180, 20);
        panel.add(txtFirstName);

        txtLastName = new JTextField();
        txtLastName.setColumns(10);
        txtLastName.setBounds(175, 119, 180, 20);
        panel.add(txtLastName);
        
        txtExtension = new JTextField();
        txtExtension.setColumns(10);
        txtExtension.setBounds(175, 151, 180, 20);
        panel.add(txtExtension);
        
        txtEmail = new JTextField();
        txtEmail.setColumns(10);
        txtEmail.setBounds(175, 182, 180, 20);
        panel.add(txtEmail);
        
        txtOfficeCode = new JTextField();
        txtOfficeCode.setColumns(10);
        txtOfficeCode.setBounds(175, 213, 180, 20);
        panel.add(txtOfficeCode);
        
        txtReportsTo = new JTextField();
        txtReportsTo.setColumns(10);
        txtReportsTo.setBounds(175, 244, 180, 20);
        panel.add(txtReportsTo);

        JButton btnSave = new JButton("Save");
        btnSave.setBounds(10, 304, 180, 30);
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveEmployee(); // Make sure this method is for saving new employee records
            }
        });
        panel.add(btnSave);
        
        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(200, 304, 180, 30);
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateEmployee(); // Make sure this method is for updating existing employee records
            }
        });
        panel.add(btnUpdate);
        
        // Display the window.
        addEmployeeFrame.setVisible(true);
    }
    
    /**
     * Method to search for an employee based on the employee number entered in the search field.
     * It performs a database search and updates the table to show the matching employee, if found.
     *
     * @param employeeNumber The employee number to search for in the database.
     */
    private void searchEmployee(String employeeNumber, JFrame frame) {
        try {
            // Prepare a SQL query to select employee information based on the provided employeeNumber
            PreparedStatement pst = dbConnection.getConnection().prepareStatement(
                "SELECT EmployeeNumber, FirstName, LastName, Extension, Email, OfficeCode, ReportsTo, JobTitle FROM Employees WHERE EmployeeNumber = ?");
            pst.setString(1, employeeNumber); // Set the parameter for the query to the provided employeeNumber
            
            ResultSet rs = pst.executeQuery(); // Execute the query and retrieve the result set
            
            if (rs.next()) { // Check if a matching employee was found in the result set
                // Retrieve the employee information from the result set
                String foundEmployeeNumber = rs.getString(1);
                String foundFirstName = rs.getString(2);
                String foundLastName = rs.getString(3);
                String foundExtension = rs.getString(4);
                String foundEmail = rs.getString(5);
                String foundOfficeCode = rs.getString(6);
                String foundReportsTo = rs.getString(7);
                String foundJobTitle = rs.getString(8);

                // Set the retrieved employee information in text fields or components on the GUI
                txtEmployeeNumber.setText(foundEmployeeNumber);
                txtFirstName.setText(foundFirstName);
                txtLastName.setText(foundLastName);
                txtExtension.setText(foundExtension);
                txtEmail.setText(foundEmail);
                txtOfficeCode.setText(foundOfficeCode);
                txtReportsTo.setText(foundReportsTo);
                txtJobTitle.setText(foundJobTitle);

            } else {
                // If no matching employee was found, clear the text fields or components
                txtEmployeeNumber.setText("");
                txtFirstName.setText("");
                txtLastName.setText("");
                txtExtension.setText("");
                txtEmail.setText("");
                txtOfficeCode.setText("");
                txtReportsTo.setText("");
                txtJobTitle.setText("");
            }
        } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
	        ex.printStackTrace();
	    }
	}

    /**
     * Method to save employee data entered in the text fields to the database.
     * It captures data from UI components and inserts a new employee record into the database.
     */
    public void saveEmployee() {
    	try {
        // Create a map to store column names and their corresponding values
        Map<String, Object> columnValues = new HashMap<>();
        columnValues.put("EmployeeNumber", txtEmployeeNumber.getText());
        columnValues.put("FirstName", txtFirstName.getText());
        columnValues.put("LastName", txtLastName.getText());
        columnValues.put("Extension", txtExtension.getText());
        columnValues.put("Email", txtEmail.getText());
        columnValues.put("OfficeCode", txtOfficeCode.getText());
        columnValues.put("ReportsTo", txtReportsTo.getText());
        columnValues.put("JobTitle", txtJobTitle.getText());
        
        // Call the insert method of your CRUD operations class to insert a new employee record
        crudOperations.insert("Employees", columnValues);
        
        // Reload the table data to reflect the changes
        tableLoad();
        
        // Clear the input fields after saving
        clearFields();
    	} catch (Exception e) {
		    // Display an error message using JOptionPane
		    JOptionPane.showMessageDialog(frame, "Database operation failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		    e.printStackTrace(); // Optional, for debugging purposes
		}
    }
    /**
     * Method to update an existing employee's data in the database.
     * It captures updated data from UI components and applies changes to the corresponding employee record.
     */
    public void updateEmployee() {
    	try {
        // Create a map to store updated column values
        Map<String, Object> updatedValues = new HashMap<>();
        updatedValues.put("FirstName", txtFirstName.getText());
        updatedValues.put("LastName", txtLastName.getText());
        updatedValues.put("Extension", txtExtension.getText());
        updatedValues.put("Email", txtEmail.getText());
        updatedValues.put("OfficeCode", txtOfficeCode.getText());
        updatedValues.put("ReportsTo", txtReportsTo.getText());
        updatedValues.put("JobTitle", txtJobTitle.getText());
        
        // Define a where clause to identify the employee to be updated (in this case, by EmployeeNumber)
        String whereClause = "EmployeeNumber = ?";
        Object[] whereArgs = { txtEmployeeNumber.getText() }; // Use the employee number from the text field as the argument
        
        // Call the update method of your CRUD operations class to update the employee record
        crudOperations.update("Employees", updatedValues, whereClause, whereArgs);
        
        // Reload the table data to reflect the changes
        tableLoad();
        
        // Clear the input fields after updating
        clearFields();
    	 } catch (Exception e) {
			    // Display an error message using JOptionPane
			    JOptionPane.showMessageDialog(frame, "Error updating product: " + e.getMessage(), "Update Error", JOptionPane.ERROR_MESSAGE);
			    e.printStackTrace(); // Optional, for debugging purposes
			}
    }

    /**
     * Method to clear all input fields in the user interface.
     * This method is typically called after save or update operations.
     */
    public void clearFields() {
        // Clear all input fields and set the focus back to txtEmployeeNumber
        txtEmployeeNumber.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtExtension.setText("");
        txtEmail.setText("");
        txtOfficeCode.setText("");
        txtReportsTo.setText("");
        txtJobTitle.setText("");
        txtEmployeeNumber.requestFocus();
    }
    /**
     * Method to delete an employee record from the database.
     * It uses the employee number from the search field to identify and delete the employee record.
     */
    public void deleteEmployee() {
        // Check if the txtsearchEmployeeNumber field is not null and not empty
        if (txtsearchEmployeeNumber != null && !txtsearchEmployeeNumber.getText().isEmpty()) {
            // Define a where clause to identify the employee to be deleted (by EmployeeNumber)
            String whereClause = "EmployeeNumber = ?";
            Object[] whereArgs = { txtsearchEmployeeNumber.getText() };
            
            // Call the delete method of your CRUD operations class to delete the employee record
            crudOperations.delete("Employees", whereClause, whereArgs);
            
            // Reload the table data to reflect the changes
            tableLoad();
            
            // Clear the input fields after deletion
            clearFields();
        } else {
            // Optionally, show an error message if the field is null or empty
            JOptionPane.showMessageDialog(frame, "Employee number is required for deletion.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Method to import employee data from a CSV file.
     * It reads data from the selected CSV file and inserts it into the database.
     */
    private void importCSV() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                // Skip the header row
                br.readLine();

                String line;
                String sql = "INSERT INTO Employees (EmployeeNumber, FirstName, LastName, Extension, Email, OfficeCode, ReportsTo, JobTitle) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = dbConnection.getConnection().prepareStatement(sql);

                while ((line = br.readLine()) != null) {
                    String[] values = line.split(","); 
                    statement.setString(1, values[0]);
                    statement.setString(2, values[1]);
                    statement.setString(3, values[2]);
                    statement.setString(4, values[3]);
                    statement.setString(5, values[4]);
                    statement.setString(6, values[5]);
                    statement.setString(7, values[6]);
                    statement.setString(8, values[7]);
                    statement.executeUpdate();
                }
                JOptionPane.showMessageDialog(frame, "Data Imported Successfully", "Info", JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh your table view after importing data
                tableLoad();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }




}