package com.group11.classicmodels;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Even Hjerpseth Unneberg
 */
public class Export extends ApplicationMenu{

	private static final long serialVersionUID = 1L;
	protected JFrame frame;
    private JTable resultTable;
    private JScrollPane scrollPane;
    private JComboBox<String> optionsDropdown;
    private JRadioButton allRadioButton;
    private JRadioButton specificRadioButton;
    private JButton btnAddToExportList; 
    private JButton btnDisplayExportList;
    private DefaultTableModel exportListModel;
    private List<Object[]> exportList = new ArrayList<>(); 
    private DatabaseConnection dbConnection;
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Export window = new Export();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    
    public Export() {
    	super();
    	// Initializes the database connection and CRUD operations
        dbConnection = new DatabaseConnection();
        new CRUD(dbConnection);
     // Initializes the main user interface
        initialize();
       
    }

 // Method to initialize the main frame
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setJMenuBar(this);
    
        // Creates a button group for radio buttons
        new ButtonGroup();
     
        // Button to go back to the main menu
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
     
        // Dropdowns for selecting tables from the database
        optionsDropdown = new JComboBox<>();
        optionsDropdown.addItem("Orders");
        optionsDropdown.addItem("Customers");
        optionsDropdown.addItem("Employees");
        optionsDropdown.addItem("Products");
        optionsDropdown.addItem("Productlines");
        optionsDropdown.addItem("Payments");
        optionsDropdown.addItem("Orderdetails");
        optionsDropdown.addItem("Offices");

        optionsDropdown.setBounds(434, 59, 120, 25);
        frame.getContentPane().add(optionsDropdown);
    
        // Radio buttons for selecting export options
        ButtonGroup radioButtonGroup = new ButtonGroup();

        allRadioButton = new JRadioButton("All");
        allRadioButton.setBounds(385, 113, 103, 21);
        frame.getContentPane().add(allRadioButton);
        radioButtonGroup.add(allRadioButton);

        specificRadioButton = new JRadioButton("Specify Export");
        specificRadioButton.setBounds(508, 113, 136, 21);
        frame.getContentPane().add(specificRadioButton);
        radioButtonGroup.add(specificRadioButton);

