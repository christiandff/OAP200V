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
import javax.swing.JTextField;
import javax.swing.JButton;
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

/**
 * @author Christian Douglas Farnes Fancy
 * @author Even Hjerpseth Unneberg
 */


public class OrderDetails extends ApplicationMenu{
	private static final long serialVersionUID = 1L;
    private JFrame frame;
    private JTable table;
    private JTextField txtsearchorderNumber;
    private DatabaseConnection dbConnection;
    private CRUD crudOperations;
    //
    // Various text fields for data input
    private JTextField txtorderNumber;
    private JTextField txtproductCode;
    private JTextField txtquantityOrdered;
    private JTextField txtpriceEach;
    private JTextField txtorderLineNumber;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    OrderDetails window = new OrderDetails();
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
    public OrderDetails() {
    	super();
        initialize();
        dbConnection = new DatabaseConnection();
        crudOperations = new CRUD(dbConnection);
        tableLoad();
        frame.setLocationRelativeTo(null);
    }

    /**
     * Load data into the table.
     */
    public void tableLoad() {
        try {
        	// Prepare and execute a query to fetch all data
            PreparedStatement pst = dbConnection.getConnection().prepareStatement("select * from orderDetails");
            ResultSet rs = pst.executeQuery();
         // Create a list to hold data objects
            List<orderdetailsData> orderdetailsDataList = new ArrayList<>();
            while (rs.next()) {
            	// Retrieve data from the ResultSet and add it to the list
                orderdetailsData orderdetailsData = new orderdetailsData(
                        rs.getString("orderNumber"),
                        rs.getString("productCode"),
                        rs.getString("quantityOrdered"),
                        rs.getString("priceEach"),
                        rs.getString("orderLineNumber")
                );
                orderdetailsDataList.add(orderdetailsData);
            }
            orderdetailsTableModel tableModel = new orderdetailsTableModel(orderdetailsDataList);
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

        // Initialize and configure the label for details
        JLabel lblorderDetails = new JLabel("Order Details");
        lblorderDetails.setFont(new Font("Tahoma", Font.BOLD, 30));
        lblorderDetails.setHorizontalAlignment(SwingConstants.CENTER);
        lblorderDetails.setBounds(331, -2, 301, 44);
        frame.getContentPane().add(lblorderDetails);
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

        
        JLabel lblsearchorderNumber = new JLabel("Order Number");
        lblsearchorderNumber.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblsearchorderNumber.setBounds(9, 31, 154, 14);
        panel_1.add(lblsearchorderNumber);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteProduct(); // Call deleteProduct method when clicked
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
        		openAddorderdetailsWindow();
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
            String query = "SELECT orderNumber, productCode, quantityOrdered, priceEach, orderLineNumber FROM orderDetails";
            
            if (!orderNumber.isEmpty()) {
                query += " WHERE orderNumber = ?";
            }

            try (PreparedStatement pst = dbConnection.getConnection().prepareStatement(query)) {
                if (!orderNumber.isEmpty()) {
                    pst.setString(1, orderNumber);
                }

                ResultSet rs = pst.executeQuery();

                List<orderdetailsData> orderdetailsDataList = new ArrayList<>();
                while (rs.next()) {
                    orderdetailsData orderdetailsData = new orderdetailsData(
                            rs.getString("orderNumber"),
                            rs.getString("productCode"),
                            rs.getString("quantityOrdered"),
                            rs.getString("priceEach"),
                            rs.getString("orderLineNumber")
                    );
                    orderdetailsDataList.add(orderdetailsData);
                }

                orderdetailsTableModel tableModel = new orderdetailsTableModel(orderdetailsDataList);
                table.setModel(tableModel);
            }
        } catch (SQLException e) {
	        // Handle SQL exception in a user-friendly way
	        JOptionPane.showMessageDialog(frame, "Error searching product: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
	        e.printStackTrace();
	    }
	}
     
    /**
	 * Opens a new window for adding or updating information.
	 */
    private void openAddorderdetailsWindow() {
        // Create and set up the window.
        final JFrame addorderdetailsFrame = new JFrame("Add New orderdetails");
        addorderdetailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addorderdetailsFrame.getContentPane().setLayout(new BorderLayout());
        addorderdetailsFrame.setSize(600, 500);
     // Create a panel for the registration section
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Registration", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(10, 43, 378, 345);
        addorderdetailsFrame.getContentPane().add(panel);
        panel.setLayout(null);
     // Create a panel for the search section 
        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(new TitledBorder(null, "Search orderdetails", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        searchPanel.setBounds(10, 405, 460, 60); // Adjust the position and size as needed
        addorderdetailsFrame.getContentPane().add(searchPanel);
        searchPanel.setLayout(null);
        // Create a text field for entering search criteria
        final JTextField txtSearch = new JTextField();
        txtSearch.setBounds(10, 405, 180, 30); // Adjust the position and size as needed
        searchPanel.add(txtSearch);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchorderdetails(txtSearch.getText(), addorderdetailsFrame); // Implement this method
            }
        });
        btnSearch.setBounds(170, 405, 180, 30); // Adjust the position and size as needed
        searchPanel.add(btnSearch);
     // Creates a label with a font and placement
        JLabel lblorderNumber = new JLabel("orderdetails Number");
        lblorderNumber.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblorderNumber.setBounds(10, 58, 168, 19);
        panel.add(lblorderNumber);

        JLabel lblproductCode = new JLabel("productCode");
        lblproductCode.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblproductCode.setBounds(10, 88, 168, 20);
        panel.add(lblproductCode);

        JLabel lblquantityOrdered = new JLabel("quantityOrdered");
        lblquantityOrdered.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblquantityOrdered.setBounds(10, 119, 168, 20);
        panel.add(lblquantityOrdered);
        
        JLabel lblpriceEach = new JLabel("priceEach");
        lblpriceEach.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblpriceEach.setBounds(10, 151, 168, 20);
        panel.add(lblpriceEach);
        
        
        JLabel lblorderLineNumber = new JLabel("orderLineNumber");
        lblorderLineNumber.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblorderLineNumber.setBounds(10, 182, 168, 20);
        panel.add(lblorderLineNumber);
        
        // Creates a txtfield with a font and placement
        txtorderNumber = new JTextField();
        txtorderNumber.setBounds(175, 59, 180, 20);
        panel.add(txtorderNumber);
        txtorderNumber.setColumns(10);

        txtproductCode = new JTextField();
        txtproductCode.setColumns(10);
        txtproductCode.setBounds(175, 88, 180, 20);
        panel.add(txtproductCode);

        txtquantityOrdered = new JTextField();
        txtquantityOrdered.setColumns(10);
        txtquantityOrdered.setBounds(175, 119, 180, 20);
        panel.add(txtquantityOrdered);
        
        txtpriceEach = new JTextField();
        txtpriceEach.setColumns(10);
        txtpriceEach.setBounds(175, 151, 180, 20);
        panel.add(txtpriceEach);
        
        txtorderLineNumber = new JTextField();
        txtorderLineNumber.setColumns(10);
        txtorderLineNumber.setBounds(175, 182, 180, 20);
        panel.add(txtorderLineNumber);
        

        JButton btnSave = new JButton("Save");
        btnSave.setBounds(10, 304, 180, 30);
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveProduct(); // Make sure this method is for saving new orderdetails records
            }
        });
        panel.add(btnSave);
        
        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(200, 304, 180, 30);
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateProduct(); // Make sure this method is for updating existing orderdetails records
            }
        });
        panel.add(btnUpdate);
        
        // Display the window.
        addorderdetailsFrame.setVisible(true);
    }
    /**
     * Method to search for an orderdetails based on the order number entered in the search field.
     * It performs a database search and updates the table to show the matching orders, if found.
     *
     * @param orderNumber The order  number to search for in the database.
     */
    private void searchorderdetails(String orderNumber, JFrame frame) {
        try {
            // Prepare a SQL query to select order details based on the provided orderNumber
            PreparedStatement pst = dbConnection.getConnection().prepareStatement(
                "SELECT orderNumber, productCode, quantityOrdered, priceEach, orderLineNumber FROM orderDetails WHERE orderNumber = ?");
            pst.setString(1, orderNumber); // Set the parameter for the query to the provided orderNumber
            
            ResultSet rs = pst.executeQuery(); // Execute the query and retrieve the result set
            
            if (rs.next()) { // Check if matching order details were found in the result set
                // Retrieve the order details from the result set
                String foundorderNumber = rs.getString(1);
                String foundproductCode = rs.getString(2);
                String foundquantityOrdered = rs.getString(3);
                String foundpriceEach = rs.getString(4);
                String foundorderLineNumber = rs.getString(5);

                // Set the retrieved order details in text fields or components on the GUI
                txtorderNumber.setText(foundorderNumber);
                txtproductCode.setText(foundproductCode);
                txtquantityOrdered.setText(foundquantityOrdered);
                txtpriceEach.setText(foundpriceEach);
                txtorderLineNumber.setText(foundorderLineNumber);

            } else {
                // If no matching order details were found, clear the text fields or components
                txtorderNumber.setText("");
                txtproductCode.setText("");
                txtquantityOrdered.setText("");
                txtpriceEach.setText("");
                txtorderLineNumber.setText("");
            }
        } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
	        ex.printStackTrace();
	    }
	}
    /**
     * Method to save order data entered in the text fields to the database.
     * It captures data from UI components and inserts a new orderdetail record into the database.
     */
    public void saveProduct() {
    	try {
        // Create a map to store column names and their corresponding values for the new order details
        Map<String, Object> columnValues = new HashMap<>();
        columnValues.put("orderNumber", txtorderNumber.getText());
        columnValues.put("productCode", txtproductCode.getText());
        columnValues.put("quantityOrdered", txtquantityOrdered.getText());
        columnValues.put("priceEach", txtpriceEach.getText());
        columnValues.put("orderLineNumber", txtorderLineNumber.getText());
        
        // Call the insert method of your CRUD operations class to insert new order details
        crudOperations.insert("orderDetails", columnValues);
        
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
     * Method to update an existing orderdetail data in the database.
     * It captures updated data from UI components and applies changes to the corresponding orderdetail record.
     */
    public void updateProduct() {
    	try {
        // Create a map to store updated column values for the order details
        Map<String, Object> updatedValues = new HashMap<>();
        updatedValues.put("productCode", txtproductCode.getText());
        updatedValues.put("quantityOrdered", txtquantityOrdered.getText());
        updatedValues.put("priceEach", txtpriceEach.getText());
        updatedValues.put("orderLineNumber", txtorderLineNumber.getText());
        
        // Define a where clause to identify the order details to be updated (in this case, by orderNumber)
        String whereClause = "orderNumber = ?";
        Object[] whereArgs = { txtorderNumber.getText() }; // Use the order number from the text field as the argument
        
        // Call the update method of your CRUD operations class to update the order details
        crudOperations.update("orderDetails", updatedValues, whereClause, whereArgs);
        
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
        txtproductCode.setText("");
        txtquantityOrdered.setText("");
        txtpriceEach.setText("");
        txtorderLineNumber.setText("");
        txtorderNumber.requestFocus();
    }
    /**
     * Method to delete an orderdetail record from the database.
     * It uses the order number from the search field to identify and delete the orderdetail record.
     */
    public void deleteProduct() {
        // Check if the txtsearchorderNumber field is not null and not empty
        if (txtsearchorderNumber != null && !txtsearchorderNumber.getText().isEmpty()) {
            String whereClause = "orderNumber = ?";
            Object[] whereArgs = { txtsearchorderNumber.getText() };

            // Call the delete method of your CRUD operations class to delete the order details record
            crudOperations.delete("orderDetails", whereClause, whereArgs);

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
     * Method to import orderdetail data from a CSV file.
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
                String sql = "INSERT INTO orderDetails (orderNumber, productCode, quantityOrdered, priceEach, orderLineNumber) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement statement = dbConnection.getConnection().prepareStatement(sql);

                while ((line = br.readLine()) != null) {
                    String[] values = line.split(","); // Check if your CSV uses a different delimiter
                    statement.setString(1, values[0]); // orderNumber
                    statement.setString(2, values[1]); // productCode
                    statement.setString(3, values[2]); // quantityOrdered
                    statement.setString(4, values[3]); // priceEach
                    statement.setString(5, values[4]); // orderLineNumber
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