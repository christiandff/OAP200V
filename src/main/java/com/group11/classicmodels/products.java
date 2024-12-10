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

public class products extends ApplicationMenu{
	private static final long serialVersionUID = 1L;
    private JFrame frame;
    private JTable table;
    private JTextField txtsearchproductCode;
    private DatabaseConnection dbConnection;
    private CRUD crudOperations;
    //
    // Various text fields for data input
    private JTextField txtproductCode;
    private JTextField txtproductName;
    private JTextField txtproductLine;
    private JTextField txtproductScale;
    private JTextField txtproductVendor;
    private JTextField txtproductDescription;
    private JTextField txtquantityInStock;
    private JTextField txtbuyPrice;
    private JTextField txtMSRP;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    products window = new products();
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
    public products() {
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
            PreparedStatement pst = dbConnection.getConnection().prepareStatement("select * from products");
            ResultSet rs = pst.executeQuery();
         // Create a list to hold data objects
            List<productData> productDataList = new ArrayList<>();
            while (rs.next()) {
            	// Retrieve data from the ResultSet and add it to the list
                productData productData = new productData(
                        rs.getString("productCode"),
                        rs.getString("productName"),
                        rs.getString("productLine"),
                        rs.getString("productScale"),
                        rs.getString("productVendor"),
                        rs.getString("productDescription"),
                        rs.getString("quantityInStock"),
                        rs.getString("buyPrice"),
                        rs.getString("MSRP")
                );
                productDataList.add(productData);
            }
            // Set the custom table model with the fetched data
            productTableModel tableModel = new productTableModel(productDataList);
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
    	String[] queryOptions = {"All products", "Motorcycles", "Classic Cars", "Trucks and Buses", "Vintage Cars", "Planes", "Ships", "Trains" }; // Defines the options for the query ComboBox
    	// Initialize the query ComboBox with the defined options, Set position and size, Add ComboBox to the frame
    	final JComboBox<String> queryComboBox = new JComboBox<>(queryOptions);
    	queryComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"All products", "Motorcycles", "Classic Cars", "Trucks and Buses", "Vintage Cars", "Planes", "Ships", "Trains"}));
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
        JLabel lblproducts = new JLabel("Products");
        lblproducts.setFont(new Font("Tahoma", Font.BOLD, 30));
        lblproducts.setHorizontalAlignment(SwingConstants.CENTER);
        lblproducts.setBounds(331, -2, 301, 44);
        frame.getContentPane().add(lblproducts);
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
        
        txtsearchproductCode = new JTextField();
        txtsearchproductCode.setColumns(10);
        txtsearchproductCode.setBounds(174, 30, 180, 20);
        panel_1.add(txtsearchproductCode);
        
        txtsearchproductCode.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchproduct(); // Call search method on key release
            }
        });


        JLabel lblsearchproductCode = new JLabel("products code");
        lblsearchproductCode.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblsearchproductCode.setBounds(9, 31, 154, 14);
        panel_1.add(lblsearchproductCode);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteproduct(); // Call deleteProduct method when clicked
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
        		openAddproductWindow();
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
    private void searchproduct() {
        try {
            String productCode = txtsearchproductCode.getText();
            String query = "SELECT productCode, productName, productLine, productScale, productVendor, productDescription, quantityInStock, buyPrice, MSRP FROM products";
            
            if (productCode != null && !productCode.isEmpty()) {
                query += " WHERE productCode = ?";
            }

            try (PreparedStatement pst = dbConnection.getConnection().prepareStatement(query)) {
                if (productCode != null && !productCode.isEmpty()) {
                    pst.setString(1, productCode);
                }

                ResultSet rs = pst.executeQuery();

                List<productData> productDataList = new ArrayList<>();
                while (rs.next()) {
                    productData productData = new productData(
                            rs.getString("productCode"),
                            rs.getString("productName"),
                            rs.getString("productLine"),
                            rs.getString("productScale"),
                            rs.getString("productVendor"),
                            rs.getString("productDescription"),
                            rs.getString("quantityInStock"),
                            rs.getString("buyPrice"),
                            rs.getString("MSRP")
                    );
                    productDataList.add(productData);
                }

                productTableModel tableModel = new productTableModel(productDataList);
                table.setModel(tableModel);  // Update the table model
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
            case "All products":
                customQuery = "SELECT productCode, productName, productLine, productScale, productVendor, productDescription, quantityInStock, buyPrice, MSRP FROM products";
                break;
            case "Motorcycles":
            case "Classic Cars":
            case "Trucks and Buses":
            case "Vintage Cars":
            case "Planes":
            case "Ships":
            case "Trains":
                customQuery = "SELECT productCode, productName, productLine, productScale, productVendor, productDescription, quantityInStock, buyPrice, MSRP FROM products WHERE productLine = ?";
                break;
            default:
                customQuery = ""; // Handle default case
        }

        try (PreparedStatement pst = dbConnection.getConnection().prepareStatement(customQuery)) {
            if (!selectedQuery.equals("All products")) {
                pst.setString(1, selectedQuery);
            }

            ResultSet rs = pst.executeQuery();

            List<productData> productDataList = new ArrayList<>();
            while (rs.next()) {
                productData productData = new productData(
                        rs.getString("productCode"),
                        rs.getString("productName"),
                        rs.getString("productLine"),
                        rs.getString("productScale"),
                        rs.getString("productVendor"),
                        rs.getString("productDescription"),
                        rs.getString("quantityInStock"),
                        rs.getString("buyPrice"),
                        rs.getString("MSRP")
                );
                productDataList.add(productData);
            }

            productTableModel tableModel = new productTableModel(productDataList);
            table.setModel(tableModel);
        } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
	        ex.printStackTrace();
	    }
	}
    /**
	 * Opens a new window for adding or updating information.
	 */
    private void openAddproductWindow() {
        // Create and set up the window.
        final JFrame addproductFrame = new JFrame("Add New product");
        addproductFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addproductFrame.getContentPane().setLayout(new BorderLayout());
        addproductFrame.setSize(700, 700);
     // Create a panel for the registration section
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Registration", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(20, 40, 504, 359);
        panel.setLayout(null);
        addproductFrame.getContentPane().add(panel);
     // Create a panel for the search section 
        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(new TitledBorder(null, "Search products", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        searchPanel.setBounds(20, 372, 404, 62);
        searchPanel.setLayout(null);
        addproductFrame.getContentPane().add(searchPanel);

        // Create a text field for entering search criteria
        final JTextField txtSearch = new JTextField();
        txtSearch.setBounds(20, 405, 180, 30); // Adjust the position and size as needed
        searchPanel.add(txtSearch);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchproduct(txtSearch.getText(), addproductFrame); // Implement this method
            }
        });
        btnSearch.setBounds(190, 405, 180, 30); // Adjust the position and size as needed
        searchPanel.add(btnSearch);
     // Creates a label with a font and placement
        JLabel lblproductCode = new JLabel("Product Code");
        lblproductCode.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblproductCode.setBounds(10, 24, 168, 19);
        panel.add(lblproductCode);

        JLabel lblproductName = new JLabel("Product Name");
        lblproductName.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblproductName.setBounds(10, 54, 168, 20);
        panel.add(lblproductName);

        JLabel lblproductLine = new JLabel("Product Line");
        lblproductLine.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblproductLine.setBounds(10, 85, 168, 20);
        panel.add(lblproductLine);
        
        JLabel lblproductScale = new JLabel("Product Scale");
        lblproductScale.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblproductScale.setBounds(10, 117, 168, 20);
        panel.add(lblproductScale);
        
        
        JLabel lblProductVendor = new JLabel("Product Vendor");
        lblProductVendor.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblProductVendor.setBounds(10, 148, 168, 20);
        panel.add(lblProductVendor);
        
        JLabel lblproductDescription = new JLabel("Product Description");
        lblproductDescription.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblproductDescription.setBounds(10, 179, 168, 20);
        panel.add(lblproductDescription);
        
        JLabel lblquantityInStock = new JLabel("Quantity In Stock");
        lblquantityInStock.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblquantityInStock.setBounds(10, 210, 168, 20);
        panel.add(lblquantityInStock);
        
        JLabel lblbuyPrice = new JLabel("Buy Price");
        lblbuyPrice.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblbuyPrice.setBounds(10, 239, 168, 20);
        panel.add(lblbuyPrice);
        
        JLabel lblMSRP = new JLabel("MSRP");
        lblMSRP.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblMSRP.setBounds(10, 270, 168, 20);
        panel.add(lblMSRP);
        // Creates a txtfield with a font and placement
        txtbuyPrice = new JTextField();
        txtbuyPrice.setColumns(10);
        txtbuyPrice.setBounds(214, 238, 180, 20);
        panel.add(txtbuyPrice);
        
        txtproductCode = new JTextField();
        txtproductCode.setBounds(214, 24, 180, 20);
        panel.add(txtproductCode);
        txtproductCode.setColumns(10);

        txtproductName = new JTextField();
        txtproductName.setColumns(10);
        txtproductName.setBounds(214, 53, 180, 20);
        panel.add(txtproductName);

        txtproductLine = new JTextField();
        txtproductLine.setColumns(10);
        txtproductLine.setBounds(214, 84, 180, 20);
        panel.add(txtproductLine);
        
        txtproductScale = new JTextField();
        txtproductScale.setColumns(10);
        txtproductScale.setBounds(214, 116, 180, 20);
        panel.add(txtproductScale);
        
        txtproductVendor = new JTextField();
        txtproductVendor.setColumns(10);
        txtproductVendor.setBounds(214, 147, 180, 20);
        panel.add(txtproductVendor);
        
        txtproductDescription = new JTextField();
        txtproductDescription.setColumns(10);
        txtproductDescription.setBounds(214, 178, 180, 20);
        panel.add(txtproductDescription);
        
        txtquantityInStock = new JTextField();
        txtquantityInStock.setColumns(10);
        txtquantityInStock.setBounds(214, 209, 180, 20);
        panel.add(txtquantityInStock);
        
        txtMSRP = new JTextField();
        txtMSRP.setColumns(10);
        txtMSRP.setBounds(214, 269, 180, 20);
        panel.add(txtMSRP);

        JButton btnSave = new JButton("Save");
        btnSave.setBounds(10, 300, 180, 30);
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveproduct(); // Make sure this method is for saving new product records
            }
        });
        panel.add(btnSave);
        
        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(200, 300, 180, 30);
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateproduct(); // Make sure this method is for updating existing product records
            }
        });
        panel.add(btnUpdate);


        // Display the window.
        addproductFrame.setVisible(true);
    }
    
    /**
     * Method to search for an product based on the product code entered in the search field.
     * It performs a database search and updates the table to show the matching products, if found.
     *
     * @param productCode The product code to search for in the database.
     */
    private void searchproduct(String productCode, JFrame frame) {
        try {
            PreparedStatement pst = dbConnection.getConnection().prepareStatement(
                "SELECT productCode, productName, productLine, productScale, productVendor, productDescription, quantityInStock, buyPrice, MSRP FROM products WHERE productCode = ?");
            pst.setString(1, productCode);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                // Retrieve product details from the result set
                String foundproductCode = rs.getString(1);
                String foundproductName = rs.getString(2);
                String foundproductLine = rs.getString(3);
                String foundproductScale = rs.getString(4);
                String foundproductVendor = rs.getString(5);
                String foundproductDescription = rs.getString(6);
                String foundquantityInStock = rs.getString(7);
                String foundbuyPrice = rs.getString(8);
                String foundMSRP = rs.getString(9);

                // Set the retrieved values to corresponding text fields
                txtproductCode.setText(foundproductCode);
                txtproductName.setText(foundproductName);
                txtproductLine.setText(foundproductLine);
                txtproductScale.setText(foundproductScale);
                txtproductVendor.setText(foundproductVendor);
                txtproductDescription.setText(foundproductDescription);
                txtquantityInStock.setText(foundquantityInStock);
                txtbuyPrice.setText(foundbuyPrice);
                txtMSRP.setText(foundMSRP);
            } else {
                // If no product found, clear all text fields
                txtproductCode.setText("");
                txtproductName.setText("");
                txtproductLine.setText("");
                txtproductScale.setText("");
                txtproductVendor.setText("");
                txtproductDescription.setText("");
                txtquantityInStock.setText("");
                txtbuyPrice.setText("");
                txtMSRP.setText("");
            }
        } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
	        ex.printStackTrace();
	    }
	}
    /**
     * Method to save product data entered in the text fields to the database.
     * It captures data from UI components and inserts a new product record into the database.
     */
    public void saveproduct() {
    	try {
        // Create a map to store column names and values for insertion
        Map<String, Object> columnValues = new HashMap<>();
        columnValues.put("productCode", txtproductCode.getText());
        columnValues.put("productName", txtproductName.getText());
        columnValues.put("productLine", txtproductLine.getText());
        columnValues.put("productScale", txtproductScale.getText());
        columnValues.put("productVendor", txtproductVendor.getText());
        columnValues.put("productDescription", txtproductDescription.getText());
        columnValues.put("quantityInStock", txtquantityInStock.getText());
        columnValues.put("buyPrice", txtbuyPrice.getText());
        columnValues.put("MSRP", txtMSRP.getText());

        // Call the insert method of your CRUD operations class to insert the product record
        crudOperations.insert("products", columnValues);

        // Reload the table data to reflect the changes
        tableLoad();

        // Clear the input fields after insertion
        clearFields();
    	} catch (Exception e) {
		    // Display an error message using JOptionPane
		    JOptionPane.showMessageDialog(frame, "Database operation failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		    e.printStackTrace(); // Optional, for debugging purposes
		}
    }
    /**
     * Method to update existing product data in the database.
     * It captures updated data from UI components and applies changes to the corresponding product record.
     */
    public void updateproduct() {
    	try {
        // Create a map to store updated column values
        Map<String, Object> updatedValues = new HashMap<>();
        updatedValues.put("productName", txtproductName.getText());
        updatedValues.put("productLine", txtproductLine.getText());
        updatedValues.put("productScale", txtproductScale.getText());
        updatedValues.put("productVendor", txtproductVendor.getText());
        updatedValues.put("productDescription", txtproductDescription.getText());
        updatedValues.put("quantityInStock", txtquantityInStock.getText());
        updatedValues.put("buyPrice", txtbuyPrice.getText());
        updatedValues.put("MSRP", txtMSRP.getText());

        // Define the WHERE clause and arguments for updating
        String whereClause = "productCode = ?";
        Object[] whereArgs = { txtproductCode.getText() };

        // Call the update method of your CRUD operations class to update the product record
        crudOperations.update("products", updatedValues, whereClause, whereArgs);

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
        // Clear all text fields and set focus on the productCode field
        txtproductCode.setText("");
        txtproductName.setText("");
        txtproductLine.setText("");
        txtproductScale.setText("");
        txtproductVendor.setText("");
        txtproductDescription.setText("");
        txtquantityInStock.setText("");
        txtbuyPrice.setText("");
        txtMSRP.setText("");
        txtproductCode.requestFocus();
    }
    /**
     * Method to delete an product record from the database.
     * It uses the product code from the search field to identify and delete the product record.
     */
    public void deleteproduct() {
        // Check if the txtsearchproductCode field is not null and not empty
        if (txtsearchproductCode != null && !txtsearchproductCode.getText().isEmpty()) {
            String whereClause = "productCode = ?";
            Object[] whereArgs = { txtsearchproductCode.getText() };

            // Call the delete method of your CRUD operations class to delete the product record
            crudOperations.delete("products", whereClause, whereArgs);

            // Reload the table data to reflect the changes
            tableLoad();

            // Clear the input fields after deletion
            clearFields();
        } else {
            // Optionally, show an error message if the field is null or empty
            JOptionPane.showMessageDialog(frame, "Productcode is required for deletion.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Method to import product data from a CSV file.
     * It reads data from the selected CSV file and inserts it into the database.
     */
    private void importCSV() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile));
                 PreparedStatement statement = dbConnection.getConnection().prepareStatement(
                    "INSERT INTO products (productCode, productName, productLine, productScale, productVendor, productDescription, quantityInStock, buyPrice, MSRP) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

                br.readLine(); // Skip the header row

                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");

                    if (values.length != 9) {
                        System.out.println("Skipping malformed line: " + line);
                        continue;
                    }

                    try {
                        // Set parameters for the prepared statement and execute the insertion
                        statement.setString(1, values[0].trim());
                        statement.setString(2, values[1].trim());
                        statement.setString(3, values[2].trim());
                        statement.setString(4, values[3].trim());
                        statement.setString(5, values[4].trim());
                        statement.setString(6, values[5].trim());
                        statement.setInt(7, Integer.parseInt(values[6].trim()));
                        statement.setDouble(8, Double.parseDouble(values[7].trim()));
                        statement.setDouble(9, Double.parseDouble(values[8].trim()));
                        statement.executeUpdate();
                    } catch (NumberFormatException e) {
                        // Handle any number format issues gracefully
                        System.out.println("Skipping line due to number format issue: " + line);
                    }
                }
                JOptionPane.showMessageDialog(frame, "Data Imported Successfully", "Info", JOptionPane.INFORMATION_MESSAGE);

                // Refresh your table view to reflect the imported data
                tableLoad();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }



}