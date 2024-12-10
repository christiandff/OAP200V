package com.group11.classicmodels;
import javax.swing.table.AbstractTableModel;

import java.util.List;
/**
 * @author Christian Douglas Farnes Fancy
 * @author Even Hjerpseth Unneberg
 */
//Define a custom table model for displaying ordersData in a JTable
public class OrdersTableModel extends AbstractTableModel {
    /**
	 * @author Christian Douglas Farnes Fancy
	 */
	private static final long serialVersionUID = 1L;
	 // List to store 
	private final List<OrdersData> OrdersDataList;
	 // Array of column names for the table
    private final String[] columnNames = {"orderNumber", "orderDate", "requiredDate", "shippedDate", "status", "comments", "customerNumber"};
    // Constructor to initialize the table model with a list
    public OrdersTableModel(List<OrdersData> OrdersDataList) {
        this.OrdersDataList = OrdersDataList;
    }
    // Override method to get the number of rows in the table
    @Override
    public int getRowCount() {
        return OrdersDataList.size();
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
        OrdersData OrdersData = OrdersDataList.get(rowIndex);
        // Switch statement to determine which column to retrieve data from
        switch (columnIndex) {
            case 0:
                return OrdersData.getorderNumber();
            case 1:
                return OrdersData.getorderDate();
            case 2:
                return OrdersData.getrequiredDate();
            case 3:
                return OrdersData.getshippedDate();
            case 4:
                return OrdersData.getstatus();
            case 5:
                return OrdersData.getcomments();
            case 6:
                return OrdersData.getcustomerNumber();
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