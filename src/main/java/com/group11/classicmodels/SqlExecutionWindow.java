package com.group11.classicmodels;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Christian Douglas Farnes Fancy 
 * @author Even Hjerpseth Unneberg
 */

public class SqlExecutionWindow extends JFrame {
	private static final long serialVersionUID = 1L;
    // Declare UI components
    private JTextArea sqlTextArea; // Changed to JTextArea
    private JButton executeButton;
    private DatabaseConnection dbConnection;

    public SqlExecutionWindow(DatabaseConnection dbConnection) {
        // Initialize database connection
        this.dbConnection = dbConnection;

        // Set up the JFrame
        setTitle("SQL Execution");
        setSize(400, 300); // Adjusted size to accommodate JTextArea
        setLayout(null);

        // Set up the SQL input text area
        sqlTextArea = new JTextArea();
        sqlTextArea.setBounds(10, 10, 360, 200); // Adjusted bounds for JTextArea
        JScrollPane scrollPane = new JScrollPane(sqlTextArea); // Adding a scroll pane
        scrollPane.setBounds(10, 10, 360, 200); // Same bounds as JTextArea
        add(scrollPane);

        // Set up the execute button
        executeButton = new JButton("Execute");
        executeButton.setBounds(150, 220, 100, 30); // Adjusted position
        add(executeButton);

        // Add an ActionListener to the execute button
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get SQL command from text area
                String sql = sqlTextArea.getText().trim().toLowerCase();

                // Determine the type of SQL command and execute it
                if (sql.startsWith("select")) {
                    // Execute a SELECT query
                    executeSelectQuery(sql);
                } else if (sql.startsWith("insert")) {
                    // Execute an INSERT query
                    executeInsertUpdateDeleteQuery(sql, "inserted");
                } else if (sql.startsWith("update")) {
                    // Execute an UPDATE query
                    executeInsertUpdateDeleteQuery(sql, "updated");
                } else if (sql.startsWith("delete")) {
                    // Execute a DELETE query
                    executeInsertUpdateDeleteQuery(sql, "deleted");
                } else {
                    // Show error message if SQL command is invalid
                    JOptionPane.showMessageDialog(SqlExecutionWindow.this, "Invalid SQL command.");
                }
            }
        });
    }

    private void executeSelectQuery(String sql) {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            // Process the ResultSet here
            // You can display the results in a dialog or a new window

        } catch (SQLException ex) {
            // Show error message if SELECT query fails
            JOptionPane.showMessageDialog(this, "Error in SELECT query: " + ex.getMessage());
        }
    }

    private void executeInsertUpdateDeleteQuery(String sql, String operation) {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            // Execute the query and get the number of affected rows
            int affectedRows = pst.executeUpdate();
            // Show a confirmation message with the number of rows affected
            JOptionPane.showMessageDialog(this, affectedRows + " rows " + operation + ".");
        } catch (SQLException ex) {
            // Show error message if INSERT, UPDATE, or DELETE query fails
            JOptionPane.showMessageDialog(this, "Error in " + operation.toUpperCase() + " query: " + ex.getMessage());
        }
    }
}
