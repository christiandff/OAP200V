package com.group11.classicmodels;
import javax.swing.table.AbstractTableModel;

import java.util.List;
/**
 * @author Christian Douglas Farnes Fancy
 * @author Even Hjerpseth Unneberg
 */
//Define a custom table model for displaying paymentsData in a JTable
public class paymentsTableModel extends AbstractTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 // List to store 
	private final List<paymentsData> paymentsDataList;
	 // Array of column names for the table
    private final String[] columnNames = {"customerNumber", "checkNumber", "paymentDate", "amount"};
    // Constructor to initialize the table model with a list
    public paymentsTableModel(List<paymentsData> paymentsDataList) {
        this.paymentsDataList = paymentsDataList;
    }
    // Override method to get the number of rows in the table
    @Override
    public int getRowCount() {
        return paymentsDataList.size();
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
        paymentsData paymentsData = paymentsDataList.get(rowIndex);
        // Switch statement to determine which column to retrieve data from
        switch (columnIndex) {
            case 0:
                return paymentsData.getcustomerNumber();
            case 1:
                return paymentsData.getcheckNumber();
            case 2:
                return paymentsData.getpaymentDate();
            case 3:
                return paymentsData.getamount();
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