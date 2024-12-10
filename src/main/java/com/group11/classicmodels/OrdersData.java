package com.group11.classicmodels;
/**
 * @author Christian Douglas Farnes Fancy
 * @author Even Hjerpseth Unneberg
 */
public class OrdersData {
    private String orderNumber;
    private String orderDate;
    private String requiredDate;
    private String shippedDate;
    private String status;
    private String comments;
    private String customerNumber;

    // Define a class representing customer data with various attributes
    public OrdersData(String orderNumber, String orderDate, String requiredDate,
                        String shippedDate, String status, String comments,
                        String customerNumber) {
    	// Initialize the object with provided customer data
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.shippedDate = shippedDate;
        this.status = status;
        this.comments = comments;
        this.customerNumber = customerNumber;
    }

    // Add getters and setters as needed

    
    public String getorderNumber() {
        return orderNumber;
    }

    public String getorderDate() {
        return orderDate;
    }

    public String getrequiredDate() {
        return requiredDate;
    }

    public String getshippedDate() {
        return shippedDate;
    }

    public String getstatus() {
        return status;
    }

    public String getcomments() {
        return comments;
    }

    public String getcustomerNumber() {
        return customerNumber;
    }
}