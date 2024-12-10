package com.group11.classicmodels;
import javax.swing.table.AbstractTableModel;

import java.util.List;
/**
 * @author Christian Douglas Farnes Fancy
 * @author Even Hjerpseth Unneberg
 */
//Define a custom table model for displaying EmployeeData in a JTable
public class EmployeeTableModel extends AbstractTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 // List to store 
	private final List<EmployeeData> employeeDataList;
	 // Array of column names for the table
    private final String[] columnNames = {"EmployeeNumber", "FirstName", "LastName", "Extension", "Email", "OfficeCode", "ReportsTo", "JobTitle"};
    // Constructor to initialize the table model with a list
    public EmployeeTableModel(List<EmployeeData> employeeDataList) {
        this.employeeDataList = employeeDataList;
    }
    // Override method to get the number of rows in the table
    @Override
    public int getRowCount() {
        return employeeDataList.size();
    }
    // Override method to get the number of columns in the table
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    // Override method to get the value at a specific cell in the table
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
    	// Get the Data object at the specified row index
        EmployeeData employeeData = employeeDataList.get(rowIndex);
        // Switch statement to determine which column to retrieve data from
        switch (columnIndex) {
            case 0:
                return employeeData.getEmployeeNumber();
            case 1:
                return employeeData.getFirstName();
            case 2:
                return employeeData.getLastName();
            case 3:
                return employeeData.getExtension();
            case 4:
                return employeeData.getEmail();
            case 5:
                return employeeData.getOfficeCode();
            case 6:
                return employeeData.getReportsTo();
            case 7:
                return employeeData.getJobTitle();
            default:
                return null;
        }
    }
    // Override method to get the column name for a specific column index
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}