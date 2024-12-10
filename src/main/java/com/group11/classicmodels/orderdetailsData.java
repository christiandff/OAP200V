package com.group11.classicmodels;
/**
 * @author Christian Douglas Farnes Fancy
 * @author Even Hjerpseth Unneberg
 */
public class orderdetailsData {
    private String orderNumber;
    private String productCode;
    private String quantityOrdered;
    private String priceEach;
    private String orderLineNumber;

    // Define a class representing customer data with various attributes
    public orderdetailsData(String orderNumber, String productCode, String quantityOrdered,
                        String priceEach, String orderLineNumber) {
    	// Initialize the object with provided customer data
        this.orderNumber = orderNumber;
        this.productCode = productCode;
        this.quantityOrdered = quantityOrdered;
        this.priceEach = priceEach;
        this.orderLineNumber = orderLineNumber;

    }

    // Add getters and setters as needed

    public String getorderNumber() {
        return orderNumber;
    }

    public String getproductCode() {
        return productCode;
    }

    public String getquantityOrdered() {
        return quantityOrdered;
    }

    public String getpriceEach() {
        return priceEach;
    }

    public String getorderLineNumber() {
        return orderLineNumber;
    }
}