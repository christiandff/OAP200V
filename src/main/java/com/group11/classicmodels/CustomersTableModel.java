package com.group11.classicmodels;
import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * @author Christian Douglas Farnes Fancy
 * @author Even Hjerpseth Unneberg
 */
//Define a custom table model for displaying CustomersData in a JTable
public class CustomersTableModel extends AbstractTableModel {
 /**
  * Generated serial version ID to ensure compatibility during serialization.
  */
 private static final long serialVersionUID = 1L;

 // List to store CustomersData objects
 private final List<CustomersData> customersDataList;

 // Array of column names for the table
 private final String[] columnNames = {"customerNumber", "customerName", "contactLastName", "contactFirstName", "phone", "addressLine1", "addressLine2", "city", "state", "postalCode", "country", "salesRepcustomersNumber", "creditLimit"};

 // Constructor to initialize the table model with a list of CustomersData
 public CustomersTableModel(List<CustomersData> customersDataList) {
     this.customersDataList = customersDataList;
 }

 // Override method to get the number of rows in the table
 @Override
 public int getRowCount() {
     return customersDataList.size();
 }

 // Override method to get the number of columns in the table
 @Override
 public int getColumnCount() {
     return columnNames.length;
 }

 // Override method to get the value at a specific cell in the table
 @Override
 public Object getValueAt(int rowIndex, int columnIndex) {
     // Get the CustomersData object at the specified row index
     CustomersData customersData = customersDataList.get(rowIndex);

     // Switch statement to determine which column to retrieve data from
     switch (columnIndex) {
         case 0:
             return customersData.getcustomerNumber();
         case 1:
             return customersData.getcustomerName();
         case 2:
             return customersData.getcontactLastName();
         case 3:
             return customersData.getcontactFirstName();
         case 4:
             return customersData.getphone();
         case 5:
             return customersData.getaddressLine1();
         case 6:
             return customersData.getaddressLine2();
         case 7:
             return customersData.getcity();
         case 8:
             return customersData.getstate();
         case 9:
             return customersData.getpostalCode();
         case 10:
             return customersData.getcountry();
         case 11:
             return customersData.getsalesRepEmployeeNumber();
         case 12:
             return customersData.getcreditLimit();
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