        JButton executeButton = new JButton("Set Query");
        executeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                executeQuery();
            }
        });
        
        executeButton.setBounds(333, 152, 131, 35);
        frame.getContentPane().add(executeButton);
        resultTable = new JTable();
     
        // Button to add displayed data to the export list
        scrollPane = new JScrollPane(resultTable);
        scrollPane.setBounds(84, 238, 820, 350);
        frame.getContentPane().add(scrollPane);
        btnAddToExportList = new JButton("Add to Export List");
        btnAddToExportList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Adds the currently displayed data to the export list
                addCurrentDataToExportList();
            }
        });
        btnAddToExportList.setBounds(474, 152, 145, 35);
        frame.getContentPane().add(btnAddToExportList);
     
        // Button to display the export list
        btnDisplayExportList = new JButton("Display Export List");
        btnDisplayExportList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display the export list in a new window
                displayExportList();
            }
        });
        btnDisplayExportList.setBounds(333, 192, 286, 35);
        frame.getContentPane().add(btnDisplayExportList);
    }
 // Method to execute the query based on user input
    private void executeQuery() {
        String selectedItem = optionsDropdown.getSelectedItem().toString();
        String query = "";

        if (specificRadioButton.isSelected()) {
        	// Show specific export options based on the selected item
            if ("Orders".equals(selectedItem)) {
                showOrderOptionPane();
                return;
            } else if ("Customers".equals(selectedItem)) {
                showCustomerOptionPane();
                return;
            } else if ("Products".equals(selectedItem)) {
            	showProductOptionPane();
                return;
            } else if ("Productlines".equals(selectedItem)) {
                showProductlinesOptionPane();
                return;
            } else if ("Orderdetails".equals(selectedItem)) {
                showOrderDetailsOptionPane();
                return;
            } else if ("Employees".equals(selectedItem)) {
                showEmployeesOptionPane();
                return;
            } else if ("Payments".equals(selectedItem)) {
                showPaymentsOptionPane();
                return;
            } else if ("Offices".equals(selectedItem)) {
                showOfficeOptionPane();
                return;
            }
        }

      
        query = "SELECT * FROM " + selectedItem;
        executeQueryAndDisplayResult(query);
    }
 // Method to display the export list
    private void displayExportList() {
        JFrame exportListFrame = new JFrame();
        exportListFrame.setBounds(100, 100, 800, 600);
        exportListFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTable exportListTable = new JTable();
        JScrollPane exportListScrollPane = new JScrollPane(exportListTable);
        exportListScrollPane.setBounds(10, 10, 560, 340);

        // Populates the export list table
        Object[][] exportListArray = new Object[exportList.size()][];
        for (int i = 0; i < exportList.size(); i++) {
            exportListArray[i] = exportList.get(i);
        }

        Object[] columnNames = new Object[resultTable.getColumnCount()];
        for (int i = 0; i < resultTable.getColumnCount(); i++) {
            columnNames[i] = resultTable.getColumnName(i);
        }

        exportListModel = new DefaultTableModel(exportListArray, columnNames);
        exportListTable.setModel(exportListModel);
     
        // Button to export data from the list
        JButton btnExportFromList = new JButton("Export");
        btnExportFromList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Calls the export method with the export list
                exportToCSV();
            }
        });
     
        // Button to remove selected rows from the list
        JButton btnRemoveFromList = new JButton("Remove Selected");
        btnRemoveFromList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Removes selected rows from the export list
                removeSelectedRows(exportListTable);
            }
        });
    
        // Panel for export and remove buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnRemoveFromList);
        buttonPanel.add(btnExportFromList);

     
        // Sets up box layout for the export list frame
        BoxLayout boxLayout = new BoxLayout(exportListFrame.getContentPane(), BoxLayout.Y_AXIS);
        exportListFrame.getContentPane().setLayout(boxLayout);

        // Adds components to the export list frame
        exportListFrame.getContentPane().add(Box.createVerticalGlue());
        exportListFrame.getContentPane().add(exportListScrollPane);
        exportListFrame.getContentPane().add(Box.createVerticalGlue());
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        exportListFrame.getContentPane().add(buttonPanel);

        // Sets size and make the export list frame visible
        exportListFrame.setSize(800, 600); 
        exportListFrame.setVisible(true);
    }
 // Method to remove selected rows from a table
    private void removeSelectedRows(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int[] selectedRows = table.getSelectedRows();

        
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            model.removeRow(selectedRows[i]);
        }
    }
 // Method to add currently displayed data to the export list
    private void addCurrentDataToExportList() {
        DefaultTableModel tableModel = (DefaultTableModel) resultTable.getModel();
        int rowCount = tableModel.getRowCount();

        if (rowCount > 0) {
            // Iterates through all rows in the table and add each row to the export list if not already present
            for (int i = 0; i < rowCount; i++) {
                Object[] rowData = new Object[tableModel.getColumnCount()];
                for (int j = 0; j < rowData.length; j++) {
                    rowData[j] = tableModel.getValueAt(i, j);
                }

                // Checks if the row data already exists in the export list
                if (!containsRowData(exportList, rowData)) {
                    exportList.add(rowData);
                }
            }
            JOptionPane.showMessageDialog(frame, "Added all displayed unique rows to Export List!");
        } else {
            JOptionPane.showMessageDialog(frame, "Table is empty. Nothing to add.");
        }
    }

    //Checks if the given row data already exists in the list
    private boolean containsRowData(List<Object[]> list, Object[] rowData) {
        for (Object[] existingRow : list) {
            if (java.util.Arrays.equals(existingRow, rowData)) {
                return true; // Row data already exists in the list
            }
        }
        return false; // Row data is not present in the list
    }
 
    // Methods to show export options for the "Customers" table
    private void showCustomerOptionPane() {
        JTextField customerNumberField = new JTextField(20);
        JTextField countryField = new JTextField(20);
        JTextField cityField = new JTextField(20);
        JTextField customerNameField = new JTextField(20);

        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel byCustomerNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel byCountryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel byCityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel byCustomerNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        tabbedPane.addTab("Customer Number", byCustomerNumberPanel);
        tabbedPane.addTab("Country", byCountryPanel);
        tabbedPane.addTab("City", byCityPanel);
        tabbedPane.addTab("Customer Name", byCustomerNamePanel);

        byCustomerNumberPanel.add(new JLabel("Customer Number:"));
        byCustomerNumberPanel.add(customerNumberField);

        byCountryPanel.add(new JLabel("Country:"));
        byCountryPanel.add(countryField);

        byCityPanel.add(new JLabel("City:"));
        byCityPanel.add(cityField);

        byCustomerNamePanel.add(new JLabel("Customer Name:"));
        byCustomerNamePanel.add(customerNameField);

     
        // Creates a dialog for specifying the export criteria
        JDialog dialog = new JDialog(frame, "Specify Export", true);
        dialog.getContentPane().setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));  

        dialog.getContentPane().add(tabbedPane);
     
        // Button to confirm the export criteria
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                switch (selectedIndex) {
                    case 0:
                        // Export by Customer Number
                        String customerNumber = customerNumberField.getText();
                        String queryByCustomerNumber = "SELECT * FROM customers WHERE customerNumber = '" + customerNumber + "'";
                        executeQueryAndDisplayResult(queryByCustomerNumber);
                        break;
                    case 1:
                        // Export by Country
                        String country = countryField.getText();
                        String queryByCountry = "SELECT * FROM customers WHERE country = '" + country + "'";
                        executeQueryAndDisplayResult(queryByCountry);
                        break;
                    case 2:
                        // Export by City
                        String city = cityField.getText();
                        String queryByCity = "SELECT * FROM customers WHERE city = '" + city + "'";
                        executeQueryAndDisplayResult(queryByCity);
                        break;
                    case 3:
                        // Export by Customer Name
                        String customerName = customerNameField.getText();
                        String queryByCustomerName = "SELECT * FROM customers WHERE customerName = '" + customerName + "'";
                        executeQueryAndDisplayResult(queryByCustomerName);
                        break;
                    default:
                        break;
                }
              // Closes the dialog when the "OK" button is clicked
                dialog.dispose();
            }
        });

         // ActionListener for the "Cancel" button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
     
        // Panel to hold the "OK" and "Cancel" buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        dialog.getContentPane().add(buttonPanel);
     
        // Sets the size, location, and visibility of the dialog
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void showPaymentsOptionPane() {
    	// Creates text fields for entering customer and check numbers
    	JTextField customerNumberField = new JTextField(20);
    	JTextField checkNumberField = new JTextField(20);

    	// Creates a tabbed pane for selecting export options
    	JTabbedPane tabbedPane = new JTabbedPane();
    	JPanel byCustomerNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JPanel byCheckNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    	tabbedPane.addTab("Customer Number", byCustomerNumberPanel);
    	tabbedPane.addTab("Check Number", byCheckNumberPanel);

    	byCustomerNumberPanel.add(new JLabel("Customer Number:"));
    	byCustomerNumberPanel.add(customerNumberField);

    	byCheckNumberPanel.add(new JLabel("Check Number:"));
    	byCheckNumberPanel.add(checkNumberField);

    	// Create a dialog for exporting payments
    	JDialog dialog = new JDialog(frame, "Specify Export", true);
    	dialog.getContentPane().setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

    	dialog.getContentPane().add(tabbedPane);

    	// ActionListener for the "OK" button
    	JButton okButton = new JButton("OK");
    	okButton.addActionListener(new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	        // Get the selected tab index
    	        int selectedIndex = tabbedPane.getSelectedIndex();

    	        // Handle the selected tab to determine export criteria
    	        switch (selectedIndex) {
    	            case 0:
    	                // Export by Customer Number
    	                String customerNumber = customerNumberField.getText();
    	                String queryByCustomerNumber = "SELECT * FROM payments WHERE customerNumber = '" + customerNumber + "'";
    	                executeQueryAndDisplayResult(queryByCustomerNumber);
    	                break;
    	            case 1:
    	                // Export by Check Number
    	                String checkNumber = checkNumberField.getText();
    	                String queryByCheckNumber = "SELECT * FROM payments WHERE checkNumber = '" + checkNumber + "'";
    	                executeQueryAndDisplayResult(queryByCheckNumber);
    	                break;
    	            default:
    	                break;
    	        }
    	        
    	        // Closes the dialog after handling the export criteria
    	        dialog.dispose();
    	    }
    	});

    	// ActionListener for the "Cancel" button
    	JButton cancelButton = new JButton("Cancel");
    	cancelButton.addActionListener(new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	        // Closes the dialog when the "Cancel" button is clicked
    	        dialog.dispose();
    	    }
    	});

    	// Panel to hold the "OK" and "Cancel" buttons
    	JPanel buttonPanel = new JPanel();
    	buttonPanel.add(okButton);
    	buttonPanel.add(cancelButton);

    	dialog.getContentPane().add(buttonPanel);

    	// Set the size, location, and visibility of the dialog
    	dialog.setSize(400, 200);
    	dialog.setLocationRelativeTo(frame);
    	dialog.setVisible(true);
    }


    private void showEmployeesOptionPane() {
    	// Creates text fields for entering office code, job title, reports to, and employee number
    	JTextField officeCodeField = new JTextField(15);
    	JTextField jobTitleField = new JTextField(15);
    	JTextField reportsToField = new JTextField(15);
    	JTextField employeeNumberField = new JTextField(15);

    	// Creates a tabbed pane for selecting export options
    	JTabbedPane tabbedPane = new JTabbedPane();
    	JPanel byOfficeCodePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JPanel byJobTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JPanel byReportsToPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JPanel byemployeeNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    	tabbedPane.addTab("Office Code", byOfficeCodePanel);
    	tabbedPane.addTab("Job Title", byJobTitlePanel);
    	tabbedPane.addTab("Reports To", byReportsToPanel);
    	tabbedPane.addTab("Employee Number", byemployeeNumberPanel);

    	byOfficeCodePanel.add(new JLabel("Office Code:"));
    	byOfficeCodePanel.add(officeCodeField);

    	byJobTitlePanel.add(new JLabel("Job Title:"));
    	byJobTitlePanel.add(jobTitleField);

    	byReportsToPanel.add(new JLabel("Reports To:"));
    	byReportsToPanel.add(reportsToField);

    	byemployeeNumberPanel.add(new JLabel("Employee Number:"));
    	byemployeeNumberPanel.add(employeeNumberField);

    	// Creates a dialog for exporting employees
    	JDialog dialog = new JDialog(frame, "Specify Export", true);
    	dialog.getContentPane().setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

    	dialog.getContentPane().add(tabbedPane);

    	// ActionListener for the "OK" button
    	JButton okButton = new JButton("OK");
    	okButton.addActionListener(new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	        // Get the selected tab index
    	        int selectedIndex = tabbedPane.getSelectedIndex();

    	        // Handle the selected tab to determine export criteria
    	        switch (selectedIndex) {
    	            case 0:
    	                // Export by Office Code
    	                String officeCode = officeCodeField.getText();
    	                String queryByOfficeCode = "SELECT * FROM employees WHERE officecode = '" + officeCode + "'";
    	                executeQueryAndDisplayResult(queryByOfficeCode);
    	                break;
    	            case 1:
    	                // Export by Job Title
    	                String jobTitle = jobTitleField.getText();
    	                String queryByJobTitle = "SELECT * FROM employees WHERE jobTitle = '" + jobTitle + "'";
    	                executeQueryAndDisplayResult(queryByJobTitle);
    	                break;
    	            case 2:
    	                // Export by Reports To
    	                String reportsTo = reportsToField.getText();
    	                String queryByReportsTo = "SELECT * FROM employees WHERE reportsTo = '" + reportsTo + "'";
    	                executeQueryAndDisplayResult(queryByReportsTo);
    	                break;
    	            case 3:
    	                // Export by Employee Number
    	                String employeeNumber = employeeNumberField.getText();
    	                String queryByEmployeeNumber = "SELECT * FROM employees WHERE employeeNumber = '" + employeeNumber + "'";
    	                executeQueryAndDisplayResult(queryByEmployeeNumber);
    	                break;
    	            default:
    	                break;
    	        }

    	        // Close the dialog after handling the export criteria
    	        dialog.dispose();
    	    }
    	});

    	// ActionListener for the "Cancel" button
    	JButton cancelButton = new JButton("Cancel");
    	cancelButton.addActionListener(new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	        // Close the dialog when the "Cancel" button is clicked
    	        dialog.dispose();
    	    }
    	});

    	// Panel to hold the "OK" and "Cancel" buttons
    	JPanel buttonPanel = new JPanel();
    	buttonPanel.add(okButton);
    	buttonPanel.add(cancelButton);

    	dialog.getContentPane().add(buttonPanel);

    	// Set the size, location, and visibility of the dialog
    	dialog.setSize(400, 200);
    	dialog.setLocationRelativeTo(frame);
    	dialog.setVisible(true);
    }
    private void showProductlinesOptionPane() {
    	// Creates a text field for entering the product line
    	JTextField plField = new JTextField();

    	// Creates an array of objects for the dialog components
    	Object[] message = {
    	    "ProductLine:", plField,
    	};

    	// Show a confirmation dialog with OK and Cancel options
    	int option = JOptionPane.showConfirmDialog(frame, message, "Export by ProductLine", JOptionPane.OK_CANCEL_OPTION);

    	// Check if the user clicked OK
    	if (option == JOptionPane.OK_OPTION) {
    	    // Retrieve the entered product line
    	    String pl = plField.getText();

    	    // Create a SQL query to export by the specified product line
    	    String query = "SELECT * FROM productlines WHERE productline = '" + pl + "'";

    	    // Execute the query and display the result
    	    executeQueryAndDisplayResult(query);
    	}
    }

    private void showOrderDetailsOptionPane() {
    	// Create text fields for entering order number, product code, and order line number
    	JTextField orderNumberField = new JTextField(20);
    	JTextField productCodeField = new JTextField(20);
    	JTextField orderLineNumberField = new JTextField(20);

    	// Creates a tabbed pane for selecting export options
    	JTabbedPane tabbedPane = new JTabbedPane();
    	JPanel byOrderNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JPanel byProductCodePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JPanel byOrderLineNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    	tabbedPane.addTab("Order Number", byOrderNumberPanel);
    	tabbedPane.addTab("Product Code", byProductCodePanel);
    	tabbedPane.addTab("Order Line Number", byOrderLineNumberPanel);

    	byOrderNumberPanel.add(new JLabel("Order Number:"));
    	byOrderNumberPanel.add(orderNumberField);

    	byProductCodePanel.add(new JLabel("Product Code:"));
    	byProductCodePanel.add(productCodeField);

    	byOrderLineNumberPanel.add(new JLabel("Order Line Number:"));
    	byOrderLineNumberPanel.add(orderLineNumberField);

    	// Creates a dialog for exporting order details
    	JDialog dialog = new JDialog(frame, "Specify Export", true);
    	dialog.getContentPane().setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

    	dialog.getContentPane().add(tabbedPane);

    	// ActionListener for the "OK" button
    	JButton okButton = new JButton("OK");
    	okButton.addActionListener(new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	        // Get the selected tab index
    	        int selectedIndex = tabbedPane.getSelectedIndex();

    	        // Handle the selected tab to determine export criteria
    	        switch (selectedIndex) {
    	            case 0:
    	                // Export by Order Number
    	                String orderNumber = orderNumberField.getText();
    	                String queryByOrderNumber = "SELECT * FROM orderdetails WHERE orderNumber = '" + orderNumber + "'";
    	                executeQueryAndDisplayResult(queryByOrderNumber);
    	                break;
    	            case 1:
    	                // Export by Product Code
    	                String productCode = productCodeField.getText();
    	                String queryByProductCode = "SELECT * FROM orderdetails WHERE productCode = '" + productCode + "'";
    	                executeQueryAndDisplayResult(queryByProductCode);
    	                break;
    	            case 2:
    	                // Export by Order Line Number
    	                String orderLineNumber = orderLineNumberField.getText();
    	                String queryByOrderLineNumber = "SELECT * FROM orderdetails WHERE orderLineNumber = '" + orderLineNumber + "'";
    	                executeQueryAndDisplayResult(queryByOrderLineNumber);
    	                break;
    	            default:
    	                break;
    	        }

    	        // Closes the dialog after handling the export criteria
    	        dialog.dispose();
    	    }
    	});

    	// ActionListener for the "Cancel" button
    	JButton cancelButton = new JButton("Cancel");
    	cancelButton.addActionListener(new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	        // Close the dialog when the "Cancel" button is clicked
    	        dialog.dispose();
    	    }
    	});

    	// Panel to hold the "OK" and "Cancel" buttons
    	JPanel buttonPanel = new JPanel();
    	buttonPanel.add(okButton);
    	buttonPanel.add(cancelButton);

    	dialog.getContentPane().add(buttonPanel);

    	// Set the size, location, and visibility of the dialog
    	dialog.setSize(400, 200);
    	dialog.setLocationRelativeTo(frame);
    	dialog.setVisible(true);
    }


    // Executes the given SQL query and displays the result in a table.
    private void executeQueryAndDisplayResult(String query) {
        try {
            // Creates a statement using the database connection
            Statement statement = dbConnection.getConnection().createStatement();

            // Execute the query and retrieve the result set
            ResultSet resultSet = statement.executeQuery(query);

            // Set the result set as the data model for the result table
            resultTable.setModel(DbUtils.resultSetToTableModel(resultSet));

            // Close the result set and statement to free resources
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            // Print the stack trace in case of an exception
            e.printStackTrace();
        }
    }

    private void showOrderOptionPane() {
    	// Creates text fields for entering order number, status, and customer number
    	JTextField orderNumberField = new JTextField(20);
    	JTextField statusField = new JTextField(20);
    	JTextField customerNumberField = new JTextField(20);

    	// Creates a tabbed pane for selecting export options
    	JTabbedPane tabbedPane = new JTabbedPane();
    	JPanel byOrderNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JPanel byStatusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JPanel byCustomerNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    	tabbedPane.addTab("Order Number", byOrderNumberPanel);
    	tabbedPane.addTab("Status", byStatusPanel);
    	tabbedPane.addTab("Customer Number", byCustomerNumberPanel);

    	byOrderNumberPanel.add(new JLabel("Order Number:"));
    	byOrderNumberPanel.add(orderNumberField);

    	byStatusPanel.add(new JLabel("Status:"));
    	byStatusPanel.add(statusField);

    	byCustomerNumberPanel.add(new JLabel("Customer Number:"));
    	byCustomerNumberPanel.add(customerNumberField);

    	// Create a dialog for exporting orders
    	JDialog dialog = new JDialog(frame, "Specify Export", true);
    	dialog.getContentPane().setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

    	dialog.getContentPane().add(tabbedPane);

    	// ActionListener for the "OK" button
    	JButton okButton = new JButton("OK");
    	okButton.addActionListener(new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	        // Get the selected tab index
    	        int selectedIndex = tabbedPane.getSelectedIndex();

    	        // Handle the selected tab to determine export criteria
    	        switch (selectedIndex) {
    	            case 0:
    	                // Export by Order Number
    	                String orderNumber = orderNumberField.getText();
    	                String queryByOrderNumber = "SELECT * FROM orders WHERE orderNumber = '" + orderNumber + "'";
    	                executeQueryAndDisplayResult(queryByOrderNumber);
    	                break;
    	            case 1:
    	                // Export by Status
    	                String status = statusField.getText();
    	                String queryByStatus = "SELECT * FROM orders WHERE status = '" + status + "'";
    	                executeQueryAndDisplayResult(queryByStatus);
    	                break;
    	            case 2:
    	                // Export by Customer Number
    	                String customerNumber = customerNumberField.getText();
    	                String queryByCustomerNumber = "SELECT * FROM orders WHERE customerNumber = '" + customerNumber + "'";
    	                executeQueryAndDisplayResult(queryByCustomerNumber);
    	                break;
    	            default:
    	                break;
    	        }

    	        // Close the dialog after handling the export criteria
    	        dialog.dispose();
    	    }
    	});

    	// ActionListener for the "Cancel" button
    	JButton cancelButton = new JButton("Cancel");
    	cancelButton.addActionListener(new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	        // Close the dialog when the "Cancel" button is clicked
    	        dialog.dispose();
    	    }
    	});

    	// Panel to hold the "OK" and "Cancel" buttons
    	JPanel buttonPanel = new JPanel();
    	buttonPanel.add(okButton);
    	buttonPanel.add(cancelButton);

    	dialog.getContentPane().add(buttonPanel);

    	// Set the size, location, and visibility of the dialog
    	dialog.setSize(400, 200);
    	dialog.setLocationRelativeTo(frame);
    	dialog.setVisible(true);
    }

    private void showProductOptionPane() {
    	// Create text fields for entering product code, product line, and product vendor
    	JTextField productCodeField = new JTextField(20);
    	JTextField productLineField = new JTextField(20);
    	JTextField productVendorField = new JTextField(20);

    	// Create a tabbed pane for selecting export options
    	JTabbedPane tabbedPane = new JTabbedPane();
    	JPanel byProductCodePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JPanel byProductLinePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JPanel byProductVendorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    	tabbedPane.addTab("Product Code", byProductCodePanel);
    	tabbedPane.addTab("Product Line", byProductLinePanel);
    	tabbedPane.addTab("Product Vendor", byProductVendorPanel);

    	byProductCodePanel.add(new JLabel("Product Code:"));
    	byProductCodePanel.add(productCodeField);

    	byProductLinePanel.add(new JLabel("Product Line:"));
    	byProductLinePanel.add(productLineField);

    	byProductVendorPanel.add(new JLabel("Product Vendor:"));
    	byProductVendorPanel.add(productVendorField);

    	// Creates a dialog for exporting products
    	JDialog dialog = new JDialog(frame, "Specify Export", true);
    	dialog.getContentPane().setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

    	dialog.getContentPane().add(tabbedPane);

    	// ActionListener for the "OK" button
    	JButton okButton = new JButton("OK");
    	okButton.addActionListener(new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	        // Get the selected tab index
    	        int selectedIndex = tabbedPane.getSelectedIndex();

    	        // Handle the selected tab to determine export criteria
    	        switch (selectedIndex) {
    	            case 0:
    	                // Export by Product Code
    	                String productCode = productCodeField.getText();
    	                String queryByProductCode = "SELECT * FROM products WHERE productCode = '" + productCode + "'";
    	                executeQueryAndDisplayResult(queryByProductCode);
    	                break;
    	            case 1:
    	                // Export by Product Line
    	                String productLine = productLineField.getText();
    	                String queryByProductLine = "SELECT * FROM products WHERE productLine = '" + productLine + "'";
    	                executeQueryAndDisplayResult(queryByProductLine);
    	                break;
    	            case 2:
    	                // Export by Product Vendor
    	                String productVendor = productVendorField.getText();
    	                String queryByProductVendor = "SELECT * FROM products WHERE productVendor = '" + productVendor + "'";
    	                executeQueryAndDisplayResult(queryByProductVendor);
    	                break;
    	            default:
    	                break;
    	        }

    	        // Closes the dialog after handling the export criteria
    	        dialog.dispose();
    	    }
    	});

    	// ActionListener for the "Cancel" button
    	JButton cancelButton = new JButton("Cancel");
    	cancelButton.addActionListener(new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	        // Closes the dialog when the "Cancel" button is clicked
    	        dialog.dispose();
    	    }
    	});

    	// Panel to hold the "OK" and "Cancel" buttons
    	JPanel buttonPanel = new JPanel();
    	buttonPanel.add(okButton);
    	buttonPanel.add(cancelButton);

    	dialog.getContentPane().add(buttonPanel);

    	// Set the size, location, and visibility of the dialog
    	dialog.setSize(400, 200);
    	dialog.setLocationRelativeTo(frame);
    	dialog.setVisible(true);
    }
    private void showOfficeOptionPane() {
    	// Creates text fields for entering office code, country, territory, and city
    	JTextField officeCodeField = new JTextField(20);
    	JTextField countryField = new JTextField(20);
    	JTextField territoryField = new JTextField(20);
    	JTextField cityField = new JTextField(20);

    	// Creates a tabbed pane for selecting export options
    	JTabbedPane tabbedPane = new JTabbedPane();
    	JPanel byOfficeCodePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JPanel byCountryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JPanel byTerritoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JPanel byCityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    	tabbedPane.addTab("Office Code", byOfficeCodePanel);
    	tabbedPane.addTab("Country", byCountryPanel);
    	tabbedPane.addTab("Territory", byTerritoryPanel);
    	tabbedPane.addTab("City", byCityPanel);

    	byOfficeCodePanel.add(new JLabel("Office Code:"));
    	byOfficeCodePanel.add(officeCodeField);

    	byCountryPanel.add(new JLabel("Country:"));
    	byCountryPanel.add(countryField);

    	byTerritoryPanel.add(new JLabel("Territory:"));
    	byTerritoryPanel.add(territoryField);

    	byCityPanel.add(new JLabel("City:"));
    	byCityPanel.add(cityField);

    	// Creates a dialog for exporting offices
    	JDialog dialog = new JDialog(frame, "Specify Export", true);
    	dialog.getContentPane().setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

    	dialog.getContentPane().add(tabbedPane);

    	// ActionListener for the "OK" button
    	JButton okButton = new JButton("OK");
    	okButton.addActionListener(new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	        // Get the selected tab index
    	        int selectedIndex = tabbedPane.getSelectedIndex();

    	        // Handle the selected tab to determine export criteria
    	        switch (selectedIndex) {
    	            case 0:
    	                // Export by Office Code
    	                String officeCode = officeCodeField.getText();
    	                String queryByOfficeCode = "SELECT * FROM offices WHERE officeCode = '" + officeCode + "'";
    	                executeQueryAndDisplayResult(queryByOfficeCode);
    	                break;
    	            case 1:
    	                // Export by Country
    	                String country = countryField.getText();
    	                String queryByCountry = "SELECT * FROM offices WHERE country = '" + country + "'";
    	                executeQueryAndDisplayResult(queryByCountry);
    	                break;
    	            case 2:
    	                // Export by Territory
    	                String territory = territoryField.getText();
    	                String queryByTerritory = "SELECT * FROM offices WHERE territory = '" + territory + "'";
    	                executeQueryAndDisplayResult(queryByTerritory);
    	                break;
    	            case 3:
    	                // Export by City
    	                String city = cityField.getText();
    	                String queryByCity = "SELECT * FROM offices WHERE city = '" + city + "'";
    	                executeQueryAndDisplayResult(queryByCity);
    	                break;
    	            default:
    	                break;
    	        }

    	        // Closes the dialog after handling the export criteria
    	        dialog.dispose();
    	    }
    	});

    	// ActionListener for the "Cancel" button
    	JButton cancelButton = new JButton("Cancel");
    	cancelButton.addActionListener(new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	        // Close the dialog when the "Cancel" button is clicked
    	        dialog.dispose();
    	    }
    	});

    	// Panel to hold the "OK" and "Cancel" buttons
    	JPanel buttonPanel = new JPanel();
    	buttonPanel.add(okButton);
    	buttonPanel.add(cancelButton);

    	dialog.getContentPane().add(buttonPanel);

    	// Set the size, location, and visibility of the dialog
    	dialog.setSize(400, 200);
    	dialog.setLocationRelativeTo(frame);
    	dialog.setVisible(true);

    }
    
 
    // Exports data from the export list to a CSV file.
    private void exportToCSV() {
        // Checks if the export list is empty
        if (exportListModel == null || exportListModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(frame, "Export List is empty. Add data before exporting.");
            return;
        }

        // Creates a file chooser dialog for selecting the CSV file destination
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save as CSV");
        int userSelection = fileChooser.showSaveDialog(frame);

        // Checks if the user selected a file destination
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                // Gets the selected file path and append ".csv" extension
                String filePath = fileChooser.getSelectedFile().getAbsolutePath() + ".csv";

                // Creates FileWriter, BufferedWriter, and PrintWriter for writing to the CSV file
                FileWriter fileWriter = new FileWriter(filePath);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                PrintWriter printWriter = new PrintWriter(bufferedWriter);

                // Writes header row to the CSV file
                for (int i = 0; i < exportListModel.getColumnCount(); i++) {
                    printWriter.print(exportListModel.getColumnName(i));
                    if (i < exportListModel.getColumnCount() - 1) {
                        printWriter.print(",");
                    }
                }
                printWriter.println();

                // Writes data rows from the export list to the CSV file
                for (int i = 0; i < exportListModel.getRowCount(); i++) {
                    for (int j = 0; j < exportListModel.getColumnCount(); j++) {
                        printWriter.print(exportListModel.getValueAt(i, j));
                        if (j < exportListModel.getColumnCount() - 1) {
                            printWriter.print(",");
                        }
                    }
                    printWriter.println();
                }

                // Closes writers to release resources
                printWriter.close();
                bufferedWriter.close();
                fileWriter.close();

                // Displays success message
                JOptionPane.showMessageDialog(frame, "Exported to CSV successfully!");
            } catch (IOException e) {
                // Prints stack trace and display error message in case of an exception
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error exporting to CSV.");
            }
        }
    }
}