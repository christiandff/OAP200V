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

public class productLines extends ApplicationMenu{
	private static final long serialVersionUID = 1L;
    private JFrame frame;
    private JTable table;
    private JTextField txtsearchproductLine;
    private DatabaseConnection dbConnection;
    private CRUD crudOperations;
    //
    // Various text fields for data input
    private JTextField txtproductLine;
    private JTextField txttextDescription;
    private JTextField txthtmlDescription;
    private JTextField txtimage;
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    productLines window = new productLines();
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
    public productLines() {
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
            PreparedStatement pst = dbConnection.getConnection().prepareStatement("select * from productLines");
            ResultSet rs = pst.executeQuery();
         // Create a list to hold data objects
            List<productLinesData> productLinesDataList = new ArrayList<>();
            while (rs.next()) {
            	// Retrieve data from the ResultSet and add it to the list
                productLinesData productLinesData = new productLinesData(
                        rs.getString("productLine"),
                        rs.getString("textDescription"),
                        rs.getString("htmlDescription"),
                        rs.getString("image")
                );
                productLinesDataList.add(productLinesData);
            }
         // Set the custom table model with the fetched data
            productLinesTableModel tableModel = new productLinesTableModel(productLinesDataList);
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
        frame.setBounds(100, 100, 1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // Initialize and configure the label for details
        JLabel lblproductLines = new JLabel("ProductLine");
        lblproductLines.setFont(new Font("Tahoma", Font.BOLD, 30));
        lblproductLines.setHorizontalAlignment(SwingConstants.CENTER);
        lblproductLines.setBounds(284, -6, 356, 44);
        frame.getContentPane().add(lblproductLines);
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
        
        txtsearchproductLine = new JTextField();
        txtsearchproductLine.setColumns(10);
        txtsearchproductLine.setBounds(174, 30, 180, 20);
        panel_1.add(txtsearchproductLine);
        
        txtsearchproductLine.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchproductLines(); // Call search method on key release
            }
        });

    
        JLabel lblsearchproductLine = new JLabel("productLines");
        lblsearchproductLine.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblsearchproductLine.setBounds(9, 31, 154, 14);
        panel_1.add(lblsearchproductLine);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteproductLines(); // Call deleteProduct method when clicked
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
        		openAddproductLinesWindow();
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
    private void searchproductLines() {
        try {
            String productLine = txtsearchproductLine.getText();
            String query = "SELECT productLine, textDescription, htmlDescription, image FROM productLines";
            
            if (productLine != null && !productLine.isEmpty()) {
                query += " WHERE productLine = ?";
            }

            try (PreparedStatement pst = dbConnection.getConnection().prepareStatement(query)) {
                if (productLine != null && !productLine.isEmpty()) {
                    pst.setString(1, productLine);
                }

                ResultSet rs = pst.executeQuery();

                List<productLinesData> productLinesDataList = new ArrayList<>();
                while (rs.next()) {
                    productLinesData productLinesData = new productLinesData(
                            rs.getString("productLine"),
                            rs.getString("textDescription"),
                            rs.getString("htmlDescription"),
                            rs.getString("image")
                    );
                    productLinesDataList.add(productLinesData);
                }

                productLinesTableModel tableModel = new productLinesTableModel(productLinesDataList);
                table.setModel(tableModel);  // Update the table model
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
    private void openAddproductLinesWindow() {
        // Create and set up the window.
        final JFrame addproductLinesFrame = new JFrame("Add New productLines");
        addproductLinesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addproductLinesFrame.getContentPane().setLayout(new BorderLayout());
        addproductLinesFrame.setSize(700, 700);
     // Create a panel for the registration section
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Registration", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(20, 40, 504, 359);
        panel.setLayout(null);
        addproductLinesFrame.getContentPane().add(panel);
     // Create a panel for the search section 
        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(new TitledBorder(null, "Search productLines", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        searchPanel.setBounds(20, 372, 404, 62);
        searchPanel.setLayout(null);
        addproductLinesFrame.getContentPane().add(searchPanel);

        // Create a text field for entering search criteria
        final JTextField txtSearch = new JTextField();
        txtSearch.setBounds(20, 405, 180, 30); // Adjust the position and size as needed
        searchPanel.add(txtSearch);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchproductLines(txtSearch.getText(), addproductLinesFrame); // Implement this method
            }
        });
        btnSearch.setBounds(190, 405, 180, 30); // Adjust the position and size as needed
        searchPanel.add(btnSearch);
     // Creates a label with a font and placement
        JLabel lblproductLine = new JLabel("productLines");
        lblproductLine.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblproductLine.setBounds(10, 24, 168, 19);
        panel.add(lblproductLine);

        JLabel lbltextDescription = new JLabel("Text Description");
        lbltextDescription.setFont(new Font("Tahoma", Font.BOLD, 14));
        lbltextDescription.setBounds(10, 54, 168, 20);
        panel.add(lbltextDescription);

        JLabel lblhtmlDescription = new JLabel("HTML Description");
        lblhtmlDescription.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblhtmlDescription.setBounds(10, 85, 168, 20);
        panel.add(lblhtmlDescription);
        
        JLabel lblimage = new JLabel("Image");
        lblimage.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblimage.setBounds(10, 117, 168, 20);
        panel.add(lblimage);
        
        // Creates a txtfield with a font and placement
        txtproductLine = new JTextField();
        txtproductLine.setBounds(214, 24, 180, 20);
        panel.add(txtproductLine);
        txtproductLine.setColumns(10);

        txttextDescription = new JTextField();
        txttextDescription.setColumns(10);
        txttextDescription.setBounds(214, 53, 180, 20);
        panel.add(txttextDescription);

        txthtmlDescription = new JTextField();
        txthtmlDescription.setColumns(10);
        txthtmlDescription.setBounds(214, 84, 180, 20);
        panel.add(txthtmlDescription);
        
        txtimage = new JTextField();
        txtimage.setColumns(10);
        txtimage.setBounds(214, 116, 180, 20);
        panel.add(txtimage);
        

        JButton btnSave = new JButton("Save");
        btnSave.setBounds(10, 300, 180, 30);
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveproductLines(); // Make sure this method is for saving new productLines records
            }
        });
        panel.add(btnSave);
        
        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(200, 300, 180, 30);
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateproductLines(); // Make sure this method is for updating existing productLines records
            }
        });
        panel.add(btnUpdate);


        // Display the window.
        addproductLinesFrame.setVisible(true);
    }
    /**
     * Method to search for an productline based on the product line entered in the search field.
     * It performs a database search and updates the table to show the matching productline, if found.
     *
     * @param productLine The product line text to search for in the database.
     */
    private void searchproductLines(String productLine, JFrame frame) {
        try {
            // Prepare a SQL query to select productLines based on the provided productLine
            PreparedStatement pst = dbConnection.getConnection().prepareStatement(
                "SELECT productLine, textDescription, htmlDescription, image FROM productLines WHERE productLine = ?");
            pst.setString(1, productLine); // Set the parameter for the query to the provided productLine
            
            ResultSet rs = pst.executeQuery(); // Execute the query and retrieve the result set
            
            if (rs.next()) { // Check if matching productLines details were found in the result set
                // Retrieve the productLines details from the result set
                String foundproductLine = rs.getString(1);
                String foundtextDescription = rs.getString(2);
                String foundhtmlDescription = rs.getString(3);
                String foundimage = rs.getString(4);

                // Set the retrieved productLines details in text fields or components on the GUI
                txtproductLine.setText(foundproductLine);
                txttextDescription.setText(foundtextDescription);
                txthtmlDescription.setText(foundhtmlDescription);
                txtimage.setText(foundimage);
            } else {
                // If no matching productLines details were found, clear the text fields or components
                txtproductLine.setText("");
                txttextDescription.setText("");
                txthtmlDescription.setText("");
                txtimage.setText("");
            }
        } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
	        ex.printStackTrace();
	    }
	}
    /**
     * Method to save product line data entered in the text fields to the database.
     * It captures data from UI components and inserts a new product line record into the database.
     */
    public void saveproductLines() {
    	try {
        // Create a map to store column names and their corresponding values for the new productLines
        Map<String, Object> columnValues = new HashMap<>();
        columnValues.put("productLine", txtproductLine.getText());
        columnValues.put("textDescription", txttextDescription.getText());
        columnValues.put("htmlDescription", txthtmlDescription.getText());
        columnValues.put("image", txtimage.getText());
        
        // Call the insert method of your CRUD operations class to insert a new productLines
        crudOperations.insert("productLines", columnValues);
        
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
     * Method to update an existing product line data in the database.
     * It captures updated data from UI components and applies changes to the corresponding product line record.
     */
    public void updateproductLines() {
    	try {
        // Create a map to store updated column values for the productLines
        Map<String, Object> updatedValues = new HashMap<>();
        updatedValues.put("textDescription", txttextDescription.getText());
        updatedValues.put("htmlDescription", txthtmlDescription.getText());
        updatedValues.put("image", txtimage.getText());
        
        // Define a where clause to identify the productLines to be updated (in this case, by productLine)
        String whereClause = "productLine = ?";
        Object[] whereArgs = { txtproductLine.getText() }; // Use the productLine from the text field as the argument
        
        // Call the update method of your CRUD operations class to update the productLines
        crudOperations.update("productLines", updatedValues, whereClause, whereArgs);
        
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
        // Clear all input fields and set the focus back to txtproductLine
        txtproductLine.setText("");
        txttextDescription.setText("");
        txthtmlDescription.setText("");
        txtimage.setText("");
        txtproductLine.requestFocus();
    }
    /**
     * Method to delete an product line record from the database.
     * It uses the productLines text from the search field to identify and delete the order record.
     */
    public void deleteproductLines() {
        // Check if the txtsearchproductLine field is not null and not empty
        if (txtsearchproductLine != null && !txtsearchproductLine.getText().isEmpty()) {
            String whereClause = "productLine = ?";
            Object[] whereArgs = { txtsearchproductLine.getText() };

            // Call the delete method of your CRUD operations class to delete the productLines record
            crudOperations.delete("productLines", whereClause, whereArgs);

            // Reload the table data to reflect the changes
            tableLoad();

            // Clear the input fields after deletion
            clearFields();
        } else {
            // Optionally, show an error message if the field is null or empty
            JOptionPane.showMessageDialog(frame, "Productline is required for deletion.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Method to import product line data from a CSV file.
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
                String sql = "INSERT INTO productLines (productLine, textDescription, htmlDescription, image) VALUES (?, ?, ?, ?)";
                PreparedStatement statement = dbConnection.getConnection().prepareStatement(sql);

                while ((line = br.readLine()) != null) {
                    String[] values = line.split(","); // Check if your CSV uses a different delimiter
                    statement.setString(1, values[0]); // productLine
                    statement.setString(2, values[1]); // textDescription
                    statement.setString(3, values[2]); // htmlDescription
                    statement.setString(4, values[3]); // image
                    statement.executeUpdate();
                }
                JOptionPane.showMessageDialog(frame, "Data Imported Successfully", "Info", JOptionPane.INFORMATION_MESSAGE);

                // Refresh your table view to reflect the newly imported data
                tableLoad();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }


    

}