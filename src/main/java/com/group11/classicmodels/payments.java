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

public class payments extends ApplicationMenu {
	private static final long serialVersionUID = 1L;
    private JFrame frame;
    private JTable table;
    private JTextField txtsearchcheckNumber;
    private DatabaseConnection dbConnection;
    private CRUD crudOperations;
    //
    // Various text fields for data input
    private JTextField txtcustomerNumber;
    private JTextField txtcheckNumber;
    private JTextField txtpaymentDate;
    private JTextField txtamount;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    payments window = new payments();
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
    public payments() {
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
            PreparedStatement pst = dbConnection.getConnection().prepareStatement("select * from payments");
            ResultSet rs = pst.executeQuery();
         // Create a list to hold data objects
            List<paymentsData> paymentsDataList = new ArrayList<>();
            while (rs.next()) {
            	// Retrieve data from the ResultSet and add it to the list
                paymentsData paymentsData = new paymentsData(
                        rs.getString("customerNumber"),
                        rs.getString("checkNumber"),
                        rs.getString("paymentDate"),
                        rs.getString("amount")
                );
                paymentsDataList.add(paymentsData);
            }
         // Set the custom table model with the fetched data
            paymentsTableModel tableModel = new paymentsTableModel(paymentsDataList);
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
    	String[] queryOptions = {"All payments", "2003", "2004", "2005"};        // Define the options for the query ComboBox
    	// Initialize the query ComboBox with the defined options, Set position and size, Add ComboBox to the frame
    	final JComboBox<String> queryComboBox = new JComboBox<>(queryOptions);
    	queryComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"All payments", "2003", "2004", "2005"}));
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
        JLabel lblpayments = new JLabel("Payments");
        lblpayments.setFont(new Font("Tahoma", Font.BOLD, 30));
        lblpayments.setHorizontalAlignment(SwingConstants.CENTER);
        lblpayments.setBounds(331, -2, 301, 44);
        frame.getContentPane().add(lblpayments);
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
        
        txtsearchcheckNumber = new JTextField();
        txtsearchcheckNumber.setColumns(10);
        txtsearchcheckNumber.setBounds(174, 30, 180, 20);
        panel_1.add(txtsearchcheckNumber);
        
        txtsearchcheckNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchpayments(); // Call search method on key release
            }
        });


        JLabel lblsearchchecknumber = new JLabel("Check number");
        lblsearchchecknumber.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblsearchchecknumber.setBounds(9, 31, 154, 14);
        panel_1.add(lblsearchchecknumber);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deletepayments(); // Call deleteProduct method when clicked
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
        		openAddpaymentsWindow();
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
   	 * Executes a query based on the selected category.
   	 * @param selectedQuery The selected category for the query.
   	 */
    private void executeSelectedQuery(String selectedQuery) {
        String customQuery;
     // Switch statement to handle different region/category selections
        switch (selectedQuery) {
            case "All payments":
                customQuery = "SELECT customerNumber, checkNumber, paymentDate, amount FROM payments WHERE YEAR(paymentDate) IN (2003, 2004, 2005)";
                break;
            case "2003":
            case "2004":
            case "2005":
                customQuery = "SELECT customerNumber, checkNumber, paymentDate, amount FROM payments " +
                              "WHERE YEAR(paymentDate) = ?";
                break;
            default:
                customQuery = ""; // Handle default case
        }

        try (PreparedStatement pst = dbConnection.getConnection().prepareStatement(customQuery)) {
            if (!selectedQuery.equals("All payments")) {
                pst.setInt(1, Integer.parseInt(selectedQuery));
            }

            ResultSet rs = pst.executeQuery();

            List<paymentsData> paymentsDataList = new ArrayList<>();
            while (rs.next()) {
                paymentsData paymentsData = new paymentsData(
                        rs.getString("customerNumber"),
                        rs.getString("checkNumber"),
                        rs.getString("paymentDate"),
                        rs.getString("amount")
                );
                paymentsDataList.add(paymentsData);
            }

            paymentsTableModel tableModel = new paymentsTableModel(paymentsDataList);
            table.setModel(tableModel);  // Update the table model
        } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
	        ex.printStackTrace();
	    }
	}

    /**
   	 * Searches based on the number entered in the search field.
   	 */
    private void searchpayments() {
        try {
            String checkNumber = txtsearchcheckNumber.getText();
            String query = "SELECT customerNumber, checkNumber, paymentDate, amount FROM payments";
            
            if (checkNumber != null && !checkNumber.isEmpty()) {
                query += " WHERE checkNumber = ?";
            }

            try (PreparedStatement pst = dbConnection.getConnection().prepareStatement(query)) {
                if (checkNumber != null && !checkNumber.isEmpty()) {
                    pst.setString(1, checkNumber);
                }

                ResultSet rs = pst.executeQuery();

                List<paymentsData> paymentsDataList = new ArrayList<>();
                while (rs.next()) {
                    paymentsData paymentsData = new paymentsData(
                            rs.getString("customerNumber"),
                            rs.getString("checkNumber"),
                            rs.getString("paymentDate"),
                            rs.getString("amount")
                    );
                    paymentsDataList.add(paymentsData);
                }

                paymentsTableModel tableModel = new paymentsTableModel(paymentsDataList);
                table.setModel(tableModel);
            }
        } catch (SQLException e) {
	        // Handle SQL exception in a user-friendly way
	        JOptionPane.showMessageDialog(frame, "Error searching payment: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
	        e.printStackTrace();
	    }
	}
     
    /**
	 * Opens a new window for adding or updating information.
	 */
    private void openAddpaymentsWindow() {
        // Create and set up the window.
        final JFrame addpaymentsFrame = new JFrame("Add New payments");
        addpaymentsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addpaymentsFrame.getContentPane().setLayout(new BorderLayout());
        addpaymentsFrame.setSize(700, 700);
     // Create a panel for the registration section
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Registration", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(20, 40, 504, 359);
        panel.setLayout(null);
        addpaymentsFrame.getContentPane().add(panel);
     // Create a panel for the search section 
        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(new TitledBorder(null, "Search payments", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        searchPanel.setBounds(20, 372, 404, 62);
        searchPanel.setLayout(null);
        addpaymentsFrame.getContentPane().add(searchPanel);

        // Create a text field for entering search criteria
        final JTextField txtSearch = new JTextField();
        txtSearch.setBounds(20, 405, 180, 30); // Adjust the position and size as needed
        searchPanel.add(txtSearch);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchpayments(txtSearch.getText(), addpaymentsFrame); // Implement this method
            }
        });
        btnSearch.setBounds(190, 405, 180, 30); // Adjust the position and size as needed
        searchPanel.add(btnSearch);
     // Creates a label with a font and placement
        JLabel lblcustomerNumber = new JLabel("Customer Number");
        lblcustomerNumber.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblcustomerNumber.setBounds(10, 24, 168, 19);
        panel.add(lblcustomerNumber);

        JLabel lblcheckNumber = new JLabel("Check Number");
        lblcheckNumber.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblcheckNumber.setBounds(10, 54, 168, 20);
        panel.add(lblcheckNumber);

        JLabel lblpaymentDate = new JLabel("payments Date");
        lblpaymentDate.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblpaymentDate.setBounds(10, 85, 168, 20);
        panel.add(lblpaymentDate);
        
        JLabel lblamount = new JLabel("Amount");
        lblamount.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblamount.setBounds(10, 117, 168, 20);
        panel.add(lblamount);
        
        // Creates a txtfield with a font and placement
        txtcustomerNumber = new JTextField();
        txtcustomerNumber.setBounds(214, 24, 180, 20);
        panel.add(txtcustomerNumber);
        txtcustomerNumber.setColumns(10);

        txtcheckNumber = new JTextField();
        txtcheckNumber.setColumns(10);
        txtcheckNumber.setBounds(214, 53, 180, 20);
        panel.add(txtcheckNumber);

        txtpaymentDate = new JTextField();
        txtpaymentDate.setColumns(10);
        txtpaymentDate.setBounds(214, 84, 180, 20);
        panel.add(txtpaymentDate);
        
        txtamount = new JTextField();
        txtamount.setColumns(10);
        txtamount.setBounds(214, 116, 180, 20);
        panel.add(txtamount);
        

        JButton btnSave = new JButton("Save");
        btnSave.setBounds(10, 300, 180, 30);
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                savepayments(); // Make sure this method is for saving new payments records
            }
        });
        panel.add(btnSave);
        
        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(200, 300, 180, 30);
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updatepayments(); // Make sure this method is for updating existing payments records
            }
        });
        panel.add(btnUpdate);

        
        // Display the window.
        addpaymentsFrame.setVisible(true);
    }
    /**
     * Method to search for an payment based on the check number entered in the search field.
     * It performs a database search and updates the table to show the matching payments, if found.
     *
     * @param checkNumber The check number to search for in the database.
     */
    private void searchpayments(String checkNumber, JFrame frame) {
        try {
            // Prepare a SQL query to select payments based on the provided checkNumber
            PreparedStatement pst = dbConnection.getConnection().prepareStatement(
                "SELECT customerNumber, checkNumber, paymentDate, amount FROM payments WHERE checkNumber = ?");
            pst.setString(1, checkNumber); // Set the parameter for the query to the provided checkNumber
            
            ResultSet rs = pst.executeQuery(); // Execute the query and retrieve the result set
            
            if (rs.next()) { // Check if matching payment details were found in the result set
                // Retrieve the payment details from the result set
                String foundcustomerNumber = rs.getString(1);
                String foundcheckNumber = rs.getString(2);
                String foundpaymentDate = rs.getString(3);
                String foundamount = rs.getString(4);

                // Set the retrieved payment details in text fields or components on the GUI
                txtcustomerNumber.setText(foundcustomerNumber);
                txtcheckNumber.setText(foundcheckNumber);
                txtpaymentDate.setText(foundpaymentDate);
                txtamount.setText(foundamount);
            } else {
                // If no matching payment details were found, clear the text fields or components
                txtcustomerNumber.setText("");
                txtcheckNumber.setText("");
                txtpaymentDate.setText("");
                txtamount.setText("");
            }
        } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
	        ex.printStackTrace();
	    }
	}
    /**
     * Method to save payment data entered in the text fields to the database.
     * It captures data from UI components and inserts a new payment record into the database.
     */
    public void savepayments() {
    	try {
        // Create a map to store column names and their corresponding values for the new payment
        Map<String, Object> columnValues = new HashMap<>();
        columnValues.put("customerNumber", txtcustomerNumber.getText());
        columnValues.put("checkNumber", txtcheckNumber.getText());
        columnValues.put("paymentDate", txtpaymentDate.getText());
        columnValues.put("amount", txtamount.getText());
        
        // Call the insert method of your CRUD operations class to insert a new payment
        crudOperations.insert("payments", columnValues);
        
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
     * Method to update an existing payment data in the database.
     * It captures updated data from UI components and applies changes to the corresponding payment record.
     */
    public void updatepayments() {
    	try {
        // Create a map to store updated column values for the payment
        Map<String, Object> updatedValues = new HashMap<>();
        updatedValues.put("checkNumber", txtcheckNumber.getText());
        updatedValues.put("paymentDate", txtpaymentDate.getText());
        updatedValues.put("amount", txtamount.getText());
        
        // Define a where clause to identify the payment to be updated (in this case, by checkNumber)
        String whereClause = "checkNumber = ?";
        Object[] whereArgs = { txtsearchcheckNumber.getText() }; // Use the check number from the text field as the argument
        
        // Call the update method of your CRUD operations class to update the payment
        crudOperations.update("payments", updatedValues, whereClause, whereArgs);
        
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
        // Clear all input fields and set the focus back to txtcustomerNumber
        txtcustomerNumber.setText("");
        txtcheckNumber.setText("");
        txtpaymentDate.setText("");
        txtamount.setText("");
        txtcustomerNumber.requestFocus();
    }
    /**
     * Method to delete an payment record from the database.
     * It uses the check number from the search field to identify and delete the payment record.
     */
    public void deletepayments() {
        // Check if the txtsearchcheckNumber field is not null and not empty
        if (txtsearchcheckNumber != null && !txtsearchcheckNumber.getText().isEmpty()) {
            String whereClause = "checkNumber = ?";
            Object[] whereArgs = { txtsearchcheckNumber.getText() };

            // Call the delete method of your CRUD operations class to delete the payment record
            crudOperations.delete("payments", whereClause, whereArgs);

            // Reload the table data to reflect the changes
            tableLoad();

            // Clear the input fields after deletion
            clearFields();
        } else {
            // Optionally, show an error message if the field is null or empty
            JOptionPane.showMessageDialog(frame, "Check number is required for deletion.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Method to import payment data from a CSV file.
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
                String sql = "INSERT INTO payments (customerNumber, checkNumber, paymentDate, amount) VALUES (?, ?, ?, ?)";
                PreparedStatement statement = dbConnection.getConnection().prepareStatement(sql);

                while ((line = br.readLine()) != null) {
                    String[] values = line.split(","); // Check if your CSV uses a different delimiter
                    statement.setString(1, values[0]); // customerNumber
                    statement.setString(2, values[1]); // checkNumber
                    statement.setString(3, values[2]); // paymentDate
                    statement.setString(4, values[3]); // amount
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