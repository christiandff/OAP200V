package com.group11.classicmodels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import javax.swing.*;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * @author Christian Douglas Farnes Fancy 
 * @author Even Hjerpseth Unneberg
 */

public class Customers extends ApplicationMenu{
	private static final long serialVersionUID = 1L;
	private JFrame frame; // Main frame of the application
    private JTable table; // Table for displaying customer data
    private JTextField txtsearchcustomerNumber; // TextField for searching customers by number
    private DatabaseConnection dbConnection; // Database connection instance
    private CRUD crudOperations; // CRUD operations handler

    // Various text fields for customer data input
    private JTextField txtcustomerNumber;
    private JTextField txtcustomerName;
    private JTextField txtcontactLastName;
    private JTextField txtcontactFirstName;
    private JTextField txtphone;
    private JTextField txtaddressLine1;
    private JTextField txtaddressLine2;
    private JTextField txtcity;
    private JTextField txtstate;
    private JTextField txtpostalCode;
    private JTextField txtcountry;
    private JTextField txtsalesRepEmployeeNumber;
    private JTextField txtcreditLimit;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Customers window = new Customers();
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
    public Customers () {
    	super();
        initialize(); // Initialize the contents of the frame
        dbConnection = new DatabaseConnection(); // Initialize the database connection
        crudOperations = new CRUD(dbConnection); // Initialize the CRUD operations
        tableLoad(); // Load the initial data into the table
        frame.setLocationRelativeTo(null);
    }

