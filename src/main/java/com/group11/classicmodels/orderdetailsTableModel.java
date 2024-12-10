package com.group11.classicmodels;
import javax.swing.table.AbstractTableModel;

import java.util.List;
/**
 * @author Christian Douglas Farnes Fancy
 * @author Even Hjerpseth Unneberg
 */
//Define a custom table model for displaying orderdetailsData in a JTable
public class orderdetailsTableModel extends AbstractTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 // List to store 
	private final List<orderdetailsData> orderdetailsDataList;
	 // Array of column names for the table
    private final String[] columnNames = {"orderNumber", "productCode", "quantityOrdered", "priceEach", "orderLineNumber"};
    // Constructor to initialize the table model with a list
    public orderdetailsTableModel(List<orderdetailsData> orderdetailsDataList) {
        this.orderdetailsDataList = orderdetailsDataList;
    }
    // Override method to get the number of rows in the table
    @Override
    public int getRowCount() {
        return orderdetailsDataList.size();
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
        orderdetailsData orderdetailsData = orderdetailsDataList.get(rowIndex);
        // Switch statement to determine which column to retrieve data from
        switch (columnIndex) {
            case 0:
                return orderdetailsData.getorderNumber();
            case 1:
                return orderdetailsData.getproductCode();
            case 2:
                return orderdetailsData.getquantityOrdered();
            case 3:
                return orderdetailsData.getpriceEach();
            case 4:
                return orderdetailsData.getorderLineNumber();
           
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