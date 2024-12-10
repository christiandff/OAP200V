package com.group11.classicmodels;
/**
 * @author Christian Douglas Farnes Fancy
 * @author Even Hjerpseth Unneberg
 */
public class paymentsData {
    private String customerNumber;
    private String checkNumber;
    private String paymentDate;
    private String amount;

    // Define a class representing customer data with various attributes
    public paymentsData(String customerNumber, String checkNumber, String paymentDate,
                        String amount) {
    	// Initialize the object with provided customer data
        this.customerNumber = customerNumber;
        this.checkNumber = checkNumber;
        this.paymentDate = paymentDate;
        this.amount = amount;

    }

    // Add getters and setters as needed

    public String getcustomerNumber() {
        return customerNumber;
    }

    public String getcheckNumber() {
        return checkNumber;
    }

    public String getpaymentDate() {
        return paymentDate;
    }

    public String getamount() {
        return amount;
    }
}