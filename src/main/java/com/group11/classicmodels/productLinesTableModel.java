package com.group11.classicmodels;
import javax.swing.table.AbstractTableModel;

import java.util.List;
/**
 * @author Christian Douglas Farnes Fancy
 * @author Even Hjerpseth Unneberg
 */
//Define a custom table model for displaying productlinesData in a JTable
public class productLinesTableModel extends AbstractTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 // List to store 
	private final List<productLinesData> productLinesDataList;
	 // Array of column names for the table
    private final String[] columnNames = {"productLine", "textDescription", "htmlDescription", "image"};
    // Constructor to initialize the table model with a list
    public productLinesTableModel(List<productLinesData> productLinesDataList) {
        this.productLinesDataList = productLinesDataList;
    }
    // Override method to get the number of rows in the table
    @Override
    public int getRowCount() {
        return productLinesDataList.size();
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
        productLinesData productLinesData = productLinesDataList.get(rowIndex);
        // Switch statement to determine which column to retrieve data from
        switch (columnIndex) {
            case 0:
                return productLinesData.getproductLine();
            case 1:
                return productLinesData.gettextDescription();
            case 2:
                return productLinesData.gethtmlDescription();
            case 3:
                return productLinesData.getimage();
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