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

public class offices extends ApplicationMenu{
	private static final long serialVersionUID = 1L;
    private JFrame frame;
    private JTable table;
    private JTextField txtsearchofficeCode;
    private DatabaseConnection dbConnection;
    private CRUD crudOperations;
    //
    // Various text fields for data input
    private JTextField txtofficeCode;
    private JTextField txtcity;
    private JTextField txtphone;
    private JTextField txtaddressLine1;
    private JTextField txtaddressLine2;
    private JTextField txtstate;
    private JTextField txtcountry;
    private JTextField txtPostalCode;
    private JTextField txtterritory;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    offices window = new offices();
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
    public offices() {
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
            PreparedStatement pst = dbConnection.getConnection().prepareStatement("select * from offices");
            ResultSet rs = pst.executeQuery();
         // Create a list to hold data objects
            List<officeData> officeDataList = new ArrayList<>();
            while (rs.next()) {
            	// Retrieve data from the ResultSet and add it to the list
                officeData officeData = new officeData(
                        rs.getString("officeCode"),
                        rs.getString("city"),
                        rs.getString("phone"),
                        rs.getString("addressLine1"),
                        rs.getString("addressLine2"),
                        rs.getString("state"),
                        rs.getString("country"),
                        rs.getString("PostalCode"),
                        rs.getString("territory")
                );
                officeDataList.add(officeData);
            }
         // Set the custom table model with the fetched data
            officeTableModel tableModel = new officeTableModel(officeDataList);
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
    	String[] queryOptions = {"All offices", "Europe", "America", "Oceania", "Asia" }; // Defines the options for the query ComboBox
    	// Initialize the query ComboBox with the defined options, Set position and size, Add ComboBox to the frame
    	final JComboBox<String> queryComboBox = new JComboBox<>(queryOptions);
    	queryComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"All offices", "Europe", "America", "Oceania", "Asia"}));
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
        JLabel lbloffices = new JLabel("Offices");
        lbloffices.setFont(new Font("Tahoma", Font.BOLD, 30));
        lbloffices.setHorizontalAlignment(SwingConstants.CENTER);
        lbloffices.setBounds(331, -2, 301, 44);
        frame.getContentPane().add(lbloffices);
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
        
        txtsearchofficeCode = new JTextField();
        txtsearchofficeCode.setColumns(10);
        txtsearchofficeCode.setBounds(174, 30, 180, 20);
        panel_1.add(txtsearchofficeCode);
        
        txtsearchofficeCode.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchoffice(); // Call search method on key release
            }
        });

        JLabel lblsearchofficeCode = new JLabel("Office Code");
        lblsearchofficeCode.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblsearchofficeCode.setBounds(9, 31, 154, 14);
        panel_1.add(lblsearchofficeCode);
     // Initialize and configure the 'Delete' button
        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteoffice(); // Call deleteOffice method when clicked
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
        		openAddofficeWindow();
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
    private void searchoffice() {
        try {
            String officeCode = txtsearchofficeCode.getText();
            String query = "SELECT officeCode, city, phone, addressLine1, addressLine2, state, country, postalCode, territory FROM offices";
            
            if (officeCode != null && !officeCode.isEmpty()) {
                query += " WHERE officeCode = ?";
            }

            try (PreparedStatement pst = dbConnection.getConnection().prepareStatement(query)) {
                if (officeCode != null && !officeCode.isEmpty()) {
                    pst.setString(1, officeCode);
                }

                ResultSet rs = pst.executeQuery();

                List<officeData> officeDataList = new ArrayList<>();
                while (rs.next()) {
                    officeData officeData = new officeData(
                            rs.getString("officeCode"),
                            rs.getString("city"),
                            rs.getString("phone"),
                            rs.getString("addressLine1"),
                            rs.getString("addressLine2"),
                            rs.getString("state"),
                            rs.getString("country"),
                            rs.getString("PostalCode"),
                            rs.getString("territory")
                    );
                    officeDataList.add(officeData);
                }

                officeTableModel tableModel = new officeTableModel(officeDataList);
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
            case "All offices":
                customQuery = "SELECT officeCode, city, phone, addressLine1, addressLine2, state, country, postalCode, territory FROM offices";
                break;
            case "Europe":
                customQuery = "SELECT officeCode, city, phone, addressLine1, addressLine2, state, country, postalCode, territory FROM offices WHERE country IN ('Denmark','Norway','Sweden','France','Germany','Polen','Spain','Portugal','Finland','UK','Ireland','Italy','Switzerland','Netherlands','Belgium','Austria','Russia')";
                break;
            case "America":
                customQuery = "SELECT officeCode, city, phone, addressLine1, addressLine2, state, country, postalCode, territory FROM offices WHERE country = ('USA')";
                break;
            case "Oceania":
                customQuery = "SELECT officeCode, city, phone, addressLine1, addressLine2, state, country, postalCode, territory FROM offices WHERE country IN ('New Zealand', 'Australia')";
                break;
            case "Asia":
                customQuery = "SELECT officeCode, city, phone, addressLine1, addressLine2, state, country, postalCode, territory FROM offices WHERE country IN ('Singapore','Japan', 'HongKong','Isreal','Philippines')";
                break;
            default:
                customQuery = ""; // Handle default case
        }

        try (PreparedStatement pst = dbConnection.getConnection().prepareStatement(customQuery)) {
            ResultSet rs = pst.executeQuery();
            List<officeData> officeDataList = new ArrayList<>();
            while (rs.next()) {
                officeData officeData = new officeData(
                        rs.getString("officeCode"),
                        rs.getString("city"),
                        rs.getString("phone"),
                        rs.getString("addressLine1"),
                        rs.getString("addressLine2"),
                        rs.getString("state"),
                        rs.getString("country"),
                        rs.getString("PostalCode"),
                        rs.getString("territory")
                );
                officeDataList.add(officeData);
            }

            officeTableModel tableModel = new officeTableModel(officeDataList);
            table.setModel(tableModel);  // Update the table model
        } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
	        ex.printStackTrace();
	    }
	}
    /**
	 * Opens a new window for adding or updating information.
	 */
    private void openAddofficeWindow() {
        // Create and set up the window.
        final JFrame addofficeFrame = new JFrame("Add New office");
        addofficeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addofficeFrame.getContentPane().setLayout(new BorderLayout());
        addofficeFrame.setSize(700, 700);
        // Create a panel for the registration section
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Registration", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(20, 40, 504, 359);
        panel.setLayout(null);
        addofficeFrame.getContentPane().add(panel);
     // Create a panel for the search section 
        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(new TitledBorder(null, "Search offices", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        searchPanel.setBounds(20, 372, 404, 62);
        searchPanel.setLayout(null);
        addofficeFrame.getContentPane().add(searchPanel);

        // Create a text field for entering search criteria
        final JTextField txtSearch = new JTextField();
        txtSearch.setBounds(20, 405, 180, 30); 
        searchPanel.add(txtSearch);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchoffice(txtSearch.getText(), addofficeFrame); // Implement this method
            }
        });
        btnSearch.setBounds(190, 405, 180, 30); 
        searchPanel.add(btnSearch);
     
        JLabel lblofficeCode = new JLabel("office Code");
        lblofficeCode.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblofficeCode.setBounds(10, 24, 168, 19);
        panel.add(lblofficeCode);

        JLabel lblcity = new JLabel("city");
        lblcity.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblcity.setBounds(10, 54, 168, 20);
        panel.add(lblcity);

        JLabel lblphone = new JLabel("phone");
        lblphone.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblphone.setBounds(10, 85, 168, 20);
        panel.add(lblphone);
        
        JLabel lbladdressLine1 = new JLabel("AddressLine1");
        lbladdressLine1.setFont(new Font("Tahoma", Font.BOLD, 14));
        lbladdressLine1.setBounds(10, 117, 168, 20);
        panel.add(lbladdressLine1);
        
        
        JLabel lbladdressLine2 = new JLabel("AddressLine2");
        lbladdressLine2.setFont(new Font("Tahoma", Font.BOLD, 14));
        lbladdressLine2.setBounds(10, 148, 168, 20);
        panel.add(lbladdressLine2);
        
        JLabel lblstate = new JLabel("State");
        lblstate.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblstate.setBounds(10, 179, 168, 20);
        panel.add(lblstate);
        
        JLabel lblcountry = new JLabel("Country");
        lblcountry.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblcountry.setBounds(10, 210, 168, 20);
        panel.add(lblcountry);
        
        JLabel lblPostalCode = new JLabel("PostalCode");
        lblPostalCode.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblPostalCode.setBounds(10, 239, 168, 20);
        panel.add(lblPostalCode);
        
        JLabel lblterritory = new JLabel("territory");
        lblterritory.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblterritory.setBounds(10, 270, 168, 20);
        panel.add(lblterritory);
        // Creates a txtfield with a font and placement
        txtPostalCode = new JTextField();
        txtPostalCode.setColumns(10);
        txtPostalCode.setBounds(214, 238, 180, 20);
        panel.add(txtPostalCode);
        
        txtofficeCode = new JTextField();
        txtofficeCode.setBounds(214, 24, 180, 20);
        panel.add(txtofficeCode);
        txtofficeCode.setColumns(10);

        txtcity = new JTextField();
        txtcity.setColumns(10);
        txtcity.setBounds(214, 53, 180, 20);
        panel.add(txtcity);

        txtphone = new JTextField();
        txtphone.setColumns(10);
        txtphone.setBounds(214, 84, 180, 20);
        panel.add(txtphone);
        
        txtaddressLine1 = new JTextField();
        txtaddressLine1.setColumns(10);
        txtaddressLine1.setBounds(214, 116, 180, 20);
        panel.add(txtaddressLine1);
        
        txtaddressLine2 = new JTextField();
        txtaddressLine2.setColumns(10);
        txtaddressLine2.setBounds(214, 147, 180, 20);
        panel.add(txtaddressLine2);
        
        txtstate = new JTextField();
        txtstate.setColumns(10);
        txtstate.setBounds(214, 178, 180, 20);
        panel.add(txtstate);
        
        txtcountry = new JTextField();
        txtcountry.setColumns(10);
        txtcountry.setBounds(214, 209, 180, 20);
        panel.add(txtcountry);
        
        txtterritory = new JTextField();
        txtterritory.setColumns(10);
        txtterritory.setBounds(214, 269, 180, 20);
        panel.add(txtterritory);

        JButton btnSave = new JButton("Save");
        btnSave.setBounds(10, 300, 180, 30);
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (validateOfficeCode()) {
                    saveoffice(); // Make sure this method is for saving new office records
                    addofficeFrame.dispose(); // Close the window after saving
                } else {
                    JOptionPane.showMessageDialog(addofficeFrame, "Please enter a valid office code.");
                }
            }
        });
        panel.add(btnSave);
        
     // Creates an "Update" button with ActionListener to validate and update office records
        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(200, 300, 180, 30);
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (validateOfficeCode()) {
                    updateoffice(); // Make sure this method is for updating existing office records
                    addofficeFrame.dispose(); // Close the window after updating
                } else {
                    JOptionPane.showMessageDialog(addofficeFrame, "Please enter a valid office code.");
                }
            }
        });
        panel.add(btnUpdate);

        // Display the window.
        addofficeFrame.setVisible(true);
    }
    private boolean validateOfficeCode() {
        String officeCode = txtofficeCode.getText();
        // You can add more validation logic if needed
        return officeCode != null && !officeCode.trim().isEmpty();
    }
    /**
     * Method to search for an office based on the office code entered in the search field.
     * It performs a database search and updates the table to show the matching offices, if found.
     *
     * @param officeCode The office code to search for in the database.
     */
    private void searchoffice(String officeCode, JFrame frame) {
        try {
            // Prepare a SQL query to select office information based on the provided officeCode
            PreparedStatement pst = dbConnection.getConnection().prepareStatement(
                "SELECT officeCode, city, phone, addressLine1, addressLine2, state, country, PostalCode, territory FROM offices WHERE officeCode = ?");
            pst.setString(1, officeCode); // Set the parameter for the query to the provided officeCode
            
            ResultSet rs = pst.executeQuery(); // Execute the query and retrieve the result set
            
            if (rs.next()) { // Check if a matching office was found in the result set
                // Retrieve the office information from the result set
                String foundofficeCode = rs.getString(1);
                String foundcity = rs.getString(2);
                String foundphone = rs.getString(3);
                String foundaddressLine1 = rs.getString(4);
                String foundaddressLine2 = rs.getString(5);
                String foundstate = rs.getString(6);
                String foundcountry = rs.getString(7);
                String foundPostalCode = rs.getString(8);
                String foundterritory = rs.getString(9);

                // Set the retrieved office information in text fields or components on the GUI
                txtofficeCode.setText(foundofficeCode);
                txtcity.setText(foundcity);
                txtphone.setText(foundphone);
                txtaddressLine1.setText(foundaddressLine1);
                txtaddressLine2.setText(foundaddressLine2);
                txtstate.setText(foundstate);
                txtcountry.setText(foundcountry);
                txtPostalCode.setText(foundPostalCode);
                txtterritory.setText(foundterritory);

            } else {
                // If no matching office was found, clear the text fields or components
                txtofficeCode.setText("");
                txtcity.setText("");
                txtphone.setText("");
                txtaddressLine1.setText("");
                txtaddressLine2.setText("");
                txtstate.setText("");
                txtcountry.setText("");
                txtPostalCode.setText("");
                txtterritory.setText("");
            }
        } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
	        ex.printStackTrace();
	    }
	}

    /**
     * Method to save office data entered in the text fields to the database.
     * It captures data from UI components and inserts a new office record into the database.
     */
    public void saveoffice() {
    	try {
        // Create a map to store column names and their corresponding values for the new office
        Map<String, Object> columnValues = new HashMap<>();
        columnValues.put("officeCode", txtofficeCode.getText());
        columnValues.put("city", txtcity.getText());
        columnValues.put("phone", txtphone.getText());
        columnValues.put("addressLine1", txtaddressLine1.getText());
        columnValues.put("addressLine2", txtaddressLine2.getText());
        columnValues.put("state", txtstate.getText());
        columnValues.put("country", txtcountry.getText());
        columnValues.put("PostalCode", txtPostalCode.getText());
        columnValues.put("territory", txtterritory.getText());
        
        // Call the insert method of your CRUD operations class to insert a new office record
        crudOperations.insert("offices", columnValues);
        
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
     * Method to update an existing office data in the database.
     * It captures updated data from UI components and applies changes to the corresponding office record.
     */
    public void updateoffice() {
    	try {
        // Create a map to store updated column values for the office
        Map<String, Object> updatedValues = new HashMap<>();
        updatedValues.put("city", txtcity.getText());
        updatedValues.put("phone", txtphone.getText());
        updatedValues.put("addressLine1", txtaddressLine1.getText());
        updatedValues.put("addressLine2", txtaddressLine2.getText());
        updatedValues.put("state", txtstate.getText());
        updatedValues.put("country", txtcountry.getText());
        updatedValues.put("PostalCode", txtPostalCode.getText());
        updatedValues.put("territory", txtterritory.getText());
        
        // Define a where clause to identify the office to be updated (in this case, by officeCode)
        String whereClause = "officeCode = ?";
        Object[] whereArgs = { txtofficeCode.getText() }; // Use the office code from the text field as the argument
        
        // Call the update method of your CRUD operations class to update the office record
        crudOperations.update("offices", updatedValues, whereClause, whereArgs);
        
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
        // Clear all input fields and set the focus back to txtofficeCode
        txtofficeCode.setText("");
        txtcity.setText("");
        txtphone.setText("");
        txtaddressLine1.setText("");
        txtaddressLine2.setText("");
        txtstate.setText("");
        txtcountry.setText("");
        txtPostalCode.setText("");
        txtterritory.setText("");
        txtofficeCode.requestFocus();
    }

    /**
     * Method to delete an office record from the database.
     * It uses the office code from the search field to identify and delete the office record.
     */
    public void deleteoffice() {
        // Check if the txtsearchofficenumber field is not null and not empty
        if (txtsearchofficeCode != null && !txtsearchofficeCode.getText().isEmpty()) {
            String whereClause = "officeCode = ?";
            Object[] whereArgs = { txtsearchofficeCode.getText() };

            crudOperations.delete("offices", whereClause, whereArgs);
            tableLoad();
            clearFields();
        } else {
            // Optionally, show an error message if the field is null or empty
            JOptionPane.showMessageDialog(frame, "Office code is required for deletion.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Method to import office data from a CSV file.
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
                // Modify the query to match the office table's columns
                String sql = "INSERT INTO offices (officeCode, city, phone, addressLine1, addressLine2, state, country, PostalCode, territory) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = dbConnection.getConnection().prepareStatement(sql);

                while ((line = br.readLine()) != null) {
                    String[] values = line.split(","); // Check if your CSV uses a different delimiter
                    // Assign values from the CSV to the SQL query placeholders
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
                tableLoad(); // Refresh your table view
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }


}