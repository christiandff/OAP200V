package com.group11.classicmodels;
import javax.swing.table.AbstractTableModel;

import java.util.List;
/**
 * @author Christian Douglas Farnes Fancy
 * @author Even Hjerpseth Unneberg
 */
//Define a custom table model for displaying productsData in a JTable
public class productTableModel extends AbstractTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 // List to store 
	private final List<productData> productDataList;
	 // Array of column names for the table
    private final String[] columnNames = {"ProductCode", "productName", "productLine", "productScale", "productVendor", "productDescription", "quantityInStock", "buyPrice", "MSRP"};
    // Constructor to initialize the table model with a list
    public productTableModel(List<productData> productDataList) {
        this.productDataList = productDataList;
    }
    // Override method to get the number of rows in the table
    @Override
    public int getRowCount() {
        return productDataList.size();
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
        productData productData = productDataList.get(rowIndex);
        // Switch statement to determine which column to retrieve data from
        switch (columnIndex) {
            case 0:
                return productData.getproductCode();
            case 1:
                return productData.getproductName();
            case 2:
                return productData.getproductLine();
            case 3:
                return productData.getproductScale();
            case 4:
                return productData.getproductVendor();
            case 5:
                return productData.getproductDescription();
            case 6:
                return productData.getquantityInStock();
            case 7:
                return productData.getbuyPrice();
            case 8:
                return productData.getMSRP();
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