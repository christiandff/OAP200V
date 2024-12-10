package com.group11.classicmodels;
import javax.swing.table.AbstractTableModel;

import java.util.List;
/**
 * @author Christian Douglas Farnes Fancy
 * @author Even Hjerpseth Unneberg
 */
//Define a custom table model for displaying officeData in a JTable
public class officeTableModel extends AbstractTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 // List to store 
	private final List<officeData> officeDataList;
	 // Array of column names for the table
    private final String[] columnNames = {"officeCode", "city", "phone", "addressLine1", "addressLine2", "state", "country", "postalCode", "territory"};
    // Constructor to initialize the table model with a list
    public officeTableModel(List<officeData> officeDataList) {
        this.officeDataList = officeDataList;
    }
    // Override method to get the number of rows in the table
    @Override
    public int getRowCount() {
        return officeDataList.size();
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
        officeData officeData = officeDataList.get(rowIndex);
        // Switch statement to determine which column to retrieve data from
        switch (columnIndex) {
            case 0:
                return officeData.getofficeCode();
            case 1:
                return officeData.getcity();
            case 2:
                return officeData.getphone();
            case 3:
                return officeData.getaddressLine1();
            case 4:
                return officeData.getaddressLine2();
            case 5:
                return officeData.getstate();
            case 6:
                return officeData.getcountry();
            case 7:
                return officeData.getpostalCode();
            case 8:
                return officeData.getterritory();
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