    /**
     * Load customer data into the table.
     */
    public void tableLoad() {
        try {
            // Prepare and execute a query to fetch all customer data
            PreparedStatement pst = dbConnection.getConnection().prepareStatement("select * from customers");
            ResultSet rs = pst.executeQuery();

            // Create a list to hold customer data objects
            List<CustomersData> customersDataList = new ArrayList<>();
            while (rs.next()) {
                // Retrieve data from the ResultSet and add it to the list
                CustomersData customersData = new CustomersData(
                        rs.getString("customerNumber"),
                        rs.getString("customerName"),
                        rs.getString("contactLastName"),
                        rs.getString("contactFirstName"),
                        rs.getString("phone"),
                        rs.getString("addressLine1"),
                        rs.getString("addressLine2"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("postalCode"),
                        rs.getString("country"),
                        rs.getString("salesRepEmployeeNumber"),
                        rs.getString("creditLimit")
                );
                customersDataList.add(customersData);
            }

            // Set the custom table model with the fetched data
            CustomersTableModel tableModel = new CustomersTableModel(customersDataList);
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
     * Initialize the contents of the frame.
     */
    private void initialize() {
    	// Initialize the main frame, Set frame size and position, Set default close operation, Use absolute layout
        frame = new JFrame(); 
        frame.setBounds(100, 100, 1000, 700); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.getContentPane().setLayout(null); 
        frame.setJMenuBar(this);
        // Define the options for the query ComboBox
        String[] queryOptions = {"All customers", "Europe", "Asia", "America", "Africa", "Oceania"};
        
        // Initialize the query ComboBox with the defined options, Set position and size, Add ComboBox to the frame
        final JComboBox<String> queryComboBox = new JComboBox<>(queryOptions);
        queryComboBox.setModel(new DefaultComboBoxModel<String>(queryOptions));
        queryComboBox.setBounds(859, 373, 100, 25);  
        frame.getContentPane().add(queryComboBox); 

        // Initialize and configure the 'Set Query' button
        JButton btnExecuteQuery = new JButton("Set Query");
        btnExecuteQuery.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedQuery = (String) queryComboBox.getSelectedItem();
                executeSelectedQuery(selectedQuery); // Execute the selected query
            }
        });
        btnExecuteQuery.setBounds(862, 337, 115, 25); // Set position and size
        frame.getContentPane().add(btnExecuteQuery); // Add button to the frame

        // Initialize and configure the label for customer details
        JLabel lblcustomers = new JLabel("Customers");
        lblcustomers.setFont(new Font("Tahoma", Font.BOLD, 30)); // Set font
        lblcustomers.setHorizontalAlignment(SwingConstants.CENTER); // Center align text
        lblcustomers.setBounds(331, -2, 301, 44); // Set position and size
        frame.getContentPane().add(lblcustomers); // Add label to the frame

        // Initialize and configure the 'Exit' button
        JButton btnExit = new JButton("Exit");
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the application
            }
        });
        btnExit.setBounds(859, 7, 115, 35); // Set position and size
        frame.getContentPane().add(btnExit); // Add button to the frame

        // Initialize and configure the scroll pane for the table
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(81, 118, 771, 413); // Set position and size
        frame.getContentPane().add(scrollPane); // Add scroll pane to the frame

        table = new JTable(); // Initialize the table
        scrollPane.setViewportView(table); // Add table to the scroll pane

        // Initialize and configure the search panel
        JPanel panel_1 = new JPanel();
        panel_1.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_1.setBounds(262, 33, 378, 77); // Set position and size
        frame.getContentPane().add(panel_1); // Add panel to the frame
        panel_1.setLayout(null); // Use absolute layout

        // Initialize and configure the search text field
        txtsearchcustomerNumber = new JTextField();
        txtsearchcustomerNumber.setColumns(10);
        txtsearchcustomerNumber.setBounds(174, 30, 180, 20); // Set position and size
        panel_1.add(txtsearchcustomerNumber); // Add text field to the panel
        txtsearchcustomerNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchProduct(); // Call search method on key release
            }
        });

        // Initialize and configure the label for 'Customer Number' search
        JLabel lblsearchcustomerNumber = new JLabel("Customer Number");
        lblsearchcustomerNumber.setFont(new Font("Tahoma", Font.BOLD, 14)); // Set font
        lblsearchcustomerNumber.setBounds(9, 31, 154, 14); // Set position and size
        panel_1.add(lblsearchcustomerNumber); // Add label to the panel

        // Initialize and configure the 'Delete' button
        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteProduct(); // Call deleteProduct method when clicked
            }
        });
        btnDelete.setBounds(859, 156, 115, 35); // Set position and size
        frame.getContentPane().add(btnDelete); // Add button to the frame

        // Initialize and configure the 'Back to Menu' button
        JButton btnBackToMenu = new JButton("Back to menu");
        btnBackToMenu.addActionListener(new ActionListener() {
            @SuppressWarnings("static-access")
            public void actionPerformed(ActionEvent e) {
                Overview overview = new Overview(); // Create an instance of Overview class
                overview.main(null); // Call the main method of Overview
                frame.dispose(); // Close the current frame
            }
        });
        btnBackToMenu.setBounds(10, 11, 115, 35); // Set position and size
        frame.getContentPane().add(btnBackToMenu); // Add button to the frame

        // Initialize and configure the 'Import CSV' button
        JButton btnImportCSV = new JButton("Import CSV");
        btnImportCSV.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                importCSV(); // Call importCSV method when clicked
            }
        });
        btnImportCSV.setBounds(859, 99, 115, 35); // Set position and size
        frame.getContentPane().add(btnImportCSV); // Add button to the frame

        // Initialize and configure the 'Add/Update' button
        JButton btnAddemp = new JButton("Add/Update ");
        btnAddemp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	openAddUppdateWindow(); // Open the window to add or update customers
            }
        });
        btnAddemp.setBounds(859, 53, 115, 35); // Set position and size
        frame.getContentPane().add(btnAddemp); // Add button to the frame

        // Initialize a dialog for SQL text area (e.g., for displaying SQL queries)
        JDialog sqlDialog = new JDialog();
        sqlDialog.getContentPane().setLayout(new BorderLayout());
        sqlDialog.setSize(400, 300); // Set dialog size

        // Initialize and configure a text area for SQL
        final JTextArea sqlTextArea = new JTextArea();
        sqlTextArea.setLineWrap(true); // Enable line wrapping
        JScrollPane scrollPane1 = new JScrollPane(sqlTextArea); // Add text area to scroll pane
        sqlDialog.getContentPane().add(scrollPane1, BorderLayout.CENTER); // Add scroll pane to dialog
    	};


    	/**
    	 * Searches for customers based on the customer number entered in the search field.
    	 */
    	private void searchProduct() {
    	    try {
    	        // Retrieve the customer number from the search text field
    	        String customerNumber = txtsearchcustomerNumber.getText();

    	        // Base SQL query to select customer data
    	        String query = "SELECT customerNumber, customerName, contactLastName, contactFirstName, phone, addressLine1, addressLine2, city, state, postalCode, country, salesRepEmployeeNumber, creditLimit FROM customers";

    	        // Append condition to the query if customer number is not empty
    	        if (!customerNumber.isEmpty()) {
    	            query += " WHERE customerNumber = ?";
    	        }

    	        // Prepare the SQL statement with the constructed query
    	        try (PreparedStatement pst = dbConnection.getConnection().prepareStatement(query)) {
    	            // Set the customer number in the query if it is not empty
    	            if (!customerNumber.isEmpty()) {
    	                pst.setString(1, customerNumber);
    	            }

    	            // Execute the query and store the result in ResultSet
    	            ResultSet rs = pst.executeQuery();

    	            // List to store the fetched customer data
    	            List<CustomersData> customersDataList = new ArrayList<>();
    	            while (rs.next()) {
    	                // Create a new CustomersData object for each row in the ResultSet
    	                CustomersData customersData = new CustomersData(
    	                        rs.getString("customerNumber"),
    	                        rs.getString("customerName"),
    	                        rs.getString("contactLastName"),
    	                        rs.getString("contactFirstName"),
    	                        rs.getString("phone"),
    	                        rs.getString("addressLine1"),
    	                        rs.getString("addressLine2"),
    	                        rs.getString("city"),
    	                        rs.getString("state"),
    	                        rs.getString("postalCode"),
    	                        rs.getString("country"),
    	                        rs.getString("salesRepEmployeeNumber"),
    	                        rs.getString("creditLimit")
    	                );
    	                // Add the customer data object to the list
    	                customersDataList.add(customersData);
    	            }

    	            // Set a new table model with the fetched data list
	    	            CustomersTableModel tableModel = new CustomersTableModel(customersDataList);
	    	            table.setModel(tableModel);
	    	        }
	    	    } catch (SQLException e) {
	    	        // Handle SQL exception in a user-friendly way
	    	        JOptionPane.showMessageDialog(frame, "Error searching product: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
	    	        e.printStackTrace();
	    	    }
	    	}
    
    	/**
    	 * Executes a query based on the selected region or category.
    	 * @param selectedQuery The selected category or region for the query.
    	 */
    	private void executeSelectedQuery(String selectedQuery) {
    	    String customQuery;

    	    // Switch statement to handle different region/category selections
    	    switch (selectedQuery) {
    	        case "All customers":
    	            // Query to select all customers
    	            customQuery = "SELECT customerNumber, customerName, contactLastName, contactFirstName, phone, addressLine1, addressLine2, city, state, postalCode, country, salesRepEmployeeNumber, creditLimit FROM customers";
    	            break;
    	        case "Europe":
    	            // Query to select customers from European countries
    	            customQuery = "SELECT customerNumber, customerName, contactLastName, contactFirstName, phone, addressLine1, addressLine2, city, state, postalCode, country, salesRepEmployeeNumber, creditLimit FROM customers WHERE country IN ('Denmark','Norway','Sweden','France','Germany','Polen','Spain','Portugal','Finland','UK','Ireland','Italy','Switzerland','Netherlands','Belgium','Austria','Russia')";
    	            break;
    	        case "Asia":
    	            // Query to select customers from Asian countries
    	            customQuery = "SELECT customerNumber, customerName, contactLastName, contactFirstName, phone, addressLine1, addressLine2, city, state, postalCode, country, salesRepEmployeeNumber, creditLimit FROM customers WHERE country IN ('Singapore','Japan', 'HongKong','Isreal','Philippines')";
    	            break;
    	        case "America":
    	            // Query to select customers from America
    	            customQuery = "SELECT customerNumber, customerName, contactLastName, contactFirstName, phone, addressLine1, addressLine2, city, state, postalCode, country, salesRepEmployeeNumber, creditLimit FROM customers WHERE country IN ('USA')";
    	            break;
    	        case "Africa":
    	            // Query to select customers from African countries
    	            customQuery = "SELECT customerNumber, customerName, contactLastName, contactFirstName, phone, addressLine1, addressLine2, city, state, postalCode, country, salesRepEmployeeNumber, creditLimit FROM customers WHERE country IN ('South Africa')";
    	            break;
    	        case "Oceania":
    	            // Query to select customers from Oceania region
    	            customQuery = "SELECT customerNumber, customerName, contactLastName, contactFirstName, phone, addressLine1, addressLine2, city, state, postalCode, country, salesRepEmployeeNumber, creditLimit FROM customers WHERE country IN ('New Zealand', 'Australia')";
    	            break;
    	        default:
    	            customQuery = ""; // Default case to handle unexpected selections
    	    }
    	    // Check if customQuery is empty and handle accordingly
    	    if (customQuery.isEmpty()) {
    	        JOptionPane.showMessageDialog(frame, "No valid query selected", "Query Error", JOptionPane.ERROR_MESSAGE);
    	        return;
    	    }

    	    try (PreparedStatement pst = dbConnection.getConnection().prepareStatement(customQuery)) {
    	        ResultSet rs = pst.executeQuery(); // Execute the query

    	        // List to store the fetched data
    	        List<CustomersData> customersDataList = new ArrayList<>();
    	        while (rs.next()) {
    	            // Create a CustomersData object for each row in ResultSet
    	            CustomersData CustomersData = new CustomersData(
    	                    rs.getString("customerNumber"),
    	                    rs.getString("customerName"),
    	                    rs.getString("contactLastName"),
    	                    rs.getString("contactFirstName"),
    	                    rs.getString("phone"),
    	                    rs.getString("addressLine1"),
    	                    rs.getString("addressLine2"),
    	                    rs.getString("city"),
    	                    rs.getString("state"),
    	                    rs.getString("postalCode"),
    	                    rs.getString("country"),
    	                    rs.getString("salesRepEmployeeNumber"),
    	                    rs.getString("creditLimit")
    	            );
    	            // Add the customer data to the list
    	            customersDataList.add(CustomersData);
    	        }

    	        // Set a new table model with the fetched data list
    	        CustomersTableModel tableModel = new CustomersTableModel(customersDataList);
    	        table.setModel(tableModel); // Update the table model
    	    } catch (SQLException ex) {
    	        JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    	        ex.printStackTrace();
    	    }
    	}


    	/**
    	 * Opens a new window for adding or updating information.
    	 */
    	private void openAddUppdateWindow() {
    	    // Initialize the frame for adding new orders (not used in this context)
    	    final JFrame addOrderFrame = new JFrame("Add New Order");
    	    addOrderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	    addOrderFrame.getContentPane().setLayout(new BorderLayout());
    	    addOrderFrame.setSize(600, 600);

    	    // Initialize the frame for adding or updating customers
    	    final JFrame addcustomersFrame = new JFrame("Add New Customers");
    	    addcustomersFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	    addcustomersFrame.getContentPane().setLayout(new BorderLayout());
    	    addcustomersFrame.setSize(600, 604);

    	    // Search panel for customers
    	    JPanel searchPanel = new JPanel();
    	    searchPanel.setBorder(new TitledBorder(null, "Search Customers", TitledBorder.LEADING, TitledBorder.TOP, null, null));
    	    searchPanel.setBounds(10, 405, 460, 60);
    	    addcustomersFrame.getContentPane().add(searchPanel);
    	    searchPanel.setLayout(null);

    	    // Text field for searching customers
    	    final JTextField txtSearch = new JTextField();
    	    txtSearch.setBounds(20, 508, 170, 30);
    	    searchPanel.add(txtSearch);

    	    // Button for searching customers
    	    JButton btnSearch = new JButton("Search");
    	    btnSearch.addActionListener(new ActionListener() {
    	        public void actionPerformed(ActionEvent e) {
    	            searchcustomers(txtSearch.getText(), addcustomersFrame);
    	        }
    	    });
    	    btnSearch.setBounds(200, 508, 180, 30);
    	    searchPanel.add(btnSearch);

    	    // Panel for customer registration form
    	    JPanel panel = new JPanel();
    	    panel.setBorder(new TitledBorder(null, "Registration", TitledBorder.LEADING, TitledBorder.TOP, null, null));
    	    panel.setBounds(10, 22, 378, 475);
    	    panel.setLayout(null);
    	    searchPanel.add(panel);

    	    // Labels and text fields for various customer attributes
    	    // For each attribute, a label and a text field are created and added to the panel
    	    JLabel lblcustomerNumber = new JLabel("Customer Number");
    	    lblcustomerNumber.setFont(new Font("Tahoma", Font.BOLD, 14));
    	    lblcustomerNumber.setBounds(10, 11, 168, 19);
    	    panel.add(lblcustomerNumber);

    	 // Create a label for customer name
    	    JLabel lblcustomerName = new JLabel("Customer Name");
    	    lblcustomerName.setFont(new Font("Tahoma", Font.BOLD, 14));
    	    lblcustomerName.setBounds(10, 41, 168, 20);
    	    panel.add(lblcustomerName);

    	    // Create a label for contact last name
    	    JLabel lblcontactLastName = new JLabel("Contact Last Name");
    	    lblcontactLastName.setFont(new Font("Tahoma", Font.BOLD, 14));
    	    lblcontactLastName.setBounds(10, 72, 168, 20);
    	    panel.add(lblcontactLastName);

    	    // Create a label for contact first name
    	    JLabel lblcontactFirstName = new JLabel("Contact First Name");
    	    lblcontactFirstName.setFont(new Font("Tahoma", Font.BOLD, 14));
    	    lblcontactFirstName.setBounds(10, 104, 168, 20);
    	    panel.add(lblcontactFirstName);

    	    // Create a label for phone
    	    JLabel lblphone = new JLabel("Phone");
    	    lblphone.setFont(new Font("Tahoma", Font.BOLD, 14));
    	    lblphone.setBounds(10, 135, 168, 20);
    	    panel.add(lblphone);

    	    // Create a label for address line 1 
    	    JLabel lbladdressLine1 = new JLabel("AddressLine1");
    	    lbladdressLine1.setFont(new Font("Tahoma", Font.BOLD, 14));
    	    lbladdressLine1.setBounds(10, 166, 168, 20);
    	    panel.add(lbladdressLine1);

    	    // Create a label for address line 2 
    	    JLabel lbladdressLine2 = new JLabel("AddressLine2");
    	    lbladdressLine2.setFont(new Font("Tahoma", Font.BOLD, 14));
    	    lbladdressLine2.setBounds(10, 197, 168, 20);
    	    panel.add(lbladdressLine2);

    	    // Create a label for city
    	    JLabel lblcity = new JLabel("City");
    	    lblcity.setFont(new Font("Tahoma", Font.BOLD, 14));
    	    lblcity.setBounds(10, 228, 168, 20);
    	    panel.add(lblcity);

    	    // Create a label for state
    	    JLabel lblstate = new JLabel("State");
    	    lblstate.setFont(new Font("Tahoma", Font.BOLD, 14));
    	    lblstate.setBounds(10, 259, 168, 20);
    	    panel.add(lblstate);

    	    // Create a label for postal code
    	    JLabel lblpostalCode = new JLabel("Postal Code");
    	    lblpostalCode.setFont(new Font("Tahoma", Font.BOLD, 14));
    	    lblpostalCode.setBounds(10, 290, 168, 20);
    	    panel.add(lblpostalCode);

    	    // Create a label for credit limit
    	    JLabel lblcreditLimit = new JLabel("Credit Limit");
    	    lblcreditLimit.setFont(new Font("Tahoma", Font.BOLD, 14));
    	    lblcreditLimit.setBounds(10, 382, 168, 20);
    	    panel.add(lblcreditLimit);

    	    // Create a label for country
    	    JLabel lblcountry = new JLabel("Country");
    	    lblcountry.setBounds(10, 323, 168, 20);
    	    lblcountry.setFont(new Font("Tahoma", Font.BOLD, 14));
    	    panel.add(lblcountry);

    	    // Create a label for sales representative employee number
    	    JLabel lblsalesRepEmployeeNumber = new JLabel("Sales Rep Employee Number");
    	    lblsalesRepEmployeeNumber.setBounds(10, 354, 186, 20);
    	    lblsalesRepEmployeeNumber.setFont(new Font("Tahoma", Font.BOLD, 14));
    	    panel.add(lblsalesRepEmployeeNumber);

    	    // Create text fields for various data inputs
    	    txtcustomerNumber = new JTextField();
    	    txtcustomerNumber.setBounds(175, 12, 180, 20);
    	    txtcustomerNumber.setColumns(10);
    	    panel.add(txtcustomerNumber);

    	    // Create text fields for various data inputs
    	    txtcustomerName = new JTextField();
    	    txtcustomerName.setColumns(10);
    	    txtcustomerName.setBounds(175, 41, 180, 20);
    	    panel.add(txtcustomerName);

    	    // Create text fields for various data inputs
    	    txtcontactLastName = new JTextField();
    	    txtcontactLastName.setColumns(10);
    	    txtcontactLastName.setBounds(175, 72, 180, 20);
    	    panel.add(txtcontactLastName);

    	    // Create text fields for various data inputs
    	    txtcontactFirstName = new JTextField();
    	    txtcontactFirstName.setColumns(10);
    	    txtcontactFirstName.setBounds(175, 104, 180, 20);
    	    panel.add(txtcontactFirstName);

    	    // Create text fields for various data inputs
    	    txtphone = new JTextField();
    	    txtphone.setColumns(10);
    	    txtphone.setBounds(175, 135, 180, 20);
    	    panel.add(txtphone);

    	    // Create text fields for various data inputs
    	    txtaddressLine1 = new JTextField();
    	    txtaddressLine1.setColumns(10);
    	    txtaddressLine1.setBounds(175, 166, 180, 20);
    	    panel.add(txtaddressLine1);

    	    // Create text fields for various data inputs
    	    txtaddressLine2 = new JTextField();
    	    txtaddressLine2.setColumns(10);
    	    txtaddressLine2.setBounds(175, 197, 180, 20);
    	    panel.add(txtaddressLine2);

    	    // Create text fields for various data inputs
    	    txtpostalCode = new JTextField();
    	    txtpostalCode.setColumns(10);
    	    txtpostalCode.setBounds(175, 292, 180, 20);
    	    panel.add(txtpostalCode);

    	    // Create text fields for various data inputs
    	    txtcountry = new JTextField();
    	    txtcountry.setColumns(10);
    	    txtcountry.setBounds(175, 325, 180, 20);
    	    panel.add(txtcountry);

    	    // Create text fields for various data inputs
    	    txtsalesRepEmployeeNumber = new JTextField();
    	    txtsalesRepEmployeeNumber.setColumns(10);
    	    txtsalesRepEmployeeNumber.setBounds(198, 356, 158, 20);
    	    panel.add(txtsalesRepEmployeeNumber);

    	    // Create text fields for various data inputs
    	    txtcreditLimit = new JTextField();
    	    txtcreditLimit.setColumns(10);
    	    txtcreditLimit.setBounds(187, 382, 180, 20);
    	    panel.add(txtcreditLimit);

    	    // Create text fields for various data inputs
    	    txtstate = new JTextField();
    	    txtstate.setColumns(10);
    	    txtstate.setBounds(175, 259, 180, 20);
    	    panel.add(txtstate);

    	    // Create text fields for various data inputs
    	    txtcity = new JTextField();
    	    txtcity.setColumns(10);
    	    txtcity.setBounds(175, 230, 180, 20);
    	    panel.add(txtcity);

        
     // Buttons for saving and updating customer records
        JButton btnSave = new JButton("Save");
        btnSave.setBounds(10, 434, 168, 30);
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveProduct(); // Call method to save new customer record
            }
        });
        panel.add(btnSave);
        
     // Buttons for saving and updating customer records           
        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(187, 434, 180, 30);
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateProduct(); // Call method to update existing customer record
            }
        });
        panel.add(btnUpdate);

        // Display the window.
        addcustomersFrame.setVisible(true);
    }
    
    	/**
    	 * Searches for customer information in the database based on the given customer number
    	 * and updates the corresponding text fields with the found data.
    	 *
    	 * @param customerNumber The customer number to search for in the database.
    	 * @param frame The JFrame containing the user interface.
    	 */
    	private void searchcustomers (String customerNumber, JFrame frame) {
    	    try {
    	        // Prepare a SQL query to retrieve customer information based on customerNumber
    	        PreparedStatement pst = dbConnection.getConnection().prepareStatement(
    	            "SELECT customerNumber, customerName, contactLastName, contactFirstName, " +
    	            "phone, addressLine1, addressLine2, city, state, postalCode, country, " +
    	            "salesRepEmployeeNumber, creditLimit FROM customers WHERE customerNumber = ?");
    	        
    	        // Set the customerNumber parameter in the SQL query
    	        pst.setString(1, customerNumber);
    	        
    	        // Execute the query and retrieve the result set
    	        ResultSet rs = pst.executeQuery();
    	        
    	        if (rs.next()) {
    	            // Retrieve customer information from the result set
    	            String foundCustomerNumber = rs.getString(1);
    	            String foundCustomerName = rs.getString(2);
    	            String foundContactLastName = rs.getString(3);
    	            String foundContactFirstName = rs.getString(4);
    	            String foundPhone = rs.getString(5);
    	            String foundAddressLine1 = rs.getString(6);
    	            String foundAddressLine2 = rs.getString(7);
    	            String foundCity = rs.getString(8);
    	            String foundState = rs.getString(9);
    	            String foundPostalCode = rs.getString(10);
    	            String foundCountry = rs.getString(11);
    	            String foundSalesRepEmployeeNumber = rs.getString(12);
    	            String foundCreditLimit = rs.getString(13);

    	            // Update the text fields with the retrieved customer information
    	            txtcustomerNumber.setText(foundCustomerNumber);
    	            txtcustomerName.setText(foundCustomerName);
    	            txtcontactLastName.setText(foundContactLastName);
    	            txtcontactFirstName.setText(foundContactFirstName);
    	            txtphone.setText(foundPhone);
    	            txtaddressLine1.setText(foundAddressLine1);
    	            txtaddressLine2.setText(foundAddressLine2);
    	            txtcity.setText(foundCity);
    	            txtstate.setText(foundState);
    	            txtpostalCode.setText(foundPostalCode);
    	            txtcountry.setText(foundCountry);
    	            txtsalesRepEmployeeNumber.setText(foundSalesRepEmployeeNumber);
    	            txtcreditLimit.setText(foundCreditLimit);
    	        } else {
    	            // If no customer was found, clear all the text fields
    	            txtcustomerNumber.setText("");
    	            txtcustomerName.setText("");
    	            txtcontactLastName.setText("");
    	            txtcontactFirstName.setText("");
    	            txtphone.setText("");
    	            txtaddressLine1.setText("");
    	            txtaddressLine2.setText("");
    	            txtcity.setText("");
    	            txtstate.setText("");
    	            txtpostalCode.setText("");
    	            txtcountry.setText("");
    	            txtsalesRepEmployeeNumber.setText("");
    	            txtcreditLimit.setText("");
    	        }
    	    } catch (SQLException ex) {
    	        JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    	        ex.printStackTrace();
    	    }
    	}

    
    	/**
    	 * Saves customer information by extracting data from text fields and inserting it into the database.
    	 * After saving, it reloads the table and clears the input fields.
    	 */
    	public void saveProduct() {
    		try {
    	    // Create a Map to store column names and their corresponding values
    	    Map<String, Object> columnValues = new HashMap<>();
    	    
    	    // Extract data from text fields and put them into the Map
    	    columnValues.put("customerNumber", txtcustomerNumber.getText());
    	    columnValues.put("customerName", txtcustomerName.getText());
    	    columnValues.put("contactLastName", txtcontactLastName.getText());
    	    columnValues.put("contactFirstName", txtcontactFirstName.getText());
    	    columnValues.put("phone", txtphone.getText());
    	    columnValues.put("addressLine1", txtaddressLine1.getText());
    	    columnValues.put("addressLine2", txtaddressLine2.getText());
    	    columnValues.put("city", txtcity.getText());
    	    columnValues.put("state", txtstate.getText());
    	    columnValues.put("postalCode", txtpostalCode.getText());
    	    columnValues.put("country", txtcountry.getText());
    	    columnValues.put("salesRepEmployeeNumber", txtsalesRepEmployeeNumber.getText());
    	    columnValues.put("creditLimit", txtcreditLimit.getText());
    	    
    	    // Insert the customer information into the "customers" table using the CRUD operations
    	    crudOperations.insert("customers", columnValues);
    	    
    	    // Reload the table to reflect the updated data
    	    tableLoad();
    	    
    	    // Clear all input fields after saving
    	    clearFields();
    		} catch (Exception e) {
    		    // Display an error message using JOptionPane
    		    JOptionPane.showMessageDialog(frame, "Database operation failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    		    e.printStackTrace(); // Optional, for debugging purposes
    		}
    	}
    	
    
    	/**
    	 * Updates customer information in the database based on the data entered in the input fields.
    	 * After updating, it reloads the table and clears the input fields.
    	 */
    	public void updateProduct() {
    		try {
    	    // Create a Map to store updated column values
    	    Map<String, Object> updatedValues = new HashMap<>();
    	    
    	    // Extract updated data from text fields and put them into the Map
    	    updatedValues.put("customerName", txtcustomerName.getText());
    	    updatedValues.put("contactLastName", txtcontactLastName.getText());
    	    updatedValues.put("contactFirstName", txtcontactFirstName.getText());
    	    updatedValues.put("phone", txtphone.getText());
    	    updatedValues.put("addressLine1", txtaddressLine1.getText());
    	    updatedValues.put("addressLine2", txtaddressLine2.getText());
    	    updatedValues.put("city", txtcity.getText());
    	    updatedValues.put("state", txtstate.getText());
    	    updatedValues.put("postalCode", txtpostalCode.getText());
    	    updatedValues.put("country", txtcountry.getText());
    	    updatedValues.put("salesRepEmployeeNumber", txtsalesRepEmployeeNumber.getText());
    	    updatedValues.put("creditLimit", txtcreditLimit.getText());
    	    
    	    // Define the WHERE clause for the update operation
    	    String whereClause = "customerNumber = ?";
    	    
    	    // Create an array of WHERE clause arguments, using the customer number from the text field
    	    Object[] whereArgs = { txtcustomerNumber.getText() };

    	    // Update the customer information in the "customers" table using the CRUD operations
    	    crudOperations.update("customers", updatedValues, whereClause, whereArgs);
    	    
    	    // Reload the table to reflect the updated data
    	    tableLoad();
    	    
    	    // Clear all input fields after updating
    	    clearFields();
    		 } catch (Exception e) {
    			    // Display an error message using JOptionPane
    			    JOptionPane.showMessageDialog(frame, "Error updating product: " + e.getMessage(), "Update Error", JOptionPane.ERROR_MESSAGE);
    			    e.printStackTrace(); // Optional, for debugging purposes
    			}
    	}

    	/**
    	 * Clears all the input fields in the user interface by setting their text values to empty strings.
    	 * Additionally, it sets focus to the "state" text field.
    	 */
    	public void clearFields() {
    	    // Clear the text in all input fields
    	    txtcustomerNumber.setText("");
    	    txtcustomerName.setText("");
    	    txtcontactLastName.setText("");
    	    txtcontactFirstName.setText("");
    	    txtphone.setText("");
    	    txtaddressLine1.setText("");
    	    txtaddressLine2.setText("");
    	    txtcity.setText("");
    	    txtstate.setText("");
    	    txtpostalCode.setText("");
    	    txtcountry.setText("");
    	    txtsalesRepEmployeeNumber.setText("");
    	    txtcreditLimit.setText("");

    	    // Set focus to the "state" text field
    	    txtstate.requestFocus();
    	}
    	/**
    	 * Deletes a customer record from the database based on the customer number entered
    	 * in the search field. After deletion, it reloads the table and clears the input fields.
    	 * If the search field is empty or null, it displays an error message.
    	 */
    	public void deleteProduct() {
    	    // Check if the txtsearchcustomerNumber field is not null and not empty
    	    if (txtsearchcustomerNumber != null && !txtsearchcustomerNumber.getText().isEmpty()) {
    	        // Define the WHERE clause for the delete operation
    	        String whereClause = "customerNumber = ?";
    	        
    	        // Create an array of WHERE clause arguments using the customer number from the search field
    	        Object[] whereArgs = { txtsearchcustomerNumber.getText() };

    	        // Delete the customer record from the "customers" table using the CRUD operations
    	        crudOperations.delete("customers", whereClause, whereArgs);
    	        
    	        // Reload the table to reflect the updated data
    	        tableLoad();
    	        
    	        // Clear all input fields after deletion
    	        clearFields();
    	    } else {
    	        // Optionally, show an error message if the field is null or empty
    	        JOptionPane.showMessageDialog(frame, "Customer number is required for deletion.", "Error", JOptionPane.ERROR_MESSAGE);
    	    }
    	}
    	/**
    	 * Imports customer data from a CSV file, inserts it into the database, and refreshes the table view.
    	 * Displays appropriate messages for successful import or any encountered errors.
    	 */
    	private void importCSV() {
    	    // Create a file chooser dialog for selecting the CSV file
    	    JFileChooser fileChooser = new JFileChooser();
    	    FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
    	    fileChooser.setFileFilter(filter);
    	    int returnValue = fileChooser.showOpenDialog(frame);
    	    if (returnValue == JFileChooser.APPROVE_OPTION) {
    	        File selectedFile = fileChooser.getSelectedFile();
    	        try {
    	            // Open the CSV file for reading
    	            BufferedReader br = new BufferedReader(new FileReader(selectedFile));
    	            String line;
    	            // Skip the first line (header row)
    	            br.readLine();
    	            // Define the SQL query for inserting data into the "customers" table
    	            String sql = "INSERT INTO customers (customerNumber, customerName, contactLastName, contactFirstName, " +
    	                         "phone, addressLine1, addressLine2, city, state, postalCode, country, " +
    	                         "salesRepEmployeeNumber, creditLimit) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    	            // Prepare the SQL statement
    	            PreparedStatement statement = dbConnection.getConnection().prepareStatement(sql);
    	            while ((line = br.readLine()) != null) {
    	                String[] values = line.split(","); // Check if your CSV uses a different delimiter
    	                // Make sure to validate and sanitize values here if needed
    	                statement.setString(1, values[0]);
    	                statement.setString(2, values[1]);
    	                statement.setString(3, values[2]);
    	                statement.setString(4, values[3]);
    	                statement.setString(5, values[4]);
    	                statement.setString(6, values[5]);
    	                statement.setString(7, values[6]);
    	                statement.setString(8, values[7]);
    	                statement.setString(9, values[8]);
    	                statement.setString(10, values[9]);
    	                statement.setString(11, values[10]);
    	                statement.setString(12, values[11]);
    	                statement.setString(13, values[12]);
    	                
    	                // Execute the SQL statement to insert the data into the database
    	                statement.executeUpdate();
    	            }
    	            // Close the CSV file
    	            br.close();
    	            // Display a success message
    	            JOptionPane.showMessageDialog(frame, "Data Imported Successfully", "Info", JOptionPane.INFORMATION_MESSAGE);
    	            // Refresh the table view to reflect the imported data
    	            tableLoad();
    	        } catch (Exception e) {
    	            // Display an error message and print the exception details
    	            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    	            e.printStackTrace();
    	        }
    	    }
    	}
}