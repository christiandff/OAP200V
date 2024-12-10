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
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
//import javax.swing.filechooser.FileNameshippedDateFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
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
import java.io.File;
import java.io.FileReader;
import javax.swing.DefaultComboBoxModel;

/**
 * @author Christian Douglas Farnes Fancy
 * @author Even Hjerpseth Unneberg
 */

public class Orders extends ApplicationMenu{
	private static final long serialVersionUID = 1L;
    private JFrame frame;
    private JTable table;
    private JTextField txtsearchorderNumber;
    private DatabaseConnection dbConnection;
    private CRUD crudOperations;
    //
    // Various text fields for data input
    private JTextField txtorderNumber;
    private JTextField txtorderDate;
    private JTextField txtrequiredDate;
    private JTextField txtshippedDate;
    private JTextField txtstatus;
    private JTextField txtcomments;
    private JTextField txtcustomerNumber;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Orders window = new Orders();
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
    public Orders() {
    	super();
        initialize();
        dbConnection = new DatabaseConnection();
        crudOperations = new CRUD(dbConnection);
        tableLoad();
        frame.setJMenuBar(this);
        frame.setLocationRelativeTo(null);
    }
    /**
     * Load data into the table.
     */
    public void tableLoad() {
        try {
            // Prepare and execute a query to fetch all data
            PreparedStatement pst = dbConnection.getConnection().prepareStatement("select * from Orders");
            ResultSet rs = pst.executeQuery();

            // Create a list to hold data objects
            List<OrdersData> orderDataList = new ArrayList<>();
            while (rs.next()) {
                // Retrieve data from the ResultSet and add it to the list
                OrdersData orderData = new OrdersData(
                        rs.getString("orderNumber"),
                        rs.getString("orderDate"),
                        rs.getString("requiredDate"),
                        rs.getString("shippedDate"),
                        rs.getString("status"),
                        rs.getString("comments"),
                        rs.getString("customerNumber")
                );
                orderDataList.add(orderData);
            }

            OrdersTableModel tableModel = new OrdersTableModel(orderDataList);
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
    	frame.setJMenuBar(this);
    	String[] queryOptions = {"All Orders", "Shipped", "In Process", "On Hold", "Disputed", "Resolved", "Cancelled"};       // Define the options for the query ComboBox
    	
    	// Initialize the query ComboBox with the defined options, Set position and size, Add ComboBox to the frame
    	final JComboBox<String> queryComboBox = new JComboBox<>(queryOptions);
    	queryComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"All Orders", "Shipped", "In Process", "On Hold", "Disputed", "Resolved", "Cancelled"}));
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
        JLabel lblOrders = new JLabel("Orders");
        lblOrders.setFont(new Font("Tahoma", Font.BOLD, 30));
        lblOrders.setHorizontalAlignment(SwingConstants.CENTER);
        lblOrders.setBounds(331, -2, 301, 44);
        frame.getContentPane().add(lblOrders);
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
        
        txtsearchorderNumber = new JTextField();
        txtsearchorderNumber.setColumns(10);
        txtsearchorderNumber.setBounds(174, 30, 180, 20);
        panel_1.add(txtsearchorderNumber);
        
        txtsearchorderNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchProduct(); // Call search method on key release
            }
        });

        JLabel lblsearchorderNumber = new JLabel("Orders Number");
        lblsearchorderNumber.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblsearchorderNumber.setBounds(9, 31, 154, 14);
        panel_1.add(lblsearchorderNumber);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteOrders(); // Call deleteProduct method when clicked
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
        		openAddOrderWindow();
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
            String orderNumber = txtsearchorderNumber.getText();
            String query = "SELECT orderNumber, orderDate, requiredDate, shippedDate, status, comments, customerNumber FROM Orders";
            
            if (!orderNumber.isEmpty()) {
                query += " WHERE orderNumber = ?";
            }

            try (PreparedStatement pst = dbConnection.getConnection().prepareStatement(query)) {
                if (!orderNumber.isEmpty()) {
                    pst.setString(1, orderNumber);
                }

                ResultSet rs = pst.executeQuery();

                List<OrdersData> OrderDataList = new ArrayList<>();
                while (rs.next()) {
                    OrdersData OrderData = new OrdersData(
                            rs.getString("orderNumber"),
                            rs.getString("orderDate"),
                            rs.getString("requiredDate"),
                            rs.getString("shippedDate"),
                            rs.getString("status"),
                            rs.getString("comments"),
                            rs.getString("customerNumber")
                    );
                    OrderDataList.add(OrderData);
                }

                OrdersTableModel tableModel = new OrdersTableModel(OrderDataList);

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
            case "All Orders":
                customQuery = "SELECT orderNumber, orderDate, requiredDate, shippedDate, status, comments, customerNumber FROM Orders";
                break;
            case "Shipped":
            	// Query to select orders by Shipped
                customQuery = "SELECT orderNumber, orderDate, requiredDate, shippedDate, status, comments, customerNumber FROM Orders WHERE status IN ('Shipped')";
                break;
            case "In Process":
            	// Query to select orders by In Process
                customQuery = "SELECT orderNumber, orderDate, requiredDate, shippedDate, status, comments, customerNumber FROM Orders WHERE status IN ('In Process')";
                break;
            case "On Hold":
            	// Query to select orders by On Hold
                customQuery = "SELECT orderNumber, orderDate, requiredDate, shippedDate, status, comments, customerNumber FROM Orders WHERE status IN ('On Hold')";
                break;
            case "Disputed":
            	// Query to select orders by Disputed
                customQuery = "SELECT orderNumber, orderDate, requiredDate, shippedDate, status, comments, customerNumber FROM Orders WHERE status IN ('Disputed')";
                break;
            case "Resolved":
            	// Query to select orders by Resolved
                customQuery = "SELECT orderNumber, orderDate, requiredDate, shippedDate, status, comments, customerNumber FROM Orders WHERE status IN ('Resolved')";
                break;
            case "Cancelled":
            	// Query to select orders by Cancelled
                customQuery = "SELECT orderNumber, orderDate, requiredDate, shippedDate, status, comments, customerNumber FROM Orders WHERE status IN ('Cancelled')";
                break;
            default:
                customQuery = ""; // Handle default case
        }

        try (PreparedStatement pst = dbConnection.getConnection().prepareStatement(customQuery)) {
            ResultSet rs = pst.executeQuery();

            List<OrdersData> OrderDataList = new ArrayList<>();
            while (rs.next()) {
                OrdersData OrderData = new OrdersData(
                        rs.getString("orderNumber"),
                        rs.getString("orderDate"),
                        rs.getString("requiredDate"),
                        rs.getString("shippedDate"),
                        rs.getString("status"),
                        rs.getString("comments"),
                        rs.getString("customerNumber")
                );
                OrderDataList.add(OrderData);
            }

            OrdersTableModel tableModel = new OrdersTableModel(OrderDataList);

            table.setModel(tableModel);  // Update the table model
        } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
	        ex.printStackTrace();
	    }
	}
    
    /**
	 * Opens a new window for adding or updating information.
	 */
    private void openAddOrderWindow() {
        // Create and set up the window.
        final JFrame addOrderFrame = new JFrame("Add New Order");
        addOrderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addOrderFrame.getContentPane().setLayout(new BorderLayout());
        addOrderFrame.setSize(600, 500);
        // Create a panel for the registration section
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Registration", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(10, 43, 378, 345);
        addOrderFrame.getContentPane().add(panel);
        panel.setLayout(null);
     // Create a panel for the search section 
        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(new TitledBorder(null, "Search Order", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        searchPanel.setBounds(10, 405, 460, 60); // Adjust the position and size as needed
        addOrderFrame.getContentPane().add(searchPanel);
        searchPanel.setLayout(null);
        // Create a text field for entering search criteria
        final JTextField txtSearch = new JTextField();
        txtSearch.setBounds(10, 405, 180, 30); // Adjust the position and size as needed
        searchPanel.add(txtSearch);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchOrder(txtSearch.getText(), addOrderFrame); // Implement this method
            }
        });
        btnSearch.setBounds(170, 405, 180, 30); // Adjust the position and size as needed
        searchPanel.add(btnSearch);
     // Creates a label with a font and placement
        JLabel lblorderNumber = new JLabel("Order Number");
        lblorderNumber.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblorderNumber.setBounds(10, 58, 168, 19);
        panel.add(lblorderNumber);

        JLabel lblorderDate = new JLabel("Order Date");
        lblorderDate.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblorderDate.setBounds(10, 88, 168, 20);
        panel.add(lblorderDate);

        JLabel lblrequiredDate = new JLabel("Required Date");
        lblrequiredDate.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblrequiredDate.setBounds(10, 119, 168, 20);
        panel.add(lblrequiredDate);
        
        JLabel lblshippedDate = new JLabel("Shipped Date");
        lblshippedDate.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblshippedDate.setBounds(10, 151, 168, 20);
        panel.add(lblshippedDate);
        
        
        JLabel lblstatus = new JLabel("Status");
        lblstatus.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblstatus.setBounds(10, 182, 168, 20);
        panel.add(lblstatus);
        
        JLabel lblcomments = new JLabel("Comments");
        lblcomments.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblcomments.setBounds(10, 213, 168, 20);
        panel.add(lblcomments);
        
        JLabel lblcustomerNumber = new JLabel("Customer Number");
        lblcustomerNumber.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblcustomerNumber.setBounds(10, 244, 168, 20);
        panel.add(lblcustomerNumber);
        
        // Creates a txtfield with a font and placement
        txtorderNumber = new JTextField();
        txtorderNumber.setBounds(175, 59, 180, 20);
        panel.add(txtorderNumber);
        txtorderNumber.setColumns(10);

        txtorderDate = new JTextField();
        txtorderDate.setColumns(10);
        txtorderDate.setBounds(175, 88, 180, 20);
        panel.add(txtorderDate);

        txtrequiredDate = new JTextField();
        txtrequiredDate.setColumns(10);
        txtrequiredDate.setBounds(175, 119, 180, 20);
        panel.add(txtrequiredDate);
        
        txtshippedDate = new JTextField();
        txtshippedDate.setColumns(10);
        txtshippedDate.setBounds(175, 151, 180, 20);
        panel.add(txtshippedDate);
        
        txtstatus = new JTextField();
        txtstatus.setColumns(10);
        txtstatus.setBounds(175, 182, 180, 20);
        panel.add(txtstatus);
        
        txtcomments = new JTextField();
        txtcomments.setColumns(10);
        txtcomments.setBounds(175, 213, 180, 20);
        DocumentFilter filter = new DocumentFilter() {
            private int maxCharacters = 200;
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() + text.length() - length) <= maxCharacters) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                    // Display a message when the character limit is reached
                    JOptionPane.showMessageDialog(null, "Maximum character limit (200) reached for comments field.", "Character Limit", JOptionPane.WARNING_MESSAGE);
                }
            }
        };

        ((AbstractDocument) txtcomments.getDocument()).setDocumentFilter(filter);
        panel.add(txtcomments);
        
        txtcustomerNumber = new JTextField();
        txtcustomerNumber.setColumns(10);
        txtcustomerNumber.setBounds(175, 244, 180, 20);
        panel.add(txtcustomerNumber);

        JButton btnSave = new JButton("Save");
        btnSave.setBounds(10, 304, 180, 30);
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveOrders(); // Make sure this method is for saving new Order records
            }
        });
        panel.add(btnSave);
        
        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(200, 304, 180, 30);
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateOrders(); // Make sure this method is for updating existing Order records
            }
        });
        panel.add(btnUpdate);
        
        // Display the window.
        addOrderFrame.setVisible(true);
    }
    /**
     * Method to search for an order based on the order number entered in the search field.
     * It performs a database search and updates the table to show the matching orders, if found.
     *
     * @param orderNumber The order number to search for in the database.
     */
    private void searchOrder(String orderNumber, JFrame frame) {
        try {
            // Prepare a SQL query to select order information based on the provided orderNumber
            PreparedStatement pst = dbConnection.getConnection().prepareStatement(
                "SELECT orderNumber, orderDate, requiredDate, shippedDate, status, comments, customerNumber FROM Orders WHERE orderNumber = ?");
            pst.setString(1, orderNumber); // Set the parameter for the query to the provided orderNumber
            
            ResultSet rs = pst.executeQuery(); // Execute the query and retrieve the result set
            
            if (rs.next()) { // Check if a matching order was found in the result set
                // Retrieve the order information from the result set
                String foundorderNumber = rs.getString(1);
                String foundorderDate = rs.getString(2);
                String foundrequiredDate = rs.getString(3);
                String foundshippedDate = rs.getString(4);
                String foundstatus = rs.getString(5);
                String foundcomments = rs.getString(6);
                String foundcustomerNumber = rs.getString(7);

                // Set the retrieved order information in text fields or components on the GUI
                txtorderNumber.setText(foundorderNumber);
                txtorderDate.setText(foundorderDate);
                txtrequiredDate.setText(foundrequiredDate);
                txtshippedDate.setText(foundshippedDate);
                txtstatus.setText(foundstatus);
                txtcomments.setText(foundcomments);
                txtcustomerNumber.setText(foundcustomerNumber);

            } else {
                // If no matching order was found, clear the text fields or components
                txtorderNumber.setText("");
                txtorderDate.setText("");
                txtrequiredDate.setText("");
                txtshippedDate.setText("");
                txtstatus.setText("");
                txtcomments.setText("");
                txtcustomerNumber.setText("");
            }
        } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
	        ex.printStackTrace();
	    }
	}
    /**
     * Method to save order data entered in the text fields to the database.
     * It captures data from UI components and inserts a new order record into the database.
     */
    public void saveOrders() {
    	try {
        // Create a map to store column names and their corresponding values for the new order
        Map<String, Object> columnValues = new HashMap<>();
        columnValues.put("orderNumber", txtorderNumber.getText());
        columnValues.put("orderDate", txtorderDate.getText());
        columnValues.put("requiredDate", txtrequiredDate.getText());
        columnValues.put("shippedDate", txtshippedDate.getText());
        columnValues.put("status", txtstatus.getText());
        columnValues.put("comments", txtcomments.getText());
        columnValues.put("customerNumber", txtcustomerNumber.getText());
        
        // Call the insert method of your CRUD operations class to insert a new order record
        crudOperations.insert("Orders", columnValues);
        
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
     * Method to update an existing order data in the database.
     * It captures updated data from UI components and applies changes to the corresponding order record.
     */
    public void updateOrders() {
    	try {
        // Create a map to store updated column values for the order
        Map<String, Object> updatedValues = new HashMap<>();
        updatedValues.put("orderDate", txtorderDate.getText());
        updatedValues.put("requiredDate", txtrequiredDate.getText());
        updatedValues.put("shippedDate", txtshippedDate.getText());
        updatedValues.put("status", txtstatus.getText());
        updatedValues.put("comments", txtcomments.getText());
        updatedValues.put("customerNumber", txtcustomerNumber.getText());
        
        // Define a where clause to identify the order to be updated (in this case, by orderNumber)
        String whereClause = "orderNumber = ?";
        Object[] whereArgs = { txtorderNumber.getText() }; // Use the order number from the text field as the argument
        
        // Call the update method of your CRUD operations class to update the order record
        crudOperations.update("Orders", updatedValues, whereClause, whereArgs);
        
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
        // Clear all input fields and set the focus back to txtorderNumber
        txtorderNumber.setText("");
        txtorderDate.setText("");
        txtrequiredDate.setText("");
        txtshippedDate.setText("");
        txtstatus.setText("");
        txtcomments.setText("");
        txtcustomerNumber.setText("");
        txtorderNumber.requestFocus();
    }
    /**
     * Method to delete an order record from the database.
     * It uses the order number from the search field to identify and delete the order record.
     */
    public void deleteOrders() {
        // Check if the txtsearchorderNumber field is not null and not empty
        if (txtsearchorderNumber != null && !txtsearchorderNumber.getText().isEmpty()) {
            String whereClause = "orderNumber = ?";
            Object[] whereArgs = { txtsearchorderNumber.getText() };

            // Call the delete method of your CRUD operations class to delete the order record
            crudOperations.delete("Orders", whereClause, whereArgs);

            // Reload the table data to reflect the changes
            tableLoad();

            // Clear the input fields after deletion
            clearFields();
        } else {
            // Optionally, show an error message if the field is null or empty
            JOptionPane.showMessageDialog(frame, "Order number is required for deletion.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Method to import order data from a CSV file.
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
                String sql = "INSERT INTO Orders (orderNumber, orderDate, requiredDate, shippedDate, status, comments, customerNumber) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = dbConnection.getConnection().prepareStatement(sql);

                while ((line = br.readLine()) != null) {
                    String[] values = line.split(","); // Check if your CSV uses a different delimiter
                    statement.setString(1, values[0]); // orderNumber
                    statement.setString(2, values[1]); // orderDate
                    statement.setString(3, values[2]); // requiredDate
                    statement.setString(4, values[3]); // shippedDate
                    statement.setString(5, values[4]); // status
                    statement.setString(6, values[5]); // comments
                    statement.setString(7, values[6]); // customerNumber
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